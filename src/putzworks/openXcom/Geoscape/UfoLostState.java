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
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Interface.Window.WindowPopup;

public class UfoLostState extends State
{
	TextButton _btnOk;
	Window _window;
	Text _txtTitle;
	WString _id;

/**
 * Initializes all the elements in the Ufo Lost window.
 * @param game Pointer to the core game.
 * @param id Name of the UFO.
 */
public UfoLostState(Game game, WString id)
{
	super(game);
	_id = id;
	_screen = false;

	// Create objects
	_window = new Window(this, 192, 104, 32, 48, WindowPopup.POPUP_BOTH);
	_btnOk = new TextButton(60, 12, 98, 112);
	_txtTitle = new Text(160, 30, 48, 72);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(7)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_txtTitle);

	// Set up objects
	_window.setColor(Palette.blockOffset(8)+8);
	_window.setBackground(_game.getResourcePack().getSurface("BACK15.SCR"));

	_btnOk.setColor(Palette.blockOffset(8)+8);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick((ActionHandler)UfoLostState.btnOkClick);

	_txtTitle.setColor(Palette.blockOffset(8)+5);
	_txtTitle.setBig();
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	WString s = _id;
	s += L'\n';
	s += _game.getLanguage().getString("STR_TRACKING_LOST");
	_txtTitle.setText(s);
}

/**
 * Resets the palette.
 */
public void init()
{
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(7)), Palette.backPos, 16);
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
