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

import putzworks.openXcom.Engine.Language;
import putzworks.openXcom.Ruleset.RuleAlien;
import putzworks.openXcom.Ruleset.RuleArmor;

public class Alien extends Unit
{
	private RuleAlien _rules;
	private Language _lang;

/**
 * Initializes a new blank soldier.
 */
public Alien()
{
}

/**
 * Initializes a new soldier with random stats and a name
 * pulled from a set of SoldierNamePool's.
 * @param names List of name pools.
 */
public Alien(RuleAlien rules, RuleArmor armor, Language lang)
{
	super(armor);
	_rules = rules;
	_lang = lang;

}

/**
 * Returns the Alien's full name.
 * @return Soldier name.
 */
public final String getName()
{
	StringBuffer name = new StringBuffer();
	name.append(_lang.getString(_rules.getRace()));
	name.append(_lang.getString(_rules.getRank()));
	return name.toString();
}

/**
 * Returns the soldier's amount of time units.
 * @return Time units.
 */
public final int getTimeUnits()
{
	return _rules.getStats().tu;
}

/**
 * Returns the soldier's amount of stamina.
 * @return Stamina.
 */
public final int getStamina()
{
	return _rules.getStats().stamina;
}

/**
 * Returns the soldier's amount of health.
 * @return Health.
 */
public final int getHealth()
{
	return _rules.getStats().health;
}

/**
 * Returns the soldier's amount of bravery.
 * @return Bravery.
 */
public final int getBravery()
{
	return _rules.getStats().bravery;
}

/**
 * Returns the soldier's amount of reactions.
 * @return Reactions.
 */
public final int getReactions()
{
	return _rules.getStats().reactions;
}

/**
 * Returns the soldier's amount of firing accuracy.
 * @return Firing accuracy.
 */
public final int getFiringAccuracy()
{
	return _rules.getStats().firing;
}

/**
 * Returns the soldier's amount of throwing accuracy.
 * @return Throwing accuracy.
 */
public final int getThrowingAccuracy()
{
	return _rules.getStats().throwing;
}

/**
 * Returns the soldier's amount of strength.
 * @return Strength.
 */
public final int getStrength()
{
	return _rules.getStats().strength;
}

/**
 * Returns the soldier's rules.
 * @return rulealien
 */
public final RuleAlien getRules() 
{
	return _rules;
}

/**
 * Returns the soldier's stand height.
 * @return stand height
 */ 
public final int getStandHeight() 
{
	return _rules.getStandHeight();
}

/**
 * Returns the soldier's kneel height.
 * @return kneel height
 */
public final int getKneelHeight() 
{
	return _rules.getKneelHeight();
}
/**
 * Returns the soldier's loftemps ID.
 * @return loftemps ID
 */
public final int gotLoftemps()
{
	return _rules.gotLoftemps();
}

}
