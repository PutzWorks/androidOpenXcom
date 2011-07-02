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

import java.util.Map;

import putzworks.openXcom.Ruleset.Ruleset;

public class ItemContainer
{
	private Map<String, Integer> _qty;

/**
 * Initializes an item container with no contents.
 */
public ItemContainer()
{
	_qty = null;
}

/**
 * Loads the item container from a YAML file.
 * @param node YAML node.
 */
public void load(final YAML.Node node)
{
	node >> _qty;
}

/**
 * Saves the item container to a YAML file.
 * @param out YAML emitter.
 */
public final void save(YAML.Emitter out)
{
	out << _qty;
}

/**
 * Adds an item amount to the container.
 * @param id Item ID.
 * @param qty Item quantity.
 */
public void addItem(final String id, int qty)
{
	if (_qty.get(id) == _qty.end())
	{
		_qty.put(id, 0);
	}
	_qty[id] += qty;
}

/**
 * Removes an item amount from the container.
 * @param id Item ID.
 * @param qty Item quantity.
 */
public void removeItem(final String id, int qty)
{
	if (_qty.find(id) == _qty.end())
	{
		return;
	}
	if (qty < _qty[id])
	{
		_qty[id] -= qty;
	}
	else
	{
		_qty.erase(id);
	}
}

/**
 * Returns the quantity of an item in the container.
 * @param id Item ID.
 * @return Item quantity.
 */
public final int getItem(final String id)
{
	Map<String, Integer>.const_iterator it = _qty.find(id);

	if (it == _qty.end())
	{
		return 0;
	}
	else
	{
		return it.second;
	}
}

/**
 * Returns the total quantity of the items in the container.
 * @return Total item quantity.
 */
public final int getTotalQuantity()
{
	int total = 0;
	for (Map<String, Integer>.const_iterator i = _qty.begin(); i != _qty.end(); ++i)
	{
		total += i.second;
	}
	return total;
}

/**
 * Returns the total size of the items in the container.
 * @param rule Pointer to ruleset.
 * @return Total item size.
 */
public final double getTotalSize(Ruleset rule)
{
	double total = 0;
	for (Map<String, Integer>.const_iterator i = _qty.begin(); i != _qty.end(); ++i)
	{
		total += rule.getItem(i.first).getSize() * i.second;
	}
	return total;
}

/**
 * Returns all the items currently contained within.
 * @return List of contents.
 */
public Map<String, Integer> getContents()
{
	return _qty;
}

}
