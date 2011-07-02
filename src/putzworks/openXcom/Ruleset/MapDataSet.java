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

import putzworks.openXcom.Engine.SurfaceSet;
import putzworks.openXcom.Resource.ResourcePack;

public class MapDataSet
{
	private String _name;
	private int _size;
	private Vector<MapData> _objects;
	private SurfaceSet _surfaceSet;
	private boolean _loaded;
	private static MapData _blankTile;
	private static MapData _scourgedTile;

/**
* MapDataSet construction.
*/
public MapDataSet(String name, int size)
{
	_name = name;
	_size = size;
	_objects = new Vector<MapData>();
	_loaded = false;
}

/**
* MapDataSet DESTRUCTION.
*/
public void clearMapDataSet()
{
	unload();
}

/**
* Gets the MapDataSet name (string).
* @return name.
*/
public String getName()
{
	return _name;
}

/**
* Gets the MapDataSet size.
* @return size in number of records.
*/
public int getSize()
{
	return _size;
}


/**
* @return pointer to the objects
*/
public Vector<MapData> getObjects()
{
	return _objects;
}

/**
* @return pointer to the surfaceset
*/
public SurfaceSet getSurfaceset()
{
	return _surfaceSet;
}

/**
 * Loads terraindata in X-Com format (MCD & PCK files)
 * @param res The resourcepack.
 * @sa http://www.ufopaedia.org/index.php?title=MCD
 */
public void load(ResourcePack res)
{
	// prevents loading twice
	if (_loaded) return;
	_loaded = true;

	int objNumber = 0;

	// the struct below helps to read the xcom file format
	struct MCD
	{
    char[] Frame = new char[8];
     char[] LOFT = new char[12];
     short ScanG;
     char u23;
     char u24;
     char u25;
     char u26;
     char u27;
     char u28;
     char u29;
     char u30;
     char UFO_Door;
     char Stop_LOS;
     char No_Floor;
     char Big_Wall;
     char Gravlift;
     char Door;
     char Block_Fire;
     char Block_Smoke;
     char u39;
     char TU_Walk;
     char TU_Fly;
     char TU_Slide;
     char Armor;
     char HE_Block;
     char Die_MCD;
     char Flammable;
     char Alt_MCD;
     char u48;
     char T_Level;
     char P_Level;
     char u51;
     char Light_Block;
     char Footstep;
     char Tile_Type;
     char HE_Type;
     char HE_Strength;
     char Smoke_Blockage;
     char Fuel;
     char Light_Source;
     char Target_Type;
     char u61;
     char u62;
	};

	MCD mcd;

	// Load Terrain Data from MCD file
	StringBuffer s = new StringBuffer();
	s.append(res.getFolder() + "TERRAIN/" + _name + ".MCD");

	// Load file
	std.ifstream mapFile (ResourcePack.insensitive(s.str()).c_str(), std.ios.in | std.ios.binary);
	if (!mapFile)
	{
		throw Exception("Failed to load MCD");
	}

	while (mapFile.read((char)mcd, sizeof(MCD)))
	{
		MapData to = new MapData(this);
		_objects.add(to);

		// set all the terrainobject properties:
		for (int frame = 0; frame < 8; frame++)
		{
			to.setSprite(frame,(int)mcd.Frame[frame]);
		}
		to.setYOffset((int)mcd.P_Level);
		to.setSpecialType((int)mcd.Target_Type, (int)mcd.Tile_Type);
		to.setTUCosts((int)mcd.TU_Walk, (int)mcd.TU_Fly, (int)mcd.TU_Slide);
		to.setFlags(mcd.UFO_Door == 1, mcd.Stop_LOS == 1, mcd.No_Floor == 1, mcd.Big_Wall == 1, mcd.Gravlift == 1, mcd.Door == 1, mcd.Block_Fire == 1, mcd.Block_Smoke == 1);
		to.setTerrainLevel((int)mcd.T_Level);
		to.setFootstepSound((int)mcd.Footstep);
		to.setAltMCD((int)(mcd.Alt_MCD));
		to.setDieMCD((int)(mcd.Die_MCD));
		to.setBlockValue((int)mcd.Light_Block, (int)mcd.Stop_LOS, (int)mcd.HE_Block, (int)mcd.Block_Smoke, (int)mcd.Block_Fire, (int)mcd.Block_Smoke);
		to.setLightSource((int)mcd.Light_Source);
		to.setArmor((int)mcd.Armor);
		to.setFlammable((int)mcd.Flammable);
		to.setFuel((int)mcd.Fuel);

		for (int layer = 0; layer < 12; layer++)
		{
			int loft = (int)mcd.LOFT[layer];
			to.setLoftID(loft, layer);
		}

		// store the 2 tiles of blanks in a static - so they are accesible everywhere
		if (_name.compare("BLANKS") == 0)
		{
			if (objNumber == 0)
				MapDataSet._blankTile = to;
			else if (objNumber == 1)
				MapDataSet._scourgedTile = to;
		}
		objNumber++;
	}


	if (!mapFile.eof())
	{
		throw Exception("Invalid data from file");
	}

	mapFile.close();

	// Load terrain sprites/surfaces/PCK files into a surfaceset
	StringBuffer s1 = new StringBuffer(), s2 = new StringBuffer();
	s1.append(res.getFolder() + "TERRAIN/" + _name + ".PCK");
	s2.append(res.getFolder() + "TERRAIN/" + _name + ".TAB");
	_surfaceSet = new SurfaceSet(32, 40);
	_surfaceSet.loadPck(ResourcePack.insensitive(s1.toString()), ResourcePack.insensitive(s2.toString()));

}

public void unload()
{
	if (_loaded)
	{
		for (MapData i: _objects)
		{
			i = null;
		}
		_surfaceSet = null;
	}
}

/**
* loadLOFTEMPS loads the LOFTEMPS.DAT into the ruleset voxeldata
* @param filename
* @param voxelData
*/
public void loadLOFTEMPS(final String filename, Vector<Uint16> voxelData)
{
	// Load file
	std.ifstream mapFile (filename.c_str(), std.ios.in | std.ios.binary);
	if (!mapFile)
	{
		throw new Exception("Failed to load DAT");
	}

	int value;

	while (mapFile.read((char)value, sizeof(value)))
	{
		voxelData.add(value);
	}

	if (!mapFile.eof())
	{
		throw Exception("Invalid data from file");
	}

	mapFile.close();
}

static public MapData getBlankFloorTile()
{
	return MapDataSet._blankTile;
}

static public MapData getScourgedEarthTile()
{
	return MapDataSet._scourgedTile;
}

}
