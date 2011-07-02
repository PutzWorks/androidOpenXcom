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
package putzworks.openXcom.Geoscape;

import java.util.List;
import java.util.Vector;

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.InteractiveSurface;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Engine.Surface;
import putzworks.openXcom.Engine.SurfaceHandler;
import putzworks.openXcom.Engine.SurfaceSet;
import putzworks.openXcom.Engine.Timer;
import putzworks.openXcom.Interface.Text;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Ruleset.City;
import putzworks.openXcom.SDL.SDL_Color;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.Country;
import putzworks.openXcom.Savegame.Craft;
import putzworks.openXcom.Savegame.Region;
import putzworks.openXcom.Savegame.Target;
import putzworks.openXcom.Savegame.Ufo;
import putzworks.openXcom.Savegame.Waypoint;

public class Globe extends InteractiveSurface
{
	private static final int NUM_SHADES = 8;
	private Vector<Double> _radius;
	private double _cenLon, _cenLat, _rotLon, _rotLat;
	private int _cenX, _cenY;
	private int _zoom;
	private SurfaceSet[] _texture = new SurfaceSet[NUM_SHADES];
	private Game _game;
	private Surface _markers, _countries;
	private boolean _blink, _detail;
	private Timer _blinkTimer, _rotTimer;
	private List<Polygon> _cacheLand;
	private Surface _mkXcomBase, _mkAlienBase, _mkCraft, _mkWaypoint, _mkCity;
	private Surface _mkFlyingUfo, _mkLandedUfo, _mkCrashedUfo, _mkAlienSite;

