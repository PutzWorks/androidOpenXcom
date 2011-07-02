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

import putzworks.openXcom.Battlescape.Position;
import putzworks.openXcom.Engine.RNG;
import putzworks.openXcom.Engine.Surface;
import putzworks.openXcom.Ruleset.MapData;
import putzworks.openXcom.Ruleset.MapDataSet;
import putzworks.openXcom.Ruleset.MapData.MovementType;

public class Tile
{
	private final static int LIGHTLAYERS = 3;

	private int[] _currentFrame = new int[4];
	private boolean _discovered, _checked;
	private int[] _light = new int[LIGHTLAYERS];
	private int[] _lastLight = new int[LIGHTLAYERS];
	private int _smoke;
	private int _fire;
	private int _explosive;
	private Position _pos;
	private boolean _cached;
	private BattleUnit _unit;
	private Vector<BattleItem> _inventory;
	private int _animationOffset;
	protected MapData[] _objects = new MapData[4];

/**
* constructor
* @param pos Position.
*/
public Tile(final Position pos)
{
	_discovered = false;
	_smoke = 0;
	_fire = 0;
	_explosive = 0;
	_pos = pos;
	_cached = false;
	_unit = null;

	for (int i = 0; i < 4; i++)
	{
		_objects[i] = null;
		_currentFrame[i] = 0;
	}
	for (int layer = 0; layer < LIGHTLAYERS; layer++)
	{
		_light[layer] = 0;
		_lastLight[layer] = 0;
	}
}

/**
 * destructor
 */
public void clearTile()
{
	_inventory.clear();
}

/**
 * Get the MapData pointer of a part of the tile.
 * @param part the part 0-3.
 * @return pointer to mapdata
 * @throws Exception 
 */
public MapData getMapData(int part)
{
	if (part < 0 || part > 3)
	{
		throw new Exception("unkown MapDataID part");
	}
	return _objects[part];
}

/**
 * Set the MapData references of part 0 to 3.
 * @param dat pointer to the data object
 * @param part the part number
 */
public void setMapData(MapData dat, int part)
{
	_objects[part] = dat;
	setCached(false);
}

/**
 * Gets wether this tile has no objects. Note that we can have a unit or smoke on this tile.
 * @return boolean True if there is nothing but air on this tile.
 */
public boolean isVoid()
{
	return _objects[0] == null && _objects[1] == null && _objects[2] == null && _objects[3] == null;
}

/**
 * Get the TU cost to walk over a certain part of the tile.
 * @param part
 * @param movementType
 * @return TU cost
 */
public int getTUCost(int part, MovementType movementType)
{
	if (_objects[part] != null)
		return _objects[part].getTUCost(movementType);
	else
		return 0;
}

/**
 * Whether this tile has a floor or not. If no object defined as floor, it has no floor.
 * @return boolean
 */
public boolean hasNoFloor()
{
	if (_objects[MapData.O_FLOOR] != null)
		return _objects[MapData.O_FLOOR].isNoFloor();
	else
		return true;
}

/**
 * Whether this tile has a big wall.
 * @return boolean
 */
public boolean isBigWall()
{
	if (_objects[MapData.O_OBJECT] != null)
		return _objects[MapData.O_OBJECT].isBigWall();
	else
		return false;
}

/**
 * If an object stand on this tile, this returns how high the unit is it standing.
 * @return the level in pixels
 */
public int getTerrainLevel()
{
	int level = 0;

	if (_objects[MapData.O_FLOOR] != null)
		level = _objects[MapData.O_FLOOR].getTerrainLevel();
	if (_objects[MapData.O_OBJECT] != null)
		level += _objects[MapData.O_OBJECT].getTerrainLevel();

	return level;
}

/**
 * Gets the tile's position.
 * @return position
 */
public final Position getPosition()
{
	return _pos;
}


/**
 * Gets the tile's footstep sound.
 * @return sound ID
 */
public int getFootstepSound()
{
	int sound = 0;

	if (_objects[MapData.O_FLOOR] != null)
		sound = _objects[MapData.O_FLOOR].getFootstepSound();
	if (_objects[MapData.O_OBJECT] != null)
		sound = _objects[MapData.O_OBJECT].getFootstepSound();

	return sound;
}


/**
 * Open a door on this tile.
 * @param part
 * @return a value: 0(normal door), 1(ufo door) or -1 if no door opened or 3 if ufo door(=animated) is still opening
 */
public int openDoor(int part)
{
	if (_objects[part] == null) return -1;

	if (_objects[part].isDoor())
	{
		setMapData(_objects[part].getDataset().getObjects().get(_objects[part].getAltMCD()),
				   _objects[part].getDataset().getObjects().get(_objects[part].getAltMCD()).getObjectType());
		setMapData(null, part);
		return 0;
	}
	if (_objects[part].isUFODoor() && _currentFrame[part] == 0) // ufo door part 0 - door is closed
	{
		_currentFrame[part] = 1; // start opening door
		return 1;
	}
	if (_objects[part].isUFODoor() && _currentFrame[part] != 7) // ufo door != part 7 - door is still opening
	{
		return 3;
	}
	return -1;
}

/**
 * Check if the ufo door is open or opening. Used for visibility/light blocking checks.
 * @param part
 * @return boolean
 */
public boolean isUfoDoorOpen(int part)
{
	if (_objects[part] != null && _objects[part].isUFODoor() && _currentFrame[part] != 0)
	{
		return true;
	}
	return false;
}

public int closeUfoDoor()
{
	int retval = 0;

	for (int part = 0; part < 4; part++)
	{
		if (isUfoDoorOpen(part))
		{
			_currentFrame[part] = 0;
			retval = 1;
			setCached(false);
		}
	}

	return retval;
}

/**
 * Sets the tile's cache flag. Set when objects or lighting on this tile changed.
 * @param cached
 */
public void setCached(boolean cached)
{
	_cached = cached;
}

/**
 * Check if the tile is still cached in the Map cache.
 * When the tile changes (door/lighting/destroyed), it needs to be re-cached.
 * @return boolean
 */
public boolean isCached()
{
	return _cached;
}

/**
 * Sets the tile's cache flag. - TODO: set this for each object seperatly?
 * @param flag true/false
 */
public void setDiscovered(boolean flag)
{
	if (_discovered != flag)
	{
		_discovered = flag;
		setCached(false);
		// if light on tile changes, units and objects on it change light too
		if (_unit != null)
		{
			_unit.setCached(false);
		}
	}
}

/**
 * Get the black fog of war state of this tile.
 * @return boolean True = discovered the tile.
 */
public boolean isDiscovered()
{
	return _discovered;
}


/**
 * Reset the light amount on the tile. This is done before a light level recalculation.
 * @param layer Light is seperated in 3 layers: Ambient, Static and Dynamic.
 */
public void resetLight(int layer)
{
	_lastLight[layer] = _light[layer];
	_light[layer] = 0;
}

/**
 * Add the light amount on the tile. Only add light if the current light is lower.
 * @param light Amount of light to add.
 * @param layer Light is seperated in 3 layers: Ambient, Static and Dynamic.
 */
public void addLight(int light, int layer)
{
	if (_light[layer] < light)
		_light[layer] = light;
}

/**
 * Tiles that have their light amount changed, need to be re-cached.
 * @param layer Light is seperated in 3 layers: Ambient, Static and Dynamic.
 */
public void checkForChangedLight(int layer)
{
	if (_lastLight[layer] != _light[layer])
	{
		setCached(false);
		// if light on tile changes, units and objects on it change light too
		if (_unit != null)
		{
			_unit.setCached(false);
		}
	}
}

/**
 * Gets the tile's shade amount 0-15. It returns the brightest of all light layers.
 * Shade level is the inverse of light level. So a maximum amount of light (15) returns shade level 0.
 * @return shade
 */
public int getShade()
{
	int light = 0;

	for (int layer = 0; layer < LIGHTLAYERS; layer++)
	{
		if (_light[layer] > light)
			light = _light[layer];
	}

	return 15 - light;
}

/**
 * Destroy a part on this tile. We first remove the old object, then replace it with the destroyed one.
 * This is because the object type of the old and new one are not nescessarly the same.
 * @param part
 */
public void destroy(int part)
{
	if (_objects[part] != null)
	{
		MapData originalPart = _objects[part];
		setMapData(null, part);
		if (originalPart.getDieMCD() != 0)
		{
			MapData dead = originalPart.getDataset().getObjects().get(originalPart.getDieMCD());
			setMapData(dead, dead.getObjectType());
		}
	}
	/* check if the floor on the lowest level is gone */
	if (part == MapData.O_FLOOR && getPosition().z == 0 && _objects[MapData.O_FLOOR] == null)
	{
		/* replace with scourched earth */
		setMapData(MapDataSet.getScourgedEarthTile(), MapData.O_FLOOR);
	}


}

/* damage terrain  - check against armor*/
public void damage(int part, int power)
{
	if (power >= _objects[part].getArmor())
		destroy(part);
}


/**
 * Set a "virtual" explosive on this tile. We mark a tile this way to detonate it later.
 * We do it this way, because the same tile can be visited multiple times by an "explosion ray".
 * The explosive power on the tile is some kind of moving average of the explosive rays that passes it.
 * @param power
 */
public void setExplosive(int power)
{
	if (_explosive != 0)
	{
		_explosive = (_explosive + power) / 2;
	}
	else
	{
		_explosive = power;
	}
}

/**
 * Apply the explosive power to the tile parts. This is where the actual destruction takes place.
 * Normally the explosive value is set to zero after this.
 * TODO : but with secondary explosions this value is set to the explosive power of the object.
 * TODO : these will be checked later and trigger the secondary explosions.
 */
public void detonate()
{
	int decrease;

	if (_explosive != 0)
	{
		// explosions create smoke which only stays 1 or 2 turns
		addSmoke(1);
		for (int i = 0; i < 4; i++)
		{
			if(_objects[i] != null)
			{
				if ((_explosive) >= _objects[i].getArmor())
				{
					decrease = _objects[i].getArmor();
					destroy(i);
					addSmoke(2);
					if (_objects[i] != null && (_explosive - decrease) >= _objects[i].getArmor())
					{
						destroy(i);
					}
				}
			}
		}
		// flammable of the tile needs to be 20 or lower (lower is better chance of catching fire) to catch fire
		// note that when we get here, flammable objects can already be destroyed by the explosion, thus not catching fire.
		int flam = getFlammability();
		if (flam <= 20)
		{
			if (RNG.generate(0, 20) - flam >= 0)
			{
				ignite();
			}
		}
		_explosive = 0;
	}
}

/*
 * Flammability of a tile is the lowest flammability of it's objects.
 * @return Flammability : the lower the value, the higher the chance the tile/object catches fire.
 */
public int getFlammability()
{
	int flam = 255;

	for (int i=0; i < 4; i++)
	{
		if (_objects[i] != null)
		{
			if (_objects[i].getFlammable() < flam)
			{
				flam = _objects[i].getFlammable();
			}
		}
	}
	return flam;
}

/*
 * Ignite starts fire on a tile, it will burn <fuel> rounds. Fuel of a tile is the highest fuel of it's objects.
 * NOT the sum of the fuel of the objects! TODO: check if this is like in the original.
 */
public void ignite()
{
	int fuel = 0;

	for (int i=0; i < 4; i++)
	{
		if (_objects[i] != null)
		{
			if (_objects[i].getFuel() > fuel)
			{
				fuel = _objects[i].getFuel();
			}
		}
	}
	setFire(fuel + 1);
	if (fuel > 1)
	{
		addSmoke(fuel * 2); // not sure
	}
}

/**
 * Animate the tile. This means to advance the current frame for every object.
 * Ufo doors are a bit special, they animated only when triggered.
 * When ufo doors are on frame 0(closed) or frame 7(open) they are not animated further.
 */
public void animate()
{
	int newframe;
	for (int i=0; i < 4; i++)
	{
		if (_objects[i] != null)
		{
			if (_objects[i].isUFODoor() && (_currentFrame[i] == 0 || _currentFrame[i] == 7)) // ufo door is static
			{
				continue;
			}
			newframe = _currentFrame[i] + 1;
			if (newframe == 8)
			{
				newframe = 0;
			}
			// only re-cache when the object actually changed.
			if (_objects[i].getSprite(_currentFrame[i]) != _objects[i].getSprite(newframe))
			{
				setCached(false);
			}
			_currentFrame[i] = newframe;
		}
	}
}

/**
 * Get the sprite of a certain part of the tile.
 * @param part
 * @return Pointer to the sprite.
 */
public Surface getSprite(int part)
{
	return _objects[part].getDataset().getSurfaceset().getFrame(_objects[part].getSprite(_currentFrame[part]));
}

/**
 * Set a unit on this tile.
 * @param unit
 */
public void setUnit(BattleUnit unit)
{
	_unit = unit;
}

/**
 * Get the unit on this tile.
 * @return BattleUnit.
 */
public BattleUnit getUnit()
{
	if (_unit != null && _unit.isOut())
		return null;
	else
		return _unit;
}

/**
 * Set the amount of turns this tile is on fire. 0 = no fire.
 * @param fire : amount of turns this tile is on fire.
 */
public void setFire(int fire)
{
	_fire = fire;
	_animationOffset = RNG.generate(0,3);
}

/**
 * Get the amount of turns this tile is on fire. 0 = no fire.
 * @return fire : amount of turns this tile is on fire.
 */
public int getFire()
{
	return _fire;
}

/**
 * Set the amount of turns this tile is smoking. 0 = no smoke.
 * @param smoke : amount of turns this tile is smoking.
 */
public void addSmoke(int smoke)
{
	_smoke += smoke;
	if (_smoke > 40) _smoke = 40;
	_animationOffset = RNG.generate(0,3);
}

/**
 * Get the amount of turns this tile is smoking. 0 = no smoke.
 * @return smoke : amount of turns this tile is smoking.
 */
public int getSmoke()
{
	return _smoke;
}

/**
 * Get the number of frames the fire or smoke animation is off-sync.
 * To public void fire and smoke animations of different tiles moving nice in sync - it looks fake.
 * @return offset
 */
public int getAnimationOffset()
{
	return _animationOffset;
}

/**
 * Add an item on the tile.
 * @param item
 */
public void addItem(BattleItem item)
{
	_inventory.add(item);
	setCached(false);
}

/**
 * Get the topmost item sprite to draw on the battlescape.
 * @return item sprite ID in floorob, or -1 when no item
 */
public int getTopItemSprite()
{
	if (!_inventory.isEmpty())
	{
		return _inventory.get(0).getRules().getFloorSprite();
	}
	else
	{
		return -1;
	}
}

/**
 * New turn preparations. Decrease smoke and fire timers.
 */
public void prepareNewTurn()
{
	_smoke--;
	if (_smoke < 0) _smoke = 0;

	if (_fire == 1)
	{
		// fire will be finished in this turn
		// destroy all objects that burned, and try to ignite again
		for (int i = 0; i < 4; i++)
		{
			if(_objects[i] != null)
			{
				if (_objects[i].getFlammable() < 255)
				{
					destroy(i);
				}
			}
		}
		if (getFlammability() < 255)
		{
			ignite();
		}
		else
		{
			_fire = 0;
		}
	}
	else
	{
		_fire--;
		if (_fire < 0) _fire = 0;
	}
}

/**
 * Set whether we checked this tile.
 * @param flag
 */
public void setChecked(boolean flag)
{
	_checked = flag;
}

/**
 * Get whether we checked this tile.
 * @return flag
 */
public boolean getChecked()
{
	return _checked;
}

/**
 * Get the inventory on this tile.
 * @return pointer to a vector of battleitems.
 */
public Vector<BattleItem > getInventory()
{
	return _inventory;
}

}
