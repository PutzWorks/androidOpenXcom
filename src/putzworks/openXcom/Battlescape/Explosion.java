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

import putzworks.openXcom.Resource.ResourcePack;
import putzworks.openXcom.Savegame.BattleItem;
import putzworks.openXcom.Savegame.SavedBattleGame;

public class Explosion
{
	private ResourcePack _res;
	private SavedBattleGame _save;
	private BattleItem _item;
	private Position _position;
	private int _currentFrame, _startFrame;
	private boolean _big;

/**
 * Sets up a Explosion sprite with the specified size and position.
 * @param position Explosion center position in voxel x/y/z.
 * @param startFrame A startframe - can be used to offset different explosions at different frames.
 * @param big Flag to indicate it is a bullet hit(false), or a real explosion(true).
 */
public Explosion(Position position, int startFrame, boolean big)
{
	_position = position;
	_currentFrame = startFrame; 
	_startFrame = startFrame;
	_big = big;

}


/**
 * Animate the explosion further.
 * @return false if the animation is finished.
 */
public boolean animate()
{
	_currentFrame++;
	if ((_big && _currentFrame == 8) || (!_big && _currentFrame == _startFrame+10))
	{
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
public Position getPosition()
{
	return _position;
}

/**
 * Get the current frame in the animation.
 * @return frame number.
 */
public int getCurrentFrame()
{
	return _currentFrame;
}

/**
 * Return flag to indicate it is a bullet hit(false), or a real explosion(true).
 * @return big
 */
public boolean isBig()
{
	return _big;
}

}
