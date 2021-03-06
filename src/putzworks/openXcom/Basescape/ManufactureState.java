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

public class ManufactureState extends State
{
	private Base _base;
	private TextButton _btnNew, _btnOk;
	private Window _window;
	private Text _txtTitle, _txtAvailable, _txtAllocated, _txtSpace, _txtFunds, _txtItem, _txtEngineers, _txtProduced, _txtTotal, _txtCost, _txtTimeLeft;
	private TextList _lstManufacture;

/**
 * Initializes all the elements in the Manufacture screen.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 */
public ManufactureState(Game game, Base base)
{
	super(game);
	_base = base;
	// Create objects
	_window = new Window(this, 320, 200, 0, 0);
	_btnNew = new TextButton(148, 16, 8, 176);
	_btnOk = new TextButton(148, 16, 164, 176);
	_txtTitle = new Text(310, 16, 5, 8);
	_txtAvailable = new Text(150, 9, 8, 24);
	_txtAllocated = new Text(150, 9, 160, 24);
	_txtSpace = new Text(150, 9, 8, 34);
	_txtFunds = new Text(150, 9, 160, 34);
	_txtItem = new Text(80, 9, 10, 52);
	_txtEngineers = new Text(46, 18, 90, 44);
	_txtProduced = new Text(42, 18, 137, 44);
	_txtTotal = new Text(42, 18, 180, 44);
	_txtCost = new Text(42, 27, 223, 44);
	_txtTimeLeft = new Text(54, 18, 265, 44);
	_lstManufacture = new TextList(288, 100, 8, 80);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(6)), Palette.backPos, 16);

	add(_window);
	add(_btnNew);
	add(_btnOk);
	add(_txtTitle);
	add(_txtAvailable);
	add(_txtAllocated);
	add(_txtSpace);
	add(_txtFunds);
	add(_txtItem);
	add(_txtEngineers);
	add(_txtProduced);
	add(_txtTotal);
	add(_txtCost);
	add(_txtTimeLeft);
	add(_lstManufacture);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+9);
	_window.setBackground(_game.getResourcePack().getSurface("BACK17.SCR"));
	
	_btnNew.setColor(Palette.blockOffset(13)+13);
	_btnNew.setText(_game.getLanguage().getString("STR_NEW_PRODUCTION"));

	_btnOk.setColor(Palette.blockOffset(13)+13);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnOkClick(action);
		}
	});

	_txtTitle.setColor(Palette.blockOffset(15)+6);
	_txtTitle.setBig();
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setText(_game.getLanguage().getString("STR_CURRENT_PRODUCTION"));

	_txtAvailable.setColor(Palette.blockOffset(15)+6);
	_txtAvailable.setSecondaryColor(Palette.blockOffset(13));
	StringBuffer ss = new StringBuffer();
	ss.append(_game.getLanguage().getString("STR_ENGINEERS_AVAILABLE") + '\x01' + _base.getAvailableEngineers());
	_txtAvailable.setText(ss.toString());

	_txtAllocated.setColor(Palette.blockOffset(15)+6);
	_txtAllocated.setSecondaryColor(Palette.blockOffset(13));
	StringBuffer ss2 = new StringBuffer();
	ss2.append(_game.getLanguage().getString("STR_ENGINEERS_ALLOCATED") + '\x01' + (_base.getTotalEngineers() - _base.getAvailableEngineers()));
	_txtAllocated.setText(ss2.toString());

	_txtSpace.setColor(Palette.blockOffset(15)+6);
	_txtSpace.setSecondaryColor(Palette.blockOffset(13));
	_txtSpace.setText(_game.getLanguage().getString("STR_WORKSHOP_SPACE_AVAILABLE"));

	_txtFunds.setColor(Palette.blockOffset(15)+6);
	_txtFunds.setSecondaryColor(Palette.blockOffset(13));
	StringBuffer ss4 = new StringBuffer();
	ss4.append(_game.getLanguage().getString("STR_CURRENT_FUNDS") + '\x01' + Text.formatFunding(_game.getSavedGame().getFunds()));
	_txtFunds.setText(ss4.toString());

	_txtItem.setColor(Palette.blockOffset(15)+1);
	_txtItem.setText(_game.getLanguage().getString("STR_ITEM"));

	_txtEngineers.setColor(Palette.blockOffset(15)+1);
	_txtEngineers.setText(_game.getLanguage().getString("STR_ENGINEERS__ALLOCATED"));

	_txtProduced.setColor(Palette.blockOffset(15)+1);
	_txtProduced.setText(_game.getLanguage().getString("STR_UNITS_PRODUCED"));

	_txtTotal.setColor(Palette.blockOffset(15)+1);
	_txtTotal.setText(_game.getLanguage().getString("STR_TOTAL_TO_PRODUCE"));

	_txtCost.setColor(Palette.blockOffset(15)+1);
	_txtCost.setText(_game.getLanguage().getString("STR_COST__PER__UNIT"));

	_txtTimeLeft.setColor(Palette.blockOffset(15)+1);
	_txtTimeLeft.setText(_game.getLanguage().getString("STR_DAYS_HOURS_LEFT"));
	
	_lstManufacture.setColor(Palette.blockOffset(13)+10);
	_lstManufacture.setArrowColor(Palette.blockOffset(15)+9);
	_lstManufacture.setColumns(6, 105, 39, 45, 27, 47, 24);
	_lstManufacture.setSelectable(true);
	_lstManufacture.setBackground(_window);
	_lstManufacture.setMargin(1);
	_lstManufacture.addRow(6, "Laser Rifle", "30", "2", "8", "$40 000", "5/2");
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
