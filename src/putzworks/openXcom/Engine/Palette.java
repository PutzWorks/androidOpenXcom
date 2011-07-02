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

import android.graphics.Color;

//TODO need to properly translate from SDL_Color or Android.Color

public class Palette
{
		private SDL_Color[] _colors;

/**
 * Initializes a brand new palette.
 */
public Palette()
{
	_colors = new SDL_Color[]();
}

/**
 * Loads an X-Com palette from a file. X-Com palettes are just a set
 * of RGB colors in a row, on a 0-63 scale, which have to be adjusted
 * for modern computers (0-255 scale).
 * @param filename Filename of the palette.
 * @param ncolors Number of colors in the palette.
 * @param offset Position of the palette in the file (in bytes).
 * @sa http://www.ufopaedia.org/index.php?title=PALETTES.DAT
 */
public void loadDat(final String filename, int ncolors, int offset)
{
	_colors = (SDL_Color)malloc(sizeof(SDL_Color) * ncolors);

	// Load file and put colors in pallete
	std.ifstream palFile (filename.c_str(), std.ios.in | std.ios.binary);
	if (!palFile)
	{
		throw Exception("Failed to load palette");
	}

	// Move pointer to proper pallete
	palFile.seekg(offset, std.ios.beg);
	
	short[] value = new short[3];

	for (int j = 0; j < ncolors && palFile.read((char)value, 3); j++)
	{
		// Correct X-Com colors to RGB colors
		_colors[j].r = value[0] * 4;
		_colors[j].g = value[1] * 4;
		_colors[j].b = value[2] * 4;
	}

	palFile.close();
}

/**
 * Provides access to colors contained in the palette.
 * @param offset Offset to a specific color.
 * @return Pointer to the requested SDL_Color.
 */
public final SDL_Color getColors(int offset)
{
	return _colors + offset;
}

/**
 * Converts an SDL_Color struct into an hexadecimal RGBA color value.
 * Mostly used for operations with SDL_gfx that require colors in this format.
 * @param pal Requested palette.
 * @param color Requested color in the palette.
 * @return Hexadecimal RGBA value.
 */
public long getRGBA(SDL_Color pal, short color)
{
	return ((long) pal[color].r << 24) | ((long) pal[color].g << 16) | ((long) pal[color].b << 8) | (long) 0xFF;
}

public static int palOffset(int palette) { return palette*(768+6); }
/// Gets the position of a certain color block in a palette.
/**
 * Returns the position of a certain color block in an X-Com palette (they're usually split in 16-color gradients).
 * Makes setting element colors a lot easier than determining the exact color position.
 * @param block Requested block.
 * @return Color position.
 */
public static short blockOffset(int block) { return (short)((short)block*16); }
/// Position of the background colors block in an X-Com palette (used for background images in screens).
public final static int backPos = 224;

}
