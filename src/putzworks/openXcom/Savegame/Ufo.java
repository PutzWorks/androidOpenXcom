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

import putzworks.openXcom.Engine.Language;
import putzworks.openXcom.Ruleset.RuleUfo;

public class Ufo extends MovingTarget
{
	private RuleUfo _rules;
	private int _id, _damage, _altitude;
	private String _direction;
	private boolean _detected;
	private int _hoursCrashed;
	private boolean _inBattlescape;

/**
 * Initializes a UFO of the specified type.
 * @param rules Pointer to ruleset.
 */
public Ufo(RuleUfo rules)
{
	super();
	_rules = rules;
	_id = 0;
	_damage = 0;
	_altitude = 0;
	_direction = "STR_NORTH";
	_detected = false;
	_hoursCrashed = -1;
	_inBattlescape = false;

}

/**
 * Loads the UFO from a YAML file.
 * @param node YAML node.
 */
public void load(final YAML.Node node)
{
	super.load(node);
	node["id"] >> _id;
	node["damage"] >> _damage;
	node["altitude"] >> _altitude;
	node["direction"] >> _direction;
	node["detected"] >> _detected;
	node["hoursCrashed"] >> _hoursCrashed;
	node["inBattlescape"] >> _inBattlescape;

	double lon, lat;
	node["dest"]["lon"] >> lon;
	node["dest"]["lat"] >> lat;
	_dest = new Waypoint();
	_dest.setLongitude(lon);
	_dest.setLatitude(lat);
}

/**
 * Saves the UFO to a YAML file.
 * @param out YAML emitter.
 */
public final void save(YAML.Emitter out)
{
	super.save(out);
	out << YAML.Key << "type" << YAML.Value << _rules.getType();
	out << YAML.Key << "id" << YAML.Value << _id;
	out << YAML.Key << "damage" << YAML.Value << _damage;
	out << YAML.Key << "altitude" << YAML.Value << _altitude;
	out << YAML.Key << "direction" << YAML.Value << _direction;
	out << YAML.Key << "detected" << YAML.Value << _detected;
	out << YAML.Key << "hoursCrashed" << YAML.Value << _hoursCrashed;
	out << YAML.Key << "inBattlescape" << YAML.Value << _inBattlescape;
	out << YAML.EndMap;
}

/**
 * Saves the UFO's unique identifiers to a YAML file.
 * @param out YAML emitter.
 */
public final void saveId(YAML.Emitter out)
{
	super.saveId(out);
	out << YAML.Key << "type" << YAML.Value << "STR_UFO";
	out << YAML.Key << "id" << YAML.Value << _id;
	out << YAML.EndMap;
}

/**
 * Returns the ruleset for the UFO's type.
 * @return Pointer to ruleset.
 */
public final RuleUfo getRules()
{
	return _rules;
}

/**
 * Returns the UFO's unique ID. If it's 0,
 * this UFO has never been detected.
 * @return Unique ID.
 */
public final int getId()
{
	return _id;
}

/**
 * Changes the UFO's unique ID.
 * @param id Unique ID.
 */
public void setId(int id)
{
	_id = id;
}

/**
 * Returns the UFO's unique identifying name.
 * @param lang Language to get strings from.
 * @return Full name.
 */
public final WString getName(Language lang)
{
	WStringstream name;
	if (!isCrashed())
	{
		name << lang.getString("STR_UFO_") << _id;
	}
	else
	{
		name << lang.getString("STR_CRASH_SITE_") << _id;
	}
	return name.str();
}

/**
 * Returns the amount of damage this UFO has taken.
 * @return Amount of damage.
 */
public final int getDamage()
{
	return _damage;
}

/**
 * Changes the amount of damage this UFO has taken.
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
 * Returns whether this UFO has been detected by radars.
 * @return Detection status.
 */
public final boolean getDetected()
{
	return _detected;
}

/**
 * Changes whether this UFO has been detected by radars.
 * @param detected Detection status.
 */
public void setDetected(boolean detected)
{
	_detected = detected;
}

/**
 * Returns the amount of hours the UFO has been crashed for.
 * @return Amount of hours.
 */
public final int getHoursCrashed()
{
	return _hoursCrashed;
}

/**
 * Changes the amount of hours the UFO has been crashed for.
 * @param hours Amount of hours.
 */
public void setHoursCrashed(int hours)
{
	_hoursCrashed = hours;
}

/**
 * Returns the current direction the UFO is heading in.
 * @return Direction.
 */
public final String getDirection()
{
	return _direction;
}

/**
 * Returns the current altitude of the UFO.
 * @return Altitude.
 */
public final String getAltitude()
{
	return "STR_HIGH_UC";
}

/**
 * Returns if this UFO took enough damage
 * to cause it to crash.
 * @return Crashed status.
 */
public final boolean isCrashed()
{
	return (_damage >= _rules.getMaxDamage() / 2);
}

/**
 * Returns if this UFO took enough damage
 * to cause it to crash.
 * @return Crashed status.
 */
public final boolean isDestroyed()
{
	return (_damage >= _rules.getMaxDamage());
}

/**
 * Calculates the direction for the UFO based
 * on the current raw speed and destination.
 */
public void calculateSpeed()
{
	super.calculateSpeed();
	if (_speedLon > 0)
	{
		if (_speedLat > 0)
		{
			_direction = "STR_SOUTH_EAST";
		}
		else if (_speedLat < 0)
		{
			_direction = "STR_NORTH_EAST";
		}
		else
		{
			_direction = "STR_EAST";
		}
	}
	else if (_speedLon < 0)
	{
		if (_speedLat > 0)
		{
			_direction = "STR_SOUTH_WEST";
		}
		else if (_speedLat < 0)
		{
			_direction = "STR_NORTH_WEST";
		}
		else
		{
			_direction = "STR_WEST";
		}
	}
	else
	{
		if (_speedLat > 0)
		{
			_direction = "STR_SOUTH";
		}
		else if (_speedLat < 0)
		{
			_direction = "STR_NORTH";
		}
	}
}

/**
 * Moves the UFO to its destination.
 */
public void think()
{
	if (!isCrashed())
	{
		setLongitude(_lon + _speedLon);
		setLatitude(_lat + _speedLat);
		if (_dest != null && finishedRoute())
		{
			_lon = _dest.getLongitude();
			_lat = _dest.getLatitude();
			setSpeed(0);
		}
	}
}

/**
 * Gets the UFO's battlescape status.
 * @return boolean
 */
public final boolean isInBattlescape()
{
	return _inBattlescape;
}

/**
 * Sets the UFO's battlescape status.
 * @param inbattle .
 */
public void setInBattlescape(boolean inbattle)
{
	_inBattlescape = inbattle;
}

}
