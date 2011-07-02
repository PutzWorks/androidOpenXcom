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

import java.util.HashMap;
import java.util.Vector;

import putzworks.openXcom.Savegame.SavedGame;
import putzworks.openXcom.Savegame.SavedGame.GameDifficulty;

public class Ruleset
{
	protected Vector<SoldierNamePool> _names;
	protected HashMap<String, RuleCountry> _countries;
	protected HashMap<String, RuleRegion> _regions;
	protected HashMap<String, RuleBaseFacility> _facilities;
	protected HashMap<String, RuleCraft> _crafts;
	protected HashMap<String, RuleCraftWeapon> _craftWeapons;
	protected HashMap<String, RuleItem> _items;
	protected HashMap<String, RuleUfo> _ufos;
	protected HashMap<String, RuleTerrain> _terrains;
	protected HashMap<String, MapDataSet> _mapDataFiles;
	protected HashMap<String, RuleSoldier> _soldiers;
	protected HashMap<String, RuleAlien> _aliens;
	protected HashMap<String, RuleArmor> _armors;
	protected HashMap<String, ArticleDefinition> _ufopaediaArticles;
	protected int _costSoldier, _costEngineer, _costScientist, _timePersonnel;

/**
 * Creates a ruleset with blank sets of rules.
 */
public Ruleset()
{
	_names = new Vector<SoldierNamePool>();
	_countries = new HashMap<String, RuleCountry>();
	_regions = new HashMap<String, RuleRegion>();
	_facilities = new HashMap<String, RuleBaseFacility>();
	_crafts = new HashMap<String, RuleCraft>();
	_craftWeapons = new HashMap<String, RuleCraftWeapon>();
	_items = new HashMap<String, RuleItem>();
	_ufos = new HashMap<String, RuleUfo>();
	_terrains = new HashMap<String, RuleTerrain>();
	_mapDataFiles = new HashMap<String, MapDataSet>();
	_soldiers = new HashMap<String, RuleSoldier>();
	_aliens = new HashMap<String, RuleAlien>();
	_costSoldier = 0;
	_costEngineer = 0; 
	_costScientist = 0;

}

/**
 * Generates a brand new blank saved game.
 * @param diff Difficulty for the save.
 * @return New saved game.
 */
public SavedGame newSave(GameDifficulty diff)
{
	SavedGame save = new SavedGame(diff);

	return save;
}

/**
 * Returns the list of soldier name pools.
 * @return Pointer to soldier name pool list.
 */
public Vector<SoldierNamePool> getPools()
{
	return _names;
}

/**
 * Returns the rules for the specified country.
 * @param id Country type.
 * @return Rules for the country.
 */
public RuleCountry  getCountry(String id)
{
	return _countries.get(id);
}

/**
 * Returns the rules for the specified region.
 * @param id Region type.
 * @return Rules for the region.
 */
public RuleRegion getRegion(String id)
{
	return _regions.get(id);
}

/**
 * Returns the rules for the specified base facility.
 * @param id Facility type.
 * @return Rules for the facility.
 */
public RuleBaseFacility getBaseFacility(String id)
{
	return _facilities.get(id);
}

/**
 * Returns the rules for the specified craft.
 * @param id Craft type.
 * @return Rules for the craft.
 */
public RuleCraft getCraft(String id)
{
	return _crafts.get(id);
}

/**
 * Returns the rules for the specified craft weapon.
 * @param id Craft weapon type.
 * @return Rules for the craft weapon.
 */
public RuleCraftWeapon getCraftWeapon(String id)
{
	return _craftWeapons.get(id);
}
/**
 * Returns the rules for the specified item.
 * @param id Item type.
 * @return Rules for the item.
 */
public RuleItem getItem(String id)
{
	return _items.get(id);
}

/**
 * Returns the rules for the specified UFO.
 * @param id UFO type.
 * @return Rules for the UFO.
 */
public RuleUfo getUfo(String id)
{
	return _ufos.get(id);
}

/**
 * Returns the rules for the specified terrain.
 * @param name terrain name.
 * @return Rules for the terrain.
 */
public RuleTerrain getTerrain(String name)
{
	return _terrains.get(name);
}

/**
 * Returns the info about a specific map data file
 * @param name datafile name.
 * @return Rules for the datafile.
 */
public MapDataSet getMapDataSet(String name)
{
	return _mapDataFiles.get(name);
}

/**
 * Returns the info about a specific unit
 * @param name Unit name.
 * @return Rules for the units.
 */
public RuleSoldier getSoldier(String name)
{
	return _soldiers.get(name);
}

/**
 * Returns the info about a specific unit
 * @param name Unit name.
 * @return Rules for the units.
 */
public RuleAlien getAlien(String name)
{
	return _aliens.get(name);
}

/**
 * Returns the info about a specific armor
 * @param name Armor name.
 * @return Rules for the armor.
 */
public RuleArmor getArmor(String name)
{
	return _armors.get(name);
}

/**
 * Returns the cost of an individual soldier
 * for purchase/maintenance.
 * @return Cost.
 */
public final int getSoldierCost()
{
	return _costSoldier;
}

/**
 * Returns the cost of an individual engineer
 * for purchase/maintenance.
 * @return Cost.
 */
public final int getEngineerCost()
{
	return _costEngineer;
}

/**
 * Returns the cost of an individual scientist
 * for purchase/maintenance.
 * @return Cost.
 */
public final int getScientistCost()
{
	return _costScientist;
}

/**
 * Returns the time it takes to transfer personnel
 * between bases.
 * @return Time in hours.
 */
public final int getPersonnelTime()
{
	return _timePersonnel;
}

/**
 * Returns the article definition for a given name.
 * @param name Article name.
 * @return Article definition.
 */
public ArticleDefinition getUfopaediaArticle(String name)
{
	return _ufopaediaArticles.get(name);
}

}
