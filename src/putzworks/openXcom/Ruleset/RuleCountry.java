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
package putzworks.openXcom.Ruleset;

public class RuleCountry
{
	private String _type;
	private int _fundingMin, _fundingMax;
	private double _labelLon, _labelLat;

/**
 * Creates a blank ruleset for a certain
 * type of country.
 * @param type String defining the type.
 */
public RuleCountry(String type)
{
	_type = type;
	_fundingMin = 0;
	_fundingMax = 0;
	_labelLon = 0.0;
	_labelLat = 0.0;

}

/**
 * Returns the language string that names
 * this country. Each country type
 * has a unique name.
 * @return Country name.
 */
public final String getType()
{
	return _type;
}

/**
 * Returns the country's minimum starting funding,
 * in thousands.
 * @return Monthly funding.
 */
public final int getMinFunding()
{
	return _fundingMin;
}

/**
 * Changes the country's minimum starting funding,
 * in thousands.
 * @param funding Monthly funding.
 */
public void setMinFunding(int funding)
{
	_fundingMin = funding;
}

/**
 * Returns the country's maximum starting funding.
 * @return Monthly funding.
 */
public final int getMaxFunding()
{
	return _fundingMax;
}

/**
 * Changes the country's maximum starting funding.
 * @param funding Monthly funding.
 */
public void setMaxFunding(int funding)
{
	_fundingMax = funding;
}

/**
 * Returns the longitude of the country's label on the globe.
 * @return Longitude in radians.
 */
public final double getLabelLongitude()
{
	return _labelLon;
}

/**
 * Changes the longitude of the country's label on the globe.
 * @param lon Longitude in radians.
 */
public void setLabelLongitude(double lon)
{
	_labelLon = lon;
}

/**
 * Returns the latitude of the country's label on the globe.
 * @return Latitude in radians.
 */
public final double getLabelLatitude()
{
	return _labelLat;
}

/**
 * Changes the latitude of the country's label on the globe.
 * @param lat Latitude in radians.
 */
public void setLabelLatitude(double lat)
{
	_labelLat = lat;
}

}
