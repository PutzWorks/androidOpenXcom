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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Vector;

import putzworks.openXcom.Engine.Language;
import putzworks.openXcom.Engine.RNG;
import putzworks.openXcom.Interface.TextList;
import putzworks.openXcom.Ruleset.Ruleset;
import putzworks.openXcom.Savegame.Soldier.SoldierRank;

public class SavedGame
{
	public static final String USER_DIR = "./USER/";
	
	public enum GameDifficulty { DIFF_BEGINNER(0), DIFF_EXPERIENCED(1), DIFF_VETERAN(2), DIFF_GENIUS(3), DIFF_SUPERHUMAN(4) ;
		private int id;
		private GameDifficulty(int i){
			this.id = i;
		}
	     public static GameDifficulty get(int code) { 
	    	 for(GameDifficulty s : EnumSet.allOf(GameDifficulty.class)){
	               if (s.id == code){return s;}
	    	 }
	    	 return null;
	     }
	}

	private GameDifficulty _difficulty;
	private GameTime _time;
	private int _funds;
	private Vector<Country> _countries;
	private Vector<Region> _regions;
	private Vector<Base> _bases;
	private Vector<Ufo> _ufos;
	private HashMap<String, Integer> _craftId;
	private Vector<Waypoint> _waypoints;
	private int _ufoId, _waypointId;
	private SavedBattleGame _battleGame;
	private UfopaediaSaved _ufopaedia;


/**
 * Initializes a brand new saved game according to the specified difficulty.
 * @param difficulty Game difficulty.
 */
public SavedGame(GameDifficulty difficulty)
{
	_difficulty = difficulty;
	_funds = 0; 
	_countries = new Vector<Country>();
	_regions = new Vector<Region>();
	_bases = new Vector<Base>(); 
	_ufos = new Vector<Ufo>();
	_craftId = new HashMap<String, Integer>();
	_waypoints = new Vector<Waypoint>();
	_ufoId = 1;
	_waypointId = 1;
	_battleGame = new SavedBattleGame();

	RNG.init(-1);
	_time = new GameTime(6, 1, 1, 1999, 12, 0, 0);
	_ufopaedia = new UfopaediaSaved();
}

/**
 * Gets all the saves found in the user folder
 * and adds them to a text list.
 * @param list Text list.
 * @param lang Loaded language.
 */
static public void getList(TextList list, Language lang)
{
	DIR dp = opendir(USER_DIR);
    if (dp == 0)
	{
        throw new Exception("Failed to open saves directory");
    }

    struct dirent dirp;
    while ((dirp = readdir(dp)) != 0)
	{
		String file = dirp.d_name;
		// Check if it's a valid save
		if (file.find(".sav") == String.npos)
		{
			continue;
		}
		String fullname = USER_DIR + file;
		std.ifstream fin(fullname.c_str());
		if (!fin)
		{
		    closedir(dp);
			throw Exception("Failed to load savegame");
		}
		YAML.Parser parser(fin);
		YAML.Node doc;

		parser.GetNextDocument(doc);
		GameTime time = GameTime(6, 1, 1, 1999, 12, 0, 0);
		time.load(doc["time"]);
		StringBuffer saveTime = new StringBuffer();
		StringBuffer saveDay = new StringBuffer(), saveMonth = new StringBuffer(), saveYear = new StringBuffer();
		saveTime.append(time.getHour() + ":" + std.setfill('0') + std.setw(2) + time.getMinute());
		saveDay.append(time.getDay() + lang.getString(time.getDayString()));
		saveMonth.append(lang.getString(time.getMonthString()));
		saveYear.append(time.getYear());
		list.addRow(5, Language.utf8ToWstr(file.substr(0, file.length()-4)).c_str(), Language.utf8ToWstr(saveTime.str()).c_str(), saveDay.str().c_str(), saveMonth.str().c_str(), saveYear.str().c_str());
		fin.close();
    }
    closedir(dp);
}

/**
 * Loads a saved game's contents from a YAML file.
 * @note Assumes the saved game is blank.
 * @param filename YAML filename.
 * @param rule Ruleset for the saved game.
 */
public void load(final String filename, Ruleset rule)
{
	int size = 0;

	String s = USER_DIR + filename + ".sav";
	std.ifstream fin(s);
	if (!fin)
	{
		throw Exception("Failed to load savegame");
	}
    YAML.Parser parser(fin);
	YAML.Node doc;

	// Get brief save info
    parser.GetNextDocument(doc);
	String v;
	doc["version"] >> v;
	if (v != "0.2")
	{
		throw Exception("Version mismatch");
	}
	_time.load(doc["time"]);

	// Get full save data
	parser.GetNextDocument(doc);
	int a = 0;
	doc["difficulty"] >> a;
	_difficulty = GameDifficulty.get(a);
	doc["funds"] >> _funds;

	size = doc["countries"].size();
	for (unsigned int i = 0; i < size; i++)
	{
		String type;
		doc["countries"][i]["type"] >> type;
		Country c = new Country(rule.getCountry(type), false);
		c.load(doc["countries"][i]);
		_countries.add(c);
	}

	size = doc["regions"].size();
	for (int i = 0; i < size; i++)
	{
		String type;
		doc["regions"][i]["type"] >> type;
		Region r = new Region(rule.getRegion(type));
		r.load(doc["regions"][i]);
		_regions.add(r);
	}

	size = doc["ufos"].size();
	for (int i = 0; i < size; i++)
	{
		String type;
		doc["ufos"][i]["type"] >> type;
		Ufo u = new Ufo(rule.getUfo(type));
		u.load(doc["ufos"][i]);
		_ufos.add(u);
	}

	doc["craftId"] >> _craftId;

	size = doc["waypoints"].size();
	for (int i = 0; i < size; i++)
	{
		Waypoint w = new Waypoint();
		w.load(doc["waypoints"][i]);
		_waypoints.add(w);
	}

	doc["ufoId"] >> _ufoId;
	doc["waypointId"] >> _waypointId;

	size = doc["bases"].size();
	for (int i = 0; i < size; i++)
	{
		Base b = new Base(rule);
		b.load(doc["bases"][i], this);
		_bases.add(b);
	}

	if (YAML.Node pName = doc.FindValue("battleGame"))
	{
		_battleGame = new SavedBattleGame();
		_battleGame.load(pName);
	}

	fin.close();
}

/**
 * Saves a saved game's contents to a YAML file.
 * @param filename YAML filename.
 */
public final void save(final String filename)
{
	String s = USER_DIR + filename + ".sav";
	std.ofstream sav(s);
	if (!sav)
	{
		throw Exception("Failed to save savegame");
	}

	YAML.Emitter out;

	// Saves the brief game info used in the saves list
	out << YAML.BeginDoc;
	out << YAML.BeginMap;
	out << YAML.Key << "version" << YAML.Value << "0.2";
	out << YAML.Key << "time" << YAML.Value;
	_time.save(out);
	out << YAML.EndMap;

	// Saves the full game data to the save
	out << YAML.BeginDoc;
	out << YAML.BeginMap;
	out << YAML.Key << "difficulty" << YAML.Value << _difficulty;
	out << YAML.Key << "funds" << YAML.Value << _funds;
	out << YAML.Key << "countries" << YAML.Value;
	out << YAML.BeginSeq;
	for (Country i: _countries)
	{
		(i).save(out);
	}
	out << YAML.EndSeq;
	out << YAML.Key << "regions" << YAML.Value;
	out << YAML.BeginSeq;
	for (Region i: _regions)
	{
		(i).save(out);
	}
	out << YAML.EndSeq;
	out << YAML.Key << "bases" << YAML.Value;
	out << YAML.BeginSeq;
	for (Base i: _bases)
	{
		(i).save(out);
	}
	out << YAML.EndSeq;
	out << YAML.Key << "ufos" << YAML.Value;
	out << YAML.BeginSeq;
	for (Ufo i = _ufos)
	{
		(i).save(out);
	}
	out << YAML.EndSeq;
	out << YAML.Key << "craftId" << YAML.Value << _craftId;
	out << YAML.Key << "waypoints" << YAML.Value;
	out << YAML.BeginSeq;
	for (Waypoint i: _waypoints)
	{
		(i).save(out);
	}
	out << YAML.EndSeq;
	out << YAML.Key << "ufoId" << YAML.Value << _ufoId;
	out << YAML.Key << "waypointId" << YAML.Value << _waypointId;
	if (_battleGame != 0)
	{
		out << YAML.Key << "battleGame" << YAML.Value;
		_battleGame.save(out);
	}
	out << YAML.EndMap;

	sav << out.c_str() << std.endl << out.c_str();
	sav.close();
}

/**
 * Returns the player's current funds.
 * @return Current funds.
 */
public final int getFunds()
{
	return _funds;
}

/**
 * Changes the player's funds to a new value.
 * @param funds New funds.
 */
public void setFunds(int funds)
{
	_funds = funds;
}

/**
 * Gives the player his monthly funds, taking in account
 * all maintenance and profit costs.
 */
public void monthlyFunding()
{
	_funds += getCountryFunding() - getBaseMaintenance();
}

/**
 * Returns the current time of the game.
 * @return Pointer to the game time.
 */
public final GameTime getTime()
{
	return _time;
}

/**
 * Returns the list of countries in the game world.
 * @return Pointer to country list.
 */
public Vector<Country> getCountries()
{
	return _countries;
}

/**
 * Adds up the monthly funding of all the countries.
 * @return Total funding.
 */
public final int getCountryFunding()
{
	int total = 0;
	for (Country i: _countries)
	{
		total += (i).getFunding();
	}
	return total;
}

/**
 * Returns the list of world regions.
 * @return Pointer to region list.
 */
public Vector<Region> getRegions()
{
	return _regions;
}

/**
 * Returns the list of player bases.
 * @return Pointer to base list.
 */
public Vector<Base> getBases()
{
	return _bases;
}

/**
 * Adds up the monthly maintenance of all the bases.
 * @return Total maintenance.
 */
public final int getBaseMaintenance()
{
	int total = 0;
	for (Base i: _bases)
	{
		total += (i).getMonthlyMaintenace();
	}
	return total;
}

/**
 * Returns the latest craft IDs for each type.
 * @return Pointer to ID list.
 */
public HashMap<String, Integer> getCraftIds()
{
	return _craftId;
}

/**
 * Returns the list of alien UFOs.
 * @return Pointer to UFO list.
 */
public Vector<Ufo> getUfos()
{
	return _ufos;
}

/**
 * Returns the latest ufo ID.
 * @return Pointer to ID value.
 */
public int getUfoId()
{
	return _ufoId;
}

/**
 * Returns the latest waypoint ID.
 * @return Pointer to ID value.
 */
public int getWaypointId()
{
	return _waypointId;
}

/**
 * Returns the list of craft waypoints.
 * @return Pointer to waypoint list.
 */
public Vector<Waypoint> getWaypoints()
{
	return _waypoints;
}

/**
 * Get pointer to the battleGame object.
 * @return Pointer to the battleGame object.
 */
public SavedBattleGame getBattleGame()
{
	return _battleGame;
}

/**
 * Set battleGame object.
 * @param battleGame Pointer to the battleGame object.
 */
public void setBattleGame(SavedBattleGame battleGame)
{
	_battleGame = null;
	_battleGame = battleGame;
}

/**
 * Handles the end battle stuff.
 */
public void endBattle()
{
	// craft goes back home
	for (Base i: _bases)
	{
		for (Craft j: (i).getCrafts())
		{
			if ((j).isInBattlescape())
			{
				(j).returnToBase();
				(j).setLowFuel(true);
				(j).setInBattlescape(false);
			}
		}
	}

	// UFO crash/landing site disappears
	for (Ufo i: _ufos)
	{
		if ((i).isInBattlescape())
		{
			i = null;
			_ufos.remove(i);
			break;
		}
	}

	// bye save game, battle is over
	setBattleGame(null);
}

/**
 * Get pointer to the ufopaedia object.
 * @return Pointer to the ufopaedia object.
 */
public UfopaediaSaved getUfopaedia()
{
	return _ufopaedia;
}

}
