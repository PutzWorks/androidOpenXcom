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

import putzworks.openXcom.Engine.*;
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Savegame.Base;

public class MonthlyCostsState extends State
{
	private Base _base;

	private TextButton _btnOk;
	private Window _window;
	private Text _txtTitle, _txtCost, _txtQuantity, _txtTotal, _txtRental, _txtSalaries, _txtIncome;
	private TextList _lstCrafts, _lstSalaries, _lstMaintenance, _lstTotal;

/**
 * Initializes all the elements in the Monthly Costs screen.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 */
public MonthlyCostsState(Game game, Base base)
{
	super(game);
	_base = base;

	// Create objects
	_window = new Window(this, 320, 200, 0, 0);
	_btnOk = new TextButton(300, 20, 10, 170);
	_txtTitle = new Text(310, 16, 5, 12);
	_txtCost = new Text(80, 9, 115, 32);
	_txtQuantity = new Text(55, 9, 195, 32);
	_txtTotal = new Text(60, 9, 250, 32);
	_txtRental = new Text(80, 9, 10, 48);
	_txtSalaries = new Text(80, 9, 10, 80);
	_txtIncome = new Text(100, 9, 10, 136);
	_lstCrafts = new TextList(300, 20, 10, 56);
	_lstSalaries = new TextList(300, 30, 10, 88);
	_lstMaintenance = new TextList(300, 9, 10, 120);
	_lstTotal = new TextList(100, 9, 205, 136);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(6)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_txtTitle);
	add(_txtCost);
	add(_txtQuantity);
	add(_txtTotal);
	add(_txtRental);
	add(_txtSalaries);
	add(_txtIncome);
	add(_lstCrafts);
	add(_lstSalaries);
	add(_lstMaintenance);
	add(_lstTotal);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+4);
	_window.setBackground(_game.getResourcePack().getSurface("BACK13.SCR"));

	_btnOk.setColor(Palette.blockOffset(15)+4);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnOkClick(action);
		}
	});

	_txtTitle.setColor(Palette.blockOffset(15)+1);
	_txtTitle.setBig();
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setText(_game.getLanguage().getString("STR_MONTHLY_COSTS"));
	
	_txtCost.setColor(Palette.blockOffset(15)+1);
	_txtCost.setText(_game.getLanguage().getString("STR_COST_PER_UNIT"));

	_txtQuantity.setColor(Palette.blockOffset(15)+1);
	_txtQuantity.setText(_game.getLanguage().getString("STR_QUANTITY"));

	_txtTotal.setColor(Palette.blockOffset(15)+1);
	_txtTotal.setText(_game.getLanguage().getString("STR_TOTA"));

	_txtRental.setColor(Palette.blockOffset(15)+1);
	_txtRental.setText(_game.getLanguage().getString("STR_CRAFT_RENTA"));

	_txtSalaries.setColor(Palette.blockOffset(15)+1);
	_txtSalaries.setText(_game.getLanguage().getString("STR_SALARIES"));

	_txtIncome.setColor(Palette.blockOffset(13)+10);
	StringBuffer ss = new StringBuffer();
	ss.append(_game.getLanguage().getString("STR_INCOME") + "=" + Text.formatFunding(_game.getSavedGame().getCountryFunding()));
	_txtIncome.setText(ss.toString());

	_lstCrafts.setColor(Palette.blockOffset(13)+10);
	_lstCrafts.setColumns(4, 125, 70, 45, 60);
	_lstCrafts.setDot(true);

	StringBuffer ss2;
	ss2.append(_base.getCraftCount("STR_SKYRANGER"));
	_lstCrafts.addRow(4, _game.getLanguage().getString("STR_SKYRANGER"), Text.formatFunding(_game.getRuleset().getCraft("STR_SKYRANGER").getCost()), ss2.toString(), Text.formatFunding(_base.getCraftCount("STR_SKYRANGER") * _game.getRuleset().getCraft("STR_SKYRANGER").getCost()));
	StringBuffer ss3;
	ss3.append(_base.getCraftCount("STR_INTERCEPTOR"));
	_lstCrafts.addRow(4, _game.getLanguage().getString("STR_INTERCEPTOR"), Text.formatFunding(_game.getRuleset().getCraft("STR_INTERCEPTOR").getCost()), ss3.toString(), Text.formatFunding(_base.getCraftCount("STR_INTERCEPTOR") * _game.getRuleset().getCraft("STR_INTERCEPTOR").getCost()));

	_lstSalaries.setColor(Palette.blockOffset(13)+10);
	_lstSalaries.setColumns(4, 125, 70, 45, 60);
	_lstSalaries.setDot(true);

	StringBuffer ss4 = new StringBuffer();
	ss4.append(_base.getSoldiers().size());
	_lstSalaries.addRow(4, _game.getLanguage().getString("STR_SOLDIERS"), Text.formatFunding(_game.getRuleset().getSoldierCost()), ss4.toString(), Text.formatFunding(_base.getSoldiers().size() * _game.getRuleset().getSoldierCost()));
	StringBuffer ss5;
	ss5.append(_base.getTotalEngineers());
	_lstSalaries.addRow(4, _game.getLanguage().getString("STR_ENGINEERS"), Text.formatFunding(_game.getRuleset().getEngineerCost()), ss5.toString(), Text.formatFunding(_base.getTotalEngineers() * _game.getRuleset().getEngineerCost()));
	StringBuffer ss6;
	ss6.append(_base.getTotalScientists());
	_lstSalaries.addRow(4, _game.getLanguage().getString("STR_SCIENTISTS"), Text.formatFunding(_game.getRuleset().getScientistCost()), ss6.toString(), Text.formatFunding(_base.getTotalScientists() * _game.getRuleset().getScientistCost()));

	_lstMaintenance.setColor(Palette.blockOffset(13)+10);
	_lstMaintenance.setColumns(2, 240, 60);
	_lstMaintenance.setDot(true);
	_lstMaintenance.addRow(2, _game.getLanguage().getString("STR_BASE_MAINTENANCE").c_str(), Text.formatFunding(_base.getFacilityMaintenance()).c_str());
	_lstMaintenance.getCell(0, 0).setColor(Palette.blockOffset(15)+1);
	_lstMaintenance.draw();

	_lstTotal.setColor(Palette.blockOffset(13));
	_lstTotal.setColumns(2, 45, 55);
	_lstTotal.setDot(true);
	_lstTotal.addRow(2, _game.getLanguage().getString("STR_TOTA"), Text.formatFunding(_base.getMonthlyMaintenace()));
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
