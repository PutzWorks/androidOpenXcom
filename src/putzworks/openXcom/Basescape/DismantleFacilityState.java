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
package putzworks.openXcom.Basescape;

import java.util.Vector;
import putzworks.openXcom.Engine.*;
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.BaseFacility;

public class DismantleFacilityState extends State
{
	private Base _base;
	private BaseFacility _fac;

	private TextButton _btnOk, _btnCancel;
	private Window _window;
	private Text _txtTitle, _txtFacility;

/**
 * Initializes all the elements in a Dismantle Facility window.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 * @param fac Pointer to the facility to dismantle.
 */
public DismantleFacilityState(Game game, Base base, BaseFacility fac)
{
	super(game);
	_base = base;
	_fac = fac;
	_screen = false;

	// Create objects
	_window = new Window(this, 152, 80, 20, 60);
	_btnOk = new TextButton(44, 16, 36, 115);
	_btnCancel = new TextButton(44, 16, 112, 115);
	_txtTitle = new Text(142, 9, 25, 75);
	_txtFacility = new Text(142, 9, 25, 85);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(6)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_btnCancel);
	add(_txtTitle);
	add(_txtFacility);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+4);
	_window.setBackground(_game.getResourcePack().getSurface("BACK13.SCR"));

	_btnOk.setColor(Palette.blockOffset(15)+9);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick((ActionHandler)DismantleFacilityState.btnOkClick);

	_btnCancel.setColor(Palette.blockOffset(15)+9);
	_btnCancel.setText(_game.getLanguage().getString("STR_CANCEL_UC"));
	_btnCancel.onMouseClick((ActionHandler)DismantleFacilityState.btnCancelClick);

	_txtTitle.setColor(Palette.blockOffset(13)+10);
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setText(_game.getLanguage().getString("STR_DISMANTLE"));

	_txtFacility.setColor(Palette.blockOffset(13)+10);
	_txtFacility.setAlign(TextHAlign.ALIGN_CENTER);
	_txtFacility.setText(_game.getLanguage().getString(_fac.getRules().getType()));
}

/**
 * Dismantles the facility and returns
 * to the previous screen.
 * @param action Pointer to an action.
 */
public void btnOkClick(Action action)
{
	if (!_fac.getRules().getLift())
	{
		for (BaseFacility i: _base.getFacilities())
		{
			if (i == _fac)
			{
				_base.getFacilities().erase(i);
				_fac = null;
				break;
			}
		}
	}
	// Remove whole base if it's the access lift
	else
	{
		for (Base i: _game.getSavedGame().getBases())
		{
			if (i == _base)
			{
				_game.getSavedGame().getBases().erase(i);
				_base = null;;
				break;
			}
		}
	}
	_game.popState();
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
