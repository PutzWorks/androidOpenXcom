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

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Ruleset.RuleBaseFacility;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.BaseFacility;

public class PlaceFacilityState extends State
{
	private Base _base;
	private RuleBaseFacility _rule;

	private BaseView _view;
	private TextButton _btnCancel;
	private Window _window;
	private Text _txtFacility, _txtCost, _numCost, _txtTime, _numTime, _txtMaintenance, _numMaintenance;

/**
 * Initializes all the elements in the Place Facility window.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 * @param rule Pointer to the facility ruleset to build.
 */
public PlaceFacilityState(Game game, Base base, RuleBaseFacility rule)
{
	super(game);
	_base = base;
	_rule = rule;
	_screen = false;

	// Create objects
	_window = new Window(this, 128, 160, 192, 40);
	_view = new BaseView(192, 192, 0, 8);
	_btnCancel = new TextButton(112, 16, 200, 176);
	_txtFacility = new Text(110, 9, 202, 50);
	_txtCost = new Text(110, 9, 202, 62);
	_numCost = new Text(110, 16, 202, 70);
	_txtTime = new Text(110, 9, 202, 90);
	_numTime = new Text(110, 16, 202, 98);
	_txtMaintenance = new Text(110, 9, 202, 118);
	_numMaintenance = new Text(110, 16, 202, 126);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(6)), Palette.backPos, 16);

	add(_window);
	add(_view);
	add(_btnCancel);
	add(_txtFacility);
	add(_txtCost);
	add(_numCost);
	add(_txtTime);
	add(_numTime);
	add(_txtMaintenance);
	add(_numMaintenance);

	// Set up objects
	_window.setColor(Palette.blockOffset(13)+13);
	_window.setBackground(_game.getResourcePack().getSurface("BACK01.SCR"));
	
	_view.setFonts(_game.getResourcePack().getFont("BIGLETS.DAT"), _game.getResourcePack().getFont("SMALLSET.DAT"));
	_view.setTexture(_game.getResourcePack().getSurfaceSet("BASEBITS.PCK"));
	_view.setBase(_base);
	_view.setSelectable(rule.getSize());
	_view.onMouseClick((ActionHandler)PlaceFacilityState.viewClick);

	_btnCancel.setColor(Palette.blockOffset(13)+13);
	_btnCancel.setText(_game.getLanguage().getString("STR_CANCEL"));
	_btnCancel.onMouseClick((ActionHandler)PlaceFacilityState.btnCancelClick);

	_txtFacility.setColor(Palette.blockOffset(13)+10);
	_txtFacility.setText(_game.getLanguage().getString(_rule.getType()));

	_txtCost.setColor(Palette.blockOffset(13)+10);
	_txtCost.setText(_game.getLanguage().getString("STR_COST_UC"));

	_numCost.setColor(Palette.blockOffset(13));
	_numCost.setBig();
	_numCost.setText(Text.formatFunding(_rule.getBuildCost()));

	_txtTime.setColor(Palette.blockOffset(13)+10);
	_txtTime.setText(_game.getLanguage().getString("STR_CONSTRUCTION_TIME_UC"));

	_numTime.setColor(Palette.blockOffset(13));
	_numTime.setBig();
	WStringstream ss;
	ss << _rule.getBuildTime() << _game.getLanguage().getString("STR_DAYS");
	_numTime.setText(ss.str());

	_txtMaintenance.setColor(Palette.blockOffset(13)+10);
	_txtMaintenance.setText(_game.getLanguage().getString("STR_MAINTENANCE_UC"));

	_numMaintenance.setColor(Palette.blockOffset(13));
	_numMaintenance.setBig();
	_numMaintenance.setText(Text.formatFunding(_rule.getMonthlyCost()));
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
 * Processes clicking on facilities.
 * @param action Pointer to an action.
 */
public void viewClick(Action action)
{
	if (!_view.isPlaceable(_rule))
	{
		_game.popState();
		_game.pushState(new BasescapeErrorState(_game, "STR_CANNOT_BUILD_HERE"));
	}
	else if (_game.getSavedGame().getFunds() < _rule.getBuildCost())
	{
		_game.popState();
		_game.pushState(new BasescapeErrorState(_game, "STR_NOT_ENOUGH_MONEY"));
	}
	else
	{
		BaseFacility fac = new BaseFacility(_rule, _base, _view.getGridX(), _view.getGridY());
		fac.setBuildTime(_rule.getBuildTime());
		_base.getFacilities().add(fac);
		_game.getSavedGame().setFunds(_game.getSavedGame().getFunds() - _rule.getBuildCost());
		_game.popState();
	}
}

}
