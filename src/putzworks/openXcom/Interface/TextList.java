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

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Font;
import putzworks.openXcom.Engine.InteractiveSurface;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Engine.Surface;
import putzworks.openXcom.Interface.ArrowButton.ArrowShape;
import putzworks.openXcom.Interface.Text.TextHAlign;

public class TextList extends InteractiveSurface
{
	private Vector< Vector<Text> > _texts;
	private Vector<Integer> _columns;
	private Font _big, _small, _font;
	private int _scroll, _visibleRows;
	private short _color, _color2;
	private TextHAlign _align;
	private boolean _dot, _selectable, _condensed;
	private int _selRow;
	private Surface _bg, _selector;
	private ArrowButton _up, _down;
	private int _margin;
	private Vector<ArrowButton> _arrowLeft, _arrowRight;
	private int _arrowPos;
	private ArrowOrientation _arrowType;
	private ActionHandler _leftClick, _leftPress, _leftRelease, _rightClick, _rightPress, _rightRelease;

	public enum ArrowOrientation { ARROW_VERTICAL, ARROW_HORIZONTAL };


/**
 * Sets up a blank list with the specified size and position.
 * @param width Width in pixels.
 * @param height Height in pixels.
 * @param x X position in pixels.
 * @param y Y position in pixels.
 */
public TextList(int width, int height, int x, int y) 
{
	super(width, height, x, y);
	_texts =  new Vector< Vector<Text> >();
	_columns = new Vector<Integer>();
	_big = null;
	_small = null;
	_font = null;
	_scroll = 0;
	_visibleRows = 0;
	_color = 0;
	_align = TextHAlign.ALIGN_LEFT;
	_dot = false;
	_selectable = false;
	_condensed = false;
	_selRow = 0;
	_bg = null;
	_selector = null;
	_margin = 0;
	_arrowLeft = new Vector<ArrowButton>();
	_arrowRight = new Vector<ArrowButton>();
	_arrowPos = -1;
	_arrowType = ArrowOrientation.ARROW_VERTICAL;
	_leftClick = null;
	_leftPress = null;
	_leftRelease = null;
	_rightClick = null;
	_rightPress = null;
	_rightRelease = null;

	_up = new ArrowButton(ArrowShape.ARROW_BIG_UP, 13, 14, getX() + getWidth() + 4, getY() + 1);
	_up.setVisible(false);
	_up.setTextList(this);
	_down = new ArrowButton(ArrowShape.ARROW_BIG_DOWN, 13, 14, getX() + getWidth() + 4, getY() + getHeight() - 12);
	_down.setVisible(false);
	_down.setTextList(this);
}

/**
 * Deletes all the stuff contained in the list.
 */
public void clearTextList()
{
	_selector = null;
	_up = null;
	_down = null;
}

/**
 * Unpresses all the arrow buttons.
 * @param state Pointer to running state.
 */
public void unpress(State state)
{
	super.unpress(state);
	for (ArrowButton i: _arrowLeft)
	{
		(i).unpress(state);
	}
	for (ArrowButton i: _arrowRight)
	{
		(i).unpress(state);
	}
}

/**
 * Returns a pointer to a certain Text object in the list.
 * Useful for customizing or getting values off an individual cell.
 * @note If a Text is changed, a redraw needs to be manually requested.
 * @param row Row number.
 * @param col Column number.
 * @return Pointer to the requested Text.
 */
public final Text getCell(int row, int col)
{
	return _texts[row][col];
}

/**
 * Adds a new row of text to the list, automatically creating
 * the required Text objects lined up where they need to be.
 * @param cols Number of columns.
 * @param ... Text for each cell in the new row.
 */
public void addRow(int cols)//, ...)
{
	va_list args;
	va_start(args, cols);
	Vector<Text> temp;
	int rowX = 0;

	for (int i = 0; i < cols; i++)
	{
		// Place text
		Text txt = new Text(_columns[i], _font.getHeight(), _margin + rowX, getY());
		txt.setPalette(this.getPalette());
		txt.setFonts(_big, _small);
		txt.setColor(_color);
		txt.setSecondaryColor(_color2);
		txt.setAlign(_align);
		if (_font == _big)
		{
			txt.setBig();
		}
		else
		{
			txt.setSmall();
		}
		txt.setText(va_arg(args, char));

		// Places dots between text
		if (_dot && i < cols - 1)
		{
			WString buf = txt.getText();
			int w = txt.getTextWidth();
			while (w < _columns[i])
			{
				w += _font.getChar('.').getCrop().w + _font.getSpacing();
				buf += '.';
			}
			txt.setText(buf);
		}

		temp.add(txt);
		if (_condensed)
		{
			rowX += txt.getTextWidth();
		}
		else
		{
			rowX += _columns[i];
		}
	}
	_texts.add(temp);

	// Place arrow buttons
	if (_arrowPos != -1)
	{
		ArrowShape shape1, shape2;
		if (_arrowType == ArrowOrientation.ARROW_VERTICAL)
		{
			shape1 = ArrowButton.ArrowShape.ARROW_SMALL_UP;
			shape2 = ArrowButton.ArrowShape.ARROW_SMALL_DOWN;
		}
		else
		{
			shape1 = ArrowButton.ArrowShape.ARROW_SMALL_LEFT;
			shape2 = ArrowButton.ArrowShape.ARROW_SMALL_RIGHT;
		}
		ArrowButton a1 = new ArrowButton(shape1, 11, 8, getX() + _arrowPos, getY());
		a1.setPalette(this.getPalette());
		a1.setColor(_up.getColor());
		a1.onMouseClick(_leftClick);
		a1.onMousePress(_leftPress);
		a1.onMouseRelease(_leftRelease);
		_arrowLeft.add(a1);
		ArrowButton a2 = new ArrowButton(shape2, 11, 8, getX() + _arrowPos + 12, getY());
		a2.setPalette(this.getPalette());
		a2.setColor(_up.getColor());
		a2.onMouseClick(_rightClick);
		a2.onMousePress(_rightPress);
		a2.onMouseRelease(_rightRelease);
		_arrowRight.add(a2);
	}

	draw();
	va_end(args);
	updateArrows();
}

/**
 * Changes the columns that the list contains.
 * While rows can be unlimited, columns need to be specified
 * since they can have various widths for lining up the text.
 * @param cols Number of columns.
 * @param ... Width of each column.
 */
public void setColumns(int cols, ...)
{
	va_list args;
	va_start(args, cols);

	for (int i = 0; i < cols; i++)
	{
		_columns.add(va_arg(args, int));
	}

	va_end(args);
}

/**
 * Replaces a certain amount of colors in the palette of all
 * the text contained in the list.
 * @param colors Pointer to the set of colors.
 * @param firstcolor Offset of the first color to replace.
 * @param ncolors Amount of colors to replace.
 */
public void setPalette(SDL_Color colors, int firstcolor, int ncolors)
{
	super.setPalette(colors, firstcolor, ncolors);
	for (Vector< Vector<Text> >.iterator u = _texts.begin(); u < _texts.end(); ++u)
	{
		for (Vector<Text>.iterator v = u.begin(); v < u.end(); ++v)
		{
			(v).setPalette(colors, firstcolor, ncolors);
		}
	}
	for (ArrowButton i: _arrowLeft)
	{
		(i).setPalette(colors, firstcolor, ncolors);
	}
	for (ArrowButton i: _arrowRight)
	{
		(i).setPalette(colors, firstcolor, ncolors);
	}
	if (_selector != null)
	{
		_selector.setPalette(colors, firstcolor, ncolors);
	}
	_up.setPalette(colors, firstcolor, ncolors);
	_down.setPalette(colors, firstcolor, ncolors);
}

/**
 * Changes the various fonts of the text in the list
 * and calculates the selector and visible amount of rows.
 * @param big Pointer to large-size font.
 * @param small Pointer to small-size font.
 */
public void setFonts(Font big, Font small)
{
	_big = big;
	_small = small;
	_font = small;

	_selector = null;
	_selector = new Surface(getWidth(), _font.getHeight() + _font.getSpacing(), getX(), getY());
	_selector.setPalette(getPalette());
	_selector.setVisible(false);

	for (int y = 0; y < getHeight(); y += _font.getHeight() + _font.getSpacing())
	{
		_visibleRows++;
	}
}

/**
 * Changes the color of the text in the list. This doesn't change
 * the color of existing text, just the color of text added from then on.
 * @param color Color value.
 */
public void setColor(short color)
{
	_color = color;
}

public void setColor(int color)
{
	_color = (short)color;
}

/**
 * Returns the color of the text in the list.
 * @return Color value.
 */
public final short getColor()
{
	return _color;
}

/**
 * Changes the secondary color of the text in the list.
 * @param color Color value.
 */
public void setSecondaryColor(short color)
{
	_color2 = color;
}

public void setSecondaryColor(int color)
{
	_color2 = (short)color;
}

/**
 * Returns the secondary color of the text in the list.
 * @return Color value.
 */
public final short getSecondaryColor()
{
	return _color2;
}

/**
 * Changes the horizontal alignment of the text in the list. This doesn't change
 * the alignment of existing text, just the alignment of text added from then on.
 * @param align Horizontal alignment.
 */
public void setAlign(TextHAlign align)
{
	_align = align;
}

/**
 * If enabled, the text in different columns will be separated by dots.
 * Otherwise, it will only be separated by blank space.
 * @param dot True for dots, False for spaces.
 */
public void setDot(boolean dot)
{
	_dot = dot;
}

/**
 * If enabled, the list will respond to player input,
 * highlighting selected rows and receiving clicks.
 * @param selectable Selectable setting.
 */
public void setSelectable(boolean selectable)
{
	_selectable = selectable;
}

/**
 * Changes the text list to use the big-size font.
 */
public void setBig()
{
	_font = _big;

	_selector = null;
	_selector = new Surface(getWidth(), _font.getHeight() + _font.getSpacing(), getX(), getY());
	_selector.setPalette(getPalette());
	_selector.setVisible(false);

	for (int y = 0; y < getHeight(); y += _font.getHeight() + _font.getSpacing())
	{
		_visibleRows++;
	}
}

/**
 * Changes the text list to use the small-size font.
 */
public void setSmall()
{
	_font = _small;

	_selector = null;
	_selector = new Surface(getWidth(), _font.getHeight() + _font.getSpacing(), getX(), getY());
	_selector.setPalette(getPalette());
	_selector.setVisible(false);

	for (int y = 0; y < getHeight(); y += _font.getHeight() + _font.getSpacing())
	{
		_visibleRows++;
	}
}

/**
 * If enabled, the text in different columns will be separated by dots.
 * Otherwise, it will only be separated by blank space.
 * @param condensed True for condensed layout, False for table layout.
 */
public void setCondensed(boolean condensed)
{
	_condensed = condensed;
}

/**
 * Returns the currently selected row if the text
 * list is selectable.
 * @return Selected row.
 */
public final int getSelectedRow()
{
	return _selRow;
}

/**
 * Changes the surface used to draw the background of the selector.
 * @param bg New background.
 */
public void setBackground(Surface bg)
{
	_bg = bg;
}

/**
 * Changes the horizontal margin placed around the text.
 * @param margin Margin in pixels.
 */
public void setMargin(int margin)
{
	_margin = margin;
}

/**
 * Changes the color of the arrow buttons in the list.
 * @param color Color value.
 */
public void setArrowColor(short color)
{
	_up.setColor(color);
	_down.setColor(color);
}

public void setArrowColor(int color)
{
	_up.setColor((short)color);
	_down.setColor((short)color);
}

/**
 * Sets the position of the column of arrow buttons
 * in the text list.
 * @param pos X in pixels (-1 to disable).
 * @param type Arrow orientation type.
 */
public void setArrowColumn(int pos, ArrowOrientation type)
{
	_arrowPos = pos;
	_arrowType = type;
}

/**
 * Sets a function to be called everytime the left arrows are mouse clicked.
 * @param handler Action handler.
 */
public void onLeftArrowClick(ActionHandler handler)
{
	_leftClick = handler;
	for (ArrowButton i: _arrowLeft)
	{
		(i).onMouseClick(handler);
	}
}

/**
 * Sets a function to be called everytime the left arrows are mouse pressed.
 * @param handler Action handler.
 */
public void onLeftArrowPress(ActionHandler handler)
{
	_leftPress = handler;
	for (ArrowButton i: _arrowLeft)
	{
		(i).onMousePress(handler);
	}
}

/**
 * Sets a function to be called everytime the left arrows are mouse released.
 * @param handler Action handler.
 */
public void onLeftArrowRelease(ActionHandler handler)
{
	_leftRelease = handler;
	for (ArrowButton i: _arrowLeft)
	{
		(i).onMouseRelease(handler);
	}
}

/**
 * Sets a function to be called everytime the right arrows are mouse clicked.
 * @param handler Action handler.
 */
public void onRightArrowClick(ActionHandler handler)
{
	_rightClick = handler;
	for (ArrowButton i: _arrowRight)
	{
		(i).onMouseClick(handler);
	}
}

/**
 * Sets a function to be called everytime the right arrows are mouse pressed.
 * @param handler Action handler.
 */
public void onRightArrowPress(ActionHandler handler)
{
	_rightPress = handler;
	for (ArrowButton i: _arrowRight)
	{
		(i).onMousePress(handler);
	}
}

/**
 * Sets a function to be called everytime the right arrows are mouse released.
 * @param handler Action handler.
 */
public void onRightArrowRelease(ActionHandler handler)
{
	_rightRelease = handler;
	for (ArrowButton i: _arrowRight)
	{
		(i).onMouseRelease(handler);
	}
}

/**
 * Removes all the rows currently stored in the list.
 */
public void clearList()
{
	for (Vector< Vector<Text*> >.iterator u = _texts.begin(); u < _texts.end(); ++u)
	{
		for (Vector<Text*>.iterator v = u.begin(); v < u.end(); ++v)
		{
			delete (*v);
		}
		u.clear();
	}
	_texts.clear();
}

/**
 * Scrolls the text in the list up by one row.
 */
public void scrollUp()
{
	if (_texts.size() > _visibleRows && _scroll > 0)
	{
		_scroll--;
		draw();
	}
	updateArrows();
}

/**
 * Scrolls the text in the list down by one row.
 */
public void scrollDown()
{
	if (_texts.size() > _visibleRows && _scroll < _texts.size() - _visibleRows)
	{
		_scroll++;
		draw();
	}
	updateArrows();
}

/**
 * Updates the visibility of the arrow buttons according to
 * the current scroll position.
 */
private void updateArrows()
{
	_up.setVisible((_texts.size() > _visibleRows && _scroll > 0));
	_down.setVisible((_texts.size() > _visibleRows && _scroll < _texts.size() - _visibleRows));
}

/**
 * Draws the text list and all the text contained within.
 */
public void draw()
{
	clear();
	for (int i = _scroll; i < _texts.size() && i < _scroll + _visibleRows; i++)
	{
		for (Text j: _texts[i])
		{
			(j).setY((i - _scroll) * (_font.getHeight() + _font.getSpacing()));
            (j).blit(this);
        }
    }
}

/**
 * Blits the text list and selector.
 * @param surface Pointer to surface to blit onto.
 */
public void blit(Surface surface)
{
	if (_visible && !_hidden)
	{
		_selector.blit(surface);
	}
	super.blit(surface);
	if (_visible && !_hidden)
	{
		_up.blit(surface);
		_down.blit(surface);
		if (_arrowPos != -1)
		{
			for (int i = _scroll; i < _texts.size() && i < _scroll + _visibleRows; i++)
			{
				_arrowLeft[i].setY(getY() + (i - _scroll) * (_font.getHeight() + _font.getSpacing()));
				_arrowLeft[i].blit(surface);
				_arrowRight[i].setY(getY() + (i - _scroll) * (_font.getHeight() + _font.getSpacing()));
				_arrowRight[i].blit(surface);
			}
		}
	}
}

/**
 * Passes events to arrow buttons.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void handle(Action action, State state)
{
	super.handle(action, state);
	_up.handle(action, state);
	_down.handle(action, state);
	if (_arrowPos != -1)
	{
		for (int i = _scroll; i < _texts.size() && i < _scroll + _visibleRows; i++)
		{
			_arrowLeft[i].handle(action, state);
			_arrowRight[i].handle(action, state);
		}
	}
}

/**
 * Passes ticks to arrow buttons.
 */
public void think()
{
	super.think();
	_up.think();
	_down.think();
	for (ArrowButton i: _arrowLeft)
	{
		(i).think();
	}
	for (ArrowButton i: _arrowRight)
	{
		(i).think();
	}
}

/**
 * Ignores any mouse clicks that aren't on a row with the left mouse button.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mousePress(Action action, State state)
{
	if (_selectable && action.getDetails().button.button == SDL_BUTTON_LEFT)
	{
		if (_selRow < _texts.size())
		{
			super.mousePress(action, state);
		}
	}
	else if (action.getDetails().button.button == SDL_BUTTON_WHEELUP)
	{
		scrollUp();
	}
	else if (action.getDetails().button.button == SDL_BUTTON_WHEELDOWN)
	{
		scrollDown();
	}
}

/*
 * Ignores any mouse clicks that aren't on a row with the left mouse button.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseRelease(Action action, State state)
{
	if (_selectable && action.getDetails().button.button == SDL_BUTTON_LEFT)
	{
		if (_selRow < _texts.size())
		{
			super.mouseRelease(action, state);
		}
	}
}

/**
 * Ignores any mouse clicks that aren't on a row with the left mouse button.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseClick(Action action, State state)
{
	if (_selectable && action.getDetails().button.button == SDL_BUTTON_LEFT)
	{
		if (_selRow < _texts.size())
		{
			super.mouseClick(action, state);
		}
	}
}

/**
 * Selects the row the mouse is over.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseOver(Action action, State state)
{
	if (_selectable)
	{
		int h = _font.getHeight() + _font.getSpacing();
		double y = action.getYMouse() - getY() * action.getYScale();
		_selRow = _scroll + (int)floor(y / (h * action.getYScale()));

		if (_selRow < _texts.size())
		{
			_selector.setY(getY() + (_selRow - _scroll) * h);
			_selector.copy(_bg);
			_selector.offset(-10, Palette.backPos);
			_selector.setVisible(true);
		}
		else
		{
			_selector.setVisible(false);
		}
	}

	super.mouseOver(action, state);
}

/**
 * Deselects the row.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseOut(Action action, State state)
{
	if (_selectable)
	{
		_selector.setVisible(false);
	}

	super.mouseOut(action, state);
}

}
