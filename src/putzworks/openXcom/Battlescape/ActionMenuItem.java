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
package putzworks.openXcom.Battlescape;

import putzworks.openXcom.Battlescape.BattlescapeState.BattleActionType;
import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.Font;
import putzworks.openXcom.Engine.InteractiveSurface;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Interface.Text;

public class ActionMenuItem extends InteractiveSurface
{
	private int _id;
	private boolean _highlighted;
	private BattleActionType _action;
	private int _tu;
	private Text _txtDescription, _txtAcc, _txtTU;

/**
 * Sets up an Action menu item.
 * @param state the parent state.
 * @param id the id.
 * @param bigFont pointer to the font.
 */
public ActionMenuItem(State state, int id, Font bigFont)
{
	super(270, 40, 25, 160 - (id*40));
	_id = id; 
	_highlighted = false;

	_txtDescription = new Text(100, 20, 16, 13);
	_txtDescription.setFonts(bigFont, null);
	_txtDescription.setBig();
	_txtDescription.setHighContrast(true);
	_txtDescription.setColor((short)0);
	_txtDescription.setVisible(true);

	_txtAcc = new Text(100, 20, 126, 13);
	_txtAcc.setFonts(bigFont, null);
	_txtAcc.setBig();
	_txtAcc.setHighContrast(true);
	_txtAcc.setColor((short)0);

	_txtTU = new Text(100, 20, 195, 13);
	_txtTU.setFonts(bigFont, null);
	_txtTU.setBig();
	_txtTU.setHighContrast(true);
	_txtTU.setColor((short)0);
}

/**
 * Link with an action and fill in the text fields.
 * @param action the battlescape action
 * @param description the actions description
 * @param accuracy the actions accuracy, including the Acc> prefix
 * @param timeunits the timeunits string, incliding the TUs> prefix
 */
public void setAction(BattleActionType action, WString description, WString accuracy, WString timeunits, int tu)
{
	_action = action;
	_txtDescription.setText(description);
	_txtAcc.setText(accuracy);
	_txtTU.setText(timeunits);
	_tu = tu;
	draw();
}

/**
 * Get the action that was linked to this menu item.
 * @return action
 */
public BattleActionType getAction()
{
	return _action;
}

/**
 * Get the action tus that was linked to this menu item.
 * @return tu
 */
int getTUs()
{
	return _tu;
}

/**
 * Replaces a certain amount of colors in the surface's palette.
 * @param colors Pointer to the set of colors.
 * @param firstcolor Offset of the first color to replace.
 * @param ncolors Amount of colors to replace.
 */
public void setPalette(SDL_Color colors, int firstcolor, int ncolors)
{
	Surface.setPalette(colors, firstcolor, ncolors);
	_txtDescription.setPalette(colors, firstcolor, ncolors);
	_txtAcc.setPalette(colors, firstcolor, ncolors);
	_txtTU.setPalette(colors, firstcolor, ncolors);
}

/**
 * Draws the bordered box.
 */
public void draw()
{
	Rect square;
	short color = 11;

	clear();

	square.x = 0;
	square.w = getWidth();

	square.y = 0;
	square.h = getHeight();

	for (int i = 0; i < 9; ++i)
	{
		if (i == 8 && _highlighted)
			color -= 4;
		drawRect(square, color);
		if (i < 3)
			color-=2;
		else
			color+=2;
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

	_txtDescription.draw();
	_txtDescription.blit(this);
	_txtAcc.draw();
	_txtAcc.blit(this);
	_txtTU.draw();
	_txtTU.blit(this);
}

/**
 * Processes a mouse hover in event.
 * @param action Pointer to an action.
 * @param state Pointer to an state.
 */
public void mouseIn(Action action, State state)
{
	_highlighted = true;
	draw();
	super.mouseIn(action, state);
}


/**
 * Processes a mouse hover out event.
 * @param action Pointer to an action.
 * @param state Pointer to an state.
 */
public void mouseOut(Action action, State state)
{
	_highlighted = false;
	draw();
	super.mouseOut(action, state);
}


}
