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

public class ArticleDefinition
{
	/// define article types
	public enum UfopaediaTypeId {
		UFOPAEDIA_TYPE_UNKNOWN (0),
		UFOPAEDIA_TYPE_CRAFT (1),
		UFOPAEDIA_TYPE_CRAFT_WEAPON (2),
		UFOPAEDIA_TYPE_HWP (3),
		UFOPAEDIA_TYPE_EQUIPMENT (4),
		UFOPAEDIA_TYPE_WEAPON (5),
		UFOPAEDIA_TYPE_FACILITY (6),
		UFOPAEDIA_TYPE_TEXTIMAGE (7),
		UFOPAEDIA_TYPE_TEXT (8),
		UFOPAEDIA_TYPE_UFO (9)
	;
		public int id;
		private UfopaediaTypeId(int i)
		{
			this.id = i;
		}
	     public static UfopaediaTypeId get(int code) { 
	    	 for(UfopaediaTypeId s : EnumSet.allOf(UfopaediaTypeId.class)){
	               if (s.id == code){return s;}
	    	 }
	    	 return null;
	     }
			
	}

	protected UfopaediaTypeId _type_id;

		public String id;
		public String title;
		public String section;
		public int sort_key;
	/**
	 * Constructor
	 * @param type_id Article type of this instance.
	 */
	protected ArticleDefinition(UfopaediaTypeId type_id)
	{
		_type_id = type_id;
	}
	
	/**
	 * Get the article definition type. (Text, TextImage, Craft, ...)
	 * @returns The type of article definition of this instance.
	 */
	public UfopaediaTypeId getType()
	{
		return _type_id;
	}
}