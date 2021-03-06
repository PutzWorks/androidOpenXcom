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

public class ArticleDefinitionTextImage extends ArticleDefinition
{
		public String image_id;
		public String text;
		public int text_width;
	
	/**
	 * Constructor (only setting type of base class)
	 */
	public ArticleDefinitionTextImage() 
	{
		super(UfopaediaTypeId.UFOPAEDIA_TYPE_TEXTIMAGE);
	}
}
