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

import java.util.Vector;

public class SoundSet
{
	private int _width, _height;
	private Vector<Sound> _sounds;

/**
 * Sets up a new empty sound set.
 */
public SoundSet()
{
	_sounds = new Vector<Sound>();
}

/**
 * Deletes the sounds from memory.
 */
public void clearSoundSet()
{
	for (Sound i: _sounds)
	{
		i = null;
	}
}

/**
 * Loads the contents of an X-Com CAT file which usually contains
 * a set of sound files. The CAT starts with an index of the offset
 * and size of every file contained within. Each file consists of a
 * filename followed by its contents.
 * @param filename Filename of the CAT set.
 * @param wav Are the sounds in WAV format?
 * @sa http://www.ufopaedia.org/index.php?title=SOUND
 */
public void loadCat(final String filename, boolean wav)
{
	// Load CAT file
	CatFile sndFile (filename.c_str());
	if (sndFile == null)
	{
		throw Exception("Failed to load CAT");
	}

	// Load each sound file
	for (int i = 0; i < sndFile.getAmount(); i++)
	{
		// Read WAV chunk
		char sound = sndFile.load(i);
		int size = sndFile.getObjectSize(i);

		// If there's no WAV header (44 bytes), add it
		// Assuming sounds are 8-bit 8000Hz (DOS version)
		char newsound = 0;
		if (!wav)
		{
			char header[] = {'R', 'I', 'F', 'F', 0x00, 0x00, 0x00, 0x00, 'W', 'A', 'V', 'E', 'f', 'm', 't', ' ',
							 0x10, 0x00, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x40, 0x1f, 0x00, 0x00, 0x40, 0x1f, 0x00, 0x00, 0x01, 0x00, 0x08, 0x00,
							 'd', 'a', 't', 'a', 0x00, 0x00, 0x00, 0x00};
			int headersize = size + 36;
			int soundsize = size;
			memcpy(header + 4, headersize, sizeof(headersize));
			memcpy(header + 40, soundsize, sizeof(soundsize));

			newsound = new char[44 + size];
			memcpy(newsound, header, 44);
			memcpy(newsound + 44, sound, size);
		}

		Sound s = new Sound();
		try
		{
			if (wav)
				s.load(sound, size);
			else
				s.load(newsound, 44 + size);
		}
		catch (Exception e)
		{
			// Ignore junk in the file
			e = e;
		}
		_sounds.add(s);

		delete[] sound;
		if (!wav)
		{
			newsound = null;
		}
	}
}

/**
 * Returns a particular wave from the sound set.
 * @param i Sound number in the set.
 * @return Pointer to the respective sound.
 */
public final Sound getSound(int i)
{
	if (i >= _sounds.size())
	{
		return 0;
	}
	return _sounds[i];
}

/**
 * Returns the total amount of sounds currently
 * stored in the set.
 * @return Number of sounds.
 */
public final int getTotalSounds()
{
	return _sounds.size();
}

}
