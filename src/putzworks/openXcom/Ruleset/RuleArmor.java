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
package putzworks.openXcom.Ruleset;

public class RuleArmor
{
	private String _type, _spriteSheet, _corpseItem;
	private int _frontArmor, _sideArmor, _rearArmor, _underArmor;

/**
 * Creates a blank ruleset for a certain
 * type of armor.
 * @param type String defining the type.
 */
public RuleArmor(String type, String spriteSheet)
{
	_type = type;
	_spriteSheet = spriteSheet;
}

/**
 * Returns the language string that names
 * this armor. Each armor has a unique name. Coveralls, Power Suit,...
 * @return Craft name.
 */
public final String getType()
{
	return _type;
}

/// Gets the unit's sprite sheet.
public final String getSpriteSheet()
{
	return _spriteSheet;
}

/// Sets the different armor levels.
public void setArmor(int front, int side, int rear, int under)
{
	_frontArmor = front;
	_sideArmor = side;
	_rearArmor = rear;
	_underArmor = under;
}

/// Get the front armor level.
public final int getFrontArmor()
{
	return _frontArmor;
}
/// Get the side armor level.
public final int getSideArmor()
{
	return _sideArmor;
}
/// get the rear armor level.
public final int getRearArmor()
{
	return _rearArmor;
}
/// get the under armor level.
public final int getUnderArmor()
{
	return _underArmor;
}
/// Set the corpse item.
public void setCorpseItem(String corpseItem)
{
	_corpseItem = corpseItem;
}
/// Get the corpse item.
public final String getCorpseItem()
{
	return _corpseItem;
}


}
