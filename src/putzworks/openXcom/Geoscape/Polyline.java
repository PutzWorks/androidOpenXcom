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
package putzworks.openXcom.Geoscape;

public class Polyline
{
	private double[] _lat, _lon;
	private final int _points;

/**
 * Initializes the polyline with arrays to store each point's coordinates.
 * @param points Number of points.
 */
public Polyline(int points)
{
	_points = points;
	_lat = new double[points];
	_lon = new double[points];
}

/**
 * Deletes the arrays from memory.
 */
public void clearPolyline()
{
	_lat = null;
	_lon = null;
}

/**
 * Returns the latitude (X) of a given point.
 * @param i Point number (0-max).
 * @return Point's latitude.
 */
public final double getLatitude(int i)
{
	return _lat[i];
}

/**
 * Changes the latitude of a given point.
 * @param i Point number (0-max).
 * @param lat Point's latitude.
 */
public void setLatitude(int i, double lat)
{
	_lat[i] = lat;
}

/**
 * Returns the longitude (Y) of a given point.
 * @param i Point number (0-max).
 * @return Point's longitude.
 */
public final double getLongitude(int i)
{
	return _lon[i];
}

/**
 * Changes the latitude of a given point.
 * @param i Point number (0-max).
 * @param lon Point's longitude.
 */
public void setLongitude(int i, double lon)
{
	_lon[i] = lon;
}

/**
 * Returns the number of points (vertexes) that make up the polyline.
 * @return Number of points.
 */
public final int getPoints()
{
	return _points;
}

}
