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

import java.util.Set;
import java.util.Vector;

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.InteractiveSurface;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Engine.Surface;
import putzworks.openXcom.Engine.SurfaceHandler;
import putzworks.openXcom.Engine.Timer;
import putzworks.openXcom.Resource.ResourcePack;
import putzworks.openXcom.Ruleset.MapData;
import putzworks.openXcom.Ruleset.MapDataSet;
import putzworks.openXcom.Savegame.BattleItem;
import putzworks.openXcom.Savegame.BattleUnit;
import putzworks.openXcom.Savegame.SavedBattleGame;
import putzworks.openXcom.Savegame.Tile;



/*
  1) Map origin is left corner.
  2) X axis goes downright. (width of the map)
  3) Y axis goes upright. (length of the map
  4) Z axis goes up (height of the map)

          y+
			/\
	    0,0/  \
		   \  /
		    \/
          x+
*/

public class Map extends InteractiveSurface
{
	private static final int SCROLL_AMOUNT = 20;
	private static final int SCROLL_BORDER = 5;
	private static final int SCROLL_DIAGONAL_EDGE = 60;
	private static final boolean RMB_SCROLL = false;
	private static final int BUTTONS_AREA = 140;

	public enum CursorType { CT_NONE, CT_NORMAL, CT_AIM, CT_PSI, CT_WAYPOINT, CT_THROW };

