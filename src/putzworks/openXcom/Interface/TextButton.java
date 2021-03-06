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

import android.graphics.Rectangle;
import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.Font;
import putzworks.openXcom.Engine.InteractiveSurface;
import putzworks.openXcom.Engine.Sound;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Interface.Text.TextVAlign;
import putzworks.openXcom.SDL.SDL_Color;

public class TextButton extends InteractiveSurface
{
	private short _color;
	private Text _text;
	private TextButton _group;

	private Sound soundPress = null;

/**
 * Sets up a text button with the specified size and position.
 * The text is centered on the button.
 * @param width Width in pixels.
 * @param height Height in pixels.
 * @param x X position in pixels.
 * @param y Y position in pixels.
 */
public TextButton(int width, int height, int x, int y) 
{
	super(width, height, x, y);
	_color = 0; 
	_group = null;

	_validButton = SDL_BUTTON_LEFT;

	_text = new Text(width, height, 0, 0);
	_text.setSmall();
	_text.setAlign(TextHAlign.ALIGN_CENTER);
	_text.setVerticalAlign(TextVAlign.ALIGN_MIDDLE);
}

/**
 * Changes the color for the button and text.
 * @param (short)i Color value.
 */
public void setColor(int i)
{
	_color = (short)i;
	_text.setColor((short)(_color - 3));
	draw();
}

/**
 * Returns the color for the button and text.
 * @return Color value.
 */
public final short getColor()
{
	return _color;
}

/**
 * Changes the various fonts for the text label to use.
 * The different fonts need to be passed in advance since the
 * text size can change mid-text.
 * @param big Pointer to large-size font.
 * @param small Pointer to small-size font.
 */
public void setFonts(Font big, Font small)
{
	_text.setFonts(big, small);
}

/**
 * Changes the text of the button label.
 * @param text Text string.
 */
public void setText(final String text)
{
	_text.setText(text);
	draw();
}

/**
 * Returns the text of the button label.
 * @return Text string.
 */
public final String getText()
{
	return _text.getText();
}

/**
 * Changes the button group this button belongs to.
 * @param group Pointer to the pressed button pointer in the group.
 * Null makes it a regular button.
 */
public void setGroup(TextButton group)
{
	_group = group;
	draw();
}

/**
 * Replaces a certain amount of colors in the surface's palette.
 * @param colors Pointer to the set of colors.
 * @param firstcolor Offset of the first color to replace.
 * @param ncolors Amount of colors to replace.
 */
public void setPalette(SDL_Color colors, int firstcolor, int ncolors)
{
	super.setPalette(colors, firstcolor, ncolors);
	_text.setPalette(colors, firstcolor, ncolors);
}

/**
 * Draws the labelled button.
 * The colors are inverted if the button is pressed.
 */
public void draw()
{
	Rectangle square = new Rectangle();
	int color = _color - 2;

	square.x = 0;
	square.y = 0;
	square.right = square.x + getWidth();
	square.bottom = square.y + getHeight();

	for (int i = 0; i < 5; i++)
	{
		drawRectangle(square, color);
		
	
		int tempWidth = square.right - square.x;
		int tempHeight = square.bottom - square.y;
		if (i % 2 == 0)
		{
			square.x++;
			square.y++;
		}
		square.right = square.x + tempWidth - 1;
		square.bottom = square.y + tempHeight - 1;

		switch (i)
		{
		case 0:
			color = _color + 2;
			setPixel(square.right, 0, color);
			break;
		case 1:
			color = _color - 1;
			break;
		case 2:
			color = _color + 1;
			setPixel(square.right+1, 1, color);
			break;
		case 3:
			color = _color;
			break;
		}
	}
	
	boolean press;
	if (_group == null)
		press = _isPressed;
	else
		press = (_group == this);

	if (press)
	{
		this.invert(_color);
	}
	_text.setInvert(press);

	_text.draw();
	_text.blit(this);
}

/**
 * Sets the button as the pressed button if it's part of a group.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mousePress(Action action, State state)
{
	if (soundPress != null)
		soundPress.play();

	if (_group != null)
	{
		TextButton old = _group;
		_group = this;
		old.draw();
	}

	super.mousePress(action, state);
	draw();
}

/**
 * Sets the button as the released button.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseRelease(Action action, State state)
{
	super.mouseRelease(action, state);
	draw();
}

}
