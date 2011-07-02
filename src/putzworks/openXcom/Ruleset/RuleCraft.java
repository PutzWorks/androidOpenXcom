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

public class RuleCraft
{
	private String _type;
	private int _sprite;
	private int _fuelMax, _damageMax, _speedMax, _accel, _weapons, _soldiers, _hwps, _cost, _repair, _refuel, _range, _time;
	// battlescape:
	private RuleTerrain _battlescapeTerrainData;

/**
 * Creates a blank ruleset for a certain
 * type of craft.
 * @param type String defining the type.
 */
public RuleCraft(String type)
{
	_type = type;
	_sprite = -1;
	_fuelMax = 0;
	_damageMax = 0;
	_speedMax = 0;
	_accel = 0;
	_weapons = 0;
	_soldiers = 0;
	_hwps = 0;
	_cost = 0;
	_repair = 1;
	_refuel = 1;
	_range = 600;
	_time = 0;
	_battlescapeTerrainData = null;

}

/**
 * Returns the language string that names
 * this craft. Each craft type has a unique name.
 * @return Craft name.
 */
public final String getType()
{
	return _type;
}

/**
 * Returns the ID of the sprite used to draw the craft
 * in the Basescape and Equip Craft screens.
 * @return Sprite ID.
 */
public final int getSprite()
{
	return _sprite;
}

/**
 * Changes the ID of the sprite used to draw the craft
 * in the Basescape and Equip Craft screens.
 * @param sprite Sprite ID.
 */
public void setSprite(int sprite)
{
	_sprite = sprite;
}

/**
 * Returns the maximum fuel the craft can contain.
 * @return Fuel amount.
 */
public final int getMaxFuel()
{
	return _fuelMax;
}

/**
 * Changes the maximum fuel the craft can contain.
 * @param fuel Fuel amount.
 */
public void setMaxFuel(int fuel)
{
	_fuelMax = fuel;
}

/**
 * Returns the maximum damage (damage the craft can take) const
 * of the craft.
 * @return Damage.
 */
public final int getMaxDamage()
{
	return _damageMax;
}

/**
 * Changes the maximum damage (damage the craft can take)
 * of the craft.
 * @param damage Damage.
 */
public void setMaxDamage(int damage)
{
	_damageMax = damage;
}

/**
 * Returns the maximum speed of the craft flying
 * around the Geoscape.
 * @return Speed in knots.
 */
public final int getMaxSpeed()
{
	return _speedMax;
}

/**
 * Changes the maximum speed of the craft flying
 * around the Geoscape.
 * @param speed Speed in knots.
 */
public void setMaxSpeed(int speed)
{
	_speedMax = speed;
}

/**
 * Returns the acceleration of the craft for
 * taking off / stopping.
 * @return Acceleration.
 */
public final int getAcceleration()
{
	return _accel;
}

/**
 * Changes the acceleration of the craft for
 * taking off / stopping.
 * @param accel Acceleration.
 */
public void setAcceleration(int accel)
{
	_accel = accel;
}

/**
 * Returns the maximum number of weapons that
 * can be equipped onto the craft.
 * @return Weapon capacity.
 */
public final int getWeapons()
{
	return _weapons;
}

/**
 * Changes the maximum number of weapons that
 * can be equipped onto the craft.
 * @param weapons Weapon capacity.
 */
public void setWeapons(int weapons)
{
	_weapons = weapons;
}

/**
 * Returns the maximum number of soldiers that
 * the craft can carry.
 * @return Soldier capacity.
 */
public final int getSoldiers()
{
	return _soldiers;
}

/**
 * Changes the maximum number of soldiers that
 * the craft can carry.
 * @param soldiers Soldier capacity.
 */
public void setSoldiers(int soldiers)
{
	_soldiers = soldiers;
}

/**
 * Returns the maximum number of HWPs that
 * the craft can carry.
 * @return HWP capacity.
 */
public final int getHWPs()
{
	return _hwps;
}

/**
 * Changes the maximum number of HWPs that
 * the craft can carry.
 * @param hwps HWP capacity.
 */
public void setHWPs(int hwps)
{
	_hwps = hwps;
}

/**
 * Returns the cost of this craft for
 * purchase/maintenance.
 * @return Cost.
 */
public final int getCost()
{
	return _cost;
}

/**
 * Changes the cost of this craft for
 * purchase/maintenance.
 * @param cost Cost.
 */
public void setCost(int cost)
{
	_cost = cost;
}

/**
 * Returns how much damage is removed from the
 * craft while repairing.
 * @return Amount of damage.
 */
public final int getRepairRate()
{
	return _repair;
}

/**
 * Changes how much damage is removed from the
 * craft while repairing.
 * @param repair Amount of damage.
 */
public void setRepairRate(int repair)
{
	_repair = repair;
}

/**
 * Returns how much fuel is added to the
 * craft while refuelling.
 * @return Amount of fuel.
 */
public final int getRefuelRate()
{
	return _refuel;
}

/**
 * Changes how much fuel is added to the
 * craft while refuelling.
 * @param refuel Amount of fuel.
 */
public void setRefuelRate(int refuel)
{
	_refuel = refuel;
}

/**
 * Returns the craft's radar range
 * for detecting UFOs.
 * @return Range in nautical miles.
 */
public final int getRadarRange()
{
	return _range;
}

/**
 * Changes the craft's radar range
 * for detecting UFOs.
 * @param range Range in nautical miles.
 */
public void setRadarRange(int range)
{
	_range = range;
}

/**
 * Returns the amount of time this item
 * takes to arrive at a base.
 * @return Time in hours.
 */
public final int getTransferTime()
{
	return _time;
}

/**
 * Changes the amount of time this item
 * takes to arrive at a base.
 * @param time Time in hours.
 */
public void setTransferTime(int time)
{
	_time = time;
}

/**
 * Returns the terrain data needed to draw the Craft in the battlescape.
 * @return Terrain.
 */
public RuleTerrain getBattlescapeTerrainData()
{
	return _battlescapeTerrainData;
}

/**
 * Changes the terrain data needed to draw the Craft in the battlescape.
 * @param terrain pointer to a RuleTerrain.
 */
public void setBattlescapeTerrainData(RuleTerrain terrain)
{
	_battlescapeTerrainData = terrain;
}

}
