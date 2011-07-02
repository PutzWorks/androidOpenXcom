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

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.InteractiveSurface;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Interface.Text;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Interface.Text.TextVAlign;
import putzworks.openXcom.Interface.TextButton;
import putzworks.openXcom.Interface.Window;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.Craft;

public class BuildNewBaseState extends State
{
	private Base _base;
	private Globe _globe;
	private InteractiveSurface _btnRotateLeft, _btnRotateRight, _btnRotateUp, _btnRotateDown, _btnZoomIn, _btnZoomOut;
	private Window _window;
	private Text _txtTitle;
	private TextButton _btnCancel;
	private boolean _first;

/**
 * Initializes all the elements in the Build New Base window.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to place.
 * @param globe Pointer to the Geoscape globe.
 * @param first Is this the first base in the game?
 */
public BuildNewBaseState(Game game, Base base, Globe globe, boolean first)
{
	super(game);
	_base = base;
	_globe = globe;
	_first = first;
	_screen = false;

	// Create objects
	_btnRotateLeft = new InteractiveSurface(12, 10, 259, 176);
	_btnRotateRight = new InteractiveSurface(12, 10, 283, 176);
	_btnRotateUp = new InteractiveSurface(13, 12, 271, 162);
	_btnRotateDown = new InteractiveSurface(13, 12, 271, 187);
	_btnZoomIn = new InteractiveSurface(23, 23, 295, 156);
	_btnZoomOut = new InteractiveSurface(13, 17, 300, 182);

	_window = new Window(this, 256, 28, 0, 0);
	_btnCancel = new TextButton(54, 12, 186, 8);
	_txtTitle = new Text(200, 16, 8, 6);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("PALETTES.DAT_0").getColors());

	add(_btnRotateLeft);
	add(_btnRotateRight);
	add(_btnRotateUp);
	add(_btnRotateDown);
	add(_btnZoomIn);
	add(_btnZoomOut);

	add(_window);
	add(_btnCancel);
	add(_txtTitle);

	// Set up objects
	_globe.onMouseClick((ActionHandler)BuildNewBaseState.globeClick);

	_btnRotateLeft.onMousePress((ActionHandler)BuildNewBaseState.btnRotateLeftPress);
	_btnRotateLeft.onMouseRelease((ActionHandler)BuildNewBaseState.btnRotateLeftRelease);

	_btnRotateRight.onMousePress((ActionHandler)BuildNewBaseState.btnRotateRightPress);
	_btnRotateRight.onMouseRelease((ActionHandler)BuildNewBaseState.btnRotateRightRelease);

	_btnRotateUp.onMousePress((ActionHandler)BuildNewBaseState.btnRotateUpPress);
	_btnRotateUp.onMouseRelease((ActionHandler)BuildNewBaseState.btnRotateUpRelease);

	_btnRotateDown.onMousePress((ActionHandler)BuildNewBaseState.btnRotateDownPress);
	_btnRotateDown.onMouseRelease((ActionHandler)BuildNewBaseState.btnRotateDownRelease);

	_btnZoomIn.onMouseClick((ActionHandler)BuildNewBaseState.btnZoomInClick);

	_btnZoomOut.onMouseClick((ActionHandler)BuildNewBaseState.btnZoomOutClick);

	_window.setColor(Palette.blockOffset(15)+2);
	_window.setBackground(_game.getResourcePack().getSurface("BACK01.SCR"));

	_btnCancel.setColor(Palette.blockOffset(15)+2);
	_btnCancel.setText(_game.getLanguage().getString("STR_CANCEL_UC"));
	_btnCancel.onMouseClick((ActionHandler)BuildNewBaseState.btnCancelClick);

	_txtTitle.setColor(Palette.blockOffset(15)-1);
	_txtTitle.setText(_game.getLanguage().getString("STR_SELECT_SITE_FOR_NEW_BASE"));
	_txtTitle.setVerticalAlign(TextVAlign.ALIGN_MIDDLE);

