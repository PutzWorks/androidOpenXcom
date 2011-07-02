/*
 * Copyright 2010 OpenXcom Developers..
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

public class Surface
{
	protected SDL_Surface _surface;
	protected int _x, _y;
	protected Rectangle _crop;
	protected boolean _visible, _hidden;

/**
 * Sets up a blank 8bpp surface with the specified size and position,
 * with pure black as the transparent color.
 * @note Surfaces don't have to fill the whole size since their
 * background is transparent, specially subclasses with their own
 * drawing logic, so it just covers the maximum drawing area.
 * @param width Width in pixels.
 * @param height Height in pixels.
 * @param x X position in pixels.
 * @param y Y position in pixels.
 */
public Surface(int width, int height, int x, int y)
{
	_x = x;
	_y = y;
	_visible = true;
	_hidden = false;
	_surface = SDL_CreateRGBSurface(SDL_SWSURFACE, width, height, 8, 0, 0, 0, 0);

	if (_surface == null)
	{
		throw Exception(SDL_GetError());
	}

	SDL_SetColorKey(_surface, SDL_SRCCOLORKEY, 0);

	_crop.x = 0;
	_crop.y = 0;
	_crop.w = 0;
	_crop.h = 0;
}

/**
 * Performs a deep copy of an existing surface.
 * @param other Surface to copy from.
 */
public Surface(final Surface other)
{
	_x = other._x;
	_y = other._y;
	_crop.y = other._crop.y;
	_crop.x = other._crop.x;
	_crop.w = other._crop.w;
	_crop.h = other._crop.h;
	_visible = other._visible;
	_hidden = other._hidden;
	_surface = SDL_ConvertSurface(other._surface, other._surface.format, other._surface.flags);
}

/**
 * Deletes the surface from memory.
 */
public void clearSurface()
{
	SDL_FreeSurface(_surface);
}

/**
 * Loads the contents of an X-Com SCR image file into
 * the surface. SCR files are simply uncompressed images
 * containing the palette offset of each pixel.
 * @param filename Filename of the SCR image.
 * @sa http://www.ufopaedia.org/index.php?title=Image_Formats#SCR_.26_DAT
 */
public void loadScr(final String filename)
{
	// Load file and put pixels in surface
	std.ifstream imgFile (filename.c_str(), std.ios.in | std.ios.binary);
	if (!imgFile)
	{
		throw Exception("Failed to load SCR");
	}

	// Lock the surface
	lock();
	
	short value;
	int x = 0, y = 0;

	while (imgFile.read((char)value, 1))
	{
		setPixelIterative(x, y, value);
	}

	if (!imgFile.eof())
	{
		throw Exception("Invalid data from file");
	}

	// Unlock the surface
	unlock();

	imgFile.close();
}

/**
 * Loads the contents of an X-Com SPK image file into
 * the surface. SPK files are compressed with a custom
 * algorithm since they're usually full-screen images.
 * @param filename Filename of the SPK image.
 * @sa http://www.ufopaedia.org/index.php?title=Image_Formats#SPK
 */
public void loadSpk(final String filename)
{
	// Load file and put pixels in surface
	std.ifstream imgFile (filename.c_str(), std.ios.in | std.ios.binary);
	if (!imgFile)
	{
		throw Exception("Failed to load SPK");
	}

	// Lock the surface
	lock();
	
	int flag;
	short value;
	int x = 0, y = 0;

	while (imgFile.read((char)flag, sizeof(flag)) && flag != 65533)
	{
		if (flag == 65535)
		{
			imgFile.read((char)flag, sizeof(flag));
			for (int i = 0; i < flag * 2; i++)
			{
				setPixelIterative(x, y, 0);
			}
		}
		else if (flag == 65534)
		{
			imgFile.read((char)flag, sizeof(flag));
			for (int i = 0; i < flag * 2; i++)
			{
				imgFile.read((char)value, 1);
				setPixelIterative(x, y, value);
			}
		}
	}

	// Unlock the surface
	unlock();

	imgFile.close();
}

/**
 * Clears the entire contents of the surface, resulting
 * in a blank image.
 */
public void clear()
{
	Rectangle square;
	square.x = 0;
	square.y = 0;
	square.w = getWidth();
	square.h = getHeight();
	SDL_FillRectangle(_surface, square, 0);
}

