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

import putzworks.openXcom.Battlescape.BattlescapeState.BattleAction;
import putzworks.openXcom.Battlescape.BattlescapeState.BattleActionType;
import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.State;

public class ActionMenuState extends State
{
	BattleAction _action;
	ActionMenuItem[] _actionMenu;

/**
 * Initializes all the elements in the Action Menu window.
 * @param game Pointer to the core game.
 * @param unit Pointer to the unit that is doing the action.
 * @param item Pointer to the selected item.
 */
public ActionMenuState(Game game, BattleAction action)
{
	super(game);
	_actionMenu = new ActionMenuItem[5];
	_action = action;
	_screen = false;
	_game.setPalette(_game.getResourcePack().getPalette("PALETTES.DAT_4").getColors());

	for (int i = 0; i < 5; ++i)
	{
		_actionMenu[i] = new ActionMenuItem(this, i, _game.getResourcePack().getFont("BIGLETS.DAT"));
		add(_actionMenu[i]);
		_actionMenu[i].setVisible(false);
		_actionMenu[i].onMouseClick(new ActionHandler() {
			public void handle(Action action) {
				btnActionMenuItemClick(action);
			}
		});
	}

	// Build up the popup menu
	int id = 0, tu;
	String strAcc = _game.getLanguage().getString("STR_ACC");
	String strTU = _game.getLanguage().getString("STR_TUS");
	StringBuffer ss1 = new StringBuffer(), ss2 = new StringBuffer();

	// throwing
	tu = (int)Math.floor(_action.actor.getUnit().getTimeUnits() * 0.25);
	ss1.append(strAcc + (int)Math.floor(_action.actor.getThrowingAccuracy() * 100) + "%");
	ss2.append(strTU + tu);
	_actionMenu[id].setAction(BattleActionType.BA_THROW, _game.getLanguage().getString("STR_THROW"), ss1.toString(), ss2.toString(), tu);
	_actionMenu[id].setVisible(true);
	id++;
	ss1.delete(0, ss1.length());
	ss2.delete(0, ss2.length());

	if (_action.weapon.getRules().getAccuracyAuto() != 0)
	{
		tu = (int)(_action.actor.getUnit().getTimeUnits() * _action.weapon.getRules().getTUAuto() / 100);
		ss1.append(strAcc + (int)Math.floor(_action.actor.getFiringAccuracy(_action.weapon.getRules().getAccuracyAuto()) * 100) + "%");
		ss2.append(strTU + tu);
		_actionMenu[id].setAction(BattleActionType.BA_AUTOSHOT, _game.getLanguage().getString("STR_AUTO_SHOT"), ss1.toString(), ss2.toString(), tu);
		_actionMenu[id].setVisible(true);
		id++;
		ss1.delete(0, ss1.length());
		ss2.delete(0, ss2.length());
	}
	if (_action.weapon.getRules().getAccuracySnap() != 0)
	{
		tu = (int)(_action.actor.getUnit().getTimeUnits() * _action.weapon.getRules().getTUSnap() / 100);
		ss1.append(strAcc + (int)Math.floor(_action.actor.getFiringAccuracy(_action.weapon.getRules().getAccuracySnap()) * 100) + "%");
		ss2.append(strTU + tu);
		_actionMenu[id].setAction(BattleActionType.BA_SNAPSHOT, _game.getLanguage().getString("STR_SNAP_SHOT"), ss1.toString(), ss2.toString(), tu);
		_actionMenu[id].setVisible(true);
		id++;
		ss1.delete(0, ss1.length());
		ss2.delete(0, ss2.length());
	}
	if (_action.weapon.getRules().getAccuracyAimed() != 0)
	{
		tu = (int)(_action.actor.getUnit().getTimeUnits() * _action.weapon.getRules().getTUAimed() / 100);
		ss1.append(strAcc + (int)Math.floor(_action.actor.getFiringAccuracy(_action.weapon.getRules().getAccuracyAimed()) * 100) + "%");
		ss2.append(strTU + tu);
		_actionMenu[id].setAction(BattleActionType.BA_AIMEDSHOT, _game.getLanguage().getString("STR_AIMED_SHOT"), ss1.toString(), ss2.toString(), tu);
		_actionMenu[id].setVisible(true);
		id++;
		ss1.delete(0, ss1.length());
		ss2.delete(0, ss2.length());
	}

}

/**
 * Execute the action corresponding with this action menu item.
 * @param action Pointer to an action.
 */
public void btnActionMenuItemClick(Action action)
{
	int btnID = -1;

	if (action.getDetails().button.button == SDL_BUTTON_RIGHT)
	{
		_game.popState();
		return;
	}

	// got to find out which button was pressed
	for (int i = 0; i < 10 && btnID == -1; ++i)
	{
		if (action.getSender() == _actionMenu[i])
		{
			btnID = i;
		}
	}

	if (btnID != -1)
	{
		_action.type = _actionMenu[btnID].getAction();
		_action.TU = _actionMenu[btnID].getTUs();
		_action.targeting = true;
		_game.popState();
	}
}

}
