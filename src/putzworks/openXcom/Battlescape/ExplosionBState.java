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

import putzworks.openXcom.Engine.RNG;
import putzworks.openXcom.Ruleset.RuleItem;
import putzworks.openXcom.Savegame.BattleItem;
import putzworks.openXcom.Savegame.BattleUnit;
import putzworks.openXcom.Savegame.SavedBattleGame;

public class ExplosionBState extends BattleState
{
	private BattleUnit _unit;
	private Position _center;
	private BattleItem _item;

/**
 * Sets up an ExplosionBState.
 */
public ExplosionBState(BattlescapeState parent, Position center, BattleItem item)

{
	super(parent);
	_center = center;
	_item = item;

}

/**
 * init explosion :
 * - create an explosion sprite
 * - add it to the list of explosion sprites(on map)
 * - explosion sound
 */
public void init()
{
	_unit = _parent.getGame().getSavedGame().getBattleGame().getSelectedUnit();
	if (_item == null || _item.getRules().getHitAnimation() == 0)
	{
		_parent.setStateInterval(BattlescapeState.DEFAULT_ANIM_SPEED);
		// create 9 explosions
		for (int i = -32; i < 48; i+=32)
			for (int j = -32; j < 48; j+=32)
			{
				Position p = _center;
				p.x += i; p.y += j;
				Explosion explosion = new Explosion(p, RNG.generate(0,6), true);
				// add the explosion on the map
				_parent.getMap().getExplosions().add(explosion);
			}
		// explosion sound
		_parent.getGame().getResourcePack().getSoundSet("GEO.CAT").getSound(10).play();
	}
	else
	{
		_parent.setStateInterval(BattlescapeState.DEFAULT_ANIM_SPEED/2);
		// create a bulet hit
		Explosion explosion = new Explosion(_center, _item.getRules().getHitAnimation(), false);
		// add the explosion on the map
		_parent.getMap().getExplosions().add(explosion);
		// bullet hit sound
		_parent.getGame().getResourcePack().getSoundSet("BATTLE.CAT").getSound(_item.getRules().getHitSound()).play();
	}
}

/*
 * Animate explosion sprites. If their animation is finished remove them from the list.
 * If the list is empty, this states is finished.
 */
public void think()
{
	for (Explosion i: _parent.getMap().getExplosions())
	{
		++inext;
		if(!(i).animate())
		{
			_parent.getMap().getExplosions().erase((i));
			if (_parent.getMap().getExplosions().isEmpty())
			{
				SavedBattleGame save = _parent.getGame().getSavedGame().getBattleGame();
				// after the animation is done, the real explosion takes place
				save.getTerrainModifier().explode(_center, _item.getRules().getPower(), _item.getRules().getDamageType(), 100, save.getSelectedUnit());

				// now check for new casualties
				for (BattleUnit j: save.getUnits())
				{
					if ((j).getHealth() == 0 && (j).getStatus() != BattleUnit.UnitStatus.STATUS_DEAD)
					{
						_parent.statePushNext(new UnitFallBState(_parent, (j), _item.getRules().getDamageType() == RuleItem.ItemDamageType.DT_HE));
					}
				}

				// if this explosion was caused by a unit shooting, now it's the time to put the gun down
				if (_unit != null && !_unit.isOut())
				{
					_unit.aim(false);
				}
				_parent.getMap().cacheUnits();

				_parent.popState();
				return;
			}
		}
	}
}

/*
 * Explosion cannot be cancelled.
 */
public void cancel()
{
}

/*
 * Get the action result. Returns error messages or an empty string when everything went fine.
 * @return returnmessage Empty when everything is fine.
 */
public String getResult()
{
	return _result;
}

}
