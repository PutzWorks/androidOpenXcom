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
import putzworks.openXcom.Ruleset.RuleItem;
import putzworks.openXcom.Savegame.Base;

public class StoresState extends State
{
	private Base _base;

	private TextButton _btnOk;
	private Window _window;
	private Text _txtTitle, _txtItem, _txtQuantity, _txtSpaceUsed;
	private TextList _lstStores;

/**
 * Initializes all the elements in the Stores window.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 */
public StoresState(Game game, Base base)
{
	super(game);
	_base = base;
	// Create objects
	_window = new Window(this, 320, 200, 0, 0);
	_btnOk = new TextButton(300, 16, 10, 176);
	_txtTitle = new Text(310, 16, 5, 8);
	_txtItem = new Text(142, 8, 10, 32);
	_txtQuantity = new Text(88, 8, 152, 32);
	_txtSpaceUsed = new Text(74, 8, 240, 32);
	_lstStores = new TextList(288, 128, 8, 40);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(0)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_txtTitle);
	add(_txtItem);
	add(_txtQuantity);
	add(_txtSpaceUsed);
	add(_lstStores);

	// Set up objects
	_window.setColor(Palette.blockOffset(13)+13);
	_window.setBackground(_game.getResourcePack().getSurface("BACK13.SCR"));

	_btnOk.setColor(Palette.blockOffset(13)+13);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnOkClick(action);
		}
	});

	_txtTitle.setColor(Palette.blockOffset(13)+10);
	_txtTitle.setBig();
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setText(_game.getLanguage().getString("STR_STORES"));

	_txtItem.setColor(Palette.blockOffset(13)+10);
	_txtItem.setText(_game.getLanguage().getString("STR_ITEM"));

	_txtQuantity.setColor(Palette.blockOffset(13)+10);
	_txtQuantity.setText(_game.getLanguage().getString("STR_QUANTITY_UC"));

	_txtSpaceUsed.setColor(Palette.blockOffset(13)+10);
	_txtSpaceUsed.setText(_game.getLanguage().getString("STR_SPACE_USED"));

	_lstStores.setColor(Palette.blockOffset(13)+10);
	_lstStores.setArrowColor(Palette.blockOffset(13)+13);
	_lstStores.setColumns(3, 162, 92, 32);
	_lstStores.setSelectable(true);
	_lstStores.setBackground(_window);
	_lstStores.setMargin(2);

	for (Map<String, Integer>.iterator i = _base.getItems().getContents().begin(); i != _base.getItems().getContents().end(); ++i)
	{
		RuleItem rule = _game.getRuleset().getItem(i.first);
		StringBuffer ss = new StringBuffer(), ss2 = new StringBuffer();
		ss.append(i.second);
		ss2.append(i.second * rule.getSize());
		_lstStores.addRow(3, _game.getLanguage().getString(i.first), ss.toString(), ss2.toString());
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
