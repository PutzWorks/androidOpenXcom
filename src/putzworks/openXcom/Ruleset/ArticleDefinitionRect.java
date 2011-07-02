/*
 * Copyright 2011 OpenXcom Developers.
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
package putzworks.openXcom.Ruleset;

import java.util.EnumSet;
import putzworks.openXcom.Savegame.Soldier.SoldierRank;


public class ArticleDefinitionRect
{
		public int x;
		public int y;
		public int width;
		public int height;
	
	/** 
	 * Constructor
	 */
	public ArticleDefinitionRect()
	{
		x = 0;
		y = 0;
		width = 0;
		height = 0;

	}
	
	/** 
	 * Set the rectangle parameters in a function
	 */
	public void set(int set_x, int set_y, int set_width, int set_height)
	{
		x = set_x;
		y = set_y;
		width = set_width;
		height = set_height;
	}
}