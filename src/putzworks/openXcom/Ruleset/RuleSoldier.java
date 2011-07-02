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
package putzworks.openXcom.Ruleset;

import putzworks.openXcom.Savegame.Unit.UnitStats;

public class RuleSoldier
{
	private String _type;
	private UnitStats _minStats;
	private UnitStats _maxStats;
	private String _armor;
	private int _standHeight, _kneelHeight, _loftemps;

/**
 * Creates a blank ruleunit for a certain
 * type of craft.
 * @param type String defining the type.
 */
public RuleSoldier(String type)
{
	_type = type;
}

/**
 * Returns the language string that names
 * this unit. Each unit type has a unique name.
 * @return Unit name.
 */
public final String getType()
{
	return _type;
}

public void setStats(UnitStats minStats, UnitStats maxStats)
{
	_minStats = minStats;
	_maxStats = maxStats;
}


public void setArmor(String armor)
{
	_armor = armor;
}

public void setVoxelParameters(int standHeight, int kneelHeight, int loftemps)
{
	_standHeight = standHeight;
	_kneelHeight = kneelHeight;
	_loftemps = loftemps;
}

public UnitStats getMinStats()
{
	return _minStats;
}
/// 
public UnitStats getMaxStats()
{
	return _maxStats;
}
/// 
public int getStandHeight()
{
	return _standHeight;
}
/// 
public int getKneelHeight()
{
	return _kneelHeight;
}
/// 
public int getLoftemps()
{
	return _loftemps;
}

public final String getArmor()
{
	return _armor;
}

}
