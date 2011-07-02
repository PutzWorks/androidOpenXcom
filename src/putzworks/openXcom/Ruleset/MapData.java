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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import putzworks.openXcom.Ruleset.RuleItem.ItemDamageType;

public class MapData
{
	public static final int O_FLOOR = 0;
	public static final int O_WESTWALL = 1;
	public static final int O_NORTHWALL = 2;
	public static final int O_OBJECT = 3;

	public enum SpecialTileType{TILE(0),
					START_POINT(1),
					ION_BEAM_ACCEL(2),
					DESTROY_OBJECTIVE(3),
					MAGNETIC_NAV(4),
					ALIEN_CRYO(5),
					ALIEN_CLON(6),
					ALIEN_LEARN(7),
					ALIEN_IMPLANT(8),
					UKNOWN09(9),
					ALIEN_PLASTICS(10),
					EXAM_ROOM(11),
					DEAD_TILE(12),
					END_POINT(13),
					MUST_DESTROY(14);

    	private static final Map<Integer,SpecialTileType> lookup 
        = new HashMap<Integer,SpecialTileType>();

    	static {
    		for(SpecialTileType s : EnumSet.allOf(SpecialTileType.class))
    			lookup.put(s.getCode(), s);
    	}

    	private int code;

    	private SpecialTileType(int code) {
    		this.code = code;
    	}

    	public int getCode() { return code; }

    	public static SpecialTileType get(int code) { 
    		return lookup.get(code); 
    	}
	}

	public enum MovementType{ MT_WALK, MT_FLY, MT_SLIDE};

