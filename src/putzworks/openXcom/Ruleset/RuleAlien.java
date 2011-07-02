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

public class RuleAlien
{
	private String _type;
	private String  _race;
	private String  _rank;
	private UnitStats _stats;
	private String  _armor;
	private int _standHeight, _kneelHeight, _loftemps;

/**
 * Creates a blank ruleunit for a certain
 * type of craft.
 * @param type String defining the type.
 */
public RuleAlien(String type, String race, String rank)
{
	_type = type;
	_race = race;
	_rank = rank;
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

public void setStats(UnitStats stats)
{
	_stats = stats;
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

public UnitStats getStats()
{
	return _stats;
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
public int gotLoftemps()
{
	return _loftemps;
}

public final String getArmor()
{
	return _armor;
}

public final String getRace()
{
	return _race;
}

public final String getRank()
{
	return _rank;
}

}
