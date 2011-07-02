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

public class Polygon
{
	private double[] _lat, _lon;
	private int[] _x, _y;
	private int _points;
	private int _texture;
	private int _shade;

/**
 * Initializes the polygon with arrays to store each point's coordinates.
 * @param points Number of points.
 */
public Polygon(int points)
{
	_points = points;
	_texture = 0;
	_shade = 0;
	_lat = new double[_points];
	_lon = new double[_points];
	_x = new int[_points];
	_y = new int[_points];
	for (int i = 0; i < _points; i++)
	{
		_lat[i] = 0.0;
		_lon[i] = 0.0;
		_x[i] = 0;
		_y[i] = 0;
	}
}

/**
 * Performs a deep copy of an existing polygon.
 * @param other Polygon to copy from.
 */
public Polygon(final Polygon other)
{
	_points = other._points;
	_lat = new double[_points];
	_lon = new double[_points];
	_x = new int[_points];
	_y = new int[_points];
	for (int i = 0; i < _points; i++)
	{
		_lat[i] = other._lat[i];
		_lon[i] = other._lon[i];
		_x[i] = other._x[i];
		_y[i] = other._y[i];
	}
	_texture = other._texture;
	_shade = other._shade;
}

/**
 * Deletes the arrays from memory.
 */
public void clearPolygon()
{
	_lat = null;
	_lon = null;
	_x = null;
	_y = null;
}

/**
 * Returns the latitude of a given point.
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
 * Returns the longitude of a given point.
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
 * Returns the X coordinate of a given point.
 * @param i Point number (0-max).
 * @return Point's X coordinate.
 */
public final int getX(int i)
{
	return _x[i];
}

/**
 * Changes the X coordinate of a given point.
 * @param i Point number (0-max).
 * @param x Point's X coordinate.
 */
public void setX(int i, int x)
{
	_x[i] = x;
}

/**
 * Returns the Y coordinate of a given point.
 * @param i Point number (0-max).
 * @return Point's Y coordinate.
 */
public final int getY(int i)
{
	return _y[i];
}

/**
 * Changes the Y coordinate of a given point.
 * @param i Point number (0-max).
 * @param y Point's Y coordinate.
 */
public void setY(int i, int y)
{
	_y[i] = y;
}

/**
 * Returns the texture used to draw the polygon
 * (textures are stored in a set).
 * @return Texture sprite number.
 */
public final int getTexture()
{
	return _texture;
}

/**
 * Changes the texture used to draw the polygon.
 * @param tex Texture sprite number.
 */
public void setTexture(int tex)
{
	_texture = tex;
}

/**
 * Returns the number of points (vertexes) that make up the polygon.
 * @return Number of points.
 */
public final int getPoints()
{
	return _points;
}

/**
 * Returns the shade on this polygon.
 * @return Shade.
 */
public final int getShade() 
{
	return _shade;
}

/**
 * Sets the shade on this polygon.
 * @param shade Shade.
 */
public void setShade(int shade)
{
	_shade = shade;
}

}
