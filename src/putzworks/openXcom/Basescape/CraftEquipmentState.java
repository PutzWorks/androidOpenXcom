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
package putzworks.openXcom.Basescape;

import java.util.Vector;

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Engine.StateHandler;
import putzworks.openXcom.Engine.Timer;
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Interface.TextList.ArrowOrientation;
import putzworks.openXcom.Ruleset.RuleItem;
import putzworks.openXcom.Ruleset.RuleItem.BattleType;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.Craft;
import putzworks.openXcom.Savegame.Soldier;

public class CraftEquipmentState extends State
{
	private TextButton _btnOk;
	private Window _window;
	private Text _txtTitle, _txtItem, _txtStores, _txtAvailable, _txtUsed;
	private TextList _lstEquipment;
	private Timer _timerLeft, _timerRight;
	private int _sel;
	private Base _base;
	private int _craft;
	private Vector<String> _items;

/**
 * Initializes all the elements in the Craft Equipment screen.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 * @param craft ID of the selected craft.
 */
public CraftEquipmentState(Game game, Base base, int craft)
{
	super(game);
	_base = base;
	_craft = craft;

	// Create objects
	_window = new Window(this, 320, 200, 0, 0);
	_btnOk = new TextButton(288, 16, 16, 176);
	_txtTitle = new Text(300, 16, 16, 7);
	_txtItem = new Text(144, 9, 16, 32);
	_txtStores = new Text(150, 9, 160, 32);
	_txtAvailable = new Text(110, 9, 16, 24);
	_txtUsed = new Text(110, 9, 130, 24);
	_lstEquipment = new TextList(288, 128, 8, 40);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(2)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_txtTitle);
	add(_txtItem);
	add(_txtStores);
	add(_txtAvailable);
	add(_txtUsed);
	add(_lstEquipment);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+4);
	_window.setBackground(_game.getResourcePack().getSurface("BACK04.SCR"));

	_btnOk.setColor(Palette.blockOffset(15)+4);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnOkClick(action);
		}
	});

	_txtTitle.setColor(Palette.blockOffset(15)+1);
	_txtTitle.setBig();
	Craft c = _base.getCrafts().get(_craft);
	String s;
	s = _game.getLanguage().getString("STR_EQUIPMENT_FOR") + c.getName(_game.getLanguage());
	_txtTitle.setText(s);

	_txtItem.setColor(Palette.blockOffset(15)+1);
	_txtItem.setText(_game.getLanguage().getString("STR_ITEM"));

	_txtStores.setColor(Palette.blockOffset(15)+1);
	_txtStores.setText(_game.getLanguage().getString("STR_STORES"));

	_txtAvailable.setColor(Palette.blockOffset(15)+1);
	StringBuffer ss = new StringBuffer();
	ss.append(_game.getLanguage().getString("STR_SPACE_AVAILABLE") + (c.getRules().getSoldiers() - c.getNumSoldiers()));
	_txtAvailable.setText(ss.toString());

	_txtUsed.setColor(Palette.blockOffset(15)+1);
	StringBuffer ss2 = new StringBuffer();
	ss2.append(_game.getLanguage().getString("STR_SPACE_USED") + c.getNumSoldiers());
	_txtUsed.setText(ss2.toString());

	_lstEquipment.setColor(Palette.blockOffset(13)+10);
	_lstEquipment.setArrowColor(Palette.blockOffset(15)+4);
	_lstEquipment.setArrowColumn(203, ArrowOrientation.ARROW_HORIZONTAL);
	_lstEquipment.setColumns(3, 154, 85, 41);
	_lstEquipment.setSelectable(true);
	_lstEquipment.setBackground(_window);
	_lstEquipment.setMargin(8);
	_lstEquipment.onLeftArrowPress(new ActionHandler() {
		public void handle(Action action) {
			lstEquipmentLeftArrowPress(action);
		}
	});
	_lstEquipment.onLeftArrowRelease(new ActionHandler() {
		public void handle(Action action) {
			lstEquipmentLeftArrowRelease(action);
		}
	});
	_lstEquipment.onRightArrowPress(new ActionHandler() {
		public void handle(Action action) {
			lstEquipmentRightArrowPress(action);
		}
	});
	_lstEquipment.onRightArrowRelease(new ActionHandler() {
		public void handle(Action action) {
			lstEquipmentRightArrowRelease(action);
		}
	});

	_items.add("STR_PISTO");
	_items.add("STR_PISTOL_CLIP");
	_items.add("STR_RIFLE");
	_items.add("STR_RIFLE_CLIP");
	_items.add("STR_HEAVY_CANNON");
	_items.add("STR_HC_AP_AMMO");
	_items.add("STR_HC_HE_AMMO");
	_items.add("STR_AUTO_CANNON");
	_items.add("STR_AC_AP_AMMO");
	_items.add("STR_ROCKET_LAUNCHER");
	_items.add("STR_SMALL_ROCKET");
	_items.add("STR_GRENADE");
	_items.add("STR_SMOKE_GRENADE");

	int row = 0;
	for (String i: _items)
	{
		if (_base.getItems().getItem(i) == 0 && c.getItems().getItem(i) == 0)
		{
			i = _items.erase(i);
		}
		else
		{
			StringBuffer ss = new StringBuffer(), ss2 = new StringBuffer();
			ss.append(_base.getItems().getItem(i));
			ss2.append(c.getItems().getItem(i));

			RuleItem rule = _game.getRuleset().getItem(i);
			String s = _game.getLanguage().getString(i);
			if (rule.getBattleType() == BattleType.BT_AMMO)
			{
				s.insert(0, "  ");
			}
			_lstEquipment.addRow(3, s, ss.toString(), ss2.toString());

			short color;
			if (c.getItems().getItem(i) == 0)
			{
				if (rule.getBattleType() == BattleType.BT_AMMO)
				{
					color = (short)(Palette.blockOffset(15)+6);
				}
				else
				{
					color = (short)(Palette.blockOffset(13)+10);
				}
			}
			else
			{
				color = Palette.blockOffset(13);
			}
			_lstEquipment.getCell(row, 0).setColor(color);
			_lstEquipment.getCell(row, 1).setColor(color);
			_lstEquipment.getCell(row, 2).setColor(color);

			++i;
			row++;
		}
	}
	_lstEquipment.draw();

	_timerLeft = new Timer(50);
	_timerLeft.onTimer(new StateHandler() {
		public void handle(State state) {
			moveLeft();
		}
	});
	_timerRight = new Timer(50);
	_timerRight.onTimer(new StateHandler() {
		public void handle(State state) {
			moveRight();
		}
	});
}

