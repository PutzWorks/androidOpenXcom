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

import putzworks.openXcom.Engine.*;
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Interface.TextList.ArrowOrientation;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.Craft;
import putzworks.openXcom.Savegame.Soldier;
import putzworks.openXcom.Savegame.Transfer;

public class TransferItemsState extends State
{
	private Base _baseFrom, _baseTo;
	private TextButton _btnOk, _btnCancel;
	private Window _window;
	private Text _txtTitle, _txtItem, _txtQuantity, _txtAmountTransfer, _txtAmountDestination;
	private TextList _lstItems;
	private Vector<Integer> _qtys;
	private Vector<Soldier> _soldiers;
	private Vector<Craft> _crafts;
	private Vector<String> _items;
	private int _sel;
	private int _total, _sOffset, _eOffset, _pQty, _cQty;
	private float _iQty;
	private double _distance;
	private Timer _timerInc, _timerDec;

/**
 * Initializes all the elements in the Transfer screen.
 * @param game Pointer to the core game.
 * @param baseFrom Pointer to the source base.
 * @param baseTo Pointer to the destination base.
 */
public TransferItemsState(Game game, Base baseFrom, Base baseTo)
{
	super(game);
	_baseFrom = baseFrom;
	_baseTo = baseTo; 
	_qtys = new Vector<Integer>(); 
	_soldiers = new Vector<Soldier>(); 
	_crafts = new Vector<Craft>(); 
	_items = new Vector<String>(); 
	_sel = 0;
	_total = 0; 
	_sOffset = 0; 
	_eOffset = 0; 
	_pQty = 0; 
	_cQty = 0; 
	_iQty = 0; 
	_distance = 0;

	// Create objects
	_window = new Window(this, 320, 200, 0, 0);
	_btnOk = new TextButton(148, 16, 8, 176);
	_btnCancel = new TextButton(148, 16, 164, 176);
	_txtTitle = new Text(310, 16, 5, 8);
	_txtItem = new Text(130, 9, 10, 22);
	_txtQuantity = new Text(50, 9, 150, 22);
	_txtAmountTransfer = new Text(60, 16, 200, 22);
	_txtAmountDestination = new Text(60, 16, 260, 22);
	_lstItems = new TextList(288, 128, 8, 40);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(0)), Palette.backPos, 16);

	add(_window);
	add(_btnOk);
	add(_btnCancel);
	add(_txtTitle);
	add(_txtItem);
	add(_txtQuantity);
	add(_txtAmountTransfer);
	add(_txtAmountDestination);
	add(_lstItems);

	// Set up objects
	_window.setColor(Palette.blockOffset(13)+13);
	_window.setBackground(_game.getResourcePack().getSurface("BACK13.SCR"));

	_btnOk.setColor(Palette.blockOffset(15)+9);
	_btnOk.setText(_game.getLanguage().getString("STR_TRANSFER"));
	_btnOk.onMouseClick((ActionHandler)TransferItemsState.btnOkClick);

	_btnCancel.setColor(Palette.blockOffset(15)+9);
	_btnCancel.setText(_game.getLanguage().getString("STR_CANCEL"));
	_btnCancel.onMouseClick((ActionHandler)TransferItemsState.btnCancelClick);

	_txtTitle.setColor(Palette.blockOffset(13)+10);
	_txtTitle.setBig();
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setText(_game.getLanguage().getString("STR_TRANSFER"));

	_txtItem.setColor(Palette.blockOffset(13)+10);
	_txtItem.setText(_game.getLanguage().getString("STR_ITEM"));

	_txtQuantity.setColor(Palette.blockOffset(13)+10);
	_txtQuantity.setText(_game.getLanguage().getString("STR_QUANTITY_UC"));

	_txtAmountTransfer.setColor(Palette.blockOffset(13)+10);
	_txtAmountTransfer.setText(_game.getLanguage().getString("STR_AMOUNT_TO_TRANSFER"));

	_txtAmountDestination.setColor(Palette.blockOffset(13)+10);
	_txtAmountDestination.setText(_game.getLanguage().getString("STR_AMOUNT_AT_DESTINATION"));

	_lstItems.setColor(Palette.blockOffset(15)+1);
	_lstItems.setArrowColor(Palette.blockOffset(13)+13);
	_lstItems.setArrowColumn(193, ArrowOrientation.ARROW_VERTICAL);
	_lstItems.setColumns(4, 162, 58, 55, 11);
	_lstItems.setSelectable(true);
	_lstItems.setBackground(_window);
	_lstItems.setMargin(2);
	_lstItems.onLeftArrowPress((ActionHandler)TransferItemsState.lstItemsLeftArrowPress);
	_lstItems.onLeftArrowRelease((ActionHandler)TransferItemsState.lstItemsLeftArrowRelease);
	_lstItems.onRightArrowPress((ActionHandler)TransferItemsState.lstItemsRightArrowPress);
	_lstItems.onRightArrowRelease((ActionHandler)TransferItemsState.lstItemsRightArrowRelease);

	for (Vector<Soldier>i = _baseFrom.getSoldiers())
	{
		if ((i).getCraft() == 0)
		{
			_qtys.add(0);
			_soldiers.add(i);
			_lstItems.addRow(4, (i).getName().c_str(), L"1", L"0", L"0");
		}
	}
	for (Vector<Craft>i = _baseFrom.getCrafts())
	{
		if ((i).getStatus() != "STR_OUT")
		{
			_qtys.add(0);
			_crafts.add(i);
			_lstItems.addRow(4, (i).getName(_game.getLanguage()).c_str(), L"1", L"0", L"0");
		}
	}
	if (_baseFrom.getAvailableScientists() > 0)
	{
		_qtys.add(0);
		_sOffset++;
		WStringstream ss, ss2;
		ss << _baseFrom.getAvailableScientists();
		ss2 << _baseTo.getAvailableScientists();
		_lstItems.addRow(4, _game.getLanguage().getString("STR_SCIENTIST").c_str(), ss.str().c_str(), L"0", ss2.str().c_str());
	}
	if (_baseFrom.getAvailableEngineers() > 0)
	{
		_qtys.add(0);
		_eOffset++;
		WStringstream ss, ss2;
		ss << _baseFrom.getAvailableEngineers();
		ss2 << _baseTo.getAvailableEngineers();
		_lstItems.addRow(4, _game.getLanguage().getString("STR_ENGINEER").c_str(), ss.str().c_str(), L"0", ss2.str().c_str());
	}
	for (std.map<std.string, int>.iterator i = _baseFrom.getItems().getContents().begin(); i != _baseFrom.getItems().getContents().end(); ++i)
	{
		_qtys.add(0);
		_items.add(i.first);
		WStringstream ss, ss2;
		ss << i.second;
		ss2 << _baseTo.getItems().getItem(i.first);
		_lstItems.addRow(4, _game.getLanguage().getString(i.first).c_str(), ss.str().c_str(), L"0", ss2.str().c_str());
	}
	_distance = getDistance();

	_timerInc = new Timer(50);
	_timerInc.onTimer((StateHandler)TransferItemsState.increase);
	_timerDec = new Timer(50);
	_timerDec.onTimer((StateHandler)TransferItemsState.decrease);
}

