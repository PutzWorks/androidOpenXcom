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

public class PathfindingNode
{
	private Position _pos;
	private boolean _checked;
	private int _tuCost, _stepsNum;
	private PathfindingNode _prevNode;
	private int _prevDir;

/**
 * Sets up a PathfindingNode.
 * @param pos Position.
 */
public PathfindingNode(Position pos)
{
	_pos = pos;

}

/**
* Get the node position
* @return node position
*/
public Position getPosition()
{
	return _pos;
}
/**
 * Reset node.
 */
public void reset()
{
	_checked = false;
}
/**
* Check node. The pathfinding marks every node as checked, storing some additional info.
* @param tuCost
* @param stepsNum
* @param prevNode
* @param prevDir
*/
public void check(int tuCost, int stepsNum, PathfindingNode prevNode, int prevDir)
{
	_checked = true;
	_tuCost = tuCost;
	_stepsNum = stepsNum;
	_prevNode = prevNode;
	_prevDir = prevDir;
}

/**
* Is checked?
* @return boolean 
*/
public boolean isChecked()
{
	return _checked;
}

/** 
 * Get TU cost.
 * @return cost
 */
public int getTUCost()
{
	return _tuCost;
}

/**
 * Get steps num
 * @return steps num
 */
public int getStepsNum()
{
	return _stepsNum;
}

/**
 * Get previous node
 * @return pointer to previous node
 */
public PathfindingNode getPrevNode()
{
	return _prevNode;
}

/**
 * Get previous walking direction how we got on this node.
 * @return previous vector
 */
public int getPrevDir()
{
	return _prevDir;
}

}