	private MapDataSet _dataset;
	private SpecialTileType _specialType;
	private boolean _isUfoDoor, _stopLOS, _isNoFloor, _isBigWall, _isGravLift, _isDoor, _blockFire, _blockSmoke;
	private int _yOffset, _TUWalk, _TUFly, _TUSlide, _terrainLevel, _footstepSound, _dieMCD, _altMCD, _objectType, _lightSource;
	private int _armor, _flammable, _fuel;
	private int[] _sprite = new int[8];
	private int[] _block = new int[6];
	private int[] _loftID = new int[12];

/**
*  Creates a new Map Data Object.
* @param dataset The dataset this object belongs to.
*/
public MapData(MapDataSet dataset)
{
	_dataset = dataset;
}

/**
* Get the dataset this object belongs to.
* @return Pointer to MapDataSet.
*/
public MapDataSet getDataset()
{
	return _dataset;
}

/**
* Get the sprite index.
* @param frameID Animation frame 0-7
* @return the original sprite index
*/
public int getSprite(int frameID)
{
	return _sprite[frameID];
}

/**
* Set the sprite index for a certain frame.
* @param frameID Animation frame
* @param value The sprite index in the surfaceset of the mapdataset.
*/
public void setSprite(int frameID, int value)
{
	_sprite[frameID] = value;
}

/**
  * Get whether this is an animated ufo door.
  * @return boolean 
  */
public boolean isUFODoor()
{
	return _isUfoDoor;
}

/**
  * Get whether this is a floor.
  * @return boolean 
  */
public boolean isNoFloor()
{
	return _isNoFloor;
}

/**
  * Get whether this is a big wall, which blocks all surrounding paths.
  * @return boolean 
  */
public boolean isBigWall()
{
	if (_terrainLevel < 0) return false; // this is a hack for eg. Skyranger Ramps
	return _isBigWall;
}

/**
  * Get whether this is a normal door.
  * @return boolean 
  */
public boolean isDoor()
{
	return _isDoor;
}

/**
  * Set all kinds of flags.
  * @param isUfoDoor
  * @param stopLOS
  * @param isNoFloor
  * @param isBigWall
  * @param isGravLift
  * @param isDoor
  * @param blockFire
  * @param blockSmoke
  */
public void setFlags(boolean isUfoDoor, boolean stopLOS, boolean isNoFloor, boolean isBigWall, boolean isGravLift, boolean isDoor, boolean blockFire, boolean blockSmoke)
{
	_isUfoDoor = isUfoDoor;
	_stopLOS = stopLOS;
	_isNoFloor = isNoFloor;
	_isBigWall = isBigWall;
	_isGravLift = isGravLift;
	_isDoor = isDoor;
	_blockFire = blockFire;
	_blockSmoke = blockSmoke;
}

/**
  * Get the amount of blockage of a certain type.
  * @param type
  * @return blockage (0-255)
  */
public int getBlock(ItemDamageType type)
{
	if (type == ItemDamageType.DT_NONE)
		return _block[1];
	if (type == ItemDamageType.DT_HE)
		return _block[2]; 
	if (type == ItemDamageType.DT_SMOKE)
		return _block[3]; 
	if (type == ItemDamageType.DT_IN)
		return _block[4]; 
	if (type == ItemDamageType.DT_STUN)
		return _block[5]; 

	return 0;
}

/**
  * Sets the amount of blockage for all types.
  * @param lightBlock
  * @param visionBlock
  * @param HEBlock
  * @param smokeBlock
  * @param fireBlock
  * @param gasBlock
  */
public void setBlockValue(int lightBlock, int visionBlock, int HEBlock, int smokeBlock, int fireBlock, int gasBlock)
{
	_block[0] = lightBlock; // not used...
	_block[1] = visionBlock==1?255:0;
	_block[2] = HEBlock;
	_block[3] = smokeBlock==1?255:0;
	_block[4] = fireBlock==1?255:0;
	_block[5] = gasBlock==1?255:0;
}

/**
  * Get the Y offset for drawing.
  * @return int height in pixels
  */
public int getYOffset()
{
	return _yOffset;
}

/**
  * Sets the offset on the Y axis for drawing this object.
  * @param value
  */
public void setYOffset(int value)
{
	_yOffset = value;
}

/**
  * Gets the Y offset for drawing.
  * @return int height in pixels
  */
public SpecialTileType getSpecialType()
{
	return _specialType;
}

/**
  * Get the type of object.
  * @return 0-3
  */
public int getObjectType()
{
	return _objectType;
}

/**
  * Sets a special tile type and object type.
  * @param value Special tile type.
  * @param otype Object type.
  */
public void setSpecialType(int value, int otype)
{
	_specialType = SpecialTileType.get(value);
	_objectType = otype;
}

/*
 * Gets the TU cost to walk over the object.
 * @param movementType
 * @return TU cost
 */
public int getTUCost(MovementType movementType)
{
	switch (movementType)
	{
	case MT_WALK:
		return _TUWalk;
	case MT_FLY:
		return _TUFly;
	case MT_SLIDE:
		return _TUSlide;
	}
	return 0;
}

/**
  * Set TU cost to move over the object.
  * @param walk
  * @param fly
  * @param slide
  */
public void setTUCosts(int walk, int fly, int slide)
{
	_TUWalk = walk;
	_TUFly = fly;
	_TUSlide = slide;
}

/**
  * Add this to the graphical Y offset of units or objects on this tile.
  * @return Y offset
  */
public int getTerrainLevel()
{
	return _terrainLevel;
}

/**
  * Sets Y offset for units/objects on this tile.
  * @param value
  */
public void setTerrainLevel(int value)
{
	_terrainLevel = value;
}

/**
  * Get index to the footstep sound.
  * @return sound ID
  */
public int getFootstepSound()
{
	return _footstepSound;
}

/**
  * Set the index to the footstep sound.
  * @param value
  */
public void setFootstepSound(int value)
{
	_footstepSound = value;
}

/**
  * Get the alternative object ID.
  * @return object ID 
  */
public int getAltMCD()
{
	return _altMCD;
}

/**
  * Set the alternative object ID.
  * @param value
  */
public void setAltMCD(int value)
{
	_altMCD = value;
}

/**
  * Get the dead object ID.
  * @return object ID 
  */
public int getDieMCD()
{
	return _dieMCD;
}

/**
  * Set the dead object ID.
  * @param value
  */
public void setDieMCD(int value)
{
	_dieMCD = value;
}

/**
  * Get the amount of light the object is emitting.
  * @return lightsource
  */
public int getLightSource()
{
	return _lightSource;
}

/**
  * Set the amount of light the object is emitting.
  * @param value
  */
public void setLightSource(int value)
{
	_lightSource = value;
}

/**
  * Get the amount of armor.
  * @return armor
  */
public int getArmor()
{
	return _armor;
}

/**
  * Set the amount of armor.
  * @value armor
  */
public void setArmor(int value)
{
	_armor = value;
}

/**
  * Get the amount of flammable.
  * @return armor
  */
public int getFlammable()
{
	return _flammable;
}

/**
  * Set the amount of flammable.
  * @value armor
  */
public void setFlammable(int value)
{
	_flammable = value;
}

/**
  * Get the amount of fuel.
  * @return fuel
  */
public int getFuel()
{
	return _fuel;
}

/**
  * Set the amount of fuel.
  * @value fuel
  */
public void setFuel(int value)
{
	_fuel = value;
}

/// Get the loft index for a certain layer.
public int getLoftID(int layer)
{
	return _loftID[layer];
}

/// Set the loft index for a certain layer.
public void setLoftID(int loft, int layer)
{
	_loftID[layer] = loft;
}

}
