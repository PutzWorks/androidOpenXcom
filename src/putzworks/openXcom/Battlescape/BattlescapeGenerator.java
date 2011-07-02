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

import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.RNG;
import putzworks.openXcom.Resource.ResourcePack;
import putzworks.openXcom.Ruleset.MapBlock;
import putzworks.openXcom.Ruleset.MapData;
import putzworks.openXcom.Ruleset.MapData.SpecialTileType;
import putzworks.openXcom.Ruleset.MapDataSet;
import putzworks.openXcom.Ruleset.RuleAlien;
import putzworks.openXcom.Ruleset.RuleArmor;
import putzworks.openXcom.Ruleset.RuleItem;
import putzworks.openXcom.Ruleset.RuleItem.BattleType;
import putzworks.openXcom.Ruleset.RuleTerrain;
import putzworks.openXcom.Savegame.Alien;
import putzworks.openXcom.Savegame.BattleItem;
import putzworks.openXcom.Savegame.BattleUnit;
import putzworks.openXcom.Savegame.Craft;
import putzworks.openXcom.Savegame.Node;
import putzworks.openXcom.Savegame.Node.NodeRank;
import putzworks.openXcom.Savegame.NodeLink;
import putzworks.openXcom.Savegame.SavedBattleGame;
import putzworks.openXcom.Savegame.SavedBattleGame.MissionType;
import putzworks.openXcom.Savegame.Soldier;
import putzworks.openXcom.Savegame.Tile;
import putzworks.openXcom.Savegame.Ufo;

public class BattlescapeGenerator
{
	private Game _game;
	private SavedBattleGame _save;
	private ResourcePack _res;
	private Craft _craft;
	private Ufo _ufo;
	private RuleTerrain _terrain;
	private int _width, _length, _height;
	private int _worldTexture, _worldShade;
	private MissionType _missionType;
	private int _unitCount;

