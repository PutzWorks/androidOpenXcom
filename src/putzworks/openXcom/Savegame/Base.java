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

import java.util.Vector;

import putzworks.openXcom.Engine.Language;
import putzworks.openXcom.Ruleset.Ruleset;
import putzworks.openXcom.Savegame.Transfer.TransferType;

public class Base extends Target
{
	private Ruleset _rule;
	private String _name;
	private Vector<BaseFacility> _facilities;
	private Vector<Soldier> _soldiers;
	private Vector<Craft> _crafts;
	private Vector<Transfer> _transfers;
	private ItemContainer _items;
	private int _scientists, _engineers;

/**
 * Initializes an empty base.
 * @param rule Pointer to ruleset.
 */
public Base(Ruleset rule)
{
	super();
	_rule = rule; 
	_name = "";
	_facilities = new Vector<BaseFacility>(); 
	_soldiers = new Vector<Soldier>();
	_crafts = new Vector<Craft>();
	_scientists = 0;
	_engineers = 0;
	_items = new ItemContainer();
}

/**
 * Loads the base from a YAML file.
 * @param node YAML node.
 * @param save Pointer to saved game.
 */
public void load(final YAML.Node node, SavedGame save)
{
	// TODO This should be an unsigned int!
	int size = 0;

	super.load(node);
	String name = node["name"];
	_name = Language.utf8ToWstr(name);

	size = node["facilities"].size();
	//TODO This should be an unsigned int
	for (int i = 0; i < size; i++)
	{
		int x, y;
		node["facilities"][i]["x"] >> x;
		node["facilities"][i]["y"] >> y;
		String type = node["facilities"][i]["type"];
		BaseFacility f = new BaseFacility(_rule.getBaseFacility(type), this, x, y);
		f.load(node["facilities"][i]);
		_facilities.add(f);
	}

	size = node["crafts"].size();
	for (int i = 0; i < size; i++)
	{
		String type = node["crafts"][i]["type"];
		Craft c = new Craft(_rule.getCraft(type), this);
		c.load(node["crafts"][i], _rule);
		if (YAML.Node pName = node["crafts"][i].FindValue("dest"))
		{
			String type;
			int id;
			(pName)["type"] >> type;
			(pName)["id"] >> id;
			if (type == "STR_BASE")
			{
				c.returnToBase();
			}
			else if (type == "STR_UFO")
			{
				for (Ufo u: save.getUfos())
				{
					if ((u).getId() == id)
					{
						c.setDestination(u);
						break;
					}
				}
			}
			else if (type == "STR_WAYPOINT")
			{
				for (Waypoint w: save.getWaypoints())
				{
					if ((w).getId() == id)
					{
						c.setDestination(w);
						break;
					}
				}
			}
		}
		_crafts.add(c);
	}

	size = node["soldiers"].size();
	for (int i = 0; i < size; i++)
	{
		Soldier s = new Soldier(_rule.getSoldier("XCOM"), _rule.getArmor("STR_NONE_UC"));
		s.load(node["soldiers"][i]);
		if (YAML.Node pName = node["soldiers"][i].FindValue("craft"))
		{
			String type;
			int id;
			(pName)["type"] >> type;
			(pName)["id"] >> id;
			for (Craft c: _crafts)
			{
				if ((c).getRules().getType() == type && (c).getId() == id)
				{
					s.setCraft(c);
					break;
				}
			}
		}
		_soldiers.add(s);
	}

	_items.load(node["items"]);
	node["scientists"] >> _scientists;
	node["engineers"] >> _engineers;

	size = node["transfers"].size();
	for (int i = 0; i < size; i++)
	{
		int hours;
		node["transfers"][i]["hours"] >> hours;
		Transfer t = new Transfer(hours);
		t.load(node["transfers"][i], this, _rule);
		_transfers.add(t);
	}
}

/**
 * Saves the base to a YAML file.
 * @param out YAML emitter.
 */
public final void save(YAML.Emitter out)
{
	super.save(out);
	out << YAML.Key << "name" << YAML.Value << Language.wstrToUtf8(_name);
	out << YAML.Key << "facilities" << YAML.Value;
	out << YAML.BeginSeq;
	for (BaseFacility i: _facilities)
	{
		(i).save(out);
	}
	out << YAML.EndSeq;
	out << YAML.Key << "soldiers" << YAML.Value;
	out << YAML.BeginSeq;
	for (Soldier i: _soldiers)
	{
		(i).save(out);
	}
	out << YAML.EndSeq;
	out << YAML.Key << "crafts" << YAML.Value;
	out << YAML.BeginSeq;
	for (Craft i: _crafts)
	{
		(i).save(out);
	}
	out << YAML.EndSeq;
	out << YAML.Key << "items" << YAML.Value;
	_items.save(out);
	out << YAML.Key << "scientists" << YAML.Value << _scientists;
	out << YAML.Key << "engineers" << YAML.Value << _engineers;
	out << YAML.Key << "transfers" << YAML.Value;
	out << YAML.BeginSeq;
	for (Transfer i: _transfers)
	{
		(i).save(out);
	}
	out << YAML.EndSeq;
	out << YAML.EndMap;
}

/**
 * Saves the base's unique identifiers to a YAML file.
 * @param out YAML emitter.
 */
public final void saveId(YAML.Emitter out)
{
	super.saveId(out);
	out << YAML.Key << "type" << YAML.Value << "STR_BASE";
	out << YAML.Key << "id" << YAML.Value << 0;
	out << YAML.EndMap;
}

/**
 * Returns the custom name for the base.
 * @param lang Language to get strings from.
 * @return Name.
 */
public final String getName()
{
	return _name;
}

/**
 * Changes the custom name for the base.
 * @param name Name.
 */
public void setName(final String name)
{
	_name = name;
}

/**
 * Returns the list of facilities in the base.
 * @return Pointer to the facility list.
 */
public Vector<BaseFacility> getFacilities()
{
	return  _facilities;
}

/**
 * Returns the list of soldiers in the base.
 * @return Pointer to the soldier list.
 */
public Vector<Soldier> getSoldiers()
{
	return _soldiers;
}

/**
 * Returns the list of crafts in the base.
 * @return Pointer to the craft list.
 */
public Vector<Craft> getCrafts()
{
	return _crafts;
}

/**
 * Returns the list of transfers destined
 * to this base.
 * @return Pointer to the transfer list.
 */
public Vector<Transfer> getTransfers()
{
	return _transfers;
}

/**
 * Returns the list of items in the base.
 * @return Pointer to the item list.
 */
public ItemContainer getItems()
{
	return _items;
}

/**
 * Returns the amount of scientists currently in the base.
 * @return Number of scientists.
 */
public final int getScientists()
{
	return _scientists;
}

/**
 * Changes the amount of scientists currently in the base.
 * @param scientists Number of scientists.
 */
public void setScientists(int scientists)
{
	 _scientists = scientists;
}

/**
 * Returns the amount of engineers currently in the base.
 * @return Number of engineers.
 */
public final int getEngineers()
{
	return _engineers;
}

/**
 * Changes the amount of engineers currently in the base.
 * @param engineers Number of engineers.
 */
public void setEngineers(int engineers)
{
	 _engineers = engineers;
}

/**
 * Returns the amount of soldiers contained
 * in the base without any assignments.
 * @return Number of soldiers.
 */
public final int getAvailableSoldiers()
{
	int total = 0;
	for (Soldier i: _soldiers)
	{
		if ((i).getCraft() == null)
		{
			total++;
		}
	}
	return total;
}

/**
 * Returns the total amount of soldiers contained
 * in the base.
 * @return Number of soldiers.
 */
public final int getTotalSoldiers()
{
	int total = _soldiers.size();
	for (Transfer i: _transfers)
	{
		if ((i).getType() == TransferType.TRANSFER_SOLDIER)
		{
			total += (i).getQuantity();
		}
	}
	return total;
}

/**
 * Returns the amount of scientists contained
 * in the base without any assignments.
 * @return Number of scientists.
 */
public final int getAvailableScientists()
{
	return _scientists;
}

/**
 * Returns the total amount of scientists contained
 * in the base.
 * @return Number of scientists.
 */
public final int getTotalScientists() 
{
	int total = _scientists;
	for (Transfer i: _transfers)
	{
		if ((i).getType() == TransferType.TRANSFER_SCIENTIST)
		{
			total += (i).getQuantity();
		}
	}
	return total;
}

/**
 * Returns the amount of engineers contained
 * in the base without any assignments.
 * @return Number of engineers.
 */
public final int getAvailableEngineers()
{
	return _engineers;
}

/**
 * Returns the total amount of engineers contained
 * in the base.
 * @return Number of engineers.
 */
public final int getTotalEngineers()
{
	int total = _engineers;
	for (Transfer i: _transfers)
	{
		if ((i).getType() == TransferType.TRANSFER_ENGINEER)
		{
			total += (i).getQuantity();
		}
	}
	return total;
}

/**
 * Returns the amount of living quarters used up
 * by personnel in the base.
 * @return Living space.
 */
public final int getUsedQuarters()
{
	return getTotalSoldiers() + getTotalScientists() + getTotalEngineers();
}

/**
 * Returns the total amount of living quarters
 * available in the base.
 * @return Living space.
 */
public final int getAvailableQuarters()
{
	int total = 0;
	for (BaseFacility i: _facilities)
	{
		if ((i).getBuildTime() == 0)
		{
			total += (i).getRules().getPersonnel();
		}
	}
	return total;
}

/**
 * Returns the amount of stores used up
 * by equipment in the base.
 * @return Storage space.
 */
public final int getUsedStores()
{
	double total = _items.getTotalSize(_rule);
	for (Craft i: _crafts)
	{
		total += (i).getItems().getTotalSize(_rule);
	}
	for (Transfer i: _transfers)
	{
		if ((i).getType() == TransferType.TRANSFER_ITEM)
		{
			total += (i).getQuantity() * _rule.getItem((i).getItems()).getSize();
		}
	}
	return (int)Math.floor(total);
}

/**
 * Returns the total amount of stores
 * available in the base.
 * @return Storage space.
 */
public final int getAvailableStores()
{
	int total = 0;
	for (BaseFacility i: _facilities)
	{
		if ((i).getBuildTime() == 0)
		{
			total += (i).getRules().getStorage();
		}
	}
	return total;
}

/**
 * Returns the amount of laboratories used up
 * by research projects in the base.
 * @return Laboratory space.
 */
public final int getUsedLaboratories()
{
	return 0;
}

/**
 * Returns the total amount of laboratories
 * available in the base.
 * @return Laboratory space.
 */
public final int getAvailableLaboratories()
{
	int total = 0;
	for (BaseFacility i: _facilities)
	{
		if ((i).getBuildTime() == 0)
		{
			total += (i).getRules().getLaboratories();
		}
	}
	return total;
}

/**
 * Returns the amount of workshops used up
 * by manufacturing projects in the base.
 * @return Storage space.
 */
public final int getUsedWorkshops()
{
	return 0;
}

/**
 * Returns the total amount of workshops
 * available in the base.
 * @return Workshop space.
 */
public final int getAvailableWorkshops()
{
	int total = 0;
	for (BaseFacility i: _facilities)
	{
		if ((i).getBuildTime() == 0)
		{
			total += (i).getRules().getWorkshops();
		}
	}
	return total;
}

/**
 * Returns the amount of hangars used up
 * by crafts in the base.
 * @return Storage space.
 */
public final int getUsedHangars()
{
	int total = _crafts.size();
	for (Transfer i: _transfers)
	{
		if ((i).getType() == TransferType.TRANSFER_CRAFT)
		{
			total += (i).getQuantity();
		}
	}
	return total;
}

/**
 * Returns the total amount of hangars
 * available in the base.
 * @return Number of hangars.
 */
public final int getAvailableHangars()
{
	int total = 0;
	for (BaseFacility i: _facilities)
	{
		if ((i).getBuildTime() == 0)
		{
			total += (i).getRules().getCrafts();
		}
	}
	return total;
}

/**
 * Returns the total defence value of all
 * the facilities in the base.
 * @return Defence value.
 */
public final int getDefenceValue()
{
	int total = 0;
	for (BaseFacility i: _facilities)
	{
		if ((i).getBuildTime() == 0)
		{
			total += (i).getRules().getDefenceValue();
		}
	}
	return total;
}

/**
 * Returns the total amount of short range
 * detection facilities in the base.
 * @return Defense value.
 */
public final int getShortRangeDetection()
{
	int total = 0;
	for (BaseFacility i: _facilities)
	{
		if ((i).getBuildTime() == 0 && (i).getRules().getRadarRange() == 1500)
		{
			total++;
		}
	}
	return total;
}

/**
 * Returns the total amount of long range
 * detection facilities in the base.
 * @return Defense value.
 */
public final int getLongRangeDetection()
{
	int total = 0;
	for (BaseFacility i: _facilities)
	{
		if ((i).getBuildTime() == 0 && (i).getRules().getRadarRange() > 1500)
		{
			total++;
		}
	}
	return total;
}

/**
 * Returns the total amount of craft of
 * a certain type stored in the base.
 * @param craft Craft type.
 * @return Number of craft.
 */
public final int getCraftCount(String craft)
{
	int total = 0;
	for (Craft i: _crafts)
	{
		if ((i).getRules().getType() == craft)
		{
			total++;
		}
	}
	return total;
}

/**
 * Returns the total amount of monthly costs
 * for maintaining the craft in the base.
 * @return Maintenance costs.
 */
public final int getCraftMaintenance()
{
	int total = 0;
	for (Craft i: _crafts)
	{
		total += (i).getRules().getCost();
	}
	return total;
}

/**
 * Returns the total amount of monthly costs
 * for maintaining the personnel in the base.
 * @return Maintenance costs.
 */
public final int getPersonnelMaintenance()
{
	int total = 0;
	total += _soldiers.size() * _rule.getSoldierCost();
	total += _engineers * _rule.getEngineerCost();
	total += _scientists * _rule.getScientistCost();
	return total;
}

/**
 * Returns the total amount of monthly costs
 * for maintaining the facilities in the base.
 * @return Maintenance costs.
 */
public final int getFacilityMaintenance()
{
	int total = 0;
	for (BaseFacility i: _facilities)
	{
		if ((i).getBuildTime() == 0)
		{
			total += (i).getRules().getMonthlyCost();
		}
	}
	return total;
}

/**
 * Returns the total amount of all the maintenance
 * monthly costs in the base.
 * @return Maintenance costs.
 */
public final int getMonthlyMaintenace()
{
	return getCraftMaintenance() + getPersonnelMaintenance() + getFacilityMaintenance();
}

}
