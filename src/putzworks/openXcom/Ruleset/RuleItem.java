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

public class RuleItem
{
	public enum ItemDamageType { DT_NONE, DT_AP, DT_IN, DT_HE, DT_LASER, DT_PLASMA, DT_STUN, DT_MELEE, DT_ACID, DT_SMOKE };
	public enum BattleType { BT_NONE, BT_FIREARM, BT_AMMO, BT_MELEE, BT_GRENADE, BT_PROXIMITYGRENADE, BT_MEDIKIT, BT_SCANNER };

	private String _type;
	private float _size;
	private int _cost, _time, _weight;
	private int _bigSprite, _floorSprite, _handSprite, _bulletSprite;
	private int _fireSound, _hitSound, _hitAnimation;
	private int _power, _displayPriority, _width, _height;
	private Vector<String> _compatibleAmmo;
	private ItemDamageType _damageType;
	private int _accuracyAuto, _accuracySnap, _accuracyAimed, _tuAuto, _tuSnap, _tuAimed;
	private int _clipSize, _accuracyMelee, _tuMelee;
	private BattleType _battleType;
	private boolean _twoHanded, _waypoint;
	private int _sizeX, _sizeY;

/**
 * Creates a blank ruleset for a certain type of item.
 * @param type String defining the type.
 */
public RuleItem(String type)
{
	_type = type;
	_size = 0.0f; 
	_cost = 0;
	_time = 24;
	_hitAnimation = 0;
	_damageType = ItemDamageType.DT_NONE;
	_accuracyAuto = 0;
	_accuracySnap = 0;
	_accuracyAimed = 0;
	_battleType = BattleType.BT_NONE;
	_twoHanded = false;

}

/**
 * Returns the language string that names
 * this item. Each item type has a unique name.
 * @return Item name.
 */
public final String getType()
{
	return _type;
}

/**
 * Returns the amount of space this item
 * takes up in a storage facility.
 * @return Storage size.
 */
public final float getSize()
{
	return _size;
}

/**
 * Changes the amount of space this item
 * takes up in a storage facility.
 * @param size Storage size.
 */
public void setSize(float size)
{
	_size = size;
}

/**
 * Returns the amount of money this item
 * costs in purchase/sale.
 * @return Cost.
 */
public final int getCost()
{
	return _cost;
}

/**
 * Changes the amount of money this item
 * costs in purchase/sale.
 * @param cost Cost.
 */
public void setCost(int cost)
{
	_cost = cost;
}

/**
 * Returns the amount of time this item
 * takes to arrive at a base.
 * @return Time in hours.
 */
public final int getTransferTime()
{
	return _time;
}

/**
 * Changes the amount of time this item
 * takes to arrive at a base.
 * @param time Time in hours.
 */
public void setTransferTime(int time)
{
	_time = time;
}

/**
 * Returns the weight of the item.
 * @return Weight in strength units.
 */
public final int getWeight()
{
	return _weight;
}

/**
 * Changes the weight of this item
 * @param weight Weight in strength units.
 */
public void setWeight(int weight)
{
	_weight = weight;
}

/**
 * Returns the reference in BIGOBS.PCK for use in inventory.
 * @return Sprite reference.
 */
public final int getBigSprite()
{
	return _bigSprite;
}

/**
 * Changes the reference in BIGOBS.PCK for use in inventory.
 * @param sprite Sprite reference.
 */
public void setBigSprite(int sprite)
{
	_bigSprite = sprite;
	_floorSprite = sprite;
}

/**
 * Returns the reference in FLOOROB.PCK for use in inventory.
 * @return Sprite reference.
 */
public final int getFloorSprite()
{
	return _floorSprite;
}

/**
 * Changes the reference in FLOOROB.PCK for use in inventory.
 * @param sprite Sprite reference.
 */
public void setFloorSprite(int sprite)
{
	_floorSprite = sprite;
}

/**
 * Returns the reference in HANDOB.PCK for use in inventory.
 * @return Sprite reference.
 */
public final int getHandSprite()
{
	return _handSprite;
}

/**
 * Changes the reference in HANDOB.PCK for use in inventory.
 * @param sprite Sprite reference.
 */
public void setHandSprite(int sprite)
{
	_handSprite = sprite;
}

/**
 * Returns whether this item is held with two hands.
 * @return Is it two-handed?
 */
public final boolean getTwoHanded()
{
	return _twoHanded;
}

/**
 * Changes whether this item is held with two hands.
 * @param equip Is it two-handed?
 */
public void setTwoHanded(boolean flag)
{
	_twoHanded = flag;
}

/**
 * Returns the item's bullet sprite reference.
 * @return Sprite reference.
 */
public final int getBulletSprite()
{
	return _bulletSprite;
}

/**
 * Changes the item's bulet sprite reference.
 * @param sprite Sprite reference.
 */
public void setBulletSprite(int sprite)
{
	_bulletSprite = sprite;
}

/**
 * Returns the item's fire sound.
 * @return Sprite reference.
 */
public final int getFireSound()
{
	return _fireSound;
}

/**
 * Changes the item's fire sound..
 * @param sprite Sprite reference.
 */
public void setFireSound(int sound)
{
	_fireSound = sound;
}

/**
 * Returns the item's hit sound.
 * @return Sprite reference.
 */
public final int getHitSound()
{
	return _hitSound;
}

/**
 * Changes the item's fire sound..
 * @param sprite Sprite reference.
 */
public void setHitSound(int sound)
{
	_hitSound = sound;
}

/**
 * Returns the item's hit sound.
 * @return Sprite reference.
 */
public final int getHitAnimation()
{
	return _hitAnimation;
}

/**
 * Changes the item's fire sound.
 * @param sprite Sprite reference.
 */
public void setHitAnimation(int animation)
{
	_hitAnimation = animation;
}

/**
 * Returns the item's power.
 * @return power Teh powah.
 */
public final int getPower()
{
	return _power;
}

/**
 * Sets the item's power.
 * @param power the item's power.
 */
public void setPower(int power)
{
	_power = power;
}

/**
 * Returns the item's accuracy for snapshots.
 * @return Accuracy the item's accuracy for snapshots.
 */
public final int getAccuracySnap()
{
	return _accuracySnap;
}

/**
 * Sets the item's accuracy for snapshots.
 * @param accuracy item's accuracy for snapshots.
 */
public void setAccuracySnap(int accuracy)
{
	_accuracySnap = accuracy;
}

/**
 * Returns the item's accuracy for autoshots.
 * @return Accuracy the item's accuracy for autoshots.
 */
public final int getAccuracyAuto()
{
	return _accuracyAuto;
}

/**
 * Sets the item's accuracy for autoshots.
 * @param accuracy item's accuracy for autoshots.
 */
public void setAccuracyAuto(int accuracy)
{
	_accuracyAuto = accuracy;
}

/**
 * Returns the item's accuracy for aimed shots.
 * @return Accuracy the item's accuracy for aimed sthos.
 */
public final int getAccuracyAimed()
{
	return _accuracyAimed;
}

/**
 * Sets the item's accuracy for aimed shot.
 * @param accuracy item's accuracy for aimed shots.
 */
public void setAccuracyAimed(int accuracy)
{
	_accuracyAimed = accuracy;
}

/**
 * Returns the item's accuracy for snapshots.
 * @return Accuracy the item's accuracy for snapshots.
 */
public final int getTUSnap()
{
	return _tuSnap;
}

/**
 * Sets the item's accuracy for snapshots.
 * @param accuracy item's accuracy for snapshots.
 */
public void setTUSnap(int tu)
{
	_tuSnap = tu;
}

/**
 * Returns the item's accuracy for autoshots.
 * @return Accuracy the item's accuracy for autoshots.
 */
public final int getTUAuto()
{
	return _tuAuto;
}

/**
 * Sets the item's accuracy for autoshots.
 * @param accuracy item's accuracy for autoshots.
 */
public void setTUAuto(int tu)
{
	_tuAuto = tu;
}

/**
 * Returns the item's accuracy for aimed shots.
 * @return Accuracy the item's accuracy for aimed sthos.
 */
public final int getTUAimed()
{
	return _tuAimed;
}

/**
 * Sets the item's accuracy for aimed shot.
 * @param accuracy item's accuracy for aimed shots.
 */
public void setTUAimed(int tu)
{
	_tuAimed = tu;
}

/**
 * Returns a list of compatible ammo.
 * @return pointer to a list of compatible ammo.
 */
public Vector<String> getCompatibleAmmo()
{
	return _compatibleAmmo;
}

/**
 * Returns the item's damage type.
 * @return damagetype the item's damage type.
 */
public final ItemDamageType getDamageType()
{
	return _damageType;
}

/**
 * Sets the item's damage type.
 * @param damagetype the item's damage type.
 */
public void setDamageType(ItemDamageType damageType)
{
	_damageType = damageType;
}

/**
 * Returns the item's type.
 * @return type the item's type.
 */
public final BattleType getBattleType()
{
	return _battleType;
}

/**
 * Sets the item's type.
 * @param type the item's type.
 */
public void setBattleType(BattleType type)
{
	_battleType = type;
}

/**
 * Returns the item's X size.
 * @return the item's X size.
 */
public final int getSizeX()
{
	return _sizeX;
}

/**
 * Sets the item's size X.
 * @param size the item's size X.
 */
public void setSizeX(int size)
{
	_sizeX = size;
}

/**
 * Returns the item's Y size.
 * @return the item's Y size.
 */
public final int getSizeY()
{
	return _sizeY;
}

/**
 * Sets the item's size Y.
 * @param size the item's size Y.
 */
public void setSizeY(int size)
{
	_sizeY = size;
}

/**
 * Returns the item's Y size.
 * @return the item's Y size.
 */
public final int getClipSize()
{
	return _clipSize;
}

/**
 * Sets the item's size Y.
 * @param size the item's size Y.
 */
public void setClipSize(int size)
{
	_clipSize = size;
}

}
