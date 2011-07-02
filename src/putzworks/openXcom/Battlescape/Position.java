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

public class Position
{
	public int x, y, z;

	/// Null position constructor.
	public Position() 
	{
		x = 0;
		y = 0; 
		z = 0;
	}
	/// X Y Z position constructor.
	public Position(int x_, int y_, int z_)
	{
		x = x_;
		y = y_;
		z = z_;
	}
	/// Copy constructor.
	public Position(final Position pos)
	{
		x = pos.x;
		y = pos.y;
		z = pos.z;
	}

	public Position set(final Position pos) { x = pos.x; y = pos.y; z = pos.z; return this;}
	//Position& operator=(const Position& pos) { x = pos.x; y = pos.y; z = pos.z; return *this; }

	public Position plus(final Position pos) { return  new Position(x + pos.x, y + pos.y, z + pos.z); }
	//Position operator+(const Position& pos) const { return Position(x + pos.x, y + pos.y, z + pos.z); }
	
	public Position plusEquals(final Position pos){ x+=pos.x; y+=pos.y; z+=pos.z; return this; }
	//Position& operator+=(const Position& pos) { x+=pos.x; y+=pos.y; z+=pos.z; return *this; }

	public Position minus(final Position pos){return new Position(x - pos.x, y - pos.y, z - pos.z); }
	//Position operator-(const Position& pos) const { return Position(x - pos.x, y - pos.y, z - pos.z); }
	
	public Position minusEquals(final Position pos){ x-=pos.x; y-=pos.y; z-=pos.z; return this; }
	//Position& operator-=(const Position& pos) { x-=pos.x; y-=pos.y; z-=pos.z; return *this; }

	public Position times(final Position pos){ return new Position(x * pos.x, y * pos.y, z * pos.z); }
	//Position operator*(const Position& pos) const { return Position(x * pos.x, y * pos.y, z * pos.z); }
	
	public Position timesEquals(final Position pos){ x*=pos.x; y*=pos.y; z*=pos.z; return this;  }
	//Position& operator*=(const Position& pos) { x*=pos.x; y*=pos.y; z*=pos.z; return *this; }
	
	public Position times(final int v){ return new Position(x * v, y * v, z * v); }
	//Position operator*(const int v) const { return Position(x * v, y * v, z * v); }
	
	public Position timesEquals(final int v){ x*=v; y*=v; z*=v; return this;  }
	//Position& operator*=(const int v) { x*=v; y*=v; z*=v; return *this; }

	public boolean equals(final Position pos){
		return x == pos.x && y == pos.y && z == pos.z;
	}
	/*/// == operator
    boolean operator== (const Position& pos) const
	{
		return x == pos.x && y == pos.y && z == pos.z;
	}*/
	
	public boolean notEquals(final Position pos){
		return x != pos.x || y != pos.y || z != pos.z;
	}
	
	/*// != operator
    boolean operator!= (const Position& pos) const
	{
		return x != pos.x || y != pos.y || z != pos.z;
	}*/
}
