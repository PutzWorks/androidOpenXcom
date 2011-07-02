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
package putzworks.openXcom.Battlescape;

import putzworks.openXcom.Battlescape.BattlescapeState.BattleAction;
import putzworks.openXcom.Battlescape.BattlescapeState.BattleActionType;
import putzworks.openXcom.Ruleset.RuleItem;
import putzworks.openXcom.Savegame.BattleItem;
import putzworks.openXcom.Savegame.BattleUnit;

public class ProjectileFlyBState extends BattleState
{
	private BattleUnit _unit;
	private BattleItem _ammo;

/**
 * Sets up an ProjectileFlyBState.
 */
public ProjectileFlyBState(BattlescapeState parent)
{
	super(parent);
}

/**
 * init sequence:
 * - create a projectile sprite & add it to the map
 * - calculate it's trajectory
 */
public void init()
{
	int baseAcc;
	BattleItem weapon = _parent.getAction().weapon;
	BattleItem projectileItem = null;

	_parent.setStateInterval(BattlescapeState.DEFAULT_BULLET_SPEED);
	_unit = _parent.getAction().actor;
	_ammo = weapon.getAmmoItem();
	if (_unit.isOut())
	{
		// something went wrong
		_parent.popState();
		return;
	}
	if (_parent.getAction().type != BattleActionType.BA_THROW)
	{
		if (_ammo == null)
		{
			_result = "STR_NO_AMMUNITION_LOADED";
			_parent.popState();
			return;
		}
		if (_ammo.getAmmoQuantity() == 0)
		{
			_result = "STR_NO_ROUNDS_LEFT";
			_parent.popState();
			return;
		}
	}
	// action specific initialisation
	switch (_parent.getAction().type)
	{
	case BA_AUTOSHOT:
		baseAcc = weapon.getRules().getAccuracyAuto();
		//_parent.setStateInterval(DEFAULT_BULLET_SPEED/2); // a little faster
		break;
	case BA_SNAPSHOT:
		baseAcc = weapon.getRules().getAccuracySnap();
		break;
	case BA_AIMEDSHOT:
		baseAcc = weapon.getRules().getAccuracyAimed();
		//_parent.setStateInterval(DEFAULT_BULLET_SPEED*1.5); // a little slower
		break;
	case BA_THROW:
		if (!validThrowRange())
		{
			// out of range
			_result = "STR_OUT_OF_RANGE";
			_parent.popState();
			return;
		}
		baseAcc = (int)(_unit.getThrowingAccuracy()*100.0);
		projectileItem = weapon;
		break;
    default:
        baseAcc = 0;
	}

	// create a new projectile
	Projectile projectile = new Projectile(_parent.getGame().getResourcePack(),
									_parent.getGame().getSavedGame().getBattleGame(),
									_unit.getPosition(),
									_parent.getAction().target,
									weapon.getRules().getBulletSprite(),
									projectileItem
									);
	// add the projectile on the map
	_parent.getMap().setProjectile(projectile);
	// let it calculate a trajectory
	if (_parent.getAction().type == BattleActionType.BA_THROW)
	{
		if (projectile.calculateThrow(baseAcc))
		{
			projectileItem.setOwner(null);
			_unit.setCached(false);
			_parent.getMap().cacheUnits();
			_parent.getGame().getResourcePack().getSoundSet("BATTLE.CAT").getSound(39).play();
		}
		else
		{
			// unable to throw here
			projectile = null;
			_parent.getMap().setProjectile(null);
			_result = "STR_UNABLE_TO_THROW_HERE";
			_parent.popState();
		}
	}
	else
	{
		if (projectile.calculateTrajectory(_unit.getFiringAccuracy(baseAcc)))
		{
				// set the soldier in an aiming position
				_unit.aim(true);
				_parent.getMap().cacheUnits();
				// and we have a lift-off
				_parent.getGame().getResourcePack().getSoundSet("BATTLE.CAT").getSound(weapon.getRules().getFireSound()).play();
				if (!_parent.getGame().getSavedGame().getBattleGame().getDebugMode() && _ammo.spendBullet() == false)
				{
					//_parent.getGame().getSavedGame().getBattleGame().getItems().erase(_parent.getSelectedItem().getAmmoItem());
					weapon.setAmmoItem(null);
				}
		}
		else
		{
			// no line of fire
			projectile = null;
			_parent.getMap().setProjectile(null);
			_result = "STR_NO_LINE_OF_FIRE";
			_parent.popState();
		}
	}
}

/**
 * Animates the projectile (move to the next point in it's trajectory).
 * If the animation is finished the projectile sprite is removed from the map.
 * And this state is finished.
 */
public void think()
{
	if(!_parent.getMap().getProjectile().move())
	{
		// impact !
		if (_parent.getAction().type == BattleActionType.BA_THROW)
		{
			Position pos = _parent.getMap().getProjectile().getPosition(-1);
			pos.x /= 16;
			pos.y /= 16;
			pos.z /= 24;
			_parent.getGame().getSavedGame().getBattleGame().getTerrainModifier().spawnItem(pos, _parent.getMap().getProjectile().getItem());
			_parent.getGame().getResourcePack().getSoundSet("BATTLE.CAT").getSound(38).play();
		}
		else
		{
			int offset = 0;
			// explosions impact not inside the voxel but one step back
			if (_ammo != null && (
				_ammo.getRules().getDamageType() == RuleItem.ItemDamageType.DT_HE ||
				_ammo.getRules().getDamageType() == RuleItem.ItemDamageType.DT_IN))
			{
				offset = -1;
			}
			_parent.statePushNext(new ExplosionBState(_parent, _parent.getMap().getProjectile().getPosition(offset), _ammo));
		}

		_parent.getMap().setProjectile((Projectile)null);
		_parent.getMap().setProjectile(null);
		_parent.popState();
	}
}

/*
 * Flying projectiles cannot be cancelled.
 */
public void cancel()
{
}

/*
 * Get the action result. Returns error messages or an empty string when everything went fine.
 * @return returnmessage Empty when everything is fine.
 */
public final String getResult()
{
	return _result;
}

/*
 * Validate the throwing range. 
 * @return true when range is valid.
 */
public boolean validThrowRange()
{
	// Throwing Distance roughly = 2.5 � Strength / Weight
	// note that all coordinates and thus also distances below are in number of tiles (not in voxels).
	double maxDistance = 2.5 * _parent.getAction().actor.getUnit().getStrength() / _parent.getAction().weapon.getRules().getWeight();
	int xdiff = _parent.getAction().target.x - _unit.getPosition().x;
	int ydiff = _parent.getAction().target.y - _unit.getPosition().y;
	int zdiff = _parent.getAction().target.z - _unit.getPosition().z;
	double realDistance = Math.sqrt((double)(xdiff*xdiff)+(double)(ydiff*ydiff));

	// throwing off a building of 1 level lets you throw 2 tiles further than normal range,
	// throwing up the roof of this building lets your throw 2 tiles less further
	realDistance += zdiff*2;

	return realDistance < maxDistance;
}

}