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
import putzworks.openXcom.Interface.TextButton;
import putzworks.openXcom.Interface.Window;
import putzworks.openXcom.Interface.Window.WindowPopup;
import putzworks.openXcom.Savegame.Craft;
import putzworks.openXcom.Savegame.Waypoint;

public class GeoscapeCraftState extends State
{
	private Craft _craft;
	private Globe _globe;
	private Waypoint _waypoint;

	private TextButton _btnBase, _btnTarget, _btnPatrol, _btnCancel;
	private Window _window;
	private Text _txtTitle, _txtStatus, _txtBase, _txtSpeed, _txtMaxSpeed, _txtAltitude, _txtFuel, _txtW1Name, _txtW1Ammo, _txtW2Name, _txtW2Ammo, _txtRedirect;

/**
 * Initializes all the elements in the Geoscape Craft window.
 * @param game Pointer to the core game.
 * @param craft Pointer to the craft to display.
 * @param globe Pointer to the Geoscape globe.
 * @param waypoint Pointer to the last UFO position (if redirecting the craft).
 */
public GeoscapeCraftState(Game game, Craft craft, Globe globe, Waypoint waypoint)
{
	super(game);
	_craft = craft;
	_globe = globe;
	_waypoint = waypoint;
	_screen = false;

	// Create objects
	_window = new Window(this, 240, 184, 8, 8, WindowPopup.POPUP_BOTH);
	if (_waypoint != null)
	{
		_btnBase = new TextButton(224, 12, 16, 132);
		_btnTarget = new TextButton(224, 12, 16, 164);
		_btnPatrol = new TextButton(224, 12, 16, 148);
	}
	else
	{
		_btnBase = new TextButton(192, 12, 32, 116);
		_btnTarget = new TextButton(192, 12, 32, 132);
		_btnPatrol = new TextButton(192, 12, 32, 148);
	}
	_btnCancel = new TextButton(192, 12, 32, 164);
	_txtTitle = new Text(190, 16, 32, 20);
	_txtStatus = new Text(190, 9, 32, 36);
	_txtBase = new Text(120, 9, 32, 52);
	_txtSpeed = new Text(120, 9, 32, 60);
	_txtMaxSpeed = new Text(120, 9, 32, 68);
	_txtAltitude = new Text(120, 9, 32, 76);
	_txtFuel = new Text(120, 9, 32, 84);
	_txtW1Name = new Text(120, 9, 32, 92);
	_txtW1Ammo = new Text(60, 9, 164, 92);
	_txtW2Name = new Text(120, 9, 32, 100);
	_txtW2Ammo = new Text(60, 9, 164, 100);
	_txtRedirect = new Text(230, 16, 13, 108);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(4)), Palette.backPos, 16);

	add(_window);
	add(_btnBase);
	add(_btnTarget);
	add(_btnPatrol);
	add(_btnCancel);
	add(_txtTitle);
	add(_txtStatus);
	add(_txtBase);
	add(_txtSpeed);
	add(_txtMaxSpeed);
	add(_txtAltitude);
	add(_txtFuel);
	add(_txtW1Name);
	add(_txtW1Ammo);
	add(_txtW2Name);
	add(_txtW2Ammo);
	add(_txtRedirect);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+2);
	_window.setBackground(_game.getResourcePack().getSurface("BACK12.SCR"));

	_btnBase.setColor(Palette.blockOffset(8)+8);
	_btnBase.setText(_game.getLanguage().getString("STR_RETURN_TO_BASE"));
	_btnBase.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnBaseClick(action);
		}
	});

	_btnTarget.setColor(Palette.blockOffset(8)+8);
	_btnTarget.setText(_game.getLanguage().getString("STR_SELECT_NEW_TARGET"));
	_btnTarget.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnTargetClick(action);
		}
	});

	_btnPatrol.setColor(Palette.blockOffset(8)+8);
	_btnPatrol.setText(_game.getLanguage().getString("STR_PATRO"));
	_btnPatrol.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnPatrolClick(action);
		}
	});

	_btnCancel.setColor(Palette.blockOffset(8)+8);
	_btnCancel.setText(_game.getLanguage().getString("STR_CANCEL_UC"));
	_btnCancel.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnCancelClick(action));
		}
	});

	_txtTitle.setColor(Palette.blockOffset(15)-1);
	_txtTitle.setBig();
	_txtTitle.setText(_craft.getName(_game.getLanguage()));

	_txtStatus.setColor(Palette.blockOffset(15)-1);
	_txtStatus.setSecondaryColor(Palette.blockOffset(8)+10);
	StringBuffer ss = new StringBuffer();
	ss.append(_game.getLanguage().getString("STR_STATUS_") + '\x01');
	if (_craft.getLowFuel())
	{
		ss.append(_game.getLanguage().getString("STR_LOW_FUEL_RETURNING_TO_BASE"));
	}
	else if (_craft.getDestination() == 0)
	{
		ss.append(_game.getLanguage().getString("STR_PATROLLING"));
	}
	else if (_craft.getDestination() == (Target)_craft.getBase())
	{
		ss.append(_game.getLanguage().getString("STR_RETURNING_TO_BASE"));
	}
	else
	{
		Ufo u = (Ufo)(_craft.getDestination());
		Waypoint w = (Waypoint)(_craft.getDestination());
		if (u != 0)
		{
			if (!u.isCrashed())
			{
				ss.append(_game.getLanguage().getString("STR_INTERCEPTING_UFO") + u.getId());
			}
			else
			{
				ss.append(_game.getLanguage().getString("STR_DESTINATION_CRASH_SITE") + u.getId());
			}
		}
		else if (w != 0)
		{
			ss.append(_game.getLanguage().getString("STR_DESTINATION_WAY_POINT") + w.getId());
		}
	}
	_txtStatus.setText(ss.toString());

	_txtBase.setColor(Palette.blockOffset(15)-1);
	_txtBase.setSecondaryColor(Palette.blockOffset(8)+5);
	StringBuffer ss2 = new StringBuffer();
	ss2.append(_game.getLanguage().getString("STR_BASE_UC_") + '\x01' + _craft.getBase().getName());
	_txtBase.setText(ss2.toString());

	_txtSpeed.setColor(Palette.blockOffset(15)-1);
	_txtSpeed.setSecondaryColor(Palette.blockOffset(8)+5);
	StringBuffer ss3 = new StringBuffer();
	ss3.append(_game.getLanguage().getString("STR_SPEED_") + '\x01' + _craft.getSpeed());
	_txtSpeed.setText(ss3.toString());

	_txtMaxSpeed.setColor(Palette.blockOffset(15)-1);
	_txtMaxSpeed.setSecondaryColor(Palette.blockOffset(8)+5);
	StringBuffer ss4 = new StringBuffer();
	ss4.append(_game.getLanguage().getString("STR_MAXIMUM_SPEED_UC") + '\x01' + _craft.getRules().getMaxSpeed());
	_txtMaxSpeed.setText(ss4.toString());

	_txtAltitude.setColor(Palette.blockOffset(15)-1);
	_txtAltitude.setSecondaryColor(Palette.blockOffset(8)+5);
	StringBuffer ss5 = new StringBuffer();
	ss5.append(_game.getLanguage().getString("STR_ALTITUDE_") + '\x01');
	_txtAltitude.setText(ss5.toString());

	_txtFuel.setColor(Palette.blockOffset(15)-1);
	_txtFuel.setSecondaryColor(Palette.blockOffset(8)+5);
	StringBuffer ss6 = new StringBuffer();
	ss6.append(_game.getLanguage().getString("STR_FUE") + '\x01' + _craft.getFuelPercentage() + "%");
	_txtFuel.setText(ss6.toString());

	_txtW1Name.setColor(Palette.blockOffset(15)-1);
	_txtW1Name.setSecondaryColor(Palette.blockOffset(8)+5);
	StringBuffer ss7 = new StringBuffer();
	ss7.append(_game.getLanguage().getString("STR_WEAPON_1") + '\x01');
	
	_txtW1Ammo.setColor(Palette.blockOffset(15)-1);
	_txtW1Ammo.setSecondaryColor(Palette.blockOffset(8)+5);
	StringBuffer ss8 = new StringBuffer();
	ss8.append(_game.getLanguage().getString("STR_ROUNDS_") + '\x01');

	if (_craft.getRules().getWeapons() > 0 && _craft.getWeapons().at(0) != 0)
	{
		CraftWeapon w1 = _craft.getWeapons().at(0);

		ss7.append(_game.getLanguage().getString(w1.getRules().getType()));
		_txtW1Name.setText(ss7.toString());
		
		ss8.append(w1.getAmmo());
		_txtW1Ammo.setText(ss8.toString());
	}
	else
	{
		ss7.append(_game.getLanguage().getString("STR_NONE_UC"));
		_txtW1Name.setText(ss7.toString());
		_txtW1Ammo.setVisible(false);
	}

	_txtW2Name.setColor(Palette.blockOffset(15)-1);
	_txtW2Name.setSecondaryColor(Palette.blockOffset(8)+5);
	StringBuffer ss9 = new StringBuffer();
	ss9.append(_game.getLanguage().getString("STR_WEAPON_2") + '\x01');
	
	_txtW2Ammo.setColor(Palette.blockOffset(15)-1);
	_txtW2Ammo.setSecondaryColor(Palette.blockOffset(8)+5);
	StringBuffer ss10 = new StringBuffer();
	ss10.append(_game.getLanguage().getString("STR_ROUNDS_") + '\x01');

	if (_craft.getRules().getWeapons() > 1 && _craft.getWeapons().at(1) != 0)
	{
		CraftWeapon w2 = _craft.getWeapons().at(1);

		ss9.append(_game.getLanguage().getString(w2.getRules().getType()));
		_txtW2Name.setText(ss9.toString());
		
		ss10.append(w2.getAmmo());
		_txtW2Ammo.setText(ss10.toString());
	}
	else
	{
		ss9.append(_game.getLanguage().getString("STR_NONE_UC"));
		_txtW2Name.setText(ss9.toString());
		_txtW2Ammo.setVisible(false);
	}

	_txtRedirect.setColor(Palette.blockOffset(15)-1);
	_txtRedirect.setBig();
	_txtRedirect.setAlign(TextHAlign.ALIGN_CENTER);
	_txtRedirect.setText(_game.getLanguage().getString("STR_REDIRECT_CRAFT"));

	if (_waypoint == 0)
	{
		_txtRedirect.setVisible(false);
	}
	else
	{
		_btnCancel.setVisible(false);
		_btnTarget.setText(_game.getLanguage().getString("STR_GO_TO_LAST_KNOWN_UFO_POSITION"));
	}

	if (_craft.getLowFuel())
	{
		_btnBase.setVisible(false);
		_btnTarget.setVisible(false);
		_btnPatrol.setVisible(false);
	}
}

