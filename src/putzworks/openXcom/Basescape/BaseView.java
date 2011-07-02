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
package putzworks.openXcom.Basescape;

import android.graphics.Rect;
import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.Font;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.InteractiveSurface;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Engine.Surface;
import putzworks.openXcom.Engine.SurfaceSet;
import putzworks.openXcom.Engine.Timer;
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Ruleset.RuleBaseFacility;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.BaseFacility;
import putzworks.openXcom.Savegame.Craft;
import putzworks.openXcom.Savegame.Soldier;

public class BaseView extends InteractiveSurface
{
	private static final int BASE_SIZE = 6;
	private Base _base;
	private SurfaceSet _texture;
	private BaseFacility[][] _facilities = new BaseFacility[BASE_SIZE][BASE_SIZE]; 
	private BaseFacility _selFacility;
	private Font _big, _small;
	private int _gridX, _gridY, _selSize;
	private Surface _selector;
	private boolean _blink;
	private Timer _timer;

	private static final int GRID_SIZE = 32;

/**
 * Sets up a base view with the specified size and position.
 * @param width Width in pixels.
 * @param height Height in pixels.
 * @param x X position in pixels.
 * @param y Y position in pixels.
 */
public BaseView(int width, int height, int x, int y)
{
	super(width, height, x, y);
	_base = null; 
	_texture = null;
	_selFacility = null;
	_big = null;
	_small = null;
	_gridX = 0;
	_gridY = 0;
	_selSize = 0; 
	_selector = null;
	_blink = true;

	_validButton = SDL_BUTTON_LEFT;

	for (int x1 = 0; x1 < BASE_SIZE; x1++)
		for (int y1 = 0; y1 < BASE_SIZE; y1++)
			_facilities[x1][y1] = null;

	_timer = new Timer(100);
	_timer.onTimer((SurfaceHandler)&BaseView.blink);
	_timer.start();
}

/**
 * Deletes contents.
 */
public void clearBaseView()
{
	_selector = null;
	_timer = null;
}

/**
 * Changes the various fonts for the text to use.
 * The different fonts need to be passed in advance since the
 * text size can change mid-text.
 * @param big Pointer to large-size font.
 * @param small Pointer to small-size font.
 */
public void setFonts(Font big, Font small)
{
	_big = big;
	_small = small;
}

/**
 * Changes the current base to display and
 * initalizes the internal base grid.
 * @param base Pointer to base to display.
 */
public void setBase(Base base)
{
	_base = base;
	_selFacility = null;

	// Clear grid
	for (int x = 0; x < BASE_SIZE; x++)
		for (int y = 0; y < BASE_SIZE; y++)
			_facilities[x][y] = null;

	// Fill grid with base facilities
	for (BaseFacility i: _base.getFacilities())
	{
		for (int y = (i).getY(); y < (i).getY() + (i).getRules().getSize(); y++)
		{
			for (int x = (i).getX(); x < (i).getX() + i).getRules().getSize(); x++)
			{
				_facilities[x][y] = i;
			}
		}
	}

	draw();
}

/**
 * Changes the texture to use for drawing
 * the various base elements.
 * @param texture Pointer to SurfaceSet to use.
 */
public void setTexture(SurfaceSet texture)
{
	_texture = texture;
}

/**
 * Returns the facility the mouse is currently over.
 * @return Pointer to base facility (0 if none).
 */
public BaseFacility getSelectedFacility()
{
	return _selFacility;
}

/**
 * Returns the X position of the grid square
 * the mouse is currently over.
 * @return X position on the grid.
 */
public int getGridX()
{
	return _gridX;
}

/**
 * Returns the Y position of the grid square
 * the mouse is currently over.
 * @return Y position on the grid.
 */
public int getGridY()
{
	return _gridY;
}

/**
 * If enabled, the base view will respond to player input,
 * highlighting the selected facility.
 * @param size Facility length (0 disables it).
 */
