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
package putzworks.openXcom.Geoscape;

import java.util.Vector;

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Interface.TextButton;
import putzworks.openXcom.Interface.TextList;
import putzworks.openXcom.Interface.Window;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.Craft;
import putzworks.openXcom.Savegame.Target;
import putzworks.openXcom.Savegame.Ufo;
import putzworks.openXcom.Savegame.Waypoint;

public class MultipleTargetsState extends State
{
	private Vector<Target> _targets;
	private Craft _craft;
	private GeoscapeState _state;
	
	private Window _window;
	private TextButton _btnCancel;
	private TextList _lstTargets;

private final static int OUTER_MARGIN = 3;
private final static int INNER_MARGIN = 4;
private final static int BORDER = 5;
private final static int BUTTON_HEIGHT = 16;

/**
 * Initializes all the elements in the Multiple Targets window.
 * @param game Pointer to the core game.
 * @param targets List of targets to display.
 * @param craft Pointer to craft to retarget (NULL if none).
 * @param state Pointer to the Geoscape state.
 */
public MultipleTargetsState(Game game, Vector<Target> targets, Craft craft, GeoscapeState state)
{
	super(game);
	_targets = targets;
	_craft = craft;
	_state = state;
	_screen = false;

	if (_targets.size() > 1)
	{
		int listHeight = _targets.size() * 8;
		int winHeight = listHeight + BUTTON_HEIGHT + OUTER_MARGIN * 2 + INNER_MARGIN + BORDER * 2;
		int winY = (200 - winHeight) / 2;
		int listY = winY + BORDER + OUTER_MARGIN;
		int btnY = listY + listHeight + INNER_MARGIN;

		// Create objects
		_window = new Window(this, 136, winHeight, 60, winY);
		_btnCancel = new TextButton(116, BUTTON_HEIGHT, 70, btnY);
		_lstTargets = new TextList(116, listHeight, 70, listY);

		// Set palette
		_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(7)), Palette.backPos, 16);

		add(_window);
		add(_btnCancel);
		add(_lstTargets);

		// Set up objects
		_window.setColor(Palette.blockOffset(8)+8);
		_window.setBackground(_game.getResourcePack().getSurface("BACK15.SCR"));

		_btnCancel.setColor(Palette.blockOffset(8)+8);
		_btnCancel.setText(_game.getLanguage().getString("STR_CANCEL_UC"));
		_btnCancel.onMouseClick((ActionHandler)MultipleTargetsState.btnCancelClick);

		_lstTargets.setColor(Palette.blockOffset(8)+5);
		_lstTargets.setAlign(TextHAlign.ALIGN_CENTER);
		_lstTargets.setColumns(1, 116);
		_lstTargets.setSelectable(true);
		_lstTargets.setBackground(_window);
		_lstTargets.onMouseClick((ActionHandler)MultipleTargetsState.lstTargetsClick);
		for (Target i: _targets)
		{
			_lstTargets.addRow(1, (i).getName(_game.getLanguage()).c_str());
		}
	}
}

/**
 * Resets the palette and ignores the window
 * if there's only one target.
 */
public void init()
{
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(7)), Palette.backPos, 16);

	if (_targets.size() == 1)
	{
		popupTarget(_targets.begin());
	}
}

/**
 * Displays the right popup for a specific target.
 * @param target Pointer to target.
 */
public void popupTarget(Target target)
{
	_game.popState();
	if (_craft == null)
	{
		Base b = (Base)(target);
		Craft c = (Craft)(target);
		Ufo u = (Ufo)(target);
		Waypoint w = (Waypoint)(target);
		if (b != null)
		{
			_game.pushState(new InterceptState(_game, _state.getGlobe(), b));
		}
		else if (c != null)
		{
			_game.pushState(new GeoscapeCraftState(_game, c, _state.getGlobe(), 0));
		}
		else if (u != null)
		{
			_game.pushState(new UfoDetectedState(_game, u, _state, false));
		}
		else if (w != null)
		{
			_game.pushState(new TargetInfoState(_game, w));
		}
	}
	else
	{
		_game.pushState(new ConfirmDestinationState(_game, _craft, target));
	}
}

/**
 * Returns to the previous screen.
 * @param action Pointer to an action.
 */
public void btnCancelClick(Action action)
{
	_game.popState();
}

/**
 * Pick a target to display.
 * @param action Pointer to an action.
 */
public void lstTargetsClick(Action action)
{
	Target t = _targets[_lstTargets.getSelectedRow()];
	popupTarget(t);
}

}
