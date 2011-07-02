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

import putzworks.openXcom.Engine.RNG;
import putzworks.openXcom.Ruleset.RuleCountry;

public class Country
{
	private RuleCountry _rules;
	private int _funding, _change;
	private int _activityXcom, _activityAlien;

/**
 * Initializes a country of the specified type.
 * @param rules Pointer to ruleset.
 * @param gen Generate new funding.
 */
public Country(RuleCountry rules, boolean gen)
{
	_rules = rules;
	_funding = 0;
	_change = 0;
	_activityXcom = 0; 
	_activityAlien = 0;

	if (gen)
	{
		_funding = RNG.generate(rules.getMinFunding(), rules.getMaxFunding()) * 1000;
	}
}

/**
 * Loads the country from a YAML file.
 * @param node YAML node.
 */
public void load(final YAML.Node node)
{
	node["funding"] >> _funding;
	node["change"] >> _change;
	node["activityXcom"] >> _activityXcom;
	node["activityAlien"] >> _activityAlien;
}

/**
 * Saves the country to a YAML file.
 * @param out YAML emitter.
 */
public final void save(YAML.Emitter out)
{
	out << YAML.BeginMap;
	out << YAML.Key << "type" << YAML.Value << _rules.getType();
	out << YAML.Key << "funding" << YAML.Value << _funding;
	out << YAML.Key << "change" << YAML.Value << _change;
	out << YAML.Key << "activityXcom" << YAML.Value << _activityXcom;
	out << YAML.Key << "activityAlien" << YAML.Value << _activityAlien;
	out << YAML.EndMap;
}

/**
 * Returns the ruleset for the country's type.
 * @return Pointer to ruleset.
 */
public final RuleCountry getRules()
{
	return _rules;
}

/**
 * Returns the country's current monthly funding.
 * @return Monthly funding.
 */
public final int getFunding()
{
	return _funding;
}

/**
 * Changes the country's current monthly funding.
 * @param funding Monthly funding.
 */
public void setFunding(int funding)
{
	_funding = funding;
}

/**
 * Returns the country's funding change since last month.
 * @return Funding change.
 */
public final int getChange()
{
	return _change;
}

}
