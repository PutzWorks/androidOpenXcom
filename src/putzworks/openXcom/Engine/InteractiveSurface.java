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



public class InteractiveSurface extends Surface
{
	private ActionHandler _click, _press, _release, _in, _over, _out, _keyPress, _keyRelease;
	protected boolean _isPressed;
	private boolean _isHovered, _isFocused;
	protected int _validButton;


/**
 * Sets up a blank interactive surface with the specified size and position.
 * @param width Width in pixels.
 * @param height Height in pixels.
 * @param x X position in pixels.
 * @param y Y position in pixels.
 */
	
public InteractiveSurface(int width, int height, int x, int y)
{
	super(width, height, x, y);
	_click =  null;
	_press =  null;
	_release =  null;
	_in =  null;
	_over =  null;
	_out =  null;
	_keyPress =  null;
	_keyRelease =  null;
	_isPressed = false;
	_isHovered = false;
	_isFocused = false;
	_validButton = 0;
}

/**
 * Changes the visibility of the surface. A hidden surface
 * isn't blitted nor receives events.
 * @param visible New visibility.
 */
public void setVisible(boolean visible)
{
	super.setVisible(visible);
	// Unpress button if it was hidden
	if (!_visible)
	{
		unpress(null);
	}
}

/**
 * Called whenever an action occurs, and processes it to
 * check if it's relevant to the surface and convert it
 * into a meaningful interaction like a "click", calling
 * the respective handlers.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void handle(Action action, State state)
{
	if (!_visible || _hidden)
		return;

	action.setSender(this);

	if (action.getDetails().type == SDL_MOUSEBUTTONUP || action.getDetails().type == SDL_MOUSEBUTTONDOWN)
	{
		action.setXMouse(action.getDetails().button.x);
		action.setYMouse(action.getDetails().button.y);
	}
	else if (action.getDetails().type == SDL_MOUSEMOTION)
	{
		action.setXMouse(action.getDetails().motion.x);
		action.setYMouse(action.getDetails().motion.y);
	}

	if (action.getXMouse() != -1 && action.getYMouse() != -1)
	{
		if ((action.getXMouse() >= getX() * action.getXScale() && action.getXMouse() < (getX() + getWidth()) * action.getXScale()) &&
			(action.getYMouse() >= getY() * action.getYScale() && action.getYMouse() < (getY() + getHeight()) * action.getYScale()))
		{
			if (!_isHovered)
			{
				_isHovered = true;
				mouseIn(action, state);
			}
			mouseOver(action, state);
		}
		else
		{
			if (_isHovered)
			{
				_isHovered = false;
				mouseOut(action, state);
			}
		}
	}

	if (!_isPressed && action.getDetails().type == SDL_MOUSEBUTTONDOWN && (_validButton == 0 || _validButton == action.getDetails().button.button))
	{		
		if (_isHovered)
		{
			_isPressed = true;
			mousePress(action, state);
		}
	}
	else if (_isPressed && action.getDetails().type == SDL_MOUSEBUTTONUP && (_validButton == 0 || _validButton == action.getDetails().button.button))
	{
		_isPressed = false;
		mouseRelease(action, state);
		if (_isHovered)
		{
			mouseClick(action, state);
		}
	}

	if (_isFocused)
	{
		if (action.getDetails().type == SDL_KEYDOWN)
		{
			keyboardPress(action, state);
		}
		else if (action.getDetails().type == SDL_KEYUP)
		{
			keyboardRelease(action, state);
		}
	}
}

/**
 * Marks ths surface as focused. Surfaces will only receive
 * keyboard events if focused.
 */
public void focus()
{
	_isFocused = true;
}

/**
 * Simulates a "mouse button release". Used in circumstances
 * where the surface is unpressed without user input.
 * @param state Pointer to running state.
 */
public void unpress(State state)
{
	if (_isPressed)
	{
		_isPressed = false;
		SDL_Event ev;
		ev.type = SDL_MOUSEBUTTONUP;
		ev.button.button = SDL_BUTTON_LEFT;
		Action a = Action(ev, 0.0, 0.0);
		mouseRelease(a, state);
	}
}

/**
 * Called everytime there's a mouse press over the surface.
 * Allows the surface to have custom functionality for this action,
 * and can be called externally to simulate the action.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mousePress(Action action, State state)
{
	if (_press !=  null)
	{
		_press.handle(action);
	}
}

/**
 * Called everytime there's a mouse release over the surface.
 * Allows the surface to have custom functionality for this action,
 * and can be called externally to simulate the action.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseRelease(Action action, State state)
{
	if (_release != null)
	{
		_release.handle(action);
	}
}

/**
 * Called everytime there's a mouse click on the surface.
 * Allows the surface to have custom functionality for this action,
 * and can be called externally to simulate the action.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseClick(Action action, State state)
{
	if (_click != null)
	{
		_click.handle(action);
	}
}

/**
 * Called everytime the mouse moves into the surface.
 * Allows the surface to have custom functionality for this action,
 * and can be called externally to simulate the action.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseIn(Action action, State state)
{
	if (_in != null)
	{
		_in.handle(action);
	}
}

/**
 * Called everytime the mouse moves over the surface.
 * Allows the surface to have custom functionality for this action,
 * and can be called externally to simulate the action.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseOver(Action action, State state)
{
	if (_over != null)
	{
		_over.handle(action);
	}
}

/**
 * Called everytime the mouse moves out of the surface.
 * Allows the surface to have custom functionality for this action,
 * and can be called externally to simulate the action.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseOut(Action action, State state)
{
	if (_out != null)
	{
		_out.handle(action);
	}
}

/**
 * Called everytime there's a keyboard press when the surface is focused.
 * Allows the surface to have custom functionality for this action,
 * and can be called externally to simulate the action.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void keyboardPress(Action action, State state)
{
	if (_keyPress != null)
	{
		_keyPress.handle(action);
	}
}

/**
 * Called everytime there's a keyboard release over the surface.
 * Allows the surface to have custom functionality for this action,
 * and can be called externally to simulate the action.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void keyboardRelease(Action action, State state)
{
	if (_keyRelease != null)
	{
		_keyRelease.handle(action);
	}
}

/**
 * Sets a function to be called everytime the surface is mouse clicked.
 * @param handler Action handler.
 */
public void onMouseClick(ActionHandler handler)
{
	_click = handler;
}

/**
 * Sets a function to be called everytime the surface is mouse pressed.
 * @param handler Action handler.
 */
public void onMousePress(ActionHandler handler)
{
	_press = handler;
}

/**
 * Sets a function to be called everytime the surface is mouse released.
 * @param handler Action handler.
 */
public void onMouseRelease(ActionHandler handler)
{
	_release = handler;
}

/**
 * Sets a function to be called everytime the mouse moves into the surface.
 * @param handler Action handler.
 */
public void onMouseIn(ActionHandler handler)
{
	_in = handler;
}

/**
 * Sets a function to be called everytime the mouse moves over the surface.
 * @param handler Action handler.
 */
public void onMouseOver(ActionHandler handler)
{
	_over = handler;
}

/**
 * Sets a function to be called everytime the mouse moves out of the surface.
 * @param handler Action handler.
 */
public void onMouseOut(ActionHandler handler)
{
	_out = handler;
}

/**
 * Sets a function to be called everytime a key is pressed when the surface is focused.
 * @param handler Action handler.
 */
public void onKeyboardPress(ActionHandler handler)
{
	_keyPress = handler;
}

/**
 * Sets a function to be called everytime a key is released when the surface is focused.
 * @param handler Action handler.
 */
public void onKeyboardRelease(ActionHandler handler)
{
	_keyRelease = handler;
}

}
