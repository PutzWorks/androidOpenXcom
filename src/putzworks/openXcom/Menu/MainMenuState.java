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
package putzworks.openXcom.Menu;

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Interface.Text;
import putzworks.openXcom.Interface.TextButton;
import putzworks.openXcom.Interface.Window;

public class MainMenuState extends State
{
	private TextButton _btnNew, _btnLoad, _btnQuit;
	private Window _window;
	private Text _txtTitle;

/**
 * Initializes all the elements in the Main Menu window.
 * @param game Pointer to the core game.
 */
public MainMenuState(Game game)
{
	super(game);
	// Create objects
	_window = new Window(this, 256, 160, 32, 20, POPUP_BOTH);
	_btnNew = new TextButton(192, 20, 64, 90);
	_btnLoad = new TextButton(192, 20, 64, 118);
	_btnQuit = new TextButton(192, 20, 64, 146);
	_txtTitle = new Text(256, 30, 32, 45);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(0)), Palette.backPos, 16);

	add(_window);
	add(_btnNew);
	add(_btnLoad);
	add(_btnQuit);
	add(_txtTitle);

	// Set up objects
	_window.setColor(Palette.blockOffset(8)+8);
	_window.setBackground(_game.getResourcePack().getSurface("BACK01.SCR"));

	_btnNew.setColor(Palette.blockOffset(8)+8);
	_btnNew.setText(_game.getLanguage().getString("STR_NEW_GAME"));
	_btnNew.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnNewClick(action);
		}
	});

	_btnLoad.setColor(Palette.blockOffset(8)+8);
	_btnLoad.setText(_game.getLanguage().getString("STR_LOAD_SAVED_GAME"));
	_btnLoad.onMouseClick((new ActionHandler() {
		public void handle(Action action) {
			btnLoadClick(action);
		}
	});

	_btnQuit.setColor(Palette.blockOffset(8)+8);
	_btnQuit.setText(_game.getLanguage().getString("STR_QUIT"));
	_btnQuit.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnQuitClick(action);
		}
	});

	_txtTitle.setColor(Palette.blockOffset(8)+10);
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setBig();
	_txtTitle.setText("OpenXcom\x02v0.2");

	// Set music
	_game.getResourcePack().getMusic("GMSTORY").play();
}

/**
 * Resets the palette
 * since it's bound to change on other screens.
 */
public void init()
{
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(0)), Palette.backPos, 16);
}

/**
 * Opens the New Game window.
 * @param action Pointer to an action.
 */
public void btnNewClick(Action action)
{
	_game.setState(new NewGameState(_game));
}

/**
 * Opens the Load Game screen.
 * @param action Pointer to an action.
 */
public void btnLoadClick(Action action)
{
	_game.pushState(new LoadGameState(_game));
}

/**
 * Quits the game.
 * @param action Pointer to an action.
 */
public void btnQuitClick(Action action)
{
	_game.quit();
}

}
