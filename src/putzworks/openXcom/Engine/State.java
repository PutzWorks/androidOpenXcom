/*
 * Copyright 2010 OpenXcom Developers.
 *
 * This file is part of OpenXcom.
 *
 * OpenXcom is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenXcom is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenXcom.  If not, see <http://www.gnu.org/licenses/>.
 */
package putzworks.openXcom.Engine;

import java.util.Vector;
import putzworks.openXcom.Interface.*;

public class State
{
	protected Game _game;
	protected Vector<Surface> _surfaces;
	protected boolean _screen;

/**
 * Initializes a brand new state with no child elements.
 * By default states are full-screen.
 * @param game Pointer to the core game.
 */
public State(Game game)
{
	_game = game;
	_screen = true;
}

/**
 * Returns the set of surfaces currently attached to this state.
 * @return List of surfaces.
 */
public Vector<Surface> getSurfaces()
{
	return _surfaces;
}

/**
 * Adds a new child surface for the state to take care of,
 * giving it the game's display palette. Once associated,
 * the state handles all of the surface's behaviour
 * and management automatically.
 * @param surface Child surface.
 * @note Since visible elements can overlap one another,
 * they have to be added in ascending Z-Order to be blitted
 * correctly onto the screen.
 */
public void add(Surface surface)
{
	// Set palette
	surface.setPalette(_game.getScreen().getPalette());

	// Set default fonts
	Text t = (Text)(surface);
	TextButton tb = (TextButton)(surface);
	TextEdit te = (TextEdit)(surface);
	TextList tl = (TextList)(surface);
	if (t != null)
	{
		t.setFonts(_game.getResourcePack().getFont("BIGLETS.DAT"), _game.getResourcePack().getFont("SMALLSET.DAT"));
	}
	else if (tb != null)
	{
		tb.setFonts(_game.getResourcePack().getFont("BIGLETS.DAT"), _game.getResourcePack().getFont("SMALLSET.DAT"));
	}
	else if (te != null)
	{
		te.setFonts(_game.getResourcePack().getFont("BIGLETS.DAT"), _game.getResourcePack().getFont("SMALLSET.DAT"));
	}
	else if (tl != null)
	{
		tl.setFonts(_game.getResourcePack().getFont("BIGLETS.DAT"), _game.getResourcePack().getFont("SMALLSET.DAT"));
	}

	_surfaces.add(surface);
}

/**
 * Returns whether this is a full-screen state.
 * This is used to optimize the state machine since full-screen
 * states automatically cover the whole screen, (whether they
 * actually use it all or not) so states behind them can be
 * safely ignored since they'd be covered up.
 * @return True if it's a screen, False otherwise.
 */
public final boolean isScreen()
{
	return _screen;
}

/**
 * Initializes the state and its child elements. This is
 * used for settings that have to be reset everytime the
 * state is returned to focus (eg. palettes), so can't
 * just be put in the constructor (remember there's a stack
 * of states, so they can be created once while being
 * repeatedly switched back into focus).
 */
public void init()
{

}

/**
 * Runs any code the state needs to keep updating every
 * game cycle, like timers and other real-time elements.
 */
public void think()
{
	for (Surface i: _surfaces)
		(i).think();
}

/**
 * Takes care of any events from the core game engine,
 * and passes them on to its InteractiveSurface child elements.
 * @param action Pointer to an action.
 */
public void handle(Action action)
{
	for (Surface i: _surfaces)
	{
		InteractiveSurface j = (InteractiveSurface)(i);
		if (j != null)
			j.handle(action, this);
	}
}

/**
 * Blits all the visible Surface child elements onto the
 * display screen, by order of addition.
 */
public void blit()
{
	for (Surface i: _surfaces)
		(i).blit(_game.getScreen().getSurface());
}

}