	private SavedBattleGame _save;
	private ResourcePack _res;
	private Timer _scrollTimer;
	private Surface _arrow;
	private Game _game;
	private Surface[] _tileFloorCache;
	private Surface[] _tileWallsCache;
	private int _tileCount;
	private Vector<Surface> _unitCache;
	private int _mapOffsetX, _mapOffsetY, _viewHeight;
	private int _bufOffsetX, _bufOffsetY;
	private int _RMBClickX, _RMBClickY;
	private int _spriteWidth, _spriteHeight;
	private int _selectorX, _selectorY;
	private CursorType _cursorType;
	private int _animFrame;
	private int _scrollX, _scrollY;
	private boolean _RMBDragging;
	private int _centerX, _centerY;
	private Surface _buffer;
	private BulletSprite[] _bullet = new BulletSprite[36];
	private BulletSprite[] _bulletShadow = new BulletSprite[36];
	private Projectile _projectile;
	private Set<Explosion> _explosions;


/**
 * Sets up a map with the specified size and position.
 * @param width Width in pixels.
 * @param height Height in pixels.
 * @param x X position in pixels.
 * @param y Y position in pixels.
 */
public Map(int width, int height, int x, int y) 
{
	super(width, height, x, y);
	_mapOffsetX = -250;
	_mapOffsetY = 250;
	_viewHeight = 0;
	_cursorType = CursorType.CT_NORMAL;
	_animFrame = 0;
	_scrollX = 0;
	_scrollY = 0;
	_RMBDragging = false;

	_scrollTimer = new Timer(50);
	_scrollTimer.onTimer(new SurfaceHandler() {
		public void handle(Surface surface) {
			scroll();
		}
	});
}

/**
 * Deletes the map.
 */
public void clearMap()
{
	_scrollTimer = null;

	_tileFloorCache = null;
	_tileWallsCache = null;

	_unitCache.clear();
//	for (Surface i: _unitCache)
//	{
//		i = null;
//	}

	_arrow = null;
	_buffer = null;

	for (int i = 0; i < 36; ++i)
	{
		_bullet[i] = null;
		_bulletShadow[i] = null;
	}
}

/**
 * Changes the pack for the map to get resources for rendering.
 * @param res Pointer to the resource pack.
 */
public void setResourcePack(ResourcePack res)
{
	_res = res;
	_spriteWidth = res.getSurfaceSet("BLANKS.PCK").getFrame(0).getWidth();
	_spriteHeight = res.getSurfaceSet("BLANKS.PCK").getFrame(0).getHeight();
}

/**
 * Changes the saved game content for the map to render.
 * @param save Pointer to the saved game.
 * @param game Pointer to the Game.
 */
public void setSavedGame(SavedBattleGame save, Game game)
{
	_save = save;
	_game = game;
	_tileCount = _save.getHeight() * _save.getLength() * _save.getWidth();
	_tileFloorCache = new Surface[_tileCount];
	_tileWallsCache = new Surface[_tileCount];
	_unitCache.clear();
	for (int i = 0; i < _tileCount; ++i)
	{
		_tileFloorCache[i] = null;
		_tileWallsCache[i] = null;
	}
	for (BattleUnit i: _save.getUnits())
	{
		_unitCache.add(null);
	}

	for (MapDataSet i: _save.getMapDataSets())
	{
		(i).getSurfaceset().setPalette(this.getPalette());
	}

}

/**
 * initializes stuff
 */
public void init()
{
	// load the tiny arrow into a surface
	int f = Palette.blockOffset(1)+1; // yellow
	int b = 15; // black
	int pixels[] = { 0, 0, b, b, b, b, b, 0, 0,
					   0, 0, b, f, f, f, b, 0, 0,
				       0, 0, b, f, f, f, b, 0, 0,
					   b, b, b, f, f, f, b, b, b,
					   b, f, f, f, f, f, f, f, b,
					   0, b, f, f, f, f, f, b, 0,
					   0, 0, b, f, f, f, b, 0, 0,
					   0, 0, 0, b, f, b, 0, 0, 0,
					   0, 0, 0, 0, b, 0, 0, 0, 0 };

	_arrow = new Surface(9, 9);
	_arrow.setPalette(this.getPalette());
	_arrow.lock();
	for (int y = 0; y < 9;y++)
		for (int x = 0; x < 9; x++)
			_arrow.setPixel(x, y, pixels[x+(y*9)]);
	_arrow.unlock();

	_buffer = new Surface(this.getWidth() + _spriteWidth*4, this.getHeight() + _spriteHeight*4);
	_buffer.setPalette(this.getPalette());

	for (int i = 0; i < 36; ++i)
	{
		_bullet[i] = new BulletSprite(i);
		_bullet[i].setPalette(this.getPalette());
		_bullet[i].draw();
		_bulletShadow[i] = new BulletSprite(i);
		_bulletShadow[i].setPalette(this.getPalette());
		_bulletShadow[i].draw();
		_bulletShadow[i].setShade(16);
	}

	_projectile = null;
}

/**
 * Keeps the animation timers running.
 */
public void think()
{
	_scrollTimer.think(null, this);
}

/**
 * Draws the whole map, part by part.
 * todo: work with dirty rects
 */
public void draw(boolean forceRedraw)
{
	int lastX = 0, lastY = 0;

	if (((_mapOffsetX - lastX) < -_spriteWidth*2 ||
		(_mapOffsetX - lastX) > _spriteWidth*2 ||
		(_mapOffsetY - lastY) < -_spriteWidth*2 ||
		(_mapOffsetY - lastY) > _spriteWidth*2) || forceRedraw)
	{
		// if the screen moved outside the buffer region, redraw it
		_buffer.clear();
		_bufOffsetX = -_spriteWidth*2;
		_bufOffsetY = -_spriteHeight*2;
		drawTerrain(_buffer);
	}
	else
	{
		// if we are still inside buffer region just move the buffer.
		_bufOffsetX += (_mapOffsetX - lastX);
		_bufOffsetY += (_mapOffsetY - lastY);
	}
	_buffer.setX(_bufOffsetX);
	_buffer.setY(_bufOffsetY);
	this.clear();
	_buffer.blit(this);

	lastX = _mapOffsetX;
	lastY = _mapOffsetY;
}

/**
* Draw the terrain.
*/
public void drawTerrain(Surface surface)
{
	int frameNumber = 0;
	Surface frame;
	Tile tile;
	int beginX = 0, endX = _save.getWidth() - 1;
    int beginY = 0, endY = _save.getLength() - 1;
    int beginZ = 0, endZ = _viewHeight;
	Position mapPosition, screenPosition, bulletPositionScreen;
	int bulletLowX=16000, bulletLowY=16000, bulletLowZ=16000, bulletHighX=0, bulletHighY=0, bulletHighZ=0;
	int index;
	//boolean dirty;
	BattleUnit unit = null;

	// if we got bullet, get the highest x and y tiles to draw it on
	if (_projectile != null && _projectile.getItem() == null)
	{
		for (int i = 1; i <= _projectile.getParticle(0); ++i)
		{
			if (_projectile.getPosition(1-i).x < bulletLowX)
				bulletLowX = _projectile.getPosition(1-i).x;
			if (_projectile.getPosition(1-i).y < bulletLowY)
				bulletLowY = _projectile.getPosition(1-i).y;
			if (_projectile.getPosition(1-i).z < bulletLowZ)
				bulletLowZ = _projectile.getPosition(1-i).z;
			if (_projectile.getPosition(1-i).x > bulletHighX)
				bulletHighX = _projectile.getPosition(1-i).x;
			if (_projectile.getPosition(1-i).y > bulletHighY)
				bulletHighY = _projectile.getPosition(1-i).y;
			if (_projectile.getPosition(1-i).z > bulletHighZ)
				bulletHighZ = _projectile.getPosition(1-i).z;
		}
		// divide by 16 to go from voxel to tile position
		bulletLowX = bulletLowX / 16;
		bulletLowY = bulletLowY / 16;
		bulletLowZ = bulletLowZ / 24;
		bulletHighX = bulletHighX / 16;
		bulletHighY = bulletHighY / 16;
		bulletHighZ = bulletHighZ / 24;
	}

    for (int itZ = beginZ; itZ <= endZ; itZ++)
	{
        for (int itX = beginX; itX <= endX; itX++)
		{
            for (int itY = endY; itY >= beginY; itY--)
			{
				mapPosition = new Position(itX, itY, itZ);
				convertMapToScreen(mapPosition, screenPosition);
				screenPosition.x += _mapOffsetX - _bufOffsetX;
				screenPosition.y += _mapOffsetY - _bufOffsetY;

				// only render cells that are inside the surface
				if (screenPosition.x > -_spriteWidth && screenPosition.x < surface.getWidth() + _spriteWidth &&
					screenPosition.y > -_spriteHeight && screenPosition.y < surface.getHeight() + _spriteHeight )
				{
					index = _save.getTileIndex(mapPosition); // index used for tile cache

					//dirty =
					cacheTileSprites(index);

					tile = _save.getTile(mapPosition);
					// Draw floor
					if (tile != null)
					{
						frame = _tileFloorCache[index];
						if (frame != null)
						{
							frame.setX(screenPosition.x);
							frame.setY(screenPosition.y);
							frame.blit(surface);
						}
                        unit = tile.getUnit();
					}
					else
					{
					    unit = null;
					}


					// Draw cursor back
					if (_selectorX == itY && _selectorY == itX && _cursorType != Map.CursorType.CT_NONE)
					{
						if (_viewHeight == itZ)
						{
							if (_cursorType == Map.CursorType.CT_NORMAL || _cursorType == Map.CursorType.CT_THROW)
							{
								if (unit != null)
									frameNumber = 1; // yellow box
								else
									frameNumber = 0; // red box
							}
							if (_cursorType == Map.CursorType.CT_AIM)
							{
								if (unit != null)
									frameNumber = 7 + (_animFrame / 2); // yellow animated crosshairs
								else
									frameNumber = 6; // red static crosshairs
							}
						}
						else if (_viewHeight > itZ)
						{
							frameNumber = 2; // blue box
						}
						frame = _res.getSurfaceSet("CURSOR.PCK").getFrame(frameNumber);
						frame.setX(screenPosition.x);
						frame.setY(screenPosition.y);
						frame.blit(surface);
					}

					// Draw walls
					if (tile != null)
					{
						frame = _tileWallsCache[index];
						if (frame != null)
						{
							frame.setX(screenPosition.x);
							frame.setY(screenPosition.y);
							frame.blit(surface);
						}
					}

					// check if we got bullet
					if (_projectile != null)
					{
						Surface item = null;
						if (_projectile.getItem() != null)
						{
							item = _projectile.getSprite();

							if (itZ == 0)
							{
								// draw shadow on the floor
								Position voxelPos = _projectile.getPosition();
								voxelPos.z = 0;
								if (voxelPos.x / 16 >= mapPosition.x-1 &&
									voxelPos.y / 16 >= mapPosition.y-1 &&
									voxelPos.x / 16 <= mapPosition.x+1 &&
									voxelPos.y / 16 <= mapPosition.y+1 )
								{
									convertVoxelToScreen(voxelPos, bulletPositionScreen);
									_projectile.getShadowSprite().setX(bulletPositionScreen.x - 16);
									_projectile.getShadowSprite().setY(bulletPositionScreen.y - 26);
									_projectile.getShadowSprite().blit(surface);
								}
							}

							Position voxelPos = _projectile.getPosition();
							if (voxelPos.x / 16 == mapPosition.x &&
								voxelPos.y / 16 == mapPosition.y )
							{
								convertVoxelToScreen(voxelPos, bulletPositionScreen);
								item.setX(bulletPositionScreen.x - 16);
								item.setY(bulletPositionScreen.y - 26);
								item.blit(surface);
							}
						}
						else
						{
							// draw bullet on the correct tile
							if (itX >= bulletLowX && itX <= bulletHighX && itY >= bulletLowY && itY <= bulletHighY)
							{
								if (itZ == 0)
								{
									// draw shadow on the floor
									for (int i = 1; i <= _projectile.getParticle(0); ++i)
									{
										if (_projectile.getParticle(i) != 0xFF)
										{
											Position voxelPos = _projectile.getPosition(1-i);
											voxelPos.z = 0;
											if (voxelPos.x / 16 == mapPosition.x &&
												voxelPos.y / 16 == mapPosition.y)
											{
												convertVoxelToScreen(voxelPos, bulletPositionScreen);
												_bulletShadow[_projectile.getParticle(i)].setX(bulletPositionScreen.x);
												_bulletShadow[_projectile.getParticle(i)].setY(bulletPositionScreen.y);
												_bulletShadow[_projectile.getParticle(i)].blit(surface);
											}
										}
									}
								}
								for (int i = 1; i <= _projectile.getParticle(0); ++i)
								{
									if (_projectile.getParticle(i) != 0xFF)
									{
										Position voxelPos = _projectile.getPosition(1-i);
										if (voxelPos.x / 16 == mapPosition.x &&
											voxelPos.y / 16 == mapPosition.y )
										{
											convertVoxelToScreen(voxelPos, bulletPositionScreen);
											_bullet[_projectile.getParticle(i)].setX(bulletPositionScreen.x);
											_bullet[_projectile.getParticle(i)].setY(bulletPositionScreen.y);
											_bullet[_projectile.getParticle(i)].blit(surface);
										}
									}
								}
							}
						}
					}

					// Draw soldier
					if (unit != null)
					{
						frame = _unitCache.get(unit.getId());
						if (frame != null)
						{
							Position offset;
							calculateWalkingOffset(unit, offset);
							frame.setX(screenPosition.x + offset.x);
							frame.setY(screenPosition.y + offset.y);
							frame.blit(surface);
							if (unit == (BattleUnit)_save.getSelectedUnit() && _cursorType != Map.CursorType.CT_NONE)
							{
								drawArrow(screenPosition.plus(offset), surface);
							}
						}
					}
					// if we can see through the floor, draw the soldier below it if it is on stairs
					if (itZ > 0 && tile.hasNoFloor())
					{
						unit = _save.selectUnit(mapPosition.plus(new Position(0, 0, -1)));
						tile = _save.getTile(mapPosition.plus(new Position(0, 0, -1)));
						if (unit != null && tile.getTerrainLevel() < 0)
						{
							frame = _unitCache.get(unit.getId());
							if (frame != null)
							{
								Position offset;
								calculateWalkingOffset(unit, offset);
								offset.y += 24;
								frame.setX(screenPosition.x + offset.x);
								frame.setY(screenPosition.y + offset.y);
								frame.blit(surface);
								if (unit == (BattleUnit)_save.getSelectedUnit() && _cursorType != Map.CursorType.CT_NONE)
								{
									drawArrow(screenPosition.plus(offset), surface);
								}
							}
						}
					}
					unit = tile.getUnit();

					// check if we gots explosions
					for (Explosion i: _explosions)
					{
						if (!(i).isBig())
						{
							Position voxelPos = (i).getPosition();
							convertVoxelToScreen(voxelPos, bulletPositionScreen);
							frame = _res.getSurfaceSet("SMOKE.PCK").getFrame(i).getCurrentFrame());
							frame.setX(bulletPositionScreen.x - 15);
							frame.setY(bulletPositionScreen.y - 15);
							frame.blit(surface);
						}
					}

					// Draw cursor front
					if (_selectorX == itY && _selectorY == itX && _cursorType != Map.CursorType.CT_NONE)
					{
						if (_viewHeight == itZ)
						{
							if (_cursorType == Map.CursorType.CT_NORMAL || _cursorType == Map.CursorType.CT_THROW)
							{
								if (unit != null)
									frameNumber = 4; // yellow box
								else
									frameNumber = 3; // red box
							}
							if (_cursorType == Map.CursorType.CT_AIM)
							{
								if (unit != null)
									frameNumber = 7 + (_animFrame / 2); // yellow animated crosshairs
								else
									frameNumber = 6; // red static crosshairs
							}
						}
						else if (_viewHeight > itZ)
						{
							frameNumber = 5; // blue box
						}
						frame = _res.getSurfaceSet("CURSOR.PCK").getFrame(frameNumber);
						frame.setX(screenPosition.x);
						frame.setY(screenPosition.y);
						frame.blit(surface);
						if (_cursorType == Map.CursorType.CT_THROW && _viewHeight == itZ)
						{
							frame = _res.getSurfaceSet("CURSOR.PCK").getFrame(15 + (_animFrame / 4));
							frame.setX(screenPosition.x);
							frame.setY(screenPosition.y);
							frame.blit(surface);
						}
					}

					tile = _save.getTile(mapPosition);
					// Draw smoke/fire
					if (tile.getFire() != 0 && tile.isDiscovered())
					{
						frameNumber = 0; // see http://www.ufopaedia.org/images/c/cb/Smoke.gif
						if ((_animFrame / 2) + tile.getAnimationOffset() > 3)
						{
							frameNumber += ((_animFrame / 2) + tile.getAnimationOffset() - 4);
						}
						else
						{
							frameNumber += (_animFrame / 2) + tile.getAnimationOffset();
						}
						frame = _res.getSurfaceSet("SMOKE.PCK").getFrame(frameNumber);
						frame.setX(screenPosition.x);
						frame.setY(screenPosition.y);
						frame.blit(surface);
					}
					if (tile.getSmoke() != 0 && tile.isDiscovered())
					{
						frameNumber = 8 + (int)(Math.floor((tile.getSmoke() / 5.0) - 0.1)); // see http://www.ufopaedia.org/images/c/cb/Smoke.gif
						if ((_animFrame / 2) + tile.getAnimationOffset() > 3)
						{
							frameNumber += ((_animFrame / 2) + tile.getAnimationOffset() - 4);
						}
						else
						{
							frameNumber += (_animFrame / 2) + tile.getAnimationOffset();
						}
						frame = _res.getSurfaceSet("SMOKE.PCK").getFrame(frameNumber);
						frame.setX(screenPosition.x);
						frame.setY(screenPosition.y);
						frame.blit(surface);
					}
				}
			}
		}
	}

