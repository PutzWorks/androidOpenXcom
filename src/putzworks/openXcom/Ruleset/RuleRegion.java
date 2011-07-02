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

import java.util.Vector;

public class RuleRegion
{
	private String _type;
	private int _cost;
	private Vector<Double> _lonMin, _lonMax, _latMin, _latMax;
	private Vector<City> _cities;

/**
 * Creates a blank ruleset for a certain
 * type of region.
 * @param type String defining the type.
 */
public RuleRegion(String type)
{
	_type = type; 
	_cost = 0; 
	_lonMin = new Vector<Double>();
	_lonMax = new Vector<Double>();
	_latMin = new Vector<Double>();
	_latMax = new Vector<Double>();
	_cities = new Vector<City>();

}

/**
 * Returns the language string that names
 * this region. Each region type
 * has a unique name.
 * @return Region name.
 */
public final String getType()
{
	return _type;
}

/**
 * Returns the cost of building a base inside this region.
 * @return Construction cost.
 */
public final int getBaseCost()
{
	return _cost;
}

/**
 * Changes the cost of building a base inside this region.
 * @param cost Construction cost.
 */
public void setBaseCost(int cost)
{
	_cost = cost;
}

/**
 * Adds a rectangular area that this region covers.
 * @param lonMin Minimum longitude.
 * @param lonMax Maximum longitude.
 * @param latMin Minimum latitude.
 * @param latMax Maximum latitude.
 */
public void addArea(double lonMin, double lonMax, double latMin, double latMax)
{
	_lonMin.add(lonMin);
	_lonMax.add(lonMax);
	_latMin.add(latMin);
	_latMax.add(latMax);
}

/**
 * Checks if a point is inside this region.
 * @param lon Longitude in radians.
 * @param lat Latitude in radians.
 * @return True if it's inside, False if it's outside.
 */
public final boolean insideRegion(double lon, double lat)
{
	for (int i = 0; i < _lonMin.size(); i++)
	{
		boolean inLon, inLat;

		if (_lonMin.get(i) <= _lonMax.get(i))
			inLon = (lon >= _lonMin.get(i) && lon < _lonMax.get(i));
		else
			inLon = ((lon >= _lonMin.get(i) && lon < 6.283) || (lon >= 0 && lon < _lonMax.get(i)));

		inLat = (lat >= _latMin.get(i) && lat < _latMax.get(i));

		if (inLon && inLat)
			return true;
	}
	return false;
}

/**
 * Returns the list of cities contained.
 * @return Pointer to list.
 */
public Vector<City> getCities()
{
	return _cities;
}

}
