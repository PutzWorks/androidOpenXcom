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

public class MovingTarget extends Target
{
	protected Target _dest;
	protected double _speedLon;
	protected double _speedLat;
	protected int _speed;

/**
 * Initializes a moving target with blank coordinates.
 */
public MovingTarget()
{
	super();
	_dest = new Target(); 
	_speedLon = 0.0;
	_speedLat = 0.0;
	_speed = 0;

}

/**
 * Loads the moving target from a YAML file.
 * @param node YAML node.
 */
public void load(final YAML.Node node)
{
	super.load(node);
	node["speedLon"] >> _speedLon;
	node["speedLat"] >> _speedLat;
	node["speed"] >> _speed;
}

/**
 * Saves the moving target to a YAML file.
 * @param out YAML emitter.
 */
public final void save(YAML.Emitter out)
{
	super.save(out);
	if (_dest != 0)
	{
		out << YAML.Key << "dest" << YAML.Value;
		_dest.saveId(out);
	}
	out << YAML.Key << "speedLon" << YAML.Value << _speedLon;
	out << YAML.Key << "speedLat" << YAML.Value << _speedLat;
	out << YAML.Key << "speed" << YAML.Value << _speed;
}

/**
 * Returns the destination the moving target is heading to.
 * @return Pointer to destination.
 */
public final Target getDestination()
{
	return final _dest;
}

/**
 * Changes the destination the moving target is heading to.
 * @param dest Pointer to destination.
 */
public void setDestination(Target dest)
{
	// Remove moving target from old destination's followers
	if (_dest != 0)
	{
		for (Vector<Target*>.iterator i = _dest.getFollowers().begin(); i != _dest.getFollowers().end(); ++i)
		{
			if ((*i) == this)
			{
				_dest.getFollowers().erase(i);
				break;
			}
		}
	}
	_dest = dest;
	// Add moving target to new destination's followers
	if (_dest != 0)
	{
		_dest.getFollowers().add(this);
	}
	calculateSpeed();
}

/**
 * Returns the speed of the moving super.
 * @return Speed in knots.
 */
public final int getSpeed()
{
	return _speed;
}

/**
 * Changes the speed of the moving super.
 * @param speed Speed in knots.
 */
public void setSpeed(int speed)
{
	_speed = speed;
	calculateSpeed();
}

/**
 * Converts the speed from the standard knots (nautical miles per hour),
 * and converts it into radians per 5 in-game seconds.
 * @return Speed in radians.
 */
public final double getRadianSpeed()
{
	// Each nautical mile is 1/60th of a degree.
	// Each hour contains 300 5-seconds.
	return _speed * (1 / 60.0) * (M_PI / 180) / 300.0;
}

/**
 * Returns the distance between this moving target and any other
 * target on the globe, accounting for the wrap-around.
 * @param target Pointer to other super.
 * @param dLon Returned longitude difference.
 * @param dLat Returned latitude difference.
 * @returns Distance in radian.
 */
public final double getDistance(Target target, double dLon, double dLat)
{
	double minLength = 2*M_PI, lat = super.getLatitude();
	for (double lon = super.getLongitude() - 2*M_PI; lon <= super.getLongitude() + 2*M_PI; lon += 2*M_PI)
	{
		double dx = lon - _lon;
		double dy = lat - _lat;
		double length = sqrt(dx * dx + dy * dy);
		if (length < minLength)
		{
			minLength = length;
			*dLon = dx;
			*dLat = dy;
		}
	}
	return minLength;
}

/**
 * Calculates the speed vector for the moving target
 * based on the current raw speed and destination.
 */
public void calculateSpeed()
{
	if (_dest != 0)
	{
		double dLon, dLat;
		double length = getDistance(_dest, &dLon, &dLat);
		_speedLon = dLon / length * getRadianSpeed();
		_speedLat = dLat / length * getRadianSpeed();
	}
	else
	{
		_speedLon = 0;
		_speedLat = 0;
	}
}

/**
 * Checks if the moving target has finished its route by checking
 * if it has exceeded the destination position based on the speed vector.
 * @return True if it has, False otherwise.
 */
public final boolean finishedRoute()
{
	return (((_speedLon > 0 && _lon >= _dest.getLongitude()) || (_speedLon < 0 && _lon <= _dest.getLongitude()) || _speedLon == 0) &&
			((_speedLat > 0 && _lat >= _dest.getLatitude()) || (_speedLat < 0 && _lat <= _dest.getLatitude()) || _speedLat == 0));
}

/**
 * Checks if the moving target has reached its destination.
 * @return True if it has, False otherwise.
 */
public final boolean reachedDestination()
{
	if (_dest == 0)
	{
		return false;
	}
	return (_lon == _dest.getLongitude() && _lat == _dest.getLatitude());
}

}