/**
 * Shifts all the colors in the surface by a set amount.
 * This is a common method in 8bpp games to simulate color
 * effects for cheap.
 * @param off Amount to shift.
 * @param min Minimum color to shift to.
 * @param max Maximum color to shift to.
 */
public void offset(int off, int min, int max)
{
	// Lock the surface
	lock();
	
	for (int x = 0, y = 0; x < getWidth() && y < getHeight();)
	{
		short pixel = getPixel(x, y);
		int p = pixel + off;
		if (min != -1 && p < min)
		{
			p = min;
		}
		else if (max != -1 && p > max)
		{
			p = max;
		}

		if (pixel > 0)
		{
			setPixelIterative(x, y, p);
		}
		else
		{
			setPixelIterative(x, y, 0);
		}
	}

	// Unlock the surface
	unlock();
}

/**
 * Multiplies all the colors in the surface by a set factor.
 * @param factor Amount to multiply.
 */
public void multiply(int factor)
{
	// Lock the surface
	lock();
	
	for (int x = 0, y = 0; x < getWidth() && y < getHeight();)
	{
		short pixel = getPixel(x, y);
		setPixelIterative(x, y, pixel * factor);
	}

	// Unlock the surface
	unlock();
}

/**
 * Inverts all the colors in the surface according to a middle point.
 * Used for effects like shifting a button between pressed and unpressed.
 * @param mid Middle point.
 */
public void invert(short mid)
{
	// Lock the surface
	lock();

	for (int x = 0, y = 0; x < getWidth() && y < getHeight();)
	{
		short pixel = getPixel(x, y);
		if (pixel > 0)
		{
			setPixelIterative(x, y, pixel + 2 * (mid - pixel));
		}
		else
		{
			setPixelIterative(x, y, 0);
		}
	}

	// Unlock the surface
	unlock();
}

public void invert(int mid)
{
	// Lock the surface
	lock();

	for (int x = 0, y = 0; x < getWidth() && y < getHeight();)
	{
		short pixel = getPixel(x, y);
		if (pixel > 0)
		{
			setPixelIterative(x, y, pixel + 2 * ((short)mid - pixel));
		}
		else
		{
			setPixelIterative(x, y, 0);
		}
	}

	// Unlock the surface
	unlock();
}

/**
 * Sets the shade level of the surface.
 * Shade 0 is original color, 16 is black.
 * @param shade Amount to shade.
 */
public void setShade(int shade)
{
	// Lock the surface
	lock();
	for (int x = 0, y = 0; x < _surface.w && y < _surface.h;)
	{
		short pixel = getPixel(x, y);
		if (pixel != 0)
		{
			int baseColor = pixel/16;
			int originalShade = pixel%16;
			int newShade = originalShade + shade;
			if (newShade > 15)
			{
				baseColor = 0;
				newShade = 15;
			}
			if (originalShade != newShade || baseColor == 0)
			{
				pixel = (short)((baseColor*16) + newShade);
				setPixel(x, y, pixel);
			}
		}
		x++;
		if (x == getWidth())
		{
			y++;
			x = 0;
		}
	}

	// Unlock the surface
	unlock();
}

/**
 * Runs any code the surface needs to keep updating every
 * game cycle, like animations and other real-time elements.
 */
public void think()
{

}

/**
 * Draws the graphic that the surface contains and
 * gets blitted onto other surfaces.
 */
public void draw()
{

}

/**
 * Blits this surface onto another one, with its position
 * relative to the top-left corner of the target surface.
 * The cropping rectangle controls the portion of the surface
 * that is blitted.
 * @param surface Pointer to surface to blit onto.
 */
public void blit(Surface surface)
{
	if (_visible && !_hidden)
	{
		Rectangle cropper;
		Rectangle target;
		if (_crop.w == 0 && _crop.h == 0)
		{
			cropper = null;
		}
		else
		{
			cropper = _crop;
		}
		target.x = getX();
		target.y = getY();
		SDL_BlitSurface(_surface, cropper, surface.getSurface(), target);
	}
}

/**
 * Copies the exact contents of another surface onto this one.
 * Only the content that would overlap both surfaces is copied, in
 * accordance with their positions. This is handy for applying
 * effects over another surface without modifying the original.
 * @param surface Pointer to surface to copy from.
 */