	// check if we got big explosions
	for (Explosion i: _explosions)
	{
		if ((i).isBig())
		{
			Position voxelPos = (i).getPosition();
			convertVoxelToScreen(voxelPos, bulletPositionScreen);
			frame = _res.getSurfaceSet("X1.PCK").getFrame((i).getCurrentFrame());
			frame.setX(bulletPositionScreen.x - 64);
			frame.setY(bulletPositionScreen.y - 64);
			frame.blit(surface);
		}
	}

}

/**
 * Handles map mouse shortcuts.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseClick(Action action, State state)
{
	super.mouseClick(action, state);
	if (action.getDetails().button.button == SDL_BUTTON_WHEELUP)
	{
		up();
	}
	else if (action.getDetails().button.button == SDL_BUTTON_WHEELDOWN)
	{
		down();
	}
}

/**
 * Handles map keyboard shortcuts.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void keyboardPress(Action action, State state)
{
	//Position pos;
	//getSelectorPosition(&pos);
	super.keyboardPress(action, state);
}

/**
 * Handles mouse over events.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseOver(Action action, State state)
{
	int posX = action.getXMouse();
	int posY = action.getYMouse();

	// handle RMB dragging
	if ((action.getDetails().motion.state & SDL_BUTTON(SDL_BUTTON_RIGHT)) && RMB_SCROLL)
	{
		_RMBDragging = true;
		_scrollX = (int)(-(double)(_RMBClickX - posX) * (action.getXScale() * 2));
		_scrollY = (int)(-(double)(_RMBClickY - posY) * (action.getYScale() * 2));
		_RMBClickX = posX;
		_RMBClickY = posY;
	}
	else
	{
		// handle scrolling with mouse at edge of screen
		if (posX < (SCROLL_BORDER * action.getXScale()) && posX > 0)
		{
			_scrollX = SCROLL_AMOUNT;
			// if close to top or bottom, also scroll diagonally
			if (posY < (SCROLL_DIAGONAL_EDGE * action.getYScale()) && posY > 0)
			{
				_scrollY = SCROLL_AMOUNT;
			}
			else if (posY > (getHeight() - SCROLL_DIAGONAL_EDGE) * action.getYScale())
			{
				_scrollY = -SCROLL_AMOUNT;
			}
		}
		else if (posX > (getWidth() - SCROLL_BORDER) * action.getXScale())
		{
			_scrollX = -SCROLL_AMOUNT;
			// if close to top or bottom, also scroll diagonally
			if (posY < (SCROLL_DIAGONAL_EDGE * action.getYScale()) && posY > 0)
			{
				_scrollY = SCROLL_AMOUNT;
			}
			else if (posY > (getHeight() - SCROLL_DIAGONAL_EDGE) * action.getYScale())
			{
				_scrollY = -SCROLL_AMOUNT;
			}
		}
		else if (posX != 0)
		{
			_scrollX = 0;
		}

		if (posY < (SCROLL_BORDER * action.getYScale()) && posY > 0)
		{
			_scrollY = SCROLL_AMOUNT;
			// if close to left or right edge, also scroll diagonally
			if (posX < (SCROLL_DIAGONAL_EDGE * action.getXScale()) && posX > 0)
			{
				_scrollX = SCROLL_AMOUNT;
			}
			else if (posX > (getWidth() - SCROLL_DIAGONAL_EDGE) * action.getXScale())
			{
				_scrollX = -SCROLL_AMOUNT;
			}
		}
		else if (posY > (getHeight() - SCROLL_BORDER) * action.getYScale())
		{
			_scrollY = -SCROLL_AMOUNT;
			// if close to left or right edge, also scroll diagonally
			if (posX < (SCROLL_DIAGONAL_EDGE * action.getXScale()) && posX > 0)
			{
				_scrollX = SCROLL_AMOUNT;
			}
			else if (posX > (getWidth() - SCROLL_DIAGONAL_EDGE) * action.getXScale())
			{
				_scrollX = -SCROLL_AMOUNT;
			}
		}
		else if (posY != 0 && _scrollX == 0)
		{
			_scrollY = 0;
		}
	}

	if ((_scrollX != 0 || _scrollY != 0) && !_scrollTimer.isRunning())
	{
		_scrollTimer.start();
	}
	else if ((_scrollX == 0&& _scrollY == 0) && _scrollTimer.isRunning())
	{
		_scrollTimer.stop();
	}

	setSelectorPosition((int)((double)posX / action.getXScale()), (int)((double)posY / action.getYScale()));
}


/**
 * Sets the value to min if it is below min, sets value to max if above max.
 * @param value pointer to the value
 * @param minimum value
 * @param maximum value
 */