/**
 * Resets the palette.
 */
public void init()
{
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(4)), Palette.backPos, 16);
}

/**
 * Returns the craft back to its base.
 * @param action Pointer to an action.
 */
public void btnBaseClick(Action action)
{
	_waypoint = null;
	_game.popState();
	_craft.returnToBase();
}

/**
 * Changes the craft's target.
 * @param action Pointer to an action.
 */
public void btnTargetClick(Action action)
{
	_game.popState();
	// Go to the last known UFO position
	if (_waypoint != null)
	{
		_waypoint.setId(_game.getSavedGame().getWaypointId());
		(_game.getSavedGame().getWaypointId())++;
		_game.getSavedGame().getWaypoints().add(_waypoint);
		_craft.setDestination(_waypoint);
	}
	// Select a new destination for the craft
	else
	{
		_game.pushState(new SelectDestinationState(_game, _craft, _globe));
	}
}

/**
 * Sets the craft to patrol the current location.
 * @param action Pointer to an action.
 */
public void btnPatrolClick(Action action)
{
	_waypoint = null;
	_game.popState();
	_craft.setDestination( null);
}

/**
 * Closes the window.
 * @param action Pointer to an action.
 */
public void btnCancelClick(Action action)
{
	_waypoint = null;
	_game.popState();
}

}