public void copy(Surface surface)
{
	Rectangle from;
	from.x = getX() - surface.getX();
	from.y = getY() - surface.getY();
	from.w = getWidth();
	from.h = getHeight();
	SDL_BlitSurface(surface.getSurface(), from, _surface, 0);
}

/**
 * Copies the exact contents of another surface onto the areas that
 * match a certain color, like a mask. Surface sizes must match.
 * @param surface Pointer to surface to copy from.
 * @param mask Color mask to replace with the other surface.
 */
public void maskedCopy(Surface surface, short mask)
{
	if (surface.getWidth() != getWidth() || surface.getHeight() != getHeight())
	{
		return;
	}

	// Lock the surface
	lock();

	for (int x = 0, y = 0; x < getWidth() && y < getHeight();)
	{
		if (getPixel(x, y) == mask)
		{
			setPixelIterative(x, y, surface.getPixel(x, y));
		}
		else
		{
			setPixelIterative(x, y, this.getPixel(x, y));
		}
	}

	// Unlock the surface
	unlock();
}

/**
 * Draws a filled rectangle on the surface.
 * @param rect Pointer to Rectangle.
 * @param color Color of the rectangle.
 */
public void drawRectangle(Rectangle rect, short color)
{
    SDL_FillRectangle(_surface, rect, color);
}

public void drawRectangle(Rectangle rect, int color)
{
    SDL_FillRectangle(_surface, rect, (short)color);
}

/**
 * Draws a line on the surface.
 * @param x1 Start x coordinate in pixels.
 * @param y1 Start y coordinate in pixels.
 * @param x2 End x coordinate in pixels.
 * @param y2 End y coordinate in pixels.
 * @param color Color of the line.
 */
public void drawLine(int x1, int y1, int x2, int y2, short color)
{
    lineColor(_surface, x1, y1, x2, y2, Palette.getRGBA(getPalette(), color));
}

public void drawLine(int x1, int y1, int x2, int y2, int color)
{
	drawLine(x1, y1, x2, y2, (short) color);
}

/**
 * Draws a filled circle on the surface.
 * @param x X coordinate in pixels.
 * @param y Y coordinate in pixels.
 * @param r Radius in pixels.
 * @param color Color of the circle.
 */
public void drawCircle(int x, int y, int r, short color)
{
    filledCircleColor(_surface, x, y, r, Palette.getRGBA(getPalette(), color));
}

public void drawCircle(int x, int y, int r, int color)
{
    filledCircleColor(_surface, x, y, r, Palette.getRGBA(getPalette(), (short)color));
}

/**
 * Draws a filled polygon on the surface.
 * @param x Array of x coordinates.
 * @param y Array of y coordinates.
 * @param n Number of points.
 * @param color Color of the polygon.
 */
public void drawPolygon(int[] x, int[] y, int n, short color)
{
    filledPolygonColor(_surface, x, y, n, Palette.getRGBA(getPalette(), color));
}

/**
 * Draws a textured polygon on the surface.
 * @param x Array of x coordinates.
 * @param y Array of y coordinates.
 * @param n Number of points.
 * @param texture Texture for polygon.
 * @param dx X offset of texture relative to the screen.
 * @param dy Y offset of texture relative to the screen.
 */
public void drawTexturedPolygon(int[] x, int[] y, int n, Surface texture, int dx, int dy)
{
    texturedPolygon(_surface, x, y, n, texture.getSurface(), dx, dy);
}

/**
 * Draws a text string on the surface.
 * @param x X coordinate in pixels.
 * @param y Y coordinate in pixels.
 * @param s Character string to draw.
 * @param color Color of string.
 */
public void drawString(int x, int y, final char s, short color)
{
    stringColor(_surface, x, y, s, Palette.getRGBA(getPalette(), color));
}

/**
 * Changes the position of the surface in the X axis.
 * @param x X position in pixels.
 */
public void setX(int x)
{
	_x = x;
}

/**
 * Returns the position of the surface in the X axis.
 * @return X position in pixels.
 */
public final int getX()
{
	return _x;
}

/**
 * Changes the position of the surface in the Y axis.
 * @param y Y position in pixels.
 */
public void setY(int y)
{
	_y = y;
}

/**
 * Returns the position of the surface in the Y axis.
 * @return Y position in pixels.
 */
public final int getY()
{
	return _y;
}

