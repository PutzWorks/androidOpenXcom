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

public class MonthlyReportState extends State
{
	private TextButton _btnOk;
	private Window _window;
	private Text _txtTitle, _txtMonth, _txtRating, _txtChange, _txtDesc;

/**
 * Initializes all the elements in the Monthly Report screen.
 * @param game Pointer to the core game.
 */
public MonthlyReportState(Game game)
{
	super(game);
	// Create objects
	_window = new Window(this, 320, 200, 0, 0);
	_btnOk = new TextButton(50, 12, 135, 180);
	_txtTitle = new Text(280, 16, 16, 8);
	_txtMonth = new Text(110, 8, 16, 24);
	_txtRating = new Text(180, 8, 125, 24);
	_txtChange = new Text(300, 8, 16, 32);
	_txtDesc = new Text(300, 140, 16, 40);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(3)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_txtTitle);
	add(_txtMonth);
	add(_txtRating);
	add(_txtChange);
	add(_txtDesc);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+2);
	_window.setBackground(_game.getResourcePack().getSurface("BACK13.SCR"));

	_btnOk.setColor(Palette.blockOffset(8)+13);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick((ActionHandler)MonthlyReportState.btnOkClick);

	_txtTitle.setColor(Palette.blockOffset(15)-1);
	_txtTitle.setBig();
	_txtTitle.setText(_game.getLanguage().getString("STR_XCOM_PROJECT_MONTHLY_REPORT"));
	
	int month = _game.getSavedGame().getTime().getMonth() - 1, year = _game.getSavedGame().getTime().getYear();
	if (month == 0)
	{
		month = 12;
		year--;
	}
	WStringstream ss;
	ss << _game.getLanguage().getString("STR_MONTH") << _game.getLanguage().getString((String)("STR_JAN" - 1 + month)) << " " << year;

	_txtMonth.setColor(Palette.blockOffset(15)-1);
	_txtMonth.setText(ss.str());

	_txtRating.setColor(Palette.blockOffset(15)-1);
	_txtRating.setText(_game.getLanguage().getString("STR_MONTHLY_RATING"));

	_txtChange.setColor(Palette.blockOffset(15)-1);
	_txtChange.setText(_game.getLanguage().getString("STR_FUNDING_CHANGE"));

	_txtDesc.setColor(Palette.blockOffset(8)+10);
	_txtDesc.setWordWrap(true);
	_txtDesc.setText(_game.getLanguage().getString("STR_COUNCIL_IS_GENERALLY_SATISFIED"));
}

/**
 * Resets the palette.
 */
public void init()
{
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(3)), Palette.backPos, 16);
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
