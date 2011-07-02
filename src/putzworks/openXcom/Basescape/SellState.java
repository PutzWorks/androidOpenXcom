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

import java.util.Map;
import java.util.Vector;

import putzworks.openXcom.Engine.*;
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Interface.TextList.ArrowOrientation;
import putzworks.openXcom.Ruleset.RuleItem;
import putzworks.openXcom.Savegame.*;

public class SellState extends State
{
	private Base _base;
	private TextButton _btnOk, _btnCancel;
	private Window _window;
	private Text _txtTitle, _txtSales, _txtFunds, _txtItem, _txtQuantity, _txtSell, _txtValue;
	private TextList _lstItems;
	private Vector<Integer> _qtys;
	private Vector<Soldier> _soldiers;
	private Vector<Craft> _crafts;
	private Vector<String> _items;
	private int _sel;
	private int _total, _sOffset, _eOffset;
	private Timer _timerInc, _timerDec;

/**
 * Initializes all the elements in the Sell/Sack screen.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 */
public SellState(Game game, Base base)
{
	super(game);
	_base = base; 
	_qtys = new Vector<Integer>(); 
	_soldiers = new Vector<Soldier>();  
	_crafts = new Vector<Craft>();  
	_items = new Vector<String>();  
	_sel = 0;
	_total = 0; 
	_sOffset = 0; 
	_eOffset = 0;

	// Create objects
	_window = new Window(this, 320, 200, 0, 0);
	_btnOk = new TextButton(148, 16, 8, 176);
	_btnCancel = new TextButton(148, 16, 164, 176);
	_txtTitle = new Text(310, 16, 5, 8);
	_txtSales = new Text(190, 9, 10, 24);
	_txtFunds = new Text(114, 9, 200, 24);
	_txtItem = new Text(130, 9, 10, 32);
	_txtQuantity = new Text(44, 9, 140, 32);
	_txtSell = new Text(96, 9, 184, 32);
	_txtValue = new Text(34, 9, 280, 32);
	_lstItems = new TextList(288, 120, 8, 44);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(0)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_btnCancel);
	add(_txtTitle);
	add(_txtSales);
	add(_txtFunds);
	add(_txtItem);
	add(_txtQuantity);
	add(_txtSell);
	add(_txtValue);
	add(_lstItems);

	// Set up objects
	_window.setColor(Palette.blockOffset(13)+13);
	_window.setBackground(_game.getResourcePack().getSurface("BACK13.SCR"));

	_btnOk.setColor(Palette.blockOffset(13)+13);
	_btnOk.setText(_game.getLanguage().getString("STR_SELL_SACK"));
	_btnOk.onMouseClick((ActionHandler)SellState.btnOkClick);

	_btnCancel.setColor(Palette.blockOffset(13)+13);
	_btnCancel.setText(_game.getLanguage().getString("STR_CANCEL"));
	_btnCancel.onMouseClick((ActionHandler)SellState.btnCancelClick);

	_txtTitle.setColor(Palette.blockOffset(13)+10);
	_txtTitle.setBig();
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setText(_game.getLanguage().getString("STR_SELL_ITEMS_SACK_PERSONNEL"));

	WString s1 = _game.getLanguage().getString("STR_VALUE_OF_SALES");
	s1 += Text.formatFunding(_total);
	_txtSales.setColor(Palette.blockOffset(13)+10);
	_txtSales.setText(s1);

	WString s2 = _game.getLanguage().getString("STR_FUNDS");
	s2 += Text.formatFunding(_game.getSavedGame().getFunds());
	_txtFunds.setColor(Palette.blockOffset(13)+10);
	_txtFunds.setText(s2);

	_txtItem.setColor(Palette.blockOffset(13)+10);
	_txtItem.setText(_game.getLanguage().getString("STR_ITEM"));

	_txtQuantity.setColor(Palette.blockOffset(13)+10);
	_txtQuantity.setText(_game.getLanguage().getString("STR_QUANTITY_UC"));

	_txtSell.setColor(Palette.blockOffset(13)+10);
	_txtSell.setText(_game.getLanguage().getString("STR_SELL_SACK"));

	_txtValue.setColor(Palette.blockOffset(13)+10);
	_txtValue.setText(_game.getLanguage().getString("STR_VALUE"));

