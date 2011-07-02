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

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Interface.Text;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Interface.TextButton;
import putzworks.openXcom.Interface.TextList;
import putzworks.openXcom.Interface.Window;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.Transfer;

public class TransfersState extends State
{
	private Base _base;

	private TextButton _btnOk;
	private Window _window;
	private Text _txtTitle, _txtItem, _txtQuantity, _txtArrivalTime;
	private TextList _lstTransfers;
	private boolean _screen;

/**
 * Initializes all the elements in the Transfers window.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 */
public TransfersState(Game game, Base base)
{
	super(game);
	_base = base;
	_screen = false;

	// Create objects
	_window = new Window(this, 288, 180, 16, 10, Window.WindowPopup.POPUP_BOTH);
	_btnOk = new TextButton(272, 16, 24, 166);
	_txtTitle = new Text(278, 16, 21, 18);
	_txtItem = new Text(114, 8, 26, 34);
	_txtQuantity = new Text(44, 8, 141, 34);
	_txtArrivalTime = new Text(112, 8, 186, 34);
	_lstTransfers = new TextList(256, 112, 24, 50);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(6)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_txtTitle);
	add(_txtItem);
	add(_txtQuantity);
	add(_txtArrivalTime);
	add(_lstTransfers);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+9);
	_window.setBackground(_game.getResourcePack().getSurface("BACK13.SCR"));

	_btnOk.setColor(Palette.blockOffset(15)+9);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnOkClick(action);
		}
	});

	_txtTitle.setColor(Palette.blockOffset(15)+6);
	_txtTitle.setBig();
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setText(_game.getLanguage().getString("STR_TRANSFERS"));

	_txtItem.setColor(Palette.blockOffset(15)+6);
	_txtItem.setText(_game.getLanguage().getString("STR_ITEM"));

	_txtQuantity.setColor(Palette.blockOffset(15)+6);
	_txtQuantity.setText(_game.getLanguage().getString("STR_QUANTITY_UC"));

	_txtArrivalTime.setColor(Palette.blockOffset(15)+6);
	_txtArrivalTime.setText(_game.getLanguage().getString("STR_ARRIVAL_TIME_HOURS"));

	_lstTransfers.setColor(Palette.blockOffset(13)+10);
	_lstTransfers.setArrowColor(Palette.blockOffset(15)+9);
	_lstTransfers.setColumns(3, 155, 55, 46);
	_lstTransfers.setSelectable(true);
	_lstTransfers.setBackground(_window);
	_lstTransfers.setMargin(2);

	for (Transfer i: _base.getTransfers())
	{
		StringBuffer ss = new StringBuffer(), ss2 = new StringBuffer();
		ss.append((i).getQuantity());
		ss2.append((i).getHours());
		_lstTransfers.addRow(3, (i).getName(_game.getLanguage()), ss.toString(), ss2.toString());
	}
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
