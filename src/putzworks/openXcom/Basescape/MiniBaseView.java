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

import java.util.Vector;

import android.graphics.Rect;
import putzworks.openXcom.Engine.*;
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.BaseFacility;

import java.util.Vector;


public class MiniBaseView extends InteractiveSurface
{
	private Vector<Base> _bases;
	private SurfaceSet _texture;
	private int _base, _hoverBase;
	private static final int MAX_BASES = 8;
	private static final int MINI_SIZE = 14;
/**
 * Sets up a mini base view with the specified size and position.
 * @param width Width in pixels.
 * @param height Height in pixels.
 * @param x X position in pixels.
 * @param y Y position in pixels.
 */
MiniBaseView(int width, int height, int x, int y)
{
	super(width, height, x, y);
	_bases = new Vector<Base>();
	_texture = null; 
	_base = 0;
	_hoverBase = 0;

	_validButton = SDL_BUTTON_LEFT;
}

/**
 * Changes the current list of bases to display.
 * @param bases Pointer to base list to display.
 */
public void setBases(Vector<Base> bases)
{
	_bases = bases;
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
 * Returns the base the mouse cursor is currently over.
 * @return ID of the base.
 */
public int getHoveredBase()
{
	return _hoverBase;
}

/**
 * Changes the base that is currently selected on
 * the mini base view.
 * @param base ID of base.
 */
public void setSelectedBase(int base)
{
	_base = base;
	draw();
}

/**
 * Draws the view of all the bases with facilities
 * in varying colors.
 */
public void draw()
{
	clear();
	for (int i = 0; i < MAX_BASES; ++i)
	{
		// Draw base squares
		if (i == _base)
		{
			Rect r;
			r.left = i * (MINI_SIZE + 2);
			r.top = 0;
			r.right = r.left + MINI_SIZE + 2;
			r.bottom = r.top + MINI_SIZE + 2;
            drawRect(r, 1);
		}
		_texture.getFrame(41).setX(i * (MINI_SIZE + 2));
		_texture.getFrame(41).setY(0);
		_texture.getFrame(41).blit(this);

		// Draw facilities
		if (i < _bases.size())
		{
			Rect r;
			lock();
			for (BaseFacility f: _bases.at(i).getFacilities())
			{
				int pal;
				if ((f).getBuildTime() == 0)
					pal = 3;
				else
					pal = 2;

				r.left = i * (MINI_SIZE + 2) + 2 + (f).getX() * 2;
				r.top = 2 + (f).getY() * 2;
				r.right = r.left + (f).getRules().getSize() * 2;
				r.bottom = r.top + (f).getRules().getSize() * 2;
                drawRect(r, Palette.blockOffset(pal)+3);
				r.x++;
				r.y++;
				r.w--;
				r.h--;
                drawRect(r, Palette.blockOffset(pal)+5);
				r.x--;
				r.y--;
                drawRect(r, Palette.blockOffset(pal)+2);
				r.x++;
				r.y++;
				r.w--;
				r.h--;
                drawRect(r, Palette.blockOffset(pal)+3);
				r.x--;
				r.y--;
				setPixel(r.x, r.y, Palette.blockOffset(pal)+1);
			}
			unlock();
		}
	}
}

/**
 * Selects the base the mouse is over.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseOver(Action action, State state)
{
	double x = action.getXMouse() - getX() * action.getXScale();
	_hoverBase = (int)Math.floor(x / ((MINI_SIZE + 2) * action.getXScale()));

	super.mouseOver(action, state);
}

}
