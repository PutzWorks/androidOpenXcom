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
package putzworks.openXcom.Interface;

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.Surface;
import putzworks.openXcom.Engine.SurfaceHandler;
import putzworks.openXcom.Engine.Timer;
import putzworks.openXcom.SDL.SDL_Color;

public class FpsCounter extends Surface
{
	private NumberText _text;
	private Timer _timer;
	private int _frames;

/**
 * Creates a FPS counter of the specified size.
 * @param width Width in pixels.
 * @param height Height in pixels.
 * @param x X position in pixels.
 * @param y Y position in pixels.
 */
public FpsCounter(int width, int height, int x, int y) 
{
	super(width, height, x, y);
	_frames = 0;
	_visible = false;

	_timer = new Timer(1000);
	_timer.onTimer(new SurfaceHandler() {
		public void handle(Surface surface) {
			update();
		}
	});
	_timer.start();

	_text = new NumberText(width, height, x, y);
	setColor(Palette.blockOffset(15)+12);
}

/**
 * Deletes FPS counter content.
 */
public void clearFpsCounter()
{
	_text = null;
	_timer = null;
}

/**
 * Replaces a certain amount of colors in the FPS counter palette.
 * @param colors Pointer to the set of colors.
 * @param firstcolor Offset of the first color to replace.
 * @param ncolors Amount of colors to replace.
 */
public void setPalette(SDL_Color colors, int firstcolor, int ncolors)
{
	super.setPalette(colors, firstcolor, ncolors);
	_text.setPalette(colors, firstcolor, ncolors);
}

/**
 * Sets the text color of the counter.
 * @param color The color to set.
 */
public void setColor(short color)
{
	_text.setColor(color);
}

/**
 * Shows / hides the FPS counter.
 * @param action Pointer to an action.
 */
public void handle(Action action)
{
	if (action.getDetails().type == SDL_KEYDOWN && action.getDetails().key.keysym.sym == SDLK_F5)
	{
		_visible = !_visible;
	}
}

/**
 * Advances frame counter.
 */
public void think()
{
	_frames++;
	_timer.think(null, this);
}

/**
 * Updates the amount of Frames per Second.
 */
public void update()
{
	int fps = (int)floor((double)_frames / _timer.getTime() * 1000);
	_text.setValue(fps);
	_frames = 0;
	draw();
}

/**
 * Draws the FPS counter.
 */
public void draw()
{
	clear();
	_text.blit(this);
}

}
