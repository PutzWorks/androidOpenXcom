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

public class Timer
{
	private long _start, _interval;
	private boolean _running;
	private StateHandler _state;
	private SurfaceHandler _surface;


/**
 * Initializes a new timer with a set interval.
 * @param interval Time interval in miliseconds.
 */
public Timer(long interval)
{
	_start = 0;
	_interval = interval;
	_running = false;
	_state = null;
	_surface = null;
}

/**
 * Starts the timer running and counting time.
 */
public void start()
{
	_start = SDL_GetTicks();
	_running = true;
}

/**
 * Stops the timer from running.
 */
public void stop()
{
	_start = 0;
	_running = false;
}

/**
 * Returns the time passed since the last interval.
 * @return Time in miliseconds.
 */
public final long getTime()
{
	if (_running)
	{
		return SDL_GetTicks() - _start;
	}
	return 0;
}

/**
 * Returns if the timer has been started.
 * @return Running state.
 */
public final boolean isRunning()
{
	return _running;
}

/**
 * The timer keeps calculating the passed time while it's running,
 * calling the respective action handler whenever the set interval passes.
 * @param state State that the action handler belongs to.
 * @param surface Surface that the action handler belongs to.
 */
public void think(State state, Surface surface)
{
	if (_running)
	{
		if (getTime() >= _interval)
		{
			if (state != null && _state != null)
			{
				(state._state)();
			}
			if (surface != null && _surface != null)
			{
				(surface._surface)();
			}			
			_start = SDL_GetTicks();
		}
	}
}

/**
 * Changes the timer's interval to a new value.
 * @param interval Interval in miliseconds.
 */
public void setInterval(long interval)
{
	_interval = interval;
}

/**
 * Sets a state function for the timer to call every interval.
 * @param handler Event handler.
 */
public void onTimer(StateHandler handler)
{
	_state = handler;
}

/**
 * Sets a surface function for the timer to call every interval.
 * @param handler Event handler.
 */
public void onTimer(SurfaceHandler handler)
{
	_surface = handler;
}

}
