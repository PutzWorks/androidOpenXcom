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
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.Craft;

public class CraftsState extends State
{

	private TextButton _btnOk;
	private Window _window;
	private Text _txtTitle, _txtBase, _txtName, _txtStatus, _txtWeapon, _txtCrew, _txtHwp;
	private TextList _lstCrafts;
	private Base _base;


/**
 * Initializes all the elements in the Equip Craft screen.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 */
public CraftsState(Game game, Base base)
{
	super(game);
	_base = base;
	// Create objects
	_window = new Window(this, 320, 200, 0, 0);
	_btnOk = new TextButton(288, 16, 16, 176);
	_txtTitle = new Text(298, 16, 16, 8);
	_txtBase = new Text(298, 16, 16, 24);
	_txtName = new Text(94, 9, 16, 40);
	_txtStatus = new Text(50, 9, 110, 40);
	_txtWeapon = new Text(50, 16, 160, 40);
	_txtCrew = new Text(58, 9, 210, 40);
	_txtHwp = new Text(46, 9, 268, 40);
	_lstCrafts = new TextList(288, 118, 8, 58);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(3)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_txtTitle);
	add(_txtBase);
	add(_txtName);
	add(_txtStatus);
	add(_txtWeapon);
	add(_txtCrew);
	add(_txtHwp);
	add(_lstCrafts);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+4);
	_window.setBackground(_game.getResourcePack().getSurface("BACK14.SCR"));

	_btnOk.setColor(Palette.blockOffset(13)+13);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnOkClick(action);
		}
	});

	_txtTitle.setColor(Palette.blockOffset(15)+1);
	_txtTitle.setBig();
	_txtTitle.setText(_game.getLanguage().getString("STR_INTERCEPTION_CRAFT"));

	_txtBase.setColor(Palette.blockOffset(15)+1);
	_txtBase.setBig();
	String baseName = _game.getLanguage().getString("STR_BASE_");
	baseName += _base.getName();
	_txtBase.setText(baseName);

	_txtName.setColor(Palette.blockOffset(15)+1);
	_txtName.setText(_game.getLanguage().getString("STR_NAME_UC"));

	_txtStatus.setColor(Palette.blockOffset(15)+1);
	_txtStatus.setText(_game.getLanguage().getString("STR_STATUS"));

	_txtWeapon.setColor(Palette.blockOffset(15)+1);
	_txtWeapon.setText(_game.getLanguage().getString("STR_WEAPON_SYSTEMS"));

	_txtCrew.setColor(Palette.blockOffset(15)+1);
	_txtCrew.setText(_game.getLanguage().getString("STR_CREW"));

	_txtHwp.setColor(Palette.blockOffset(15)+1);
	_txtHwp.setText(_game.getLanguage().getString("STR_HWPS"));

	_lstCrafts.setColor(Palette.blockOffset(13)+10);
	_lstCrafts.setArrowColor(Palette.blockOffset(15)+4);
	_lstCrafts.setColumns(5, 94, 65, 47, 46, 28);
	_lstCrafts.setSelectable(true);
	_lstCrafts.setBackground(_window);
	_lstCrafts.setMargin(8);
	_lstCrafts.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			lstCraftsClick(action);
		}
	});
}

/**
 * The soldier names can change
 * after going into other screens.
 */
public void init()
{
	_lstCrafts.clearList();
	for (Craft i: _base.getCrafts())
	{
		StringBuffer ss = new StringBuffer(), ss2 = new StringBuffer(), ss3 = new StringBuffer();
		ss.append((i).getNumWeapons() + "/" + (i).getRules().getWeapons());
		ss2.append((i).getNumSoldiers());
		ss3.append((i).getNumHWPs());
		_lstCrafts.addRow(5, (i).getName(_game.getLanguage()), _game.getLanguage().getString((i).getStatus()), ss.toString(), ss2.toString(), ss3.toString());
	}
}

/**
 * Returns to the previous screen.
 * @param action Pointer to an action.
 */
public void btnOkClick(Action action)
{
	_game.popState();
}

/**
 * Shows the selected craft's info.
 * @param action Pointer to an action.
 */
public void lstCraftsClick(Action action)
{
	if (!_base.getCrafts().get(_lstCrafts.getSelectedRow()).getStatus().equals("STR_OUT"))
	{
		_game.pushState(new CraftInfoState(_game, _base, _lstCrafts.getSelectedRow()));
	}
}

}
