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
package putzworks.openXcom.Battlescape;

import java.util.Vector;

import putzworks.openXcom.Engine.RNG;
import putzworks.openXcom.Ruleset.MapData;
import putzworks.openXcom.Ruleset.RuleItem.ItemDamageType;
import putzworks.openXcom.Savegame.BattleItem;
import putzworks.openXcom.Savegame.BattleUnit;
import putzworks.openXcom.Savegame.SavedBattleGame;
import putzworks.openXcom.Savegame.Tile;

public class TerrainModifier
{
	private SavedBattleGame _save;
	private Vector<Integer> _voxelData;

/**
 * Sets up a TerrainModifier.
 * @param save pointer to SavedBattleGame object.
 */
public TerrainModifier(SavedBattleGame save, Vector<Integer> voxelData)
{
	_save = save;
	_voxelData = voxelData;

}


/**
  * Calculate sun shading for the whole terrain.
  */
public void calculateSunShading()
{
	for (int i = 0; i < _save.getWidth() * _save.getLength() * _save.getHeight(); ++i)
	{
		calculateSunShading(_save.getTiles()[i]);
	}
}

/**
  * Calculate sun shading for 1 tile. Sun comes from above and is blocked by floors or objects.
  * @param tile The tile to calculate sun shading for.
  */
public void calculateSunShading(Tile tile)
{
	int layer = 0; // Ambient lighting layer.

	int power = 15 - _save.getGlobalShade();

	// At night/dusk sun isn't dropping shades
	if (_save.getGlobalShade() <= 5)
	{
		if (verticalBlockage(_save.getTile(new Position(tile.getPosition().x, tile.getPosition().y, _save.getHeight() - 1)), tile, ItemDamageType.DT_NONE) != 0)
		{
			power-=2;
		}
	}

	tile.addLight(power, layer);
}

/**
  * Recalculate lighting for the terrain: objects,items,fire.
  */
public void calculateTerrainLighting()
{
	int layer = 1; // Static lighting layer.
	int fireLightPower = 15; // amount of light a fire generates

	// during daytime don't calculate lighting
	if (_save.getGlobalShade() < 1)
		return;

	// reset all light to 0 first
	for (int i = 0; i < _save.getWidth() * _save.getLength() * _save.getHeight(); ++i)
	{
		_save.getTiles()[i].resetLight(layer);
	}

	// add lighting of terrain
	for (int i = 0; i < _save.getWidth() * _save.getLength() * _save.getHeight(); ++i)
	{
		// only floors and objects can light up
		if (_save.getTiles()[i].getMapData(MapData.O_FLOOR) != null
			&& _save.getTiles()[i].getMapData(MapData.O_FLOOR).getLightSource() != 0)
		{
			addLight(_save.getTiles()[i].getPosition(), _save.getTiles()[i].getMapData(MapData.O_FLOOR).getLightSource(), layer);
		}
		if (_save.getTiles()[i].getMapData(MapData.O_OBJECT) != null
			&& _save.getTiles()[i].getMapData(MapData.O_OBJECT).getLightSource() != 0)
		{
			addLight(_save.getTiles()[i].getPosition(), _save.getTiles()[i].getMapData(MapData.O_OBJECT).getLightSource(), layer);
		}

		// fires
		if (_save.getTiles()[i].getFire() != 0)
		{
			addLight(_save.getTiles()[i].getPosition(), fireLightPower, layer);
		}

	}

	// todo: add lighting of items (flares)

	// set changed light tiles to uncached
	for (int i = 0; i < _save.getWidth() * _save.getLength() * _save.getHeight(); ++i)
	{
		_save.getTiles()[i].checkForChangedLight(layer);
	}
}

/**
  * Recalculate lighting for the units.
  */
public void calculateUnitLighting()
{
	int layer = 2; // Dynamic lighting layer.
	int personalLightPower = 15; // amount of light a unit generates

	// during daytime don't calculate lighting
	if (_save.getGlobalShade() < 1)
		return;

	// reset all light to 0 first
	for (int i = 0; i < _save.getWidth() * _save.getLength() * _save.getHeight(); ++i)
	{
		_save.getTiles()[i].resetLight(layer);
	}

	// add lighting of soldiers
	for (BattleUnit i: _save.getUnits())
	{
		if ((i).getFaction() == BattleUnit.UnitFaction.FACTION_PLAYER && !(i).isOut())
		{
			addLight((i).getPosition(), personalLightPower, layer);
		}
	}

	// set changed light tiles to uncached
	for (int i = 0; i < _save.getWidth() * _save.getLength() * _save.getHeight(); ++i)
	{
		_save.getTiles()[i].checkForChangedLight(layer);
	}
}

/**
 * Calculates line of sight of a soldier. For every visible tile fog of war is removed.
 * @param unit
 */
public void calculateFOV(BattleUnit unit)
{
	// units see 90 degrees sidewards.
	double startAngle[] = { 45, 0, -45, 270, 225, 180, 135, 90 };
	double endAngle[] = { 135, 90, 45, 360, 315, 270, 225, 180 };

	double centerZ = (unit.getPosition().z * 2) + 1.5;
	double centerX = unit.getPosition().x + 0.5;
	double centerY = unit.getPosition().y + 0.5;
	int power_, objectFalloff;

	// units see 90 degrees down and 60 degrees up.
	double startFi = -90;
	double endFi = 60;

	if (unit.getPosition().z == 0)
	{
		startFi = 0;
	}

	// we see the tile we are standing on
	if (unit.getFaction() == BattleUnit.UnitFaction.FACTION_PLAYER)
	{
		_save.getTile(unit.getPosition()).setDiscovered(true);
	}

	unit.clearVisibleUnits();
	for (int i = 0; i < _save.getWidth() * _save.getLength() * _save.getHeight(); ++i)
	{
		_save.getTiles()[i].setChecked(false);
	}


	// raytrace up and down
	for (double fi = startFi; fi <= endFi; fi += 6)
	{
		double cos_fi = Math.cos(fi * Math.PI / 180.0);
		double sin_fi = Math.sin(fi * Math.PI / 180.0);

		// raytrace every 3 degrees makes sure we cover all tiles in a circle.
		for (double te = startAngle[unit.getDirection()]; te <= endAngle[unit.getDirection()]; te += 3)
		{
			double cos_te = Math.cos(te * Math.PI / 180.0);
			double sin_te = Math.sin(te * Math.PI / 180.0);

			Tile origin = _save.getTile(unit.getPosition());
			double l = 0;
			double vx, vy, vz;
			int tileX, tileY, tileZ;
			power_ = 20;

			while (power_ > 0)
			{
				l++;
				vx = centerX + l * cos_te * cos_fi;
				vy = centerY + l * sin_te * cos_fi;
				vz = centerZ + l * sin_fi;

				tileZ = (int)(Math.floor(vz / 2.0));
				tileX = (int)(Math.floor(vx));
				tileY = (int)(Math.floor(vy));

				power_--;

				Tile dest = _save.getTile(new Position(tileX, tileY, tileZ));
				if (dest == null) break; // out of map!

				// horizontal blockage by walls
				power_ -= horizontalBlockage(origin, dest, ItemDamageType.DT_NONE);

				// vertical blockage by ceilings/floors
				power_ -= verticalBlockage(origin, dest, ItemDamageType.DT_NONE);

				// objects on destination tile affect the ray after it has crossed this tile
				// but it has to be calculated before we affect the tile (it could have been blown up)
				if (dest.getMapData(MapData.O_OBJECT) != null)
				{
					objectFalloff = dest.getMapData(MapData.O_OBJECT).getBlock(ItemDamageType.DT_NONE);
				}
				else
				{
					objectFalloff = 0;
				}

				// smoke decreases visibility - but not for terrain
				/*if (dest.getSmoke())
				{
					objectFalloff += int(dest.getSmoke() / 3);
				}*/

				if (power_ > 0 && dest.getShade() < 10 && !dest.getChecked())
				{
					dest.setChecked(true);
					checkForVisibleUnits(unit, dest);
					if (unit.getFaction() == BattleUnit.UnitFaction.FACTION_PLAYER)
					{
						dest.setDiscovered(true);
					}
					// if there is a door to the east or south of a visible tile, we see that too
					if (unit.getFaction() == BattleUnit.UnitFaction.FACTION_PLAYER)
					{
						Tile t = _save.getTile(new Position(tileX + 1, tileY, tileZ));
						if (t != null && t.getMapData(MapData.O_WESTWALL) != null && (t.getMapData(MapData.O_WESTWALL).isDoor() || t.getMapData(MapData.O_WESTWALL).isUFODoor()))
						{
							t.setDiscovered(true);
						}
						t = _save.getTile(new Position(tileX, tileY - 1, tileZ));
						if (t != null && t.getMapData(MapData.O_NORTHWALL) != null && (t.getMapData(MapData.O_NORTHWALL).isDoor() || t.getMapData(MapData.O_NORTHWALL).isUFODoor()))
						{
							t.setDiscovered(true);
						}
					}
				}
				power_ -= objectFalloff;
				origin = dest;
			}
		}
	}
}

/**
 * Check for an opposing unit on this tile
 * @param unit
 * @param tile
 */
boolean checkForVisibleUnits(BattleUnit unit, Tile tile)
{
	BattleUnit bu = tile.getUnit();

	if (bu == null || bu.isOut())
	{
		return false;
	}

	if (unit.getFaction() == BattleUnit.UnitFaction.FACTION_PLAYER && (bu.getFaction() == BattleUnit.UnitFaction.FACTION_PLAYER || bu.getFaction() == BattleUnit.UnitFaction.FACTION_NEUTRAL))
	{
		return false;
	}

	if (unit.getFaction() == BattleUnit.UnitFaction.FACTION_HOSTILE && bu.getFaction() == BattleUnit.UnitFaction.FACTION_HOSTILE)
	{
		return false;
	}

	Position originVoxel, targetVoxel;
	originVoxel = new Position((unit.getPosition().x * 16) + 8, (unit.getPosition().y * 16) + 8, unit.getPosition().z*24);
	originVoxel.z += -tile.getTerrainLevel();
	originVoxel.z += unit.isKneeled()?unit.getUnit().getKneelHeight():unit.getUnit().getStandHeight();
	boolean unitSeen = false;

	targetVoxel = new Position((bu.getPosition().x * 16) + 8, (bu.getPosition().y * 16) + 8, bu.getPosition().z*24);
	targetVoxel.z += -_save.getTile(bu.getPosition()).getTerrainLevel();
	targetVoxel.z += bu.isKneeled()?bu.getUnit().getKneelHeight():bu.getUnit().getStandHeight();

	// cast a ray from the middle of the unit to the middle of this one
	int test = calculateLine(originVoxel, targetVoxel, false, null, null);
	Position hitPosition = new Position(targetVoxel.x/16, targetVoxel.y/16, targetVoxel.z/24);
	if (test == -1 || (test == 4 && bu.getPosition() == hitPosition))
	{
		unitSeen = true;
		unit.addToVisibleUnits(bu);
	}

	return unitSeen;
}


/**
 * Calculates line of sight of a soldiers within range of the Position.
 * TODO: review this, because it is recalculating all soldiers.
 * @param position
 */
public void calculateFOV(final Position position)
{
	for (BattleUnit i: _save.getUnits())
	{
		if ((i).getFaction() == _save.getSide())
		{
			calculateFOV(i);
		}
	}
}

/**
 * Adds circular light pattern starting from center and loosing power with distance travelled.
 * @param center
 * @param power
 * @param layer Light is seperated in 3 layers: Ambient, Static and Dynamic.
 */
private void addLight(final Position center, int power, int layer)
{
	// only loop through the positive quadrant.
	for (int x = 0; x <= power; x++)
	{
		for (int y = 0; y <= power; y++)
		{
			for (int z = 0; z < _save.getHeight(); z++)
			{
				int distance = (int)(Math.floor(Math.sqrt((float)(x*x + y*y)) + 0.5));

				if (_save.getTile(new Position(center.x + x,center.y + y, z)) != null)
					_save.getTile(new Position(center.x + x,center.y + y, z)).addLight(power - distance, layer);

				if (_save.getTile(new Position(center.x - x,center.y - y, z)) != null)
					_save.getTile(new Position(center.x - x,center.y - y, z)).addLight(power - distance, layer);

				if (_save.getTile(new Position(center.x - x,center.y + y, z)) != null)
					_save.getTile(new Position(center.x - x,center.y + y, z)).addLight(power - distance, layer);

				if (_save.getTile(new Position(center.x + x,center.y - y, z)) != null)
					_save.getTile(new Position(center.x + x,center.y - y, z)).addLight(power - distance, layer);
			}
		}
	}
}

/*
 * The amount this certain wall or floor-part of the tile blocks.
 * @param tile
 * @param part
 * @param affector
 * @return amount of blockage
 */
private int blockage(Tile tile, final int part, ItemDamageType type)
{
	int blockage = 0;

	if (tile == null) return 0; // probably outside the map here

	if (part == MapData.O_FLOOR && tile.getMapData(MapData.O_FLOOR) != null)
	{
		// blockage modifiers of floors in ufo only counted for horizontal stuff, so this is kind of an experiment
		if (type == ItemDamageType.DT_HE)
			blockage += 15;
		else
			blockage += 255;
	}
	else
	{
		if (tile.getMapData(part) != null)
			blockage += tile.getMapData(part).getBlock(type);

		// open ufo doors are actually still closed behind the scenes
		// so a special trick is needed to see if they are open, if they are, they obviously don't block anything
		if (tile.isUfoDoorOpen(part))
			blockage = 0;
	}

	return blockage;
}

/**
 * HE, smoke and fire explodes in a circular pattern on 1 level only. HE however damages floor tiles of the above level. Not the units on it.
 * HE destroys an object if its armor is lower than the explosive power, then it's HE blockage is applied for further propagation.
 * See http://www.ufopaedia.org/index.php?title=Explosions for more info.
 * TODO : use bresenham?
 * @param center
 * @param power
 * @param affector
 * @param maxRadius
 */
public void explode(final Position center, int power, ItemDamageType type, int maxRadius, BattleUnit unit)
{
	if (type == ItemDamageType.DT_AP)
	{
		int part = voxelCheck(center, unit);
		if (part >= 0 && part <= 3)
		{
			// power 25% to 75%
			_save.getTile(new Position(center.x/16, center.y/16, center.z/24)).damage(
				part, (int)(RNG.generate(power/4, (power*3)/4)));
		}
		else if (part == 4)
		{
			// power 0 - 200%
			_save.getTile(new Position(center.x/16, center.y/16, center.z/24)).getUnit().damage(
				new Position(center.x%16, center.y%16, center.z%24), RNG.generate(0, power*2));
		}
	}
	else
	{
		double centerZ = (int)(center.z / 24) + 0.5;
		double centerX = (int)(center.x / 16) + 0.5;
		double centerY = (int)(center.y / 16) + 0.5;
		int power_;

		if (type == ItemDamageType.DT_IN)
		{
			power /= 2;
		}

		// raytrace every 3 degrees makes sure we cover all tiles in a circle.
		for (double te = 0; te <= 360; te += 3)
		{
			double cos_te = Math.cos(te * Math.PI / 180.0);
			double sin_te = Math.sin(te * Math.PI / 180.0);

			Tile origin = _save.getTile(center);
			double l = 0;
			double vx, vy, vz;
			int tileX, tileY, tileZ;
			power_ = power;

			while (power_ > 0 && l <= maxRadius)
			{
				vx = centerX + l * cos_te;
				vy = centerY + l * sin_te;
				vz = centerZ;

				tileZ = (int)(Math.floor(vz));
				tileX = (int)(Math.floor(vx));
				tileY = (int)(Math.floor(vy));

				Tile dest = _save.getTile(new Position(tileX, tileY, tileZ));
				if (dest == null) break; // out of map!

				// horizontal blockage by walls
				power_ -= horizontalBlockage(origin, dest, type);

				if (power_ > 0)
				{
					if (type == ItemDamageType.DT_HE)
					{
						// explosives do 1/2 damage to terrain and 1/2 up to 3/2 random damage to units
						dest.setExplosive(power_ / 2);
						// power 50 - 150%
						if (dest.getUnit() != null)
							dest.getUnit().damage(new Position(0, 0, 0), (int)(RNG.generate(power_/2.0, power_*1.5)));

						// TODO: destroy floors above

					}
					if (type == ItemDamageType.DT_SMOKE)
					{
						// smoke from explosions always stay 15 to 20 turns
						if (dest.getSmoke() < 10)
						{
							dest.addSmoke(RNG.generate(15, 20));
						}
					}
					if (type == ItemDamageType.DT_IN)
					{
						if (dest.getFire() == 0)
						{
							dest.ignite();
						}
					}
				}

				power_ -= 10; // explosive damage decreases by 10

				// objects on destination tile affect the ray after it has crossed this tile
				// but it has to be calculated before we affect the tile (it could have been blown up)
				if (dest.getMapData(MapData.O_OBJECT) != null)
				{
					power_ -= dest.getMapData(MapData.O_OBJECT).getBlock(type);
				}
				origin = dest;
				l++;
			}
		}

		// indicate we have finished recalculating
		if (type == ItemDamageType.DT_HE)
		{
			for (int i = 0; i < _save.getWidth() * _save.getLength() * _save.getHeight(); ++i)
			{
				_save.getTiles()[i].detonate();
			}
		}
	}

	// recalculate line of sight (to optimise: only units in range)
	calculateFOV(center);
	calculateTerrainLighting(); // fires could have been started
}

/**
 * The amount of power that is blocked going from one tile to another on a different level.
 * Can cross more than one level. Only floor tiles are taken into account.
 * @param startTile
 * @param endTile
 * @param affector
 * @return amount of blockage
 */
private int verticalBlockage(Tile startTile, Tile endTile, ItemDamageType type)
{
	int block = 0;

	// safety check
	if (startTile == null || endTile == null) return 0;

	int direction = endTile.getPosition().z - startTile.getPosition().z;
	int x = startTile.getPosition().x;
	int y = startTile.getPosition().y;

	if (direction < 0) // down
	{
		for (int z = startTile.getPosition().z; z > endTile.getPosition().z; z--)
		{
			block += blockage(_save.getTile(new Position(x, y, z)), MapData.O_FLOOR, type);
		}
	}
	else if (direction > 0) // up
	{
		for (int z = startTile.getPosition().z + 1; z <= endTile.getPosition().z; z++)
		{
			block += blockage(_save.getTile(new Position(x, y, z)), MapData.O_FLOOR, type);
		}
	}

	return block;
}

/**
 * The amount of power that is blocked going from one tile to another on the same level.
 * @param startTile
 * @param endTile
 * @param affector
 * @return amount of blockage
 */
private int horizontalBlockage(Tile startTile, Tile endTile, ItemDamageType type)
{
	// safety check
	if (startTile == null || endTile == null) return 0;

	int direction = vectorToDirection(endTile.getPosition().minus(startTile.getPosition()));
	if (direction == -1) return 0;

	switch(direction)
	{
	case 0:	// north
		return blockage(startTile, MapData.O_NORTHWALL, type);
//		break;
	case 1: // north east
		return (blockage(startTile,MapData.O_NORTHWALL, type) + blockage(endTile,MapData.O_WESTWALL, type))/2
			+ (blockage(_save.getTile(startTile.getPosition().plus(new Position(1, 0, 0))),MapData.O_WESTWALL, type)
			+ blockage(_save.getTile(startTile.getPosition().plus(new Position(1, 0, 0))),MapData.O_NORTHWALL, type))/2;
//		break;
	case 2: // east
		return blockage(endTile,MapData.O_WESTWALL, type);
//		break;
	case 3: // south east
		return (blockage(endTile,MapData.O_WESTWALL, type) + blockage(endTile,MapData.O_NORTHWALL, type))/2
			+ (blockage(_save.getTile(startTile.getPosition().plus(new Position(1, 0, 0))),MapData.O_WESTWALL, type)
			+ blockage(_save.getTile(startTile.getPosition().plus(new Position(0, -1, 0))),MapData.O_NORTHWALL, type))/2;
//		break;
	case 4: // south
		return blockage(endTile,MapData.O_NORTHWALL, type);
//		break;
	case 5: // south west
		return (blockage(endTile,MapData.O_NORTHWALL, type) + blockage(startTile,MapData.O_WESTWALL, type))/2
			+ (blockage(_save.getTile(startTile.getPosition().plus(new Position(0, -1, 0))),MapData.O_WESTWALL, type)
			+ blockage(_save.getTile(startTile.getPosition().plus(new Position(0, -1, 0))),MapData.O_NORTHWALL, type))/2;
//		break;
	case 6: // west
		return blockage(startTile,MapData.O_WESTWALL, type);
//		break;
	case 7: // north west
		return (blockage(startTile,MapData.O_WESTWALL, type) + blockage(startTile,MapData.O_NORTHWALL, type))/2
			+ (blockage(_save.getTile(startTile.getPosition().plus(new Position(0, 1, 0))),MapData.O_WESTWALL, type)
			+ blockage(_save.getTile(startTile.getPosition().plus(new Position(-1, 0, 0))),MapData.O_NORTHWALL, type))/2;
//		break;
	}

	return 0;
}

/*
 * Converts direction to a vector. Direction starts north = 0 and goes clockwise.
 * @param vector pointer to a position (which acts as a vector)
 * @return direction
 */
private int vectorToDirection(final Position vector)
{
	int x[] = {0, 1, 1, 1, 0, -1, -1, -1};
	int y[] = {1, 1, 0, -1, -1, -1, 0, 1};
	for (int i=0;i<9;++i)
	{
		if (x[i] == vector.x && y[i] == vector.y)
			return i;
	}
	return -1;
}

/**
 * Soldier opens a door (if any) by rightclick, or by walking through it. The unit has to face in the right direction.
 * @param unit
 * @return -1 there is no door, you can walk through.
 *          0 normal door opened, make a squeeky sound and you can walk through.
 *          1 ufo door is starting to open, make a woosh sound, don't walk through.
 *          3 ufo door is still opening, don't walk through it yet. (have patience, futuristic technology...)
 */
public int unitOpensDoor(BattleUnit unit)
{
	int door = -1;
	Tile tile = _save.getTile(unit.getPosition());
	switch(unit.getDirection())
	{
	case 0:	// north
		door = tile.openDoor(MapData.O_NORTHWALL);
		if (door == 1)
		{
			// check for adjacent door(s)
			tile = _save.getTile(unit.getPosition().plus(new Position(1, 0, 0)));
			if (tile != null) tile.openDoor(MapData.O_NORTHWALL);
			tile = _save.getTile(unit.getPosition().plus(new Position(-1, 0, 0)));
			if (tile != null) tile.openDoor(MapData.O_NORTHWALL);
		}
		break;
	case 2: // east
		tile = _save.getTile(tile.getPosition().plus(new Position(1, 0, 0)));
		if (tile != null) door = tile.openDoor(MapData.O_WESTWALL);
		if (door == 1)
		{
			// check for adjacent door(s)
			tile = _save.getTile(unit.getPosition().plus(new Position(1, -1, 0)));
			if (tile != null) tile.openDoor(MapData.O_WESTWALL);
			tile = _save.getTile(unit.getPosition().plus(new Position(1, 1, 0)));
			if (tile != null) tile.openDoor(MapData.O_WESTWALL);
		}
		break;
	case 4: // south
		tile = _save.getTile(tile.getPosition().plus(new Position(0, -1, 0)));
		if (tile != null) door = tile.openDoor(MapData.O_NORTHWALL);
		if (door == 1)
		{
			// check for adjacent door(s)
			tile = _save.getTile(unit.getPosition().plus(new Position(1, -1, 0)));
			if (tile != null) tile.openDoor(MapData.O_NORTHWALL);
			tile = _save.getTile(unit.getPosition().plus(new Position(-1, -1, 0)));
			if (tile != null) tile.openDoor(MapData.O_NORTHWALL);
		}
		break;
	case 6: // west
		door = tile.openDoor(MapData.O_WESTWALL);
		if (door == 1)
		{
			// check for adjacent door(s)
			tile = _save.getTile(unit.getPosition().plus(new Position(0, -1, 0)));
			if (tile != null) tile.openDoor(MapData.O_WESTWALL);
			tile = _save.getTile(unit.getPosition().plus(new Position(0, 1, 0)));
			if (tile != null) tile.openDoor(MapData.O_WESTWALL);
		}
		break;
	}


	if (door == 0 || door == 1)
	{
		_save.getTerrainModifier().calculateFOV(tile.getPosition());
	}

	return door;
}

/**
 * calculateLine. Using bresenham algorithm in 3D.
 * @param origin
 * @param target
 * @param storeTrajectory true will store the whole trajectory - otherwise it just stores the last position.
 * @return the objectnumber(0-3) or unit(4) or out of map (5) or -1(hit nothing)
 */
public int calculateLine(final Position origin, final Position target, boolean storeTrajectory, Vector<Position> trajectory, BattleUnit excludeUnit)
{
	int x, x0, x1, delta_x, step_x;
    int y, y0, y1, delta_y, step_y;
    int z, z0, z1, delta_z, step_z;
    boolean swap_xy, swap_xz;
    int drift_xy, drift_xz;
    int cx, cy, cz;

    //start and end points
    x0 = origin.x;     x1 = target.x;
    y0 = origin.y;     y1 = target.y;
    z0 = origin.z;     z1 = target.z;

    //'steep' xy Line, make longest delta x plane
    swap_xy = Math.abs(y1 - y0) > Math.abs(x1 - x0);
    if (swap_xy)
	{
    	//swap x0 and y0
    	int temp = x0;
    	x0 = y0;
    	y0 = temp;
    	//swap x1 and y1
    	temp = x1;
    	x1 = y1;
    	y1 = temp;
	}

    //do same for xz
    swap_xz = Math.abs(z1 - z0) > Math.abs(x1 - x0);
    if (swap_xz)
	{
    	//swap x0 and z0
    	int temp = x0;
    	x0 = z0;
    	z0 = temp;
    	//swap x1 and z1
    	temp = x1;
    	x1 = z1;
    	z1 = temp;
	}

    //delta is Length in each plane
    delta_x = Math.abs(x1 - x0);
    delta_y = Math.abs(y1 - y0);
    delta_z = Math.abs(z1 - z0);

    //drift controls when to step in 'shallow' planes
    //starting value keeps Line centred
    drift_xy  = (delta_x / 2);
    drift_xz  = (delta_x / 2);

    //direction of line
	step_x = 1;  if (x0 > x1) {  step_x = -1; }
	step_y = 1;  if (y0 > y1) {  step_y = -1; }
	step_z = 1;  if (z0 > z1) {  step_z = -1; }

    //starting point
    y = y0;
    z = z0;

    //step through longest delta (which we have swapped to x)
    for (x = x0; x != x1; x += step_x)
	{
        //copy position
        cx = x;    cy = y;    cz = z;

        //unswap (in reverse)
        if (swap_xz) 
        {
        	//swap cx and cz
        	int temp = cx;
        	cx = cz;
        	cz = temp;
        }
        if (swap_xy) 
        	{
        	//swap cx and cy
        	int temp = cx;
        	cx = cy;
        	cy = temp;
        	}

		if (storeTrajectory)
		{
			trajectory.add(new Position(cx, cy, cz));
		}
        //passes through this point?
		int result = voxelCheck(new Position(cx, cy, cz), excludeUnit);
		if (result != -1)
		{
			if (!storeTrajectory && trajectory != null)
			{ // store the position of impact
				trajectory.add(new Position(cx, cy, cz));
			}
			return result;
		}

        //update progress in other planes
        drift_xy = drift_xy - delta_y;
        drift_xz = drift_xz - delta_z;

        //step in y plane
        if (drift_xy < 0)
		{
            y = y + step_y;
            drift_xy = drift_xy + delta_x;
		}

        //same in z
        if (drift_xz < 0)
		{
            z = z + step_z;
            drift_xz = drift_xz + delta_x;
		}
	}

	return -1;
}

/**
 * Calculate a parabola trajectory, used for throwing items.
 * @param origin in voxelspace
 * @param target in voxelspace
 * @param storeTrajectory true will store the whole trajectory - otherwise it just stores the last position.
 * @param excludeUnit makes sure the trajectory does not hit the shooter itself
 * @param curvature how high the parabola goes: 1.0 is almost straight throw, 3.0 is a very high throw, to throw over a fence for example
 * @param accuracy is the deviation of the angles it should take into account. 1.0 is perfection.
 * @return the objectnumber(0-3) or unit(4) or out of map (5) or -1(hit nothing)
 */
public int calculateParabola(final Position origin, final Position target, boolean storeTrajectory, Vector<Position> trajectory, BattleUnit excludeUnit, double curvature, double accuracy)
{
    double ro = Math.sqrt((double)((target.x - origin.x) * (target.x - origin.x) + (target.y - origin.y) * (target.y - origin.y) + (target.z - origin.z) * (target.z - origin.z)));

    double fi = Math.acos((double)(target.z - origin.z) / ro);
    double te = Math.atan2((double)(target.y - origin.y), (double)(target.x - origin.x));

	fi *= accuracy;
	te *= accuracy;

    double zA = Math.sqrt(ro)*curvature;
    double zK = 4.0 * zA / ro / ro;

    int x = origin.x;
	int y = origin.y;
	int z = origin.z;
    int i = 8;

    while (z > 0) {
        x = (int)((double)origin.x + (double)i * Math.cos(te) * Math.sin(fi));
        y = (int)((double)origin.y + (double)i * Math.sin(te) * Math.sin(fi));
        z = (int)((double)origin.z + (double)i * Math.cos(fi) - zK * ((double)i - ro / 2.0) * ((double)i - ro / 2.0) + zA);
		if (storeTrajectory)
		{
			trajectory.add(new Position(x, y, z));
		}
        //passes through this point?
		int result = voxelCheck(new Position(x, y, z), excludeUnit);
		if (result != -1)
		{
			if (!storeTrajectory && trajectory != null)
			{ // store the position of impact
				trajectory.add(new Position(x, y, z));
			}
			return result;
		}
        ++i;
    }
	return -1;
}

/**
 * check if we hit a voxel.
 * @return the objectnumber(0-3) or unit(4) or out of map (5) or -1(hit nothing)
 */
private int voxelCheck(final Position voxel, BattleUnit excludeUnit)
{

	Tile tile = _save.getTile(new Position(voxel.x/16, voxel.y/16, voxel.z/24));
	// check if we are not out of the map
	if (tile == null)
	{
		return 5;
	}

	BattleUnit unit = tile.getUnit();
	if (unit != null && unit != excludeUnit)
	{
		if ((voxel.z%24) < (unit.isKneeled()?unit.getUnit().getKneelHeight():unit.getUnit().getStandHeight()))
		{
			int x = 15 - voxel.x%16;
			int y = 15 - voxel.y%16;
			int idx = (unit.getUnit().gotLoftemps() * 16) + y;
			if ((_voxelData.get(idx) & (1 << x))==(1 << x))
			{
				return 4;
			}
		}
	}

	for (int i=0; i< 4; ++i)
	{
		MapData mp = tile.getMapData(i);
		if (mp != null)
		{
			int x = 15 - voxel.x%16;
			int y = 15 - voxel.y%16;
			int idx = (mp.getLoftID((voxel.z%24)/2)*16) + y;
			if ((_voxelData.get(idx) & (1 << x))==(1 << x))
			{
				return i;
			}
		}
	}
	return -1;
}

/**
 * Add item & affect with gravity.
 * @param position
 * @param item
 */
public void spawnItem(final Position position, BattleItem item)
{
	Position p = position;

	// don't spawn anything outside of bounds
	if (_save.getTile(p) == null)
		return;

	while (_save.getTile(p).getMapData(MapData.O_FLOOR) == null && p.z > 0)
	{
		p.z--;
	}

	_save.getTile(p).addItem(item);
}

/**
 * Close ufo doors.
 * @return whether doors are closed.
 */
public int closeUfoDoors()
{
	int doorsclosed = 0;

	// prepare a list of tiles on fire/smoke & close any ufo doors
	for (int i = 0; i < _save.getWidth() * _save.getLength() * _save.getHeight(); ++i)
	{
		doorsclosed += _save.getTiles()[i].closeUfoDoor();
	}

	return doorsclosed;
}



/**
 * New turn preparations. Like fire and smoke spreading.
 */
public void prepareNewTurn()
{
	Vector<Tile> tilesOnFire = new Vector<Tile>();
	Vector<Tile> tilesOnSmoke = new Vector<Tile>();

	// prepare a list of tiles on fire/smoke
	for (int i = 0; i < _save.getWidth() * _save.getLength() * _save.getHeight(); ++i)
	{
		if (_save.getTiles()[i].getFire() > 0)
		{
			tilesOnFire.add(_save.getTiles()[i]);
		}
		if (_save.getTiles()[i].getSmoke() > 0)
		{
			tilesOnSmoke.add(_save.getTiles()[i]);
		}
	}

	for (Tile i: tilesOnSmoke)
	{

		(i).prepareNewTurn();
	}

	for (Tile i: tilesOnFire)
	{
		int z = (i).getPosition().z;
		for (int x = (i).getPosition().x-1; x <= (i).getPosition().x+1; x++)
		{
			for (int y = (i).getPosition().y-1; y <= (i).getPosition().y+1; y++)
			{
				Tile t = _save.getTile(new Position(x, y, z));
				if (t != null && t.getFire() == 0)
				{
					// check adjacent tiles - if they have a flammability of < 255, there is a chance...
					if (horizontalBlockage((i), t, ItemDamageType.DT_IN) == 0)
					{
						int flam = t.getFlammability();
						if (flam < 255)
						{
							double base = RNG.boxMuller(0,126);
							if (base < 0) base *= -1;

							if (flam < base)
							{
								if (RNG.generate(0, flam) < 2)
								{
									t.ignite();
								}
							}
						}
					}
				}
			}
		}

		(i).prepareNewTurn();
	}

	if (!tilesOnFire.isEmpty())
	{
		calculateTerrainLighting(); // fires could have been stopped
	}

}

}
