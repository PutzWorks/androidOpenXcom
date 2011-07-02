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

public abstract class BattleState
{
	protected BattlescapeState _parent;
	protected String _result;

/**
 * Sets up a BattleState.
 * @param parent pointer to the parent state.
 */
public BattleState(BattlescapeState parent)
{
	_parent = parent;
	_result = "";

}

/**
 * Start the current BattleState.
 */
public void init()
{

}


/**
 * Cancel the current BattleState.
 */
public void cancel()
{

}

/**
 * Runs any code the state needs to keep updating every
 * game cycle.
 */
public void think()
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
