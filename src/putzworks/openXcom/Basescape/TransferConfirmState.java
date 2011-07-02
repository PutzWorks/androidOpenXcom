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
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Savegame.Base;

public class TransferConfirmState extends State
{
	private TextButton _btnCancel, _btnOk;
	private Window _window;
	private Text _txtTitle, _txtCost, _txtTotal;
	private Base _base;
	private TransferItemsState _state;

/**
 * Initializes all the elements in the Confirm Transfer window.
 * @param game Pointer to the core game.
 * @param base Pointer to the destination base.
 * @param state Pointer to the Transfer state.
 */
public TransferConfirmState(Game game, Base base, TransferItemsState state) //unknown : State(game)
{
	super(game);
	_base = base; 
	_state = state;

	_screen = false;

	// Create objects
	_window = new Window(this, 320, 80, 0, 60);
	_btnCancel = new TextButton(128, 16, 176, 115);
	_btnOk = new TextButton(128, 16, 16, 115);
	_txtTitle = new Text(310, 16, 5, 75);
	_txtCost = new Text(60, 16, 110, 95);
	_txtTotal = new Text(100, 16, 170, 95);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(6)), Palette.backPos, 16);

	add(_window);
	add(_btnCancel);
	add(_btnOk);
	add(_txtTitle);
	add(_txtCost);
	add(_txtTotal);

	// Set up objects
	_window.setColor(Palette.blockOffset(13)+8);
	_window.setBackground(_game.getResourcePack().getSurface("BACK13.SCR"));

	_btnCancel.setColor(Palette.blockOffset(15)+9);
	_btnCancel.setText(_game.getLanguage().getString("STR_CANCEL_UC"));
	_btnCancel.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnCancelClick(action);
		}
	});

	_btnOk.setColor(Palette.blockOffset(15)+9);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnOkClick(action);
		}
	});

	_txtTitle.setColor(Palette.blockOffset(13)+10);
	_txtTitle.setBig();
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	String s = _game.getLanguage().getString("STR_TRANSFER_ITEMS_TO");
	s += _base.getName();
	_txtTitle.setText(s);

	_txtCost.setColor(Palette.blockOffset(13)+10);
	_txtCost.setBig();
	_txtCost.setText(_game.getLanguage().getString("STR_COST"));

	_txtTotal.setColor(Palette.blockOffset(15)+1);
	_txtTotal.setBig();
	_txtTotal.setText(Text.formatFunding(_state.getTotal()));
}

/**
 * Returns to the previous screen.
 * @param action Pointer to an action.
 */
public void btnCancelClick(Action action)
{
	_game.popState();
}

/**
 * Completes the transfer.
 * @param action Pointer to an action.
 */
public void btnOkClick(Action action)
{
	_state.completeTransfer();
	_game.popState();
	_game.popState();
	_game.popState();
}

}