	private static final int NUM_TEXTURES  = 13;
	private static final int NUM_LANDSHADES = 48;
	private static final int NUM_SEASHADES = 72;
	private static final double QUAD_LONGITUDE = 0.05;
	private static final double QUAD_LATITUDE = 0.2;
	private static final double ROTATE_LONGITUDE = 0.25;
	private static final double ROTATE_LATITUDE = 0.15;
	private static final int NEAR_RADIUS = 25;

/**
 * Sets up a globe with the specified size and position.
 * @param game Pointer to core game.
 * @param cenX X position of the center of the globe.
 * @param cenY Y position of the center of the globe.
 * @param width Width in pixels.
 * @param height Height in pixels.
 * @param x X position in pixels.
 * @param y Y position in pixels.
 */
public Globe(Game game, int cenX, int cenY, int width, int height, int x, int y) 
{
	super(width, height, x, y);
	_radius=  new Vector<Double>();
	_cenLon = -0.01;
	_cenLat = -0.1;
	_rotLon = 0.0;
	_rotLat = 0.0;
	_cenX = cenX;
	_cenY = cenY;
	_zoom = 0;
	_game = game;
	_blink = true;
	_detail = true;
	_cacheLand = null;
	_texture[0] = _game.getResourcePack().getSurfaceSet("TEXTURE.DAT");
	for (int shade = 1; shade < NUM_SHADES; shade++)
	{
		_texture[shade] = new SurfaceSet(_texture[0]);
		for (int f = 0; f < _texture[shade].getTotalFrames(); f++)
			_texture[shade].getFrame(f).offset(shade);
	}

	_radius.add(90.);
	_radius.add(120.);
	_radius.add(180.);
	_radius.add(280.);
	_radius.add(450.);
	_radius.add(720.);

	_countries = new Surface(width, height, x, y);
	_markers = new Surface(width, height, x, y);

	// Animation timers
	_blinkTimer = new Timer(100);
	_blinkTimer.onTimer(new SurfaceHandler() {
		public void handle(Surface surface) {
			blink();
		}
	});
	_blinkTimer.start();
	_rotTimer = new Timer(50);
	_rotTimer.onTimer((new SurfaceHandler() {
		public void handle(Surface surface) {
			rotate();
		}
	});

	// Globe markers
	_mkXcomBase = new Surface(3, 3);
	_mkXcomBase.lock();
	_mkXcomBase.setPixel(0, 0, 9);
	_mkXcomBase.setPixel(1, 0, 9);
	_mkXcomBase.setPixel(2, 0, 9);
	_mkXcomBase.setPixel(0, 1, 9);
	_mkXcomBase.setPixel(2, 1, 9);
	_mkXcomBase.setPixel(0, 2, 9);
	_mkXcomBase.setPixel(1, 2, 9);
	_mkXcomBase.setPixel(2, 2, 9);
	_mkXcomBase.unlock();

	_mkAlienBase = new Surface(3, 3);
	_mkAlienBase.lock();
	_mkAlienBase.setPixel(0, 0, 1);
	_mkAlienBase.setPixel(1, 0, 1);
	_mkAlienBase.setPixel(2, 0, 1);
	_mkAlienBase.setPixel(0, 1, 1);
	_mkAlienBase.setPixel(2, 1, 1);
	_mkAlienBase.setPixel(0, 2, 1);
	_mkAlienBase.setPixel(1, 2, 1);
	_mkAlienBase.setPixel(2, 2, 1);
	_mkAlienBase.unlock();

	_mkCraft = new Surface(3, 3);
	_mkCraft.lock();
	_mkCraft.setPixel(1, 0, 11);
	_mkCraft.setPixel(0, 1, 11);
	_mkCraft.setPixel(2, 1, 11);
	_mkCraft.setPixel(1, 2, 11);
	_mkCraft.unlock();

	_mkWaypoint = new Surface(3, 3);
	_mkWaypoint.lock();
	_mkWaypoint.setPixel(0, 0, 3);
	_mkWaypoint.setPixel(0, 2, 3);
	_mkWaypoint.setPixel(1, 1, 3);
	_mkWaypoint.setPixel(2, 0, 3);
	_mkWaypoint.setPixel(2, 2, 3);
	_mkWaypoint.unlock();

	_mkCity = new Surface(3, 3);
	_mkCity.lock();
	_mkCity.setPixel(0, 0, 14);
	_mkCity.setPixel(1, 0, 14);
	_mkCity.setPixel(2, 0, 14);
	_mkCity.setPixel(0, 1, 14);
	_mkCity.setPixel(1, 1, 11);
	_mkCity.setPixel(2, 1, 14);
	_mkCity.setPixel(0, 2, 14);
	_mkCity.setPixel(1, 2, 14);
	_mkCity.setPixel(2, 2, 14);
	_mkCity.unlock();

	_mkFlyingUfo = new Surface(3, 3);
	_mkFlyingUfo.lock();
	_mkFlyingUfo.setPixel(1, 0, 13);
	_mkFlyingUfo.setPixel(0, 1, 13);
	_mkFlyingUfo.setPixel(1, 1, 13);
	_mkFlyingUfo.setPixel(2, 1, 13);
	_mkFlyingUfo.setPixel(1, 2, 13);
	_mkFlyingUfo.unlock();

	_mkLandedUfo = new Surface(3, 3);
	_mkLandedUfo.lock();
	_mkLandedUfo.setPixel(0, 0, 7);
	_mkLandedUfo.setPixel(0, 2, 7);
	_mkLandedUfo.setPixel(1, 1, 7);
	_mkLandedUfo.setPixel(2, 0, 7);
	_mkLandedUfo.setPixel(2, 2, 7);
	_mkLandedUfo.unlock();

	_mkCrashedUfo = new Surface(3, 3);
	_mkCrashedUfo.lock();
	_mkCrashedUfo.setPixel(0, 0, 5);
	_mkCrashedUfo.setPixel(0, 2, 5);
	_mkCrashedUfo.setPixel(1, 1, 5);
	_mkCrashedUfo.setPixel(2, 0, 5);
	_mkCrashedUfo.setPixel(2, 2, 5);
	_mkCrashedUfo.unlock();

	_mkAlienSite = new Surface(3, 3);
	_mkAlienSite.lock();
	_mkAlienSite.setPixel(1, 0, 1);
	_mkAlienSite.setPixel(0, 1, 1);
	_mkAlienSite.setPixel(1, 1, 1);
	_mkAlienSite.setPixel(2, 1, 1);
	_mkAlienSite.setPixel(1, 2, 1);
	_mkAlienSite.unlock();

	cachePolygons();
}

/**
 * Deletes the contained surfaces.
 */
public void clearGlobe()
{
	for (int i = 1; i < NUM_SHADES; i++)
		_texture[i] = null;

    _blinkTimer = null;
    _rotTimer = null;
	_countries = null;
	_markers = null;
	_mkXcomBase = null;
	_mkAlienBase = null;
	_mkCraft = null;
	_mkWaypoint = null;
	_mkCity = null;
	_mkFlyingUfo = null;
	_mkLandedUfo = null;
	_mkCrashedUfo = null;
	_mkAlienSite = null;

	for (Polygon i: _cacheLand)
	{
		i = null;
	}
}

/**
 * Converts a polar point into a cartesian point for
 * mapping a polygon onto the 3D-looking globe.
 * @param lon Longitude of the polar point.
 * @param lat Latitude of the polar point.
 * @param x Pointer to the output X position.
 * @param y Pointer to the output Y position.
 */
public final void polarToCart(double lon, double lat, int x, int y)
{
	// Orthographic projection
	x = _cenX + (int)Math.floor(_radius.get(_zoom) * Math.cos(lat) * Math.sin(lon - _cenLon));
	y = _cenY + (int)Math.floor(_radius.get(_zoom) * (Math.cos(_cenLat) * Math.sin(lat) - Math.sin(_cenLat) * Math.cos(lat) * Math.cos(lon - _cenLon)));
}

/**
 * Converts a cartesian point into a polar point for
 * mapping a globe click onto the flat world map.
 * @param x X position of the cartesian point.
 * @param y Y position of the cartesian point.
 * @param lon Pointer to the output longitude.
 * @param lat Pointer to the output latitude.
 */
public final void cartToPolar(int x, int y, double lon, double lat)
{
	// Orthographic projection
	x -= _cenX;
	y -= _cenY;

	double rho = Math.sqrt((double)(x*x + y*y));
	double c = Math.asin(rho / (_radius.get(_zoom)));

	lat = Math.asin((y * Math.sin(c) * Math.cos(_cenLat)) / rho + Math.cos(c) * Math.sin(_cenLat));
	lon = Math.atan2(x * Math.sin(c),(rho * Math.cos(_cenLat) * Math.cos(c) - y * Math.sin(_cenLat) * Math.sin(c))) + _cenLon;

	// Keep between 0 and 2xPI
	while (lon < 0)
		lon += 2 * Math.PI;
	while (lon >= 2 * Math.PI)
		lon -= 2 * Math.PI;
}

/**
 * Checks if a polar point is on the back-half of the globe,
 * invisible to the player.
 * @param lon Longitude of the point.
 * @param lat Latitude of the point.
 * @return True if it's on the back, False if it's on the front.
 */
private final boolean pointBack(double lon, double lat)
{
	double c = Math.cos(_cenLat) * Math.cos(lat) * Math.cos(lon - _cenLon) + Math.sin(_cenLat) * Math.sin(lat);

	return c < 0;
}


/** Return latitude of last visible to player point on given longitude.
 * @param lon Longitude of the point.
 * @return Longitude of last visible point.
 */
private final double lastVisibleLat(double lon)
{
//	double c = cos(_cenLat) * cos(lat) * cos(lon - _cenLon) + sin(_cenLat) * sin(lat);
//        tan(lat) = -cos(_cenLat) * cos(lon - _cenLon)/sin(_cenLat) ;
	return Math.atan(-Math.cos(_cenLat) * Math.cos(lon - _cenLon)/Math.sin(_cenLat));
}

/**
 * Checks if a polar point is inside a certain polygon.
 * @param lon Longitude of the point.
 * @param lat Latitude of the point.
 * @param poly Pointer to the polygon.
 * @return True if it's inside, False if it's outside.
 */
private final boolean insidePolygon(double lon, double lat, Polygon poly)
{
	boolean backFace = true;
	for (int i = 0; i < poly.getPoints(); i++)
	{
		backFace = backFace && pointBack(poly.getLongitude(i), poly.getLatitude(i));
	}
	if (backFace != pointBack(lon, lat))
		return false;

	boolean c = false;
	for (int i = 0; i < poly.getPoints(); i++)
	{
		int j = (i + 1) % poly.getPoints();

		int x = 0, y = 0, x_i = 0, x_j = 0, y_i = 0, y_j = 0;
		polarToCart(poly.getLongitude(i), poly.getLatitude(i), x_i, y_i);
		polarToCart(poly.getLongitude(j), poly.getLatitude(j), x_j, y_j);
		polarToCart(lon, lat, x, y);

		if ( ((y_i > y) != (y_j > y)) &&
			 (x < (x_j - x_i) * (y - y_i) / (y_j - y_i) + x_i) )
		{
			c = !c;
		}
	}
	return c;
}

/**
 * Loads a series of map polar coordinates in X-Com format,
 * converts them and stores them in a set of polygons.
 * @param filename Filename of the DAT file.
 * @param polygons Pointer to the polygon set.
 * @sa http://www.ufopaedia.org/index.php?title=WORLD.DAT
 */
static public void loadDat(final String filename, List<Polygon> polygons)
{
	// Load file
	std.ifstream mapFile (filename.c_str(), std.ios.in | std.ios.binary);
	if (!mapFile)
	{
		throw new Exception("Failed to load DAT");
	}

	short[] value = new short[10];

	while (mapFile.read((char)value, sizeof(value)))
	{
		Polygon poly;
		int points;

		if (value[6] != -1)
		{
			points = 4;
		}
		else
		{
			points = 3;
		}
		poly = new Polygon(points);

		for (int i = 0, j = 0; i < points; i++)
		{
			// Correct X-Com degrees and convert to radians
			double lonRad = value[j++] * 0.125f * Math.PI / 180;
			double latRad = value[j++] * 0.125f * Math.PI / 180;

			poly.setLongitude(i, lonRad);
			poly.setLatitude(i, latRad);
		}
		poly.setTexture(value[8]);

		polygons.add(poly);
	}

	if (!mapFile.eof())
	{
		throw new Exception("Invalid data from file");
	}

	mapFile.close();
}

/**
 * Sets a leftwards rotation speed and starts the timer.
 */
public void rotateLeft()
{
	_rotLon = -ROTATE_LONGITUDE;
	_rotTimer.start();
}

/**
 * Sets a rightwards rotation speed and starts the timer.
 */
public void rotateRight()
{
	_rotLon = ROTATE_LONGITUDE;
	_rotTimer.start();
}

/**
 * Sets a upwards rotation speed and starts the timer.
 */
public void rotateUp()
{
	_rotLat = -ROTATE_LATITUDE;
	_rotTimer.start();
}

/**
 * Sets a downwards rotation speed and starts the timer.
 */
public void rotateDown()
{
	_rotLat = ROTATE_LATITUDE;
	_rotTimer.start();
}

/**
 * Resets the rotation speed and timer.
 */
public void rotateStop()
{
	_rotLon = 0.0;
	_rotLat = 0.0;
	_rotTimer.stop();
}

/**
 * Increases the zoom level on the globe.
 */
public void zoomIn()
{
	if (_zoom < _radius.size() - 1)
	{
		_zoom++;
		cachePolygons();
	}
}

/**
 * Decreases the zoom level on the globe.
 */
public void zoomOut()
{
	if (_zoom > 0)
	{
		_zoom--;
		cachePolygons();
	}
}

/**
 * Zooms the globe out as far as possible.
 */
public void zoomMin()
{
	_zoom = 0;
	cachePolygons();
}

/**
 * Zooms the globe in as close as possible.
 */
public void zoomMax()
{
	_zoom = _radius.size() - 1;
	cachePolygons();
}

/**
 * Rotates the globe to center on a certain
 * polar point on the world map.
 * @param lon Longitude of the point.
 * @param lat Latitude of the point.
 */
public void center(double lon, double lat)
{
	_cenLon = lon;
	_cenLat = lat;
	// HORRIBLE HORRORS CONTAINED WITHIN
	if (_cenLon > -0.01 && _cenLon < 0.01)
	{
		_cenLon = -0.01;
	}
	if (_cenLat > -0.01 && _cenLat < 0.01)
	{
		_cenLat = -0.1;
	}
	cachePolygons();
}

/**
 * Checks if a polar point is inside the globe's landmass.
 * @param lon Longitude of the point.
 * @param lat Latitude of the point.
 * @return True if it's inside, False if it's outside.
 */
public final boolean insideLand(double lon, double lat)
{
	boolean inside = false;
	for (Polygon i: _game.getResourcePack().getPolygons())
	{
		inside = insidePolygon(lon, lat, i);
	}
	return inside;
}

/**
 * Switches the amount of detail shown on the globe.
 * With detail on, country and city details are shown when zoomed in.
 */
public void toggleDetail()
{
	_detail = !_detail;
	drawDetail();
}

/**
 * Checks if a certain target is near a certain cartesian point
 * (within a circled area around it) over the globe.
 * @param target Pointer to target.
 * @param x X coordinate of point.
 * @param y Y coordinate of point.
 * @return True if it's near, false otherwise.
 */
private final boolean targetNear(Target target, int x, int y)
{
	int tx = 0, ty = 0;
	polarToCart(target.getLongitude(), target.getLatitude(), tx, ty);

	int dx = x - tx;
	int dy = y - ty;
	return (dx * dx + dy * dy <= NEAR_RADIUS);
}

/**
 * Returns a list of all the targets currently near a certain
 * cartesian point over the globe.
 * @param x X coordinate of point.
 * @param y Y coordinate of point.
 * @param craft Only get craft targets.
 * @return List of pointers to targets.
 */
public final Vector<Target> getTargets(int x, int y, boolean craft)
{
	Vector<Target> v = new Vector<Target>();
	if (!craft)
	{
		for (Base i: _game.getSavedGame().getBases())
		{
			if ((i).getLongitude() == 0.0 && (i).getLatitude() == 0.0)
				continue;

			if (targetNear((i), x, y))
			{
				v.add(i);
			}

			for (Craft j: (i).getCrafts())
			{
				if ((j).getLongitude() == (i).getLongitude() && (j).getLatitude() == (i).getLatitude() && (j).getDestination() == null)
					continue;

				if (targetNear((j), x, y))
				{
					v.add(j);
				}
			}
		}
	}
	for (Ufo i: _game.getSavedGame().getUfos())
	{
		if (!(i).getDetected())
			continue;

		if (targetNear((i), x, y))
		{
			v.add(i);
		}
	}
	for (Waypoint i: _game.getSavedGame().getWaypoints())
	{
		if (targetNear((i), x, y))
		{
			v.add(i);
		}
	}
	return v;
}

/**
 * Takes care of pre-calculating all the polygons currently visible
 * on the globe and caching them so they only need to be recalculated
 * when the globe is actually moved.
 */
public void cachePolygons()
{
	cache(_game.getResourcePack().getPolygons(), _cacheLand);
	draw();
}

/**
 * Caches a set of polygons.
 * @param polygons Pointer to list of polygons.
 * @param cache Pointer to cache.
 */
private void cache(List<Polygon> polygons, List<Polygon> cache)
{
	// Clear existing cache
	for (Polygon i: cache)
	{
		i = null;
	}
	cache.clear();

	// Pre-calculate values to cache
	for (Polygon i: polygons)
	{
		// Is quad on the back face?
		boolean backFace = true;
		for (int j = 0; j < (i).getPoints(); j++)
		{
			backFace = backFace && pointBack((i).getLongitude(j), (i).getLatitude(j));
		}
		if (backFace)
			continue;

		Polygon p = new Polygon(i);

		// Convert coordinates
		for (int j = 0; j < p.getPoints(); j++)
		{
			int x, y;
			polarToCart(p.getLongitude(j), p.getLatitude(j), x, y);
			p.setX(j, x);
			p.setY(j, y);
		}

		cache.add(p);
	}
}

/**
 * Replaces a certain amount of colors in the palette of the globe.
 * @param colors Pointer to the set of colors.
 * @param firstcolor Offset of the first color to replace.
 * @param ncolors Amount of colors to replace.
 */
public void setPalette(SDL_Color colors, int firstcolor, int ncolors)
{
	Surface.setPalette(colors, firstcolor, ncolors);
	for (int shade = 0; shade < NUM_SHADES; shade++)
	{
		_texture[shade].setPalette(colors, firstcolor, ncolors);
	}
	_countries.setPalette(colors, firstcolor, ncolors);
	_markers.setPalette(colors, firstcolor, ncolors);
	_mkXcomBase.setPalette(colors, firstcolor, ncolors);
	_mkAlienBase.setPalette(colors, firstcolor, ncolors);
	_mkCraft.setPalette(colors, firstcolor, ncolors);
	_mkWaypoint.setPalette(colors, firstcolor, ncolors);
	_mkCity.setPalette(colors, firstcolor, ncolors);
	_mkFlyingUfo.setPalette(colors, firstcolor, ncolors);
	_mkLandedUfo.setPalette(colors, firstcolor, ncolors);
	_mkCrashedUfo.setPalette(colors, firstcolor, ncolors);
	_mkAlienSite.setPalette(colors, firstcolor, ncolors);
}

/**
 * Keeps the animation timers running.
 */
public void think()
{
	_blinkTimer.think(null, this);
	_rotTimer.think(null, this);
}

/**
 * Makes the globe markers blink.
 */
public void blink()
{
	_blink = !_blink;

	int off = 0;
	if (_blink)
		off = -1;
	else
		off = 1;

	_mkXcomBase.offset(off);
	_mkAlienBase.offset(off);
	_mkCraft.offset(off);
	_mkWaypoint.offset(off);
	_mkFlyingUfo.offset(off);
	_mkLandedUfo.offset(off);
	_mkCrashedUfo.offset(off);
	_mkAlienSite.offset(off);

	drawMarkers();
}

/**
 * Rotates the globe by a set amount. Necessary
 * since the globe keeps rotating while a button
 * is pressed down.
 */
public void rotate()
{
	_cenLon += _rotLon;
	_cenLat += _rotLat;
	// DON'T UNLEASH THE TERRORS
	if (_cenLon > -0.01 && _cenLon < 0.01)
	{
		_cenLon = -0.01;
	}
	if (_cenLat > -0.01 && _cenLat < 0.01)
	{
		_cenLat = -0.1;
	}
	cachePolygons();
}

/**
 * Draws the whole globe, part by part.
 */
public void draw()
{
	clear();
	drawOcean();
	drawLand();
	drawDetail();
	drawMarkers();
}

/**
 * Draws a segment of the ocean shade along the longitude.
 * @param startLon Starting longitude.
 * @param endLon Ending longitude.
 * @param colourShift Colour shade.
 */
private void fillLongitudeSegments(double startLon, double endLon, int colourShift)
{
	double traceLon, traceLat, endLan, startLan;
	double dL; // dL - delta of Latitude and used as delta of pie
	int direction, x, y;
	Vector<Integer> polyPointsX, polyPointsY, polyPointsX2, polyPointsY2;
	int[] dx, dy;
	double sx, sy;
	double angle1 = 0.0, angle2 = 0.0;
	boolean bigLonAperture = false;

	if (Math.abs(startLon-endLon) > 1)
	{
		bigLonAperture = true;
	}

    // find two latitudes where
	startLan = lastVisibleLat(startLon);
	endLan   = lastVisibleLat(endLon);

	traceLon = startLon;

	// If North pole visible, we want to head south (+1), if South pole, head North (-1)
	if (!pointBack(traceLon, -Math.PI / 2))
	{
		direction = 1;
	}
	else
	{
		direction = -1;
	}

	// Draw globe depending on the direction
	if (direction == 1)
	{
	    // draw fisrt longtitude line from pole
		traceLon = startLon;
		dL = (startLan + Math.PI/2) / 20;
		for (traceLat = -Math.PI/2; traceLat < startLan; traceLat += dL)
		{
			polarToCart(traceLon, traceLat, x, y);
			polyPointsX.add(x);
			polyPointsY.add(y);
		}

        // if aperture of longtitude is big then we need find first angle of sector
		if (bigLonAperture)
		{
			sx = x - _cenX;
			sy = y - _cenY;
			angle1 = Math.atan(sy / sx);
			if (sx < 0) angle1 += Math.PI;
		}

	    // draw second longtitude line from pole
		traceLon = endLon;
		dL = (endLan + Math.PI/2) / 20;
		for (traceLat = -Math.PI/2; traceLat < endLan; traceLat += dL)
		{
			polarToCart(traceLon, traceLat, x, y);
			polyPointsX2.add(x);
			polyPointsY2.add(y);
		}

        // if aperture of longtitudes is big we need find second angle of sector and draw pie of circle between two longtitudes
		if (bigLonAperture)
		{
			sx = x - _cenX;
			sy = y - _cenY;
			angle2 = Math.atan(sy/sx);
			if (sx < 0)
			{
				angle2 += Math.PI;
			}

		    // draw sector part of circle
            if (angle1 > angle2)
		    {
                dL = (angle1 - angle2) / 20;
				for (double a = angle2 + dL / 2; a < angle1; a += dL)
				{
					x = _cenX + (int)Math.floor(_radius.get(_zoom) * Math.cos(a));
					y = _cenY + (int)Math.floor(_radius.get(_zoom) * Math.sin(a));
					polyPointsX2.add(x);
					polyPointsY2.add(y);
				}
		    }
			else
            {
				dL = (2*Math.PI + angle1 - angle2) / 20;
				for (double a = angle2 + dL / 2; a < 2*Math.PI + angle1; a += dL)
				{
					x = _cenX + (int)Math.floor(_radius.get(_zoom) * Math.cos(a));
					y = _cenY + (int)Math.floor(_radius.get(_zoom) * Math.sin(a));
					polyPointsX2.add(x);
					polyPointsY2.add(y);
				}
			}
		}
	}
	else // another direction
	{
	    // draw fisrt longtitude line from pole
		traceLon = startLon;
		dL = (startLan - Math.PI/2) / 20;
		for (traceLat = Math.PI/2; traceLat > startLan; traceLat += dL)
		{
			polarToCart(traceLon, traceLat, x, y);
			polyPointsX.add(x);
			polyPointsY.add(y);
		}

        // if aperture of longtitude is big then we need find first angle of sector of pie between longtitudes
		if (bigLonAperture)
		{
			sx = x - _cenX;
			sy = y - _cenY;
			angle1 = Math.atan(sy / sx);
			if (sx < 0)
			{
				angle1 += Math.PI;
			}
		}

	    // draw second longtitude line from pole
		traceLon = endLon;
		dL = (endLan - Math.PI/2) / 20;
		for (traceLat = Math.PI/2; traceLat > endLan; traceLat += dL)
		{
			polarToCart(traceLon, traceLat, x, y);
			polyPointsX2.add(x);
			polyPointsY2.add(y);
		}

        // if aperture of longtitudes is big we need find second angle of sector and draw pie of circle between two longtitudes
		if (bigLonAperture)
		{
			sx = x - _cenX;
			sy = y - _cenY;
			angle2 = Math.atan(sy / sx);
			if (sx < 0)
			{
				angle2 += Math.PI;
			}
			if (angle2 > angle1)
			{
				dL = (angle2 - angle1) / 20;
				for (double a = angle1 + dL / 2; a < angle2; a += dL)
				{
					x = _cenX + (int)Math.floor(_radius.get(_zoom) * Math.cos(a));
					y = _cenY + (int)Math.floor(_radius.get(_zoom) * Math.sin(a));
					polyPointsX.add(x);
					polyPointsY.add(y);
				}
		    }
			else
            {
				dL = (2*Math.PI + angle2 - angle1) / 20;
				for (double a = angle1 + dL / 2; a < 2*Math.PI + angle2; a += dL)
				{
					x = _cenX + (int)Math.floor(_radius.get(_zoom) * Math.cos(a));
					y = _cenY + (int)Math.floor(_radius.get(_zoom) * Math.sin(a));
					polyPointsX.add(x);
					polyPointsY.add(y);
				}
			}
		}
	}

	dx = new int[polyPointsX.size()+polyPointsX2.size()];
	dy = new int[polyPointsX.size()+polyPointsX2.size()];

	if (polyPointsX.size()+polyPointsX2.size() > 0)
	{
		for (int i = 0; i < polyPointsX.size(); i++)
		{
			if (i < dx.length && i < dy.length){
			dx[i] = polyPointsX.get(i);
			dy[i] = polyPointsY.get(i);
			}
		}

		for (int i = 0 ; i < polyPointsX2.size() ; i++)
		{
			if (i < dx.length && i < dy.length){
			dx[i+polyPointsX.size()] = polyPointsX2.get(polyPointsX2.size()-1-i);
			dy[i+polyPointsX.size()] = polyPointsY2.get(polyPointsX2.size()-1-i);
			}
		}
		drawPolygon(dx, dy, polyPointsX.size()+polyPointsX2.size(), (short)(Palette.blockOffset(12) + colourShift));
	}

	dx = null;
	dy = null;
}

/**
 * Renders the ocean, shading it according to the time of day.
 */
public void drawOcean()
{
	double curTime = _game.getSavedGame().getTime().getDaylight();
	double dayLon = -curTime * 2*Math.PI;
	double nightLon = dayLon + Math.PI;

	lock();

	drawCircle(_cenX, _cenY, (int)Math.floor(_radius.get(_zoom)), (short)(Palette.blockOffset(12)+28));

	fillLongitudeSegments(dayLon   + QUAD_LONGITUDE, nightLon - QUAD_LONGITUDE, 0);
	fillLongitudeSegments(dayLon - QUAD_LONGITUDE, dayLon, 16);
	fillLongitudeSegments(dayLon, dayLon + QUAD_LONGITUDE, 8);
	fillLongitudeSegments(nightLon - QUAD_LONGITUDE, nightLon, 8);
	fillLongitudeSegments(nightLon, nightLon + QUAD_LONGITUDE, 16);

	unlock();
}

/**
 * Renders the land, taking all the visible world polygons
 * and shading them according to the time of day.
 */
public void drawLand()
{
	int _shades[] = {3, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3,
					 4, 5, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 6, 5, 4};
	double minLon = 100.0, maxLon = -100.0, curTime = _game.getSavedGame().getTime().getDaylight();
	int x[] = new int[4], y[] = new int[4];
	boolean pole;

	for (Polygon i: _cacheLand)
	{
		minLon = 100.0;
		maxLon = -100.0;
		pole = false;
		// Convert coordinates
		for (int j = 0; j < (i).getPoints(); j++)
		{
			double tmpLon = (i).getLongitude(j);
			double tmpLat = (i).getLatitude(j);

			if (Math.abs(tmpLat) < (Math.PI/2 - 0.0001)) //pole vertexes have no longitude
			{
				if (tmpLon < minLon && tmpLon >= (maxLon - Math.PI))
					minLon = tmpLon;
				if (tmpLon > maxLon && tmpLon <= (minLon + Math.PI))
					maxLon = tmpLon;
			}
			else
			{
				pole = true;
			}

			x[j] = (i).getX(j);
			y[j] = (i).getY(j);
		}

		// Apply textures according to zoom and shade
		int zoom = (2 - (int)Math.floor(_zoom / 2.0)) * NUM_TEXTURES;
		int shade = (int)((curTime + (((minLon + maxLon) / 2) / (2 * Math.PI))) * NUM_LANDSHADES);
		shade = _shades[shade % NUM_LANDSHADES];
		if (pole)
		{
			shade = (int)(shade * 0.6 + 4 * (1 - 0.6)); // twilight zone
		}
		drawTexturedPolygon(x, y, (i).getPoints(), _texture[shade].getFrame((i).getTexture() + zoom), 0, 0);
		(i).setShade(shade);
	}
}

/**
 * Draws the details of the countries on the globe,
 * based on the current zoom level.
 */
public void drawDetail()
{
	_countries.clear();

	if (!_detail)
		return;

	// Draw the country borders
	if (_zoom >= 1)
	{
		// Lock the surface
		_countries.lock();

		for (Polyline i: _game.getResourcePack().getPolylines())
		{
			int[] x = new int[2], y = new int[2];
			for (int j = 0; j < (i).getPoints() - 1; j++)
			{
				// Don't draw if polyline is facing back
				if (pointBack((i).getLongitude(j), (i).getLatitude(j)) || pointBack((i).getLongitude(j + 1), (i).getLatitude(j + 1)))
					continue;

				// Convert coordinates
				polarToCart((i).getLongitude(j), (i).getLatitude(j), x[0], y[0]);
				polarToCart((i).getLongitude(j + 1), (i).getLatitude(j + 1), x[1], y[1]);

				_countries.drawLine(x[0], y[0], x[1], y[1], Palette.blockOffset(10)+2);
			}
		}

		// Unlock the surface
		_countries.unlock();
	}

	// Draw the country names
	if (_zoom >= 2)
	{
		Text label = new Text(80, 9, 0, 0);
		label.setPalette(getPalette());
		label.setFonts(_game.getResourcePack().getFont("BIGLETS.DAT"), _game.getResourcePack().getFont("SMALLSET.DAT"));
		label.setAlign(TextHAlign.ALIGN_CENTER);
		label.setColor(Palette.blockOffset(15)-1);

		int x, y;
		for (Country i: _game.getSavedGame().getCountries())
		{
			// Don't draw if label is facing back
			if (pointBack((i).getRules().getLabelLongitude(), (i).getRules().getLabelLatitude()))
				continue;

			// Convert coordinates
			polarToCart((i).getRules().getLabelLongitude(), (i).getRules().getLabelLatitude(), x, y);

			label.setX(x - 40);
			label.setY(y);
			label.setText(_game.getLanguage().getString((i).getRules().getType()));
			label.blit(_countries);
		}

		label = null;;
	}

	// Draw the city markers
	if (_zoom >= 3)
	{
		Text label = new Text(80, 9, 0, 0);
		label.setPalette(getPalette());
		label.setFonts(_game.getResourcePack().getFont("BIGLETS.DAT"), _game.getResourcePack().getFont("SMALLSET.DAT"));
		label.setAlign(TextHAlign.ALIGN_CENTER);
		label.setColor(Palette.blockOffset(8)+10);

		int x, y;
		for (Region i: _game.getSavedGame().getRegions())
		{
			for (City j: (i).getRules().getCities())
			{
				// Don't draw if city is facing back
				if (pointBack((j).getLongitude(), (j).getLatitude()))
					continue;

				// Convert coordinates
				polarToCart((j).getLongitude(), (j).getLatitude(), x, y);

				_mkCity.setX(x - 1);
				_mkCity.setY(y - 1);
				_mkCity.setPalette(getPalette());
				_mkCity.blit(_countries);

				label.setX(x - 40);
				label.setY(y + 2);
				label.setText(_game.getLanguage().getString(j.getName()));
				label.blit(_countries);
			}
		}

		label = null;
	}
}

/**
 * Draws the markers of all the various things going
 * on around the world on top of the globe.
 */
public void drawMarkers()
{
	int x = 0, y = 0;
	_markers.clear();

	// Draw the base markers
	for (Base i: _game.getSavedGame().getBases())
	{
		// Cheap hack to hide bases when they haven't been placed yet
		if (((i).getLongitude() != 0.0 || (i).getLatitude() != 0.0) &&
			!pointBack((i).getLongitude(), (i).getLatitude()))
		{
			polarToCart((i).getLongitude(), (i).getLatitude(), x, y);

			_mkXcomBase.setX(x - 1);
			_mkXcomBase.setY(y - 1);
			_mkXcomBase.blit(_markers);
		}
		// Draw the craft markers
		for (Craft j: (i).getCrafts())
		{
			// Hide crafts docked at base
			if ((j).getStatus() != "STR_OUT" || pointBack((j).getLongitude(), (j).getLatitude()))
				continue;

			polarToCart((j).getLongitude(), (j).getLatitude(), x, y);

			_mkCraft.setX(x - 1);
			_mkCraft.setY(y - 1);
			_mkCraft.blit(_markers);
		}
	}

	// Draw the UFO markers
	for (Ufo i: _game.getSavedGame().getUfos())
	{
		if (pointBack((i).getLongitude(), (i).getLatitude()))
			continue;

		polarToCart((i).getLongitude(), (i).getLatitude(), x, y);

		if ((i).getDetected())
		{
			if ((i).isCrashed())
			{
				_mkCrashedUfo.setX(x - 1);
				_mkCrashedUfo.setY(y - 1);
				_mkCrashedUfo.blit(_markers);
			}
			else
			{
				_mkFlyingUfo.setX(x - 1);
				_mkFlyingUfo.setY(y - 1);
				_mkFlyingUfo.blit(_markers);
			}
		}
	}

	// Draw the waypoint markers
	for (Waypoint i: _game.getSavedGame().getWaypoints())
	{
		if (pointBack((i).getLongitude(), (i).getLatitude()))
			continue;

		polarToCart((i).getLongitude(), (i).getLatitude(), x, y);

		_mkWaypoint.setX(x - 1);
		_mkWaypoint.setY(y - 1);
		_mkWaypoint.blit(_markers);
	}
}

/**
 * Blits the globe onto another surface.
 * @param surface Pointer to another surface.
 */
public void blit(Surface surface)
{
	super.blit(surface);
	_countries.blit(surface);
	_markers.blit(surface);
}

/**
 * Ignores any mouse clicks that are outside the globe.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mousePress(Action action, State state)
{
	double lon = 0, lat = 0;
	cartToPolar((int)Math.floor(action.getXMouse() / action.getXScale()), (int)Math.floor(action.getYMouse() / action.getYScale()), lon, lat);

	// Check for errors
	if (lat == lat && lon == lon)
		super.mousePress(action, state);
}

/**
 * Ignores any mouse clicks that are outside the globe.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseRelease(Action action, State state)
{
	double lon = 0, lat = 0;
	cartToPolar((int)Math.floor(action.getXMouse() / action.getXScale()), (int)Math.floor(action.getYMouse() / action.getYScale()), lon, lat);

	// Check for errors
	if (lat == lat && lon == lon)
		super.mouseRelease(action, state);
}

/**
 * Ignores any mouse clicks that are outside the globe
 * and handles globe rotation and zooming.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void mouseClick(Action action, State state)
{
	double lon, lat;
	cartToPolar((int)Math.floor(action.getXMouse() / action.getXScale()), (int)Math.floor(action.getYMouse() / action.getYScale()), lon, lat);

	// Check for errors
	if (lat == lat && lon == lon)
	{
		super.mouseClick(action, state);

		// Handle globe control
		if (action.getDetails().button.button == SDL_BUTTON_RIGHT)
		{
			center(lon, lat);
		}
		else if (action.getDetails().button.button == SDL_BUTTON_WHEELUP)
		{
			zoomIn();
		}
		else if (action.getDetails().button.button == SDL_BUTTON_WHEELDOWN)
		{
			zoomOut();
		}
	}
}

/**
 * Handles globe keyboard shortcuts.
 * @param action Pointer to an action.
 * @param state State that the action handlers belong to.
 */
public void keyboardPress(Action action, State state)
{
	super.keyboardPress(action, state);
	if (action.getDetails().key.keysym.sym == SDLK_TAB)
	{
		toggleDetail();
	}
}

/**
 * Get the polygons texture at a given point
 * @param lon Longitude of the point.
 * @param lat Latitude of the point.
 * @param texture pointer to texture ID returns -1 when polygon not found
 * @param shade pointer to shade
 */
public void getPolygonTextureAndShade(double lon, double lat, int texture, int shade)
{
	texture = -1;
	for (Polygon i: _cacheLand)
	{
		if(insidePolygon(lon, lat, i))
		{
			texture = ((Polygon)(i)).getTexture();
			shade = ((Polygon)(i)).getShade();
			return;
		}
	}
}

}
