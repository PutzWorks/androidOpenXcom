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
import putzworks.openXcom.Engine.Surface;
import putzworks.openXcom.Resource.ResourcePack;
import putzworks.openXcom.Ruleset.MapData;
import putzworks.openXcom.Savegame.BattleItem;
import putzworks.openXcom.Savegame.BattleUnit;
import putzworks.openXcom.Savegame.SavedBattleGame;
import putzworks.openXcom.Savegame.Tile;

public class Projectile
{
	private ResourcePack _res;
	private SavedBattleGame _save;
	private BattleItem _item;
	private Position _origin, _target;
	private Vector<Position> _trajectory;
	private int _position;
	//private static final int[][] _trail = new int[11][36];
	private int _bulletType;
	private Surface _sprite, _shadowSprite;

	// the wonderful bullet trail art
	private static final int[][] _trail = {
	{0x23, 0x0, 0x0, 0x0, 0x1, 0x2, 0x2, 0x3, 0x3, 0x3, 0x3, 0x2, 0x4, 0x4, 0x4, 0x4, 0x5, 0x5, 0xFF, 0x6, 0xFF, 0x6, 0xFF, 0x6, 0xFF, 0x7, 0xFF, 0x7, 0xFF, 0x7, 0xFF, 0xFF, 0x7, 0xFF, 0xFF, 0xFF},
	{0x7, 0x0D, 0x0A, 0x0A, 0x0B, 0x0C, 0xFF, 0x0C, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF},
	{0x11, 0x8, 0x9, 0x9, 0x0A, 0x0A, 0x0B, 0x0B, 0x0C, 0x0C, 0xFF, 0x0C, 0xFF, 0xFF, 0x0C, 0xFF, 0xFF, 0x0C, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF},
	{0x16, 0x0E, 0x0E, 0x0F, 0xFF, 0x8, 0x10, 0x11, 0x12, 0xFF, 0x12, 0xFF, 0xFF, 0x6, 0xFF, 0xFF, 0x6, 0xFF, 0xFF, 0x7, 0xFF, 0xFF, 0x7, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF},
	{0x20, 0x0E, 0x0E, 0x0F, 0xFF, 0x8, 0x10, 0x11, 0x12, 0x11, 0x12, 0x11, 0x12, 0x11, 0xFF, 0x6, 0xFF, 0x6, 0xFF, 0xFF, 0x6, 0xFF, 0xFF, 0x6, 0xFF, 0xFF, 0x7, 0xFF, 0xFF, 0x7, 0xFF, 0xFF, 0x7, 0xFF, 0xFF, 0xFF},
	{0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x11, 0x13, 0x14, 0x15, 0x16, 0x17, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF},
	{0x1A, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x11, 0x13, 0x14, 0x15, 0x16, 0x17, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF},
	{0x23, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x11, 0x13, 0x14, 0x15, 0x16, 0x17},
	{0x1D, 0x18, 0x19, 0x19, 0x1A, 0x1A, 0x1B, 0x1B, 0x1C, 0x1C, 0x1D, 0x1D, 0x1E, 0x1E, 0xFF, 0x1E, 0xFF, 0x1E, 0xFF, 0xFF, 0x1E, 0xFF, 0xFF, 0xFF, 0x1E, 0xFF, 0xFF, 0xFF, 0xFF, 0x1E, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF},
	{0x0F, 0x1F, 0x21, 0x22, 0x23, 0x24, 0x24, 0xFF, 0x24, 0xFF, 0xFF, 0x24, 0xFF, 0xFF, 0xFF, 0x24, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF},
	{0x1E, 0x20, 0x20, 0x1F, 0x1F, 0x1F, 0x21, 0x21, 0x22, 0x22, 0x23, 0x23, 0x23, 0x24, 0x24, 0x24, 0x24, 0xFF, 0x24, 0xFF, 0xFF, 0x24, 0xFF, 0xFF, 0xFF, 0x24, 0xFF, 0xFF, 0xFF, 0xFF, 0x24, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF}
};

/**
 * Sets up a UnitSprite with the specified size and position.
 * @param res Pointer to resourcepack.
 * @param save Pointer to battlesavegame.
 * @param origin Projectile's start position in tile x/y/z.
 * @param target Projectile's target position in tile x/y/z.
 * @param bulletType A number that corresponds to the type of bullet this is.
 */
public Projectile(ResourcePack res, SavedBattleGame save, Position origin, Position target, int bulletType, BattleItem item)
{
	_res = res;
	_save = save;
	_item = item;
	_origin = origin;
	_target = target;
	_position = 0;
	_bulletType = bulletType;

	if (item != null)
	{
		_sprite = _res.getSurfaceSet("FLOOROB.PCK").getFrame(getItem().getRules().getFloorSprite());
		_shadowSprite = new Surface(_sprite.getWidth(), _sprite.getHeight());
		_shadowSprite.setPalette(_sprite.getPalette());
		_sprite.blit(_shadowSprite);
		_shadowSprite.setShade(16);
	}
}

/**
 * Deletes the Projectile.
 */
public void clearProjectile()
{
	if (_item != null)
	{
		_shadowSprite = null;
	}
}

/**
 * calculateTrajectory.
 * @return true when a trajectory is possible.
 */
public boolean calculateTrajectory(double accuracy)
{
	Position originVoxel, targetVoxel;
	int direction;
	int dirYshift[] = {1, 1, 8, 15, 15, 15, 8, 1 };
	int dirXshift[] = {8, 14, 15, 15, 8, 1, 1, 1 };
	// large units : x2

	originVoxel = new Position(_origin.x*16, _origin.y*16, _origin.z*24);
	originVoxel.z += -_save.getTile(_origin).getTerrainLevel();
	BattleUnit bu = _save.getTile(_origin).getUnit();
	originVoxel.z += bu.isKneeled()?bu.getUnit().getKneelHeight():bu.getUnit().getStandHeight();
	originVoxel.z -= 3;
	if (originVoxel.z >= (_origin.z + 1)*24)
	{
		_origin.z++;
	}
	direction = bu.getDirection();
	originVoxel.x += dirXshift[direction];
	originVoxel.y += 15-dirYshift[direction];

	// determine the target voxel.
	// aim at the center of the unit, the object, the walls or the floor (in that priority)
	// if there is no LOF to the center, try elsewhere (more outward).
	// Store this target voxel.
	Tile tile = _save.getTile(_target);
	if (tile.getUnit() != null)
	{
		if (_origin == _target)
		{
			// don't shoot at yourself but shoot at the floor
			targetVoxel = new Position(_target.x*16 + 8, _target.y*16 + 8, _target.z*24);
		}
		else
		{
			// first try is at half the unit height 
			targetVoxel = new Position(_target.x*16 + 8, _target.y*16 + 8, _target.z*24 + tile.getUnit().getUnit().getStandHeight()/2);
			int test = _save.getTerrainModifier().calculateLine(originVoxel, targetVoxel, false, _trajectory, bu);
			_trajectory.clear();
			if (test != 4)
			{
				// did not hit a unit, try at different heights
				targetVoxel = new Position(_target.x*16 + 8, _target.y*16 + 8, _target.z*24 + (tile.getUnit().getUnit().getStandHeight()*3)/4);
				test = _save.getTerrainModifier().calculateLine(originVoxel, targetVoxel, false, _trajectory, bu);
				_trajectory.clear();
			}
		}
	}
	else if (tile.getMapData(MapData.O_OBJECT) != null)
	{
		targetVoxel = new Position(_target.x*16 + 8, _target.y*16 + 8, _target.z*24 + 10);
	}
	else if (tile.getMapData(MapData.O_NORTHWALL) != null)
	{
		targetVoxel = new Position(_target.x*16 + 8, _target.y*16 + 16, _target.z*24 + 10);
	}
	else if (tile.getMapData(MapData.O_WESTWALL) != null)
	{
		targetVoxel = new Position(_target.x*16, _target.y*16 + 8, _target.z*24 + 10);
	}
	else if (tile.getMapData(MapData.O_FLOOR) != null)
	{
		targetVoxel = new Position(_target.x*16 + 8, _target.y*16 + 8, _target.z*24);
	}
	else
	{
		return false; // no line of fire
	}

	// apply some accuracy modifiers (todo: calculate this)
	// This will results in a new target voxel
	applyAccuracy(originVoxel, targetVoxel, accuracy);

	// finally do a line calculation and store this trajectory.
	_save.getTerrainModifier().calculateLine(originVoxel, targetVoxel, true, _trajectory, bu);

	return true;
}

/**
 * calculateTrajectory.
 * @return true when a trajectory is possible.
 */
public boolean calculateThrow(double accuracy)
{
	Position originVoxel, targetVoxel;
	boolean foundCurve = false;

	originVoxel = new Position(_origin.x*16 + 8, _origin.y*16 + 8, _origin.z*24);
	originVoxel.z += -_save.getTile(_origin).getTerrainLevel();
	BattleUnit bu = _save.getTile(_origin).getUnit();
	originVoxel.z += bu.isKneeled()?bu.getUnit().getKneelHeight():bu.getUnit().getStandHeight();
	originVoxel.z -= 3;
	if (originVoxel.z >= (_origin.z + 1)*24)
	{
		_origin.z++;
	}

	// determine the target voxel.
	// aim at the center of the floor
	targetVoxel = new Position(_target.x*16 + 8, _target.y*16 + 8, _target.z*24 + 2);

	// we try 4 different curvatures to try and reach our goal.
	double curvature = 1.0;
	while (!foundCurve && curvature < 5.0)
	{
		_save.getTerrainModifier().calculateParabola(originVoxel, targetVoxel, false, _trajectory, bu, curvature, 1.0);
		if ((int)_trajectory.get(0).x/16 == (int)targetVoxel.x/16 && (int)_trajectory.get(0).y/16 == (int)targetVoxel.y/16)
		{
			foundCurve = true;
		}
		else
		{
			curvature += 1.0;
		}
		_trajectory.clear();
	}
	if (curvature == 5.0)
	{
		return false;
	}

	// apply some accuracy modifiers
	if (accuracy > 100)
		accuracy = 100;
	final double maxDeviation = 0.09;
	final double minDeviation = 0;
	double baseDeviation = (maxDeviation - (maxDeviation * accuracy / 100.0)) + minDeviation;
	double deviation = RNG.boxMuller(0, baseDeviation);

	_trajectory.clear();
	// finally do a line calculation and store this trajectory.
	_save.getTerrainModifier().calculateParabola(originVoxel, targetVoxel, true, _trajectory, bu, curvature, 1.0 + deviation);

	return true;
}


/**
 * applyAccuracy calculates the new target in voxel space, based on the given accuracy modifier.
 * @param origin Startposition of the trajectory.
 * @param target Endpoint of the trajectory.
 * @param accuracy Accuracy modifier.
 */
private void applyAccuracy(Position origin, Position target, double accuracy)
{
	// maxDeviation is the max angle deviation for accuracy 0% in degrees
	final double maxDeviation = 8.0;
	// minDeviation is the max angle deviation for accuracy 100% in degrees
	final double minDeviation = 0.2;
	// maxRange is the maximum range a projectile shall ever travel in voxel space
	final double maxRange = 16*1000; // 1000 tiles

	double dRot, dTilt;
	double rotation, tilt;
	double baseDeviation = (maxDeviation - (maxDeviation * accuracy)) + minDeviation;
	// the angle deviations are spread using a normal distribution between 0 and baseDeviation
	dRot = RNG.boxMuller(0, baseDeviation);
	dTilt = RNG.boxMuller(0, baseDeviation / 2.0);
	rotation = Math.atan2((double)(target.y - origin.y), (double)(target.x - origin.x)) * 180 / Math.PI;
	tilt = Math.atan2((double)(target.z - origin.z),
			Math.sqrt((double)(target.x - origin.x)*(double)(target.x - origin.x)+(double)(target.y - origin.y)*(double)(target.y - origin.y))) * 180 / Math.PI;
	// add deviations
	rotation += dRot;
	tilt += dTilt;
	// calculate new target
	// this can be very far out of the map, but we don't care about that right now
	double cos_fi = Math.cos(tilt * Math.PI / 180.0);
	double sin_fi = Math.sin(tilt * Math.PI / 180.0);
	double cos_te = Math.cos(rotation * Math.PI / 180.0);
	double sin_te = Math.sin(rotation * Math.PI / 180.0);
	target.x = (int)(origin.x + maxRange * cos_te * cos_fi);
	target.y = (int)(origin.y + maxRange * sin_te * cos_fi);
	target.z = (int)(origin.z + maxRange * sin_fi);
}

/**
 * Move further in the trajectory.
 * @return false if the trajectory is finished - no new position exists in the trajectory.
 */
public boolean move()
{
	_position++;
	if (_position == _trajectory.size())
	{
		_position--;
		return false;
	}
	_position++;
	if (_position == _trajectory.size())
	{
		_position--;
		return false;
	}
	else
	{
		return true;
	}
}

/**
 * Get the current position in voxel space.
 * @param offset
 * @return position in voxel space.
 */
public Position getPosition(int offset)
{
	if (_position + offset >= 0 && _position + offset < _trajectory.size())
		return _trajectory.get(_position + offset);
	else
		return _trajectory.get(_position);
}

public Position getPosition()
{
	return _trajectory.get(_position);
}

/**
 * Get a particle from the particle array.
 * @param i
 * @return particle id
 */
public int getParticle(int i)
{
	return _trail[_bulletType][i];
}

public BattleItem getItem()
{
	return _item;
}

public Surface getSprite()
{
	return _sprite;
}

public Surface getShadowSprite()
{
	return _shadowSprite;
}

}
