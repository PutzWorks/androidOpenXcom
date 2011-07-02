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

public class CatFile extends std.ifstream
{
		private int _amount, _offset, _size;

/**
 * Creates a CAT file stream. A CAT file starts with an index of the
 * offset and size of every file contained within. Each file consists
 * of a filename followed by its contents.
 * @param path Full path to CAT file.
 */
public CatFile(final char path)
{
	super(path, std.ios.in | std.ios.binary);
	_amount = 0; 
	_offset = 0; 
	_size = 0;

	if (!this)
		return;

	// Get amount of files
	read((char)_amount, sizeof(_amount));
	_amount /= 2 * sizeof(_amount);

	// Get object offsets
	seekg(0, std.ios.beg);

	_offset = new int[_amount];
	_size   = new int[_amount];

	for (int i = 0; i < _amount; ++i)
	{
		read((char)_offset[i], sizeof(_offset));
		read((char)_size[i],   sizeof(_size));
	}
}

/**
 * Frees associated memory.
 */
public void clearCatFile()
{
	_offset = 0;
	_size = 0;

	close();
}

/**
 * Loads an object into memory.
 * @param i Object number to load.
 * @return Pointer to the loaded object.
 */
public char load(int i)
{
	if (i >= _amount)
		return 0;

	seekg(_offset[i], std.ios.beg);

	// Skip filename
	char namesize;
	read(namesize, 1);
	seekg(namesize, std.ios.cur);

	// Read object
	char object = new char[_size[i]];
	read(object, _size[i]);

	return object;
}

public final boolean operator !()
	{
		return std::ifstream::operator!();
	}
	/// Get amount of objects.
public final int getAmount()
	{
		return _amount;
	}
	/// Get object size.
public final int getObjectSize(int i)
	{
		return (i < _amount) ? _size[i] : 0;
	}

}
