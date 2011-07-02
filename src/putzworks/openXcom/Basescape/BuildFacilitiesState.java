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

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Interface.Window.WindowPopup;
import putzworks.openXcom.Ruleset.RuleBaseFacility;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.Craft;
import putzworks.openXcom.Savegame.Soldier;

public class BuildFacilitiesState extends State
{
	private Base _base;
	private State _state;
	private Vector<RuleBaseFacility> _facilities;

	private TextButton _btnOk;
	private Window _window;
	private Text _txtTitle;
	private TextList _lstFacilities;

/**
 * Initializes all the elements in the Build Facilities window.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 * @param state Pointer to the base state to refresh.
 */
public BuildFacilitiesState(Game game, Base base, State state)
{
	super(game);
	_base = base; 
	_state = state; 
	_facilities = new Vector<RuleBaseFacility>();

	_screen = false;

	// Create objects
	_window = new Window(this, 128, 160, 192, 40, WindowPopup.POPUP_VERTICAL);
	_btnOk = new TextButton(112, 16, 200, 176);
	_txtTitle = new Text(118, 16, 197, 48);
	_lstFacilities = new TextList(105, 110, 200, 64);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(6)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_txtTitle);
	add(_lstFacilities);

	// Set up objects
	_window.setColor(Palette.blockOffset(13)+8);
	_window.setBackground(_game.getResourcePack().getSurface("BACK05.SCR"));

	_btnOk.setColor(Palette.blockOffset(13)+8);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnOkClick(action);
		}
	});

	_txtTitle.setColor(Palette.blockOffset(13));
	_txtTitle.setBig();
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setText(_game.getLanguage().getString("STR_INSTALLATION"));

	_lstFacilities.setColor(Palette.blockOffset(13)+5);
	_lstFacilities.setArrowColor(Palette.blockOffset(13)+8);
	_lstFacilities.setColumns(1, 105);
	_lstFacilities.setSelectable(true);
	_lstFacilities.setBackground(_window);
	_lstFacilities.setMargin(2);
	_lstFacilities.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			lstFacilitiesClick(action);
		}
	});

	_facilities.add(_game.getRuleset().getBaseFacility("STR_LIVING_QUARTERS"));
	_facilities.add(_game.getRuleset().getBaseFacility("STR_LABORATORY"));
	_facilities.add(_game.getRuleset().getBaseFacility("STR_WORKSHOP"));
	_facilities.add(_game.getRuleset().getBaseFacility("STR_GENERAL_STORES"));
	_facilities.add(_game.getRuleset().getBaseFacility("STR_ALIEN_CONTAINMENT"));
	_facilities.add(_game.getRuleset().getBaseFacility("STR_SMALL_RADAR_SYSTEM"));
	_facilities.add(_game.getRuleset().getBaseFacility("STR_LARGE_RADAR_SYSTEM"));
	_facilities.add(_game.getRuleset().getBaseFacility("STR_MISSILE_DEFENSES"));
	_facilities.add(_game.getRuleset().getBaseFacility("STR_HANGAR"));

	for (RuleBaseFacility i: _facilities)
	{
		_lstFacilities.addRow(1, _game.getLanguage().getString((i).getType()));
	}
}

/**
 * The player can change the selected base
 * or change info on other screens.
 */
public void init()
{
	_state.init();
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
 * Places the selected facility.
 * @param action Pointer to an action.
 */
public void lstFacilitiesClick(Action action)
{
	_game.pushState(new PlaceFacilityState(_game, _base, _facilities.get(_lstFacilities.getSelectedRow())));
}

}