	/// links tiles with terrainobjects, for easier/faster lookup
	//public void linkTilesWithMapDatas();  couldn't find this one is this class...



/**
 * Sets up a BattlescapeGenerator.
 * @param game pointer to Game object.
 */
public BattlescapeGenerator(Game game)
{
	_game = game;
	_save = _game.getSavedGame().getBattleGame();
	_res = _game.getResourcePack();
	_ufo = null;
	_craft = null;
}

/**
 * Sets the x-com craft involved in the battle.
 * @param craft Pointer to XCom craft.
 */
public void setCraft(Craft craft)
{
	_craft = craft;
	_craft.setInBattlescape(true);
}

/**
 * Sets the ufo involved in the battle.
 * @param ufo Pointer to UFO.
 */
public void setUfo(Ufo ufo)
{
	_ufo = ufo;
	_ufo.setInBattlescape(true);
}

/**
 * Sets the world texture where a ufo crashed. This is used to determine the terrain.
 * @param texture Texture id of the polygon on the globe.
 */
public void setWorldTexture(int texture)
{
	_worldTexture = texture;
}

/**
 * Sets the world shade where a ufo crashed. This is used to determine the battlescape light level.
 * @param shade Shade of the polygon on the globe.
 */
public void setWorldShade(int shade)
{
	_worldShade = shade;
}

/**
 * Sets the mission type. This is used to determine the various elements of the battle.
 * @param missionType MissionType.
 */
public void setMissionType(MissionType missionType)
{
	_missionType = missionType;
	_save.setMissionType(missionType);
}

/**
 * This will start the generator: it will fill up the battlescapesavegame with data.
 */
public void run()
{
	_width = 50;
	_length = 50;
	_height = 4;
	_unitCount = 0;

	// find out the terrain type
	if (_missionType == MissionType.MISS_TERROR)
	{
		_terrain = _game.getRuleset().getTerrain("URBAN");
	}
	else
	{
		switch (_worldTexture)
		{
		case 0:
		case 6:
		case 10:
		case 11:
			{
				if (_ufo != null)
				{
					if (_ufo.getLatitude() < 0)
					{ // northern hemisphere
						_terrain = _game.getRuleset().getTerrain("FOREST");
					}else
					{ // southern hemisphere
						_terrain = _game.getRuleset().getTerrain("JUNGLE");
					}
				}
				else
				{
					_terrain = _game.getRuleset().getTerrain("FOREST");
				}
				break;
			}
		case 1:
		case 2:
		case 3:
		case 4:
			{
				_terrain = _game.getRuleset().getTerrain("CULTA");
				break;
			}
		case 5:
			{
				_terrain = _game.getRuleset().getTerrain("MOUNT");
				break;
			}
		case 7:
		case 8:
			{
				_terrain = _game.getRuleset().getTerrain("DESERT");
				break;
			}
		case 9:
		case 12:
			{
				_terrain = _game.getRuleset().getTerrain("POLAR");
				break;
			}
		}
	}

	// creates the tile objects
	_save.initMap(_width, _length, _height);
	_save.initUtilities(_res);

	// lets generate the map now and store it inside the tile objects
	generateMap();

	if (_craft != null)
	{
		// add soldiers that are in the craft
		for (Soldier i: _craft.getBase().getSoldiers())
		{
			if ((i).getCraft() == _craft)
				addSoldier((i));
		}
		_save.setSelectedUnit(_save.getUnits().get(0)); // select first soldier

		// add items that are in the craft
		/*for (std.map<std.string, ItemContainer*>.iterator i = _craft.getItems().begin(); i != _craft.getItems().end(); ++i)
		{
				addItem((*i).second);
		}*/
		// test data
		addItem(_game.getRuleset().getItem("STR_RIFLE"));
		addItem(_game.getRuleset().getItem("STR_HEAVY_CANNON"));
		addItem(_game.getRuleset().getItem("STR_ROCKET_LAUNCHER"));
		addItem(_game.getRuleset().getItem("STR_RIFLE_CLIP"));
		addItem(_game.getRuleset().getItem("STR_HC_HE_AMMO"));
		addItem(_game.getRuleset().getItem("STR_SMALL_ROCKET"));
		addItem(_game.getRuleset().getItem("STR_GRENADE"));
		addItem(_game.getRuleset().getItem("STR_SMOKE_GRENADE"));
		addItem(_game.getRuleset().getItem("STR_PISTO"));
		addItem(_game.getRuleset().getItem("STR_PISTOL_CLIP"));
	}

	if (_missionType == MissionType.MISS_UFORECOVERY)
	{
		// add aliens (should depend on mission type & difficulty level)
		addAlien(_game.getRuleset().getAlien("SECTOID_SOLDIER"), _game.getRuleset().getArmor("SECTOID_ARMOR0"), NodeRank.ENGINEER);
		addAlien(_game.getRuleset().getAlien("SECTOID_SOLDIER"), _game.getRuleset().getArmor("SECTOID_ARMOR0"), NodeRank.NAVIGATOR);
		addAlien(_game.getRuleset().getAlien("SECTOID_SOLDIER"), _game.getRuleset().getArmor("SECTOID_ARMOR0"), NodeRank.SOLDIER);
	}
	else
	{
		addAlien(_game.getRuleset().getAlien("SECTOID_SOLDIER"), _game.getRuleset().getArmor("SECTOID_ARMOR0"), NodeRank.SCOUT);
		addAlien(_game.getRuleset().getAlien("SECTOID_SOLDIER"), _game.getRuleset().getArmor("SECTOID_ARMOR0"), NodeRank.SCOUT);
		addAlien(_game.getRuleset().getAlien("SECTOID_SOLDIER"), _game.getRuleset().getArmor("SECTOID_ARMOR0"), NodeRank.SCOUT);
	}
	addAlien(_game.getRuleset().getAlien("SECTOID_SOLDIER"), _game.getRuleset().getArmor("SECTOID_ARMOR0"), NodeRank.SCOUT);
	addAlien(_game.getRuleset().getAlien("SECTOID_SOLDIER"), _game.getRuleset().getArmor("SECTOID_ARMOR0"), NodeRank.SCOUT);
	addAlien(_game.getRuleset().getAlien("SECTOID_SOLDIER"), _game.getRuleset().getArmor("SECTOID_ARMOR0"), NodeRank.SCOUT);

	// set shade (alien bases are a little darker, sites depend on worldshade)
	int[] worldshades = { 0, 1, 2, 3, 5, 7, 9 , 15 };
	_save.setGlobalShade(worldshades[_worldShade]);

	_save.getTerrainModifier().calculateSunShading();
	_save.getTerrainModifier().calculateTerrainLighting();
	_save.getTerrainModifier().calculateUnitLighting();
}

/**
 * Adds a soldier to the game and place him on a free spawnpoint.
 * @param soldier pointer to the Soldier
 */
private void addSoldier(Soldier soldier)
{
	BattleUnit unit = new BattleUnit(soldier, BattleUnit.UnitFaction.FACTION_PLAYER);
	unit.setId(_unitCount++);

	Position pos;
	int x = 0, y = 0, z = 0;

	for (int i = _height * _length * _width - 1; i >= 0; i--)
	{
		// to spawn an xcom soldier, there has to be a tile, with a floor, with the starting point attribute and no object in the way
		if (_save.getTiles()[i] != null && _save.getTiles()[i].getMapData(MapData.O_FLOOR) != null && _save.getTiles()[i].getMapData(MapData.O_FLOOR).getSpecialType() == SpecialTileType.START_POINT && _save.getTiles()[i].getMapData(MapData.O_OBJECT) == null)
		{
			_save.getTileCoords(i, x, y, z);
			pos = new Position(x, y, z);
			if (_save.selectUnit(pos) == null)
			{
				unit.setPosition(pos);
				_save.getTiles()[i].setUnit(unit);
				break;
			}
		}
	}

	_save.getUnits().add(unit);
}

/**
 * Adds an alien to the game and place him on a free spawnpoint.
 * @param rules pointer to the RuleAlien which holds info about alien .
 * @param armor The armor of the alien.
 * @param rank The rank of the alien, used for spawn point search.
 */
private void addAlien(RuleAlien rules, RuleArmor armor, NodeRank rank)
{
	BattleUnit unit = new BattleUnit(new Alien(rules, armor, _game.getLanguage()), BattleUnit.UnitFaction.FACTION_HOSTILE);
	Node node;
	boolean bFound = false;
	unit.setId(_unitCount++);

	// find a place to spawn, going from highest priority to lowest
	// some randomness is added
	for (int priority=10; priority > 0 && !bFound; priority--)
	{
		for (Node i: _save.getNodes()) //!bFound
		{
			node = i;
			if (node.getRank() == rank
				&& node.getPriority() == priority
				&& _save.selectUnit(node.getPosition()) == null
				&& (RNG.generate(0,2) == 1))
			{
				unit.setPosition(node.getPosition());
				_save.getTile(node.getPosition()).setUnit(unit);
				bFound = true;
			}
		}
	}

	// second try in case we still haven't found a place to spawn
	// this time without randomness
	for (int priority = 10; priority > 0 && !bFound; priority--)
	{
		for (Node i: _save.getNodes()) //!bFound
		{
			node = i;
			if (node.getRank() == rank
				&& node.getPriority() == priority
				&& _save.selectUnit(node.getPosition()) == null
				)
			{
				unit.setPosition(node.getPosition());
				_save.getTile(node.getPosition()).setUnit(unit);
				bFound = true;
				break;
			}
		}
	}

	unit.setDirection(RNG.generate(0,7));

	_save.getUnits().add(unit);
}

/**
 * Adds an item to the game and assign it to a soldier?.
 * @param item pointer to the Item
 */
private void addItem(RuleItem item)
{
	BattleItem bi = new BattleItem(item);

	if (item.getBattleType() == BattleType.BT_AMMO)
	{
		// find equipped weapons that can be loaded with this ammo
		for (BattleUnit i: _save.getUnits())
		{
			BattleItem weapon = _save.getItemFromUnit((i), BattleItem.InventorySlot.RIGHT_HAND);
			if (weapon != null && weapon.getAmmoItem() == null)
			{
				if (weapon.setAmmoItem(bi) == 0)
					break;
			}
		}
	}
	else
	{
		// find the first soldier with a free right hand
		for (BattleUnit i: _save.getUnits())
		{
			if (_save.getItemFromUnit((i), BattleItem.InventorySlot.RIGHT_HAND) == null)
			{
				bi.setOwner((i));
				bi.setSlot(BattleItem.InventorySlot.RIGHT_HAND);
				break;
			}
		}
	}

	_save.getItems().add(bi);
}

/**
 * Generates a map (set of tiles) for a new battlescape game.
 */
private void generateMap()
{
	int x = 0, y = 0;
	int blocksToDo = 0;
	MapBlock[][] blocks = new MapBlock[10][10];
	boolean[][] landingzone = new boolean[10][10];
	int craftX = 0, craftY = 0;
	int ufoX = 0, ufoY = 0;
	boolean placed = false;

	MapBlock dummy = new MapBlock(_terrain, "dummy", 0, 0, false);
	MapBlock craftMap = null;
	MapBlock ufoMap = null;

	for (int i = 0; i < 10; ++i)
	{
		for (int j = 0; j < 10; j++)
		{
			blocks[i][j] = null;
			landingzone[i][j] = false;
		}
	}

	blocksToDo = (_width / 10) * (_length / 10);

	/* create tile objects */
	for (int i = 0; i < _height * _length * _width; ++i)
	{
		Position pos = new Position();
		_save.getTileCoords(i, pos.x, pos.y, pos.z);
		_save.getTiles()[i] = new Tile(pos);
	}

	/* Determine UFO landingzone (do this first because ufo is generally bigger) */
	if (_ufo != null)
	{
		// crafts always consist of 1 mapblock, but can have all sorts of sizes
		ufoMap = _ufo.getRules().getBattlescapeTerrainData().getMapBlocks().get(0);

		ufoX = RNG.generate(0, (_length / 10) - ufoMap.getWidth() / 10);
		ufoY = RNG.generate(0, (_width / 10) - ufoMap.getLength() / 10);

		for (int i = 0; i < ufoMap.getWidth() / 10; ++i)
		{
			for (int j = 0; j < ufoMap.getLength() / 10; j++)
			{
				landingzone[ufoX + i][ufoY + j] = true;
			}
		}
	}

	/* Determine Craft landingzone */
	if (_craft != null)
	{
		// crafts always consist of 1 mapblock, but can have all sorts of sizes
		craftMap = _craft.getRules().getBattlescapeTerrainData().getMapBlocks().get(0);
		while (!placed)
		{
			craftX = RNG.generate(0, (_length/10)- craftMap.getWidth() / 10);
			craftY = RNG.generate(0, (_width/10)- craftMap.getLength() / 10);
			placed = true;
			// check if this place is ok
			for (int i = 0; i < craftMap.getWidth() / 10; ++i)
			{
				for (int j = 0; j < craftMap.getLength() / 10; j++)
				{
					if (landingzone[craftX + i][craftY + j])
					{
						placed = false; // whoops the ufo is already here, try again
					}
				}
			}
			// if ok, allocate it
			if (placed)
			{
				for (int i = 0; i < craftMap.getWidth() / 10; ++i)
					for (int j = 0; j < craftMap.getLength() / 10; j++)
						landingzone[craftX + i][craftY + j] = true;
			}
		}
	}

	/* Random map generation for crash/landing sites */
	while (blocksToDo != 0)
	{
		if (blocks[x][y] == null)
		{
			// last block of this row or column or next block is not free or big block would block landingzone
			if (x == ((_width / 10) - 1) || y == ((_length / 10) - 1) || blocks[x + 1][y] == dummy
				|| landingzone[x + 1][y] || landingzone[x + 1][y + 1] || landingzone[x][y + 1] || blocksToDo == 1)
			{
				// only small block will fit
				blocks[x][y] = _terrain.getRandomMapBlock(10, landingzone[x][y]);
				blocksToDo--;
				x++;
			}
			else
			{
				blocks[x][y] = _terrain.getRandomMapBlock(20, landingzone[x][y]);
				blocksToDo--;
				if (blocks[x][y].getWidth() == 20) // big block
				{
					// mark mapblocks as used
					blocks[x + 1][y] = dummy;
					blocksToDo--;
					blocks[x + 1][y + 1] = dummy;
					blocksToDo--;
					blocks[x][y + 1] = dummy;
					blocksToDo--;
					x++;
				}
				x++;
			}
		}
		else
		{
			x++;
		}

		if (x >= (_width / 10)) // reach the end
		{
			x = 0;
			y++;
		}
	}

	for (MapDataSet i: _terrain.getMapDataSets())
	{
		(i).load(_res);
		_save.getMapDataSets().add(i);
	}

	/* now load them up */
	for (int itY = 0; itY < 10; itY++)
	{
		for (int itX = 0; itX < 10; itX++)
		{
			if (blocks[itX][itY] != null && blocks[itX][itY] != dummy)
			{
				loadMAP(blocks[itX][itY], itX * 10, itY * 10, _terrain);
				if (!landingzone[itX][itY])
				{
					loadRMP(blocks[itX][itY], itX * 10, itY * 10);
				}
			}
		}
	}

	if (_ufo != null)
	{
		for (MapDataSet i: _ufo.getRules().getBattlescapeTerrainData().getMapDataSets())
		{
			(i).load(_res);
			_save.getMapDataSets().add(i);
		}
		loadMAP(ufoMap, ufoX * 10, ufoY * 10, _ufo.getRules().getBattlescapeTerrainData());
		loadRMP(ufoMap, ufoX * 10, ufoY * 10);
	}

	if (_craft != null)
	{
		for (MapDataSet i: _craft.getRules().getBattlescapeTerrainData().getMapDataSets())
		{
			(i).load(_res);
			_save.getMapDataSets().add(i);
		}
		loadMAP(craftMap, craftX * 10, craftY * 10, _craft.getRules().getBattlescapeTerrainData(), true);
		loadRMP(craftMap, craftX * 10, craftY * 10);
	}

	/* TODO: map generation for terror sites */

	/* TODO: map generation for base defense mission */

	/* TODO: map generation for alien base assault */

	dummy = null;
}


/**
 * Loads a X-Com format MAP file into the tiles of the battlegame.
 * @param mapblock Pointer to MapBlock.
 * @param xoff Mapblock offset in X direction.
 * @param yoff Mapblock offset in Y direction.
 * @param save Pointer to the current SavedBattleGame.
 * @param terrain Pointer to the Terrain rule.
 * @param discovered Whether or not this mapblock is discovered (eg. landingsite of the x-com plane)
 * @return int Height of the loaded mapblock (this is needed for spawpoint calculation...)
 * @sa http://www.ufopaedia.org/index.php?title=MAPS
 * @note Y-axis is in reverse order
 */
private int loadMAP(MapBlock mapblock, int xoff, int yoff, RuleTerrain terrain, boolean discovered)
{
	int width, length, height;
	int x = xoff, y = yoff, z = 0;
	char[] size = new char[3];
	char[] value= new char[4];
	std.stringstream filename;
	filename << _res.getFolder() << "MAPS/" << mapblock.getName() << ".MAP";
	String mapDataFileName;
	int terrainObjectID;

	// Load file
	std.ifstream mapFile (ResourcePack.insensitive(filename.str()).c_str(), std.ios.in| std.ios.binary);
	if (!mapFile)
	{
		throw Exception("Failed to load MAP");
	}

	mapFile.read((char)size, sizeof(size));
	length = (int)size[0];
	width = (int)size[1];
	height = (int)size[2];
	z += height - 1;
	y += length - 1;
	mapblock.setHeight(height);

	while (mapFile.read((char)value, sizeof(value)))
	{
		for (int part = 0; part < 4; part++)
		{
			terrainObjectID = (int)((char)value[part]);
			if (terrainObjectID>0)
			{
				_save.getTile(new Position(x, y, z)).setMapData(terrain.getMapData(terrainObjectID),part);
			}
			// if the part is empty and it's not a floor, remove it
			// it prevents growing grass in UFOs
			if (terrainObjectID == 0 && part > 0)
			{
				_save.getTile(new Position(x, y, z)).setMapData(0,part);
			}
		}
		_save.getTile(new Position(x, y, z)).setDiscovered(discovered);

		x++;

		if (x == (width + xoff))
		{
			x = xoff;
			y--;
		}
		if (y == yoff - 1)
		{
			y = length - 1 + yoff;
			z--;
		}
	}

	if (!mapFile.eof())
	{
		throw Exception("Invalid data from file");
	}

	mapFile.close();

	return height;
}

/**
 * Loads a X-Com format RMP file into the spawnpoints of the battlegame.
 * @param mapblock pointer to MapBlock.
 * @param xoff mapblock offset in X direction
 * @param yoff mapblock offset in Y direction
 * @sa http://www.ufopaedia.org/index.php?title=ROUTES
 */
private void loadRMP(MapBlock mapblock, int xoff, int yoff)
{
	int id = 0;
	char[] value = new char[24];
	std.stringstream filename;
	filename << _res.getFolder() << "ROUTES/" << mapblock.getName() << ".RMP";

	// Load file
	std.ifstream mapFile (ResourcePack.insensitive(filename.str()).c_str(), std.ios.in| std.ios.binary);
	if (!mapFile)
	{
		throw Exception("Failed to load RMP");
	}

	int nodeOffset = _save.getNodes().size();

	while (mapFile.read((char)value, sizeof(value)))
	{
		Node node = new Node(nodeOffset + id, Position(xoff + (int)value[1], yoff + (mapblock.getLength() - 1 - (int)value[0]), mapblock.getHeight() - 1 - (int)value[2]), (int)value[3], (int)value[19], (int)value[20], (int)value[21], (int)value[22], (int)value[23]);
		for (int j=0;j<5;j++)
		{
			int connectID = (int)((signed char)value[4 + j*3]);
			if (connectID > -1)
			{
				connectID += nodeOffset;
			}
			node.assignNodeLink(new NodeLink(connectID, (int)value[5 + j*3], (int)value[6 + j*3]), j);
		}
		_save.getNodes().add(node);
		id++;
	}

	if (!mapFile.eof())
	{
		throw Exception("Invalid data from file");
	}

	mapFile.close();
}

}