/**
 * Resets the cropping rectangle set for this surface,
 * so the whole surface is blitted.
 */
public void resetCrop()
{
	_crop.y = 0;
	_crop.h = 0;
	_crop.x = 0;
	_crop.w = 0;
}

/**
 * Returns the cropping rectangle for this surface.
 * @return Pointer to the cropping rectangle.
 */
public Rectangle getCrop()
{
	return _crop;
}

/**
 * Replaces a certain amount of colors in the surface's palette.
 * @param colors Pointer to the set of colors.
 * @param firstcolor Offset of the first color to replace.
 * @param ncolors Amount of colors to replace.
 */
public void setPalette(SDL_Color colors, int firstcolor, int ncolors)
{
	SDL_SetColors(_surface, colors, firstcolor, ncolors);
}

/**
 * Returns the surface's 8bpp palette.
 * @return Pointer to the palette's colors.
 */
public SDL_Color getPalette()
{
	return _surface.format.palette.colors;
}

/**
 * Changes the color of a pixel in the surface, relative to
 * the top-left corner of the surface.
 * @param x X position of the pixel.
 * @param y Y position of the pixel.
 * @param i New color for the pixel.
 */
public void setPixel(int x, int y, int i)
{
	if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight())
	{
		return;
	}
    ((short)_surface.pixels)[y * _surface.pitch + x * _surface.format.BytesPerPixel] = (short)i;
}

public void setPixel(int x, int y, short i)
{
	if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight())
	{
		return;
	}
    ((short)_surface.pixels)[y * _surface.pitch + x * _surface.format.BytesPerPixel] = i;
}

/**
 * Changes the color of a pixel in the surface and returns the
 * next pixel position. Useful when changing a lot of pixels in
 * a row, eg. loading images.
 * @param x Pointer to the X position of the pixel. Changed to the next X position in the sequence.
 * @param y Pointer to the Y position of the pixel. Changed to the next Y position in the sequence.
 * @param pixel New color for the pixel.
 */
public void setPixelIterative(int x, int y, short pixel)
{
    setPixel(x, y, pixel);
	(x)++;
	if (x == getWidth())
	{
		(y)++;
		x = 0;
	}
}

public void setPixelIterative(int x, int y, int pixel)
{
    setPixel(x, y, (short)pixel);
	(x)++;
	if (x == getWidth())
	{
		(y)++;
		x = 0;
	}
}

/**
 * Returns the color of a specified pixel in the surface.
 * @param x X position of the pixel.
 * @param y Y position of the pixel.
 * @return Color of the pixel.
 */
public final short getPixel(int x, int y)
{
    return ((short)_surface.pixels)[y * _surface.pitch + x * _surface.format.BytesPerPixel];
}

/**
 * Returns the internal SDL_Surface for SDL calls.
 * @return Pointer to the surface.
 */
public SDL_Surface getSurface()
{
	return _surface;
}

/**
 * Returns the width of the surface.
 * @return Width in pixels.
 */
public final int getWidth()
{
	return _surface.w;
}

/**
 * Returns the height of the surface.
 * @return Height in pixels
 */
public final int getHeight()
{
	return _surface.h;
}

/**
 * Changes the visibility of the surface. A hidden surface
 * isn't blitted nor receives events.
 * @param visible New visibility.
 */
public void setVisible(boolean visible)
{
	_visible = visible;
}

/**
 * Returns the visible state of the surface.
 * @return Current visibility.
 */
public final boolean getVisible()
{
	return _visible;
}

/**
 * Hides the surface if it's visible. This is a separate
 * visibility setting intended for temporary effects,
 * so as to not override the actual visibility setting.
 */
public void hide()
{
	_hidden = true;
}

/**
 * Shows the surface if it's not invisible. This is a separate
 * visibility setting intended for temporary effects,
 * so as to not override the actual visibility setting.
 */
public void show()
{
	_hidden = false;
}

/**
 * Locks the surface from outside access
 * for pixel-level access. Must be unlocked
 * afterwards.
 * @sa unlock()
 */
public void lock()
{
	SDL_LockSurface(_surface);
}

/**
 * Unlocks the surface after it's been locked
 * to resume blitting operations.
 * @sa lock()
 */
public void unlock()
{
	SDL_UnlockSurface(_surface);
}

}