private void minMaxInt(int value,int minValue, int maxValue)
{
	if (value < minValue)
	{
		value = minValue;
	}
	else if (value > maxValue)
	{
		value = maxValue;
	}
}

/**
 * Sets the selector to a certain tile on the map.
 * @param mx mouse x position
 * @param my mouse y position
 */
public void setSelectorPosition(int mx, int my)
{
	int oldX = _selectorX, oldY = _selectorY;

	if (mx == 0 && my == 0) return; // cursor is offscreen
	convertScreenToMap(mx, my, _selectorX, _selectorY);
	minMaxInt(_selectorX, 0, _save.getWidth() - 1);
	minMaxInt(_selectorY, 0, _save.getLength() - 1);

	if (oldX != _selectorX || oldY != _selectorY)
	{
		//draw(false);
		// todo: when working with dirty rects we can implement smooth mouse movement
	}
}

private void convertScreenToMap(int screenX, int screenY, int mapX, int mapY)
{
	// add half a tileheight to the mouseposition per layer we are above the floor
    screenY += -_spriteHeight + (_viewHeight + 1) * (_spriteHeight / 2);

	// calculate the actual x/y pixelposition on a diamond shaped map
	// taking the view offset into account
    mapX = screenX - _mapOffsetX - 2 * screenY + 2 * _mapOffsetY;
    mapY = screenY - _mapOffsetY + mapX / 4;

	// to get the row&col itself, divide by the size of a tile
    mapY /= (_spriteWidth / 4);
	mapX /= _spriteWidth;
}


