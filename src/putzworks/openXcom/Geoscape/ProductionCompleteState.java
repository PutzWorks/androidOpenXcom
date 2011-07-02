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
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Interface.Text.*;
import putzworks.openXcom.Interface.Window.WindowPopup;

public class ProductionCompleteState extends State
{
	private TextButton _btnOk;
	private Window _window;
	private Text _txtMessage;

/**
 * Initializes all the elements in a Production Complete window.
 * @param game Pointer to the core game.
 * @param item Item that finished producing.
 * @param base Base the item belongs to.
 */
public ProductionCompleteState(Game game, final String item, final String base)
{
	super(game);
	_screen = false;

	// Create objects
	_window = new Window(this, 256, 160, 32, 20, WindowPopup.POPUP_BOTH);
	_btnOk = new TextButton(120, 18, 100, 154);
	_txtMessage = new Text(246, 80, 37, 50);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(6)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_txtMessage);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+2);
	_window.setBackground(_game.getResourcePack().getSurface("BACK17.SCR"));

	_btnOk.setColor(Palette.blockOffset(8)+8);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnOkClick(action);
		}
	});

	_txtMessage.setColor(Palette.blockOffset(15)-1);
	_txtMessage.setAlign(TextHAlign.ALIGN_CENTER);
	_txtMessage.setVerticalAlign(TextVAlign.ALIGN_MIDDLE);
	_txtMessage.setBig();
	_txtMessage.setWordWrap(true);
	String s = _game.getLanguage().getString("STR_PRODUCTION_OF");
	s += item;
	s += _game.getLanguage().getString("STR__AT__");
	s += base;
	s += _game.getLanguage().getString("STR_IS_COMPLETE");
	_txtMessage.setText(s);
}

/**
 * Resets the palette.
 */
public void init()
{
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(6)), Palette.backPos, 16);
}

/**
 * Closes the window.
 * @param action Pointer to an action.
 */
public void btnOkClick(Action action)
{
	_game.popState();
}

}
