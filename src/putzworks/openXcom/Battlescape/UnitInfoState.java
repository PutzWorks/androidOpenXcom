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
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Engine.Surface;
import putzworks.openXcom.Interface.Bar;
import putzworks.openXcom.Interface.Text;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Savegame.BattleItem;
import putzworks.openXcom.Savegame.BattleUnit;
import putzworks.openXcom.Savegame.Soldier;

public class UnitInfoState extends State
{
	private BattleUnit _unit;

	private Surface _bg;
	private Text _txtName;

	private Text _txtTimeUnits, _txtEnergy, _txtHealth, _txtFatalWounds, _txtBravery, _txtMorale, _txtReactions, _txtFiring, _txtThrowing, _txtStrength;
	private Text _numTimeUnits, _numEnergy, _numHealth, _numFatalWounds, _numBravery, _numMorale, _numReactions, _numFiring, _numThrowing, _numStrength;
	private Bar _barTimeUnits, _barEnergy, _barHealth, _barFatalWounds, _barBravery, _barMorale, _barReactions, _barFiring, _barThrowing, _barStrength;

	private Text _txtFrontArmour, _txtLeftArmour, _txtRightArmour, _txtRearArmour, _txtUnderArmour;
	private Text _numFrontArmour, _numLeftArmour, _numRightArmour, _numRearArmour, _numUnderArmour;
	private Bar _barFrontArmour, _barLeftArmour, _barRightArmour, _barRearArmour, _barUnderArmour;

/**
 * Initializes all the elements in the Soldier Info screen.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 * @param soldier ID of the selected soldier.
 */
public UnitInfoState(Game game, BattleUnit unit)
{
	super(game);
	_unit = unit;
	// Create objects
	_bg = new Surface(320, 200, 0, 0);
	_txtName = new Text(312, 192, 4, 4);

	_txtTimeUnits = new Text(120, 9, 8, 31);
	_numTimeUnits = new Text(18, 9, 150, 31);
	_barTimeUnits = new Bar(170, 7, 170, 31);

	_txtEnergy = new Text(120, 9, 8, 41);
	_numEnergy = new Text(18, 9, 150, 41);
	_barEnergy = new Bar(170, 7, 170, 41);

	_txtHealth = new Text(120, 9, 8, 51);
	_numHealth = new Text(18, 9, 150, 51);
	_barHealth = new Bar(170, 7, 170, 51);

	_txtFatalWounds = new Text(120, 9, 8, 61);
	_numFatalWounds = new Text(18, 9, 150, 61);
	_barFatalWounds = new Bar(170, 7, 170, 61);

	_txtBravery = new Text(120, 9, 8, 71);
	_numBravery = new Text(18, 9, 150, 71);
	_barBravery = new Bar(170, 7, 170, 71);

	_txtMorale = new Text(120, 9, 8, 81);
	_numMorale = new Text(18, 9, 150, 81);
	_barMorale = new Bar(170, 7, 170, 81);

	_txtReactions = new Text(120, 9, 8, 91);
	_numReactions = new Text(18, 9, 150, 91);
	_barReactions = new Bar(170, 7, 170, 91);

	_txtFiring = new Text(120, 9, 8, 101);
	_numFiring = new Text(18, 9, 150, 101);
	_barFiring = new Bar(170, 7, 170, 101);

	_txtThrowing = new Text(120, 9, 8, 111);
	_numThrowing = new Text(18, 9, 150, 111);
	_barThrowing = new Bar(170, 7, 170, 111);

	_txtStrength = new Text(120, 9, 8, 121);
	_numStrength = new Text(18, 9, 150, 121);
	_barStrength = new Bar(170, 7, 170, 121);

	// 131

	// 141
	
	_txtFrontArmour = new Text(120, 9, 8, 151);
	_numFrontArmour= new Text(18, 9, 150, 151);
	_barFrontArmour = new Bar(170, 7, 170, 151);
	
	_txtLeftArmour = new Text(120, 9, 8, 161);
	_numLeftArmour = new Text(18, 9, 150, 161);
	_barLeftArmour = new Bar(170, 7, 170, 161);
	
	_txtRightArmour = new Text(120, 9, 8, 171);
	_numRightArmour = new Text(18, 9, 150, 171);
	_barRightArmour = new Bar(170, 7, 170, 171);
	
	_txtRearArmour = new Text(120, 9, 8, 181);
	_numRearArmour = new Text(18, 9, 150, 181);
	_barRearArmour = new Bar(170, 7, 170, 181);
	
	_txtUnderArmour = new Text(120, 9, 8, 191);
	_numUnderArmour = new Text(18, 9, 150, 191);
	_barUnderArmour = new Bar(170, 7, 170, 191);
	
	add(_bg);
	add(_txtName);

	add(_txtTimeUnits);
	add(_numTimeUnits);
	add(_barTimeUnits);

	add(_txtEnergy);
	add(_numEnergy);
	add(_barEnergy);

	add(_txtHealth);
	add(_numHealth);
	add(_barHealth);

	add(_txtFatalWounds);
	add(_numFatalWounds);
	add(_barFatalWounds);

	add(_txtBravery);
	add(_numBravery);
	add(_barBravery);

	add(_txtMorale);
	add(_numMorale);
	add(_barMorale);

	add(_txtReactions);
	add(_numReactions);
	add(_barReactions);

	add(_txtFiring);
	add(_numFiring);
	add(_barFiring);

	add(_txtThrowing);
	add(_numThrowing);
	add(_barThrowing);

	add(_txtStrength);
	add(_numStrength);
	add(_barStrength);

	add(_txtFrontArmour);
	add(_numFrontArmour);
	add(_barFrontArmour);

	add(_txtLeftArmour);
	add(_numLeftArmour);
	add(_barLeftArmour);

	add(_txtRightArmour);
	add(_numRightArmour);
	add(_barRightArmour);

	add(_txtRearArmour);
	add(_numRearArmour);
	add(_barRearArmour);

	add(_txtUnderArmour);
	add(_numUnderArmour);
	add(_barUnderArmour);

	// Set up objects
	_game.getResourcePack().getSurface("UNIBORD.PCK").blit(_bg);

	_txtName.setAlign(TextHAlign.ALIGN_CENTER);
	_txtName.setBig();

	_txtTimeUnits.setColor(Palette.blockOffset(15)+1);
	_txtTimeUnits.setText(_game.getLanguage().getString("STR_TIME_UNITS"));

	_numTimeUnits.setColor(Palette.blockOffset(13));

	_barTimeUnits.setColor(Palette.blockOffset(3));
	_barTimeUnits.setScale(1.0);
	_barTimeUnits.setInvert(true);

	_txtEnergy.setColor(Palette.blockOffset(15)+1);
	_txtEnergy.setText(_game.getLanguage().getString("STR_ENERGY"));

	_numEnergy.setColor(Palette.blockOffset(13));

	_barEnergy.setColor(Palette.blockOffset(9));
	_barEnergy.setScale(1.0);
	_barEnergy.setInvert(true);

	_txtHealth.setColor(Palette.blockOffset(15)+1);
	_txtHealth.setText(_game.getLanguage().getString("STR_HEALTH"));

	_numHealth.setColor(Palette.blockOffset(13));

	_barHealth.setColor(Palette.blockOffset(2));
	_barHealth.setScale(1.0);
	_barHealth.setInvert(true);

	_txtFatalWounds.setColor(Palette.blockOffset(15)+1);
	_txtFatalWounds.setText(_game.getLanguage().getString("STR_FATAL_WOUNDS"));

	_numFatalWounds.setColor(Palette.blockOffset(13));

	_barFatalWounds.setColor(Palette.blockOffset(2));
	_barFatalWounds.setScale(1.0);
	_barFatalWounds.setInvert(true);

	_txtBravery.setColor(Palette.blockOffset(15)+1);
	_txtBravery.setText(_game.getLanguage().getString("STR_BRAVERY"));

	_numBravery.setColor(Palette.blockOffset(13));

	_barBravery.setColor(Palette.blockOffset(4));
	_barBravery.setScale(1.0);
	_barBravery.setInvert(true);

	_txtMorale.setColor(Palette.blockOffset(15)+1);
	_txtMorale.setText(_game.getLanguage().getString("STR_MORALE"));

	_numMorale.setColor(Palette.blockOffset(13));

	_barMorale.setColor(Palette.blockOffset(4));
	_barMorale.setScale(1.0);
	_barMorale.setInvert(true);

	_txtReactions.setColor(Palette.blockOffset(15)+1);
	_txtReactions.setText(_game.getLanguage().getString("STR_REACTIONS"));

	_numReactions.setColor(Palette.blockOffset(13));

	_barReactions.setColor(Palette.blockOffset(1));
	_barReactions.setScale(1.0);
	_barReactions.setInvert(true);

	_txtFiring.setColor(Palette.blockOffset(15)+1);
	_txtFiring.setText(_game.getLanguage().getString("STR_FIRING_ACCURACY"));

	_numFiring.setColor(Palette.blockOffset(13));

	_barFiring.setColor(Palette.blockOffset(8));
	_barFiring.setScale(1.0);
	_barFiring.setInvert(true);

	_txtThrowing.setColor(Palette.blockOffset(15)+1);
	_txtThrowing.setText(_game.getLanguage().getString("STR_THROWING_ACCURACY"));

	_numThrowing.setColor(Palette.blockOffset(13));

	_barThrowing.setColor(Palette.blockOffset(10));
	_barThrowing.setScale(1.0);
	_barThrowing.setInvert(true);

	_txtStrength.setColor(Palette.blockOffset(15)+1);
	_txtStrength.setText(_game.getLanguage().getString("STR_STRENGTH"));

	_numStrength.setColor(Palette.blockOffset(13));

	_barStrength.setColor(Palette.blockOffset(5));
	_barStrength.setScale(1.0);
	_barStrength.setInvert(true);

	_txtFrontArmour.setText(_game.getLanguage().getString("STR_FRONT_ARMOR_UC"));

	_barFrontArmour.setColor(Palette.blockOffset(5));
	_barFrontArmour.setScale(1.0);
	_barFrontArmour.setInvert(true);

	_txtLeftArmour.setText(_game.getLanguage().getString("STR_LEFT_ARMOR_UC"));

	_barLeftArmour.setColor(Palette.blockOffset(5));
	_barLeftArmour.setScale(1.0);
	_barLeftArmour.setInvert(true);

	_txtRightArmour.setText(_game.getLanguage().getString("STR_RIGHT_ARMOR_UC"));

	_barRightArmour.setColor(Palette.blockOffset(5));
	_barRightArmour.setScale(1.0);
	_barRightArmour.setInvert(true);

	_txtRearArmour.setText(_game.getLanguage().getString("STR_REAR_ARMOR_UC"));

	_barRearArmour.setColor(Palette.blockOffset(5));
	_barRearArmour.setScale(1.0);
	_barRearArmour.setInvert(true);

	_txtUnderArmour.setText(_game.getLanguage().getString("STR_UNDER_ARMOR_UC"));

	_barUnderArmour.setColor(Palette.blockOffset(5));
	_barUnderArmour.setScale(1.0);
	_barUnderArmour.setInvert(true);

}

/**
 * The soldier names can change
 * after going into other screens.
 */
public void init()
{
	Soldier soldier = (Soldier)(_unit.getUnit());

	WStringstream ss;
	ss << _unit.getTimeUnits();
	_numTimeUnits.setText(ss.str());		
	_barTimeUnits.setMax(_unit.getUnit().getTimeUnits());
	_barTimeUnits.setValue(_unit.getTimeUnits());

	ss.str(L"");
	if (soldier != null)
	{
		ss << _game.getLanguage().getString(soldier.getRankString());
		ss << " ";
	}
	ss << _unit.getUnit().getName();
	_txtName.setText(ss.str());

	ss.str(L"");
	ss << _unit.getEnergy();
	_numEnergy.setText(ss.str());		
	_barEnergy.setMax(_unit.getUnit().getStamina());
	_barEnergy.setValue(_unit.getEnergy());

	ss.str(L"");
	ss << _unit.getHealth();
	_numHealth.setText(ss.str());		
	_barHealth.setMax(_unit.getUnit().getHealth());
	_barHealth.setValue(_unit.getHealth());

	ss.str(L"");
	ss << _unit.getFatalWounds();
	_numFatalWounds.setText(ss.str());		
	_barFatalWounds.setMax(_unit.getFatalWounds());
	_barFatalWounds.setValue(_unit.getFatalWounds());

	ss.str(L"");
	ss << _unit.getUnit().getBravery();
	_numBravery.setText(ss.str());		
	_barBravery.setMax(_unit.getUnit().getBravery());
	_barBravery.setValue(_unit.getUnit().getBravery());

	ss.str(L"");
	ss << _unit.getMorale();
	_numMorale.setText(ss.str());		
	_barMorale.setMax(100);
	_barMorale.setValue(_unit.getMorale());

	ss.str(L"");
	ss << _unit.getUnit().getReactions();
	_numReactions.setText(ss.str());		
	_barReactions.setMax(_unit.getUnit().getReactions());
	_barReactions.setValue(_unit.getUnit().getReactions());

	ss.str(L"");
	ss << _unit.getUnit().getFiringAccuracy();
	_numFiring.setText(ss.str());		
	_barFiring.setMax(_unit.getUnit().getFiringAccuracy());
	_barFiring.setValue(_unit.getUnit().getFiringAccuracy());

	ss.str(L"");
	ss << _unit.getUnit().getThrowingAccuracy();
	_numThrowing.setText(ss.str());		
	_barThrowing.setMax(_unit.getUnit().getThrowingAccuracy());
	_barThrowing.setValue(_unit.getUnit().getThrowingAccuracy());

	ss.str(L"");
	ss << _unit.getUnit().getStrength();
	_numStrength.setText(ss.str());		
	_barStrength.setMax(_unit.getUnit().getStrength());
	_barStrength.setValue(_unit.getUnit().getStrength());

	ss.str(L"");
	ss << _unit.getArmor(BattleUnit.UnitSide.SIDE_FRONT);
	_numFrontArmour.setText(ss.str());		
	_barFrontArmour.setMax(_unit.getUnit().getArmor().getFrontArmor());
	_barFrontArmour.setValue(_unit.getArmor(BattleUnit.UnitSide.SIDE_FRONT));

	ss.str(L"");
	ss << _unit.getArmor(BattleUnit.UnitSide.SIDE_LEFT);
	_numLeftArmour.setText(ss.str());		
	_barLeftArmour.setMax(_unit.getUnit().getArmor().getSideArmor());
	_barLeftArmour.setValue(_unit.getArmor(BattleUnit.UnitSide.SIDE_LEFT));

	ss.str(L"");
	ss << _unit.getArmor(BattleUnit.UnitSide.SIDE_RIGHT);
	_numRightArmour.setText(ss.str());		
	_barRightArmour.setMax(_unit.getUnit().getArmor().getSideArmor());
	_barRightArmour.setValue(_unit.getArmor(BattleUnit.UnitSide.SIDE_RIGHT));

	ss.str(L"");
	ss << _unit.getArmor(BattleUnit.UnitSide.SIDE_REAR);
	_numRearArmour.setText(ss.str());		
	_barRearArmour.setMax(_unit.getUnit().getArmor().getRearArmor());
	_barRearArmour.setValue(_unit.getArmor(BattleUnit.UnitSide.SIDE_REAR));

	ss.str(L"");
	ss << _unit.getArmor(BattleUnit.UnitSide.SIDE_UNDER);
	_numUnderArmour.setText(ss.str());		
	_barUnderArmour.setMax(_unit.getUnit().getArmor().getUnderArmor());
	_barUnderArmour.setValue(_unit.getArmor(BattleUnit.UnitSide.SIDE_UNDER));
}


/**
 * Takes care of any events from the core game engine.
 * @param action Pointer to an action.
 */
public void handle(Action action)
{
	if (action.getDetails().button.button == SDL_BUTTON_RIGHT)
	{
		_game.popState();
		super.handle(action);
	}
}

}