	_lstItems.setColor(Palette.blockOffset(13)+10);
	_lstItems.setArrowColor(Palette.blockOffset(13)+13);
	_lstItems.setArrowColumn(189, ArrowOrientation.ARROW_VERTICAL);
	_lstItems.setColumns(4, 156, 62, 28, 40);
	_lstItems.setSelectable(true);
	_lstItems.setBackground(_window);
	_lstItems.setMargin(2);
	_lstItems.onLeftArrowPress((ActionHandler)SellState.lstItemsLeftArrowPress);
	_lstItems.onLeftArrowRelease((ActionHandler)SellState.lstItemsLeftArrowRelease);
	_lstItems.onRightArrowPress((ActionHandler)SellState.lstItemsRightArrowPress);
	_lstItems.onRightArrowRelease((ActionHandler)SellState.lstItemsRightArrowRelease);

	for (Soldier i: _base.getSoldiers())
	{
		if ((i).getCraft() == null)
		{
			_qtys.add(0);
			_soldiers.add(i);
			_lstItems.addRow(4, (i).getName().c_str(), L"1", L"0", Text.formatFunding(0).c_str());
		}
	}
	for (Craft i: _base.getCrafts())
	{
		if ((i).getStatus() != "STR_OUT")
		{
			_qtys.add(0);
			_crafts.add(i);
			_lstItems.addRow(4, (i).getName(_game.getLanguage()).c_str(), L"1", L"0", Text.formatFunding(0).c_str());
		}
	}
	if (_base.getAvailableScientists() > 0)
	{
		_qtys.add(0);
		_sOffset++;
		WStringstream ss;
		ss << _base.getAvailableScientists();
		_lstItems.addRow(4, _game.getLanguage().getString("STR_SCIENTIST").c_str(), ss.str().c_str(), L"0", Text.formatFunding(0).c_str());
	}
	if (_base.getAvailableEngineers() > 0)
	{
		_qtys.add(0);
		_eOffset++;
		WStringstream ss;
		ss << _base.getAvailableEngineers();
		_lstItems.addRow(4, _game.getLanguage().getString("STR_ENGINEER").c_str(), ss.str().c_str(), L"0", Text.formatFunding(0).c_str());
	}
	for (Map<String, Integer>.iterator i = _base.getItems().getContents().begin(); i != _base.getItems().getContents().end(); ++i)
	{
		_qtys.add(0);
		_items.add(i.first);
		RuleItem rule = _game.getRuleset().getItem(i.first);
		WStringstream ss;
		ss << i.second;
		_lstItems.addRow(4, _game.getLanguage().getString(i.first).c_str(), ss.str().c_str(), L"0", Text.formatFunding(rule.getCost() / 2).c_str());
	}

	_timerInc = new Timer(50);
	_timerInc.onTimer((StateHandler)SellState.increase);
	_timerDec = new Timer(50);
	_timerDec.onTimer((StateHandler)SellState.decrease);
}

/**
 *
 */
public void clearSellState()
{
	_timerInc = null;
	_timerDec = null;
}

/**
 * Runs the arrow timers.
 */
public void think()
{
	super.think();

	_timerInc.think(this, null);
	_timerDec.think(this, null);
}

/**
 * Sells the selected items.
 * @param action Pointer to an action.
 */
public void btnOkClick(Action action)
{
	_game.getSavedGame().setFunds(_game.getSavedGame().getFunds() + _total);
	for (int i = 0; i < _qtys.size(); ++i)
	{
		if (_qtys[i] > 0)
		{
			// Sell soldiers
			if (i < _soldiers.size())
			{
				_soldiers[i] = null;;
				for (Soldier s: _base.getSoldiers())
				{
					if (s == _soldiers[i])
					{
						_base.getSoldiers().erase(s);
						break;
					}
				}
			}
			// Sell crafts
			else if (i >= _soldiers.size() && i < _soldiers.size() + _crafts.size())
			{
				Craft craft =  _crafts[i - _soldiers.size()];

				// Remove weapons from craft
				for (CraftWeapon w: craft.getWeapons())
				{
					if (w != null)
					{
						_base.getItems().addItem((w).getRules().getLauncherItem());
						_base.getItems().addItem((w).getRules().getClipItem(), (int)floor((double)(w).getAmmo() / (w).getRules().getRearmRate()));
					}
				}

				// Remove items from craft
				for (Map<String, Integer> it: craft.getItems().getContents())
				{
					_base.getItems().addItem(it.first, it.second);
				}

				// Remove soldiers from craft
				for (Soldier s: _base.getSoldiers())
				{
					if ((s).getCraft() == craft)
					{
						(s).setCraft(0);
					}
				}

				// Remove craft
				craft = null;
				for (Craft c: _base.getCrafts())
				{
					if (c == craft)
					{
						_base.getCrafts().erase(c);
						break;
					}
				}
			}
			// Sell scientists
			else if (_base.getAvailableScientists() > 0 && i == _soldiers.size() + _crafts.size())
			{
				_base.setScientists(_base.getScientists() - _qtys[i]);
			}
			// Sell engineers
			else if (_base.getAvailableEngineers() > 0 && i == _soldiers.size() + _crafts.size() + _sOffset)
			{
				_base.setEngineers(_base.getEngineers() - _qtys[i]);
			}
			// Sell items
			else
			{
				_base.getItems().removeItem(_items[i - _soldiers.size() - _crafts.size() - _sOffset - _eOffset], _qtys[i]);
			}
		}
	}
	_game.popState();
}

