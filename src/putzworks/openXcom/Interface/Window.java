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

import putzworks.openXcom.Engine.RNG;
import putzworks.openXcom.Engine.Sound;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Engine.Surface;
import putzworks.openXcom.Engine.Timer;

public class Window extends Surface
{
	private Surface _bg;
	private short _color;
	private WindowPopup _popup;
	private double _popupStep;
	private Timer _timer;
	private State _state;

	private static final double POPUP_SPEED = 0.075;

	private Sound[] soundPopup = new Sound[3];

	public enum WindowPopup { POPUP_NONE, POPUP_HORIZONTAL, POPUP_VERTICAL, POPUP_BOTH };


/**
 * Sets up a blank window with the specified size and position.
 * @param state Pointer to state the window belongs to.
 * @param width Width in pixels.
 * @param height Height in pixels.
 * @param x X position in pixels.
 * @param y Y position in pixels.
 * @param popup Popup animation.
 */
public Window(State state, int width, int height, int x, int y, WindowPopup popup)
{ 
	super(width, height, x, y);
	_bg = null;
	_color = 0;
	_popup = popup;
	_popupStep = 0.0;
	_state = state;

	_timer = new Timer(10);
	_timer.onTimer((SurfaceHandler)Window.popup);

	if (_popup == Window.WindowPopup.POPUP_NONE)
	{
		_popupStep = 1.0;
	}
	else
	{
		_timer.start();
	}
}

/**
 * Deletes timers.
 */
public void clearWindow()
{
	_timer = null;
}

/**
 * Changes the surface used to draw the background of the window.
 * @param bg New background.
 */
public void setBackground(Surface bg)
{
	if (_popupStep < 1.0)
	{
		for (Surface i: _state.getSurfaces())
			if ((i) != this)
				(i).hide();
	}

	_bg = bg;
	draw();
}

/**
 * Changes the color used to draw the shaded border.
 * @param color Color value.
 */
public void setColor(short color)
{
	if (_popupStep < 1.0)
	{
		for (Surface i: _state.getSurfaces())
			if ((i) != this)
				(i).hide();
	}

	_color = color;
	draw();
}

public void setColor(int color) {
	if (_popupStep < 1.0)
	{
		for (Surface i: _state.getSurfaces())
			if ((i) != this)
				(i).hide();
	}

	_color = (short)color;
	draw();
	
}

/**
 * Returns the color used to draw the shaded border.
 * @return Color value.
 */
public final short getColor()
{
	return _color;
}

/**
 * Keeps the animation timers running.
 */
public void think()
{
	_timer.think(null, this);
}

/**
 * Plays the window popup animation.
 */
public void popup()
{
	if (_popupStep == 0.0)
	{
		int sound = RNG.generate(0, 2);
		if (soundPopup[sound] != null)
			soundPopup[sound].play();
	}
	if (_popupStep < 1.0)
	{
		_popupStep += POPUP_SPEED;
	}
	else
	{
		for (Surface i: _state.getSurfaces())
			if ((i) != this)
				(i).show();
		_popupStep = 1.0;
		_timer.stop();
	}
	draw();
}

/**
 * Draws the bordered window with a graphic background.
 * The background never moves with the window, it's
 * always aligned to the top-left corner of the screen
 * and cropped to fit the inside area.
 */
public void draw()
{
	Rect square;
	short color = _color;

	clear();

	if (_popup == WindowPopup.POPUP_HORIZONTAL || _popup == WindowPopup.POPUP_BOTH)
	{
		square.x = (int)((getWidth() - getWidth() * _popupStep) / 2);
		square.w = (int)(getWidth() * _popupStep);
	}
	else
	{
		square.x = 0;
		square.w = getWidth();
	}
	if (_popup == WindowPopup.POPUP_VERTICAL || _popup == WindowPopup.OPUP_BOTH)
	{
		square.y = (int)((getHeight() - getHeight() * _popupStep) / 2);
		square.h = (int)(getHeight() * _popupStep);
	}
	else
	{
		square.y = 0;
		square.h = getHeight();
	}

	for (int i = 0; i < 5; i++)
	{
		drawRect(square, color);
		if (i < 2)
			color--;
		else
			color++;
		square.x++;
		square.y++;
		if (square.w >= 2)
			square.w -= 2;
		else
			square.w = 1;

		if (square.h >= 2)
			square.h -= 2;
		else
			square.h = 1;
	}

	if (_bg != null)
	{
		_bg.getCrop().x = getX() + square.x;
		_bg.getCrop().y = getY() + square.y;
		_bg.getCrop().w = square.w;
		_bg.getCrop().h = square.h;
		_bg.setX(square.x);
		_bg.setY(square.y);
		_bg.blit(this);
	}
}



}
