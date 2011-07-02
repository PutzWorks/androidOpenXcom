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

import java.util.List;
import java.util.Vector;

import android.graphics.Rect;

import putzworks.openXcom.Battlescape.Map.CursorType;
import putzworks.openXcom.Engine.*;
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Ruleset.RuleItem.BattleType;
import putzworks.openXcom.Savegame.BattleItem;
import putzworks.openXcom.Savegame.BattleItem.InventorySlot;
import putzworks.openXcom.Savegame.BattleUnit;
import putzworks.openXcom.Savegame.SavedBattleGame;
import putzworks.openXcom.Savegame.Soldier;


public class BattlescapeState extends State
{
	enum BattleActionType { BA_NONE, BA_THROW, BA_AUTOSHOT, BA_SNAPSHOT, BA_AIMEDSHOT, BA_STUN, BA_HIT };

	public static final int DEFAULT_WALK_SPEED = 40;
	public static final int DEFAULT_BULLET_SPEED = 20;
	public static final int DEFAULT_ANIM_SPEED = 100;

	public class BattleAction
	{
		BattleActionType type;
		BattleUnit actor;
		BattleItem weapon;
		Position target;
		int TU;
		boolean targeting;
	}

	private Surface _icons, _rank;
	private Map _map;
	private InteractiveSurface _btnUnitUp, _btnUnitDown, _btnMapUp, _btnMapDown, _btnShowMap, _btnKneel;
	private InteractiveSurface _btnSoldier, _btnCenter, _btnNextSoldier, _btnNextStop, _btnShowLayers, _btnHelp;
	private InteractiveSurface _btnEndTurn, _btnAbort;
	private ImageButton _reserve;
	private ImageButton _btnReserveNone, _btnReserveSnap, _btnReserveAimed, _btnReserveAuto;
	private InteractiveSurface _btnLeftHandItem, _btnRightHandItem;
	private InteractiveSurface[] _btnVisibleUnit =  new InteractiveSurface[10];
	private NumberText[] _numVisibleUnit = new NumberText[10];
	private BattleUnit[] _visibleUnit = new BattleUnit[10];
	private Surface _warningMessageBackground;
	private Text _txtWarningMessage;

	private Text _txtName;
	private NumberText _numTimeUnits, _numEnergy, _numHealth, _numMorale, _numLayers, _numAmmoLeft, _numAmmoRight;
	private Bar _barTimeUnits, _barEnergy, _barHealth, _barMorale;
	private Timer _stateTimer, _animTimer;
	private SavedBattleGame _battleGame;
	private Text _txtDebug;
	private int _animFrame;
	private List<BattleState> _states;
	private BattleAction _action;
	private Vector<State> _popups;

/**
 * Initializes all the elements in the Battlescape screen.
 * @param game Pointer to the core game.
 */
public BattlescapeState(Game game)
{
	super(game);
	_popups = new Vector<State>();
	// Create the battlemap view
	_map = new Map(320, 200, 0, 0);

	// Create buttonbar
	_icons = new Surface(320, 200, 0, 0);
	_numLayers = new NumberText(3, 5, 232, 150);
	_rank = new Surface(26,23,107,177);

	// Create buttons
	_btnAbort = new InteractiveSurface(32, 16, 240, 160);
	_btnEndTurn = new InteractiveSurface(32, 16, 240, 144);
	_btnMapUp = new InteractiveSurface(32, 16, 80, 144);
	_btnMapDown = new InteractiveSurface(32, 16, 80, 160);
	_btnNextSoldier = new InteractiveSurface(32, 16, 176, 144);
	_btnCenter = new InteractiveSurface(32, 16, 145, 160);
	_btnReserveNone = new ImageButton(28, 11, 49, 177);
	_btnReserveSnap = new ImageButton(28, 11, 78, 177);
	_btnReserveAimed = new ImageButton(28, 11, 49, 189);
	_btnReserveAuto = new ImageButton(28, 11, 78, 189);
	_btnLeftHandItem = new InteractiveSurface(32, 48, 8, 149);
	_numAmmoLeft = new NumberText(30, 5, 8, 149);
	_btnRightHandItem = new InteractiveSurface(32, 48, 280, 149);
	_numAmmoRight = new NumberText(30, 5, 280, 149);
	_btnKneel = new InteractiveSurface(32, 16, 113, 160);
	for (int i = 0; i < 10; ++i)
	{
		_btnVisibleUnit[i] = new InteractiveSurface(15, 12, 300, 128 - (i * 13));
		_numVisibleUnit[i] = new NumberText(15, 12, 306, 132 - (i * 13));
	}
	_numVisibleUnit[9].setX(304); // center number 10
	_warningMessageBackground = new Surface(224, 24, 48, 176);
	_txtWarningMessage = new Text(224, 24, 48, 184);

	// Create soldier stats summary
	_txtName = new Text(120, 10, 135, 176);

	_numTimeUnits = new NumberText(15, 5, 136, 186);
	_barTimeUnits = new Bar(102, 3, 170, 185);

	_numEnergy = new NumberText(15, 5, 154, 186);
	_barEnergy = new Bar(102, 3, 170, 189);

	_numHealth = new NumberText(15, 5, 136, 194);
	_barHealth= new Bar(102, 3, 170, 193);

	_numMorale = new NumberText(15, 5, 154, 194);
	_barMorale = new Bar(102, 3, 170, 197);

	_txtDebug = new Text(300, 10, 20, 0);

	_reserve = _btnReserveNone;

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("PALETTES.DAT_4").getColors());

