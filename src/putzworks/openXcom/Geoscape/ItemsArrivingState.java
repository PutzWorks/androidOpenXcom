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
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Interface.TextButton;
import putzworks.openXcom.Interface.TextList;
import putzworks.openXcom.Interface.Window;
import putzworks.openXcom.Interface.Window.WindowPopup;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.Transfer;

public class ItemsArrivingState extends State
{
	private GeoscapeState _state;
	private TextButton _btnOk, _btnOk5Secs;
	private Window _window;
	private Text _txtTitle, _txtItem, _txtQuantity, _txtDestination;
	private TextList _lstTransfers;

/**
 * Initializes all the elements in the Items Arriving window.
 * @param game Pointer to the core game.
 */
public ItemsArrivingState(Game game, GeoscapeState state)
{
	super(game);
	_state = state;
	_screen = false;

	// Create objects
	_window = new Window(this, 320, 180, 0, 10, WindowPopup.POPUP_BOTH);
	_btnOk = new TextButton(148, 16, 8, 166);
	_btnOk5Secs = new TextButton(148, 16, 160, 166);
	_txtTitle = new Text(310, 16, 5, 18);
	_txtItem = new Text(120, 8, 10, 34);
	_txtQuantity = new Text(50, 8, 150, 34);
	_txtDestination = new Text(110, 8, 205, 34);
	_lstTransfers = new TextList(288, 112, 8, 50);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(6)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_btnOk5Secs);
	add(_txtTitle);
	add(_txtItem);
	add(_txtQuantity);
	add(_txtDestination);
	add(_lstTransfers);

	// Set up objects
	_window.setColor(Palette.blockOffset(8)+8);
	_window.setBackground(_game.getResourcePack().getSurface("BACK13.SCR"));

	_btnOk.setColor(Palette.blockOffset(8)+8);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick((ActionHandler)ItemsArrivingState.btnOkClick);

	_btnOk5Secs.setColor(Palette.blockOffset(8)+8);
	_btnOk5Secs.setText(_game.getLanguage().getString("STR_OK_5_SECS"));
	_btnOk5Secs.onMouseClick((ActionHandler)ItemsArrivingState.btnOk5SecsClick);

	_txtTitle.setColor(Palette.blockOffset(8)+5);
	_txtTitle.setBig();
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setText(_game.getLanguage().getString("STR_ITEMS_ARRIVING"));

	_txtItem.setColor(Palette.blockOffset(8)+5);
	_txtItem.setText(_game.getLanguage().getString("STR_ITEM"));

	_txtQuantity.setColor(Palette.blockOffset(8)+5);
	_txtQuantity.setText(_game.getLanguage().getString("STR_QUANTITY_UC"));

	_txtDestination.setColor(Palette.blockOffset(8)+5);
	_txtDestination.setText(_game.getLanguage().getString("STR_DESTINATION"));

	_lstTransfers.setColor(Palette.blockOffset(8)+10);
	_lstTransfers.setArrowColor(Palette.blockOffset(8)+8);
	_lstTransfers.setColumns(3, 165, 25, 98);
	_lstTransfers.setSelectable(true);
	_lstTransfers.setBackground(_window);
	_lstTransfers.setMargin(2);

	for (Base i: _game.getSavedGame().getBases())
	{
		for (Transfer j: (i).getTransfers())
		{
			if ((j).getHours() == 0)
			{
				WStringstream ss;
				ss << (j).getQuantity();
				_lstTransfers.addRow(3, (j).getName(_game.getLanguage()).c_str(), ss.str().c_str(), (*i).getName().c_str());
				j = null;
				j = (i).getTransfers().erase(j);
			}
			else
			{
				++j;
			}
		}
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

/**
 * Reduces the speed to 5 Secs and returns to the previous screen.
 * @param action Pointer to an action.
 */
public void btnOk5SecsClick(Action action)
{
	_state.timerReset();
	_game.popState();
}

}