public void setSelectable(int size)
{
	_selSize = size;
	if (_selSize > 0)
	{
		_selector = new Surface(size * GRID_SIZE, size * GRID_SIZE, _x, _y);
		_selector.setPalette(getPalette());
		Rect r;
		r.left = _selector.getWidth();
		r.top = _selector.getHeight();
		r.right = r.left;
		r.bottom = r.bottom;
        _selector.drawRect(r, Palette.blockOffset(1));
		r.right -= 2;
		r.bottom -= 2;
		r.left++;
		r.right++;
        _selector.drawRect(r, (short)0);
		_selector.setVisible(false);
	}
	else
	{
		_selector = null;
	}
}

/**
 * Returns if a certain facility can be successfully
 * placed on the currently selected square.
 * @param rule Facility type.
 * @return True if placeable, False otherwise.
 */
public boolean isPlaceable(RuleBaseFacility rule)
{
	// Check if square isn't occupied
	for (int y = _gridY; y < _gridY + rule.getSize(); y++)
	{
		for (int x = _gridX; x < _gridX + rule.getSize(); x++)
		{
			if (x < 0 || x >= BASE_SIZE || y < 0 || y >= BASE_SIZE)
			{
				return false;
			}
			if (_facilities[x][y] != null)
			{
				return false;
			}
		}
	}

	// Check for another facility to connect to
	for (int i = 0; i < rule.getSize(); ++i)
	{
		if ((_gridX > 0 && _facilities[_gridX - 1][_gridY + i] != null && _facilities[_gridX - 1][_gridY + i].getBuildTime() == 0) ||
			(_gridY > 0 && _facilities[_gridX + i][_gridY - 1] != null && _facilities[_gridX + i][_gridY - 1].getBuildTime() == 0) ||
			(_gridX + rule.getSize() < BASE_SIZE && _facilities[_gridX + rule.getSize()][_gridY + i] != null && _facilities[_gridX + rule.getSize()][_gridY + i].getBuildTime() == 0) ||
			(_gridY + rule.getSize() < BASE_SIZE && _facilities[_gridX + i][_gridY + rule.getSize()] != null && _facilities[_gridX + i][_gridY + rule.getSize()].getBuildTime() == 0))
		{
			return true;
		}
	}

	return false;
}

/**
 * Counts all the occupied squares connected to a certain position in the
 * grid inclusive, but ignoring facilities under construction.
 * Mostly used to ensure a base stays connected to the Access Lift.
 * -1 = Unoccupied, 0 = Occupied, 1 = Connected.
 * @param x X position in grid.
 * @param y Y position in grid.
 * @param grid Pointer to connection grid (Null to create one from scratch).
 * @param remove Facility to ignore (in case of facility dismantling).
 * @return Number of squares connected to the starting position.
 */
public int countConnected(int x, int y, int grid, BaseFacility remove)
{
	boolean newgrid = (grid == null);

	// Create connection grid
	if (newgrid)
	{
		grid = new int[BASE_SIZE];

		for (int xx = 0; xx < BASE_SIZE; xx++)
		{
			grid[xx] = new int[BASE_SIZE];
			for (int yy = 0; yy < BASE_SIZE; yy++)
			{
				if (_facilities[xx][yy] == 0 || _facilities[xx][yy] == remove)
				{
					grid[xx][yy] = -1;
				}
				else
				{
					grid[xx][yy] = 0;
				}
			}
		}
	}

	if (x < 0 || x >= BASE_SIZE || y < 0 || y >= BASE_SIZE || grid[x][y] != 0)
	{
		return 0;
	}

	// Add connected facilities to grid
	int total = 1;
	grid[x][y]++;

	// Check for facilities under construction bigger than a square
	if (_facilities[x][y].getBuildTime() > 0)
	{
		if (x > 0 && _facilities[x - 1][y] == _facilities[x][y])
		{
			total += countConnected(x - 1, y, grid, remove);
		}
		if (y > 0 && _facilities[x][y - 1] == _facilities[x][y])
		{
			total += countConnected(x, y - 1, grid, remove);
		}
		if (x < BASE_SIZE - 1 && _facilities[x + 1][y] == _facilities[x][y])
		{
			total += countConnected(x + 1, y, grid, remove);
		}
		if (y < BASE_SIZE - 1 && _facilities[x][y + 1] == _facilities[x][y])
		{
			total += countConnected(x, y + 1, grid, remove);
		}
		return total;
	}

	total += countConnected(x - 1, y, grid, remove);
	total += countConnected(x, y - 1, grid, remove);
	total += countConnected(x + 1, y, grid, remove);
	total += countConnected(x, y + 1, grid, remove);

	// Delete connection grid
	if (newgrid)
	{
		for (int xx = 0; xx < BASE_SIZE; xx++)
		{
			grid[xx] = null;
		}
		 grid = null;
	}

	return total;
}

