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

import java.util.Vector;

import putzworks.openXcom.Engine.RNG;

public class SoldierNamePool
{
	Vector<String> _maleFirst, _femaleFirst, _maleLast, _femaleLast;

/**
 * Initializes a new pool with blank lists of names.
 */
public SoldierNamePool()
{
	_maleFirst = new Vector<String>();
	_femaleFirst = new Vector<String>();
	_maleLast = new Vector<String>();
	_femaleLast = new Vector<String>();

}

/**
 * Loads the pool from a YAML file.
 * @param filename YAML file.
 */
public void load(final String filename)
{
	int size = 0;

	String s = "./DATA/SoldierName/" + filename + ".nam";
	std.ifstream fin(s.c_str());
	if (!fin)
	{
		throw new Exception("Failed to load name pool");
	}
    YAML.Parser parser(fin);
	YAML.Node doc;
    parser.GetNextDocument(doc);

	for(YAML.Iterator it = doc["maleFirst"].begin(); it != doc["maleFirst"].end(); it++)
	{
		String name;
		it >> name;
		_maleFirst.add(Language.utf8ToWstr(name));
	}
	for(YAML.Iterator it = doc["femaleFirst"].begin(); it != doc["femaleFirst"].end(); it++)
	{
		String name;
		it >> name;
		_femaleFirst.add(Language.utf8ToWstr(name));
	}
	for(YAML.Iterator it = doc["maleLast"].begin(); it != doc["maleLast"].end(); it++)
	{
		String name;
		it >> name;
		_maleLast.add(Language.utf8ToWstr(name));
	}
	if (YAML.Node pName = doc.FindValue("femaleLast"))
	{
		for(YAML.Iterator it = doc["femaleLast"].begin(); it != doc["femaleLast"].end(); it++)
		{
			String name;
			it >> name;
			_femaleLast.add(Language.utf8ToWstr(name));
		}
	}
	else
	{
		_femaleLast = _maleLast;
	}
		
	fin.close();
}

/**
 * Returns a new random name (first + last) from the
 * lists of names contained within.
 * @param gender Returned gender of the name.
 * @return Soldier name.
 */
public final String genName(int gender)
{
	StringBuffer name = new StringBuffer();
	int first = RNG.generate(1, _maleFirst.size() + _femaleFirst.size());
	if (first <= _maleFirst.size())
	{
		gender = 0;
		name.append(_maleFirst.get(first - 1));
		int last = RNG.generate(1, _maleLast.size());
		name.append(" " + _maleLast.get(last - 1));
	}
	else
	{
		gender = 1;
		name.append(_femaleFirst.get(first - _maleFirst.size() - 1));
		int last = RNG.generate(1, _femaleLast.size());
		name.append(" " + _femaleLast.get(last - 1));
	}
	return name.toString();
}

}
