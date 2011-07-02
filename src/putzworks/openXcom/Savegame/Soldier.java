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
import java.util.Vector;

import putzworks.openXcom.Engine.Language;
import putzworks.openXcom.Engine.RNG;
import putzworks.openXcom.Ruleset.RuleArmor;
import putzworks.openXcom.Ruleset.RuleSoldier;
import putzworks.openXcom.Ruleset.SoldierNamePool;

public class Soldier extends Unit
{
	public enum SoldierRank { RANK_ROOKIE(0), RANK_SQUADDIE(1), RANK_SERGEANT(2), RANK_CAPTAIN(3), RANK_COLONEL(4), RANK_COMMANDER(5);
		private int id;
		private SoldierRank(int i){
			this.id = i;
		}
	     public static SoldierRank get(int code) { 
	    	 for(SoldierRank s : EnumSet.allOf(SoldierRank.class)){
	               if (s.id == code){return s;}
	    	 }
	    	 return null;
	     }

	}
	public enum SoldierGender { GENDER_MALE(0), GENDER_FEMALE(1) ;
		private int id;
		private SoldierGender(int i){
			this.id = i;
		}
	     public static SoldierGender get(int code) { 
	    	 for(SoldierGender s : EnumSet.allOf(SoldierGender.class)){
	               if (s.id == code){return s;}
	    	 }
	    	 return null;
	     }
	
	}
	public enum SoldierLook { LOOK_BLONDE(0), LOOK_BROWNHAIR(1), LOOK_ORIENTAL(2), LOOK_AFRICAN(3) ;
	private int id;
	private SoldierLook(int i){
		this.id = i;
	}
     public static SoldierLook get(int code) { 
    	 for(SoldierLook s : EnumSet.allOf(SoldierLook.class)){
               if (s.id == code){return s;}
    	 }
    	 return null;
     }

}

