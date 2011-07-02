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
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Interface.Text;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Interface.TextButton;
import putzworks.openXcom.Interface.Window;
import putzworks.openXcom.Savegame.Craft;
import putzworks.openXcom.Savegame.Target;
import putzworks.openXcom.Savegame.Waypoint;

public class ConfirmDestinationState extends State
{
	private Craft _craft;
	private Target _target;
	private Window _window;
	private Text _txtTarget;
	private TextButton _btnOk, _btnCancel;

/**
 * Initializes all the elements in the Confirm Destination window.
 * @param game Pointer to the core game.
 * @param craft Pointer to the craft to retarget.
 * @param target Pointer to the selected target (NULL if it's just a point on the globe).
 */
public ConfirmDestinationState(Game game, Craft craft, Target target)
{
	super(game);
	_craft = craft;
	_target = target;
	Waypoint w = (Waypoint)(_target);
	_screen = false;

	// Create objects
	_window = new Window(this, 224, 72, 16, 64);
	_btnOk = new TextButton(50, 12, 68, 104);
	_btnCancel = new TextButton(50, 12, 138, 104);
	_txtTarget = new Text(214, 16, 21, 80);

	// Set palette
	if (w != null && w.getId() == 0)
	{
		_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(6)), Palette.backPos, 16);
	}
	else
	{
		_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(4)), Palette.backPos, 16);
	}
	
	add(_window);
	add(_btnOk);
	add(_btnCancel);
	add(_txtTarget);
	
	// Set up objects
	_window.setColor(Palette.blockOffset(15)+2);
	_window.setBackground(_game.getResourcePack().getSurface("BACK12.SCR"));

	_btnOk.setColor(Palette.blockOffset(8)+8);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnOkClick(action);
		}
	});

	_btnCancel.setColor(Palette.blockOffset(8)+8);
	_btnCancel.setText(_game.getLanguage().getString("STR_CANCEL_UC"));
	_btnCancel.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnCancelClick(action);
		}
	});

	_txtTarget.setColor(Palette.blockOffset(15)-1);
	_txtTarget.setBig();
	_txtTarget.setAlign(TextHAlign.ALIGN_CENTER);
	StringBuffer ss = new StringBuffer();
	if (w != null && w.getId() == 0)
	{
		ss.append(_game.getLanguage().getString("STR_TARGET_WAY_POINT"));
	}
	else
	{
		ss.append(_game.getLanguage().getString("STR_TARGET") + _target.getName(_game.getLanguage()));
	}
	_txtTarget.setText(ss.toString());
}

/**
 * Confirms the selected target for the craft.
 * @param action Pointer to an action.
 */
public void btnOkClick(Action action)
{
	Waypoint w = (Waypoint)(_target);
	if (w != null && w.getId() == 0)
	{
		w.setId(_game.getSavedGame().getWaypointId());
		(_game.getSavedGame().getWaypointId())++;
		_game.getSavedGame().getWaypoints().add(w);
	}
	_craft.setDestination(_target);
	_craft.setStatus("STR_OUT");
	_game.popState();
	_game.popState();
}

/**
 * Returns to the previous screen.
 * @param action Pointer to an action.
 */
public void btnCancelClick(Action action)
{
	Waypoint w = (Waypoint)(_target);
	if (w != null && w.getId() == 0)
	{
		w = null;
	}
	_game.popState();
}

}