	if (_first)
	{
		_btnCancel.setVisible(false);
	}
}

/**
 * Resets the palette since it's bound to change on other screens.
 */
public void init()
{
	_game.setPalette(_game.getResourcePack().getPalette("PALETTES.DAT_0").getColors());
}

/**
 * Runs the globe rotation timer.
 */
public void think()
{
	super.think();
	_globe.think();
}

/**
 * Handles the globe.
 * @param action Pointer to an action.
 */
public void handle(Action action)
{
	super.handle(action);
	_globe.handle(action, this);
}

/**
 * Processes any left-clicks for base placement,
 * or right-clicks to scroll the globe.
 * @param action Pointer to an action.
 */
public void globeClick(Action action)
{
	double lon, lat;
	int mouseX = (int)Math.floor(action.getXMouse() / action.getXScale()), mouseY = (int)Math.floor(action.getYMouse() / action.getYScale());
	_globe.cartToPolar(mouseX, mouseY, lon, lat);

	// Ignore window clicks
	if (mouseY < 28)
	{
		return;
	}

	// Clicking on land for a base location
	if (action.getDetails().button.button == SDL_BUTTON_LEFT)
	{
		if (_globe.insideLand(lon, lat))
		{
			_base.setLongitude(lon);
			_base.setLatitude(lat);
			for (Craft i: _base.getCrafts())
			{
				(i).setLongitude(lon);
				(i).setLatitude(lat);
			}
			if (_first)
			{
				_game.pushState(new BaseNameState(_game, _base, _globe, _first));
			}
			else
			{
				_game.pushState(new ConfirmNewBaseState(_game, _base, _globe));
			}
		}
	}
}

/**
 * Starts rotating the globe to the left.
 * @param action Pointer to an action.
 */
public void btnRotateLeftPress(Action action)
{
	_globe.rotateLeft();
}

/**
 * Stops rotating the globe to the left.
 * @param action Pointer to an action.
 */
public void btnRotateLeftRelease(Action action)
{
	_globe.rotateStop();
}

/**
 * Starts rotating the globe to the right.
 * @param action Pointer to an action.
 */
public void btnRotateRightPress(Action action)
{
	_globe.rotateRight();
}

/**
 * Stops rotating the globe to the right.
 * @param action Pointer to an action.
 */
public void btnRotateRightRelease(Action action)
{
	_globe.rotateStop();
}

/**
 * Starts rotating the globe upwards.
 * @param action Pointer to an action.
 */
public void btnRotateUpPress(Action action)
{
	_globe.rotateUp();
}

/**
 * Stops rotating the globe upwards.
 * @param action Pointer to an action.
 */
public void btnRotateUpRelease(Action action)
{
	_globe.rotateStop();
}

/**
 * Starts rotating the globe downwards.
 * @param action Pointer to an action.
 */
public void btnRotateDownPress(Action action)
{
	_globe.rotateDown();
}

/**
 * Stops rotating the globe downwards.
 * @param action Pointer to an action.
 */
public void btnRotateDownRelease(Action action)
{
	_globe.rotateStop();
}

/**
 * Zooms into the globe.
 * @param action Pointer to an action.
 */
public void btnZoomInClick(Action action)
{
	if (action.getDetails().button.button == SDL_BUTTON_LEFT)
		_globe.zoomIn();
	else if (action.getDetails().button.button == SDL_BUTTON_RIGHT)
		_globe.zoomMax();
}

/**
 * Zooms out of the globe.
 * @param action Pointer to an action.
 */
public void btnZoomOutClick(Action action)
{
	if (action.getDetails().button.button == SDL_BUTTON_LEFT)
		_globe.zoomOut();
	else if (action.getDetails().button.button == SDL_BUTTON_RIGHT)
		_globe.zoomMin();
}

/**
 * Returns to the previous screen.
 * @param action Pointer to an action.
 */
public void btnCancelClick(Action action)
{
	_base = null;
	_game.popState();
}

}