/**
 * Keeps the animation timers running.
 */
public void think()
{
	_timer.think(null, this);
}

/**
 * Makes the facility selector blink.
 */
public void blink()
{
	_blink = !_blink;

	if (_selSize > 0)
	{
		Rect r;
		if (_blink)
		{
			r.right = _selector.getWidth();
			r.bottom = _selector.getHeight();
			r.left = 0;
			r.top = 0;
            _selector.drawRect(r, Palette.blockOffset(1));
			r.right -= 2;
			r.bottom -= 2;
			r.left++;
			r.top++;
            _selector.drawRect(r, (short)0);
		}
		else
		{
			r.right = _selector.getWidth();
			r.bottom = _selector.getHeight();
			r.left = 0;
			r.top = 0;
            _selector.drawRect(r, (short)0);
		}
	}
}

/**
 * Draws the view of all the facilities in the base, connectors
 * between them and crafts landed in hangars.
 */
public void draw()
{
	// Draw grid squares
	for (int x = 0; x < 8; x++)
	{
		for (int y = 0; y < 8; y++)
		{
			Surface frame = _texture.getFrame(0);
			frame.setX(x * GRID_SIZE);
			frame.setY(y * GRID_SIZE);
			frame.blit(this);
		}
	}

	Vector<Craft>.iterator craft = _base.getCrafts().begin();

	for (BaseFacility i: _base.getFacilities())
	{
		// Draw facility shape
		int num = 0;
		for (int y = (i).getY(); y < (i).getY() + (i).getRules().getSize(); y++)
		{
			for (int x = (i).getX(); x < (i).getX() + (i).getRules().getSize(); x++)
			{
				Surface frame;

				if ((i).getBuildTime() == 0)
					frame = _texture.getFrame((i).getRules().getSpriteShape() + num);
				else
					frame = _texture.getFrame((i).getRules().getSpriteShape() + num + 2 + (i).getRules().getSize());

				frame.setX(x * GRID_SIZE);
				frame.setY(y * GRID_SIZE);
				frame.blit(this);

				num++;
			}
		}
	}

	for (BaseFacility i: _base.getFacilities())
	{
		// Draw connectors
		if ((i).getBuildTime() == 0)
		{
			// Facilities to the right
			int x = (i).getX() + (i).getRules().getSize();
			if (x < BASE_SIZE)
			{
				for (int y = (i).getY(); y < (i).getY() + (i).getRules().getSize(); y++)
				{
					if (_facilities[x][y] != 0 && _facilities[x][y].getBuildTime() == 0)
					{
						Surface frame = _texture.getFrame(7);
						frame.setX(x * GRID_SIZE - GRID_SIZE / 2);
						frame.setY(y * GRID_SIZE);
						frame.blit(this);
					}
				}
			}

			// Facilities to the bottom
			int y = (i).getY() + (i).getRules().getSize();
			if (y < BASE_SIZE)
			{
				for (int x = (i).getX(); x < (i).getX() + (i).getRules().getSize(); x++)
				{
					if (_facilities[x][y] != 0 && _facilities[x][y].getBuildTime() == 0)
					{
						Surface frame = _texture.getFrame(8);
						frame.setX(x * GRID_SIZE);
						frame.setY(y * GRID_SIZE - GRID_SIZE / 2);
						frame.blit(this);
					}
				}
			}
		}
	}

	for (BaseFacility i: _base.getFacilities())
	{
		// Draw facility graphic
		int num = 0;
		for (int y = (i).getY(); y < (i).getY() + (i).getRules().getSize(); y++)
		{
			for (int x = (i).getX(); x < (i).getX() + (i).getRules().getSize(); x++)
			{
				if ((i).getRules().getSize() == 1)
				{
					Surface frame = _texture.getFrame((i).getRules().getSpriteFacility() + num);
					frame.setX(x * GRID_SIZE);
					frame.setY(y * GRID_SIZE);
					frame.blit(this);
				}

				num++;
			}
		}

		// Draw crafts
		if ((i).getBuildTime() == 0 && (i).getRules().getCrafts() > 0 && craft != _base.getCrafts().end())
		{
			Surface frame = _texture.getFrame((craft).getRules().getSprite() + 33);
			frame.setX((i).getX() * GRID_SIZE + ((i).getRules().getSize() - 1) * GRID_SIZE / 2);
			frame.setY((i).getY() * GRID_SIZE + ((i).getRules().getSize() - 1) * GRID_SIZE / 2);
			frame.blit(this);
			++craft;
		}

		// Draw time remaining
		if ((i).getBuildTime() > 0)
		{
			Text text = new Text(GRID_SIZE * (i).getRules().getSize(), 16, 0, 0);
			text.setPalette(getPalette());
			text.setFonts(_big, _small);
			text.setX((i).getX() * GRID_SIZE);
			text.setY((i).getY() * GRID_SIZE + (GRID_SIZE * (i).getRules().getSize() - 16) / 2);
			text.setBig();
			WStringstream ss;
			ss << (i).getBuildTime();
			text.setAlign(TextHAlign.ALIGN_CENTER);
			text.setColor(Palette.blockOffset(13)+5);
			text.setText(ss.str());
			text.blit(this);
			delete text;
		}
	}
}

