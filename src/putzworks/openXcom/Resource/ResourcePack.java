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
package putzworks.openXcom.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import putzworks.openXcom.Engine.*;
import putzworks.openXcom.Geoscape.Polygon;
import putzworks.openXcom.Geoscape.Polyline;

public class ResourcePack
{
	protected String _folder;
	protected HashMap<String, Palette> _palettes;
	protected HashMap<String, Font> _fonts;
	protected HashMap<String, Surface> _surfaces;
	protected HashMap<String, SurfaceSet> _sets;
	protected HashMap<String, SoundSet> _sounds;
	protected List<Polygon> _polygons;
	protected List<Polyline> _polylines;
	protected HashMap<String, Music> _musics;
	protected Vector<Integer> _voxelData;

/**
 * Initializes a blank resource set pointing to a folder.
 * @param folder Subfolder to load resources from.
 */
public ResourcePack(final String folder)
{
	_folder = folder; 
	_palettes = new HashMap<String, Palette>();
	_fonts = new HashMap<String, Font>();
	_surfaces = new HashMap<String, Surface>();
	_sets = new HashMap<String, SurfaceSet>();
	_polygons = null;
	_musics = new HashMap<String, Music>();

}

/**
 * Takes a filename and tries to figure out the existing
 * case-insensitive filename for it, for file operations.
 * @param filename Original filename.
 * @return Correctly-cased filename or "" if it doesn't exist.
 * @note There's no actual method for figuring out the correct
 * filename on case-sensitive systems, this is just a workaround.
 */
String insensitive(final String filename)
{
	String newName = filename;

	// Ignore DATA folder
	size_t i = newName.find("/DATA/");
	if (i != String.npos)
		i += 6;
	else
		i = 0;

	// Try lowercase and uppercase names
	struct stat info;
	if (stat(newName.c_str(), info) == 0)
	{
		return newName;
	}
	else
	{
		for (String.iterator c = newName.begin() + i; c != newName.end(); ++c)
			c = toupper(c);
		if (stat(newName.c_str(), info) == 0)
		{
			return newName;
		}
		else
		{
			for (String.iterator c = newName.begin() + i; c != newName.end(); ++c)
				c = tolower(c);
			if (stat(newName.c_str(), info) == 0)
			{
				return newName;
			}
			else
			{
				return "";
			}
		}
	}
}

/**
 * Gets the data folder name.
 * Certain battlescape data is loaded on-the-fly
 * it needs to know the folder where to load it.
 * @return the data folder name.
 */
public final String getFolder()
{
	return _folder;
}

/**
 * Returns a specific font from the resource set.
 * @param name Name of the font.
 * @return Pointer to the font.
 */
public Font getFont(String name)
{
	return _fonts[name];
}

/**
 * Returns a specific surface from the resource set.
 * @param name Name of the surface.
 * @return Pointer to the surface.
 */
public Surface getSurface(final String name)
{
	return _surfaces[name];
}

/**
 * Returns a specific surface set from the resource set.
 * @param name Name of the surface set.
 * @return Pointer to the surface set.
 */
public SurfaceSet getSurfaceSet(final String &name)
{
	return _sets[name];
}

/**
 * Returns the list of polygons in the resource set.
 * @return Pointer to the list of polygons.
 */
public List<Polygon> getPolygons()
{
	return  _polygons;
}

/**
 * Returns the list of polylines in the resource set.
 * @return Pointer to the list of polylines.
 */
public List<Polyline> getPolylines()
{
	return _polylines;
}

/**
 * Returns a specific music from the resource set.
 * @param name Name of the music.
 * @return Pointer to the music.
 */
public Music getMusic(final String name)
{
	return _musics[name];
}

/**
 * Returns a specific sound set from the resource set.
 * @param name Name of the sound set.
 * @return Pointer to the sound set.
 */
public SoundSet getSoundSet(final String name)
{
	return _sounds[name];
}

/**
 * Returns a specific palette from the resource set.
 * @param name Name of the palette.
 * @return Pointer to the palette.
 */
public Palette getPalette(final String name)
{
	return _palettes[name];
}

/**
 * Changes the palette of all the graphics in the resource set.
 * @param colors Pointer to the set of colors.
 * @param firstcolor Offset of the first color to replace.
 * @param ncolors Amount of colors to replace.
 */
public void setPalette(SDL_Color colors, int firstcolor, int ncolors)
{
	for (HashMap<String, Font>.iterator i = _fonts.begin(); i != _fonts.end(); ++i)
	{
		i.second.getSurface().setPalette(colors, firstcolor, ncolors);
	}
	for (HashMap<String, Surface>.iterator i = _surfaces.begin(); i != _surfaces.end(); ++i)
	{
		i.second.setPalette(colors, firstcolor, ncolors);
	}
	for (HashMap<String, SurfaceSet>.iterator i = _sets.begin(); i != _sets.end(); ++i)
	{
		i.second.setPalette(colors, firstcolor, ncolors);
	}
}

/**
 * Returns the list of voxeldata in the resource set.
 * @return Pointer to the list of voxeldata.
 */
public Vector<Integer> getVoxelData()
{
	return _voxelData;
}

}
