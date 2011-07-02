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
import putzworks.openXcom.Interface.TextList;
import putzworks.openXcom.Interface.Window;
import putzworks.openXcom.Savegame.Country;

public class FundingState extends State
{
	private TextButton _btnOk;
	private Window _window;
	private Text _txtTitle, _txtCountry, _txtFunding, _txtChange;
	private TextList _lstCountries;

/**
 * Initializes all the elements in the Funding screen.
 * @param game Pointer to the core game.
 */
public FundingState(Game game)
{
	super(game);
	// Create objects
	_window = new Window(this, 320, 200, 0, 0);
	_btnOk = new TextButton(50, 12, 135, 180);
	_txtTitle = new Text(280, 16, 32, 8);
	_txtCountry = new Text(100, 16, 32, 24);
	_txtFunding = new Text(120, 16, 140, 24);
	_txtChange = new Text(120, 16, 240, 24);
	_lstCountries = new TextList(280, 136, 32, 40);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(0)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_txtTitle);
	add(_txtCountry);
	add(_txtFunding);
	add(_txtChange);
	add(_lstCountries);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+2);
	_window.setBackground(_game.getResourcePack().getSurface("BACK13.SCR"));

	_btnOk.setColor(Palette.blockOffset(15)+2);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick((ActionHandler)FundingState.btnOkClick);

	_txtTitle.setColor(Palette.blockOffset(15)-1);
	_txtTitle.setBig();
	_txtTitle.setText(_game.getLanguage().getString("STR_INTERNATIONAL_RELATIONS"));

	_txtCountry.setColor(Palette.blockOffset(15)-1);
	_txtCountry.setBig();
	_txtCountry.setText(_game.getLanguage().getString("STR_COUNTRY"));

	_txtFunding.setColor(Palette.blockOffset(15)-1);
	_txtFunding.setBig();
	_txtFunding.setText(_game.getLanguage().getString("STR_FUNDING"));
	if (_game.getLanguage().getName() == L"ITALIANO")
	{
		_txtFunding.setX(125);
	}

	_txtChange.setColor(Palette.blockOffset(15)-1);
	_txtChange.setBig();
	_txtChange.setText(_game.getLanguage().getString("STR_CHANGE"));

	_lstCountries.setColor(Palette.blockOffset(15)-1);
	_lstCountries.setSecondaryColor(Palette.blockOffset(8)+10);
	_lstCountries.setColumns(3, 108, 100, 72);
	_lstCountries.setDot(true);
	for (Country i: _game.getSavedGame().getCountries())
	{
		WStringstream ss, ss2;
		ss << L'\x01' << Text.formatFunding((i).getFunding()) << L'\x01';
		ss2 << (*i).getChange();
		_lstCountries.addRow(3, _game.getLanguage().getString((i).getRules().getType()).c_str(), ss.str().c_str(), ss2.str().c_str());
	}
	_lstCountries.addRow(2, _game.getLanguage().getString("STR_TOTAL_UC").c_str(), Text.formatFunding(_game.getSavedGame().getCountryFunding()).c_str());
	_lstCountries.getCell(_game.getSavedGame().getCountries().size(), 0).setColor(Palette.blockOffset(8)+5);
	_lstCountries.getCell(_game.getSavedGame().getCountries().size(), 1).setColor(Palette.blockOffset(8)+5);
	_lstCountries.draw();
}

/**
 * Returns to the previous screen.
 * @param action Pointer to an action.
 */
public void btnOkClick(Action action)
{
	_game.popState();
}

}
