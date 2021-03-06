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

import putzworks.openXcom.Engine.Language;
import putzworks.openXcom.Ruleset.Ruleset;

public class Transfer
{
	private int _hours;
	private Soldier _soldier;
	private Craft _craft;
	private String _itemId;
	private int _itemQty, _scientists, _engineers;
	private boolean _delivered;

	enum TransferType { TRANSFER_SOLDIER, TRANSFER_CRAFT, TRANSFER_ITEM, TRANSFER_SCIENTIST, TRANSFER_ENGINEER };


/**
 * Initializes a transfer.
 * @param hours Hours in-transit.
 */
public Transfer(int hours)
{
	_hours = hours;
	_soldier = null;
	_craft = null;
	_itemId = "";
	_itemQty = 0;
	_scientists = 0;
	_engineers = 0;
	_delivered = false;

}

/**
 * Cleans up undelivered transfers.
 */
public void clearTransfer()
{
	if (!_delivered)
	{
		_soldier = null;;
		_craft = null;;
	}
}

/**
 * Loads the transfer from a YAML file.
 * @param node YAML node.
 * @param base Destination base.
 * @param rule Game ruleset.
 */
public void load(final YAML.Node node, Base base, Ruleset rule)
{
	node["hours"] >> _hours;
	if (YAML.Node pName = node.FindValue("soldier"))
	{
		_soldier = new Soldier(rule.getSoldier("XCOM"), rule.getArmor("STR_NONE_UC"));
		_soldier.load(pName);
	}
	else if (YAML.Node pName = node.FindValue("craft"))
	{
		String type;
		(pName)["type"] >> type;
		_craft = new Craft(rule.getCraft(type), base);
		_craft.load(*pName, rule);
	}
	else if (YAML.Node pName = node.FindValue("itemId"))
	{
		*pName >> _itemId;
		node["itemQty"] >> _itemQty;
	}
	else if (YAML.Node pName = node.FindValue("scientists"))
	{
		pName >> _scientists;
	}
	else if (YAML.Node *pName = node.FindValue("engineers"))
	{
		pName >> _engineers;
	}
	node["delivered"] >> _delivered;
}

/**
 * Saves the transfer to a YAML file.
 * @param out YAML emitter.
 */
public final void save(YAML.Emitter out)
{
	out << YAML.BeginMap;
	out << YAML.Key << "hours" << YAML.Value << _hours;
	if (_soldier != 0)
	{
		out << YAML.Key << "soldier" << YAML.Value;
		_soldier.save(out);
	}
	else if (_craft != 0)
	{
		out << YAML.Key << "craft" << YAML.Value;
		_craft.save(out);
	}
	else if (_itemQty != 0)
	{
		out << YAML.Key << "itemId" << YAML.Value << _itemId;
		out << YAML.Key << "itemQty" << YAML.Value << _itemQty;
	}
	else if (_scientists != 0)
	{
		out << YAML.Key << "scientists" << YAML.Value << _scientists;
	}
	else if (_engineers != 0)
	{
		out << YAML.Key << "engineers" << YAML.Value << _engineers;
	}
	out << YAML.Key << "delivered" << YAML.Value << _delivered;
	out << YAML.EndMap;
}

/**
 * Changes the soldier being transferred.
 * @param soldier Pointer to soldier.
 */
public void setSoldier(Soldier soldier)
{
	_soldier = soldier;
}

/**
 * Changes the craft being transferred.
 * @param craft Pointer to craft.
 */
public void setCraft(Craft craft)
{
	_craft = craft;
}

/**
 * Returns the items being transferred.
 * @return Item ID.
 */
public final String getItems()
{
	return _itemId;
}

/**
 * Changes the items being transferred.
 * @param id Item identifier.
 * @param qty Item quantity.
 */
public void setItems(String id, int qty)
{
	_itemId = id;
	_itemQty = qty;
}

/**
 * Changes the scientists being transferred.
 * @param scientists Amount of scientists.
 */
public void setScientists(int scientists)
{
	_scientists = scientists;
}

/**
 * Changes the engineers being transferred.
 * @param engineers Amount of engineers.
 */
public void setEngineers(int engineers)
{
	_engineers = engineers;
}

/**
 * Returns the name of the contents of the transfer.
 * @param lang Language to get strings from.
 * @return Name string.
 */
public final String getName(Language lang)
{
	if (_soldier != null)
	{
		return _soldier.getName();
	}
	else if (_craft != null)
	{
		return _craft.getName(lang);
	}
	else if (_scientists != 0)
	{
		return lang.getString("STR_SCIENTISTS");
	}
	else if (_engineers != 0)
	{
		return lang.getString("STR_ENGINEERS");
	}
	return lang.getString(_itemId);
}

/**
 * Returns the time remaining until the
 * transfer arrives at its destination.
 * @return Amount of hours.
 */
public final int getHours()
{
	return _hours;
}

/**
 * Returns the quantity of items in the transfer.
 * @return Amount of items.
 */
public final int getQuantity()
{
	if (_itemQty != 0)
	{
		return _itemQty;
	}
	else if (_scientists != 0)
	{
		return _scientists;
	}
	else if (_engineers != 0)
	{
		return _engineers;
	}
	return 1;
}

/**
 * Returns the type of the contents of the transfer.
 * @return TransferType.
 */
public final TransferType getType()
{
	if (_soldier != null)
	{
		return TRANSFER_SOLDIER;
	}
	else if (_craft != null)
	{
		return TRANSFER_CRAFT;
	}
	else if (_scientists != 0)
	{
		return TRANSFER_SCIENTIST;
	}
	else if (_engineers != 0)
	{
		return TRANSFER_ENGINEER;
	}
	return TRANSFER_ITEM;
}

/**
 * Advances the transfer and takes care of
 * the delivery once it's arrived.
 * @param base Pointer to destination base.
 */
public void advance(Base base)
{
	_hours--;
	if (_hours == 0)
	{
		if (_soldier != null)
		{
			base.getSoldiers().add(_soldier);
		}
		else if (_craft != null)
		{
			base.getCrafts().add(_craft);
		}
		else if (_itemQty != 0)
		{
			base.getItems().addItem(_itemId, _itemQty);
		}
		else if (_scientists != 0)
		{
			base.setScientists(base.getScientists() + _scientists);
		}
		else if (_engineers != 0)
		{
			base.setEngineers(base.getEngineers() + _engineers);
		}
		_delivered = true;
	}
}

}
