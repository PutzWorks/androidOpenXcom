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
import putzworks.openXcom.Interface.Text.*;
import putzworks.openXcom.Interface.TextButton;
import putzworks.openXcom.Interface.Window;
import putzworks.openXcom.Interface.Window.WindowPopup;

public class GeoscapeErrorState extends State
{
	private TextButton _btnOk;
	private Window _window;
	private Text _txtMessage;

/**
 * Initializes all the elements in a Geoscape error window.
 * @param game Pointer to the core game.
 * @param str Error message to display.
 */
public GeoscapeErrorState(Game game, String str)
{
	super(game);
	_screen = false;

	// Create objects
	_window = new Window(this, 256, 160, 32, 20, WindowPopup.POPUP_BOTH);
	_btnOk = new TextButton(120, 18, 100, 154);
	_txtMessage = new Text(246, 80, 37, 50);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(0)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_txtMessage);

	// Set up objects
	_window.setColor(Palette.blockOffset(8)+13);
	_window.setBackground(_game.getResourcePack().getSurface("BACK01.SCR"));

	_btnOk.setColor(Palette.blockOffset(8)+13);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick((ActionHandler)GeoscapeErrorState.btnOkClick);

	_txtMessage.setColor(Palette.blockOffset(8)+10);
	_txtMessage.setAlign(TextHAlign.ALIGN_CENTER);
	_txtMessage.setVerticalAlign(TextVAlign.ALIGN_MIDDLE);
	_txtMessage.setBig();
	_txtMessage.setWordWrap(true);
	_txtMessage.setText(_game.getLanguage().getString(str));
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
