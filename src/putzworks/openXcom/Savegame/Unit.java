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
package putzworks.openXcom.Savegame;

import putzworks.openXcom.Ruleset.RuleArmor;


public abstract class Unit
{
public class UnitStats
{
	public int tu, stamina, health, bravery, reactions, firing, throwing, strength, psiStrength,
			psiSkill, melee;
};

	private RuleArmor _armor;

/**
 * Initializes a new blank unit.
 */
public Unit()
{
	_armor = null;
}

/**
 * Initializes a new unit with rules and armor.
 * @param rules
 * @param armor
 */
public Unit(RuleArmor armor)
{
	_armor = armor;
}

/**
 * Returns the unit's armor.
 * @return rules.
 */
public final RuleArmor getArmor()
{
	return _armor;
}

public abstract String getName(); //was a String
/// Gets the unit's time units.
public abstract int getTimeUnits();
/// Gets the unit's stamina.
public abstract int getStamina();
/// Gets the unit's health.
public abstract int getHealth();
/// Gets the unit's bravery.
public abstract int getBravery();
/// Gets the unit's reactions.
public abstract int getReactions();
/// Gets the unit's firing accuracy.
public abstract int getFiringAccuracy();
/// Gets the unit's throwing accuracy.
public abstract int getThrowingAccuracy();
/// Gets the unit's strength.
public abstract int getStrength();
/// Get the unit's stand height.
public abstract int getStandHeight();
/// Get the unit's kneel height.
public abstract int getKneelHeight();
/// Get the unit's loft ID.
public abstract int gotLoftemps();

}
