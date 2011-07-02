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
import putzworks.openXcom.Savegame.Base;

public class ConfirmNewBaseState extends State
{
	private Base _base;
	private Globe _globe;
	private Window _window;
	private Text _txtCost, _txtArea;
	private TextButton _btnOk, _btnCancel;
	private int _cost;

/**
 * Initializes all the elements in the Confirm New Base window.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to place.
 * @param globe Pointer to the Geoscape globe.
 */
public ConfirmNewBaseState(Game game, Base base, Globe globe)
{
	super(game);
	_base = base;
	_globe = globe;
	_cost = 0;
	_screen = false;

	// Create objects
	_window = new Window(this, 224, 72, 16, 64);
	_btnOk = new TextButton(54, 12, 68, 104);
	_btnCancel = new TextButton(54, 12, 138, 104);
	_txtCost = new Text(120, 9, 68, 80);
	_txtArea = new Text(120, 9, 68, 90);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(0)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_btnCancel);
	add(_txtCost);
	add(_txtArea);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+2);
	_window.setBackground(_game.getResourcePack().getSurface("BACK01.SCR"));

	_btnOk.setColor(Palette.blockOffset(15)+2);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnOkClick(action);
		}
	});

	_btnCancel.setColor(Palette.blockOffset(15)+2);
	_btnCancel.setText(_game.getLanguage().getString("STR_CANCEL_UC"));
	_btnCancel.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnCancelClick(action);
		}
	});

	StringBuffer ss = new StringBuffer();
	for (Region i: _game.getSavedGame().getRegions())
	{
		if ((i).getRules().insideRegion(_base.getLongitude(), _base.getLatitude()))
		{
			_cost = (i).getRules().getBaseCost();
			ss.append(_game.getLanguage().getString("STR_AREA_") << '\x01' << _game.getLanguage().getString((*i).getRules().getType()));
			break;
		}
	}

	String s = _game.getLanguage().getString("STR_COST_");
	s.erase(s.size()-1, 1);
	s += L'\x01' + Text.formatFunding(_cost);
	_txtCost.setColor(Palette.blockOffset(15)-1);
	_txtCost.setSecondaryColor(Palette.blockOffset(8)+10);
	_txtCost.setText(s);

	_txtArea.setColor(Palette.blockOffset(15)-1);
	_txtArea.setSecondaryColor(Palette.blockOffset(8)+10);
	_txtArea.setText(ss.str());
}

/**
 * Go to the Place Access Lift screen.
 * @param action Pointer to an action.
 */
public void btnOkClick(Action action)
{
	if (_game.getSavedGame().getFunds() >= _cost)
	{
		_game.getSavedGame().setFunds(_game.getSavedGame().getFunds() - _cost);
		_game.getSavedGame().getBases().add(_base);
		_game.pushState(new BaseNameState(_game, _base, _globe, false));
	}
	else
	{
		_game.pushState(new GeoscapeErrorState(_game, "STR_NOT_ENOUGH_MONEY"));
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
