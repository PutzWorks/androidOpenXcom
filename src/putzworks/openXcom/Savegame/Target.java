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

import java.util.Vector;

public class Target
{
	protected double _lon;
	protected double _lat;
	private Vector<Target> _followers;

/**
 * Initializes a target with blank coordinates.
 */
public Target()
{
	_lon = 0.0;
	_lat = 0.0;
	_followers = new Vector<Target>();

}

/**
 * Loads the target from a YAML file.
 * @param node YAML node.
 */
public void load(final YAML.Node node)
{
	node["lon"] >> _lon;
	node["lat"] >> _lat;
}

/**
 * Saves the target to a YAML file.
 * @param out YAML emitter.
 */
public final void save(YAML.Emitter out)
{
	out << YAML.BeginMap;
	out << YAML.Key << "lon" << YAML.Value << _lon;
	out << YAML.Key << "lat" << YAML.Value << _lat;
}

/**
 * Saves the target's unique identifiers to a YAML file.
 * @param out YAML emitter.
 */
public final void saveId(YAML.Emitter out)
{
	out << YAML.BeginMap;
	out << YAML.Key << "lon" << YAML.Value << _lon;
	out << YAML.Key << "lat" << YAML.Value << _lat;
}

/**
 * Returns the longitude coordinate of the target.
 * @return Longitude in radian.
 */
public final double getLongitude()
{
	return _lon;
}

/**
 * Changes the longitude coordinate of the target.
 * @param lon Longitude in radian.
 */
public void setLongitude(double lon)
{
	_lon = lon;

	// Keep between 0 and 2xPI
	while (_lon < 0)
		_lon += 2 * M_PI;
	while (_lon >= 2 * M_PI)
		_lon -= 2 * M_PI;
}

/**
 * Returns the latitude coordinate of the target.
 * @return Latitude in radian.
 */
public final double getLatitude()
{
	return _lat;
}

/**
 * Changes the latitude coordinate of the target.
 * @param lat Latitude in radian.
 */
public void setLatitude(double lat)
{
	_lat = lat;
}

/**
 * Returns the list of crafts currently 
 * following this target.
 * @return Pointer to list of crafts.
 */
public Vector<Target> getFollowers()
{
	return _followers;
}

}