/**
 * Returns to the previous screen.
 * @param action Pointer to an action.
 */
public void btnCancelClick(Action action)
{
	_game.popState();
}

/**
 * Starts increasing the item.
 * @param action Pointer to an action.
 */
public void lstItemsLeftArrowPress(Action action)
{
	_sel = _lstItems.getSelectedRow();
	_timerInc.start();
}

/**
 * Stops increasing the item.
 * @param action Pointer to an action.
 */
public void lstItemsLeftArrowRelease(Action action)
{
	_timerInc.stop();
}

/**
 * Starts decreasing the item.
 * @param action Pointer to an action.
 */
public void lstItemsRightArrowPress(Action action)
{
	_sel = _lstItems.getSelectedRow();
	_timerDec.start();
}

/**
 * Stops decreasing the item.
 * @param action Pointer to an action.
 */
public void lstItemsRightArrowRelease(Action action)
{
	_timerDec.stop();
}

/**
 * Gets the price of the currently selected item.
 */
private int getPrice()
{
	// Personnel/craft aren't worth anything
	if (_sel < _soldiers.size() + _crafts.size() + _sOffset + _eOffset)
	{
		return 0;
	}
	// Item cost
	else
	{
		return _game.getRuleset().getItem(_items[_sel - _soldiers.size() - _crafts.size() - _sOffset - _eOffset]).getCost() / 2;
	}
}

/**
 * Gets the quantity of the currently selected item
 * on the base.
 */
private int getQuantity()
{
	// Soldiers/crafts are individual
	if (_sel < _soldiers.size() + _crafts.size())
	{
		return 1;
	}
	// Scientist quantity
	else if (_base.getAvailableScientists() > 0 && _sel == _soldiers.size() + _crafts.size())
	{
		return _base.getAvailableScientists();
	}
	// Engineer quantity
	else if (_base.getAvailableEngineers() > 0 && _sel == _soldiers.size() + _crafts.size() + _sOffset)
	{
		return _base.getAvailableEngineers();
	}
	// Item quantity
	else
	{
		return _base.getItems().getItem(_items[_sel - _soldiers.size() - _crafts.size() - _sOffset - _eOffset]);
	}
}

/**
 * Increases the quantity of the selected item to sell.
 */
public void increase()
{
	if (_qtys[_sel] < getQuantity())
	{
		_qtys[_sel]++;
		WStringstream ss;
		ss << _qtys[_sel];
		_lstItems.getCell(_sel, 2).setText(ss.str());
		_lstItems.draw();
		_total += getPrice();
		WString s = _game.getLanguage().getString("STR_VALUE_OF_SALES");
		s += Text.formatFunding(_total);
		_txtSales.setText(s);
	}
}

/**
 * Decreases the quantity of the selected item to sell.
 */
public void decrease()
{
	if (_qtys[_sel] > 0)
	{
		_qtys[_sel]--;
		WStringstream ss;
		ss << _qtys[_sel];
		_lstItems.getCell(_sel, 2).setText(ss.str());
		_lstItems.draw();
		_total -= getPrice();
		WString s = _game.getLanguage().getString("STR_VALUE_OF_SALES");
		s += Text.formatFunding(_total);
		_txtSales.setText(s);
	}
}

}
