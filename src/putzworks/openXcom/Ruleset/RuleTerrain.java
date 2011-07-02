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

import putzworks.openXcom.Engine.RNG;

public class RuleTerrain
{
	private Vector<MapDataSet> _mapDataFiles;
	private Vector<MapBlock> _mapBlocks;
	private String _name;

/**
* RuleTerrain construction
*/
public RuleTerrain(final String name)
{
	_name = name;
}

/**
* Ruleterrain only holds mapblocks. Map datafiles are referenced.
*/
public void clearRuleTerrain()
{
	for (MapBlock i: _mapBlocks)
	{
		i = null;
	}
}

/**
* gets a pointer to the array of mapblock
* @return pointer to the array of mapblocks
*/
public Vector<MapBlock> getMapBlocks()
{
	return _mapBlocks;
}

/**
* gets a pointer to the array of mapdatafiles
* @return pointer to the array of mapdatafiles
*/
public Vector<MapDataSet> getMapDataSets()
{
	return _mapDataFiles;
}

/**
* gets the terrain name (string)
* @return name
*/
public String getName()
{
	return _name;
}

/**
* gets a random mapblock within the given constraints
* @param maxsize maximum size of the mapblock (1 or 2)
* @param landingzone whether this must be a landingzone (true) or don't care (false)
* @return pointer to mapblock
*/
public MapBlock getRandomMapBlock(int maxsize, boolean landingzone)
{
	MapBlock mb = null;

	while (mb == null)
	{
		int n = RNG.generate(0, _mapBlocks.size() - 1);
		mb = _mapBlocks.get(n);
		if (landingzone && !mb.isLandingZone())
		{
			mb = null;
		}
		else if (maxsize < mb.getWidth())
		{
			mb = null;
		}
	}

	return mb;
}

/**
* Gets a mapdata object.
* @param id the id in the terrain
* @return pointer to object
*/
public MapData getMapData(int id)
{
	MapDataSet mdf = null;
	int relativeID = id;

	for (MapDataSet i: _mapDataFiles)
	{
		mdf = i;
		if (relativeID- mdf.getSize() < 0)
		{
			break;
		}
		relativeID -= mdf.getSize();
	}

	return mdf.getObjects().get(relativeID);
}

}
