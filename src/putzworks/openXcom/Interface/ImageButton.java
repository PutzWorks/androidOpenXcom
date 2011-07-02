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
package putzworks.openXcom.Interface;

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.InteractiveSurface;
import putzworks.openXcom.Engine.State;

public class ImageButton extends InteractiveSurface
{
	protected short _color;
	private ImageButton _group;

/**
 * Sets up an image button with the specified size and position.
 * @param width Width in pixels.
 * @param height Height in pixels.
 * @param x X position in pixels.
 * @param y Y position in pixels.
 */
public ImageButton(int width, int height, int x, int y) 
{
	super(width, height, x, y);
	_color = 0;
	_group = null;

	_validButton = SDL_BUTTON_LEFT;
}

/**
 * Changes the color for the image button.
 * @param color Color value.
 */
public void setColor(short color)
{
	_color = color;
}

/**
 * Returns the color for the image button.
 * @return Color value.
 */
public final short getColor()
{
	return _color;
}

/**
 * Changes the button group this image button belongs to.
 * @param group Pointer to the pressed button pointer in the group.
 * Null makes it a regular button.
 */
public void setGroup(ImageButton group)
{
	_group = group;
	if (_group != null && _group == this)
		invert(_color);
}

/**
 * Sets the button as the pressed button if it's part of a group,
 * and inverts the colors when pressed.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mousePress(Action action, State state)
{
	if (_group != null)
	{
		(_group).invert((_group).getColor());
		_group = this;
	}
	invert(_color);
	super.mousePress(action, state);
}

/*
 * Sets the button as the released button if it's part of a group.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseRelease(Action action, State state)
{
	if (_group == null)
		invert(_color);
	super.mouseRelease(action, state);
}

}
