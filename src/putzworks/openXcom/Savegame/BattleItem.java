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
package putzworks.openXcom.Savegame;

import putzworks.openXcom.Battlescape.Position;
import putzworks.openXcom.Ruleset.RuleItem;

public class BattleItem
{
	public enum InventorySlot { RIGHT_HAND, LEFT_HAND };

	private RuleItem _rules;
	private Position _position;
	private BattleUnit _owner;
	private InventorySlot _inventorySlot;
	private BattleItem _ammoItem;
	private int[] _itemProperty = new int[3];

/**
 * Initializes a item of the specified type.
 * @param rules Pointer to ruleset.
 */
public BattleItem(RuleItem rules)
{
	_rules = rules; 
	_owner = null;
	_ammoItem = null;

	_itemProperty[0] = 0;
	_itemProperty[1] = 0;
	_itemProperty[2] = 0;
	if (_rules.getBattleType() == RuleItem.BattleType.BT_AMMO)
	{
		setAmmoQuantity(_rules.getClipSize());
	}
}

/**
 * Loads the item from a YAML file.
 * @param node YAML node.
 */
public void load(final YAML.Node node)
{
	int a;

	node["X"] >> _position.x;
	node["Y"] >> _position.y;
	node["Z"] >> _position.z;

	node["inventoryslot"] >> a;
	_inventorySlot = (InventorySlot)a;
}

/**
 * Saves the item to a YAML file.
 * @param out YAML emitter.
 */
public final void save(YAML.Emitter out)
{
	out << YAML.BeginMap;

	out << YAML.Key << "type" << YAML.Value << _rules.getType();
	out << YAML.Key << "X" << YAML.Value << _position.x;
	out << YAML.Key << "Y" << YAML.Value << _position.y;
	out << YAML.Key << "Z" << YAML.Value << _position.z;
	out << YAML.Key << "owner" << YAML.Value << _owner.getId();
	out << YAML.Key << "inventoryslot" << YAML.Value << (int)_inventorySlot;

	out << YAML.EndMap;
}

/**
 * Returns the ruleset for the item's type.
 * @return Pointer to ruleset.
 */
public final RuleItem getRules()
{
	return _rules;
}

/**
 * Returns the quantity of ammo in this item.
 * @return Ammo quantity.
 */
public final int getAmmoQuantity()
{
	return _itemProperty[0];
}

/**
 * Changes the quantity of ammo in this item.
 * @param qty Ammo quantity.
 */
public void setAmmoQuantity(int qty)
{
	_itemProperty[0] = qty;
}

/**
 * Changes the quantity of ammo in this item.
 * @param qty Ammo quantity.
 */
public boolean spendBullet()
{
	_itemProperty[0]--;
	if (_itemProperty[0] == 0)
		return false;
	else
		return true;
}


/// Gets the item's owner.
public final BattleUnit getOwner()
{
	return _owner;
}

/// Sets the item's owner.
public void setOwner(BattleUnit owner)
{
	_owner = owner;
}

/// Gets the item's inventory slot.
public InventorySlot getSlot()
{
	return _inventorySlot;
}

/// Sets the item's inventory slot.
public void setSlot(InventorySlot slot)
{
	_inventorySlot = slot;
}

/// Gets the item's ammo item.
public BattleItem getAmmoItem()
{
	return _ammoItem;
}

/// Sets the item's ammo item. Return -2 when ammo doesn't fit, or -1 when weapon already contains ammo?
public int setAmmoItem(BattleItem item)
{
	if (item == null)
	{
		_ammoItem = null;
		return 0;
	}

	if (_ammoItem != null)
		return -1;

	for (String i: _rules.getCompatibleAmmo())
	{
		if (i == item.getRules().getType())
		{
			_ammoItem = item;
			return 0;
		}
	}

	return -2;
}

}
