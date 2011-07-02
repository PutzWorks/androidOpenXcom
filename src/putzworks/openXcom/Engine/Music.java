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

public class Music
{
		Mix_Music _music;

/**
 * Initializes a new music track.
 */
public Music()
{
	_music = new Mix_Music();
}

/**
 * Deletes the loaded music content.
 */
public void clearMusic()
{
	Mix_FreeMusic(_music);
}

/**
 * Loads a music file from a specified filename.
 * @param filename Filename of the music file.
 */
public void load(final String filename)
{
	_music = Mix_LoadMUS(filename.c_str());
	if (_music == 0) 
	{
		throw Exception(Mix_GetError());
	}
}

/**
 * Loads a music file from a specified memory chunk.
 * @param data Pointer to the music file in memory
 * @param size Size of the music file in bytes.
 */
public void load(final void data, int size)
{
	SDL_RWops rwops = SDL_RWFromConstMem(data, size);
	_music = Mix_LoadMUS_RW(rwops);
	SDL_FreeRW(rwops);
	if (_music == 0)
	{
		throw Exception(Mix_GetError());
	}
}

/**
 * Plays the contained music track.
 */
public final void play()
{
	if (_music != 0 && Mix_PlayMusic(_music, -1) == -1)
	{
		throw Exception(Mix_GetError());
	}
}

}
