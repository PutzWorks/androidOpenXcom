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

public class Bar extends Surface
{
	private short _color;
	private double _scale, _max, _value;
	private boolean _invert;

/**
 * Sets up a blank bar with the specified size and position.
 * @param width Width in pixels.
 * @param height Height in pixels.
 * @param x X position in pixels.
 * @param y Y position in pixels.
 */
public Bar(int width, int height, int x, int y) 
{	
	super(width, height, x, y);
	_color = 0;
	_scale = 0;
	_max = 0;
	_value = 0;
	_invert = false;

}

/**
 * Changes the color used to draw the border and contents.
 * @param color Color value.
 */
public void setColor(short color)
{
	_color = color;
	draw();
}

/**
 * Returns the color used to draw the bar.
 * @return Color value.
 */
public final short getColor()
{
	return _color;
}

/**
 * Changes the scale factor used to draw the bar values.
 * @param scale Scale in pixels/unit.
 */
public void setScale(double scale)
{
	_scale = scale;
	draw();
}

/**
 * Returns the scale factor used to draw the bar values.
 * @return Scale in pixels/unit.
 */
public final double getScale()
{
	return _scale;
}

/**
 * Changes the maximum value used to draw the outer border.
 * @param max Maximum value.
 */
public void setMax(double max)
{
	_max = max;
	draw();
}

/**
 * Returns the maximum value used to draw the outer border.
 * @return Maximum value.
 */
public final double getMax()
{
	return _max;
}

/**
 * Changes the value used to draw the inner contents.
 * @param value Current value.
 */
public void setValue(double value)
{
	_value = value;
	draw();
}

/**
 * Returns the value used to draw the inner contents.
 * @return Current value.
 */
public final double getValue()
{
	return _value;
}

/**
 * Enables/disables color inverting. Some bars have
 * darker borders and others have lighter borders.
 * @param invert Invert setting.
 */
public void setInvert(boolean invert)
{
	_invert = invert;
	draw();
}

/**
 * Draws the bordered bar filled according
 * to its values.
 */
public void draw()
{
	clear();

	Rect square;

	square.x = 0;
	square.y = 0;
	square.w = (short)(_scale * _max) + 1;
	square.h = getHeight();

	if (_invert)
	{
        drawRect(square, _color); 
	}
	else
	{
        drawRect(square, _color + 4);
	}

	square.y++;
	square.w--;
	square.h -= 2;

	drawRect(square, 0);
	
	square.w = (short)(_scale * _value);

	if (_invert)
    {
		drawRect(square, _color + 4);
    }
	else
    {
		drawRect(square, _color);
    }
}

}