/**
 *
 */
public void clearTransferItemsState()
{
	_timerInc = null;
	_timerDec = null;
}

/**
 * Resets the palette since it's bound to change on other screens.
 */
public void init()
{
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(0)), Palette.backPos, 16);
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
 * Transfers the selected items.
 * @param action Pointer to an action.
 */
public void btnOkClick(Action action)
{
	_game.pushState(new TransferConfirmState(_game, _baseTo, this));
}

public void completeTransfer()
{
	int time = (int)Math.floor(6 + _distance / 200.0);
	_game.getSavedGame().setFunds(_game.getSavedGame().getFunds() - _total);
	for (int i = 0; i < _qtys.size(); ++i)
	{
		if (_qtys[i] > 0)
		{
			// Transfer soldiers
			if (i < _soldiers.size())
			{
				for (Soldier s: _baseFrom.getSoldiers())
				{
					if (s == _soldiers[i])
					{
						_baseFrom.getSoldiers().erase(s);
						Transfer t = new Transfer(time);
						t.setSoldier(s);
						_baseTo.getTransfers().add(t);
						break;
					}
				}
			}
			// Transfer crafts
			else if (i >= _soldiers.size() && i < _soldiers.size() + _crafts.size())
			{
				Craft craft =  _crafts[i - _soldiers.size()];

				// Transfer soldiers inside craft
				for (Soldier s: _baseFrom.getSoldiers())
				{
					if ((s).getCraft() == craft)
					{
						_baseFrom.getSoldiers().erase(s);
						Transfer t = new Transfer(time);
						t.setSoldier(s);
						_baseTo.getTransfers().add(t);
					}
				}

				// Transfer craft
				for (Craft c: _baseFrom.getCrafts())
				{
					if (c == craft)
					{
						_baseFrom.getCrafts().erase(c);
						Transfer t = new Transfer(time);
						t.setCraft(c);
						_baseTo.getTransfers().add(t);
						break;
					}
				}
			}
			// Transfer scientists
			else if (_baseFrom.getAvailableScientists() > 0 && i == _soldiers.size() + _crafts.size())
			{
				_baseFrom.setScientists(_baseFrom.getScientists() - _qtys[i]);
				Transfer t = new Transfer(time);
				t.setScientists(_qtys[i]);
				_baseTo.getTransfers().add(t);
			}
			// Transfer engineers
			else if (_baseFrom.getAvailableEngineers() > 0 && i == _soldiers.size() + _crafts.size() + _sOffset)
			{
				_baseFrom.setEngineers(_baseFrom.getEngineers() - _qtys[i]);
				Transfer t = new Transfer(time);
				t.setEngineers(_qtys[i]);
				_baseTo.getTransfers().add(t);
			}
			// Transfer items
			else
			{
				_baseFrom.getItems().removeItem(_items[i - _soldiers.size() - _crafts.size() - _sOffset - _eOffset], _qtys[i]);
				Transfer t = new Transfer(time);
				t.setItems(_items[i - _soldiers.size() - _crafts.size() - _sOffset - _eOffset], _qtys[i]);
				_baseTo.getTransfers().add(t);
			}
		}
	}
}

