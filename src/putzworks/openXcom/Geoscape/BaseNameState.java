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

import putzworks.openXcom.Basescape.PlaceLiftState;
import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Interface.Text;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Interface.TextButton;
import putzworks.openXcom.Interface.TextEdit;
import putzworks.openXcom.Interface.Window;
import putzworks.openXcom.Interface.Window.WindowPopup;
import putzworks.openXcom.Savegame.Base;

public class BaseNameState extends State
{
	private Base _base;
	private Globe _globe;
	private Window _window;
	private Text _txtTitle;
	private TextEdit _edtName;
	private TextButton _btnOk;
	private boolean _first;

/**
 * Initializes all the elements in a Base Name window.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to name.
 * @param globe Pointer to the Geoscape globe.
 * @param first Is this the first base in the game?
 */
public BaseNameState(Game game, Base base, Globe globe, boolean first)
{
	super(game);
	_base = base;
	_globe = globe;
	_first = first;
	_screen = false;

	// Create objects
	_window = new Window(this, 192, 80, 32, 60, WindowPopup.POPUP_BOTH);
	_btnOk = new TextButton(162, 12, 47, 118);
	_txtTitle = new Text(182, 16, 37, 70);
	_edtName = new TextEdit(136, 16, 54, 94);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(0)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_txtTitle);
	add(_edtName);

	// Set up objects
	_window.setColor(Palette.blockOffset(8)+8);
	_window.setBackground(_game.getResourcePack().getSurface("BACK01.SCR"));

	_btnOk.setColor(Palette.blockOffset(8)+8);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnOkClick(action);
		}
	});

	_txtTitle.setColor(Palette.blockOffset(8)+5);
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setBig();
	_txtTitle.setText(_game.getLanguage().getString("STR_BASE_NAME"));

	_edtName.setColor(Palette.blockOffset(8)+5);
	_edtName.setBig();
	_edtName.focus();
	_edtName.onKeyboardPress(new ActionHandler() {
		public void handle(Action action) {
			edtNameKeyPress(action);
		}
	});
}

/**
 *
 */
public void nameBase()
{
	_base.setName(_edtName.getText());
	_game.popState();
	_game.popState();
	if (!_first)
	{
		_game.popState();
		_game.pushState(new PlaceLiftState(_game, _base, _globe));
	}
}

/**
 * Returns to the previous screen.
 * @param action Pointer to an action.
 */
public void edtNameKeyPress(Action action)
{
	if (action.getDetails().key.keysym.sym == SDLK_RETURN)
	{
		nameBase();
	}
}

/**
 * Returns to the previous screen
 * @param action Pointer to an action.
 */
public void btnOkClick(Action action)
{
	nameBase();
}

}
