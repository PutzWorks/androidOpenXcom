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
package putzworks.openXcom.Battlescape;

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Interface.Text;
import putzworks.openXcom.Interface.TextButton;
import putzworks.openXcom.Interface.Window;
import putzworks.openXcom.Savegame.Craft;

public class BriefingCrashState extends State
{
	private TextButton _btnOk;
	private Window _window;
	private Text _txtTitle, _txtUfo, _txtCraft, _txtBriefing;
	private Craft _craft;

/**
 * Initializes all the elements in the Crash Briefing screen.
 * @param game Pointer to the core game.
 * @param craft Pointer to the craft.
 */
public BriefingCrashState(Game game, Craft craft)
{
	super(game);
	_craft = craft;
	// Create objects
	_window = new Window(this, 320, 200, 0, 0);
	_btnOk = new TextButton(120, 18, 100, 164);
	_txtTitle = new Text(274, 16, 16, 24);
	_txtUfo = new Text(274, 16, 16, 40);
	_txtCraft = new Text(274, 16, 16, 56);
	_txtBriefing = new Text(274, 64, 16, 72);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(0)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_txtTitle);
	add(_txtUfo);
	add(_txtCraft);
	add(_txtBriefing);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+2);
	_window.setBackground(_game.getResourcePack().getSurface("BACK16.SCR"));

	_btnOk.setColor(Palette.blockOffset(8)+8);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnOkClick(action);
		}
	});

	_txtTitle.setColor(Palette.blockOffset(8)+5);
	_txtTitle.setBig();
	_txtTitle.setText(_game.getLanguage().getString("STR_UFO_CRASH_RECOVERY"));

	_txtUfo.setColor(Palette.blockOffset(8)+5);
	_txtUfo.setBig();
	_txtUfo.setText(_craft.getDestination().getName(_game.getLanguage()));

	_txtCraft.setColor(Palette.blockOffset(8)+5);
	_txtCraft.setBig();
	StringBuffer ss = new StringBuffer();
	ss.append(_game.getLanguage().getString("STR_CRAFT_") + _craft.getName(_game.getLanguage()));
	_txtCraft.setText(ss.toString());

	_txtBriefing.setColor(Palette.blockOffset(8)+5);
	_txtBriefing.setWordWrap(true);
	_txtBriefing.setText(_game.getLanguage().getString("STR_UFO_CRASH_RECOVERY_BRIEFING"));

	// Set music
	_game.getResourcePack().getMusic("GMDEFEND").play();
}

/**
 * Closes the window.
 * @param action Pointer to an action.
 */
public void btnOkClick(Action action)
{
	_game.popState();
	_game.pushState(new BattlescapeState(_game));
}

}