/**
 * Handle scrolling.
 */
public void scroll()
{
	_mapOffsetX += _scrollX;
	_mapOffsetY += _scrollY;

	convertScreenToMap((getWidth() / 2), (BUTTONS_AREA / 2), _centerX, _centerY);

	// if center goes out of map bounds, hold the scrolling (may need further tweaking)
	if (_centerX > _save.getWidth() - 1 || _centerY > _save.getLength() - 1 || _centerX < 0 || _centerY < 0)
	{
		_mapOffsetX -= _scrollX;
		_mapOffsetY -= _scrollY;
	}

	if (_RMBDragging)
	{
		_RMBDragging = false;
		_scrollX = 0;
		_scrollY = 0;
	}
	draw(false);
}

/**
 * Handle animating tiles/units/bullets. 8 Frames per animation.
 */
public void animate()
{
	_animFrame++;
	if (_animFrame == 8) _animFrame = 0;

	for (int i = 0; i < _tileCount; ++i)
	{
		_save.getTiles()[i].animate();
	}
	cacheTileSprites();
	draw(true);
}

/**
 * Go one level up.
 */
public void up()
{
	if (_viewHeight < _save.getHeight() - 1)
	{
		_viewHeight++;
		_mapOffsetY += _spriteHeight / 2;
		draw(true);
	}
}

