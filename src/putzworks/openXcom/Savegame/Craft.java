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

import java.util.HashMap;
import java.util.Vector;

import putzworks.openXcom.Ruleset.RuleCraft;
import putzworks.openXcom.Ruleset.Ruleset;

public class Craft extends MovingTarget
{
	private RuleCraft _rules;
	private Base _base;
	private int _id, _fuel, _damage;
	private Vector<CraftWeapon> _weapons;
	private ItemContainer _items;
	private String _status;
	private boolean _lowFuel;
	private boolean _inBattlescape;

/**
 * Initializes a craft of the specified type and
 * assigns it the latest craft ID available.
 * @param rules Pointer to ruleset.
 * @param base Pointer to base of origin.
 * @param ids List of craft IDs (Leave NULL for no ID).
 */
public Craft(RuleCraft rules, Base base, HashMap<String, Integer> ids)
{
	super();
	_rules = rules;
	_base = base;
	_id = 0;
	_fuel = 0;
	_damage = 0;
	_weapons = new Vector<CraftWeapon>();
	_status = "STR_READY";
	_lowFuel = false;
	_inBattlescape = false;

	_items = new ItemContainer();
	if (ids != null)
	{
		_id = (ids)[_rules.getType()];
		(ids)[_rules.getType()]++;
	}
	for (int i = 0; i < _rules.getWeapons(); ++i)
	{
		_weapons.add(0);
	}
}

/**
 * Delete the contents of the craft from memory.
 */
public void clearCraft()
{
	for (CraftWeapon i: _weapons)
	{
		i = null;;
	}
	_items = null;
}

/**
 * Loads the craft from a YAML file.
 * @param node YAML node.
 * @param rule Ruleset for the saved game.
 */
public void load(final YAML.Node node, Ruleset rule)
{
	int size = 0;

	super.load(node);
	node["id"] >> _id;
	node["fuel"] >> _fuel;
	node["damage"] >> _damage;

	size = node["weapons"].size();
	for (unsigned int i = 0; i < size; i++)
	{
		String type;
		node["weapons"][i]["type"] >> type;
		if (type != "0")
		{
			CraftWeapon *w = new CraftWeapon(rule.getCraftWeapon(type), 0);
			w.load(node["weapons"][i]);
			_weapons[i] = w;
		}
	}

	_items.load(node["items"]);
	node["status"] >> _status;
	node["lowFuel"] >> _lowFuel;
	node["inBattlescape"] >> _inBattlescape;
}

/**
 * Saves the craft to a YAML file.
 * @param out YAML emitter.
 */
public final void save(YAML.Emitter out)
{
	super.save(out);
	out << YAML.Key << "type" << YAML.Value << _rules.getType();
	out << YAML.Key << "id" << YAML.Value << _id;
	out << YAML.Key << "fuel" << YAML.Value << _fuel;
	out << YAML.Key << "damage" << YAML.Value << _damage;
	out << YAML.Key << "weapons" << YAML.Value;
	out << YAML.BeginSeq;
	for (Vector<CraftWeapon*>.const_iterator i = _weapons.begin(); i != _weapons.end(); ++i)
	{
		if (*i != 0)
		{
			(*i).save(out);
		}
		else
		{
			out << YAML.BeginMap;
			out << YAML.Key << "type" << YAML.Value << "0";
			out << YAML.EndMap;
		}
	}
	out << YAML.EndSeq;
	out << YAML.Key << "items" << YAML.Value;
	_items.save(out);
	out << YAML.Key << "status" << YAML.Value << _status;
	out << YAML.Key << "lowFuel" << YAML.Value << _lowFuel;
	out << YAML.Key << "inBattlescape" << YAML.Value << _inBattlescape;
	out << YAML.EndMap;
}

/**
 * Saves the craft's unique identifiers to a YAML file.
 * @param out YAML emitter.
 */
public final void saveId(YAML.Emitter out)
{
	super.saveId(out);
	out << YAML.Key << "type" << YAML.Value << _rules.getType();
	out << YAML.Key << "id" << YAML.Value << _id;
	out << YAML.EndMap;
}

/**
 * Returns the ruleset for the craft's type.
 * @return Pointer to ruleset.
 */
public final RuleCraft getRules()
{
	return _rules;
}

/**
 * Returns the craft's unique ID. Each craft
 * can be identified by its type and ID.
 * @return Unique ID.
 */
public final int getId()
{
	return _id;
}

/**
 * Returns the craft's unique identifying name.
 * @param lang Language to get strings from.
 * @return Full name.
 */
public final WString getName(Language lang)
{
	WStringstream name;
	name << lang.getString(_rules.getType()) << "-" << _id;
	return name.str();
}

/**
 * Returns the base the craft belongs to.
 * @return Pointer to base.
 */
public final Base getBase()
{
	return _base;
}

/**
 * Changes the base the craft belongs to.
 * @param base Pointer to base.
 */
public void setBase(Base base)
{
	_base = base;
}

/**
 * Returns the current status of the craft.
 * @return Status string.
 */
public final String getStatus()
{
	return _status;
}

/**
 * Changes the current status of the craft.
 * @param status Status string.
 */
public void setStatus(String status)
{
	_status = status;
}

/**
 * Changes the destination the craft is heading to.
 * @param dest Pointer to new destination.
 */
public void setDestination(Target dest)
{
	super.setDestination(dest);
	setSpeed(_rules.getMaxSpeed());
}

/**
 * Returns the amount of weapons currently
 * equipped on this craft.
 * @return Number of weapons.
 */
public final int getNumWeapons()
{
	if (_rules.getWeapons() == 0)
	{
		return 0;
	}

	int total = 0;

	for (CraftWeapon i: _weapons)
	{
		if ((i) != null)
		{
			total++;
		}
	}

	return total;
}

/**
 * Returns the amount of soldiers from a list
 * that are currently attached to this craft.
 * @return Number of soldiers.
 */
public final int getNumSoldiers()
{
	if (_rules.getSoldiers() == 0)
		return 0;

	int total = 0;

	for (Soldier i: _base.getSoldiers())
	{
		if ((i).getCraft() == this)
			total++;
	}

	return total;
}

/**
 * Returns the amount of equipment currently
 * equipped on this craft.
 * @return Number of items.
 */
public final int getNumEquipment()
{
	return _items.getTotalQuantity();
}

/**
 * Returns the amount of HWPs currently
 * contained in this craft.
 * @return Number of HWPs.
 */
public final int getNumHWPs()
{
	return 0;
}

/**
 * Returns the list of weapons currently equipped
 * in the craft.
 * @return Pointer to weapon list.
 */
public Vector<CraftWeapon> getWeapons()
{
	return _weapons;
}

/**
 * Returns the list of items in the craft.
 * @return Pointer to the item list.
 */
public ItemContainer getItems()
{
	return _items;
}

/**
 * Returns the amount of fuel currently contained
 * in this craft.
 * @return Amount of fuel.
 */
public final int getFuel()
{
	return _fuel;
}

/**
 * Changes the amount of fuel currently contained
 * in this craft.
 * @param fuel Amount of fuel.
 */
public void setFuel(int fuel)
{
	_fuel = fuel;
	if (_fuel > _rules.getMaxFuel())
	{
		_fuel = _rules.getMaxFuel();
	}
	else if (_fuel < 0)
	{
		_fuel = 0;
	}
}

/**
 * Returns the ratio between the amount of fuel currently
 * contained in this craft and the total it can carry.
 * @return Percentage of fuel.
 */
public final int getFuelPercentage()
{
	return (int)floor((double)_fuel / _rules.getMaxFuel() * 100.0);
}

/**
 * Returns the amount of damage this craft has taken.
 * @return Amount of damage.
 */
public final int getDamage()
{
	return _damage;
}

/**
 * Changes the amount of damage this craft has taken.
 * @param damage Amount of damage.
 */
public void setDamage(int damage)
{
	_damage = damage;
	if (_damage < 0)
	{
		_damage = 0;
	}
}

/**
 * Returns the ratio between the amount of damage this
 * craft can take and the total it can take before it's
 * destroyed.
 * @return Percentage of damage.
 */
public final int getDamagePercentage()
{
	return (int)floor((double)_damage / _rules.getMaxDamage() * 100);
}

/**
 * Returns whether the craft is currently low on fuel
 * (only has enough to head back to base).
 * @return True if it's low, false otherwise.
 */
public final boolean getLowFuel()
{
	return _lowFuel;
}

/**
 * Changes whether the craft is currently low on fuel
 * (only has enough to head back to base).
 * @param low True if it's low, false otherwise.
 */
public void setLowFuel(boolean low)
{
	_lowFuel = low;
}

/**
 * Returns the current distance between the craft
 * and the base it belongs to.
 * @return Distance in radian.
 */
public final ouble getDistanceFromBase()
{
	double dLon, dLat;
	return getDistance(_base, dLon, dLat);
}

/**
 * Returns the amount of fuel the craft uses up
 * while it's on the air, based on its speed.
 * @return Fuel amount.
 */
public final int getFuelConsumption()
{
	return (int)floor(_speed / 100.0);
}

/**
 * Returns the minimum required fuel for the
 * craft to make it back to base.
 * @return Fuel amount.
 */
public final int getFuelLimit()
{
	return (int)floor(getFuelConsumption() * getDistanceFromBase() / (getRadianSpeed() * 120));
}

/**
 * Sends the craft back to its origin base.
 */
public void returnToBase()
{
	setDestination(_base);
}

/**
 * Moves the craft to its destination.
 */
public void think()
{
	if (_dest != 0)
	{
		calculateSpeed();
	}
	setLongitude(_lon + _speedLon);
	setLatitude(_lat + _speedLat);
	if (_dest != 0 && finishedRoute())
	{
		_lon = _dest.getLongitude();
		_lat = _dest.getLatitude();

		if (_dest == (Target)_base)
		{
			int available = 0, full = 0;
			for (CraftWeapon i: _weapons)
			{
				if ((i) == 0)
					continue;
				available++;
				if ((i).getAmmo() >= (i).getRules().getAmmoMax())
				{
					full++;
				}
			}

			if (_damage > 0)
			{
				_status = "STR_REPAIRS";
			}
			else if (available != full)
			{
				_status = "STR_REARMING";
			}
			else
			{
				_status = "STR_REFUELLING";
			}
			setSpeed(0);
			setDestination(0);
			_lowFuel = false;
		}
	}
}

/**
 * Returns if a certain point is covered by the craft's
 * radar range, taking in account the positions of both.
 * @param target Pointer to target to compare.
 * @return True if it's within range, False otherwise.
 */
public final boolean insideRadarRange(Target target)
{
	boolean inside = false;
	double newrange = _rules.getRadarRange() * (1 / 60.0) * (M_PI / 180);
	for (double lon = target.getLongitude() - 2*M_PI; lon <= target.getLongitude() + 2*M_PI; lon += 2*M_PI)
	{
		double dLon = lon - _lon;
		double dLat = target.getLatitude() - _lat;
		inside = inside || (dLon * dLon + dLat * dLat <= newrange * newrange);
	}
    return inside;
}

/**
 * Consumes the craft's fuel every 10 minutes
 * while it's on the air.
 */
public void consumeFuel()
{
	setFuel(_fuel - getFuelConsumption());
}

/**
 * Repairs the craft's damage every hour
 * while it's docked in the base.
 */
public void repair()
{
	setDamage(_damage - _rules.getRepairRate());
	if (_damage <= 0)
	{
		_status = "STR_REARMING";
	}
}

/**
 * Refuels the craft every 30 minutes
 * while it's docked in the base.
 */
public void refuel()
{
	setFuel(_fuel + _rules.getRefuelRate());
	if (_fuel >= _rules.getMaxFuel())
	{
		_status = "STR_READY";
	}
}

/**
 * Rearms the craft's weapons by adding ammo every hour
 * while it's docked in the base.
 */
public String rearm()
{
	String ammo = "";
	for (CraftWeapon i: _weapons)
	{
		if (i == _weapons.end())
		{
			_status = "STR_REFUELLING";
			break;
		}
		if (i != 0 && (i).isRearming())
		{
			if (_base.getItems().getItem((i).getRules().getClipItem()) > 0)
			{
				(i).rearm();
				_base.getItems().removeItem((i).getRules().getClipItem());
				break;
			}
			else
			{
				ammo = (i).getRules().getClipItem();
				(i).setRearming(false);
			}
		}
	}
	return ammo;
}

/**
 * Gets the craft's battlescape status.
 * @return boolean
 */
public final boolean isInBattlescape()
{
	return _inBattlescape;
}

/**
 * Sets the craft's battlescape status.
 * @param inbattle .
 */
public void setInBattlescape(boolean inbattle)
{
	_inBattlescape = inbattle;
}

}