/**
 *
 */
public void clearCraftEquipmentState()
{
	_timerLeft = null;;
	_timerRight = null;;
}

/**
 * Runs the arrow timers.
 */
public void think()
{
	super.think();

	_timerLeft.think(this, null);
	_timerRight.think(this, null);
}


/**
 * Returns to the previous screen.
 * @param action Pointer to an action.
 */
public void btnOkClick(Action action)
{
	_game.popState();
}

/**
 * Starts moving the item to the base.
 * @param action Pointer to an action.
 */
public void lstEquipmentLeftArrowPress(Action action)
{
	_sel = _lstEquipment.getSelectedRow();
	_timerLeft.start();
}

/**
 * Stops moving the item to the base.
 * @param action Pointer to an action.
 */
public void lstEquipmentLeftArrowRelease(Action action)
{
	_timerLeft.stop();
}

/**
 * Starts moving the item to the craft.
 * @param action Pointer to an action.
 */
public void lstEquipmentRightArrowPress(Action action)
{
	_sel = _lstEquipment.getSelectedRow();
	_timerRight.start();
}

/**
 * Stops moving the item to the craft.
 * @param action Pointer to an action.
 */
public void lstEquipmentRightArrowRelease(Action action)
{
	_timerRight.stop();
}

/**
 * Updates the displayed quantities of the
 * selected item on the list.
 */
private void updateQuantity()
{
	Craft c = _base.getCrafts().get(_craft);
	StringBuffer ss = new StringBuffer(), ss2 = new StringBuffer();
	ss.append(_base.getItems().getItem(_items.get(_sel)));
	ss2.append(c.getItems().getItem(_items.get(_sel)));

	short color;
	if (c.getItems().getItem(_items.get(_sel)) == 0)
	{
		RuleItem rule = _game.getRuleset().getItem(_items.get(_sel));
		if (rule.getBattleType() == BattleType.BT_AMMO)
		{
			color = (short)(Palette.blockOffset(15)+6);
		}
		else
		{
			color = (short)(Palette.blockOffset(13)+10);
		}
	}
	else
	{
		color = Palette.blockOffset(13);
	}
	_lstEquipment.getCell(_sel, 0).setColor(color);
	_lstEquipment.getCell(_sel, 1).setColor(color);
	_lstEquipment.getCell(_sel, 1).setText(ss.str());
	_lstEquipment.getCell(_sel, 2).setColor(color);
	_lstEquipment.getCell(_sel, 2).setText(ss2.str());
	_lstEquipment.draw();
}

/**
 * Moves the selected item to the base.
 */
public void moveLeft()
{
	Craft c = _base.getCrafts().get(_craft);
	if (c.getItems().getItem(_items.get(_sel)) > 0)
	{
		_base.getItems().addItem(_items.get(_sel));
		c.getItems().removeItem(_items.get(_sel));
		updateQuantity();
	}
}

/**
 * Moves the selected item to the craft.
 */
public void moveRight()
{
	Craft c = _base.getCrafts().get(_craft);
	if (_base.getItems().getItem(_items.get(_sel)) > 0)
	{
		_base.getItems().removeItem(_items.get(_sel));
		c.getItems().addItem(_items.get(_sel));
		updateQuantity();
	}
}

}
