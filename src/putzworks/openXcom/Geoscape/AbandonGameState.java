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
import putzworks.openXcom.Menu.MainMenuState;

public class AbandonGameState extends State
{
	private TextButton _btnYes, _btnNo;
	private Window _window;
	private Text _txtTitle;

/**
 * Initializes all the elements in the Abandon Game screen.
 * @param game Pointer to the core game.
 */
public AbandonGameState(Game game)
{
	super(game);
	_screen = false;

	// Create objects
	_window = new Window(this, 216, 160, 20, 20, WindowPopup.POPUP_BOTH);
	_btnYes = new TextButton(50, 20, 38, 140);
	_btnNo = new TextButton(50, 20, 168, 140);
	_txtTitle = new Text(206, 15, 25, 70);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(0)), Palette.backPos, 16);

	add(_window);
	add(_btnYes);
	add(_btnNo);
	add(_txtTitle);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+2);
	_window.setBackground(_game.getResourcePack().getSurface("BACK01.SCR"));

	_btnYes.setColor(Palette.blockOffset(15)+2);
	_btnYes.setText(_game.getLanguage().getString("STR_YES"));
	_btnYes.onMouseClick((ActionHandler)AbandonGameState.btnYesClick);

	_btnNo.setColor(Palette.blockOffset(15)+2);
	_btnNo.setText(_game.getLanguage().getString("STR_NO"));
	_btnNo.onMouseClick((ActionHandler)AbandonGameState.btnNoClick);

	_txtTitle.setColor(Palette.blockOffset(15)-1);
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setBig();
	WStringstream ss;
	ss << _game.getLanguage().getString("STR_ABANDON_GAME") << "?";
	_txtTitle.setText(ss.str());
}

/**
 * Goes back to the Main Menu.
 * @param action Pointer to an action.
 */
public void btnYesClick(Action action)
{
	_game.setState(new MainMenuState(_game));
	_game.setSavedGame(0);
	_game.setRuleset(0);
}

/**
 * Closes the window.
 * @param action Pointer to an action.
 */
public void btnNoClick(Action action)
{
	_game.popState();
}

}
