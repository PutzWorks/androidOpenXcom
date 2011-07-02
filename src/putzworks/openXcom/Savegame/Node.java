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

import java.util.EnumSet;

import putzworks.openXcom.Battlescape.Position;
import putzworks.openXcom.Savegame.Soldier.SoldierRank;

public class Node
{
	public enum NodeRank{SCOUT(0), XCOM(1), SOLDIER(2), NAVIGATOR(3), LEADER(4), ENGINEER(5), MISC1(6), MEDIC(7), MISC2(8);
	private int id;
	private NodeRank(int i){
		this.id = i;
	}
     public static NodeRank get(int code) { 
    	 for(NodeRank s : EnumSet.allOf(NodeRank.class)){
               if (s.id == code){return s;}
    	 }
    	 return null;
     }
	}

	private int _id;
	private Position _pos;
	private int _segment;
	private NodeLink[] _nodeLinks = new NodeLink[5];
	private int _type;
	private int _rank;
	private int _flags;
	private int _reserved;
	private int _priority;

/**
 * Initializes a Node.
 * @param id
 * @param pos
 * @param segment
 * @param type
 * @param rank
 * @param flags
 * @param reserved
 * @param priority
 */
public Node(int id, Position pos, int segment, int type, int rank, int flags, int reserved, int priority)
{
	_id = id;
	_pos = pos;
	_segment = segment;
	_type = type;
	_rank = rank;
	_flags = flags;
	_reserved = reserved; 
	_priority = priority;

	_nodeLinks[0] = null;
	_nodeLinks[1] = null;
	_nodeLinks[2] = null;
	_nodeLinks[3] = null;
	_nodeLinks[4] = null;
}

/**
 * clean up node.
 */
public void clearNode()
{
	 _nodeLinks[0] = null;
	 _nodeLinks[1] = null;
	 _nodeLinks[2] = null;
	 _nodeLinks[3] = null;
	 _nodeLinks[4] = null;
}

/**
 * Assign a node link to this node.
 * @param link pointer to the link
 * @param index 0-4
 */
public void assignNodeLink(NodeLink link, int index)
{
	_nodeLinks[index] = link;
}

/**
 * Get the rank of units that can spawn on this node.
 * @return noderank
 */
public final NodeRank getRank()
{
	return NodeRank.get(_rank);
}

/**
 * Get the priority of this spawnpoint.
 * @return priority
 */
public final int getPriority()
{
	return _priority;
}

/**
 * Gets the Node's position.
 * @return position
 */
public final Position getPosition()
{
	return _pos;
}

}
