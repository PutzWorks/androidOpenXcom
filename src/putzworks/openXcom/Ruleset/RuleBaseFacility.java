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

public class RuleBaseFacility
{
	private String _type;
	private int _spriteShape, _spriteFacility;
	private boolean _lift;
	private int _size, _buildCost, _buildTime, _monthlyCost;
	private int _storage, _personnel, _aliens, _crafts, _labs, _workshops, _psiLabs;
	private int _radarRange, _radarChance, _defence, _hitRatio;

/**
 * Creates a blank ruleset for a certain
 * type of base facility.
 * @param type String defining the type.
 */
public RuleBaseFacility(String type)
{
	_type = type;
	_spriteShape = -1;
	_spriteFacility = -1;
	_lift = false;
	_size = 1;
	_buildCost = 0;
	_buildTime = 0;
	_monthlyCost = 0;
	_storage = 0;
	_personnel = 0;
	_aliens = 0;
	_crafts = 0;
	_labs = 0;
	_workshops = 0;
	_psiLabs = 0;
	_radarRange = 0;
	_radarChance = 0;
	_defence = 0;

}

/**
 * Returns the language string that names
 * this base facility. Each base facility type
 * has a unique name.
 * @return Facility name.
 */
public final String getType()
{
	return _type;
}

/**
 * Returns the ID of the sprite used to draw the
 * base structure of the facility that defines its shape.
 * @return Sprite ID.
 */
public final int getSpriteShape()
{
	return _spriteShape;
}

/**
 * Changes the ID of the sprite used to draw the
 * base structure of the facility that defines its shape.
 * @param sprite Sprite ID.
 */
public void setSpriteShape(int sprite)
{
	_spriteShape = sprite;
}

/**
 * Returns the ID of the sprite used to draw the
 * facility's contents inside the base shape.
 * @return Sprite ID.
 */
public final int getSpriteFacility()
{
	return _spriteFacility;
}

/**
 * Changes the ID of the sprite used to draw the
 * facility's contents inside the base shape.
 * @param sprite Sprite ID.
 */
public void setSpriteFacility(int sprite)
{
	_spriteFacility = sprite;
}

/**
 * Returns the size of the facility on the base grid.
 * @return Length in grid squares.
 */
public final int getSize()
{
	return _size;
}

/**
 * Changes the size of the facility on the base grid.
 * @param size Length in grid squares.
 */
public void setSize(int size)
{
	_size = size;
}

/**
 * Returns whether this facility is the core access lift
 * of a base. Every base has an access lift and all
 * facilities have to be connected to it.
 * @return True if it's a lift, False otherwise.
 */
public final boolean getLift()
{
	return _lift;
}

/**
 * Changes whether this facility is the core access lift
 * of a base.
 * @param lift Lift state.
 */
public void setLift(boolean lift)
{
	_lift = lift;
}

/**
 * Returns the amount of funds that this facility costs
 * to build on a base.
 * @return Building cost.
 */
public final int getBuildCost()
{
	return _buildCost;
}

/**
 * Changes the amount of funds that this facility costs
 * to place on a base.
 * @param cost Building cost.
 */
public void setBuildCost(int cost)
{
	_buildCost = cost;
}

/**
 * Returns the amount of time that this facility takes
 * to be constructed since placement.
 * @return Time in days.
 */
public final int getBuildTime()
{
	return _buildTime;
}

/**
 * Changes the amount of time that this facility takes
 * to be constructed since placement.
 * @param time Time in days.
 */
public void setBuildTime(int time)
{
	_buildTime = time;
}

/**
 * Returns the amount of funds this facility costs monthly
 * to maintain once it's fully built.
 * @return Monthly cost.
 */
public final int getMonthlyCost()
{
	return _monthlyCost;
}

/**
 * Changes the amount of funds this facility costs monthly
 * to maintain once it's fully built.
 * @param cost Monthly cost.
 */
public void setMonthlyCost(int cost)
{
	_monthlyCost = cost;
}

/**
 * Returns the amount of storage space this facility provides
 * for base equipment.
 * @return Storage space.
 */
public final int getStorage()
{
	return _storage;
}

/**
 * Changes the amount of storage space this facility provides
 * for base equipment.
 * @param storage Storage space.
 */
public void setStorage(int storage)
{
	_storage = storage;
}

/**
 * Returns the amount of base personnel (soldiers, scientists,
 * engineers) const this facility can contain.
 * @return Amount of personnel.
 */
public final int getPersonnel()
{
	return _personnel;
}

/**
 * Changes the amount of base personnel this facility can contain.
 * @param personnel Amount of personnel.
 */
public void setPersonnel(int personnel)
{
	_personnel = personnel;
}

/**
 * Returns the amount of captured live aliens this facility
 * can contain.
 * @return Amount of aliens.
 */
public final int getAliens()
{
	return _aliens;
}

/**
 * Changes the amount of captured live aliens this
 * facility can contain.
 * @param aliens Amount of aliens.
 */
public void setAliens(int aliens)
{
	_aliens = aliens;
}

/**
 * Returns the amount of base craft this facility can contain.
 * @return Amount of craft.
 */
public final int getCrafts() 
{
	return _crafts;
}

/**
 * Changes the amount of base craft this facility can contain.
 * @param crafts Amount of craft.
 */
public void setCrafts(int crafts)
{
	_crafts = crafts;
}

/**
 * Returns the amount of laboratory space this facility provides
 * for research projects.
 * @return Laboratory space.
 */
public final int getLaboratories()
{
	return _labs;
}

/**
 * Changes the amount of laboratory space this facility provides
 * for research projects.
 * @param labs Laboratory space.
 */
public void setLaboratories(int labs)
{
	_labs = labs;
}

/**
 * Returns the amount of workshop space this facility provides
 * for manufacturing projects.
 * @return Workshop space.
 */
public final int getWorkshops()
{
	return _workshops;
}

/**
 * Changes the amount of workshop space this facility provides
 * for manufacturing projects.
 * @param workshops Workshop space.
 */
public void setWorkshops(int workshops)
{
	_workshops = workshops;
}

/**
 * Returns the amount of soldiers this facility can contain
 * for monthly psi-training.
 * @return Amount of soldiers.
 */
public final int getPsiLaboratories()
{
	return _psiLabs;
}

/**
 * Changes the amount of soldiers this facility can contain
 * for monthly psi-training.
 * @param psi Amount of soldiers.
 */
public void setPsiLaboratories(int psi)
{
	_psiLabs = psi;
}

/**
 * Returns the radar range this facility provides for the
 * detection of UFOs.
 * @return Range in nautical miles.
 */
public final int getRadarRange()
{
	return _radarRange;
}

/**
 * Changes the radar range this facility provides for the
 * detection of UFOs.
 * @param range Range in nautical miles.
 */
public void setRadarRange(int range)
{
	_radarRange = range;
}

/**
 * Returns the chance of UFOs that come within the facility's
 * radar range to be detected.
 * @return Chance in percentage.
 */
public final int getRadarChance()
{
	return _radarChance;
}

/**
 * Changes the chance of UFOs that come within the facility's
 * radar range to be detected.
 * @param chance Chance in percentage.
 */
public void setRadarChance(int chance)
{
	_radarChance = chance;
}

/**
 * Returns the defence value of this facility's weaponry
 * against UFO invasions on the base.
 * @return Defence value.
 */
public final int getDefenceValue()
{
	return _defence;
}

/**
 * Changes the defence value of this facility's weaponry
 * against UFO invasions on the base.
 * @param defence Defence value.
 */
public void setDefenceValue(int defence)
{
	_defence = defence;
}

/**
 * Returns the hit ratio of this facility's weaponry
 * against UFO invasions on the base.
 * @return Ratio in percentage.
 */
public final int getHitRatio()
{
	return _hitRatio;
}

/**
 * Changes the hit ratio of this facility's weaponry
 * against UFO invasions on the base.
 * @param ratio Ratio in percentage.
 */
public void setHitRatio(int ratio)
{
	_hitRatio = ratio;
}

}
