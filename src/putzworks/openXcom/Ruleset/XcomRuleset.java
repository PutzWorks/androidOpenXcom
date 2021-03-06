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

import putzworks.openXcom.Ruleset.RuleItem.BattleType;
import putzworks.openXcom.Ruleset.RuleItem.ItemDamageType;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.BaseFacility;
import putzworks.openXcom.Savegame.Country;
import putzworks.openXcom.Savegame.Craft;
import putzworks.openXcom.Savegame.CraftWeapon;
import putzworks.openXcom.Savegame.Region;
import putzworks.openXcom.Savegame.SavedGame;
import putzworks.openXcom.Savegame.SavedGame.GameDifficulty;
import putzworks.openXcom.Savegame.Soldier;
import putzworks.openXcom.Savegame.Unit.UnitStats;
import putzworks.openXcom.Ufopaedia.Ufopaedia;

public class XcomRuleset extends Ruleset
{

/**
 * Initializes the X-Com ruleset with all the rules
 * mimicking the original game.
 */
public XcomRuleset()
{
	super();
	// Add soldier names
	SoldierNamePool american = new SoldierNamePool();
	american.load("American");
	_names.add(american);

	SoldierNamePool british = new SoldierNamePool();
	british.load("British");
	_names.add(british);

	SoldierNamePool french = new SoldierNamePool();
	french.load("French");
	_names.add(french);

	SoldierNamePool german = new SoldierNamePool();
	german.load("German");
	_names.add(german);

	SoldierNamePool japanese = new SoldierNamePool();
	japanese.load("Japanese");
	_names.add(japanese);

	SoldierNamePool russian = new SoldierNamePool();
	russian.load("Russian");
	_names.add(russian);

	// Add countries
	RuleCountry usa = new RuleCountry("STR_USA");
	usa.setMinFunding(600);
	usa.setMaxFunding(1200);
	usa.setLabelLongitude(4.53786);
	usa.setLabelLatitude(-0.698132);

	RuleCountry russia = new RuleCountry("STR_RUSSIA");
	russia.setMinFunding(230);
	russia.setMaxFunding(460);
	russia.setLabelLongitude(1.0472);
	russia.setLabelLatitude(-1.0472);

	RuleCountry uk = new RuleCountry("STR_UK");
	uk.setMinFunding(240);
	uk.setMaxFunding(480);
	uk.setLabelLongitude(6.24828);
	uk.setLabelLatitude(-0.935933);

	RuleCountry france = new RuleCountry("STR_FRANCE");
	france.setMinFunding(320);
	france.setMaxFunding(640);
	france.setLabelLongitude(0.0436332);
	france.setLabelLatitude(-0.811578);

	RuleCountry germany = new RuleCountry("STR_GERMANY");
	germany.setMinFunding(250);
	germany.setMaxFunding(500);
	germany.setLabelLongitude(0.200713);
	germany.setLabelLatitude(-0.872665);

	RuleCountry italy = new RuleCountry("STR_ITALY");
	italy.setMinFunding(160);
	italy.setMaxFunding(320);
	italy.setLabelLongitude(0.218166);
	italy.setLabelLatitude(-0.765763);

	RuleCountry spain = new RuleCountry("STR_SPAIN");
	spain.setMinFunding(140);
	spain.setMaxFunding(280);
	spain.setLabelLongitude(6.23955);
	spain.setLabelLatitude(-0.743947);

	RuleCountry china = new RuleCountry("STR_CHINA");
	china.setMinFunding(245);
	china.setMaxFunding(490);
	china.setLabelLongitude(1.74533);
	china.setLabelLatitude(-0.610865);

	RuleCountry japan = new RuleCountry("STR_JAPAN");
	japan.setMinFunding(400);
	japan.setMaxFunding(800);
	japan.setLabelLongitude(2.40855);
	japan.setLabelLatitude(-0.667588);

	RuleCountry india = new RuleCountry("STR_INDIA");
	india.setMinFunding(150);
	india.setMaxFunding(300);
	india.setLabelLongitude(1.39626);
	india.setLabelLatitude(-0.418879);

	RuleCountry brazil = new RuleCountry("STR_BRAZI");
	brazil.setMinFunding(300);
	brazil.setMaxFunding(600);
	brazil.setLabelLongitude(5.32325);
	brazil.setLabelLatitude(0.0872665);

	RuleCountry australia = new RuleCountry("STR_AUSTRALIA");
	australia.setMinFunding(280);
	australia.setMaxFunding(560);
	australia.setLabelLongitude(2.35619);
	australia.setLabelLatitude(0.436332);

	RuleCountry nigeria = new RuleCountry("STR_NIGERIA");
	nigeria.setMinFunding(180);
	nigeria.setMaxFunding(360);
	nigeria.setLabelLongitude(0.1309);
	nigeria.setLabelLatitude(-0.174533);

	RuleCountry africa = new RuleCountry("STR_SOUTH_AFRICA");
	africa.setMinFunding(230);
	africa.setMaxFunding(460);
	africa.setLabelLongitude(0.436332);
	africa.setLabelLatitude(0.523599);

	RuleCountry egypt = new RuleCountry("STR_EGYPT");
	egypt.setMinFunding(230);
	egypt.setMaxFunding(460);
	egypt.setLabelLongitude(0.506145);
	egypt.setLabelLatitude(-0.453786);

	RuleCountry canada = new RuleCountry("STR_CANADA");
	canada.setMinFunding(230);
	canada.setMaxFunding(460);
	canada.setLabelLongitude(4.53786);
	canada.setLabelLatitude(-0.959931);

	_countries = null;
	_countries.put("STR_USA", (RuleCountry)usa);
	_countries.put("STR_RUSSIA", (RuleCountry)russia);
	_countries.put("STR_UK", (RuleCountry)uk);
	_countries.put("STR_FRANCE", (RuleCountry)france);
	_countries.put("STR_GERMANY", (RuleCountry)germany);
	_countries.put("STR_ITALY", (RuleCountry)italy);
	_countries.put("STR_SPAIN", (RuleCountry)spain);
	_countries.put("STR_CHINA", (RuleCountry)china);
	_countries.put("STR_JAPAN", (RuleCountry)japan);
	_countries.put("STR_INDIA", (RuleCountry)india);
	_countries.put("STR_BRAZI", (RuleCountry)brazil);
	_countries.put("STR_AUSTRALIA", (RuleCountry)australia);
	_countries.put("STR_NIGERIA", (RuleCountry)nigeria);
	_countries.put("STR_SOUTH_AFRICA", (RuleCountry)africa);
	_countries.put("STR_EGYPT", (RuleCountry)egypt);
	_countries.put("STR_CANADA", (RuleCountry)canada);

	// Add regions
	RuleRegion namerica = new RuleRegion("STR_NORTH_AMERICA");
	namerica.setBaseCost(800000);
	namerica.addArea(3.40339, 5.32107, -1.22173, -0.962113);
	namerica.addArea(4.01426, 5.32107, -0.959931, -0.52578);
	namerica.addArea(4.18879, 5.23381, -0.523599, -0.176715);
	namerica.getCities().add(new City("STR_NEW_YORK", 4.99382, -0.711222));
	namerica.getCities().add(new City("STR_WASHINGTON", 4.9371, -0.676315));
	namerica.getCities().add(new City("STR_LOS_ANGELES", 4.21933, -0.595594));
	namerica.getCities().add(new City("STR_MONTREA", 4.9611, -0.794125));
	namerica.getCities().add(new City("STR_HAVANA", 4.84547, -0.392699));
	namerica.getCities().add(new City("STR_MEXICO_CITY", 4.55313, -0.338158));
	namerica.getCities().add(new City("STR_CHICAGO", 4.75384, -0.730857));
	namerica.getCities().add(new City("STR_VANCOUVER", 4.13207, -0.861756));
	namerica.getCities().add(new City("STR_DALLAS", 4.59676, -0.571595));
	
	RuleRegion arctic = new RuleRegion("STR_ARCTIC");
	arctic.setBaseCost(950000);
	arctic.addArea(0, 6.281, -1.5708, -1.22391);
	
	RuleRegion antarctica = new RuleRegion("STR_ANTARCTICA");
	antarctica.setBaseCost(900000);
	antarctica.addArea(0, 6.281, 1.0472, 1.5708);
	
	RuleRegion samerica = new RuleRegion("STR_SOUTH_AMERICA");
	samerica.setBaseCost(600000);
	samerica.addArea(4.71239, 5.49561, -0.174533, -0.00218166);
	samerica.addArea(4.79966, 5.7574, 0, 0.259618);
	samerica.addArea(4.79966, 5.67014, 0.261799, 1.04502);
	samerica.getCities().add(new City("STR_BRASILIA", 5.44761, 0.274889));
	samerica.getCities().add(new City("STR_BOGOTA", 4.98946, -0.0785398));
	samerica.getCities().add(new City("STR_BUENOS_AIRES", 5.27962, 0.602139));
	samerica.getCities().add(new City("STR_SANTIAGO", 5.05055, 0.582504));
	samerica.getCities().add(new City("STR_RIO_DE_JANEIRO", 5.53051, 0.399244));
	samerica.getCities().add(new City("STR_LIMA", 4.93928, 0.20944));
	samerica.getCities().add(new City("STR_CARACAS", 5.116, -0.18326));
	
	RuleRegion europe = new RuleRegion("STR_EUROPE");
	europe.setBaseCost(1000000);
	europe.addArea(5.84685, 1.04502, -1.22173, -0.613047);
	europe.getCities().add(new City("STR_LONDON", 6.281, -0.898845));
	europe.getCities().add(new City("STR_PARIS", 0.0414516, -0.850848));
	europe.getCities().add(new City("STR_BERLIN", 0.233438, -0.916298));
	europe.getCities().add(new City("STR_MOSCOW", 0.65668, -0.973021));
	europe.getCities().add(new City("STR_ROME", 0.218166, -0.730857));
	europe.getCities().add(new City("STR_MADRID", 6.21774, -0.704677));
	europe.getCities().add(new City("STR_BUDAPEST", 0.333794, -0.829031));
	
	RuleRegion nafrica = new RuleRegion("STR_NORTH_AFRICA");
	nafrica.setBaseCost(650000);
	nafrica.addArea(5.84685, 0.69595, -0.610865, -0.263981);
	nafrica.addArea(5.84685, 0.957749, -0.261799, -0.00218166);
	nafrica.getCities().add(new City("STR_LAGOS", 0.0545415, -0.113446));
	nafrica.getCities().add(new City("STR_CAIRO", 0.545415, -0.523599));
	nafrica.getCities().add(new City("STR_CASABLANCA", 6.1501, -0.584685));
	
	RuleRegion safrica = new RuleRegion("STR_SOUTHERN_AFRICA");
	safrica.setBaseCost(550000);
	safrica.addArea(0.0872665, 0.957749, 0, 0.69595);
	safrica.getCities().add(new City("STR_PRETORIA", 0.490874, 0.458149));
	safrica.getCities().add(new City("STR_NAIROBI", 0.641409, 0.0218166));
	safrica.getCities().add(new City("STR_CAPE_TOWN", 0.322886, 0.593412));
	safrica.getCities().add(new City("STR_KINSHASA", 0.268344, 0.0763582));
	
	RuleRegion casia = new RuleRegion("STR_CENTRAL_ASIA");
	casia.setBaseCost(500000);
	casia.addArea(0.698132, 1.21955, -0.610865, -0.263981);
	casia.addArea(1.0472, 1.56861, -0.872665, -0.613047);
	casia.addArea(1.22173, 1.56861, -0.610865, -0.0894481);
	casia.getCities().add(new City("STR_ANKARA", 0.571595, -0.69595));
	casia.getCities().add(new City("STR_DELHI", 1.34827, -0.4996));
	casia.getCities().add(new City("STR_KARACHI", 1.16937, -0.434151));
	casia.getCities().add(new City("STR_BAGHDAD", 0.776672, -0.580322));
	casia.getCities().add(new City("STR_TEHRAN", 0.898845, -0.621774));
	casia.getCities().add(new City("STR_BOMBAY", 1.27627, -0.329431));
	casia.getCities().add(new City("STR_CALCUTTA", 1.54243, -0.394881));
	
	RuleRegion seasia = new RuleRegion("STR_SOUTH_EAST_ASIA");
	seasia.setBaseCost(750000);
	seasia.addArea(1.5708, 1.83041, -0.872665, 0.172351);
	seasia.addArea(1.8326, 2.61581, -0.872665, -0.0894481);
	seasia.getCities().add(new City("STR_TOKYO", 2.4391, -0.621774));
	seasia.getCities().add(new City("STR_BEIJING", 2.03113, -0.69595));
	seasia.getCities().add(new City("STR_BANGKOK", 1.75624, -0.237801));
	seasia.getCities().add(new City("STR_MANILA", 2.11185, -0.255254));
	seasia.getCities().add(new City("STR_SEOU", 2.21657, -0.654498));
	seasia.getCities().add(new City("STR_SINGAPORE", 1.81296, -0.0239983));
	seasia.getCities().add(new City("STR_JAKARTA", 1.86314, 0.109083));
	seasia.getCities().add(new City("STR_SHANGHAI", 2.12058, -0.545415));
	seasia.getCities().add(new City("STR_HONG_KONG", 1.99186, -0.388336));
	
	RuleRegion siberia = new RuleRegion("STR_SIBERIA");
	siberia.setBaseCost(800000);
	siberia.addArea(1.0472, 3.13941, -1.22173, -0.874846);
	siberia.getCities().add(new City("STR_NOVOSIBIRSK", 1.44426, -0.959931));
	
	RuleRegion australasia = new RuleRegion("STR_AUSTRALASIA");
	australasia.setBaseCost(750000);
	//australasia.addArea(1.8326, 3.13941, -0.0872665, 0.870483);
	australasia.addArea(1.8326, 3.13941, -0.0872665, 1.04502);
	australasia.getCities().add(new City("STR_CANBERRA", 2.60272, 0.61741));
	australasia.getCities().add(new City("STR_WELLINGTON", 3.05651, 0.719948));
	australasia.getCities().add(new City("STR_MELBOURNE", 2.53073, 0.661043));
	australasia.getCities().add(new City("STR_PERTH", 2.02022, 0.558505));
	
	RuleRegion pacific = new RuleRegion("STR_PACIFIC");
	pacific.setBaseCost(600000);
	pacific.addArea(3.14159, 3.40121, -1.22173, -0.962113);
	pacific.addArea(3.14159, 4.01208, -0.959931, -0.52578);
	pacific.addArea(3.14159, 4.18661, -0.523599, -0.176715);
	pacific.addArea(3.14159, 4.71021, -0.174533, -0.00218166);
	pacific.addArea(3.14159, 4.79747, 0, 1.04502);
	pacific.addArea(2.61799, 3.13941, -0.872665, -0.0894481);
	
	RuleRegion natlantic = new RuleRegion("STR_NORTH_ATLANTIC");
	natlantic.setBaseCost(500000);
	natlantic.addArea(5.32325, 5.84467, -1.22173, -0.52578);
	natlantic.addArea(5.23599, 5.84467, -0.523599, -0.176715);
	natlantic.addArea(5.49779, 5.84467, -0.174533, -0.00218166);
	
	RuleRegion satlantic = new RuleRegion("STR_SOUTH_ATLANTIC");
	satlantic.setBaseCost(500000);
	satlantic.addArea(5.75959, 0.0850848, 0, 0.259618);
	satlantic.addArea(5.67232, 0.0850848, 0.261799, 1.04502);
	satlantic.addArea(0.0872665, 0.959931, 0.698132, 1.04502);
	
	RuleRegion indian = new RuleRegion("STR_INDIAN_OCEAN");
	indian.setBaseCost(500000);
	indian.addArea(0.959931, 1.21955, -0.261799, 0.172351);
	indian.addArea(1.22173, 1.56861, -0.0872665, 0.172351);
	indian.addArea(0.959931, 1.83041, 0.174533, 1.04502);
	
	_regions.put("STR_NORTH_AMERICA", (RuleRegion)namerica);
	_regions.put("STR_ARCTIC", (RuleRegion)arctic);
	_regions.put("STR_ANTARCTICA", (RuleRegion)antarctica);
	_regions.put("STR_SOUTH_AMERICA", (RuleRegion)samerica);
	_regions.put("STR_EUROPE", (RuleRegion)europe);
	_regions.put("STR_NORTH_AFRICA", (RuleRegion)nafrica);
	_regions.put("STR_SOUTHERN_AFRICA", (RuleRegion)safrica);
	_regions.put("STR_CENTRAL_ASIA", (RuleRegion)casia);
	_regions.put("STR_SOUTH_EAST_ASIA", (RuleRegion)seasia);
	_regions.put("STR_SIBERIA", (RuleRegion)siberia);
	_regions.put("STR_AUSTRALASIA", (RuleRegion)australasia);
	_regions.put("STR_PACIFIC", (RuleRegion)pacific);
	_regions.put("STR_NORTH_ATLANTIC", (RuleRegion)natlantic);
	_regions.put("STR_SOUTH_ATLANTIC", (RuleRegion)satlantic);
	_regions.put("STR_INDIAN_OCEAN", (RuleRegion)indian);

	// Add base facilities
	RuleBaseFacility lift = new RuleBaseFacility("STR_ACCESS_LIFT");
	lift.setSpriteShape(2);
	lift.setSpriteFacility(17);
	lift.setBuildCost(300000);
	lift.setBuildTime(1);
	lift.setMonthlyCost(4000);
	lift.setLift(true);

	RuleBaseFacility quarters = new RuleBaseFacility("STR_LIVING_QUARTERS");
	quarters.setSpriteShape(1);
	quarters.setSpriteFacility(18);
	quarters.setBuildCost(400000);
	quarters.setBuildTime(16);
	quarters.setMonthlyCost(10000);
	quarters.setPersonnel(50);

	RuleBaseFacility lab = new RuleBaseFacility("STR_LABORATORY");
	lab.setSpriteShape(1);
	lab.setSpriteFacility(19);
	lab.setBuildCost(750000);
	lab.setBuildTime(26);
	lab.setMonthlyCost(30000);
	lab.setLaboratories(50);

	RuleBaseFacility workshop = new RuleBaseFacility("STR_WORKSHOP");
	workshop.setSpriteShape(1);
	workshop.setSpriteFacility(20);
	workshop.setBuildCost(800000);
	workshop.setBuildTime(32);
	workshop.setMonthlyCost(35000);
	workshop.setWorkshops(50);

	RuleBaseFacility stores = new RuleBaseFacility("STR_GENERAL_STORES");
	stores.setSpriteShape(1);
	stores.setSpriteFacility(24);
	stores.setBuildCost(150000);
	stores.setBuildTime(10);
	stores.setMonthlyCost(5000);
	stores.setStorage(50);

	RuleBaseFacility aliens = new RuleBaseFacility("STR_ALIEN_CONTAINMENT");
	aliens.setSpriteShape(1);
	aliens.setSpriteFacility(25);
	aliens.setBuildCost(400000);
	aliens.setBuildTime(18);
	aliens.setMonthlyCost(15000);
	aliens.setAliens(10);

	RuleBaseFacility hangar = new RuleBaseFacility("STR_HANGAR");
	hangar.setSpriteShape(9);
	hangar.setSpriteFacility(9);
	hangar.setSize(2);
	hangar.setBuildCost(200000);
	hangar.setBuildTime(25);
	hangar.setMonthlyCost(25000);
	hangar.setCrafts(1);

	RuleBaseFacility sRadar = new RuleBaseFacility("STR_SMALL_RADAR_SYSTEM");
	sRadar.setSpriteShape(2);
	sRadar.setSpriteFacility(21);
	sRadar.setBuildCost(500000);
	sRadar.setBuildTime(12);
	sRadar.setMonthlyCost(10000);
	sRadar.setRadarRange(1500);
	sRadar.setRadarChance(10);

	RuleBaseFacility lRadar = new RuleBaseFacility("STR_LARGE_RADAR_SYSTEM");
	lRadar.setSpriteShape(1);
	lRadar.setSpriteFacility(22);
	lRadar.setBuildCost(800000);
	lRadar.setBuildTime(25);
	lRadar.setMonthlyCost(15000);
	lRadar.setRadarRange(2250);
	lRadar.setRadarChance(20);

	RuleBaseFacility missile = new RuleBaseFacility("STR_MISSILE_DEFENSES");
	missile.setSpriteShape(2);
	missile.setSpriteFacility(23);
	missile.setBuildCost(200000);
	missile.setBuildTime(16);
	missile.setMonthlyCost(5000);
	missile.setDefenceValue(500);
	missile.setHitRatio(50);

	_facilities.put("STR_ACCESS_LIFT", (RuleBaseFacility)lift);
	_facilities.put("STR_LIVING_QUARTERS", (RuleBaseFacility)quarters);
	_facilities.put("STR_LABORATORY", (RuleBaseFacility)lab);
	_facilities.put("STR_WORKSHOP", (RuleBaseFacility)workshop);
	_facilities.put("STR_GENERAL_STORES", (RuleBaseFacility)stores);
	_facilities.put("STR_ALIEN_CONTAINMENT", (RuleBaseFacility)aliens);
	_facilities.put("STR_HANGAR", (RuleBaseFacility)hangar);
	_facilities.put("STR_SMALL_RADAR_SYSTEM", (RuleBaseFacility)sRadar);
	_facilities.put("STR_LARGE_RADAR_SYSTEM", (RuleBaseFacility)lRadar);
	_facilities.put("STR_MISSILE_DEFENSES", (RuleBaseFacility)missile);

	// Add mapdatafiles
	_mapDataFiles.put("AVENGER",new MapDataSet("AVENGER",59));
	_mapDataFiles.put("BARN",new MapDataSet("BARN",29));
	_mapDataFiles.put("BLANKS",new MapDataSet("BLANKS",2));
	_mapDataFiles.put("BRAIN",new MapDataSet("BRAIN",4));
	_mapDataFiles.put("CULTIVAT",new MapDataSet("CULTIVAT",37));
	_mapDataFiles.put("DESERT",new MapDataSet("DESERT",66));
	_mapDataFiles.put("FOREST",new MapDataSet("FOREST",83));
	_mapDataFiles.put("FRNITURE",new MapDataSet("FRNITURE",26));
	_mapDataFiles.put("JUNGLE",new MapDataSet("JUNGLE",82));
	_mapDataFiles.put("LIGHTNIN",new MapDataSet("LIGHTNIN",42));
	_mapDataFiles.put("MARS",new MapDataSet("MARS",36));
	_mapDataFiles.put("MOUNT",new MapDataSet("MOUNT",78));
	_mapDataFiles.put("PLANE",new MapDataSet("PLANE",65));
	_mapDataFiles.put("POLAR",new MapDataSet("POLAR",81));
	_mapDataFiles.put("ROADS",new MapDataSet("ROADS",23));
	_mapDataFiles.put("UFO1",new MapDataSet("UFO1",20));
	_mapDataFiles.put("URBAN",new MapDataSet("URBAN",112));
	_mapDataFiles.put("URBITS",new MapDataSet("URBITS",25));
	_mapDataFiles.put("U_BASE",new MapDataSet("U_BASE",67));
	_mapDataFiles.put("U_BITS",new MapDataSet("U_BITS",8));
	_mapDataFiles.put("U_DISEC2",new MapDataSet("U_DISEC2",17));
	_mapDataFiles.put("U_EXT02",new MapDataSet("U_EXT02",34));
	_mapDataFiles.put("U_OPER2",new MapDataSet("U_OPER2",15));
	_mapDataFiles.put("U_PODS",new MapDataSet("U_PODS",11));
	_mapDataFiles.put("U_WALL02",new MapDataSet("U_WALL02",47));
	_mapDataFiles.put("XBASE1",new MapDataSet("XBASE1",97));
	_mapDataFiles.put("XBASE2",new MapDataSet("XBASE2",62));

	// Add crafts
	RuleCraft skyranger = new RuleCraft("STR_SKYRANGER");
	skyranger.setSprite(0);
	skyranger.setMaxSpeed(760);
	skyranger.setAcceleration(2);
	skyranger.setMaxFuel(1500);
	skyranger.setWeapons(0);
	skyranger.setMaxDamage(150);
	skyranger.setSoldiers(14);
	skyranger.setCost(500000);
	skyranger.setHWPs(3);
	skyranger.setRefuelRate(50);
	skyranger.setTransferTime(72);

	RuleTerrain ruleTerrain = new RuleTerrain("PLANE");
	skyranger.setBattlescapeTerrainData(ruleTerrain);
	ruleTerrain.getMapDataSets().add(getMapDataSet("BLANKS"));
	ruleTerrain.getMapDataSets().add(getMapDataSet("PLANE"));
	ruleTerrain.getMapBlocks().add(new MapBlock(ruleTerrain,"PLANE",10,20,true));

	RuleCraft lightning = new RuleCraft("STR_LIGHTNING");
	lightning.setSprite(1);
	lightning.setMaxSpeed(3100);
	lightning.setAcceleration(9);
	lightning.setMaxFuel(30);
	lightning.setWeapons(1);
	lightning.setMaxDamage(800);
	lightning.setSoldiers(12);
	lightning.setHWPs(0);
	lightning.setRefuelRate(5);

	RuleCraft avenger = new RuleCraft("STR_AVENGER");
	avenger.setSprite(2);
	avenger.setMaxSpeed(5400);
	avenger.setAcceleration(10);
	avenger.setMaxFuel(60);
	avenger.setWeapons(2);
	avenger.setMaxDamage(1200);
	avenger.setSoldiers(26);
	avenger.setHWPs(4);
	avenger.setRefuelRate(5);

	RuleCraft interceptor = new RuleCraft("STR_INTERCEPTOR");
	interceptor.setSprite(3);
	interceptor.setMaxSpeed(2100);
	interceptor.setAcceleration(3);
	interceptor.setMaxFuel(1000);
	interceptor.setWeapons(2);
	interceptor.setMaxDamage(100);
	interceptor.setSoldiers(0);
	interceptor.setCost(600000);
	interceptor.setHWPs(0);
	interceptor.setRefuelRate(50);
	interceptor.setTransferTime(96);

	RuleCraft firestorm = new RuleCraft("STR_FIRESTORM");
	firestorm.setSprite(4);
	firestorm.setMaxSpeed(4200);
	firestorm.setAcceleration(9);
	firestorm.setMaxFuel(20);
	firestorm.setWeapons(2);
	firestorm.setMaxDamage(50);
	firestorm.setSoldiers(0);
	firestorm.setHWPs(0);
	firestorm.setRefuelRate(5);

	_crafts.put("STR_SKYRANGER", (RuleCraft)skyranger);
	_crafts.put("STR_LIGHTNING", (RuleCraft)lightning);
	_crafts.put("STR_AVENGER", (RuleCraft)avenger);
	_crafts.put("STR_INTERCEPTOR", (RuleCraft)interceptor);
	_crafts.put("STR_FIRESTORM", (RuleCraft)firestorm);

	// Add craft weapons
	RuleCraftWeapon stingray = new RuleCraftWeapon("STR_STINGRAY");
	stingray.setSprite(0);
	stingray.setSound(8);
	stingray.setDamage(70);
	stingray.setRange(30);
	stingray.setAccuracy(70);
	stingray.setCautiousReload(32);
	stingray.setStandardReload(24);
	stingray.setAggressiveReload(16);
	stingray.setAmmoMax(6);
	stingray.setLauncherItem("STR_STINGRAY_LAUNCHER");
	stingray.setClipItem("STR_STINGRAY_MISSILES");

	RuleCraftWeapon avalanche = new RuleCraftWeapon("STR_AVALANCHE");
	avalanche.setSprite(1);
	avalanche.setSound(8);
	avalanche.setDamage(100);
	avalanche.setRange(60);
	avalanche.setAccuracy(80);
	avalanche.setCautiousReload(48);
	avalanche.setStandardReload(36);
	avalanche.setAggressiveReload(24);
	avalanche.setAmmoMax(3);
	avalanche.setLauncherItem("STR_AVALANCHE_LAUNCHER");
	avalanche.setClipItem("STR_AVALANCHE_MISSILES");

	RuleCraftWeapon cannon = new RuleCraftWeapon("STR_CANNON_UC");
	cannon.setSprite(2);
	cannon.setSound(4);
	cannon.setDamage(10);
	cannon.setRange(10);
	cannon.setAccuracy(25);
	cannon.setCautiousReload(2);
	cannon.setStandardReload(2);
	cannon.setAggressiveReload(2);
	cannon.setAmmoMax(200);
	cannon.setRearmRate(50);
	cannon.setLauncherItem("STR_CANNON");
	cannon.setClipItem("STR_CANNON_ROUNDS_X50");

	RuleCraftWeapon laser = new RuleCraftWeapon("STR_LASER_CANNON_UC");
	laser.setSprite(4);
	laser.setSound(5);
	laser.setDamage(70);
	laser.setRange(21);
	laser.setAccuracy(35);
	laser.setCautiousReload(12);
	laser.setStandardReload(12);
	laser.setAggressiveReload(12);
	laser.setAmmoMax(99);
	laser.setLauncherItem("STR_LASER_CANNON");

	RuleCraftWeapon plasma = new RuleCraftWeapon("STR_PLASMA_BEAM_UC");
	plasma.setSprite(5);
	plasma.setSound(9);
	plasma.setDamage(140);
	plasma.setRange(52);
	plasma.setAccuracy(50);
	plasma.setCautiousReload(12);
	plasma.setStandardReload(12);
	plasma.setAggressiveReload(12);
	plasma.setAmmoMax(100);
	plasma.setLauncherItem("STR_PLASMA_BEAM");

	RuleCraftWeapon fusion = new RuleCraftWeapon("STR_FUSION_BALL_UC");
	fusion.setSprite(3);
	fusion.setSound(7);
	fusion.setDamage(230);
	fusion.setRange(65);
	fusion.setAccuracy(100);
	fusion.setCautiousReload(32);
	fusion.setStandardReload(24);
	fusion.setAggressiveReload(16);
	fusion.setAmmoMax(2);
	fusion.setLauncherItem("STR_FUSION_BALL_LAUNCHER");
	fusion.setClipItem("STR_FUSION_BAL");

	_craftWeapons.put("STR_STINGRAY", (RuleCraftWeapon)stingray);
	_craftWeapons.put("STR_AVALANCHE", (RuleCraftWeapon)avalanche);
	_craftWeapons.put("STR_CANNON_UC", (RuleCraftWeapon)cannon);
	_craftWeapons.put("STR_FUSION_BALL_UC", (RuleCraftWeapon)fusion);
	_craftWeapons.put("STR_LASER_CANNON_UC", (RuleCraftWeapon)laser);
	_craftWeapons.put("STR_PLASMA_BEAM_UC", (RuleCraftWeapon)plasma);

	// Add items
	RuleItem slauncher = new RuleItem("STR_STINGRAY_LAUNCHER");
	slauncher.setSize(0.8f);
	slauncher.setCost(16000);
	slauncher.setTransferTime(48);

	RuleItem alauncher = new RuleItem("STR_AVALANCHE_LAUNCHER");
	alauncher.setSize(1.0f);
	alauncher.setCost(17000);
	alauncher.setTransferTime(48);

	RuleItem icannon = new RuleItem("STR_CANNON");
	icannon.setSize(1.5f);
	icannon.setCost(30000);
	icannon.setTransferTime(48);

	RuleItem smissile = new RuleItem("STR_STINGRAY_MISSILES");
	smissile.setSize(0.4f);
	smissile.setCost(3000);
	smissile.setTransferTime(48);

	RuleItem amissile = new RuleItem("STR_AVALANCHE_MISSILES");
	amissile.setSize(1.5f);
	amissile.setCost(9000);
	amissile.setTransferTime(48);

	RuleItem crounds = new RuleItem("STR_CANNON_ROUNDS_X50");
	crounds.setSize(0.0f);
	crounds.setCost(1240);
	crounds.setTransferTime(96);

	RuleItem pistol = new RuleItem("STR_PISTO");
	pistol.setSize(0.1f);
	pistol.setCost(800);
	pistol.setBigSprite(3);
	pistol.setHandSprite(96);
	pistol.setBulletSprite(1);
	pistol.setFireSound(4);
	pistol.setAccuracySnap(60);
	pistol.setTUSnap(18);
	pistol.setAccuracyAimed(78);
	pistol.setTUAimed(30);
	pistol.getCompatibleAmmo().add("STR_PISTOL_CLIP");
	pistol.setBattleType(BattleType.BT_FIREARM);
	pistol.setSizeX(1);
	pistol.setSizeY(2);
	pistol.setWeight(5);

	RuleItem pclip = new RuleItem("STR_PISTOL_CLIP");
	pclip.setSize(0.1f);
	pclip.setCost(70);
	pclip.setBigSprite(4);
	pclip.setHandSprite(120);
	pclip.setPower(26);
	pclip.setDamageType(ItemDamageType.DT_AP);
	pclip.setBattleType(BattleType.BT_AMMO);
	pclip.setHitAnimation(26);
	pclip.setHitSound(22);
	pclip.setSizeX(1);
	pclip.setSizeY(1);
	pclip.setClipSize(12);
	pclip.setWeight(3);
	
	RuleItem rifle = new RuleItem("STR_RIFLE");
	rifle.setSize(0.2f);
	rifle.setCost(3000);
	rifle.setBigSprite(1);
	rifle.setHandSprite(0);
	rifle.setTwoHanded(true);
	rifle.setBulletSprite(2);
	rifle.setFireSound(4);
	rifle.setAccuracyAuto(35);
	rifle.setTUAuto(35);
	rifle.setAccuracySnap(60);
	rifle.setTUSnap(25);
	rifle.setAccuracyAimed(110);
	rifle.setTUAimed(80);
	rifle.getCompatibleAmmo().add("STR_RIFLE_CLIP");
	rifle.setBattleType(BattleType.BT_FIREARM);
	rifle.setSizeX(1);
	rifle.setSizeY(3);
	rifle.setWeight(8);

	RuleItem rclip = new RuleItem("STR_RIFLE_CLIP");
	rclip.setSize(0.1f);
	rclip.setCost(200);
	rclip.setBigSprite(2);
	rclip.setHandSprite(120);
	rclip.setPower(30);
	rclip.setDamageType(ItemDamageType.DT_AP);
	rclip.setBattleType(BattleType.BT_AMMO);
	rclip.setHitAnimation(26);
	rclip.setHitSound(22);
	rclip.setSizeX(1);
	rclip.setSizeY(1);
	rclip.setClipSize(20);
	rclip.setWeight(3);

	RuleItem hcannon = new RuleItem("STR_HEAVY_CANNON");
	hcannon.setSize(0.3f);
	hcannon.setCost(6400);
	hcannon.setBigSprite(11);
	hcannon.setHandSprite(24);
	hcannon.setTwoHanded(true);
	hcannon.setBulletSprite(4);
	hcannon.setFireSound(2);
	hcannon.setAccuracySnap(60);
	hcannon.setTUSnap(33);
	hcannon.setAccuracyAimed(90);
	hcannon.setTUAimed(80);
	hcannon.getCompatibleAmmo().add("STR_HC_AP_AMMO");
	hcannon.getCompatibleAmmo().add("STR_HC_HE_AMMO");
	hcannon.getCompatibleAmmo().add("STR_HC_IN_AMMO");
	hcannon.setBattleType(BattleType.BT_FIREARM);
	hcannon.setSizeX(2);
	hcannon.setSizeY(3);
	hcannon.setWeight(18);

	RuleItem hcap = new RuleItem("STR_HC_AP_AMMO");
	hcap.setSize(0.1f);
	hcap.setCost(300);
	hcap.setBigSprite(12);
	hcap.setHandSprite(120);
	hcap.setPower(56);
	hcap.setDamageType(ItemDamageType.DT_AP);
	hcap.setBattleType(BattleType.BT_AMMO);
	hcap.setHitAnimation(26);
	hcap.setHitSound(13);
	hcap.setSizeX(1);
	hcap.setSizeY(1);
	hcap.setClipSize(6);
	hcap.setWeight(6);

	RuleItem hche = new RuleItem("STR_HC_HE_AMMO");
	hche.setSize(0.1f);
	hche.setCost(500);
	hche.setBigSprite(13);
	hche.setHandSprite(120);
	hche.setPower(52);
	hche.setDamageType(ItemDamageType.DT_HE);
	hche.setBattleType(BattleType.BT_AMMO);
	hche.setHitAnimation(0);
	hche.setHitSound(0);
	hche.setSizeX(1);
	hche.setSizeY(1);
	hche.setClipSize(6);
	hche.setWeight(6);

	RuleItem hcin = new RuleItem("STR_HC_IN_AMMO");
	hcin.setSize(0.1f);
	hcin.setCost(400);
	hcin.setBigSprite(14);
	hcin.setHandSprite(120);
	hcin.setPower(60);
	hcin.setDamageType(ItemDamageType.DT_IN);
	hcin.setBattleType(BattleType.BT_AMMO);
	hcin.setHitAnimation(0);
	hcin.setHitSound(0);
	hcin.setSizeX(1);
	hcin.setSizeY(1);
	hcin.setClipSize(6);
	hcin.setWeight(6);

	RuleItem acannon = new RuleItem("STR_AUTO_CANNON");
	acannon.setSize(0.3f);
	acannon.setCost(13500);
	acannon.setBigSprite(7);
	acannon.setHandSprite(32);
	acannon.setTwoHanded(true);
	acannon.setBulletSprite(3);
	acannon.setFireSound(2);
	acannon.setAccuracyAuto(32);
	acannon.setTUAuto(40);
	acannon.setAccuracySnap(56);
	acannon.setTUSnap(33);
	acannon.setAccuracyAimed(82);
	acannon.setTUAimed(80);
	acannon.getCompatibleAmmo().add("STR_AC_AP_AMMO");
	acannon.getCompatibleAmmo().add("STR_AC_HE_AMMO");
	acannon.getCompatibleAmmo().add("STR_AC_IN_AMMO");
	acannon.setBattleType(BattleType.BT_FIREARM);
	acannon.setSizeX(2);
	acannon.setSizeY(3);
	acannon.setWeight(19);
	
	RuleItem acap = new RuleItem("STR_AC_AP_AMMO");
	acap.setSize(0.1f);
	acap.setCost(500);
	acap.setBigSprite(8);
	acap.setHandSprite(120);
	acap.setPower(42);
	acap.setDamageType(ItemDamageType.DT_AP);
	acap.setBattleType(BattleType.BT_AMMO);
	acap.setHitSound(13);
	acap.setHitAnimation(26);
	acap.setSizeX(1);
	acap.setSizeY(1);
	acap.setClipSize(14);
	acap.setWeight(5);

	RuleItem rlauncher = new RuleItem("STR_ROCKET_LAUNCHER");
	rlauncher.setSize(0.4f);
	rlauncher.setCost(4000);
	rlauncher.setBigSprite(15);
	rlauncher.setHandSprite(9);
	rlauncher.setTwoHanded(true);
	rlauncher.setBulletSprite(0);
	rlauncher.setFireSound(52);
	rlauncher.setAccuracySnap(55);
	rlauncher.setTUSnap(45);
	rlauncher.setAccuracyAimed(115);
	rlauncher.setTUAimed(75);
	rlauncher.getCompatibleAmmo().add("STR_SMALL_ROCKET");
	rlauncher.setBattleType(BattleType.BT_FIREARM);
	rlauncher.setSizeX(2);
	rlauncher.setSizeY(3);
	rlauncher.setWeight(10);

	RuleItem srocket = new RuleItem("STR_SMALL_ROCKET");
	srocket.setSize(0.2f);
	srocket.setCost(600);
	srocket.setBigSprite(16);
	srocket.setHandSprite(120);
	srocket.setPower(75);
	srocket.setDamageType(ItemDamageType.DT_HE);
	srocket.setBattleType(BattleType.BT_AMMO);
	srocket.setHitSound(0);
	srocket.setHitAnimation(0);
	srocket.setSizeX(1);
	srocket.setSizeY(3);
	srocket.setClipSize(1);
	srocket.setWeight(6);

	RuleItem grenade = new RuleItem("STR_GRENADE");
	grenade.setSize(0.1f);
	grenade.setCost(300);
	grenade.setBigSprite(19);
	grenade.setHandSprite(120);
	grenade.setPower(50);
	grenade.setDamageType(ItemDamageType.DT_HE);
	grenade.setBattleType(BattleType.BT_GRENADE);
	grenade.setSizeX(1);
	grenade.setSizeY(1);
	grenade.setWeight(3);

	RuleItem sgrenade = new RuleItem("STR_SMOKE_GRENADE");
	sgrenade.setSize(0.1f);
	sgrenade.setCost(150);
	sgrenade.setBigSprite(20);
	sgrenade.setHandSprite(120);
	sgrenade.setPower(60);
	sgrenade.setDamageType(ItemDamageType.DT_SMOKE);
	sgrenade.setBattleType(BattleType.BT_GRENADE);
	sgrenade.setSizeX(1);
	sgrenade.setSizeY(1);
	sgrenade.setWeight(3);

	RuleItem corpse = new RuleItem("STR_CORPSE");
	corpse.setBigSprite(45);
	corpse.setFloorSprite(39);
	corpse.setSizeX(2);
	corpse.setSizeY(3);
	corpse.setWeight(22);

	RuleItem scorpse = new RuleItem("STR_SECTOID_CORPSE");
	scorpse.setBigSprite(46);
	scorpse.setFloorSprite(42);
	scorpse.setSizeX(2);
	scorpse.setSizeY(3);
	scorpse.setWeight(30);

	_items.put("STR_STINGRAY_LAUNCHER", slauncher);
	_items.put("STR_AVALANCHE_LAUNCHER", alauncher);
	_items.put("STR_CANNON", icannon);
	_items.put("STR_STINGRAY_MISSILES", smissile);
	_items.put("STR_AVALANCHE_MISSILES", amissile);
	_items.put("STR_CANNON_ROUNDS_X50", crounds);
	_items.put("STR_PISTO", pistol);
	_items.put("STR_PISTOL_CLIP", pclip);
	_items.put("STR_RIFLE", rifle);
	_items.put("STR_RIFLE_CLIP", rclip);
	_items.put("STR_HEAVY_CANNON", hcannon);
	_items.put("STR_HC_AP_AMMO", hcap);
	_items.put("STR_HC_HE_AMMO", hche);
	_items.put("STR_HC_IN_AMMO", hcin);
	_items.put("STR_AUTO_CANNON", acannon);
	_items.put("STR_AC_AP_AMMO", acap);
	_items.put("STR_ROCKET_LAUNCHER", rlauncher);
	_items.put("STR_SMALL_ROCKET", srocket);
	_items.put("STR_GRENADE", grenade);
	_items.put("STR_SMOKE_GRENADE", sgrenade);
	_items.put("STR_CORPSE", corpse);
	_items.put("STR_SECTOID_CORPSE", scorpse);

	// Add UFOs
	RuleUfo sscout = new RuleUfo("STR_SMALL_SCOUT");
	sscout.setSize("STR_VERY_SMAL");
	sscout.setSprite(0);
	sscout.setMaxSpeed(2200);
	sscout.setAcceleration(12);
	sscout.setMaxDamage(50);
	sscout.setScore(50);
	ruleTerrain = new RuleTerrain("UFO1A");
	sscout.setBattlescapeTerrainData(ruleTerrain);
	ruleTerrain.getMapDataSets().add(getMapDataSet("BLANKS"));
	ruleTerrain.getMapDataSets().add(getMapDataSet("UFO1"));
	ruleTerrain.getMapBlocks().add(new MapBlock(ruleTerrain,"UFO1A",10,10,true));

	RuleUfo mscout = new RuleUfo("STR_MEDIUM_SCOUT");
	mscout.setSize("STR_SMAL");
	mscout.setSprite(1);
	mscout.setMaxSpeed(2400);
	mscout.setAcceleration(9);
	mscout.setWeaponPower(20);
	mscout.setWeaponRange(15);
	mscout.setMaxDamage(200);
	mscout.setScore(75);
	ruleTerrain = new RuleTerrain("UFO_110");
	mscout.setBattlescapeTerrainData(ruleTerrain);
	ruleTerrain.getMapDataSets().add(getMapDataSet("BLANKS"));
	ruleTerrain.getMapDataSets().add(getMapDataSet("U_EXT02"));
	ruleTerrain.getMapDataSets().add(getMapDataSet("U_WALL02"));
	ruleTerrain.getMapDataSets().add(getMapDataSet("U_BITS"));
//  ruleTerrain.getMapDataSets().add(getMapDataSet("U_DISEC2"));
//	ruleTerrain.getMapDataSets().add(getMapDataSet("U_OPER2"));
//  ruleTerrain.getMapDataSets().add(getMapDataSet("U_PODS"));
	ruleTerrain.getMapBlocks().add(new MapBlock(ruleTerrain,"UFO_110",10,10,true));

	RuleUfo lscout = new RuleUfo("STR_LARGE_SCOUT");
	lscout.setSize("STR_SMAL");
	lscout.setSprite(2);
	lscout.setMaxSpeed(2700);
	lscout.setAcceleration(9);
	lscout.setWeaponPower(20);
	lscout.setWeaponRange(34);
	lscout.setMaxDamage(250);
	lscout.setScore(125);
	ruleTerrain = new RuleTerrain("UFO_120");
	lscout.setBattlescapeTerrainData(ruleTerrain);
	ruleTerrain.getMapDataSets().add(getMapDataSet("BLANKS"));
	ruleTerrain.getMapDataSets().add(getMapDataSet("U_EXT02"));
	ruleTerrain.getMapDataSets().add(getMapDataSet("U_WALL02"));
	ruleTerrain.getMapDataSets().add(getMapDataSet("U_BITS"));
	ruleTerrain.getMapBlocks().add(new MapBlock(ruleTerrain,"UFO_120",20,20,true));

	RuleUfo abducter = new RuleUfo("STR_ABDUCTER");
	abducter.setSize("STR_MEDIUM");
	abducter.setSprite(3);
	abducter.setMaxSpeed(4000);
	abducter.setAcceleration(8);
	abducter.setWeaponPower(40);
	abducter.setWeaponRange(22);
	abducter.setMaxDamage(500);
	abducter.setScore(250);

	RuleUfo harvester = new RuleUfo("STR_HARVESTER");
	harvester.setSize("STR_MEDIUM");
	harvester.setSprite(4);
	harvester.setMaxSpeed(4300);
	harvester.setAcceleration(8);
	harvester.setWeaponPower(40);
	harvester.setWeaponRange(20);
	harvester.setMaxDamage(500);
	harvester.setScore(250);

	RuleUfo supply = new RuleUfo("STR_SUPPLY_SHIP");
	supply.setSize("STR_LARGE");
	supply.setSprite(5);
	supply.setMaxSpeed(3200);
	supply.setAcceleration(6);
	supply.setWeaponPower(60);
	supply.setWeaponRange(36);
	supply.setMaxDamage(2200);
	supply.setScore(400);

	RuleUfo terror = new RuleUfo("STR_TERROR_SHIP");
	terror.setSize("STR_LARGE");
	terror.setSprite(6);
	terror.setMaxSpeed(4800);
	terror.setAcceleration(6);
	terror.setWeaponPower(120);
	terror.setWeaponRange(42);
	terror.setMaxDamage(1200);
	terror.setScore(500);

	RuleUfo battleship = new RuleUfo("STR_BATTLESHIP");
	battleship.setSize("STR_VERY_LARGE");
	battleship.setSprite(7);
	battleship.setMaxSpeed(5000);
	battleship.setAcceleration(6);
	battleship.setWeaponPower(148);
	battleship.setWeaponRange(65);
	battleship.setMaxDamage(3200);
	battleship.setScore(700);

	_ufos.put("STR_SMALL_SCOUT", sscout);
	_ufos.put("STR_MEDIUM_SCOUT", mscout);
	_ufos.put("STR_LARGE_SCOUT", lscout);
	_ufos.put("STR_ABDUCTER", abducter);
	_ufos.put("STR_HARVESTER", harvester);
	_ufos.put("STR_SUPPLY_SHIP", supply);
	_ufos.put("STR_TERROR_SHIP", terror);
	_ufos.put("STR_BATTLESHIP", battleship);

	RuleTerrain culta = new RuleTerrain("CULTA");
	culta.getMapDataSets().add(getMapDataSet("BLANKS"));
	culta.getMapDataSets().add(getMapDataSet("CULTIVAT"));
	culta.getMapDataSets().add(getMapDataSet("BARN"));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA00",10,10,true));
	//culta.getMapBlocks().add(new MapBlock(culta,"CULTA0B",10,10,false));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA01",10,10,false));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA02",10,10,false));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA03",10,10,false));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA04",10,10,false));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA05",10,10,false));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA06",10,10,false));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA07",10,10,true));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA08",10,10,false));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA09",10,10,false));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA10",10,10,false));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA11",10,10,true));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA12",10,10,false));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA13",10,10,false));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA14",10,10,false));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA15",10,10,false));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA16",10,10,false));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA17",10,10,false));
	culta.getMapBlocks().add(new MapBlock(culta,"CULTA18",10,10,false));
	RuleTerrain jungle = new RuleTerrain("JUNGLE");
	jungle.getMapDataSets().add(getMapDataSet("BLANKS"));
	jungle.getMapDataSets().add(getMapDataSet("JUNGLE"));
	jungle.getMapBlocks().add(new MapBlock(jungle,"JUNGLE00",10,10,true));
	jungle.getMapBlocks().add(new MapBlock(jungle,"JUNGLE01",10,10,true));
	jungle.getMapBlocks().add(new MapBlock(jungle,"JUNGLE02",10,10,true));
	jungle.getMapBlocks().add(new MapBlock(jungle,"JUNGLE03",10,10,false));
	jungle.getMapBlocks().add(new MapBlock(jungle,"JUNGLE04",10,10,false));
	jungle.getMapBlocks().add(new MapBlock(jungle,"JUNGLE05",10,10,false));
	jungle.getMapBlocks().add(new MapBlock(jungle,"JUNGLE06",10,10,false));
	jungle.getMapBlocks().add(new MapBlock(jungle,"JUNGLE07",10,10,false));
	jungle.getMapBlocks().add(new MapBlock(jungle,"JUNGLE08",10,10,false));
	jungle.getMapBlocks().add(new MapBlock(jungle,"JUNGLE09",10,10,false));
	jungle.getMapBlocks().add(new MapBlock(jungle,"JUNGLE10",10,10,false));
	jungle.getMapBlocks().add(new MapBlock(jungle,"JUNGLE11",10,10,false));
	RuleTerrain forest = new RuleTerrain("FOREST");
	forest.getMapDataSets().add(getMapDataSet("BLANKS"));
	forest.getMapDataSets().add(getMapDataSet("FOREST"));
	forest.getMapBlocks().add(new MapBlock(forest,"FOREST00",10,10,true));
	forest.getMapBlocks().add(new MapBlock(forest,"FOREST01",10,10,true));
	forest.getMapBlocks().add(new MapBlock(forest,"FOREST02",10,10,false));
	forest.getMapBlocks().add(new MapBlock(forest,"FOREST03",10,10,false));
	forest.getMapBlocks().add(new MapBlock(forest,"FOREST04",10,10,false));
	forest.getMapBlocks().add(new MapBlock(forest,"FOREST05",10,10,false));
	forest.getMapBlocks().add(new MapBlock(forest,"FOREST06",10,10,false));
	forest.getMapBlocks().add(new MapBlock(forest,"FOREST07",10,10,false));
	forest.getMapBlocks().add(new MapBlock(forest,"FOREST08",10,10,false));
	forest.getMapBlocks().add(new MapBlock(forest,"FOREST09",10,10,false));
	forest.getMapBlocks().add(new MapBlock(forest,"FOREST10",20,20,false));
	forest.getMapBlocks().add(new MapBlock(forest,"FOREST11",20,20,false));
	RuleTerrain desert = new RuleTerrain("DESERT");
	desert.getMapDataSets().add(getMapDataSet("BLANKS"));
	desert.getMapDataSets().add(getMapDataSet("DESERT"));
	desert.getMapBlocks().add(new MapBlock(desert,"DESERT00",10,10,true));
	desert.getMapBlocks().add(new MapBlock(desert,"DESERT01",10,10,true));
	desert.getMapBlocks().add(new MapBlock(desert,"DESERT02",10,10,true));
	desert.getMapBlocks().add(new MapBlock(desert,"DESERT03",10,10,false));
	desert.getMapBlocks().add(new MapBlock(desert,"DESERT04",10,10,false));
	desert.getMapBlocks().add(new MapBlock(desert,"DESERT05",10,10,false));
	desert.getMapBlocks().add(new MapBlock(desert,"DESERT06",10,10,false));
	desert.getMapBlocks().add(new MapBlock(desert,"DESERT07",10,10,false));
	desert.getMapBlocks().add(new MapBlock(desert,"DESERT08",10,10,false));
	desert.getMapBlocks().add(new MapBlock(desert,"DESERT09",20,20,false));
	desert.getMapBlocks().add(new MapBlock(desert,"DESERT10",20,20,false));
	desert.getMapBlocks().add(new MapBlock(desert,"DESERT11",20,20,false));
	RuleTerrain mount = new RuleTerrain("MOUNT");
	mount.getMapDataSets().add(getMapDataSet("BLANKS"));
	mount.getMapDataSets().add(getMapDataSet("MOUNT"));
	mount.getMapBlocks().add(new MapBlock(mount,"MOUNT00",10,10,true));
	mount.getMapBlocks().add(new MapBlock(mount,"MOUNT01",10,10,true));
	mount.getMapBlocks().add(new MapBlock(mount,"MOUNT02",10,10,true));
	mount.getMapBlocks().add(new MapBlock(mount,"MOUNT03",10,10,false));
	mount.getMapBlocks().add(new MapBlock(mount,"MOUNT04",10,10,false));
	mount.getMapBlocks().add(new MapBlock(mount,"MOUNT05",10,10,false));
	mount.getMapBlocks().add(new MapBlock(mount,"MOUNT06",10,10,false));
	mount.getMapBlocks().add(new MapBlock(mount,"MOUNT07",10,10,false));
	mount.getMapBlocks().add(new MapBlock(mount,"MOUNT08",10,10,false));
	mount.getMapBlocks().add(new MapBlock(mount,"MOUNT09",10,10,false));
	mount.getMapBlocks().add(new MapBlock(mount,"MOUNT10",10,10,false));
	mount.getMapBlocks().add(new MapBlock(mount,"MOUNT11",20,20,false));
	mount.getMapBlocks().add(new MapBlock(mount,"MOUNT12",20,20,false));
	RuleTerrain polar = new RuleTerrain("POLAR");
	polar.getMapDataSets().add(getMapDataSet("BLANKS"));
	polar.getMapDataSets().add(getMapDataSet("POLAR"));
	polar.getMapBlocks().add(new MapBlock(polar,"POLAR00",10,10,true));
	polar.getMapBlocks().add(new MapBlock(polar,"POLAR01",10,10,true));
	polar.getMapBlocks().add(new MapBlock(polar,"POLAR02",10,10,true));
	polar.getMapBlocks().add(new MapBlock(polar,"POLAR03",10,10,false));
	polar.getMapBlocks().add(new MapBlock(polar,"POLAR04",10,10,false));
	polar.getMapBlocks().add(new MapBlock(polar,"POLAR05",10,10,false));
	polar.getMapBlocks().add(new MapBlock(polar,"POLAR06",10,10,false));
	polar.getMapBlocks().add(new MapBlock(polar,"POLAR07",10,10,false));
	polar.getMapBlocks().add(new MapBlock(polar,"POLAR08",10,10,false));
	polar.getMapBlocks().add(new MapBlock(polar,"POLAR09",10,10,false));
	polar.getMapBlocks().add(new MapBlock(polar,"POLAR10",10,10,false));
	polar.getMapBlocks().add(new MapBlock(polar,"POLAR11",10,10,false));
	polar.getMapBlocks().add(new MapBlock(polar,"POLAR12",20,20,false));
	polar.getMapBlocks().add(new MapBlock(polar,"POLAR13",20,20,false));
	RuleTerrain mars = new RuleTerrain("MARS");
	mars.getMapDataSets().add(getMapDataSet("BLANKS"));
	mars.getMapDataSets().add(getMapDataSet("MARS"));
	mars.getMapDataSets().add(getMapDataSet("U_WALL02"));
	mars.getMapBlocks().add(new MapBlock(mars,"MARS00",10,10,true));
	RuleTerrain urban = new RuleTerrain("URBAN");
	urban.getMapDataSets().add(getMapDataSet("BLANKS"));
	urban.getMapDataSets().add(getMapDataSet("ROADS"));
	urban.getMapDataSets().add(getMapDataSet("URBITS"));
	urban.getMapDataSets().add(getMapDataSet("URBAN"));
	urban.getMapDataSets().add(getMapDataSet("FRNITURE"));
	//urban.getMapBlocks().add(new MapBlock(urban,"URBAN00",10,10,false));
	//urban.getMapBlocks().add(new MapBlock(urban,"URBAN01",10,10,false));
	//urban.getMapBlocks().add(new MapBlock(urban,"URBAN02",10,10,false));
	urban.getMapBlocks().add(new MapBlock(urban,"URBAN03",10,10,true));
	urban.getMapBlocks().add(new MapBlock(urban,"URBAN04",10,10,true));
	urban.getMapBlocks().add(new MapBlock(urban,"URBAN05",20,20,false));
	urban.getMapBlocks().add(new MapBlock(urban,"URBAN06",20,20,false));
	urban.getMapBlocks().add(new MapBlock(urban,"URBAN07",20,20,false));
	urban.getMapBlocks().add(new MapBlock(urban,"URBAN08",20,20,false));
	urban.getMapBlocks().add(new MapBlock(urban,"URBAN09",20,20,false));
	urban.getMapBlocks().add(new MapBlock(urban,"URBAN14",10,10,false));
	urban.getMapBlocks().add(new MapBlock(urban,"URBAN15",10,10,false));
	urban.getMapBlocks().add(new MapBlock(urban,"URBAN16",10,10,false));
	urban.getMapBlocks().add(new MapBlock(urban,"URBAN17",10,10,false));
	urban.getMapBlocks().add(new MapBlock(urban,"URBAN18",10,10,false));

	_terrains.put("CULTA",culta);
	_terrains.put("JUNGLE",jungle);
	_terrains.put("FOREST",forest);
	_terrains.put("DESERT",desert);
	_terrains.put("MOUNT",mount);
	_terrains.put("POLAR",polar);
	_terrains.put("MARS",mars);
	_terrains.put("URBAN",urban);

	RuleArmor coveralls = new RuleArmor("STR_NONE_UC", "XCOM_0.PCK");
	coveralls.setArmor(12, 8, 5, 2);
	coveralls.setCorpseItem("STR_CORPSE");

	RuleArmor personalArmor = new RuleArmor("STR_PERSONAL_ARMOR_UC", "XCOM_1.PCK");
	personalArmor.setArmor(50, 40, 40, 30);

	RuleArmor powerSuit = new RuleArmor("STR_POWER_SUIT_UC", "XCOM_2.PCK");
	powerSuit.setArmor(100, 80, 70, 60);

	RuleArmor flyingSuit = new RuleArmor("STR_FLYING_SUIT_UC", "XCOM_2.PCK");
	flyingSuit.setArmor(110, 90, 80, 70);

	RuleArmor sectoidSoldierArmor = new RuleArmor("SECTOID_ARMOR0", "SECTOID.PCK");
	sectoidSoldierArmor.setArmor(4, 3, 2, 2);
	sectoidSoldierArmor.setCorpseItem("STR_SECTOID_CORPSE");

	_armors.put("STR_NONE_UC", coveralls);
	_armors.put("STR_PERSONAL_ARMOR_UC", personalArmor);
	_armors.put("STR_POWER_SUIT_UC" ,powerSuit);
	_armors.put("STR_FLYING_SUIT_UC", flyingSuit);
	_armors.put("SECTOID_ARMOR0", sectoidSoldierArmor);

	RuleSoldier xcom = new RuleSoldier("XCOM");
	xcom.setArmor("STR_NONE_UC");
	UnitStats s1;
	s1.tu = 50;
	s1.stamina = 40;
	s1.health = 25;
	s1.bravery = 10;
	s1.reactions = 30;
	s1.firing = 40;
	s1.throwing = 50;
	s1.strength = 20;
	s1.psiStrength = 0;
	s1.psiSkill = 16;
	s1.melee = 20;
	UnitStats s2;
	s2.tu = 60;
	s2.stamina = 70;
	s2.health = 40;
	s2.bravery = 60;
	s2.reactions = 60;
	s2.firing = 70;
	s2.throwing = 80;
	s2.strength = 40;
	s2.psiStrength = 100;
	s2.psiSkill = 24;
	s2.melee = 40;
	xcom.setStats(s1, s2);
	xcom.setVoxelParameters(22, 14, 3);

	_soldiers.put("XCOM", xcom);

	
	RuleAlien sectoidSoldier = new RuleAlien("SECTOID_SOLDIER", "STR_SECTOID", "STR_LIVE_SOLDIER");
	sectoidSoldier.setArmor("SECTOID_ARMOR0");
	s1.tu = 54;
	s1.stamina = 90;
	s1.health = 30;
	s1.bravery = 80;
	s1.reactions = 63;
	s1.firing = 52;
	s1.throwing = 58;
	s1.strength = 30;
	s1.psiStrength = 40;
	s1.psiSkill = 0;
	s1.melee = 76;
	sectoidSoldier.setStats(s1);
	sectoidSoldier.setVoxelParameters(16, 12, 2);

	_aliens.put("SECTOID_SOLDIER", sectoidSoldier);

	// create Ufopaedia article definitions
	int sort_key = 1;
	
	// XCOM CRAFT AND ARMAMENT
	ArticleDefinitionCraft article_craft;
	article_craft.section = Ufopaedia.UFOPAEDIA_XCOM_CRAFT_ARMAMENT;

	article_craft.id = "STR_SKYRANGER";
	article_craft.title = "STR_SKYRANGER";
	article_craft.image_id = "UP004.SPK";
	article_craft.text = "STR_SKYRANGER_UFOPEDIA";
	article_craft.rect_text.set(5, 40, 140, 100);
	article_craft.craft = _crafts.get(article_craft.id);
	article_craft.rect_stats.set(160, 5, 140, 60);
	article_craft.sort_key = sort_key++;
	
	_ufopaediaArticles.put(article_craft.id,  new ArticleDefinitionCraft(article_craft));
	
	article_craft.id = "STR_LIGHTNING";
	article_craft.title = "STR_LIGHTNING";
	article_craft.image_id = "UP003.SPK";
	article_craft.text = "STR_LIGHTNING_UFOPEDIA";
	article_craft.rect_text.set(5, 40, 310, 60);
	article_craft.craft = _crafts.get(article_craft.id);
	article_craft.rect_stats.set(5, 132, 140, 60);
	article_craft.sort_key = sort_key++;
	
	_ufopaediaArticles.put(article_craft.id, new ArticleDefinitionCraft(article_craft));
	
	article_craft.id = "STR_AVENGER";
	article_craft.title = "STR_AVENGER";
	article_craft.image_id = "UP001.SPK";
	article_craft.text = "STR_AVENGER_UFOPEDIA";
	article_craft.rect_text.set(5, 40, 140, 100);
	article_craft.craft = _crafts.get(article_craft.id);
	article_craft.rect_stats.set(160, 5, 140, 60);
	article_craft.sort_key = sort_key++;
	
	_ufopaediaArticles.put(article_craft.id, new ArticleDefinitionCraft(article_craft));

	article_craft.id = "STR_INTERCEPTOR";
	article_craft.title = "STR_INTERCEPTOR";
	article_craft.image_id = "UP002.SPK";
	article_craft.text = "STR_INTERCEPTOR_UFOPEDIA";
	article_craft.rect_text.set(5, 40, 210, 60);
	article_craft.craft = _crafts.get(article_craft.id);
	article_craft.rect_stats.set(5, 110, 140, 60);
	article_craft.sort_key = sort_key++;
	
	_ufopaediaArticles.put(article_craft.id, new ArticleDefinitionCraft(article_craft));
	
	article_craft.id = "STR_FIRESTORM";
	article_craft.title = "STR_FIRESTORM";
	article_craft.image_id = "UP005.SPK";
	article_craft.text = "STR_FIRESTORM_UFOPEDIA";
	article_craft.rect_text.set(5, 40, 140, 100);
	article_craft.craft = _crafts.get(article_craft.id);
	article_craft.rect_stats.set(160, 5, 140, 60);
	article_craft.sort_key = sort_key++;
	
	_ufopaediaArticles.put(article_craft.id, new ArticleDefinitionCraft(article_craft));
	
	
	ArticleDefinitionCraftWeapon article_craft_weapon;
	article_craft_weapon.section = Ufopaedia.UFOPAEDIA_XCOM_CRAFT_ARMAMENT;
	
	article_craft_weapon.id = "STR_STINGRAY";
	article_craft_weapon.title = "STR_STINGRAY";
	article_craft_weapon.image_id = "UP006.SPK";
	article_craft_weapon.weapon = _craftWeapons.get(article_craft_weapon.id);
	article_craft.sort_key = sort_key++;
	
	_ufopaediaArticles.put(article_craft_weapon.id, new ArticleDefinitionCraftWeapon(article_craft_weapon));
	
	article_craft_weapon.id = "STR_AVALANCHE";
	article_craft_weapon.title = "STR_AVALANCHE";
	article_craft_weapon.image_id = "UP007.SPK";
	article_craft_weapon.weapon = _craftWeapons.get(article_craft_weapon.id);
	article_craft.sort_key = sort_key++;
	
	_ufopaediaArticles.put(article_craft_weapon.id, new ArticleDefinitionCraftWeapon(article_craft_weapon));
	
	article_craft_weapon.id = "STR_CANNON_UC";
	article_craft_weapon.title = "STR_CANNON_UC";
	article_craft_weapon.image_id = "UP008.SPK";
	article_craft_weapon.weapon = _craftWeapons.get(article_craft_weapon.id);
	article_craft.sort_key = sort_key++;
	
	_ufopaediaArticles.put(article_craft_weapon.id, new ArticleDefinitionCraftWeapon(article_craft_weapon));
	
	article_craft_weapon.id = "STR_FUSION_BALL_UC";
	article_craft_weapon.title = "STR_FUSION_BALL_UC";
	article_craft_weapon.image_id = "UP009.SPK";
	article_craft_weapon.weapon = _craftWeapons.get(article_craft_weapon.id);
	article_craft.sort_key = sort_key++;
	
	_ufopaediaArticles.put(article_craft_weapon.id, new ArticleDefinitionCraftWeapon(article_craft_weapon));
	
	article_craft_weapon.id = "STR_LASER_CANNON_UC";
	article_craft_weapon.title = "STR_LASER_CANNON_UC";
	article_craft_weapon.image_id = "UP010.SPK";
	article_craft_weapon.weapon = _craftWeapons.get(article_craft_weapon.id);
	article_craft.sort_key = sort_key++;
	
	_ufopaediaArticles.put(article_craft_weapon.id, new ArticleDefinitionCraftWeapon(article_craft_weapon));
	
	article_craft_weapon.id = "STR_PLASMA_BEAM_UC";
	article_craft_weapon.title = "STR_PLASMA_BEAM_UC";
	article_craft_weapon.image_id = "UP011.SPK";
	article_craft_weapon.weapon = _craftWeapons.get(article_craft_weapon.id);
	article_craft.sort_key = sort_key++;
	
	_ufopaediaArticles.put(article_craft_weapon.id, new ArticleDefinitionCraftWeapon(article_craft_weapon));
	
	
	// ALIEN LIFE FORMS
	ArticleDefinitionTextImage article_textimage;
	article_textimage.text_width = 100;
	article_textimage.section = Ufopaedia.UFOPAEDIA_ALIEN_LIFE_FORMS;

	article_textimage.id = "STR_SECTOID";
	article_textimage.title = "STR_SECTOID";
	article_textimage.image_id = "UP024.SPK";
	article_textimage.text = "STR_SECTOID_UFOPEDIA";
	article_textimage.sort_key = sort_key++;

	_ufopaediaArticles.put(article_textimage.id, new ArticleDefinitionTextImage(article_textimage));
	
	article_textimage.id = "STR_SNAKEMAN";
	article_textimage.title = "STR_SNAKEMAN";
	article_textimage.image_id = "UP030.SPK";
	article_textimage.text = "STR_SNAKEMAN_UFOPEDIA";
	article_textimage.sort_key = sort_key++;

	_ufopaediaArticles.put(article_textimage.id, new ArticleDefinitionTextImage(article_textimage));

	// ALIEN RESEARCH
	ArticleDefinitionText article_text;
	article_text.section = Ufopaedia.UFOPAEDIA_ALIEN_RESEARCH;

	article_text.id = "STR_ALIEN_ORIGINS";
	article_text.title = "STR_ALIEN_ORIGINS";
	article_text.text = "STR_ALIEN_ORIGINS_UFOPEDIA";
	article_text.sort_key = sort_key++;
	
	_ufopaediaArticles.put(article_text.id, new ArticleDefinitionText(article_text));

	_costSoldier = 20000;
	_costEngineer = 25000;
	_costScientist = 30000;
	_timePersonnel = 72;
}

/**
 * Generates a brand new saved game with the default countries
 * and a base with all the starting equipment.
 * @param diff Difficulty for the save.
 * @return New saved game.
 */
public SavedGame newSave(GameDifficulty diff)
{
	SavedGame save = new SavedGame(diff);

	// Add countries
	save.getCountries().add(new Country(getCountry("STR_USA")));
	save.getCountries().add(new Country(getCountry("STR_RUSSIA")));
	save.getCountries().add(new Country(getCountry("STR_UK")));
	save.getCountries().add(new Country(getCountry("STR_FRANCE")));
	save.getCountries().add(new Country(getCountry("STR_GERMANY")));
	save.getCountries().add(new Country(getCountry("STR_ITALY")));
	save.getCountries().add(new Country(getCountry("STR_SPAIN")));
	save.getCountries().add(new Country(getCountry("STR_CHINA")));
	save.getCountries().add(new Country(getCountry("STR_JAPAN")));
	save.getCountries().add(new Country(getCountry("STR_INDIA")));
	save.getCountries().add(new Country(getCountry("STR_BRAZI")));
	save.getCountries().add(new Country(getCountry("STR_AUSTRALIA")));
	save.getCountries().add(new Country(getCountry("STR_NIGERIA")));
	save.getCountries().add(new Country(getCountry("STR_SOUTH_AFRICA")));
	save.getCountries().add(new Country(getCountry("STR_EGYPT")));
	save.getCountries().add(new Country(getCountry("STR_CANADA")));
	save.setFunds(save.getCountryFunding());

	// Add regions
	save.getRegions().add(new Region(getRegion("STR_NORTH_AMERICA")));
	save.getRegions().add(new Region(getRegion("STR_ARCTIC")));
	save.getRegions().add(new Region(getRegion("STR_ANTARCTICA")));
	save.getRegions().add(new Region(getRegion("STR_SOUTH_AMERICA")));
	save.getRegions().add(new Region(getRegion("STR_EUROPE")));
	save.getRegions().add(new Region(getRegion("STR_NORTH_AFRICA")));
	save.getRegions().add(new Region(getRegion("STR_SOUTHERN_AFRICA")));
	save.getRegions().add(new Region(getRegion("STR_CENTRAL_ASIA")));
	save.getRegions().add(new Region(getRegion("STR_SOUTH_EAST_ASIA")));
	save.getRegions().add(new Region(getRegion("STR_SIBERIA")));
	save.getRegions().add(new Region(getRegion("STR_AUSTRALASIA")));
	save.getRegions().add(new Region(getRegion("STR_PACIFIC")));
	save.getRegions().add(new Region(getRegion("STR_NORTH_ATLANTIC")));
	save.getRegions().add(new Region(getRegion("STR_SOUTH_ATLANTIC")));
	save.getRegions().add(new Region(getRegion("STR_INDIAN_OCEAN")));
	
	// Set up craft IDs
	save.getCraftIds().put("STR_SKYRANGER", 1);
	save.getCraftIds().put("STR_LIGHTNING", 1);
	save.getCraftIds().put("STR_AVENGER", 1);
	save.getCraftIds().put("STR_INTERCEPTOR", 1);
	save.getCraftIds().put("STR_FIRESTORM", 1);

	// Set up initial base
	Base base = new Base(this);
	base.setEngineers(10);
	base.setScientists(10);

	// Add facilities
	base.getFacilities().add(new BaseFacility(getBaseFacility("STR_ACCESS_LIFT"), base, 2, 2));
	base.getFacilities().add(new BaseFacility(getBaseFacility("STR_HANGAR"), base, 2, 0));
	base.getFacilities().add(new BaseFacility(getBaseFacility("STR_HANGAR"), base, 0, 4));
	base.getFacilities().add(new BaseFacility(getBaseFacility("STR_HANGAR"), base, 4, 4));
	base.getFacilities().add(new BaseFacility(getBaseFacility("STR_LIVING_QUARTERS"), base, 3, 2));
	base.getFacilities().add(new BaseFacility(getBaseFacility("STR_GENERAL_STORES"), base, 2, 3));
	base.getFacilities().add(new BaseFacility(getBaseFacility("STR_LABORATORY"), base, 3, 3));
	base.getFacilities().add(new BaseFacility(getBaseFacility("STR_WORKSHOP"), base, 4, 3));
	base.getFacilities().add(new BaseFacility(getBaseFacility("STR_SMALL_RADAR_SYSTEM"), base, 1, 3));

	// Add items
	base.getItems().addItem("STR_STINGRAY_LAUNCHER", 1);
	base.getItems().addItem("STR_AVALANCHE_LAUNCHER", 1);
	base.getItems().addItem("STR_CANNON", 2);
	base.getItems().addItem("STR_STINGRAY_MISSILES", 25);
	base.getItems().addItem("STR_AVALANCHE_MISSILES", 10);
	base.getItems().addItem("STR_CANNON_ROUNDS_X50", 1);
	base.getItems().addItem("STR_PISTO", 2);
	base.getItems().addItem("STR_PISTOL_CLIP", 8);
	base.getItems().addItem("STR_RIFLE", 2);
	base.getItems().addItem("STR_RIFLE_CLIP", 8);
	base.getItems().addItem("STR_HEAVY_CANNON", 1);
	base.getItems().addItem("STR_HC_AP_AMMO", 6);
	base.getItems().addItem("STR_AUTO_CANNON", 1);
	base.getItems().addItem("STR_AC_AP_AMMO", 6);
	base.getItems().addItem("STR_ROCKET_LAUNCHER", 1);
	base.getItems().addItem("STR_SMALL_ROCKET", 4);
	base.getItems().addItem("STR_GRENADE", 5);
	base.getItems().addItem("STR_SMOKE_GRENADE", 5);

	// Add crafts
	Craft skyranger = new Craft(getCraft("STR_SKYRANGER"), base, save.getCraftIds());
	skyranger.setFuel(skyranger.getRules().getMaxFuel());
	skyranger.getItems().addItem("STR_PISTO", 3);
	skyranger.getItems().addItem("STR_PISTOL_CLIP", 5);
	skyranger.getItems().addItem("STR_RIFLE", 6);
	skyranger.getItems().addItem("STR_RIFLE_CLIP", 12);
	skyranger.getItems().addItem("STR_HEAVY_CANNON", 1);
	skyranger.getItems().addItem("STR_HC_AP_AMMO", 2);
	skyranger.getItems().addItem("STR_HC_HE_AMMO", 2);
	skyranger.getItems().addItem("STR_GRENADE", 8);
	base.getCrafts().add(skyranger);

	for (int i = 0; i < 2; i++)
	{
		Craft interceptor = new Craft(getCraft("STR_INTERCEPTOR"), base, save.getCraftIds());
		interceptor.setFuel(interceptor.getRules().getMaxFuel());
		interceptor.getWeapons().set(0, new CraftWeapon(getCraftWeapon("STR_STINGRAY"), getCraftWeapon("STR_STINGRAY").getAmmoMax()));
		interceptor.getWeapons().set(1, new CraftWeapon(getCraftWeapon("STR_CANNON_UC"), getCraftWeapon("STR_CANNON_UC").getAmmoMax()));
		base.getCrafts().add(interceptor);
	}

	// Generate soldiers
	for (int i = 0; i < 8; i++)
	{
		Soldier soldier = new Soldier(getSoldier("XCOM"), getArmor("STR_NONE_UC"), _names);
		soldier.setCraft(skyranger);
		base.getSoldiers().add(soldier);
	}

	save.getBases().add(base);

	// init savedgame articles
	save.getUfopaedia().insertArticle(_ufopaediaArticles.get("STR_SKYRANGER"));
	save.getUfopaedia().insertArticle(_ufopaediaArticles.get("STR_LIGHTNING"));
	save.getUfopaedia().insertArticle(_ufopaediaArticles.get("STR_AVENGER"));
	save.getUfopaedia().insertArticle(_ufopaediaArticles.get("STR_INTERCEPTOR"));
	save.getUfopaedia().insertArticle(_ufopaediaArticles.get("STR_FIRESTORM"));

	save.getUfopaedia().insertArticle(_ufopaediaArticles.get("STR_STINGRAY"));
	save.getUfopaedia().insertArticle(_ufopaediaArticles.get("STR_AVALANCHE"));
	save.getUfopaedia().insertArticle(_ufopaediaArticles.get("STR_CANNON_UC"));
	save.getUfopaedia().insertArticle(_ufopaediaArticles.get("STR_FUSION_BALL_UC"));
	save.getUfopaedia().insertArticle(_ufopaediaArticles.get("STR_LASER_CANNON_UC"));
	save.getUfopaedia().insertArticle(_ufopaediaArticles.get("STR_PLASMA_BEAM_UC"));

	save.getUfopaedia().insertArticle(_ufopaediaArticles.get("STR_SECTOID"));
//	save.getUfopaedia().insertArticle(_ufopaediaArticles["STR_SNAKEMAN"]);
	save.getUfopaedia().insertArticle(_ufopaediaArticles.get("STR_ALIEN_ORIGINS"));
	
	return save;
}

}
