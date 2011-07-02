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

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.Craft;
import putzworks.openXcom.Savegame.Soldier;

public class CraftArmorState extends State
{
	private TextButton _btnOk;
	private Window _window;
	private Text _txtTitle, _txtName, _txtCraft, _txtArmor;
	private TextList _lstSoldiers;

	private Base _base;
	private int _craft;


/**
 * Initializes all the elements in the Craft Armor screen.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 * @param craft ID of the selected craft.
 */
public CraftArmorState(Game game, Base base, int craft) //unknown  : State(game)
{
	super(game);
	_base = base;
	_craft = craft;

	// Create objects
	_window = new Window(this, 320, 200, 0, 0);
	_btnOk = new TextButton(288, 16, 16, 176);
	_txtTitle = new Text(300, 16, 16, 7);
	_txtName = new Text(114, 9, 16, 32);
	_txtCraft = new Text(70, 9, 130, 32);
	_txtArmor = new Text(100, 9, 200, 32);
	_lstSoldiers = new TextList(288, 128, 8, 40);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(4)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_txtTitle);
	add(_txtName);
	add(_txtCraft);
	add(_txtArmor);
	add(_lstSoldiers);

	// Set up objects
	_window.setColor(Palette.blockOffset(13)+13);
	_window.setBackground(_game.getResourcePack().getSurface("BACK14.SCR"));

	_btnOk.setColor(Palette.blockOffset(13)+13);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick((ActionHandler)CraftArmorState.btnOkClick);

	_txtTitle.setColor(Palette.blockOffset(13)+10);
	_txtTitle.setBig();
	_txtTitle.setText(_game.getLanguage().getString("STR_SELECT_ARMOR"));

	_txtName.setColor(Palette.blockOffset(13)+10);
	_txtName.setText(_game.getLanguage().getString("STR_NAME_UC"));

	_txtCraft.setColor(Palette.blockOffset(13)+10);
	_txtCraft.setText(_game.getLanguage().getString("STR_CRAFT"));

	_txtArmor.setColor(Palette.blockOffset(13)+10);
	_txtArmor.setText(_game.getLanguage().getString("STR_ARMOR"));

	_lstSoldiers.setColor(Palette.blockOffset(13)+10);
	_lstSoldiers.setArrowColor(Palette.blockOffset(13)+13);
	_lstSoldiers.setColumns(3, 114, 70, 96);
	_lstSoldiers.setSelectable(true);
	_lstSoldiers.setBackground(_window);
	_lstSoldiers.setMargin(8);
	_lstSoldiers.onMouseClick((ActionHandler)CraftArmorState.lstSoldiersClick);

	int row = 0;
	Craft c = _base.getCrafts().at(_craft);
	for (Soldier i: _base.getSoldiers())
	{
		WString s;
		if ((i).getCraft() == 0)
		{
			s = _game.getLanguage().getString("STR_NONE_UC");
		}
		else
		{
			s = (i).getCraft().getName(_game.getLanguage());
		}
		_lstSoldiers.addRow(3, (i).getName().c_str(), s.c_str(), _game.getLanguage().getString("STR_NONE_UC").c_str());

		Uint8 color;
		if ((i).getCraft() == c)
		{
			color = Palette.blockOffset(13);
		}
		else if ((i).getCraft() != 0)
		{
			color = Palette.blockOffset(15)+6;
		}
		else
		{
			color = Palette.blockOffset(13)+10;
		}
		_lstSoldiers.getCell(row, 0).setColor(color);
		_lstSoldiers.getCell(row, 1).setColor(color);
		_lstSoldiers.getCell(row, 2).setColor(color);
		row++;
	}
	_lstSoldiers.draw();
}

/**
 * Returns to the previous screen.
 * @param action Pointer to an action.
 */
public void btnOkClick(Action action)
{
	_game.popState();
}

/**
 * Shows the Select Armor window.
 * @param action Pointer to an action.
 */
public void lstSoldiersClick(Action action)
{
	_game.pushState(new SoldierArmorState(_game, _base, _lstSoldiers.getSelectedRow()));
}

}