/**
 * Go one level down.
 */
public void down()
{
	if (_viewHeight > 0)
	{
		_viewHeight--;
		_mapOffsetY -= _spriteHeight / 2;
		draw(true);
	}
}

/**
 * Set viewheight.
 * @param viewheight
 */
public void setViewHeight(int viewheight)
{
	_viewHeight = viewheight;
	minMaxInt(_viewHeight, 0, _save.getHeight()-1);
	draw(true);
}


/**
 * Center map on a certain position.
 * @param mapPos Position to center on.
 */
public void centerOnPosition(Position mapPos)
{
	Position screenPos = new Position();

	convertMapToScreen(mapPos, screenPos);

	_mapOffsetX = -(screenPos.x - (getWidth() / 2));
	_mapOffsetY = -(screenPos.y - (BUTTONS_AREA / 2));

	convertScreenToMap((getWidth() / 2), (BUTTONS_AREA / 2), _centerX, _centerY);

	_viewHeight = mapPos.z;

	draw(true);
}

/**
 * Convert map coordinates X,Y,Z to screen positions X, Y.
 * @param mapPos X,Y,Z coordinates on the map.
 * @param screenPos to screen position.
 */
public void convertMapToScreen(Position mapPos, Position screenPos)
{
	screenPos.z = 0; // not used
	screenPos.x = mapPos.x * (_spriteWidth / 2) + mapPos.y * (_spriteWidth / 2);
	screenPos.y = mapPos.x * (_spriteWidth / 4) - mapPos.y * (_spriteWidth / 4) - mapPos.z * ((_spriteHeight + _spriteWidth / 4) / 2);
}

/**
 * Convert map coordinates X,Y,Z to screen positions X, Y.
 * @param mapPos X,Y,Z coordinates on the map.
 * @param screenPos to screen position.
 */
public void convertVoxelToScreen(Position voxelPos, Position screenPos)
{
	Position mapPosition = new Position(voxelPos.x / 16, voxelPos.y / 16, voxelPos.z / 24);
	convertMapToScreen(mapPosition, screenPos);
	double dx = voxelPos.x - (mapPosition.x * 16);
	double dy = voxelPos.y - (mapPosition.y * 16);
	double dz = voxelPos.z - (mapPosition.z * 24);
	screenPos.x += (int)(dx + dy - 1);
	screenPos.y += (int)(((_spriteHeight / 4.0) * 3.0) + (dx / 2.0) - (dy / 2.0) - dz);
	screenPos.x += _mapOffsetX - _bufOffsetX;
	screenPos.y += _mapOffsetY - _bufOffsetY;
}

/**
 * Draws the small arrow above the selected soldier.
 * @param screenPos
 */
public void drawArrow(Position screenPos, Surface surface)
{
	_arrow.setX(screenPos.x + (_spriteWidth / 2) - (_arrow.getWidth() / 2));
	_arrow.setY(screenPos.y - _arrow.getHeight() + _animFrame);
	_arrow.blit(surface);
}

/**
 * Draws the small arrow above the selected soldier.
 * @param pos pointer to a position
 */
