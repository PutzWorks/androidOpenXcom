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
import putzworks.openXcom.Savegame.BattleUnit;

public class UnitTurnBState extends BattleState
{
		BattleUnit _unit;

/**
 * Sets up an UnitTurnBState.
 */
public UnitTurnBState(BattlescapeState parent) 
{
	super(parent);
}

public void init()
{
	_parent.setStateInterval(BattlescapeState.DEFAULT_WALK_SPEED);
	_unit = _parent.getGame().getSavedGame().getBattleGame().getSelectedUnit();
	_unit.lookAt(_parent.getAction().target);
	if (_unit.getStatus() != BattleUnit.UnitStatus.STATUS_TURNING)
	{
		// try to open a door
		int door = _parent.getGame().getSavedGame().getBattleGame().getTerrainModifier().unitOpensDoor(_unit);
		if (door == 0)
		{
			_parent.getGame().getResourcePack().getSoundSet("BATTLE.CAT").getSound(3).play(); // normal door
		}
		if (door == 1)
		{
			_parent.getGame().getResourcePack().getSoundSet("BATTLE.CAT").getSound(RNG.generate(20,21)).play(); // ufo door
		}
		_parent.popState();
	}
}

public void think()
{
	if (_unit.spendTimeUnits(1, _parent.getGame().getSavedGame().getBattleGame().getDebugMode()))
	{
		_unit.turn();
		_parent.getGame().getSavedGame().getBattleGame().getTerrainModifier().calculateFOV(_unit);
		_parent.getMap().cacheUnits();
		if (_unit.getStatus() == BattleUnit.UnitStatus.STATUS_STANDING)
		{
			_parent.popState();
		}
	}
	else
	{
		_unit.abortTurn();
		_result = "STR_NOT_ENOUGH_TIME_UNITS";
		_parent.popState();
	}
}

/*
 * Unit turning cannot be cancelled.
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

}
