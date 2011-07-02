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

import java.util.List;

import putzworks.openXcom.Interface.Cursor;
import putzworks.openXcom.Interface.FpsCounter;
import putzworks.openXcom.Resource.ResourcePack;
import putzworks.openXcom.Ruleset.Ruleset;
import putzworks.openXcom.Savegame.SavedGame;


public class Game
{
	private SDL_Event _event;
	private Screen _screen;
	private Cursor _cursor;
	private Language _lang;
	private List<State> _states, _deleted;
	private ResourcePack _res;
	private SavedGame _save;
	private Ruleset _rules;
	private boolean _quit, _init;
	private FpsCounter _fpsCounter;

/**
 * Starts up SDL with all the subsystems and SDL_mixer for audio processing,
 * creates the display screen and sets up the cursor.
 * @param title Title of the game window.
 * @param width Width of the display screen.
 * @param height Height of the display screen.
 * @param bpp Bits-per-pixel of the display screen.
 * @warning Currently the game is designed for 8bpp, so there's no telling what'll
 * happen if you use a different value.
 */
public Game(final String title, int width, int height, int bpp)
{
	_screen = null;
	_cursor = null;
	_lang = new Language();
	_states = null;
	_deleted =  null;
	_res = null;
	_save = null;
	_rules = new Ruleset();
	_quit = false;
	_init = false;
	// Initialize SDL
	if (SDL_Init(SDL_INIT_VIDEO | SDL_INIT_AUDIO) < 0)
	{
		throw Exception(SDL_GetError());
	}

	// Initialize SDL_mixer
	if (Mix_OpenAudio(22050, AUDIO_S16SYS, 2, 1024) != 0)
	{
		throw Exception(Mix_GetError());
	}
	Mix_AllocateChannels(16);

	// Set the window caption
	SDL_WM_SetCaption(title.c_str(), 0);

	// Set up keyboard events
	SDL_EnableKeyRepeat(SDL_DEFAULT_REPEAT_DELAY, SDL_DEFAULT_REPEAT_INTERVAL);
	SDL_EnableUNICODE(1);

	// Create display
	_screen = new Screen(width, height, bpp);

	// Create cursor
	_cursor = new Cursor(9, 13);
	_cursor.setColor(Palette.blockOffset(15)+12);

	// Create fps counter
	_fpsCounter = new FpsCounter(15, 5, 0, 0);
}

/**
 * Deletes the display screen, cursor, states and shuts down all the SDL subsystems.
 */
public void clearGame()
{
	Mix_HaltChannel(-1);

	Mix_CloseAudio();

	SDL_Quit();
}

/**
 * The state machine takes care of passing all the events from SDL to the
 * active state, running any code within and blitting all the states and
 * cursor to the screen. This is run indefinitely until the game quits.
 */
public void run()
{
	while (!_quit)
	{
		// Clean up states
		while (!_deleted.isEmpty())
		{
			delete _deleted.back();
			_deleted.pop_back();
		}

		// Initialize active state
		if (!_init)
		{
			_states.back().init();
			_init = true;

			// Unpress buttons
			for (Surface i: _states.back().getSurfaces())
			{
				InteractiveSurface s = (InteractiveSurface)(i);
				if (s != null)
				{
					s.unpress(_states.back());
				}
			}

			// Refresh mouse position
			SDL_Event ev;
			int x, y;
			SDL_GetMouseState(x, y);
			ev.type = SDL_MOUSEMOTION;
			ev.motion.x = x;
			ev.motion.y = y;
			Action action = Action(ev, _screen.getXScale(), _screen.getYScale());
			_states.back().handle(action);
		}

		// Process events
		while (SDL_PollEvent(_event))
		{
			if (_event.type == SDL_QUIT)
			{
				_quit = true;
			}
			else
			{
				Action action = Action(_event, _screen.getXScale(), _screen.getYScale());
				_screen.handle(action);
				_cursor.handle(action);
				_fpsCounter.handle(action);
				_states.back().handle(action);
			}
		}

		// Process logic
		_fpsCounter.think();
		_states.back().think();

		// Process rendering
		if (_init)
		{
			_screen.clear();
			std.list<State*>.iterator i = _states.end();
			do
			{
				--i;
			}
			while(i != _states.begin() && !(*i).isScreen());

			for (; i != _states.end(); ++i)
			{
				(*i).blit();
			}
			_fpsCounter.blit(_screen.getSurface());
			_cursor.blit(_screen.getSurface());
		}
		_screen.flip();

		SDL_Delay(0);
	}
}

/**
 * Stops the state machine and the game is shut down.
 */
public void quit()
{
	_quit = true;
}

/**
 * Returns the display screen used by the game.
 * @return Pointer to the screen.
 */
public final Screen getScreen()
{
	return _screen;
}

/**
 * Returns the mouse cursor used by the game.
 * @return Pointer to the cursor.
 */
public final Cursor getCursor()
{
	return _cursor;
}

/**
 * Returns the FpsCounter used by the game.
 * @return Pointer to the FpsCounter.
 */
public final FpsCounter getFpsCounter()
{
	return _fpsCounter;
}

/**
 * Replaces a certain amount of colors in the palettes of the game's
 * screen and resources.
 * @param colors Pointer to the set of colors.
 * @param firstcolor Offset of the first color to replace.
 * @param ncolors Amount of colors to replace.
 */
public void setPalette(SDL_Color colors, int firstcolor, int ncolors)
{
	_screen.setPalette(colors, firstcolor, ncolors);
	_cursor.setPalette(colors, firstcolor, ncolors);
	_cursor.draw();

	_fpsCounter.setPalette(colors, firstcolor, ncolors);

	if (_res != 0)
	{
		_res.setPalette(colors, firstcolor, ncolors);
	}
}

/**
 * Pops all the states currently in stack and pushes in the new state.
 * A shortcut for cleaning up all the old states when they're not necessary
 * like in one-way transitions.
 * @param state Pointer to the new state.
 */
public void setState(State state)
{
	while (!_states.isEmpty())
	{
		popState();
	}
	pushState(state);
	_init = false;
}

/**
 * Pushes a new state into the top of the stack and initializes it.
 * The new state will be used once the next game cycle starts.
 * @param state Pointer to the new state.
 */
public void pushState(State state)
{
	_states.add(state);
	_init = false;
}

/**
 * Pops the last state from the top of the stack. Since states
 * can't actually be deleted mid-cycle, it's moved into a separate queue
 * which is cleared at the start of every cycle, so the transition
 * is seamless.
 */
public void popState()
{
	_deleted.add(_states.back());
	_states.pop_back();
	_init = false;
}

/**
 * Returns the language currently in use by the game.
 * @return Pointer to the language.
 */
public final Language getLanguage()
{
	return final _lang;
}

/**
 * Changes the language currently in use by the game.
 * @param lang Pointer to the language.
 */
public void setLanguage(Language lang)
{
	delete _lang;
	_lang = lang;
}

/**
 * Returns the resource pack currently in use by the game.
 * @return Pointer to the resource pack.
 */
public final ResourcePack getResourcePack()
{
	return final _res;
}

/**
 * Sets a new resource pack for the game to use.
 * @param res Pointer to the resource pack.
 */
public void setResourcePack(ResourcePack res)
{
	_res = res;
}

/**
 * Returns the saved game currently in use by the game.
 * @return Pointer to the saved game.
 */
public final SavedGame getSavedGame()
{
	return final _save;
}

/**
 * Sets a new saved game for the game to use.
 * @param save Pointer to the saved game.
 */
public void setSavedGame(SavedGame save)
{
	delete _save;
	_save = save;
}

/**
 * Returns the ruleset currently in use by the game.
 * @return Pointer to the ruleset.
 */
public final Ruleset getRuleset()
{
	return final _rules;
}

/**
 * Sets a new ruleset for the game to use.
 * @param rules Pointer to the ruleset.
 */
public void setRuleset(Ruleset rules)
{
	delete _rules;
	_rules = rules;
}

}
