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

public class RuleCraftWeapon
{
	String _type;
	int _sprite, _sound, _damage, _range, _accuracy, _reloadCautious, _reloadStandard, _reloadAggressive, _ammoMax, _rearm;
	String _launcher, _clip;

/**
 * Creates a blank ruleset for a certain type of craft weapon.
 * @param type String defining the type.
 */
public RuleCraftWeapon(String type)
{
	_type = type;
	_sprite = -1;
	_sound = -1;
	_damage = 0;
	_range = 0;
	_accuracy = 0;
	_reloadCautious = 0;
	_reloadStandard = 0;
	_reloadAggressive = 0;
	_ammoMax = 0; 
	_rearm = 1;

}

/**
 * Returns the language string that names this craft weapon.
 * Each craft weapon type has a unique name.
 * @return Craft weapon name.
 */
public final String getType()
{
	return _type;
}

/**
 * Returns the ID of the sprite used to draw the craft weapon
 * in the Equip Craft and Interception screens.
 * @return Sprite ID.
 */
public final int getSprite()
{
	return _sprite;
}

/**
 * Changes the ID of the sprite used to draw the craft weapon
 * in the Equip Craft and Interception screens.
 * @param sprite Sprite ID.
 */
public void setSprite(int sprite)
{
	_sprite = sprite;
}

/**
 * Returns the ID of the sound used when firing the weapon
 * in the Dogfight screen.
 * @return Sound ID.
 */
public final int getSound()
{
	return _sound;
}

/**
 * Changes the ID of the sound used when firing the weapon
 * in the Dogfight screen.
 * @param sound Sound ID.
 */
public void setSound(int sound)
{
	_sound = sound;
}

/**
 * Returns the amount of damage this craft weapon
 * inflicts on enemy crafts.
 * @return Damage amount.
 */
public final int getDamage()
{
	return _damage;
}

/**
 * Changes the amount of damage this craft weapon
 * inflicts on enemy crafts.
 * @param damage Damage amount.
 */
public void setDamage(int damage)
{
	_damage = damage;
}

/**
 * Returns the maximum range of this craft weapon.
 * @return Range in km.
 */
public final int getRange()
{
	return _range;
}

/**
 * Changes the maximum range of this craft weapon.
 * @param range Range in km.
 */
public void setRange(int range)
{
	_range = range;
}

/**
 * Returns the percentage chance of each shot of
 * this craft weapon hitting an enemy craft.
 * @return Percentage accuracy.
 */
public final int getAccuracy()
{
	return _accuracy;
}

/**
 * Changes the percentage chance of each shot of
 * this craft weapon hitting an enemy craft.
 * @param accuracy Percentage accuracy.
 */
public void setAccuracy(int accuracy)
{
	_accuracy = accuracy;
}

/**
 * Returns the amount of time the craft weapon takes to
 * reload in cautious mode.
 * @return Time in game seconds.
 */
public final int getCautiousReload()
{
	return _reloadCautious;
}

/**
 * Changes the amount of time the craft weapon takes to
 * reload in cautious mode.
 * @param reload Time in game seconds.
 */
public void setCautiousReload(int reload)
{
	_reloadCautious = reload;
}

/**
 * Returns the amount of time the craft weapon takes to
 * reload in standard mode.
 * @return Time in game seconds.
 */
public final int getStandardReload()
{
	return _reloadStandard;
}

/**
 * Changes the amount of time the craft weapon takes to
 * reload in standard mode.
 * @param reload Time in game seconds.
 */
public void setStandardReload(int reload)
{
	_reloadStandard = reload;
}

/**
 * Returns the amount of time the craft weapon takes to
 * reload in aggressive mode.
 * @return Time in game seconds.
 */
public final int getAggressiveReload()
{
	return _reloadAggressive;
}

/**
 * Changes the amount of time the craft weapon takes to
 * reload in aggressive mode.
 * @param reload Time in game seconds.
 */
public void setAggressiveReload(int reload)
{
	_reloadAggressive = reload;
}

/**
 * Returns the maximum amount of ammo the craft weapon
 * can carry.
 * @return Amount of ammo.
 */
public final int getAmmoMax()
{
	return _ammoMax;
}

/**
 * Changes the maximum amount of ammo the craft weapon
 * can carry.
 * @param ammo Amount of ammo.
 */
public void setAmmoMax(int ammo)
{
	_ammoMax = ammo;
}

/**
 * Returns how much ammo is added to the craft weapon
 * while rearming (the amount of ammo in each clip item).
 * @return Amount of ammo.
 */
public final int getRearmRate() 
{
	return _rearm;
}

/**
 * Changes how much ammo is added to the craft weapon
 * while rearming (the amount of ammo in each clip item).
 * @param rearm Amount of ammo.
 */
public void setRearmRate(int rearm)
{
	_rearm = rearm;
}

/**
 * Returns the language string of the item used to
 * equip this craft weapon.
 * @return Item name.
 */
public final String getLauncherItem()
{
	return _launcher;
}

/**
 * Changes the language string of the item used to
 * equip this craft weapon.
 * @param launcher Item name.
 */
public void setLauncherItem(String launcher)
{
	_launcher = launcher;
}

/**
 * Returns the language string of the item used to
 * load this craft weapon with ammo.
 * @return Item name.
 */
public final String getClipItem()
{
	return _clip;
}

/**
 * Changes the language string of the item used to
 * load this craft weapon with ammo.
 * @param clip Item name.
 */
public void setClipItem(String clip)
{
	_clip = clip;
}

}
