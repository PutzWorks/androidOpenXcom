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

import putzworks.openXcom.Ruleset.RuleBaseFacility;

public class BaseFacility
{
	private RuleBaseFacility _rules;
	private Base _base;
	private int _x, _y, _buildTime;

/**
 * Initializes a base facility of the specified type.
 * @param rules Pointer to ruleset.
 * @param base Pointer to base of origin.
 * @param x X position in grid squares.
 * @param y Y position in grid squares.
 */
public BaseFacility(RuleBaseFacility rules, Base base, int x, int y)
{
	_rules = rules;
	_base = base;
	_x = x;
	_y = y;
	_buildTime = 0;

}

/**
 * Loads the base facility from a YAML file.
 * @param node YAML node.
 */
public void load(final YAML.Node node)
{
	node["x"] >> _x;
	node["y"] >> _y;
	node["buildTime"] >> _buildTime;
}

/**
 * Saves the base facility to a YAML file.
 * @param out YAML emitter.
 */
public final void save(YAML.Emitter out)
{
	out << YAML.BeginMap;
	out << YAML.Key << "type" << YAML.Value << _rules.getType();
	out << YAML.Key << "x" << YAML.Value << _x;
	out << YAML.Key << "y" << YAML.Value << _y;
	out << YAML.Key << "buildTime" << YAML.Value << _buildTime;
	out << YAML.EndMap;
}

/**
 * Returns the ruleset for the base facility's type.
 * @return Pointer to ruleset.
 */
public final RuleBaseFacility getRules()
{
	return _rules;
}

/**
 * Returns the base facility's X position on the
 * base grid that it's placed on.
 * @return X position in grid squares.
 */
public final int getX()
{
	return _x;
}

/**
 * Returns the base facility's Y position on the
 * base grid that it's placed on.
 * @return Y position in grid squares.
 */
public final int getY()
{
	return _y;
}

/**
 * Returns the base facility's remaining time
 * until it's finished building (0 = complete).
 * @return Time left in days.
 */
public final int getBuildTime()
{
	return _buildTime;
}

/**
 * Changes the base facility's remaining time
 * until it's finished building.
 * @param time Time left in days.
 */
public void setBuildTime(int time)
{
	_buildTime = time;
}

/**
 * Handles the facility building every day.
 */
public void build()
{
	_buildTime--;
}

/**
 * Returns if a certain target is covered by the facility's
 * radar range, taking in account the positions of both.
 * @param target Pointer to target to compare.
 * @return True if it's within range, False otherwise.
 */
public final boolean insideRadarRange(Target target)
{
	if (_rules.getRadarRange() == 0)
		return false;

	boolean inside = false;
	double newrange = _rules.getRadarRange() * (1 / 60.0) * (Math.PI / 180);
	for (double lon = target.getLongitude() - 2*Math.PI; lon <= target.getLongitude() + 2*Math.PI; lon += 2*Math.PI)
	{
		double dLon = lon - _base.getLongitude();
		double dLat = target.getLatitude() - _base.getLatitude();
		inside = inside || (dLon * dLon + dLat * dLat <= newrange * newrange);
	}
    return inside;
}

/**
 * Returns if this facility is currently being
 * used by its base.
 * @return True if it's under use, False otherwise.
 */
public final boolean inUse()
{
	if (_buildTime > 0)
	{
		return false;
	}
	return (_base.getAvailableQuarters() - _rules.getPersonnel() < _base.getUsedQuarters() ||
			_base.getAvailableStores() - _rules.getStorage() < _base.getUsedStores() ||
			_base.getAvailableLaboratories() - _rules.getLaboratories() < _base.getUsedLaboratories() ||
			_base.getAvailableWorkshops() - _rules.getWorkshops() < _base.getUsedWorkshops() ||
			_base.getAvailableHangars() - _rules.getCrafts() < _base.getUsedHangars());
}

}