	// Last 16 colors are a greyish gradient
	SDL_Color color[] = {{140, 152, 148},
						 {132, 136, 140},
						 {116, 124, 132},
						 {108, 116, 124},
						 {92, 104, 108},
						 {84, 92, 100},
						 {76, 80, 92},
						 {56, 68, 84},
						 {48, 56, 68},
						 {40, 48, 56},
						 {32, 36, 48},
						 {24, 28, 32},
						 {16, 20, 24},
						 {8, 12, 16},
						 {0, 4, 8},
						 {0, 0, 0}};
	_game.setPalette(color, Palette.backPos+16, 16);

	// Fix cursor
	_game.getCursor().setColor(Palette.blockOffset(9));

	// Fix the FpsCounter color.
	_game.getFpsCounter().setColor(Palette.blockOffset(9));

	add(_map);
	add(_icons);
	add(_numLayers);
	add(_rank);
	add(_btnAbort);
	add(_btnEndTurn);
	add(_btnMapUp);
	add(_btnMapDown);
	add(_btnNextSoldier);
	add(_btnCenter);
	add(_btnKneel);
	add(_txtName);
	add(_numTimeUnits);
	add(_numEnergy);
	add(_numHealth);
	add(_numMorale);
	add(_barTimeUnits);
	add(_barEnergy);
	add(_barHealth);
	add(_barMorale);
	add(_btnReserveNone);
	add(_btnReserveSnap);
	add(_btnReserveAimed);
	add(_btnReserveAuto);
	add(_btnLeftHandItem);
	add(_numAmmoLeft);
	add(_btnRightHandItem);
	add(_numAmmoRight);
	for (int i = 0; i < 10; ++i)
	{
		add(_btnVisibleUnit[i]);
		add(_numVisibleUnit[i]);
	}
	add(_warningMessageBackground);
	add(_txtWarningMessage);

	add(_txtDebug);
	// Set up objects
	_game.getResourcePack().getSurface("ICONS.PCK").blit(_icons);

	_battleGame = _game.getSavedGame().getBattleGame();
	_map.setResourcePack(_game.getResourcePack());
	_map.setSavedGame(_battleGame, _game);
	_map.init();
	_map.onMouseClick((ActionHandler)BattlescapeState.mapClick);

	_numLayers.setColor(Palette.blockOffset(1)-2);
	_numLayers.setValue(1);

	_numAmmoLeft.setColor(2);
	_numAmmoLeft.setValue(999);

	_numAmmoRight.setColor(2);
	_numAmmoRight.setValue(999);

