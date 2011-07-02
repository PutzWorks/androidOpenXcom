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
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Interface.Text.TextVAlign;
import putzworks.openXcom.Interface.Window.WindowPopup;
import putzworks.openXcom.Savegame.Base;

public class BasescapeErrorState extends State
{
	private TextButton _btnOk;
	private Window _window;
	private Text _txtError;

/**
 * Initializes all the elements in a Basescape error window.
 * @param game Pointer to the core game.
 * @param str Error message to display.
 */
public BasescapeErrorState(Game game, String str)
{
	super(game);
	_screen = false;

	// Create objects
	_window = new Window(this, 256, 160, 32, 20, WindowPopup.POPUP_BOTH);
	_btnOk = new TextButton(120, 18, 100, 154);
	_txtError = new Text(234, 80, 44, 50);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(6)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_txtError);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+4);
	_window.setBackground(_game.getResourcePack().getSurface("BACK13.SCR"));

	_btnOk.setColor(Palette.blockOffset(15)+4);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick((ActionHandler)BasescapeErrorState.btnOkClick);

	_txtError.setColor(Palette.blockOffset(15)+1);
	_txtError.setAlign(TextHAlign.ALIGN_CENTER);
	_txtError.setVerticalAlign(TextVAlign.ALIGN_MIDDLE);
	_txtError.setBig();
	_txtError.setWordWrap(true);
	_txtError.setText(_game.getLanguage().getString(str));
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
