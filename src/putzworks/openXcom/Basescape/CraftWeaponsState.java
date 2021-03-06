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
package putzworks.openXcom.Basescape;

import java.util.Vector;
import putzworks.openXcom.Engine.*;
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Ruleset.RuleCraftWeapon;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.CraftWeapon;

public class CraftWeaponsState extends State
{
	private Base _base;
	private int _craft, _weapon;

	private TextButton _btnCancel;
	private Window _window;
	private Text _txtTitle, _txtArmament, _txtQuantity, _txtAmmunition, _txtAvailable;
	private TextList _lstWeapons;
	private Vector<RuleCraftWeapon> _weapons;


/**
 * Initializes all the elements in the Craft Weapons window.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 * @param craft ID of the selected craft.
 * @param weapon ID of the selected weapon.
 */
public CraftWeaponsState(Game game, Base base, int craft, int weapon)
{
	super(game);
	_base = base; 
	_craft = craft;
	_weapon = weapon;
	_weapons = new Vector<RuleCraftWeapon>();
	_screen = false;

	// Create objects
	_window = new Window(this, 220, 160, 50, 20, POPUP_BOTH);
	_btnCancel = new TextButton(140, 16, 90, 156);
	_txtTitle = new Text(208, 16, 56, 28);
	_txtArmament = new Text(76, 9, 66, 52);
	_txtQuantity = new Text(50, 9, 145, 52);
	_txtAmmunition = new Text(68, 9, 195, 44);
	_txtAvailable = new Text(68, 9, 195, 52);
	_lstWeapons = new TextList(188, 80, 58, 68);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(4)), Palette.backPos, 16);

	add(_window);
	add(_btnCancel);
	add(_txtTitle);
	add(_txtArmament);
	add(_txtQuantity);
	add(_txtAmmunition);
	add(_txtAvailable);
	add(_lstWeapons);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+9);
	_window.setBackground(_game.getResourcePack().getSurface("BACK14.SCR"));

	_btnCancel.setColor(Palette.blockOffset(15)+9);
	_btnCancel.setText(_game.getLanguage().getString("STR_CANCEL_UC"));
	_btnCancel.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnCancelClick(action);
		}
	});

	_txtTitle.setColor(Palette.blockOffset(15)+6);
	_txtTitle.setBig();
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setText(_game.getLanguage().getString("STR_SELECT_ARMAMENT"));

	_txtArmament.setColor(Palette.blockOffset(15)+6);
	_txtArmament.setText(_game.getLanguage().getString("STR_ARMAMENT"));

	_txtQuantity.setColor(Palette.blockOffset(15)+6);
	_txtQuantity.setText(_game.getLanguage().getString("STR_QUANTITY_UC"));

	_txtAmmunition.setColor(Palette.blockOffset(15)+6);
	_txtAmmunition.setText(_game.getLanguage().getString("STR_AMMUNITION"));

	_txtAvailable.setColor(Palette.blockOffset(15)+6);
	_txtAvailable.setText(_game.getLanguage().getString("STR_AVAILABLE"));

	_lstWeapons.setColor(Palette.blockOffset(13)+10);
	_lstWeapons.setArrowColor(Palette.blockOffset(15)+9);
	_lstWeapons.setColumns(3, 94, 50, 36);
	_lstWeapons.setSelectable(true);
	_lstWeapons.setBackground(_window);
	_lstWeapons.setMargin(8);

	_lstWeapons.addRow(1, _game.getLanguage().getString("STR_NONE_UC"));
	_weapons.add(null);

	String[] s = new String[]{"STR_STINGRAY", "STR_AVALANCHE", "STR_CANNON_UC"};

	for (int i = 0; i < 3; ++i)
	{
		RuleCraftWeapon w = _game.getRuleset().getCraftWeapon(s[i]);
		if (_base.getItems().getItem(w.getLauncherItem()) > 0)
		{
			_weapons.add(w);
			StringBuffer ss = new StringBuffer(), ss2 = new StringBuffer();
			ss.append(_base.getItems().getItem(w.getLauncherItem()));
			ss2.append(_base.getItems().getItem(w.getClipItem()));
			_lstWeapons.addRow(3, _game.getLanguage().getString(w.getType())), ss.toString(), ss2.toString());
		}
	}
	_lstWeapons.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			lstWeaponsClick(action);
		}
	});
}

/**
 * Returns to the previous screen.
 * @param action Pointer to an action.
 */
public void btnCancelClick(Action action)
{
	_game.popState();
}

/**
 * Equips the weapon on the craft and returns to the previous screen.
 * @param action Pointer to an action.
 */
public void lstWeaponsClick(Action action)
{
	CraftWeapon current = _base.getCrafts().at(_craft).getWeapons().at(_weapon);
	// Remove current weapon
	if (current != null)
	{
		_base.getItems().addItem(current.getRules().getLauncherItem());
		_base.getItems().addItem(current.getRules().getClipItem(), (int)Math.floor((double)current.getAmmo() / current.getRules().getRearmRate()));
		current = null;
		_base.getCrafts().get(_craft).getWeapons().set(_weapon, null);
	}

	// Equip new weapon
	if (_weapons.get(_lstWeapons.getSelectedRow()) != null)
	{
		CraftWeapon sel = new CraftWeapon(_weapons.get(_lstWeapons.getSelectedRow()), 0);
		sel.setRearming(true);
		_base.getItems().removeItem(sel.getRules().getLauncherItem());
		_base.getCrafts().get(_craft).getWeapons().set(_weapon, sel);
		if (_base.getCrafts().get(_craft).getStatus().equals("STR_READY"))
		{
			_base.getCrafts().get(_craft).setStatus("STR_REARMING");
		}
	}

	_game.popState();
}

}