	_btnAbort.onMouseClick((ActionHandler)BattlescapeState.btnAbortClick);
	_btnEndTurn.onMouseClick((ActionHandler)BattlescapeState.btnEndTurnClick);
	_btnMapUp.onMouseClick((ActionHandler)BattlescapeState.btnMapUpClick);
	_btnMapDown.onMouseClick((ActionHandler)BattlescapeState.btnMapDownClick);
	_btnNextSoldier.onMouseClick((ActionHandler)BattlescapeState.btnNextSoldierClick);
	_btnCenter.onMouseClick((ActionHandler)BattlescapeState.btnCenterClick);
	_btnKneel.onMouseClick((ActionHandler)BattlescapeState.btnKneelClick);
	_btnLeftHandItem.onMouseClick((ActionHandler)BattlescapeState.btnLeftHandItemClick);
	_btnRightHandItem.onMouseClick((ActionHandler)BattlescapeState.btnRightHandItemClick);
	for (int i = 0; i < 10; ++i)
	{
		_btnVisibleUnit[i].onMouseClick((ActionHandler)BattlescapeState.btnVisibleUnitClick);
		_numVisibleUnit[i].setColor(16);
		_numVisibleUnit[i].setValue(i+1);
	}
	_txtWarningMessage.setColor(Palette.blockOffset(2)-1);
	_txtWarningMessage.setHighContrast(true);
	_txtWarningMessage.setAlign(TextHAlign.ALIGN_CENTER);
	_warningMessageBackground.setVisible(false);

	_txtName.setColor(Palette.blockOffset(8)-1);
	_txtName.setHighContrast(true);
	_numTimeUnits.setColor(Palette.blockOffset(4));
	_numEnergy.setColor(Palette.blockOffset(1));
	_numHealth.setColor(Palette.blockOffset(2));
	_numMorale.setColor(Palette.blockOffset(12));
	_barTimeUnits.setColor(Palette.blockOffset(4));
	_barTimeUnits.setScale(1.0);
	_barEnergy.setColor(Palette.blockOffset(1));
	_barEnergy.setScale(1.0);
	_barHealth.setColor(Palette.blockOffset(2));
	_barHealth.setScale(1.0);
	_barMorale.setColor(Palette.blockOffset(12));
	_barMorale.setScale(1.0);

	_txtDebug.setColor(Palette.blockOffset(8)-1);
	_txtDebug.setHighContrast(true);

	updateSoldierInfo(_battleGame.getSelectedUnit());
	_map.centerOnPosition(_battleGame.getSelectedUnit().getPosition());

	_btnReserveNone.copy(_icons);
	_btnReserveNone.setColor(Palette.blockOffset(4)+6);
	_btnReserveNone.setGroup(_reserve);

	_btnReserveSnap.copy(_icons);
	_btnReserveSnap.setColor(Palette.blockOffset(2)+6);
	_btnReserveSnap.setGroup(_reserve);

	_btnReserveAimed.copy(_icons);
	_btnReserveAimed.setColor(Palette.blockOffset(2)+6);
	_btnReserveAimed.setGroup(_reserve);

	_btnReserveAuto.copy(_icons);
	_btnReserveAuto.setColor(Palette.blockOffset(2)+6);
	_btnReserveAuto.setGroup(_reserve);

	// Set music
	_game.getResourcePack().getMusic("GMTACTIC").play();

	_stateTimer = new Timer(DEFAULT_ANIM_SPEED);
	_stateTimer.onTimer((StateHandler)BattlescapeState.handleState);
	_stateTimer.start();

	_animTimer = new Timer(DEFAULT_ANIM_SPEED);
	_animTimer.onTimer((StateHandler)BattlescapeState.animate);
	_animTimer.start();

	_action.type = BattleActionType.BA_NONE;
	_action.TU = 0;
	_action.targeting = false;
}

/**
 *
 */
public void clearBattlescapeState()
{
	_stateTimer = null;
	_animTimer = null;
}

public void init()
{
	_map.focus();
	_map.cacheUnits();
	_map.draw(true);
}

/**
  * think
  */
public void think()
{
	boolean popped = false;

	if (_popups.isEmpty())
	{
		_stateTimer.think(this, null);
		_animTimer.think(this, null);
		_map.think();
		if (popped)
		{
			setupCursor();
			popped = false;
		}
	}
	else
	{
		// Handle popups
		_game.pushState(_popups.begin());
		_popups.erase(_popups.begin());
		popped = true;
	}
}