public void getSelectorPosition(Position pos)
{
	// don't know why X and Y are inverted here...
	pos.x = _selectorY;
	pos.y = _selectorX;
	pos.z = _viewHeight;
}

/**
 * Calculate the offset of a soldier, when it is walking in the middle of 2 tiles.
 * @param unit pointer to BattleUnit
 * @param offset pointer to the offset to return the calculation.
 */
public void calculateWalkingOffset(BattleUnit unit, Position offset)
{
	int offsetX[] = { 1, 1, 1, 0, -1, -1, -1, 0 };
	int offsetY[] = { 1, 0, -1, -1, -1, 0, 1, 1 };
	int phase = unit.getWalkingPhase() + unit.getDiagonalWalkingPhase();
	int dir = unit.getDirection();
	int midphase = 4 + 4 * (dir % 2);
	int endphase = 8 + 8 * (dir % 2);

	if (unit.getStatus() == BattleUnit.UnitStatus.STATUS_WALKING)
	{
		if (phase < midphase)
		{
			offset.x = phase * 2 * offsetX[dir];
			offset.y = - phase * offsetY[dir];
		}
		else
		{
			offset.x = (phase - endphase) * 2 * offsetX[dir];
			offset.y = - (phase - endphase) * offsetY[dir];
		}
	}

	// If we are walking in between tiles, interpolate it's terrain level.
	if (unit.getStatus() == BattleUnit.UnitStatus.STATUS_WALKING)
	{
		if (phase < midphase)
		{
			int fromLevel = _save.getTile(unit.getPosition()).getTerrainLevel();
			int toLevel = _save.getTile(unit.getDestination()).getTerrainLevel();
			if (unit.getPosition().z > unit.getDestination().z)
			{
				// going down a level, so toLevel 0 becomes +24, -8 becomes  16
				toLevel += 24*(unit.getPosition().z - unit.getDestination().z);
			}else if (unit.getPosition().z < unit.getDestination().z)
			{
				// going up a level, so toLevel 0 becomes -24, -8 becomes -16
				toLevel = -24*(unit.getDestination().z - unit.getPosition().z) + Math.abs(toLevel);
			}
			offset.y += ((fromLevel * (endphase - phase)) / endphase) + ((toLevel * (phase)) / endphase);
		}
		else
		{
			// from phase 4 onwards the unit behind the scenes already is on the destination tile
			// we have to get it's last position to calculate the correct offset
			int fromLevel = _save.getTile(unit.getLastPosition()).getTerrainLevel();
			int toLevel = _save.getTile(unit.getDestination()).getTerrainLevel();
			if (unit.getLastPosition().z > unit.getDestination().z)
			{
				// going down a level, so fromLevel 0 becomes -24, -8 becomes -32
				fromLevel -= 24*(unit.getLastPosition().z - unit.getDestination().z);
			}else if (unit.getLastPosition().z < unit.getDestination().z)
			{
				// going up a level, so fromLevel 0 becomes +24, -8 becomes 16
				fromLevel = -24*(unit.getDestination().z - unit.getLastPosition().z) + Math.abs(fromLevel);
			}
			offset.y += ((fromLevel * (endphase - phase)) / endphase) + ((toLevel * (phase)) / endphase);
		}
	}
	else
	{
		offset.y += _save.getTile(unit.getPosition()).getTerrainLevel();
	}

}

/**
 * Set the 3D cursor to selection/aim mode
 * @param type
 */
public void setCursorType(CursorType type)
{
	_cursorType = type;
}

/**
 * Get cursor type.
 * @return cursortype
 */
public CursorType getCursorType()
{
	return _cursorType;
}

/**
 * cacheTileSprites
 */
void cacheTileSprites()
{
	for (int i = 0; i < _tileCount; ++i)
	{
		cacheTileSprites(i);
	}

}

/**
 * Caches 1 tile's sprites.
 * @return false if the tile did not need redrawing.
 */
