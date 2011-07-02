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

import putzworks.openXcom.Ruleset.RuleRegion;

public class Region
{
	private RuleRegion _rules;
	private int _activityXcom, _activityAlien;

/**
 * Initializes a region of the specified type.
 * @param rules Pointer to ruleset.
 */
public Region(RuleRegion rules)
{
	_rules = rules;
	_activityXcom = 0;
	_activityAlien = 0;

}

/**
 * Loads the region from a YAML file.
 * @param node YAML node.
 */
public void load(final YAML.Node node)
{
	node["activityXcom"] >> _activityXcom;
	node["activityAlien"] >> _activityAlien;
}

/**
 * Saves the region to a YAML file.
 * @param out YAML emitter.
 */
public final void save(YAML.Emitter out)
{
	out << YAML.BeginMap;
	out << YAML.Key << "type" << YAML.Value << _rules.getType();
	out << YAML.Key << "activityXcom" << YAML.Value << _activityXcom;
	out << YAML.Key << "activityAlien" << YAML.Value << _activityAlien;
	out << YAML.EndMap;
}

/**
 * Returns the ruleset for the region's type.
 * @return Pointer to ruleset.
 */
public final RuleRegion getRules()
{
	return _rules;
}

}
