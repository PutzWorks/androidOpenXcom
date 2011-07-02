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

import putzworks.openXcom.Savegame.BattleUnit;
import putzworks.openXcom.Savegame.Tile;

public class UnitWalkBState extends BattleState
{
	private Position _target;
	private BattleUnit _unit;
	private Pathfinding _pf;
	private TerrainModifier _terrain;

/**
 * Sets up an UnitWalkBState.
 */
public UnitWalkBState(BattlescapeState parent)
{
	super(parent);
}

public void init()
{
	_parent.setStateInterval(BattlescapeState.DEFAULT_WALK_SPEED);
	_unit = _parent.getGame().getSavedGame().getBattleGame().getSelectedUnit();
	_pf = _parent.getGame().getSavedGame().getBattleGame().getPathfinding();
	_terrain = _parent.getGame().getSavedGame().getBattleGame().getTerrainModifier();
	_target = _parent.getAction().target;
	_pf.calculate(_unit, _target);
}

public void think()
{
	// during a walking cycle we make step sounds
	if (_unit.getStatus() == BattleUnit.UnitStatus.STATUS_WALKING)
	{

		// play footstep sound 1
		if (_unit.getWalkingPhase() == 3)
		{
			Tile tile = _parent.getGame().getSavedGame().getBattleGame().getTile(_unit.getPosition());
			if (tile.getFootstepSound() != 0)
			{
				_parent.getGame().getResourcePack().getSoundSet("BATTLE.CAT").getSound(22 + (tile.getFootstepSound()*2)).play();
			}
		}
		// play footstep sound 2
		if (_unit.getWalkingPhase() == 7)
		{
			Tile tile = _parent.getGame().getSavedGame().getBattleGame().getTile(_unit.getPosition());
			if (tile.getFootstepSound() != 0)
			{
				_parent.getGame().getResourcePack().getSoundSet("BATTLE.CAT").getSound(23 + (tile.getFootstepSound()*2)).play();
			}
		}

		_unit.keepWalking(); // advances the phase

		// unit moved from one tile to the other, update the tiles
		if (_unit.getPosition() != _unit.getLastPosition())
		{
			_parent.getGame().getSavedGame().getBattleGame().getTile(_unit.getLastPosition()).setUnit(null);
			_parent.getGame().getSavedGame().getBattleGame().getTile(_unit.getPosition()).setUnit(_unit);
			// if the unit changed level, camera changes level with
			_parent.getMap().setViewHeight(_unit.getPosition().z);
		}

		// is the walking cycle finished?
		if (_unit.getStatus() == BattleUnit.UnitStatus.STATUS_STANDING)
		{
			_terrain.calculateFOV(_unit);
			_terrain.calculateUnitLighting();
		}
		else
		{
			// make sure the unit sprites are up to date
			_parent.getMap().cacheUnits();
		}
	}

	// we are just standing around, shouldn't we be walking?
	if (_unit.getStatus() == BattleUnit.UnitStatus.STATUS_STANDING)
	{
		int dir = _pf.getStartDirection();
		if (dir != -1)
		{
			Position destination = new Position();
			int tu = _pf.getTUCost(_unit.getPosition(), dir, destination, _unit);

			if (tu > _unit.getTimeUnits())
			{
				_result = "STR_NOT_ENOUGH_TIME_UNITS";
				_pf.abortPath();
				return;
			}

			// we are looking in the wrong way, turn first
			// we are not using the turn state, because turning during walking costs no tu
			if (dir != _unit.getDirection()) 
			{
				_unit.lookAt(dir);
				return;
			}

			// now open doors (if any)
			int door = _terrain.unitOpensDoor(_unit);
			if (door == 3)
			{
				return; // don't start walking yet, wait for the ufo door to open
			}
			if (door == 0)
			{
				_parent.getGame().getResourcePack().getSoundSet("BATTLE.CAT").getSound(3).play(); // normal door
			}
			if (door == 1)
			{
				_parent.getGame().getResourcePack().getSoundSet("BATTLE.CAT").getSound(20).play(); // ufo door
				return; // don't start walking yet, wait for the ufo door to open
			}

			// now start moving
			dir = _pf.dequeuePath();
			if (_unit.spendTimeUnits(tu, _parent.getGame().getSavedGame().getBattleGame().getDebugMode()))
			{
				_unit.startWalking(dir, destination);
			}
			else
			{
				_result = "STR_NOT_ENOUGH_TIME_UNITS";
				_parent.popState();
			}
			// make sure the unit sprites are up to date
			_parent.getMap().cacheUnits();
		}
		else
		{
			postWalkingProcedures();
			return;
		}
	}

	// turning during walking costs no tu
	if (_unit.getStatus() == BattleUnit.UnitStatus.STATUS_TURNING)
	{
		_unit.turn();
		_terrain.calculateFOV(_unit);
		// make sure the unit sprites are up to date
		_parent.getMap().cacheUnits();
	}
}

/*
 * Abort unit walking.
 */
public void cancel()
{
	_pf.abortPath();
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
 * Handle some calculations when the walking finished.
 */
public void postWalkingProcedures()
{
	_terrain.calculateUnitLighting();
	_terrain.calculateFOV(_unit);
	_parent.getMap().cacheUnits();
	_parent.popState();
}

}
