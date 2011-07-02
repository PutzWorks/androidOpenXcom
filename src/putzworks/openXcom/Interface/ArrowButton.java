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
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Engine.Timer;

public class ArrowButton extends ImageButton
{
	private ArrowShape _shape;
	private TextList _list;
	private Timer _timer;

	public enum ArrowShape { ARROW_BIG_UP, ARROW_BIG_DOWN, ARROW_SMALL_UP, ARROW_SMALL_DOWN, ARROW_SMALL_LEFT, ARROW_SMALL_RIGHT };


/**
 * Sets up an arrow button with the specified size and position.
 * @param shape Shape of the arrow.
 * @param width Width in pixels.
 * @param height Height in pixels.
 * @param x X position in pixels.
 * @param y Y position in pixels.
 */
public ArrowButton(ArrowShape shape, int width, int height, int x, int y)
{
	super(width, height, x, y);
	_shape = shape; 
	_list = null;

	_validButton = SDL_BUTTON_LEFT;

	_timer = new Timer(50);
	_timer.onTimer((SurfaceHandler)ArrowButton.scroll);
}

/**
 * Deletes timers.
 */
public void clearArrowButton()
{
	_timer = null;
}

/**
 * Changes the color for the arrow button.
 * @param color Color value.
 */
public void setColor(short color)
{
	super.setColor(color);
	draw();
}

/**
 * Changes the text associated with the arrow button.
 * This makes the button scroll that list.
 * @param list Pointer to text list.
 */
public void setTextList(TextList list)
{
	_list = list;
}

/**
 * Draws the button with the specified arrow shape.
 */
public void draw()
{
	lock();

	// Draw button
	Rect square;
	int color = _color - 1;

	square.left = 0;
	square.top = 0;
	square.right = square.left + getWidth() - 1;
	square.bottom = square.top + getHeight() - 1;

	drawRect(square, color);

	square.x++;
	square.y++;
	color = _color + 2;

	drawRect(square, color);

	square.w--;
	square.h--;
	color = _color + 1;

	drawRect(square, color);

	setPixel(0, 0, (short)(_color - 2));
	setPixel(0, getHeight() - 1, (short)(_color + 1));
	setPixel(getWidth() - 1, 0, (short)(_color + 1));

	color = _color - 2;

	if (_shape == ArrowShape.ARROW_BIG_UP)
	{
		// Draw arrow square
		square.x = 5;
		square.y = 8;
		square.w = 3;
		square.h = 3;

		drawRect(square, color);

		// Draw arrow triangle
		square.x = 2;
		square.y = 7;
		square.w = 9;
		square.h = 1;

		for (; square.w > 1; square.w -= 2)
		{
			drawRect(square, color);
			square.x++;
			square.y--;
		}
		drawRect(square, color);
	}
	else if (_shape == ArrowShape.ARROW_BIG_DOWN)
	{
		// Draw arrow square
		square.x = 5;
		square.y = 3;
		square.w = 3;
		square.h = 3;

		drawRect(square, color);

		// Draw arrow triangle
		square.x = 2;
		square.y = 6;
		square.w = 9;
		square.h = 1;

		for (; square.w > 1; square.w -= 2)
		{
			drawRect(square, color);
			square.x++;
			square.y++;
		}
		drawRect(square, color);
	}
	else if (_shape == ArrowShape.ARROW_SMALL_UP)
	{
		// Draw arrow triangle 1
		square.x = 1;
		square.y = 5;
		square.w = 9;
		square.h = 1;

		for (; square.w > 1; square.w -= 2)
		{
			drawRect(square, color + 2);
			square.x++;
			square.y--;
		}
		drawRect(square, color + 2);

		// Draw arrow triangle 2
		square.x = 2;
		square.y = 5;
		square.w = 7;
		square.h = 1;

		for (; square.w > 1; square.w -= 2)
		{
			drawRect(square, color);
			square.x++;
			square.y--;
		}
		drawRect(square, color);
	}
	else if (_shape == ArrowShape.ARROW_SMALL_DOWN)
	{
		// Draw arrow triangle 1
		square.x = 1;
		square.y = 2;
		square.w = 9;
		square.h = 1;

		for (; square.w > 1; square.w -= 2)
		{
			drawRect(square, color + 2);
			square.x++;
			square.y++;
		}
		drawRect(square, color + 2);

		// Draw arrow triangle 2
		square.x = 2;
		square.y = 2;
		square.w = 7;
		square.h = 1;

		for (; square.w > 1; square.w -= 2)
		{
			drawRect(square, color);
			square.x++;
			square.y++;
		}
		drawRect(square, color);
	}
	else if (_shape == ArrowShape.ARROW_SMALL_LEFT)
	{
		// Draw arrow triangle 1
		square.x = 2;
		square.y = 4;
		square.w = 2;
		square.h = 1;

		for (; square.h < 5; square.h += 2)
		{
			drawRect(square, color + 2);
			square.x += 2;
			square.y--;
		}
		square.w = 1;
		drawRect(square, color + 2);

		// Draw arrow triangle 2
		square.x = 3;
		square.y = 4;
		square.w = 2;
		square.h = 1;

		for (; square.h < 5; square.h += 2)
		{
			drawRect(square, color);
			square.x += 2;
			square.y--;
		}
		square.w = 1;
		drawRect(square, color);
	}
	else if (_shape == ArrowShape.ARROW_SMALL_RIGHT)
	{
		// Draw arrow triangle 1
		square.x = 7;
		square.y = 4;
		square.w = 2;
		square.h = 1;

		for (; square.h < 5; square.h += 2)
		{
			drawRect(square, color + 2);
			square.x -= 2;
			square.y--;
		}
		square.x++;
		square.w = 1;
		drawRect(square, color + 2);

		// Draw arrow triangle 2
		square.x = 6;
		square.y = 4;
		square.w = 2;
		square.h = 1;

		for (; square.h < 5; square.h += 2)
		{
			drawRect(square, color);
			square.x -= 2;
			square.y--;
		}
		square.x++;
		square.w = 1;
		drawRect(square, color);
	}

	unlock();
}

/**
 * Keeps the scrolling timers running.
 */
public void think()
{
	_timer.think(null, this);
}

/**
 * Scrolls the list.
 */
public void scroll()
{
	if (_shape == ArrowShape.ARROW_BIG_UP)
	{
		_list.scrollUp();
	}
	else if (_shape == ArrowShape.ARROW_BIG_DOWN)
	{
		_list.scrollDown();
	}
}

/**
 * Starts scrolling the associated list.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mousePress(Action action, State state)
{
	super.mousePress(action, state);
	if (_list != null)
	{
		_timer.start();
	}
}

/*
 * Stops scrolling the associated list.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseRelease(Action action, State state)
{
	super.mouseRelease(action, state);
	if (_list != null)
	{
		_timer.stop();
	}
}

}
