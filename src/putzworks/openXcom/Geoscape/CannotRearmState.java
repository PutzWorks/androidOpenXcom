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
import putzworks.openXcom.Interface.Text.TextVAlign;
import putzworks.openXcom.Interface.TextButton;
import putzworks.openXcom.Interface.Window;
import putzworks.openXcom.Interface.Window.WindowPopup;

public class CannotRearmState extends State
{
	private GeoscapeState _state;
	private TextButton _btnOk, _btnOk5Secs;
	private Window _window;
	private Text _txtMessage;

/**
 * Initializes all the elements in a Cannot Rearm window.
 * @param game Pointer to the core game.
 * @param ammo Ammo missing.
 * @param craft Craft rearming.
 * @param base Base the craft belongs to.
 */
public CannotRearmState(Game game, GeoscapeState state, final String ammo, final String craft, final String base)
{
	super(game);
	_state = state;
	_screen = false;

	// Create objects
	_window = new Window(this, 256, 160, 32, 20, WindowPopup.POPUP_BOTH);
	_btnOk = new TextButton(100, 18, 48, 150);
	_btnOk5Secs = new TextButton(100, 18, 172, 150);
	_txtMessage = new Text(226, 80, 47, 50);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(0)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_btnOk5Secs);
	add(_txtMessage);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+2);
	_window.setBackground(_game.getResourcePack().getSurface("BACK12.SCR"));

	_btnOk.setColor(Palette.blockOffset(8)+8);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnOkClick(action);
		}
	});

	_btnOk5Secs.setColor(Palette.blockOffset(8)+8);
	_btnOk5Secs.setText(_game.getLanguage().getString("STR_OK_5_SECS"));
	_btnOk5Secs.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnOk5SecsClick(action);
		}
	});

	_txtMessage.setColor(Palette.blockOffset(15)-1);
	_txtMessage.setAlign(TextHAlign.ALIGN_CENTER);
	_txtMessage.setVerticalAlign(TextVAlign.ALIGN_MIDDLE);
	_txtMessage.setBig();
	_txtMessage.setWordWrap(true);
	String s = _game.getLanguage().getString("STR_NOT_ENOUGH");
	s += ammo;
	s += _game.getLanguage().getString("STR_TO_REARM");
	s += craft;
	s += _game.getLanguage().getString("STR_AT_");
	s += base;
	_txtMessage.setText(s);
}

/**
 * Resets the palette.
 */
public void init()
{
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(0)), Palette.backPos, 16);
}

/**
 * Closes the window.
 * @param action Pointer to an action.
 */
public void btnOkClick(Action action)
{
	_game.popState();
}

/**
 * Closes the window.
 * @param action Pointer to an action.
 */
public void btnOk5SecsClick(Action action)
{
	_state.timerReset();
	_game.popState();
}

}
