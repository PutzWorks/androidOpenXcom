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

import putzworks.openXcom.Ruleset.RuleCraftWeapon;

public class CraftWeapon
{
	private RuleCraftWeapon _rules;
	private int _ammo;
	private boolean _rearming;

/**
 * Initializes a craft weapon of the specified type.
 * @param rules Pointer to ruleset.
 * @param ammo Initial ammo.
 */
public CraftWeapon(RuleCraftWeapon rules, int ammo)
{
	_rules = rules;
	_ammo = ammo;
	_rearming = false;

}

/**
 * Loads the craft weapon from a YAML file.
 * @param node YAML node.
 */
public void load(final YAML.Node node)
{
	node["ammo"] >> _ammo;
	node["rearming"] >> _rearming;
}

/**
 * Saves the base to a YAML file.
 * @param out YAML emitter.
 */
public final void save(YAML.Emitter out)
{
	out << YAML.BeginMap;
	out << YAML.Key << "type" << YAML.Value << _rules.getType();
	out << YAML.Key << "ammo" << YAML.Value << _ammo;
	out << YAML.Key << "rearming" << YAML.Value << _rearming;
	out << YAML.EndMap;
}

/**
 * Returns the ruleset for the craft weapon's type.
 * @return Pointer to ruleset.
 */
public final RuleCraftWeapon getRules()
{
	return _rules;
}

/**
 * Returns the ammo contained in this craft weapon.
 * @return Weapon ammo.
 */
public final int getAmmo()
{
	return _ammo;
}

/**
 * Changes the ammo contained in this craft weapon.
 * @param ammo Weapon ammo.
 */
public void setAmmo(int ammo)
{
	_ammo = ammo;
	if (_ammo > _rules.getAmmoMax())
	{
		_ammo = _rules.getAmmoMax();
	}
}

/**
 * Returns whether this craft weapon needs rearming.
 * @return Rearming status.
 */
public final boolean isRearming()
{
	return _rearming;
}

/**
 * Changes whether this craft weapon needs rearming
 * (for example, in case there's no more ammo).
 * @param rearming Rearming status.
 */
public void setRearming(boolean rearming)
{
	_rearming = rearming;
}

/**
 * Rearms this craft weapon's ammo.
 */
public void rearm()
{
	setAmmo(_ammo + _rules.getRearmRate());
	if (_ammo == _rules.getAmmoMax())
	{
		_rearming = false;
	}
}

}