/**
 * Processes any clicks on the map to
 * command units.
 * @param action Pointer to an action.
 */
public void mapClick(Action action)
{
	// right-click aborts walking state
	if (action.getDetails().button.button == SDL_BUTTON_RIGHT)
	{
		if (_states.isEmpty())
		{
			if (_action.targeting)
			{
				_action.targeting = false;
				_action.type = BattleActionType.BA_NONE;
				setupCursor();
				_game.getCursor().setVisible(true);
				return;
			}
		}
		else
		{
			_states.front().cancel();
			return;
		}
	}

	if (action.getDetails().button.button == SDL_BUTTON_LEFT &&
		action.getYMouse() / action.getYScale() > 180 &&
		action.getXMouse() / action.getXScale() > 110 &&
		action.getXMouse() / action.getXScale() < 270)
	{
		if (_battleGame.getSelectedUnit() != null)
		{
			popup(new UnitInfoState(_game, _battleGame.getSelectedUnit()));
		}
	}

	// don't handle mouseclicks below 140, because they are in the buttons area (it overlaps with map surface)
	if (action.getYMouse() / action.getYScale() > BUTTONS_AREA) return;

	// don't accept leftclicks if there is no cursor or there is an action busy
	if (_map.getCursorType() == CursorType.CT_NONE || !_states.isEmpty()) return;

	Position pos;
	_map.getSelectorPosition(pos);

	if (action.getDetails().button.button == SDL_BUTTON_LEFT)
	{
		if (_action.targeting && _battleGame.getSelectedUnit() != null)
		{
			//  -= fire weapon or throw =-
			if (_battleGame.getSelectedUnit().getTimeUnits() < _action.TU)
			{
				showWarningMessage("STR_NOT_ENOUGH_TIME_UNITS");
				return;
			}
			_action.target = pos;
			_map.setCursorType(CursorType.CT_NONE);
			_game.getCursor().setVisible(false);
			_states.add(new ProjectileFlyBState(this));
			if (_action.type == BattleActionType.BA_AUTOSHOT)
			{
				_states.add(new ProjectileFlyBState(this));
				_states.add(new ProjectileFlyBState(this));
			}
			statePushFront(new UnitTurnBState(this));
		}
		else
		{
			BattleUnit unit = _battleGame.selectUnit(pos);
			if (unit != null && !unit.isOut())
			{
			//  -= select unit =-
				if (unit.getFaction() == _battleGame.getSide())
				{
					_battleGame.setSelectedUnit(unit);
					updateSoldierInfo(unit);
				}
			}
			else if (_battleGame.getSelectedUnit() != null)
			{
			//  -= start walking =-
				_action.target = pos;
				_map.setCursorType(CursorType.CT_NONE);
				_game.getCursor().setVisible(false);
				statePushBack(new UnitWalkBState(this));
			}
		}
	}
	else if (action.getDetails().button.button == SDL_BUTTON_RIGHT && _battleGame.getSelectedUnit())
	{
		//  -= turn to or open door =-
		_action.target = pos;
		statePushBack(new UnitTurnBState(this));
	}

}

/**
 * Move unit up.
 * @param action Pointer to an action.
 */
public void btnUnitUpClick(Action action)
{
	/*Pathfinding *pf = _battleGame.getPathfinding();
	Position start = _battleGame.getSelectedUnit().getPosition();
	Position end = start + Position(0, 0, +1);
	pf.calculate(_battleGame.getSelectedUnit(), end);*/
}

/**
 * Move unit down.
 * @param action Pointer to an action.
 */
public void btnUnitDownClick(Action action)
{
	/*Pathfinding *pf = _battleGame.getPathfinding();
	Position start = _battleGame.getSelectedUnit().getPosition();
	Position end = start + Position(0, 0, -1);
	pf.calculate((BattleUnit*)_battleGame.getSelectedUnit(), end);*/
}

/**
 * Show next map layer.
 * @param action Pointer to an action.
 */
public void btnMapUpClick(Action action)
{
	_map.up();
}

/**
 * Show previous map layer.
 * @param action Pointer to an action.
 */
