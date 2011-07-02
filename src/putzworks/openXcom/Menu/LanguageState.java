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
import putzworks.openXcom.Engine.Language;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Interface.TextButton;
import putzworks.openXcom.Interface.Window;
import putzworks.openXcom.Interface.Window.WindowPopup;

public class LanguageState extends State
{
	public TextButton _btnEnglish, _btnGerman, _btnFrench, _btnItalian, _btnSpanish;
	public Window _window;

/**
 * Initializes all the elements in the Language window.
 * @param game Pointer to the core game.
 */
LanguageState(Game game)
{
	super(game);
	// Create objects
	_window = new Window(this, 256, 160, 32, 20, WindowPopup.POPUP_BOTH);
	_btnEnglish = new TextButton(192, 20, 64, 34);
	_btnGerman = new TextButton(192, 20, 64, 62);
	_btnFrench = new TextButton(192, 20, 64, 90);
	_btnItalian = new TextButton(192, 20, 64, 118);
	_btnSpanish = new TextButton(192, 20, 64, 146);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("PALETTES.DAT_0").getColors());
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(0)), Palette.backPos, 16);
	
	add(_window);
	add(_btnEnglish);
	add(_btnGerman);
	add(_btnFrench);
	add(_btnItalian);
	add(_btnSpanish);

	// Set up objects
	_window.setColor(Palette.blockOffset(8)+8);
	_window.setBackground(_game.getResourcePack().getSurface("BACK01.SCR"));
	
	_btnEnglish.setColor(Palette.blockOffset(8)+8);
	_btnEnglish.setText("ENGLISH");
	_btnEnglish.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnEnglishClick(action);
		}
	});
	
	_btnGerman.setColor(Palette.blockOffset(8)+8);
	_btnGerman.setText("DEUTSCH");
	_btnGerman.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnGermanClick(action);
		}
	});

	_btnFrench.setColor(Palette.blockOffset(8)+8);
	_btnFrench.setText("FRANCAIS");
	_btnFrench.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnFrenchClick(action);
		}
	});

	_btnItalian.setColor(Palette.blockOffset(8)+8);
	_btnItalian.setText("ITALIANO");
	_btnItalian.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnItalianClick(action);
		}
	});

	_btnSpanish.setColor(Palette.blockOffset(8)+8);
	_btnSpanish.setText("ESPANO");
	_btnSpanish.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnSpanishClick(action);
		}
	});
}

public void changeLanguage(final String lang)
{
	StringBuffer ss = new StringBuffer();
	ss.append(_game.getResourcePack().getFolder() + "Language/" + lang);
	Language l = new Language();
	l.loadLng(ss.toString());
	_game.setLanguage(l);
	_game.setState(new MainMenuState(_game));
}

/**
 * Sets the language to English and opens
 * the Main Menu window.
 * @param action Pointer to an action.
 */
public void btnEnglishClick(Action action)
{
	changeLanguage("English.lng");
}

/**
 * Sets the language to German and opens
 * the Main Menu window.
 * @param action Pointer to an action.
 */
public void btnGermanClick(Action action)
{
	changeLanguage("German.lng");
}

/**
 * Sets the language to French and opens
 * the Main Menu window.
 * @param action Pointer to an action.
 */
public void btnFrenchClick(Action action)
{
	changeLanguage("French.lng");
}

/**
 * Sets the language to Italian and opens
 * the Main Menu window.
 * @param action Pointer to an action.
 */
public void btnItalianClick(Action action)
{
	changeLanguage("Italian.lng");
}

/**
 * Sets the language to Spanish and opens
 * the Main Menu window.
 * @param action Pointer to an action.
 */
public void btnSpanishClick(Action action)
{
	changeLanguage("Spanish.lng");
}

}
