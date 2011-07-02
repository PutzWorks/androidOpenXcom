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
package putzworks.openXcom.Engine;

import java.util.Random;

public class RNG
{
	private static int _seed;
	static double y2;
	static int use_last = 0;
	static Random generator = new Random();
	static final int RAND_MAX = 2147483640;

/**
 * Seeds the random generator with a new number.
 * Defaults to the current time if none is set.
 * @param seed New seed.
 */
public static void init(int seed)
{
	if (seed == -1)
	{
		_seed = (int)System.currentTimeMillis();
	}
	else
	{
		_seed = seed;
	}
	generator = new Random(_seed);
}

/**
 * Returns the last seed used by the generator.
 * @return Generator seed.
 */
public int getSeed()
{
	return _seed;
}

/**
 * Generates a random integer number within a certain range.
 * @param min Minimum number.
 * @param max Maximum number.
 * @return Generated number.
 */
public static int generate(int min, int max)
{
	_seed = generator.nextInt();
	return (_seed % (max - min + 1) + min);
}

/**
 * Generates a random decimal number within a certain range.
 * @param min Minimum number.
 * @param max Maximum number.
 * @return Generated number.
 */
public static double generate(double min, double max)
{
	_seed = generator.nextInt();
	return (_seed * (max - min) / RAND_MAX + min);
}

/**
 * Normal random variate generator
 * @param m mean
 * @param s standard deviation
 * @return normally distributed value.
 */
public static double boxMuller(double m, double s)	
{
	double x1, x2, w, y1;

	if (use_last != 0)		        /* use value from previous call */
	{
		y1 = y2;
		use_last = 0;
	}
	else
	{
		do {
			x1 = 2.0 * generate(0.0, 1.0) - 1.0;
			x2 = 2.0 * generate(0.0, 1.0) - 1.0;
			w = x1 * x1 + x2 * x2;
		} while ( w >= 1.0 );

		w = Math.sqrt( (-2.0 * Math.log( w ) ) / w );
		y1 = x1 * w;
		y2 = x2 * w;
		use_last = 1;
	}

	return( m + y1 * s );
}

}