public void btnMapDownClick(Action action)
{
	_map.down();
}

/**
 * Show minimap.
 * @param action Pointer to an action.
 */
public void btnShowMapClick(Action action)
{

}

/**
 * Kneel/Standup.
 * @param action Pointer to an action.
 */
public void btnKneelClick(Action action)
{
	// TODO: check for timeunits... check for FOV...
	BattleUnit bu = _battleGame.getSelectedUnit();
	if (bu != null)
	{
		if (bu.spendTimeUnits(bu.isKneeled()?8:4, _battleGame.getDebugMode()))
		{
			bu.kneel(!bu.isKneeled());
			_map.cacheUnits();
			updateSoldierInfo(bu);
		}
	}
}

/**
 * Go to soldier info screen.
 * @param action Pointer to an action.
 */
public void btnSoldierClick(Action action)
{

}

/**
 * Center on currently selected soldier.
 * @param action Pointer to an action.
 */
public void btnCenterClick(Action action)
{
	if (_battleGame.getSelectedUnit() != null)
	{
		_map.centerOnPosition(_battleGame.getSelectedUnit().getPosition());
	}
}

/**
 * Select next soldier.
 * @param action Pointer to an action.
 */
public void btnNextSoldierClick(Action action)
{
	BattleUnit unit = _battleGame.selectNextPlayerUnit();
	updateSoldierInfo(unit);
	if (unit != null) _map.centerOnPosition(unit.getPosition());
}

/**
 * Don't select current soldier and select next soldier.
 * @param action Pointer to an action.
 */
public void btnNextStopClick(Action action)
{

}

/**
 * Show/hide all map layers.
 * @param action Pointer to an action.
 */
public void btnShowLayersClick(Action action)
{

}

/**
 * Show options.
 * @param action Pointer to an action.
 */
public void btnHelpClick(Action action)
{

}

/**
 * End turn request. This will add a 0 to the end of the state queue,
 * so all ongoing actions, like explosions are finished first before really switching turn.
 * @param action Pointer to an action.
 */
public void btnEndTurnClick(Action action)
{
	statePushBack(null);
}

/**
 * End turn.
 */
private void endTurn()
{
	Position p;
	// check for hot grenades
	for (int i = 0; i < _battleGame.getWidth() * _battleGame.getLength() * _battleGame.getHeight(); ++i)
	{
		for (BattleItem it: _battleGame.getTiles()[i].getInventory())
		{
			if ((it).getRules().getBattleType() == BattleType.BT_GRENADE)  // it's a grenade // todo: it's primed
			{
				p.x = _battleGame.getTiles()[i].getPosition().x*16 + 8;
				p.y = _battleGame.getTiles()[i].getPosition().y*16 + 8;
				p.z = _battleGame.getTiles()[i].getPosition().z*24 + _battleGame.getTiles()[i].getTerrainLevel();
				statePushNext(new ExplosionBState(this, p, (it)));
				it = _battleGame.getTiles()[i].getInventory().erase(it);
			}
			else
			{
				++it;
			}
		}
	}

	if (_battleGame.getTerrainModifier().closeUfoDoors() != null)
	{
		_game.getResourcePack().getSoundSet("BATTLE.CAT").getSound(21).play(); // ufo door closed
	}

	_battleGame.endTurn();
	updateSoldierInfo(_battleGame.getSelectedUnit());
	if (_battleGame.getSelectedUnit() != null)
	{
		_map.centerOnPosition(_battleGame.getSelectedUnit().getPosition());
	}

	if (_battleGame.getSide() == BattleUnit.UnitFaction.FACTION_HOSTILE)
	{
		// do AI stuff

		if (!_battleGame.getDebugMode())
		{
			endTurn();
		}
	}
}

/**
 * Abort game.
 * @param action Pointer to an action.
 */
public void btnAbortClick(Action action)
{
	_game.getSavedGame().endBattle();
	_game.getCursor().setColor(Palette.blockOffset(15)+12);
	_game.getFpsCounter().setColor(Palette.blockOffset(15)+12);
	_game.popState();
}

/**
 * Shows action popup menu. When clicked, create the action.
 * @param action Pointer to an action.
 */