	private String _name;
	private RuleSoldier _rules;
	private UnitStats _initialStats, _currentStats;
	private SoldierRank _rank;
	private Craft _craft;
	private SoldierGender _gender;
	private SoldierLook _look;
	private int _missions, _kills;

/**
 * Initializes a new blank soldier.
 * @param rules Soldier ruleset.
 * @param armor Soldier armor.
 */
public Soldier(RuleSoldier rules, RuleArmor armor)
{
	super(armor);
	_name = "";
	_rules = rules;
	_rank = SoldierRank.RANK_ROOKIE;
	_craft = null;
	_gender = SoldierGender.GENDER_MALE;
	_look = SoldierLook.LOOK_BLONDE;
	_missions = 0;
	_kills = 0;

	_initialStats.bravery = 0;
	_initialStats.firing = 0;
	_initialStats.health = 0;
	_initialStats.melee = 0;
	_initialStats.psiSkill = 0;
	_initialStats.psiStrength = 0;
	_initialStats.reactions = 0;
	_initialStats.stamina = 0;
	_initialStats.strength = 0;
	_initialStats.throwing = 0;
	_initialStats.tu = 0;

	_currentStats = _initialStats;
}

/**
 * Initializes a new soldier with random stats and a name
 * pulled from a set of SoldierNamePool's.
 * @param rules Soldier ruleset.
 * @param armor Soldier armor.
 * @param names List of name pools.
 */
public Soldier(RuleSoldier rules, RuleArmor armor, Vector<SoldierNamePool> names) //unknown: Unit(armor)
{
	_rules = rules;
	_rank = SoldierRank.RANK_ROOKIE;
	_craft = null;
	_missions = 0;
	_kills = 0;

	UnitStats minStats = rules.getMinStats();
	UnitStats maxStats = rules.getMaxStats();

	_initialStats.tu = RNG.generate(minStats.tu, maxStats.tu);
	_initialStats.stamina = RNG.generate(minStats.stamina, maxStats.stamina);
	_initialStats.health = RNG.generate(minStats.health, maxStats.health);
	_initialStats.bravery = RNG.generate(minStats.bravery, maxStats.bravery);
	_initialStats.reactions = RNG.generate(minStats.reactions, maxStats.reactions);
	_initialStats.firing = RNG.generate(minStats.firing, maxStats.firing);
	_initialStats.throwing = RNG.generate(minStats.throwing, maxStats.throwing);
	_initialStats.strength = RNG.generate(minStats.strength, maxStats.strength);
	_initialStats.psiStrength = RNG.generate(minStats.psiStrength, maxStats.psiStrength);
	_initialStats.melee = RNG.generate(minStats.melee, maxStats.melee);
	_initialStats.psiSkill = 0;

	_currentStats = _initialStats;

	int gender = 0;
	_name = names.get(RNG.generate(0, names.size()-1)).genName(gender);
	_gender = SoldierGender.get(gender);
	_look = SoldierLook.get(RNG.generate(0, 3));
}

/**
 * Loads the soldier from a YAML file.
 * @param node YAML node.
 */
public void load(final YAML.Node node)
{
	int a = 0;
	String name;
	node["name"] >> name;
	_name = Language.utf8ToWstr(name);
	node["tu"] >> _initialStats.tu;
	node["stamina"] >> _initialStats.stamina;
	node["health"] >> _initialStats.health;
	node["bravery"] >> _initialStats.bravery;
	node["reactions"] >> _initialStats.reactions;
	node["firing"] >> _initialStats.firing;
	node["throwing"] >> _initialStats.throwing;
	node["strength"] >> _initialStats.strength;
	node["psiStrength"] >> _initialStats.psiStrength;
	node["psiSkill"] >> _initialStats.psiSkill;
	node["melee"] >> _initialStats.melee;
	_currentStats = _initialStats;
	node["rank"] >> a;
	_rank = SoldierRank.get(a);
	node["gender"] >> a;
	_gender = SoldierGender.get(a);
	node["look"] >> a;
	_look = SoldierLook.get(a);
	node["missions"] >> _missions;
	node["kills"] >> _kills;
}

/**
 * Saves the soldier to a YAML file.
 * @param out YAML emitter.
 */
public final void save(YAML.Emitter out)
{
	out << YAML.BeginMap;
	out << YAML.Key << "name" << YAML.Value << Language.wstrToUtf8(_name);
	out << YAML.Key << "tu" << YAML.Value << _initialStats.tu;
	out << YAML.Key << "stamina" << YAML.Value << _initialStats.stamina;
	out << YAML.Key << "health" << YAML.Value << _initialStats.health;
	out << YAML.Key << "bravery" << YAML.Value << _initialStats.bravery;
	out << YAML.Key << "reactions" << YAML.Value << _initialStats.reactions;
	out << YAML.Key << "firing" << YAML.Value << _initialStats.firing;
	out << YAML.Key << "throwing" << YAML.Value << _initialStats.throwing;
	out << YAML.Key << "strength" << YAML.Value << _initialStats.strength;
	out << YAML.Key << "psiStrength" << YAML.Value << _initialStats.psiStrength;
	out << YAML.Key << "psiSkill" << YAML.Value << _initialStats.psiSkill;
	out << YAML.Key << "melee" << YAML.Value << _initialStats.melee;
	out << YAML.Key << "rank" << YAML.Value << _rank;
	if (_craft != null)
	{
		out << YAML.Key << "craft" << YAML.Value;
		_craft.saveId(out);
	}
	out << YAML.Key << "gender" << YAML.Value << _gender;
	out << YAML.Key << "look" << YAML.Value << _look;
	out << YAML.Key << "missions" << YAML.Value << _missions;
	out << YAML.Key << "kills" << YAML.Value << _kills;
	out << YAML.EndMap;
}

/**
 * Returns the soldier's full name.
 * @return Soldier name.
 */
public final String getName()
{
	return _name;
}

/**
 * Changes the soldier's full name.
 * @param name Soldier name.
 */
public void setName(final String name)
{
	_name = name;
}

/**
 * Returns the craft the soldier is assigned to.
 * @return Pointer to craft.
 */
public final Craft getCraft()
{
	return _craft;
}

/**
 * Assigns the soldier to a new craft.
 * @param craft Pointer to craft.
 */
public void setCraft(Craft craft)
{
	_craft = craft;
}

/**
 * Returns a localizable-string representation of
 * the soldier's military rank.
 * @return String ID for rank.
 */
public final String getRankString()
{
	switch (_rank)
	{
	case RANK_ROOKIE:
		return "STR_ROOKIE";
	case RANK_SQUADDIE:
		return "STR_SQUADDIE";
	case RANK_SERGEANT:
		return "STR_SERGEANT";
	case RANK_CAPTAIN:
		return "STR_CAPTAIN";
	case RANK_COLONEL:
		return "STR_COLONE";
	case RANK_COMMANDER:
		return "STR_COMMANDER";
	}
	return "";
}

/**
 * Returns a graphic representation of
 * the soldier's military rank.
 * @return Sprite ID for rank.
 */
public final int getRankSprite()
{
	return 42 + _rank.id;
}

/**
 * Returns the soldier's amount of time units.
 * @return Time units.
 */
public final int getTimeUnits()
{
	return _currentStats.tu;
}

/**
 * Returns the soldier's amount of stamina.
 * @return Stamina.
 */
public final int getStamina()
{
	return _currentStats.stamina;
}

/**
 * Returns the soldier's amount of health.
 * @return Health.
 */
public final int getHealth()
{
	return _currentStats.health;
}

/**
 * Returns the soldier's amount of bravery.
 * @return Bravery.
 */
public final int getBravery()
{
	return _currentStats.bravery;
}

/**
 * Returns the soldier's amount of reactions.
 * @return Reactions.
 */
public final int getReactions()
{
	return _currentStats.reactions;
}

/**
 * Returns the soldier's amount of firing accuracy.
 * @return Firing accuracy.
 */
public final int getFiringAccuracy()
{
	return _currentStats.firing;
}

/**
 * Returns the soldier's amount of throwing accuracy.
 * @return Throwing accuracy.
 */
public final int getThrowingAccuracy()
{
	return _currentStats.throwing;
}

/**
 * Returns the soldier's amount of strength.
 * @return Strength.
 */
public final int getStrength()
{
	return _currentStats.strength;
}

/**
 * Returns the soldier's amount of missions.
 * @return Missions.
 */
public final int getMissions()
{
	return _missions;
}

/**
 * Returns the soldier's amount of kills.
 * @return Kills.
 */
public final int getKills()
{
	return _kills;
}

/**
 * Returns the soldier's gender.
 * @return Gender.
 */
public final SoldierGender getGender()
{
	return _gender;
}

/**
 * Returns the soldier's rules.
 * @return rulesoldier
 */
public final RuleSoldier getRules()
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
