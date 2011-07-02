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
import putzworks.openXcom.Savegame.Target;


public class TargetInfoState extends State
{
	Target _target;

	TextButton _btnOk;
	Window _window;
	Text _txtTitle, _txtTargetted, _txtFollowers;

/**
 * Initializes all the elements in the Target Info window.
 * @param game Pointer to the core game.
 * @param target Pointer to the target to show info from.
 */
public TargetInfoState(Game game, Target target)
{
	super(game);
	_target = target;
	_screen = false;

	// Create objects
	_window = new Window(this, 192, 120, 32, 40, WindowPopup.POPUP_BOTH);
	_btnOk = new TextButton(160, 16, 48, 135);
	_txtTitle = new Text(182, 16, 37, 54);
	_txtTargetted = new Text(182, 8, 37, 74);
	_txtFollowers = new Text(182, 48, 37, 84);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(0)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_txtTitle);
	add(_txtTargetted);
	add(_txtFollowers);

	// Set up objects
	_window.setColor(Palette.blockOffset(8)+13);
	_window.setBackground(_game.getResourcePack().getSurface("BACK01.SCR"));

	_btnOk.setColor(Palette.blockOffset(8)+13);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick((ActionHandler)TargetInfoState.btnOkClick);

	_txtTitle.setColor(Palette.blockOffset(8)+10);
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setBig();
	_txtTitle.setText(_target.getName(_game.getLanguage()));

	_txtTargetted.setColor(Palette.blockOffset(15)-1);
	_txtTargetted.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTargetted.setText(_game.getLanguage().getString("STR_TARGETTED_BY"));

	_txtFollowers.setColor(Palette.blockOffset(15)-1);
	_txtFollowers.setAlign(TextHAlign.ALIGN_CENTER);
	WString s = L"";
	for (Target i: _target.getFollowers())
	{
		s += (i).getName(_game.getLanguage());
		s += L'\n';
	}
	_txtFollowers.setText(s);
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
