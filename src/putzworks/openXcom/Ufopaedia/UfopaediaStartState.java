/*
 * Copyright 2011 OpenXcom Developers.
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
package putzworks.openXcom.Ufopaedia;

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
import putzworks.openXcom.Ruleset.XcomRuleset;

public class UfopaediaStartState extends State
{
	protected Window _window;
	protected Text _txtTitle;
	protected TextButton _btnOk;
	protected TextButton _btnCraftArmament;
	protected TextButton _btnHWP;
	protected TextButton _btnWeaponsEquipment;
	protected TextButton _btnAlienArtefacts;
	protected TextButton _btnBaseFacilities;
	protected TextButton _btnAlienLifeforms;
	protected TextButton _btnAlienResearch;
	protected TextButton _btnUfoComponents;
	protected TextButton _btnUfos;

	public UfopaediaStartState(Game game)
	{
		super(game);
		_screen = false;

		// set background window
		_window = new Window(this, 256, 180, 32, 10, WindowPopup.POPUP_BOTH);
		
		// set title
		_txtTitle = new Text(224, 16, 48, 33);
		
		// set buttons
		_btnOk = new TextButton(224, 12, 48, 167);
		_btnCraftArmament = new TextButton(224, 12, 48, 50);
		_btnAlienLifeforms = new TextButton(224, 12, 48, 115);
		_btnAlienResearch = new TextButton(224, 12, 48, 128);

		// Set palette
		_game.setPalette(_game.getResourcePack().getPalette("PALETTES.DAT_0").getColors());

		add(_window);
		add(_txtTitle);
		add(_btnOk);
		add(_btnCraftArmament);
		add(_btnAlienLifeforms);
		add(_btnAlienResearch);
		
		_window.setColor(Palette.blockOffset(15)+2);
		_window.setBackground(_game.getResourcePack().getSurface("BACK01.SCR"));
		
		_txtTitle.setColor(Palette.blockOffset(8)+10);
		_txtTitle.setBig();
		_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
		_txtTitle.setText(_game.getLanguage().getString("STR_UFOPAEDIA"));
		
		_btnOk.setColor(Palette.blockOffset(8)+8);
		_btnOk.setText(_game.getLanguage().getString("STR_OK"));
		_btnOk.onMouseClick(new ActionHandler() {
			public void handle(Action action) {
				btnOkClick(action);
			}
		});
		
		_btnCraftArmament.setColor(Palette.blockOffset(8)+8);
		_btnCraftArmament.setText(_game.getLanguage().getString("STR_XCOM_CRAFT_ARMAMENT"));
		_btnCraftArmament.onMouseClick(new ActionHandler() {
			public void handle(Action action) {
				btnCraftArmamentClick(action);
			}
		});

		_btnAlienLifeforms.setColor(Palette.blockOffset(8)+8);
		_btnAlienLifeforms.setText(_game.getLanguage().getString("STR_ALIEN_LIFE_FORMS"));
		_btnAlienLifeforms.onMouseClick(new ActionHandler() {
			public void handle(Action action) {
				btnAlienLifeformsClick(action);
			}
		});
		
		_btnAlienResearch.setColor(Palette.blockOffset(8)+8);
		_btnAlienResearch.setText(_game.getLanguage().getString("STR_ALIEN_RESEARCH_UC"));
		_btnAlienResearch.onMouseClick(new ActionHandler() {
			public void handle(Action action) {
				btnAlienResearchClick(action);
			}
		});
	}
	
	/**
	 * Returns to the previous screen.
	 * @param action Pointer to an action.
	 */
	protected void btnOkClick(Action action)
	{
		_game.popState();
//		_game.quit();
	}
	
	/**
	 * 
	 * @param action Pointer to an action.
	 */
	protected void btnCraftArmamentClick(Action action)
	{
		_game.pushState(new UfopaediaSelectState(_game, Ufopaedia.UFOPAEDIA_XCOM_CRAFT_ARMAMENT));
	}
	
	/**
	 * 
	 * @param action Pointer to an action.
	 */
	protected void btnAlienLifeformsClick(Action action)
	{
		_game.pushState(new UfopaediaSelectState(_game, Ufopaedia.UFOPAEDIA_ALIEN_LIFE_FORMS));
	}
	
	/**
	 * 
	 * @param action Pointer to an action.
	 */
	protected void btnAlienResearchClick(Action action)
	{
		_game.pushState(new UfopaediaSelectState(_game, Ufopaedia.UFOPAEDIA_ALIEN_RESEARCH));
	}
	
}
