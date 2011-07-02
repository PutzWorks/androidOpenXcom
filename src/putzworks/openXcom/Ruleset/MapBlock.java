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

public class MapBlock
{
	private RuleTerrain _terrain;
	private String _name;
	private int _width, _length, _height;
	private boolean _landingZone;

/**
* MapBlock construction
*/
public MapBlock(RuleTerrain terrain, String name, int width, int length, boolean landingZone)
{
	_terrain = terrain;
	_name = name;
	_width = width;
	_length = length;
	_landingZone = landingZone;

}

/**
* Gets the MapBlock name (string).
* @return name
*/
public String getName()
{
	return _name;
}

/**
* Gets the MapBlock width.
* @return width in tiles.
*/
public int getWidth()
{
	return _width;
}

/**
* Gets the MapBlock length.
* @return length in tiles.
*/
public int getLength()
{
	return _length;
}

/**
* Sets the MapBlock height.
* @param height
*/
public void setHeight(int height)
{
	_height = height;
}

/**
* Gets the MapBlock height.
* @return height
*/
public int getHeight()
{
	return _height;
}

/**
* Is this mapblock usable as a landingzone.
* @return boolean whether usable as landingzone.
*/
public boolean isLandingZone()
{
	return _landingZone;
}

}
