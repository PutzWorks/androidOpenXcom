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

public class Sound
{
	Mix_Chunk _sound;

/**
 * Initializes a new sound effect.
 */
public Sound()
{
	_sound = new Mux_Chunk;
}

/**
 * Deletes the loaded sound content.
 */
public void clearSound()
{
	Mix_FreeChunk(_sound);
}

/**
 * Loads a sound file from a specified filename.
 * @param filename Filename of the sound file.
 */
public void load(final String filename)
{
	_sound = Mix_LoadWAV(filename.c_str());
	if (_sound == 0) 
	{
		throw Exception(Mix_GetError());
	}
}

/**
 * Loads a sound file from a specified memory chunk.
 * @param data Pointer to the sound file in memory
 * @param size Size of the sound file in bytes.
 */
public void load(final void data, int size)
{
	SDL_RWops rw = SDL_RWFromConstMem(data, size);
	_sound = Mix_LoadWAV_RW(rw, 1);
	if (_sound == 0)
	{
		throw Exception(Mix_GetError());
	}
}

/**
 * Plays the contained sound effect.
 */
public final void play()
{
	if (_sound != 0 && Mix_PlayChannel(-1, _sound, 0) == -1)
	{
		std.cerr << Mix_GetError() << std.endl;
	}
}

}
