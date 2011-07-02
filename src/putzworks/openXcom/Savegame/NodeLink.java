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

public class NodeLink
{
	private int _connectedNodeID;
	private int _distance;
	private int _travelType;
	private Node _connectedNode;

/**
 * Initializes a NodeLink.
 * @param connectedNodeID
 * @param distance
 * @param travelType
 */
public NodeLink(int connectedNodeID, int distance, int travelType)
{
	_connectedNodeID = connectedNodeID;
	_distance = distance;
	_travelType = travelType;

}

}