public void btnLeftHandItemClick(Action action)
{
	if (_action.type != BattleActionType.BA_NONE) return;
	if (_battleGame.getSelectedUnit() != null)
	{
		BattleItem leftHandItem = _battleGame.getItemFromUnit(_battleGame.getSelectedUnit(), BattleItem.InventorySlot.LEFT_HAND);
		handleItemClick(leftHandItem);
	}
}

/**
 * Shows action popup menu. When clicked, create the action.
 * @param action Pointer to an action.
 */
public void btnRightHandItemClick(Action action)
{
	if (_action.type != BattleActionType.BA_NONE) return;
	if (_battleGame.getSelectedUnit() != null)
	{
		BattleItem rightHandItem = _battleGame.getItemFromUnit(_battleGame.getSelectedUnit(), BattleItem.InventorySlot.RIGHT_HAND);
		handleItemClick(rightHandItem);
	}
}

/**
 * Center on the unit corresponding with this button.
 * @param action Pointer to an action.
 */
public void btnVisibleUnitClick(Action action)
{
	int btnID = -1;

	_states.clear();
	setupCursor();
	_game.getCursor().setVisible(true);

	// got to find out which button was pressed
	for (int i = 0; i < 10 && btnID == -1; ++i)
	{
		if (action.getSender() == _btnVisibleUnit[i])
		{
			btnID = i;
		}
	}

	if (btnID != -1)
	{
		_map.centerOnPosition(_visibleUnit[btnID].getPosition());
	}
}

/**
 * Set the cursor according to the selected action.
 */
private void setupCursor()
{
	if (_action.targeting)
	{
		if (_action.type == BattleActionType.BA_THROW)
		{
			_map.setCursorType(Map.CursorType.CT_THROW);
		}
		else
		{
			_map.setCursorType(Map.CursorType.CT_AIM);
		}
	}
	else
	{
		_map.setCursorType(Map.CursorType.CT_NORMAL);
	}
}

/**
 * Updates soldier name/rank/tu/energy/health/morale.
 * @param battleUnit Pointer to current unit.
 */
public void updateSoldierInfo(BattleUnit battleUnit)
{

	for (int i = 0; i < 10; ++i)
	{
		_btnVisibleUnit[i].hide();
		_numVisibleUnit[i].hide();
		_visibleUnit[i] = null;
	}

	if (battleUnit == null)
	{
		_txtName.setText(L"");
		_rank.clear();
		_numTimeUnits.clear();
		_barTimeUnits.clear();
		_barTimeUnits.clear();
		_numEnergy.clear();
		_barEnergy.clear();
		_barEnergy.clear();
		_numHealth.clear();
		_barHealth.clear();
		_barHealth.clear();
		_numMorale.clear();
		_barMorale.clear();
		_barMorale.clear();
		_btnLeftHandItem.clear();
		_btnRightHandItem.clear();
		return;
	}

	_txtName.setText(battleUnit.getUnit().getName());
	Soldier soldier = (Soldier)(battleUnit.getUnit());
	if (soldier != null)
	{
		SurfaceSet texture = _game.getResourcePack().getSurfaceSet("BASEBITS.PCK");
		texture.getFrame(soldier.getRankSprite()).blit(_rank);
	}
	_numTimeUnits.setValue(battleUnit.getTimeUnits());
	_barTimeUnits.setMax(battleUnit.getUnit().getTimeUnits());
	_barTimeUnits.setValue(battleUnit.getTimeUnits());
	_numEnergy.setValue(battleUnit.getEnergy());
	_barEnergy.setMax(battleUnit.getUnit().getStamina());
	_barEnergy.setValue(battleUnit.getEnergy());
	_numHealth.setValue(battleUnit.getHealth());
	_barHealth.setMax(battleUnit.getUnit().getHealth());
	_barHealth.setValue(battleUnit.getHealth());
	_numMorale.setValue(battleUnit.getMorale());
	_barMorale.setMax(100);
	_barMorale.setValue(battleUnit.getMorale());

	BattleItem leftHandItem = _battleGame.getItemFromUnit(battleUnit, InventorySlot.LEFT_HAND);
	_btnLeftHandItem.clear();
	_numAmmoLeft.clear();
	if (leftHandItem != null)
	{
		drawItemSprite(leftHandItem, _btnLeftHandItem);
		if (leftHandItem.getAmmoItem() != null)
			_numAmmoLeft.setValue(leftHandItem.getAmmoItem().getAmmoQuantity());
	}
	BattleItem rightHandItem = _battleGame.getItemFromUnit(battleUnit, InventorySlot.RIGHT_HAND);
	_btnRightHandItem.clear();
	_numAmmoRight.clear();
	if (rightHandItem != null)
	{
		drawItemSprite(rightHandItem, _btnRightHandItem);
		if (rightHandItem.getAmmoItem()!= null)
			_numAmmoRight.setValue(rightHandItem.getAmmoItem().getAmmoQuantity());
	}

	_battleGame.getTerrainModifier().calculateFOV(_battleGame.getSelectedUnit());
	int j = 0;
	for (BattleUnit i: battleUnit.getVisibleUnits())
	{
		_btnVisibleUnit[j].show();
		_numVisibleUnit[j].show();
		_visibleUnit[j] = (i);
		j++;
	}
}