/**
 * Blits the base view and selector.
 * @param surface Pointer to surface to blit onto.
 */
public void blit(Surface surface)
{
	super.blit(surface);
	if (_selector != 0)
	{
		_selector.blit(surface);
	}
}

/**
 * Selects the facility the mouse is over.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseOver(Action action, State state)
{
	double x = action.getXMouse() - _x * action.getXScale(), y = action.getYMouse() - _y * action.getYScale();
	_gridX = (int)floor(x / (GRID_SIZE * action.getXScale()));
	_gridY = (int)floor(y / (GRID_SIZE * action.getYScale()));
	if (_gridX >= 0 && _gridX < BASE_SIZE && _gridY >= 0 && _gridY < BASE_SIZE)
	{
		_selFacility = _facilities[_gridX][_gridY];
		if (_selSize > 0)
		{
			if (_gridX + _selSize - 1 < BASE_SIZE && _gridY + _selSize - 1 < BASE_SIZE)
			{
				_selector.setX(_x + _gridX * GRID_SIZE);
				_selector.setY(_y + _gridY * GRID_SIZE);
				_selector.setVisible(true);
			}
			else
			{
				_selector.setVisible(false);
			}
		}
	}
	else
	{
		_selFacility = 0;
		if (_selSize > 0)
		{
			_selector.setVisible(false);
		}
	}

	InteractiveSurface.mouseOver(action, state);
}

/**
 * Deselects the facility.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseOut(Action action, State state)
{
	_selFacility = 0;
	if (_selSize > 0)
	{
		_selector.setVisible(false);
	}

	InteractiveSurface.mouseOut(action, state);
}

}
