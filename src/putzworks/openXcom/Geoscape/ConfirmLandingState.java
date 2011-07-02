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

import putzworks.openXcom.Battlescape.BattlescapeGenerator;
import putzworks.openXcom.Battlescape.BriefingCrashState;
import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Interface.Text;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Interface.TextButton;
import putzworks.openXcom.Interface.Window;
import putzworks.openXcom.Interface.Window.WindowPopup;
import putzworks.openXcom.Savegame.Craft;
import putzworks.openXcom.Savegame.SavedBattleGame;
import putzworks.openXcom.Savegame.Ufo;

public class ConfirmLandingState extends State
{
	private Craft _craft;
	private Window _window;
	private int _texture, _shade;
	private Text _txtCraft, _txtTarget, _txtReady, _txtBegin;
	private TextButton _btnYes, _btnNo;

/**
 * Initializes all the elements in the Confirm Landing window.
 * @param game Pointer to the core game.
 * @param craft Pointer to the craft to confirm.
 * @param texture Texture of the landing site.
 * @param shade Shade of the landing site.
 */
public ConfirmLandingState(Game game, Craft craft, int texture, int shade)
{
	super(game);
	_craft = craft;
	_texture = texture;
	_shade = shade;
	_screen = false;

	// Create objects
	_window = new Window(this, 216, 160, 20, 20, WindowPopup.POPUP_BOTH);
	_btnYes = new TextButton(80, 20, 40, 150);
	_btnNo = new TextButton(80, 20, 136, 150);
	_txtCraft = new Text(206, 16, 25, 40);
	_txtTarget = new Text(206, 16, 25, 88);
	_txtReady = new Text(206, 32, 25, 56);
	_txtBegin = new Text(206, 16, 25, 130);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(3)), Palette.backPos, 16);
	
	add(_window);
	add(_btnYes);
	add(_btnNo);
	add(_txtCraft);
	add(_txtTarget);
	add(_txtReady);
	add(_txtBegin);
	
	// Set up objects
	_window.setColor(Palette.blockOffset(8)+8);
	_window.setBackground(_game.getResourcePack().getSurface("BACK15.SCR"));

	_btnYes.setColor(Palette.blockOffset(8)+8);
	_btnYes.setText(_game.getLanguage().getString("STR_YES"));
	_btnYes.onMouseClick((ActionHandler)ConfirmLandingState.btnYesClick);

	_btnNo.setColor(Palette.blockOffset(8)+8);
	_btnNo.setText(_game.getLanguage().getString("STR_NO"));
	_btnNo.onMouseClick((ActionHandler)ConfirmLandingState.btnNoClick);

	_txtCraft.setColor(Palette.blockOffset(8)+10);
	_txtCraft.setBig();
	_txtCraft.setAlign(TextHAlign.ALIGN_CENTER);
	_txtCraft.setText(_craft.getName(_game.getLanguage()));

	_txtTarget.setColor(Palette.blockOffset(8)+10);
	_txtTarget.setBig();
	_txtTarget.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTarget.setText(_craft.getDestination().getName(_game.getLanguage()));

	_txtReady.setColor(Palette.blockOffset(8)+5);
	_txtReady.setBig();
	_txtReady.setAlign(TextHAlign.ALIGN_CENTER);
	_txtReady.setText(_game.getLanguage().getString("STR_READY_TO_LAND_NEAR"));

	_txtBegin.setColor(Palette.blockOffset(8)+5);
	_txtBegin.setBig();
	_txtBegin.setAlign(TextHAlign.ALIGN_CENTER);
	_txtBegin.setText(_game.getLanguage().getString("STR_BEGIN_MISSION"));
}

/**
 * Enters the mission.
 * @param action Pointer to an action.
 */
public void btnYesClick(Action action)
{
	_game.popState();
	Ufo u = (Ufo)(_craft.getDestination());
	if (u != null)
	{
		_game.getSavedGame().setBattleGame(new SavedBattleGame());

		BattlescapeGenerator bgen = new BattlescapeGenerator(_game);
		bgen.setMissionType(MISS_UFORECOVERY);
		bgen.setWorldTexture(_texture);
		bgen.setWorldShade(_shade);
		bgen.setCraft(_craft);
		bgen.setUfo(u);
		bgen.run();
		bgen = null;

		_game.pushState(new BriefingCrashState(_game, _craft));
	}
}

/**
 * Returns the craft to base and closes the window.
 * @param action Pointer to an action.
 */
public void btnNoClick(Action action)
{
	_craft.returnToBase();
	_game.popState();
}

}
