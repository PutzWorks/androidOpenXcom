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

import java.util.Vector;

import putzworks.openXcom.Engine.Font;
import putzworks.openXcom.Engine.Surface;

public class Text extends Surface
{
	private Font _big, _small, _font;
	private String _text, _wrappedText; //was String
	private Vector<Integer> _lineWidth, _lineHeight;
	private boolean _wrap, _invert, _contrast;
	private TextHAlign _align;
	private TextVAlign _valign;
	private short _color, _color2;

	public enum TextHAlign { ALIGN_LEFT, ALIGN_CENTER, ALIGN_RIGHT };
	public enum TextVAlign { ALIGN_TOP, ALIGN_MIDDLE, ALIGN_BOTTOM };


/**
 * Sets up a blank text with the specified size and position.
 * @param width Width in pixels.
 * @param height Height in pixels.
 * @param x X position in pixels.
 * @param y Y position in pixels.
 */
public Text(int width, int height, int x, int y) 
{
	super(width, height, x, y);
	_big = null;
	_small = null;
	_font = null;
	_text = "";
	_wrap = false;
	_invert = false;
	_contrast = false;
	_align = TextHAlign.ALIGN_LEFT;
	_valign = TextVAlign.ALIGN_TOP;
	_color = 0;
	_color2 = 0;

}

/**
 * Takes an integer value and formats it as currency,
 * spacing the thousands and adding a $ sign to the front.
 * @param funds The funding value.
 * @return The formatted string.
 */
public static String formatFunding(int funds)
{
	StringBuffer ss = new StringBuffer();
	ss.append(funds);
	int spacer = ss.length() - 3;
	while (spacer > 0 && spacer < ss.length())
	{
		ss.insert(spacer, " ");
		spacer -= 3;
	}
	ss.insert(0, "$");
	return ss.toString();
}

/**
 * Changes the text to use the big-size font.
 */
public void setBig()
{
	_font = _big;
	processText();
}

/**
 * Changes the text to use the small-size font.
 */
public void setSmall()
{
	_font = _small;
	processText();
}

/**
 * Returns the font currently used by the text.
 * @return Pointer to font.
 */
public final Font getFont()
{
	return _font;
}

/**
 * Changes the various fonts for the text to use.
 * The different fonts need to be passed in advance since the
 * text size can change mid-text.
 * @param big Pointer to large-size font.
 * @param small Pointer to small-size font.
 */
public void setFonts(Font big, Font small)
{
	_big = big;
	_small = small;
	_font = _small;
}

/**
 * Changes the string displayed on screen.
 * @param text Text string.
 */
public void setText(final String text)
{
	_text = text;
	processText();
}

/**
 * Returns the string displayed on screen.
 * @return Text string.
 */
public final String getText()
{
	return _text;
}

/**
 * Enables/disables text wordwrapping. When enabled, lines of
 * text are automatically split to ensure they stay within the
 * drawing area, otherwise they simply go off the edge.
 * @param wrap Wordwrapping setting.
 */
public void setWordWrap(boolean wrap)
{
	if (wrap != _wrap)
	{
		_wrap = wrap;
		processText();
	}
}

/**
 * Enables/disables color inverting. Mostly used to make
 * button text look pressed along with the button.
 * @param invert Invert setting.
 */
public void setInvert(boolean invert)
{
	_invert = invert;
	draw();
}

/**
 * Enables/disables high contrast color. Mostly used for
 * Battlescape text.
 * @param contrast High contrast setting.
 */
public void setHighContrast(boolean contrast)
{
	_contrast = contrast;
	draw();
}

/**
 * Changes the way the text is aligned horizontally
 * relative to the drawing area.
 * @param align Horizontal alignment.
 */
public void setAlign(TextHAlign align)
{
	_align = align;
	draw();
}

/**
 * Changes the way the text is aligned vertically
 * relative to the drawing area.
 * @param valign Vertical alignment.
 */
public void setVerticalAlign(TextVAlign valign)
{
	_valign = valign;
	draw();
}

/**
 * Changes the color used to render the text. Unlike regular graphics,
 * fonts are greyscale so they need to be assigned a specific position
 * in the palette to be displayed.
 * @param (short)i Color value.
 */
public void setColor(int i)
{
	_color = (short)i;
	draw();
}

/**
 * Returns the color used to render the text.
 * @return Color value.
 */
public final short getColor()
{
	return _color;
}

/**
 * Changes the secondary color used to render the text. The text
 * switches between the primary and secondary color whenever there's
 * a \x01 in the string.
 * @param color Color value.
 */
public void setSecondaryColor(short color)
{
	_color2 = color;
	draw();
}

public void setSecondaryColor(int color)
{
	_color2 = (short)color;
	draw();
}

/**
 * Returns the secondary color used to render the text.
 * @return Color value.
 */
public final short getSecondaryColor()
{
	return _color2;
}

/**
 * Returns the rendered text's height. Useful to check if wordwrap applies.
 * @return Height in pixels.
 */
public final int getTextHeight()
{
	int height = 0;
	for (int i: _lineHeight)
	{
		height += i;
	}
	return height;
}

/**
  * Returns the rendered text's width.
  * @return Width in pixels.
  */
public final int getTextWidth()
{
	int width = 0;
	for (int i: _lineWidth)
	{
		if (i > width)
		{
			width = i;
		}
	}
	return width;
}

/**
 * Takes care of any text post-processing like calculating
 * line metrics for alignment and wordwrapping if necessary.
 */
private void processText()
{
	if (_font == null)
	{
		return;
	}

	String s = _text;

	// Use a separate string for wordwrapping text
	if (_wrap)
	{
		_wrappedText = _text;
		s = _wrappedText;
	}

	_lineWidth.clear();
	_lineHeight.clear();

	int width = 0, word = 0;
	String.iterator space = s.begin();
	Font font = _font;

	// Go through the text character by character
	for (String.iterator c = s.begin(); c <= s.end(); ++c)
	{
		// End of the line
		if (c == s.end() || c == L'\n' || c == 2)
		{
			// Add line measurements for alignment later
			_lineWidth.add(width);
			_lineHeight.add(font.getHeight() + font.getSpacing());
			width = 0;
			word = 0;

			if (c == s.end())
				break;
			// \x02 marks start of small text
			else if (c == 2)
				font = _small;
		}
		// Keep track of spaces for wordwrapping
		else if (c == L' ')
		{
			space = c;
			width += font.getWidth() / 2;
			word = 0;
		}
		// Keep track of the width of the last line and word
		else if (c != 1)
		{
			width += font.getChar(c).getCrop().w + font.getSpacing();
			word += font.getChar(c).getCrop().w + font.getSpacing();

			// Wordwrap if the last word doesn't fit the line
			if (_wrap && width > getWidth())
			{
				// Go back to the last space and put a linebreak there
				space = L'\n';
				c = space - 1;
				width -= word + font.getWidth() / 2;
			}
		}
	}

	draw();
}

/**
 * Draws all the characters in the text with a really
 * nasty complex gritty text rendering algorithm logic stuff.
 */
public void draw()
{
	clear();

	if (_text.equals("") || _font == null)
	{
		return;
	}

	int x = 0, y = 0, line = 0, height = 0;
	Font font = _font;
	short color = _color;
	String s = _text;

	for (int i: _lineHeight)
	{
		height += i;
	}

	switch (_valign)
	{
	case ALIGN_TOP:
		y = 0;
		break;
	case ALIGN_MIDDLE:
		y = (getHeight() - height) / 2;
		break;
	case ALIGN_BOTTOM:
		y = getHeight() - height;
		break;
	}

	switch (_align)
	{
	case ALIGN_LEFT:
		x = 0;
		break;
	case ALIGN_CENTER:
		x = (getWidth() - _lineWidth.get(line)) / 2;
		break;
	case ALIGN_RIGHT:
		x = getWidth() - _lineWidth.get(line);
		break;
	}

	if (_wrap)
	{
		s = _wrappedText;
	}

	// Draw each letter one by one
	for (String.iterator c = s.begin(); c != s.end(); ++c)
	{
		if (c == ' ')
		{
			x += font.getWidth() / 2;
		}
		else if (c == '\n' || c == 2)
		{
			line++;
			y += font.getHeight() + font.getSpacing();
			switch (_align)
			{
			case ALIGN_LEFT:
				x = 0;
				break;
			case ALIGN_CENTER:
				x = (getWidth() - _lineWidth.get(line)) / 2;
				break;
			case ALIGN_RIGHT:
				x = getWidth() - _lineWidth.get(line);
				break;
			}
			if (c == 2)
			{
				font = _small;
			}
		}
		else if (c == 1)
		{
			color = (color == _color ? _color2 : _color);
		}
		else
		{
			Surface chr = font.getChar(c);
			Surface letter = Surface(chr.getCrop().w, chr.getCrop().h, x, y);
			letter.setPalette(getPalette());
			chr.blit(letter);
			letter.offset(color);
			letter.blit(this);
			x += chr.getCrop().w + font.getSpacing();
		}
	}
	if (_contrast)
	{
		this.multiply(3);
	}
	if (_invert)
	{
		this.invert(_color + 3);
	}
}

}
