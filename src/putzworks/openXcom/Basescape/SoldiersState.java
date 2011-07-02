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
import putzworks.openXcom.Savegame.Soldier;

public class SoldiersState extends State
{
	private TextButton _btnOk;
	private Window _window;
	private Text _txtTitle, _txtName, _txtRank, _txtCraft;
	private TextList _lstSoldiers;
	private Base _base;

/**
 * Initializes all the elements in the Soldiers screen.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 */
public SoldiersState(Game game, Base base)
{
	super(game);
	_base = base;
	// Create objects
	_window = new Window(this, 320, 200, 0, 0);
	_btnOk = new TextButton(288, 16, 16, 176);
	_txtTitle = new Text(310, 16, 5, 8);
	_txtName = new Text(114, 9, 16, 32);
	_txtRank = new Text(102, 9, 130, 32);
	_txtCraft = new Text(82, 9, 232, 32);
	_lstSoldiers = new TextList(288, 128, 8, 40);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(2)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_txtTitle);
	add(_txtName);
	add(_txtRank);
	add(_txtCraft);
	add(_lstSoldiers);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+4);
	_window.setBackground(_game.getResourcePack().getSurface("BACK02.SCR"));

	_btnOk.setColor(Palette.blockOffset(13)+13);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick((ActionHandler)SoldiersState.btnOkClick);

	_txtTitle.setColor(Palette.blockOffset(13)+10);
	_txtTitle.setBig();
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setText(_game.getLanguage().getString("STR_SOLDIER_LIST"));

	_txtName.setColor(Palette.blockOffset(15)+1);
	_txtName.setText(_game.getLanguage().getString("STR_NAME_UC"));

	_txtRank.setColor(Palette.blockOffset(15)+1);
	_txtRank.setText(_game.getLanguage().getString("STR_RANK"));

	_txtCraft.setColor(Palette.blockOffset(15)+1);
	_txtCraft.setText(_game.getLanguage().getString("STR_CRAFT"));

	_lstSoldiers.setColor(Palette.blockOffset(13)+10);
	_lstSoldiers.setArrowColor(Palette.blockOffset(15)+4);
	_lstSoldiers.setColumns(3, 114, 102, 64);
	_lstSoldiers.setSelectable(true);
	_lstSoldiers.setBackground(_window);
	_lstSoldiers.setMargin(8);
	_lstSoldiers.onMouseClick((ActionHandler)SoldiersState.lstSoldiersClick);
}

/**
 * The soldier names can change
 * after going into other screens.
 */
public void init()
{
	int row = 0;
	_lstSoldiers.clearList();
	for (Vector<Soldier>.iterator i = _base.getSoldiers().begin(); i != _base.getSoldiers().end(); ++i)
	{
		WString s;
		if ((*i).getCraft() == 0)
		{
			s = _game.getLanguage().getString("STR_NONE_UC");
		}
		else
		{
			s = (*i).getCraft().getName(_game.getLanguage());
		}
		_lstSoldiers.addRow(3, (*i).getName().c_str(), _game.getLanguage().getString((*i).getRankString()).c_str(), s.c_str());
		if ((*i).getCraft() == 0)
		{
			_lstSoldiers.getCell(row, 0).setColor(Palette.blockOffset(15)+6);
			_lstSoldiers.getCell(row, 1).setColor(Palette.blockOffset(15)+6);
			_lstSoldiers.getCell(row, 2).setColor(Palette.blockOffset(15)+6);
		}
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
 * Shows the selected soldier's info.
 * @param action Pointer to an action.
 */
public void lstSoldiersClick(Action action)
{
	_game.pushState(new SoldierInfoState(_game, _base, _lstSoldiers.getSelectedRow()));
}

}
