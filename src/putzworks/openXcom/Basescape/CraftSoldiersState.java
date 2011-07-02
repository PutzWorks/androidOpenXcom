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

public class CraftSoldiersState extends State
{
	private TextButton _btnOk;
	private Window _window;
	private Text _txtTitle, _txtName, _txtRank, _txtCraft, _txtAvailable, _txtUsed;
	private TextList _lstSoldiers;

	private Base _base;
	private int _craft;


/**
 * Initializes all the elements in the Craft Soldiers screen.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 * @param craft ID of the selected craft.
 */
public CraftSoldiersState(Game game, Base base, int craft)
{
	super(game);
	_base = base; 
	_craft = craft;
	// Create objects
	_window = new Window(this, 320, 200, 0, 0);
	_btnOk = new TextButton(288, 16, 16, 176);
	_txtTitle = new Text(300, 16, 16, 7);
	_txtName = new Text(114, 9, 16, 32);
	_txtRank = new Text(102, 9, 130, 32);
	_txtCraft = new Text(82, 9, 232, 32);
	_txtAvailable = new Text(110, 9, 16, 24);
	_txtUsed = new Text(110, 9, 130, 24);
	_lstSoldiers = new TextList(288, 128, 8, 40);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(2)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_txtTitle);
	add(_txtName);
	add(_txtRank);
	add(_txtCraft);
	add(_txtAvailable);
	add(_txtUsed);
	add(_lstSoldiers);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+9);
	_window.setBackground(_game.getResourcePack().getSurface("BACK02.SCR"));

	_btnOk.setColor(Palette.blockOffset(13)+13);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnOkClick(action);
		}
	});

	_txtTitle.setColor(Palette.blockOffset(15)+6);
	_txtTitle.setBig();
	Craft c = _base.getCrafts().get(_craft);
	String s;
	s = _game.getLanguage().getString("STR_SELECT_SQUAD_FOR") + c.getName(_game.getLanguage());
	_txtTitle.setText(s);

	_txtName.setColor(Palette.blockOffset(15)+6);
	_txtName.setText(_game.getLanguage().getString("STR_NAME_UC"));

	_txtRank.setColor(Palette.blockOffset(15)+6);
	_txtRank.setText(_game.getLanguage().getString("STR_RANK"));

	_txtCraft.setColor(Palette.blockOffset(15)+6);
	_txtCraft.setText(_game.getLanguage().getString("STR_CRAFT"));

	_txtAvailable.setColor(Palette.blockOffset(15)+6);
	StringBuffer ss = new StringBuffer();
	ss.append(_game.getLanguage().getString("STR_SPACE_AVAILABLE") + (c.getRules().getSoldiers() - c.getNumSoldiers()));
	_txtAvailable.setText(ss.toString());

	_txtUsed.setColor(Palette.blockOffset(15)+6);
	StringBuffer ss2;
	ss2.append(_game.getLanguage().getString("STR_SPACE_USED") + c.getNumSoldiers());
	_txtUsed.setText(ss2.toString());

	_lstSoldiers.setColor(Palette.blockOffset(13)+10);
	_lstSoldiers.setArrowColor(Palette.blockOffset(15)+9);
	_lstSoldiers.setColumns(3, 114, 102, 64);
	_lstSoldiers.setSelectable(true);
	_lstSoldiers.setBackground(_window);
	_lstSoldiers.setMargin(8);
	_lstSoldiers.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			lstSoldiersClick(action);
		}
	});

	int row = 0;
	for (Soldier i: _base.getSoldiers())
	{
		String s;
		if ((i).getCraft() == null)
		{
			s = _game.getLanguage().getString("STR_NONE_UC");
		}
		else
		{
			s = (i).getCraft().getName(_game.getLanguage());
		}
		_lstSoldiers.addRow(3, (i).getName(), _game.getLanguage().getString((i).getRankString()), s);

		Uint8 color;
		if ((i).getCraft() == c)
		{
			color = Palette.blockOffset(13);
		}
		else if ((i).getCraft() != null)
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
 * Shows the selected soldier's info.
 * @param action Pointer to an action.
 */
public void lstSoldiersClick(Action action)
{
	int row = _lstSoldiers.getSelectedRow();
	Craft c = _base.getCrafts().get(_craft);
	Soldier s = _base.getSoldiers().get(_lstSoldiers.getSelectedRow());
	Uint8 color;
	if (s.getCraft() == c)
	{
		s.setCraft(null);
		_lstSoldiers.getCell(row, 2).setText(_game.getLanguage().getString("STR_NONE_UC"));
		color = Palette.blockOffset(13)+10;
	}
	else
	{
		s.setCraft(c);
		_lstSoldiers.getCell(row, 2).setText(c.getName(_game.getLanguage()));
		color = Palette.blockOffset(13);
	}
	_lstSoldiers.getCell(row, 0).setColor(color);
	_lstSoldiers.getCell(row, 1).setColor(color);
	_lstSoldiers.getCell(row, 2).setColor(color);
	_lstSoldiers.draw();

	StringBuffer ss = new StringBuffer();
	ss.append(_game.getLanguage().getString("STR_SPACE_AVAILABLE") + (c.getRules().getSoldiers() - c.getNumSoldiers()));
	_txtAvailable.setText(ss.toString());
	StringBuffer ss2 = new StringBuffer();
	ss2.append(_game.getLanguage().getString("STR_SPACE_USED") + c.getNumSoldiers());
	_txtUsed.setText(ss2.toString());
}

}
