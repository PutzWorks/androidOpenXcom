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
package putzworks.openXcom.Engine;

import android.graphics.Rect;

public class Screen
{
	private Surface _surface;
	private SDL_Surface _screen;
	private double _scaleX, _scaleY;
	private long _flags;
	private boolean _fullscreen;

	private static final double BASE_WIDTH = 320.0;
	private static final double BASE_HEIGHT = 200.0;

/**
 * Initializes a new display screen for the game to render contents to.
 * @param width Width in pixels.
 * @param height Height in pixels.
 * @param bpp Bits-per-pixel.
 */
public Screen(int width, int height, int bpp)
{
	_scaleX = 1.0;
	_scaleY = 1.0;
	_fullscreen = false;
	_flags = SDL_SWSURFACE|SDL_HWPALETTE;
	_screen = SDL_SetVideoMode(width, height, bpp, _flags);
	if (_screen == 0)
	{
		throw Exception(SDL_GetError());
	}
	_surface = new Surface(width, height);
}

/**
 * Deletes the buffer from memory. The display screen itself
 * is automatically freed once SDL shuts down.
 */
public void clearScreen()
{
	_surface = null;
}

/**
 * Returns the screen's internal buffer surface. Any
 * contents that need to be shown will be blitted to this.
 * @return Pointer to the buffer surface.
 */
public final Surface getSurface()
{
	return _surface;
}

/**
 * Handles screen key shortcuts.
 * @param action Pointer to an action.
 */
public void handle(Action action)
{
	if (action.getDetails().type == SDL_KEYDOWN && action.getDetails().key.keysym.sym == SDLK_RETURN && (SDL_GetModState() & KMOD_ALT) != 0)
	{
		setFullscreen(!_fullscreen);
	}
	else if (action.getDetails().type == SDL_KEYDOWN && action.getDetails().key.keysym.sym == SDLK_F12)
	{
		std.stringstream ss;
		int i = 0;
		struct stat info;
		do
		{
			ss.str("");
			ss << "./USER/" << "screen" << std.setfill('0') << std.setw(3) << i << ".bmp";
			i++;
		}
		while (stat(ss.str().c_str(), &info) == 0);
		SDL_SaveBMP(_screen, ss.str().c_str());
	}
}

/**
 * Renders the buffer's contents onto the screen, applying
 * any necessary filters or conversions in the process.
 * If the scaling factor is bigger than 1, the entire contents
 * of the buffer are resized by that factor (eg. 2 = doubled)
 * before being put on screen.
 */
public void flip()
{
	if (getWidth() != BASE_WIDTH || getHeight() != BASE_HEIGHT)
	{
		SDL_Surface zoom = zoomSurface(_surface.getSurface(), _scaleX, _scaleY, 0);
		SDL_BlitSurface(zoom, 0, _screen, 0);
		SDL_FreeSurface(zoom);
	}
	else
	{
		SDL_BlitSurface(_surface.getSurface(), 0, _screen, 0);
	}

	if (SDL_Flip(_screen) == -1)
    {
        throw Exception(SDL_GetError());
    }
}

/**
 * Clears all the contents out of the internal buffer.
 */
public void clear()
{
	_surface.clear();
	Rect square;
	square.left = 0;
	square.top = 0;
	square.right = square.left + getWidth();
	square.bottom = square.top + getHeight();
	SDL_FillRect(_screen, square, 0);
}

/**
 * Changes the 8bpp palette used to render the screen's contents.
 * @param colors Pointer to the set of colors.
 * @param firstcolor Offset of the first color to replace.
 * @param ncolors Amount of colors to replace.
 */
public void setPalette(SDL_Color colors, int firstcolor, int ncolors)
{
	_surface.setPalette(colors, firstcolor, ncolors);
	SDL_SetColors(_screen, colors, firstcolor, ncolors);
}

/**
 * Returns the screen's 8bpp palette.
 * @return Pointer to the palette's colors.
 */
public final SDL_Color getPalette()
{
	return _surface.getPalette();
}

/**
 * Returns the width of the screen.
 * @return Width in pixels.
 */
public final int getWidth()
{
	return _screen.w;
}

/**
 * Returns the height of the screen.
 * @return Height in pixels
 */
public final int getHeight()
{
	return _screen.h;
}

/**
 * Changes the screen's resolution. The display surface
 * and palette have to be reset for this to happen properly.
 * @param width Width in pixels.
 * @param height Height in pixels.
 */
public void setResolution(int width, int height)
{
	_scaleX = width / BASE_WIDTH;
	_scaleY = height / BASE_HEIGHT;
	_screen = SDL_SetVideoMode(width, height, _screen.format.BitsPerPixel, _flags);
	if (_screen == 0)
	{
		throw Exception(SDL_GetError());
	}
	setPalette(getPalette());
}

/**
 * Switches the screen between full-screen and/or windowed.
 * The screen has to be reset for this to happen properly.
 * @param full True for full-screen, False for windowed.
 */
public void setFullscreen(boolean full)
{
	_fullscreen = full;
	if (_fullscreen)
	{
		_flags |= SDL_FULLSCREEN;
	}
	else
	{
		_flags &= ~SDL_FULLSCREEN;
	}
	setResolution(getWidth(), getHeight());
}

/**
 * Returns the screen's X scale.
 * @return Scale factor.
 */
public final double getXScale()
{
	return _scaleX;
}

/**
 * Returns the screen's Y scale.
 * @return Scale factor.
 */
public final double getYScale()
{
	return _scaleY;
}

}
