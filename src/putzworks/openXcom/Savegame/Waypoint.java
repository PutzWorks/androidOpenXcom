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

public class Waypoint extends Target
{
	private int _id;

/**
 * Initializes a waypoint.
 */
public Waypoint()
{
	super();
	_id = 0;
}

/**
 * Loads the waypoint from a YAML file.
 * @param node YAML node.
 */
public void load(final YAML.Node node)
{
	super.load(node);
	node["id"] >> _id;
}

/**
 * Saves the waypoint to a YAML file.
 * @param out YAML emitter.
 */
public final void save(YAML.Emitter out)
{
	super.save(out);
	out << YAML.Key << "id" << YAML.Value << _id;
	out << YAML.EndMap;
}

/**
 * Saves the waypoint's unique identifiers to a YAML file.
 * @param out YAML emitter.
 */
public final void saveId(YAML.Emitter out)
{
	super.saveId(out);
	out << YAML.Key << "type" << YAML.Value << "STR_WAYPOINT";
	out << YAML.Key << "id" << YAML.Value << _id;
	out << YAML.EndMap;
}

/**
 * Returns the waypoint's unique ID.
 * @return Unique ID.
 */
public final int getId()
{
	return _id;
}

/**
 * Changes the waypoint's unique ID.
 * @param id Unique ID.
 */
public void setId(int id)
{
	_id = id;
}

/**
 * Returns the waypoint's unique identifying name.
 * @param lang Language to get strings from.
 * @return Full name.
 */
public final String getName(Language lang)
{
	StringBuffer name = new StringBuffer();
	name.append(lang.getString("STR_WAY_POINT_") + _id);
	return name.toString();
}

}
