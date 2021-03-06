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

import java.util.Vector;

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Interface.Window.WindowPopup;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.Craft;

public class InterceptState extends State
{
	private TextButton _btnCancel;
	private Window _window;
	private Text _txtTitle, _txtCraft, _txtStatus, _txtBase, _txtWeapons;
	private TextList _lstCrafts;
	private Globe _globe;
	private Base _base;
	private Vector<Craft> _crafts;

/**
 * Initializes all the elements in the Intercept window.
 * @param game Pointer to the core game.
 * @param globe Pointer to the Geoscape globe.
 * @param base Pointer to base to show contained crafts (NULL to show all crafts).
 */
public InterceptState(Game game, Globe globe, Base base)
{
	super(game);
	_globe = globe;
	_base = base;
	_crafts = new Vector<Craft>();
	_screen = false;

	// Create objects
	_window = new Window(this, 320, 140, 0, 30, WindowPopup.POPUP_HORIZONTAL);
	_btnCancel = new TextButton(288, 16, 16, 146);
	_txtTitle = new Text(300, 16, 10, 46);
	_txtCraft = new Text(86, 9, 14, 70);
	_txtStatus = new Text(65, 9, 100, 70);
	_txtBase = new Text(85, 9, 165, 70);
	_txtWeapons = new Text(80, 16, 238, 62);
	_lstCrafts = new TextList(288, 64, 8, 78);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(4)), Palette.backPos, 16);

	add(_window);
	add(_btnCancel);
	add(_txtTitle);
	add(_txtCraft);
	add(_txtStatus);
	add(_txtBase);
	add(_txtWeapons);
	add(_lstCrafts);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+2);
	_window.setBackground(_game.getResourcePack().getSurface("BACK12.SCR"));

	_btnCancel.setColor(Palette.blockOffset(8)+8);
	_btnCancel.setText(_game.getLanguage().getString("STR_CANCE"));
	_btnCancel.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnCancelClick(action);
		}
	});

	_txtTitle.setColor(Palette.blockOffset(15)-1);
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setBig();
	_txtTitle.setText(_game.getLanguage().getString("STR_LAUNCH_INTERCEPTION"));

	_txtCraft.setColor(Palette.blockOffset(8)+5);
	_txtCraft.setText(_game.getLanguage().getString("STR_CRAFT"));

	_txtStatus.setColor(Palette.blockOffset(8)+5);
	_txtStatus.setText(_game.getLanguage().getString("STR_STATUS"));

	_txtBase.setColor(Palette.blockOffset(8)+5);
	_txtBase.setText(_game.getLanguage().getString("STR_BASE"));

	_txtWeapons.setColor(Palette.blockOffset(8)+5);
	_txtWeapons.setText(_game.getLanguage().getString("STR_WEAPONS_CREW_HWPS"));

	_lstCrafts.setColor(Palette.blockOffset(15)-1);
	_lstCrafts.setSecondaryColor(Palette.blockOffset(8)+10);
	_lstCrafts.setArrowColor(Palette.blockOffset(15)+2);
	_lstCrafts.setColumns(4, 86, 65, 85, 46);
	_lstCrafts.setSelectable(true);
	_lstCrafts.setBackground(_window);
	_lstCrafts.setMargin(6);
	_lstCrafts.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			lstCraftsClick(action);
		}
	});

	int row = 0;
	for (Base i: _game.getSavedGame().getBases())
	{
		if (_base != null && i != _base)
			continue;
		for (Craft j: (i).getCrafts())
		{
			StringBuffer ss = new StringBuffer();
			if ((j).getNumWeapons() > 0)
			{
				ss.append('\x01' + (j).getNumWeapons() + '\x01');
			}
			else
			{
				ss.append((j).getNumWeapons());
			}
			ss.append("/");
			if ((j).getNumSoldiers() > 0)
			{
				ss.append('\x01' + (j).getNumSoldiers() + '\x01');
			}
			else
			{
				ss.append((j).getNumSoldiers());
			}
			ss.append("/");
			if ((j).getNumHWPs() > 0)
			{
				ss.append('\x01' + (j).getNumWeapons() + '\x01');
			}
			else
			{
				ss.append((j).getNumHWPs());
			}
			_crafts.add(j);
			_lstCrafts.addRow(4, (j).getName(_game.getLanguage()).c_str(), _game.getLanguage().getString((*j).getStatus()).c_str(), (*i).getName().c_str(), ss.str().c_str());
			if ((j).getStatus().equals("STR_READY"))
			{
				_lstCrafts.getCell(row, 1).setColor(Palette.blockOffset(8)+10);
			}
			row++;
		}
	}
	_lstCrafts.draw();
}

/**
 * Closes the window.
 * @param action Pointer to an action.
 */
public void btnCancelClick(Action action)
{
	_game.popState();
}

/**
 * Pick a target for the selected craft.
 * @param action Pointer to an action.
 */
public void lstCraftsClick(Action action)
{
	Craft c = _crafts.get(_lstCrafts.getSelectedRow());
	if (c.getStatus() == "STR_READY")
	{
		_game.popState();
		_game.pushState(new SelectDestinationState(_game, c, _globe));
	}
}

}
