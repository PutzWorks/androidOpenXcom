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
import putzworks.openXcom.Engine.InteractiveSurface;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Savegame.Craft;
import putzworks.openXcom.Savegame.Target;
import putzworks.openXcom.Savegame.Waypoint;

public class SelectDestinationState extends State
{
	Craft _craft;
	Globe _globe;
	InteractiveSurface _btnRotateLeft, _btnRotateRight, _btnRotateUp, _btnRotateDown, _btnZoomIn, _btnZoomOut;
	Window _window;
	Text _txtTitle;
	TextButton _btnCancel;

/**
 * Initializes all the elements in the Select Destination window.
 * @param game Pointer to the core game.
 * @param craft Pointer to the craft to target.
 * @param globe Pointer to the Geoscape globe.
 */
public SelectDestinationState(Game game, Craft craft, Globe globe)
{
	super(game);
	_craft = craft;
	_globe = globe;
	_screen = false;

	// Create objects
	_btnRotateLeft = new InteractiveSurface(12, 10, 259, 176);
	_btnRotateRight = new InteractiveSurface(12, 10, 283, 176);
	_btnRotateUp = new InteractiveSurface(13, 12, 271, 162);
	_btnRotateDown = new InteractiveSurface(13, 12, 271, 187);
	_btnZoomIn = new InteractiveSurface(23, 23, 295, 156);
	_btnZoomOut = new InteractiveSurface(13, 17, 300, 182);

	_window = new Window(this, 256, 28, 0, 0);
	_btnCancel = new TextButton(60, 12, 110, 8);
	_txtTitle = new Text(100, 9, 10, 10);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(0)), Palette.backPos, 16);
	
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
	_globe.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			globeClick(action);
		}
	});

	_btnRotateLeft.onMousePress(new ActionHandler() {
		public void handle(Action action) {
			btnRotateLeftPress(action);
		}
	});
	_btnRotateLeft.onMouseRelease(new ActionHandler() {
		public void handle(Action action) {
			btnRotateLeftRelease(action);
		}
	});

	_btnRotateRight.onMousePress(new ActionHandler() {
		public void handle(Action action) {
			btnRotateRightPress(action);
		}
	});
	_btnRotateRight.onMouseRelease(new ActionHandler() {
		public void handle(Action action) {
			btnRotateRightRelease(action);
		}
	});

	_btnRotateUp.onMousePress(new ActionHandler() {
		public void handle(Action action) {
			btnRotateUpPress(action);
		}
	});
	_btnRotateUp.onMouseRelease(new ActionHandler() {
		public void handle(Action action) {
			btnRotateUpRelease(action);
		}
	});

	_btnRotateDown.onMousePress(new ActionHandler() {
		public void handle(Action action) {
			btnRotateDownPress(action);
		}
	});
	_btnRotateDown.onMouseRelease(new ActionHandler() {
		public void handle(Action action) {
			btnRotateDownRelease(action);
		}
	});

	_btnZoomIn.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnZoomInClick(action);
		}
	});

	_btnZoomOut.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnZoomOutClick(action);
		}
	});

	_window.setColor(Palette.blockOffset(15)+2);
	_window.setBackground(_game.getResourcePack().getSurface("BACK01.SCR"));

	_btnCancel.setColor(Palette.blockOffset(8)+8);
	_btnCancel.setText(_game.getLanguage().getString("STR_CANCEL_UC"));
	_btnCancel.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnCancelClick(action);
		}
	});

	_txtTitle.setColor(Palette.blockOffset(15)-1);
	_txtTitle.setText(_game.getLanguage().getString("STR_SELECT_DESTINATION"));
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
 * Processes any left-clicks for picking a target,
 * or right-clicks to scroll the globe.
 * @param action Pointer to an action.
 */
public void globeClick(Action action)
{
	double lon, lat;
	int mouseX = (int)Math.floor(action.getXMouse() / action.getXScale()), mouseY = (int)floor(action.getYMouse() / action.getYScale());
	_globe.cartToPolar(mouseX, mouseY, lon, lat);
	
	// Ignore window clicks
	if (mouseY < 28)
	{
		return;
	}

	// Clicking on a valid target
	if (action.getDetails().button.button == SDL_BUTTON_LEFT)
	{
		Vector<Target> v = _globe.getTargets(mouseX, mouseY, true);
		if (v.size() == 0)
		{
			Waypoint w = new Waypoint();
			w.setLongitude(lon);
			w.setLatitude(lat);
			v.add(w);
		}
		_game.pushState(new MultipleTargetsState(_game, v, _craft, null));
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
	{
		_globe.zoomIn();
	}
	else if (action.getDetails().button.button == SDL_BUTTON_RIGHT)
	{
		_globe.zoomMax();
	}
}

/**
 * Zooms out of the globe.
 * @param action Pointer to an action.
 */
public void btnZoomOutClick(Action action)
{
	if (action.getDetails().button.button == SDL_BUTTON_LEFT)
	{
		_globe.zoomOut();
	}
	else if (action.getDetails().button.button == SDL_BUTTON_RIGHT)
	{
		_globe.zoomMin();
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

}
