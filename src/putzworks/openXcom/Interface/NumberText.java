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

import putzworks.openXcom.Engine.Surface;

public class NumberText extends Surface
{
	private int _value;
	private Surface[] _chars = new Surface[10];
	private short _color;

/**
 * Sets up a blank number text.
 * @param width Width in pixels.
 * @param height Height in pixels.
 * @param x X position in pixels.
 * @param y Y position in pixels.
 */
public NumberText(int width, int height, int x, int y) 
{
	super(width, height, x, y);
	_value = 0;
	_color = 0;
	_chars[0] = new Surface(3, 5);
	_chars[0].lock();
	_chars[0].setPixel(0, 0, (short)1);
	_chars[0].setPixel(1, 0, (short)1);
	_chars[0].setPixel(2, 0, (short)1);
	_chars[0].setPixel(0, 1, (short)1);
	_chars[0].setPixel(0, 2, (short)1);
	_chars[0].setPixel(0, 3, (short)1);
	_chars[0].setPixel(2, 1, (short)1);
	_chars[0].setPixel(2, 2, (short)1);
	_chars[0].setPixel(2, 3, (short)1);
	_chars[0].setPixel(0, 4, (short)1);
	_chars[0].setPixel(1, 4, (short)1);
	_chars[0].setPixel(2, 4, (short)1);
	_chars[0].unlock();

	_chars[1] = new Surface(3, 5);
	_chars[1].lock();
	_chars[1].setPixel(1, 0, (short)1);
	_chars[1].setPixel(1, 1, (short)1);
	_chars[1].setPixel(1, 2, (short)1);
	_chars[1].setPixel(1, 3, (short)1);
	_chars[1].setPixel(0, 4, (short)1);
	_chars[1].setPixel(1, 4, (short)1);
	_chars[1].setPixel(2, 4, (short)1);
	_chars[1].setPixel(0, 1, (short)1);
	_chars[1].unlock();

	_chars[2] = new Surface(3, 5);
	_chars[2].lock();
	_chars[2].setPixel(0, 0, (short)1);
	_chars[2].setPixel(1, 0, (short)1);
	_chars[2].setPixel(2, 0, (short)1);
	_chars[2].setPixel(2, 1, (short)1);
	_chars[2].setPixel(0, 2, (short)1);
	_chars[2].setPixel(1, 2, (short)1);
	_chars[2].setPixel(2, 2, (short)1);
	_chars[2].setPixel(0, 3, (short)1);
	_chars[2].setPixel(0, 4, (short)1);
	_chars[2].setPixel(1, 4, (short)1);
	_chars[2].setPixel(2, 4, (short)1);
	_chars[2].unlock();

	_chars[3] = new Surface(3, 5);
	_chars[3].lock();
	_chars[3].setPixel(0, 0, (short)1);
	_chars[3].setPixel(1, 0, (short)1);
	_chars[3].setPixel(2, 0, (short)1);
	_chars[3].setPixel(2, 1, (short)1);
	_chars[3].setPixel(2, 2, (short)1);
	_chars[3].setPixel(2, 3, (short)1);
	_chars[3].setPixel(0, 2, (short)1);
	_chars[3].setPixel(1, 2, (short)1);
	_chars[3].setPixel(0, 4, (short)1);
	_chars[3].setPixel(1, 4, (short)1);
	_chars[3].setPixel(2, 4, (short)1);
	_chars[3].unlock();

	_chars[4] = new Surface(3, 5);
	_chars[4].lock();
	_chars[4].setPixel(0, 0, (short)1);
	_chars[4].setPixel(0, 1, (short)1);
	_chars[4].setPixel(0, 2, (short)1);
	_chars[4].setPixel(1, 2, (short)1);
	_chars[4].setPixel(2, 0, (short)1);
	_chars[4].setPixel(2, 1, (short)1);
	_chars[4].setPixel(2, 2, (short)1);
	_chars[4].setPixel(2, 3, (short)1);
	_chars[4].setPixel(2, 4, (short)1);
	_chars[4].unlock();

	_chars[5] = new Surface(3, 5);
	_chars[5].lock();
	_chars[5].setPixel(0, 0, (short)1);
	_chars[5].setPixel(1, 0, (short)1);
	_chars[5].setPixel(2, 0, (short)1);
	_chars[5].setPixel(0, 1, (short)1);
	_chars[5].setPixel(0, 2, (short)1);
	_chars[5].setPixel(1, 2, (short)1);
	_chars[5].setPixel(2, 2, (short)1);
	_chars[5].setPixel(2, 3, (short)1);
	_chars[5].setPixel(0, 4, (short)1);
	_chars[5].setPixel(1, 4, (short)1);
	_chars[5].setPixel(2, 4, (short)1);
	_chars[5].unlock();

	_chars[6] = new Surface(3, 5);
	_chars[6].lock();
	_chars[6].setPixel(0, 0, (short)1);
	_chars[6].setPixel(1, 0, (short)1);
	_chars[6].setPixel(2, 0, (short)1);
	_chars[6].setPixel(0, 1, (short)1);
	_chars[6].setPixel(0, 2, (short)1);
	_chars[6].setPixel(1, 2, (short)1);
	_chars[6].setPixel(2, 2, (short)1);
	_chars[6].setPixel(0, 3, (short)1);
	_chars[6].setPixel(2, 3, (short)1);
	_chars[6].setPixel(0, 4, (short)1);
	_chars[6].setPixel(1, 4, (short)1);
	_chars[6].setPixel(2, 4, (short)1);
	_chars[6].unlock();

	_chars[7] = new Surface(3, 5);
	_chars[7].lock();
	_chars[7].setPixel(0, 0, (short)1);
	_chars[7].setPixel(1, 0, (short)1);
	_chars[7].setPixel(2, 0, (short)1);
	_chars[7].setPixel(2, 1, (short)1);
	_chars[7].setPixel(2, 2, (short)1);
	_chars[7].setPixel(2, 3, (short)1);
	_chars[7].setPixel(2, 4, (short)1);
	_chars[7].unlock();

	_chars[8] = new Surface(3, 5);
	_chars[8].lock();
	_chars[8].setPixel(0, 0, (short)1);
	_chars[8].setPixel(1, 0, (short)1);
	_chars[8].setPixel(2, 0, (short)1);
	_chars[8].setPixel(0, 1, (short)1);
	_chars[8].setPixel(0, 2, (short)1);
	_chars[8].setPixel(0, 3, (short)1);
	_chars[8].setPixel(2, 1, (short)1);
	_chars[8].setPixel(2, 2, (short)1);
	_chars[8].setPixel(2, 3, (short)1);
	_chars[8].setPixel(1, 2, (short)1);
	_chars[8].setPixel(0, 4, (short)1);
	_chars[8].setPixel(1, 4, (short)1);
	_chars[8].setPixel(2, 4, (short)1);
	_chars[8].unlock();

	_chars[9] = new Surface(3, 5);
	_chars[9].lock();
	_chars[9].setPixel(0, 0, (short)1);
	_chars[9].setPixel(1, 0, (short)1);
	_chars[9].setPixel(2, 0, (short)1);
	_chars[9].setPixel(0, 1, (short)1);
	_chars[9].setPixel(0, 2, (short)1);
	_chars[9].setPixel(2, 1, (short)1);
	_chars[9].setPixel(2, 2, (short)1);
	_chars[9].setPixel(2, 3, (short)1);
	_chars[9].setPixel(1, 2, (short)1);
	_chars[9].setPixel(0, 4, (short)1);
	_chars[9].setPixel(1, 4, (short)1);
	_chars[9].setPixel(2, 4, (short)1);
	_chars[9].unlock();
}

/**
 *
 */
public void clearNumberText()
{
	for (int i = 0; i < 10; i++)
	{
		_chars[i] = null;
	}
}

/**
 * Changes the value used to render the number.
 * @param value Number value.
 */
public void setValue(int value)
{
	_value = value;
	draw();
}

/**
 * Returns the value used to render the number.
 * @return Number value.
 */
public final int getValue()
{
	return _value;
}

/**
 * Changes the color used to render the number.
 * @param color Color value.
 */
public void setColor(short color)
{
	_color = color;
	draw();
}

/**
 * Returns the color used to render the number.
 * @return Color value.
 */
public final short getColor()
{
	return _color;
}

/**
 * Replaces a certain amount of colors in the number text palette.
 * @param colors Pointer to the set of colors.
 * @param firstcolor Offset of the first color to replace.
 * @param ncolors Amount of colors to replace.
 */
public void setPalette(SDL_Color colors, int firstcolor, int ncolors)
{
	super.setPalette(colors, firstcolor, ncolors);
	for (int i = 0; i < 10; i++)
	{
		_chars[i].setPalette(colors, firstcolor, ncolors);
	}
}

/**
 * Draws all the digits in the number.
 */
public void draw()
{
	clear();

	Stringstream ss;
	ss << _value;
	String s = ss.str();
	int x = 0;
	for (String.iterator i = s.begin(); i != s.end(); ++i)
	{
		_chars[*i - '0'].setX(x);
		_chars[*i - '0'].setY(0);
		_chars[*i - '0'].blit(this);
		x += _chars[*i - '0'].getWidth() + 1;
	}

	this.offset(_color);
}

}