public boolean cacheTileSprites(int i)
{
	MapData object = null;
	Surface frame = null;
	Tile tile = _save.getTiles()[i];
	boolean door = false;

	if(tile != null && !tile.isCached())
	{

		/* draw a floor object on the cache (if any) */
		object = tile.getMapData(MapData.O_FLOOR);

		if (object != null)
		{
			if (_tileFloorCache[i] == null)
			{
				_tileFloorCache[i] = new Surface(_spriteWidth, _spriteHeight);
				_tileFloorCache[i].setPalette(this.getPalette());
			}
			else
			{
				_tileFloorCache[i].clear();
			}

			// Draw floor
			frame = tile.getSprite(MapData.O_FLOOR);
			frame.setX(0);
			frame.setY(-object.getYOffset());
			frame.blit(_tileFloorCache[i]);

			_tileFloorCache[i].setShade(tile.isDiscovered()?tile.getShade():16);
		}
		else if (_tileFloorCache[i] != null)
		{
			_tileFloorCache[i].clear();
		}

		/* draw terrain objects on the cache (if any) */
		if (tile.getMapData(MapData.O_WESTWALL) != null || tile.getMapData(MapData.O_NORTHWALL) != null || tile.getMapData(MapData.O_OBJECT) != null || tile.getTopItemSprite() != -1)
		{
			if (_tileWallsCache[i] == null)
			{
				_tileWallsCache[i] = new Surface(_spriteWidth, _spriteHeight);
				_tileWallsCache[i].setPalette(this.getPalette());
			}
			else
			{
				_tileWallsCache[i].clear();
			}

			// Draw west wall
			object = tile.getMapData(MapData.O_WESTWALL);
			if (object != null)
			{
				frame = tile.getSprite(MapData.O_WESTWALL);
				frame.setX(0);
				frame.setY(-object.getYOffset());
				frame.blit(_tileWallsCache[i]);
				door = object.isDoor() || object.isUFODoor();
			}
			// Draw north wall
			object = tile.getMapData(MapData.O_NORTHWALL);
			if (object != null)
			{
				frame = tile.getSprite(MapData.O_NORTHWALL);
				frame.setX(0);
				frame.setY(-object.getYOffset());
				// if there is a westwall, cut off some of the north wall (otherwise it will overlap)
				if (tile.getMapData(MapData.O_WESTWALL) != null)
				{
					frame.setX(frame.getWidth() / 2);
					frame.getCrop().x = frame.getWidth() / 2;
					frame.getCrop().w = frame.getWidth() / 2;
					frame.getCrop().h = frame.getHeight();
				}else
				{
					frame.getCrop().w = 0;
					frame.getCrop().h = 0;
				}
				frame.blit(_tileWallsCache[i]);
				door = object.isDoor() || object.isUFODoor();
			}
			// Draw object
			object = tile.getMapData(MapData.O_OBJECT);
			if (object != null)
			{
				frame = tile.getSprite(MapData.O_OBJECT);
				frame.setX(0);
				frame.setY(-object.getYOffset());
				frame.blit(_tileWallsCache[i]);
			}

			// draw an item on top of the floor (if any)
			int sprite = tile.getTopItemSprite();
			if (sprite != -1)
			{
				frame = _res.getSurfaceSet("FLOOROB.PCK").getFrame(sprite);
				frame.setX(0);
				if (object == null)
				{
					object = tile.getMapData(MapData.O_FLOOR);
				}
				frame.setY(object.getTerrainLevel());
				frame.blit(_tileWallsCache[i]);
			}

			if (door && tile.getShade() > 8 && tile.isDiscovered()) // don't shade doors too dark, so you can still see them
			{
				_tileWallsCache[i].setShade(8);
			}
			else
			{
				_tileWallsCache[i].setShade(tile.isDiscovered()?tile.getShade():16);
			}
		}else if (_tileWallsCache[i] != null)
		{
			_tileWallsCache[i].clear();
		}

		// if lighting changed for a tile, then it also does for a unit standing on it
		if (tile.getUnit() != null && tile.getUnit().getFaction() != BattleUnit.UnitFaction.FACTION_PLAYER)
		{
			tile.getUnit().setCached(false);
		}

		tile.setCached(true);
		return true;
	}
	else
	{
		return false;
	}
}

/**
 * Check all units if they need to be redrawn.
 */
public void cacheUnits()
{
	UnitSprite unitSprite = new UnitSprite(_spriteWidth, _spriteHeight, 0, 0);
	//unitSprite.setSurfaces();
	unitSprite.setPalette(this.getPalette());

	for (BattleUnit i: _save.getUnits())
	{
		if (!(i).isCached())
		{
			if (_unitCache.get((i).getId()) == null)
			{
				_unitCache.get((i).getId()) = new Surface(_spriteWidth, _spriteHeight);
				_unitCache.get((i).getId()).setPalette(this.getPalette());
			}
			else
			{
				_unitCache.get((i).getId()).clear();
			}
			unitSprite.setBattleUnit((i));
			BattleItem handItem = _save.getItemFromUnit((i), BattleItem.InventorySlot.RIGHT_HAND);
			if (handItem != null)
			{
				unitSprite.setBattleItem(handItem);
			}
			else
			{
				unitSprite.setBattleItem(null);
			}
			unitSprite.setSurfaces(_res.getSurfaceSet((i).getUnit().getArmor().getSpriteSheet()),
									_res.getSurfaceSet("HANDOB.PCK"));
			unitSprite.draw();
			unitSprite.blit(_unitCache.get((i).getId()));

			_unitCache.get((i).getId()).setShade(_save.getTile((i).getPosition()).isDiscovered()?_save.getTile((i).getPosition()).getShade():16);
			(i).setCached(true);
		}
	}
//	delete unitSprite;
}

/**
 * Put a projectile sprite on the map
 * @param projectile
 */
public void setProjectile(Projectile projectile)
{
	_projectile = projectile;
}

/**
 * Get the current projectile sprite on the map
 * @return 0 if there is no projectile sprite on the map.
 */
public Projectile getProjectile()
{
	return _projectile;
}

/**
 * Get a list of explosion sprites on the map.
 * @return a list of explosion sprites.
 */
public Set<Explosion> getExplosions()
{
	return _explosions;
}


}