/**
  * Shift the red colors of the visible unit buttons backgrounds.
  */
private void blinkVisibleUnitButtons()
{
	int delta = 1, color = 32;

	Rect square1;
	square1.left = 0;
	square1.top = 0;
	square1.right = 15;
	square1.bottom = 12;
	Rect square2;
	square2.left = 1;
	square2.top = 1;
	square2.right = square2.left + 13;
	square2.bottom = square2.top + 10;

	for (int i = 0; i < 10;  ++i)
	{
		if (_btnVisibleUnit[i].getVisible() == true)
		{
			_btnVisibleUnit[i].drawRect(square1, 15);
			_btnVisibleUnit[i].drawRect(square2, color);
		}
	}

	if (color == 44) delta = -2;
	if (color == 32) delta = 1;

	color += delta;
}


/**
  * Shift the red color of the warning message.
  */
private void blinkWarningMessage()
{
	int color = 32, delay = 12;

	if (_warningMessageBackground.getVisible() == false)
		return;

	Rect square1;
	square1.x = 0;
	square1.y = 0;
	square1.w = 224;
	square1.h = 48;
	_warningMessageBackground.drawRect(square1, color);

	if (color >= 44)
	{
		delay--;
	}
	else
	{
		color++;
	}

	if (delay == 0)
	{
		_warningMessageBackground.setVisible(false);
		_txtWarningMessage.setVisible(false);
		color = 32;
		delay = 12;
	}

}

/**
  * Show warning message.
  * @param message untranslated
  */
private void showWarningMessage(String message)
{
	WString messageText = _game.getLanguage().getString(message);
	_warningMessageBackground.setVisible(true);
	_txtWarningMessage.setVisible(true);
	_txtWarningMessage.setText(messageText);
}

/*
 * This function popups a context sensitive list of actions the user can choose from.
 * Some actions result in a change of gamestate.
 * @param item Item the user clicked on (righthand/lefthand)
 */
private void handleItemClick(BattleItem item)
{
	// make sure there is an item, and the battlescape is in an idle state
	if (item != null && _states.isEmpty())
	{
		BattleUnit bu = _battleGame.getSelectedUnit();
		_action.actor = bu;
		_action.weapon = item;
		popup(new ActionMenuState(_game, _action));
	}
}

/**
 * Draws the item's sprite on a surface.
 * @param item the given item
 * @surface surface the given surface
 */
private void drawItemSprite(BattleItem item, Surface surface)
{
	SurfaceSet texture = _game.getResourcePack().getSurfaceSet("BIGOBS.PCK");
	Surface frame = texture.getFrame(item.getRules().getBigSprite());
	frame.setX((2 - item.getRules().getSizeX()) * 8);
	frame.setY((3 - item.getRules().getSizeY()) * 8);
	texture.getFrame(item.getRules().getBigSprite()).blit(surface);
}

/**
 * Give time slice to the front state.
 */
public void handleState()
{
	if (!_states.isEmpty())
	{
		_states.get(0).think();
		_map.draw(true); // redraw map
	}
}