/**
 * Returns to the previous screen.
 * @param action Pointer to an action.
 */
public void btnCancelClick(Action action)
{
	_game.popState();
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
 * Gets the transfer cost of the currently selected item.
 * @return Transfer cost.
 */
private int getCost()
{
	int cost = 0;
	// Personnel cost
	if (_sel < _soldiers.size() || (_sel >= _soldiers.size() + _crafts.size()  && _sel < _soldiers.size() + _crafts.size() + _sOffset + _eOffset))
	{
		cost = 5;
	}
	// Craft cost
	else if (_sel >= _soldiers.size() && _sel < _soldiers.size() + _crafts.size())
	{
		cost = 25;
	}
	// Item cost
	else
	{
		cost = 1;
	}
	return (int)Math.floor(_distance / 20.0 * cost);
}

/**
 * Gets the quantity of the currently selected item
 * on the base.
 * @return Item quantity.
 */
private int getQuantity()
{
	// Soldiers/crafts are individual
	if (_sel < _soldiers.size() + _crafts.size())
	{
		return 1;
	}
	// Scientist quantity
	else if (_baseFrom.getAvailableScientists() > 0 && _sel == _soldiers.size() + _crafts.size())
	{
		return _baseFrom.getAvailableScientists();
	}
	// Engineer quantity
	else if (_baseFrom.getAvailableEngineers() > 0 && _sel == _soldiers.size() + _crafts.size() + _sOffset)
	{
		return _baseFrom.getAvailableEngineers();
	}
	// Item quantity
	else
	{
		return _baseFrom.getItems().getItem(_items[_sel - _soldiers.size() - _crafts.size() - _sOffset - _eOffset]);
	}
}

/**
 * Increases the quantity of the selected item to sell.
 */
public void increase()
{
	if ((_sel < _soldiers.size() || (_sel >= _soldiers.size() + _crafts.size()  && _sel < _soldiers.size() + _crafts.size() + _sOffset + _eOffset)) && _pQty + 1 > _baseTo.getAvailableQuarters() - _baseTo.getUsedQuarters())
	{
		_timerInc.stop();
		_game.pushState(new PurchaseErrorState(_game, "STR_NO_FREE_ACCOMODATION"));
	}
	else if (_sel >= _soldiers.size() && _sel < _soldiers.size() + _crafts.size())
	{
		Craft craft =  _crafts[_sel - _soldiers.size()];
		if (_cQty + 1 > _baseTo.getAvailableHangars() - _baseTo.getUsedHangars())
		{
			_timerInc.stop();
			_game.pushState(new PurchaseErrorState(_game, "STR_NO_FREE_HANGARS_FOR_TRANSFER"));
		}
		else if (_pQty + craft.getNumSoldiers() > _baseTo.getAvailableQuarters() - _baseTo.getUsedQuarters())
		{
			_timerInc.stop();
			_game.pushState(new PurchaseErrorState(_game, "STR_NO_FREE_ACCOMODATION_CREW"));
		}
	}
	else if (_sel >= _soldiers.size() + _crafts.size() + _sOffset + _eOffset && _iQty + _game.getRuleset().getItem(_items[_sel - _soldiers.size() - _crafts.size() - _sOffset - _eOffset]).getSize() > _baseTo.getAvailableStores() - _baseTo.getUsedStores())
	{
		_timerInc.stop();
		_game.pushState(new PurchaseErrorState(_game, "STR_NOT_ENOUGH_STORE_SPACE"));
	}
	else if (_qtys[_sel] < getQuantity())
	{
		// Personnel count
		if (_sel < _soldiers.size() || (_sel >= _soldiers.size() + _crafts.size()  && _sel < _soldiers.size() + _crafts.size() + _sOffset + _eOffset))
		{
			_pQty++;
		}
		// Craft count
		else if (_sel >= _soldiers.size() && _sel < _soldiers.size() + _crafts.size())
		{
			Craft craft =  _crafts[_sel - _soldiers.size()];
			_cQty++;
			_pQty += craft.getNumSoldiers();
		}
		// Item count
		else
		{
			_iQty += _game.getRuleset().getItem(_items[_sel - _soldiers.size() - _crafts.size() - _sOffset - _eOffset]).getSize();
		}
		_qtys[_sel]++;
		WStringstream ss;
		ss << _qtys[_sel];
		_lstItems.getCell(_sel, 2).setText(ss.str());
		_lstItems.draw();
		_total += getCost();
	}
}

/**
 * Decreases the quantity of the selected item to sell.
 */
public void decrease()
{
	if (_qtys[_sel] > 0)
	{
		// Personnel count
		if (_sel < _soldiers.size() || (_sel >= _soldiers.size() + _crafts.size()  && _sel < _soldiers.size() + _crafts.size() + _sOffset + _eOffset))
		{
			_pQty--;
		}
		// Craft count
		else if (_sel >= _soldiers.size() && _sel < _soldiers.size() + _crafts.size())
		{
			Craft craft =  _crafts[_sel - _soldiers.size()];
			_cQty--;
			_pQty -= craft.getNumSoldiers();
		}
		// Item count
		else
		{
			_iQty -= _game.getRuleset().getItem(_items[_sel - _soldiers.size() - _crafts.size() - _sOffset - _eOffset]).getSize();
		}
		_qtys[_sel]--;
		WStringstream ss;
		ss << _qtys[_sel];
		_lstItems.getCell(_sel, 2).setText(ss.str());
		_lstItems.draw();
		_total -= getCost();
	}
}

/**
 * Gets the total cost of the current transfer.
 * @return Total cost.
 */
public int getTotal()
{
	return _total;
}

/**
 * Gets the shortest distance between the two bases.
 * @return Distance
 */
private double getDistance()
{
	double x[], y[], z[], r = 128.0;
	x = new double[3];
	y = new double[3];
	z = new double[3];
	Base base = _baseFrom;
	for (int i = 0; i < 2; ++i) {
		x[i] = - r * Math.sin(base.getLatitude()) * Math.cos(base.getLongitude());
		y[i] = - r * Math.sin(base.getLatitude()) * Math.sin(base.getLongitude());
		z[i] = r * Math.cos(base.getLatitude());
		base = _baseTo;
	}
	x[2] = x[1] - x[0];
	y[2] = y[1] - y[0];
	z[2] = z[1] - z[0];
	return Math.sqrt(x[2] * x[2] + y[2] * y[2] + z[2] * z[2]);
}

}
