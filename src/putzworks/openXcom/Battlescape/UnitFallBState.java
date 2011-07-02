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
import putzworks.openXcom.Savegame.BattleItem;
import putzworks.openXcom.Savegame.BattleUnit;
import putzworks.openXcom.Savegame.Soldier;

public class UnitFallBState extends BattleState
{
	private BattleUnit _unit;
	private boolean _instakill;

/**
 * Sets up an UnitFallBState.
 */
public UnitFallBState(BattlescapeState parent, BattleUnit unit, boolean instakill)
{
	super(parent);
	_unit = unit;
	_instakill = instakill;

}

public void init()
{
	if (_instakill)
	{
		_unit.startFalling();
		while (_unit.getStatus() == BattleUnit.UnitStatus.STATUS_FALLING)
		{
			_unit.keepFalling();
		}
	}
	else
	{
		_parent.setStateInterval(BattlescapeState.DEFAULT_ANIM_SPEED);
		_unit.lookAt(3);
	}
	if (_unit.getHealth() == 0)
	{
		// soldiers have 3 screams depending on gender
		Soldier s = (Soldier)(_unit.getUnit());
		if (s != null)
		{
			if (s.getGender() == Soldier.SoldierGender.GENDER_MALE)
			{
				_parent.getGame().getResourcePack().getSoundSet("BATTLE.CAT").getSound(RNG.generate(41,43)).play();
			}
			else
			{
				_parent.getGame().getResourcePack().getSoundSet("BATTLE.CAT").getSound(RNG.generate(44,46)).play();
			}
		}
		else
		{
			// todo get death sound from rulealien
			_parent.getGame().getResourcePack().getSoundSet("BATTLE.CAT").getSound(10).play();
		}
	}

	think();
}

public void think()
{
	if (_unit.getStatus() == BattleUnit.UnitStatus.STATUS_TURNING)
	{
		_unit.turn();
	}
	else if (_unit.getStatus() == BattleUnit.UnitStatus.STATUS_STANDING)
	{
		_unit.startFalling();
	}
	else if (_unit.getStatus() == BattleUnit.UnitStatus.STATUS_FALLING)
	{
		_unit.keepFalling();
	}
	
	if (_unit.isOut())
	{
		_unit.keepFalling();
		TerrainModifier terrain = _parent.getGame().getSavedGame().getBattleGame().getTerrainModifier();
		convertUnitToCorpse(_unit, terrain);
		terrain.calculateUnitLighting();
		_parent.getMap().cacheTileSprites();
		_parent.popState();
	}

	_parent.getMap().cacheUnits();
}

/*
 * Unit falling cannot be cancelled.
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

public void convertUnitToCorpse(BattleUnit unit, TerrainModifier terrain)
{
	terrain.spawnItem(_unit.getPosition(), new BattleItem(_parent.getGame().getRuleset().getItem(_unit.getUnit().getArmor().getCorpseItem())));
}

}