/**
 * Animate map objects on the map, also smoke,fire,....
 */
public void animate()
{
	_animFrame++;
	if (_animFrame == 8) _animFrame = 0;
	_map.animate();

	blinkVisibleUnitButtons();
	blinkWarningMessage();
}

/**
 * Get a pointer to the current action.
 */
BattleAction getAction()
{
	return _action;
}

/**
 * Get pointer to the game. Some states need this info.
 */
public Game getGame()
{
	return _game;
}

/**
 * Get pointer to the map. Some states need this info.
 */
public Map getMap()
{
	return _map;
}

/**
 * Push a state at the front of the queue and start it.
 * @param bs Battlestate.
 */
public void statePushFront(BattleState bs)
{
	_states.add(0,bs);
	bs.init();
}

/**
 * Push a state as the next state after the current one.
 * @param bs Battlestate.
 */
public void statePushNext(BattleState bs)
{
	if (_states.isEmpty())
	{
		_states.add(0,bs);
		bs.init();
	}
	else
	{
		_states.insert(++_states.begin(), bs);
	}

}

/**
 * Push a state at the back.
 * @param bs Battlestate.
 */
public void statePushBack(BattleState bs)
{
	if (_states.isEmpty())
	{
		_states.add(0,bs);
		// end turn request?
		if (_states.get(0) == null)
		{
			_states.pop_front();
			endTurn();
			return;
		}
		else
		{
			bs.init();
		}
	}
	else
	{
		_states.add(bs);
	}
}

/**
 * Pop the current state. Handle errors and mouse cursor appearing again.
 * States pop themselves when they are finished.
 */
public void popState()
{
	boolean actionFailed = false;

	if (_states.isEmpty()) return;

	if (_states.get(0).getResult().length() > 0)
	{
		showWarningMessage(_states.get(0).getResult());
		actionFailed = true;
	}
	_states.pop_front();

	if (_states.isEmpty())
	{
		if (_action.targeting && _battleGame.getSelectedUnit() != null && !actionFailed)
		{
			_battleGame.getSelectedUnit().spendTimeUnits(_action.TU, _battleGame.getDebugMode());
		}
		if (_action.type == BattleActionType.BA_THROW && !actionFailed)
		{
			_action.targeting = false;
			_action.type = BattleActionType.BA_NONE;
		}
		_game.getCursor().setVisible(true);
		setupCursor();
	}
	else
	{
		// end turn request?
		if (_states.get(0) == null)
		{
			_states.pop_front();
			endTurn();
			return;
		}
		// init the next state in queue
		_states.get(0).init();
	}
	if (_battleGame.getSelectedUnit() == null || _battleGame.getSelectedUnit().isOut())
	{
		_action.targeting = false;
		_action.type = BattleActionType.BA_NONE;
		_map.setCursorType(CursorType.CT_NORMAL);
		_game.getCursor().setVisible(true);
		_battleGame.setSelectedUnit(null);
	}
	updateSoldierInfo(_battleGame.getSelectedUnit());
}

/**
 * Sets the timer interval for think() calls of the state.
 * @param interval An interval in ms.
 */
public void setStateInterval(long interval)
{
	_stateTimer.setInterval(interval);
}

/**
 * Show a debug message in the topleft corner.
 * @param message Debug message.
 */
public void debug(String message) //was WString
{
	if (_battleGame.getDebugMode())
	{
		_txtDebug.setText(message);
	}
}

/**
 * Takes care of any events from the core game engine.
 * @param action Pointer to an action.
 */
public void handle(Action action)
{
	if (_game.getCursor().getVisible() || action.getDetails().button.button == SDL_BUTTON_RIGHT)
	{
		super.handle(action);

		if (action.getDetails().type == SDL_KEYDOWN)
		{
				// "d" - enable debug mode
			if (action.getDetails().key.keysym.sym == SDLK_d)
			{
				_battleGame.setDebugMode();
				debug(L"Debug Mode");
			}
		}
	}

}

/**
 * Adds a new popup window to the queue
 * (this prevents popups from overlapping)
 * @param state Pointer to popup state.
 */
public void popup(State state)
{
	_popups.add(state);
}

}
