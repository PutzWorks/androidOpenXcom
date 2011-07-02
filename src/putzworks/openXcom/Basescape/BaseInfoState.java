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
import putzworks.openXcom.Engine.Surface;
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Savegame.Base;

public class BaseInfoState extends State
{
	private Base _base;
	private BasescapeState _state;
	
	private Surface _bg;
	private MiniBaseView _mini;
	private TextButton _btnOk, _btnTransfers, _btnStores, _btnMonthlyCosts;
	private TextEdit _edtBase;

	private Text _txtPersonnel, _txtSoldiers, _txtEngineers, _txtScientists;
	private Text _numSoldiers, _numEngineers, _numScientists;
	private Bar _barSoldiers, _barEngineers, _barScientists;

	private Text _txtSpace, _txtQuarters, _txtStores, _txtLaboratories, _txtWorkshops, _txtHangars;
	private Text _numQuarters, _numStores, _numLaboratories, _numWorkshops, _numHangars;
	private Bar _barQuarters, _barStores, _barLaboratories, _barWorkshops, _barHangars;

	private Text _txtDefence, _txtShortRange, _txtLongRange;
	private Text _numDefence, _numShortRange, _numLongRange;
	private Bar _barDefence, _barShortRange, _barLongRange;


/**
 * Initializes all the elements in the Base Info screen.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 * @param state Pointer to the Basescape state.
 */
public BaseInfoState(Game game, Base base, BasescapeState state)
{
	super(game);
	_base = base;
	_state = state;

	// Create objects
	_bg = new Surface(320, 200, 0, 0);
	_mini = new MiniBaseView(128, 16, 182, 8);
	_btnOk = new TextButton(30, 14, 10, 180);
	_btnTransfers = new TextButton(80, 14, 46, 180);
	_btnStores = new TextButton(80, 14, 132, 180);
	_btnMonthlyCosts = new TextButton(92, 14, 218, 180);
	_edtBase = new TextEdit(136, 16, 8, 8);

	_txtPersonnel = new Text(300, 9, 8, 30);
	_txtSoldiers = new Text(114, 9, 8, 41);
	_numSoldiers = new Text(40, 9, 126, 41);
	_barSoldiers = new Bar(150, 5, 166, 43);
	_txtEngineers = new Text(114, 9, 8, 51);
	_numEngineers = new Text(40, 9, 126, 51);
	_barEngineers = new Bar(150, 5, 166, 53);
	_txtScientists = new Text(114, 9, 8, 61);
	_numScientists = new Text(40, 9, 126, 61);
	_barScientists = new Bar(150, 5, 166, 63);

	_txtSpace = new Text(300, 9, 8, 72);
	_txtQuarters = new Text(114, 9, 8, 83);
	_numQuarters = new Text(40, 9, 126, 83);
	_barQuarters = new Bar(150, 5, 166, 85);
	_txtStores = new Text(114, 9, 8, 93);
	_numStores = new Text(40, 9, 126, 93);
	_barStores = new Bar(150, 5, 166, 95);
	_txtLaboratories = new Text(114, 9, 8, 103);
	_numLaboratories = new Text(40, 9, 126, 103);
	_barLaboratories = new Bar(150, 5, 166, 105);
	_txtWorkshops = new Text(114, 9, 8, 113);
	_numWorkshops = new Text(40, 9, 126, 113);
	_barWorkshops = new Bar(150, 5, 166, 115);
	_txtHangars = new Text(114, 9, 8, 123);
	_numHangars = new Text(40, 9, 126, 123);
	_barHangars = new Bar(150, 5, 166, 125);

	_txtDefence = new Text(114, 9, 8, 138);
	_numDefence = new Text(40, 9, 126, 138);
	_barDefence = new Bar(150, 5, 166, 140);
	_txtShortRange = new Text(130, 9, 8, 153);
	_numShortRange = new Text(40, 9, 126, 153);
	_barShortRange = new Bar(150, 5, 166, 155);
	_txtLongRange = new Text(130, 9, 8, 163);
	_numLongRange = new Text(40, 9, 126, 163);
	_barLongRange = new Bar(150, 5, 166, 165);

	add(_bg);
	add(_mini);
	add(_btnOk);
	add(_btnTransfers);
	add(_btnStores);
	add(_btnMonthlyCosts);
	add(_edtBase);

	add(_txtPersonnel);
	add(_txtSoldiers);
	add(_numSoldiers);
	add(_barSoldiers);
	add(_txtEngineers);
	add(_numEngineers);
	add(_barEngineers);
	add(_txtScientists);
	add(_numScientists);
	add(_barScientists);

	add(_txtSpace);
	add(_txtQuarters);
	add(_numQuarters);
	add(_barQuarters);
	add(_txtStores);
	add(_numStores);
	add(_barStores);
	add(_txtLaboratories);
	add(_numLaboratories);
	add(_barLaboratories);
	add(_txtWorkshops);
	add(_numWorkshops);
	add(_barWorkshops);
	add(_txtHangars);
	add(_numHangars);
	add(_barHangars);

	add(_txtDefence);
	add(_numDefence);
	add(_barDefence);
	add(_txtShortRange);
	add(_numShortRange);
	add(_barShortRange);
	add(_txtLongRange);
	add(_numLongRange);
	add(_barLongRange);

	// Set up objects
	_game.getResourcePack().getSurface("BACK07.SCR").blit(_bg);

	_mini.setTexture(_game.getResourcePack().getSurfaceSet("BASEBITS.PCK"));
	_mini.setBases(_game.getSavedGame().getBases());
	for (int i = 0; i < _game.getSavedGame().getBases().size(); ++i)
	{
		if (_game.getSavedGame().getBases().get(i) == _base)
		{
			_mini.setSelectedBase(i);
			break;
		}
	}
	_mini.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			miniClick(action);
		}
	});

	_btnOk.setColor(Palette.blockOffset(15)+9);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnOkClick(action);
		}
	});

	_btnTransfers.setColor(Palette.blockOffset(15)+9);
	_btnTransfers.setText(_game.getLanguage().getString("STR_TRANSFERS_UC"));
	_btnTransfers.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnTransfersClick(action);
		}
	});

	_btnStores.setColor(Palette.blockOffset(15)+9);
	_btnStores.setText(_game.getLanguage().getString("STR_STORES_UC"));
	_btnStores.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnStoresClick(action);
		}
	});

	_btnMonthlyCosts.setColor(Palette.blockOffset(15)+9);
	_btnMonthlyCosts.setText(_game.getLanguage().getString("STR_MONTHLY_COSTS"));
	_btnMonthlyCosts.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnMonthlyCostsClick(action);
		}
	});

	_edtBase.setColor(Palette.blockOffset(15)+1);
	_edtBase.setBig();
	_edtBase.onKeyboardPress(new ActionHandler() {
		public void handle(Action action) {
			edtBaseKeyPress(action);
		}
	});


	_txtPersonnel.setColor(Palette.blockOffset(15)+1);
	_txtPersonnel.setText(_game.getLanguage().getString("STR_PERSONNEL_AVAILABLE_PERSONNEL_TOTA"));

	_txtSoldiers.setColor(Palette.blockOffset(13)+5);
	_txtSoldiers.setText(_game.getLanguage().getString("STR_SOLDIERS"));

	_numSoldiers.setColor(Palette.blockOffset(13));

	_barSoldiers.setColor(Palette.blockOffset(1));
	_barSoldiers.setScale(1.0);

	_txtEngineers.setColor(Palette.blockOffset(13)+5);
	_txtEngineers.setText(_game.getLanguage().getString("STR_ENGINEERS"));

	_numEngineers.setColor(Palette.blockOffset(13));

	_barEngineers.setColor(Palette.blockOffset(1));
	_barEngineers.setScale(1.0);

	_txtScientists.setColor(Palette.blockOffset(13)+5);
	_txtScientists.setText(_game.getLanguage().getString("STR_SCIENTISTS"));

	_numScientists.setColor(Palette.blockOffset(13));

	_barScientists.setColor(Palette.blockOffset(1));
	_barScientists.setScale(1.0);


	_txtSpace.setColor(Palette.blockOffset(15)+1);
	_txtSpace.setText(_game.getLanguage().getString("STR_SPACE_USED_SPACE_AVAILABLE"));

	_txtQuarters.setColor(Palette.blockOffset(13)+5);
	_txtQuarters.setText(_game.getLanguage().getString("STR_LIVING_QUARTERS"));

	_numQuarters.setColor(Palette.blockOffset(13));

	_barQuarters.setColor(Palette.blockOffset(3));
	_barQuarters.setScale(0.5);

	_txtStores.setColor(Palette.blockOffset(13)+5);
	_txtStores.setText(_game.getLanguage().getString("STR_STORES"));

	_numStores.setColor(Palette.blockOffset(13));

	_barStores.setColor(Palette.blockOffset(3));
	_barStores.setScale(0.5);

	_txtLaboratories.setColor(Palette.blockOffset(13)+5);
	_txtLaboratories.setText(_game.getLanguage().getString("STR_LABORATORIES"));

	_numLaboratories.setColor(Palette.blockOffset(13));

	_barLaboratories.setColor(Palette.blockOffset(3));
	_barLaboratories.setScale(0.5);

	_txtWorkshops.setColor(Palette.blockOffset(13)+5);
	_txtWorkshops.setText(_game.getLanguage().getString("STR_WORK_SHOPS"));

	_numWorkshops.setColor(Palette.blockOffset(13));

	_barWorkshops.setColor(Palette.blockOffset(3));
	_barWorkshops.setScale(0.5);

	_txtHangars.setColor(Palette.blockOffset(13)+5);
	_txtHangars.setText(_game.getLanguage().getString("STR_HANGARS"));

	_numHangars.setColor(Palette.blockOffset(13));

	_barHangars.setColor(Palette.blockOffset(3));
	_barHangars.setScale(18.0);


	_txtDefence.setColor(Palette.blockOffset(13)+5);
	_txtDefence.setText(_game.getLanguage().getString("STR_DEFENCE_STRENGTH"));

	_numDefence.setColor(Palette.blockOffset(13));

	_barDefence.setColor(Palette.blockOffset(2));
	_barDefence.setScale(0.125);

	_txtShortRange.setColor(Palette.blockOffset(13)+5);
	_txtShortRange.setText(_game.getLanguage().getString("STR_SHORT_RANGE_DETECTION"));

	_numShortRange.setColor(Palette.blockOffset(13));
	if (_game.getLanguage().getName().equals("DEUTSCH"))
	{
		_numShortRange.setX(137);
	}

	_barShortRange.setColor(Palette.blockOffset(8));
	_barShortRange.setScale(25.0);

	_txtLongRange.setColor(Palette.blockOffset(13)+5);
	_txtLongRange.setText(_game.getLanguage().getString("STR_LONG_RANGE_DETECTION"));

	_numLongRange.setColor(Palette.blockOffset(13));
	if (_game.getLanguage().getName().equals("DEUTSCH"))
	{
		_numLongRange.setX(137);
	}

	_barLongRange.setColor(Palette.blockOffset(8));
	_barLongRange.setScale(25.0);
}

/**
 * The player can change the selected base.
 */
public void init()
{
	_edtBase.setText(_base.getName());

	StringBuffer ss = new StringBuffer();
	ss.append(_base.getAvailableSoldiers() + ":" + _base.getTotalSoldiers());
	_numSoldiers.setText(ss.toString());

	_barSoldiers.setMax(_base.getTotalSoldiers());
	_barSoldiers.setValue(_base.getAvailableSoldiers());

	StringBuffer ss2 = new StringBuffer();
	ss2.append(_base.getAvailableEngineers() + ":" + _base.getTotalEngineers());
	_numEngineers.setText(ss2.toString());

	_barEngineers.setMax(_base.getTotalEngineers());
	_barEngineers.setValue(_base.getAvailableEngineers());

	StringBuffer ss3 = new StringBuffer();
	ss3.append(_base.getAvailableScientists() + ":" + _base.getTotalScientists());
	_numScientists.setText(ss3.toString());

	_barScientists.setMax(_base.getTotalScientists());
	_barScientists.setValue(_base.getAvailableScientists());


	StringBuffer ss4 = new StringBuffer();
	ss4.append(_base.getUsedQuarters() + ":" + _base.getAvailableQuarters());
	_numQuarters.setText(ss4.toString());

	_barQuarters.setMax(_base.getAvailableQuarters());
	_barQuarters.setValue(_base.getUsedQuarters());

	StringBuffer ss5 = new StringBuffer();
	ss5.append(_base.getUsedStores() + ":" + _base.getAvailableStores());
	_numStores.setText(ss5.toString());

	_barStores.setMax(_base.getAvailableStores());
	_barStores.setValue(_base.getUsedStores());

	StringBuffer ss6 = new StringBuffer();
	ss6.append(_base.getUsedLaboratories() + ":" + _base.getAvailableLaboratories());
	_numLaboratories.setText(ss6.toString());

	_barLaboratories.setMax(_base.getAvailableLaboratories());
	_barLaboratories.setValue(_base.getUsedLaboratories());

	StringBuffer ss7 = new StringBuffer();
	ss7.append(_base.getUsedWorkshops() + ":" + _base.getAvailableWorkshops());
	_numWorkshops.setText(ss7.toString());

	_barWorkshops.setMax(_base.getAvailableWorkshops());
	_barWorkshops.setValue(_base.getUsedWorkshops());

	StringBuffer ss8 = new StringBuffer();
	ss8.append(_base.getUsedHangars() + ":" + _base.getAvailableHangars());
	_numHangars.setText(ss8.toString());

	_barHangars.setMax(_base.getAvailableHangars());
	_barHangars.setValue(_base.getUsedHangars());


	StringBuffer ss9 = new StringBuffer();
	ss9.append(_base.getDefenceValue());
	_numDefence.setText(ss9.toString());

	_barDefence.setMax(_base.getDefenceValue());
	_barDefence.setValue(_base.getDefenceValue());

	StringBuffer ss10 = new StringBuffer();
	ss10.append(_base.getShortRangeDetection());
	_numShortRange.setText(ss10.toString());

	_barShortRange.setMax(_base.getShortRangeDetection());
	_barShortRange.setValue(_base.getShortRangeDetection());

	StringBuffer ss11 = new StringBuffer();
	ss11.append(_base.getLongRangeDetection());
	_numLongRange.setText(ss11.toString());

	_barLongRange.setMax(_base.getLongRangeDetection());
	_barLongRange.setValue(_base.getLongRangeDetection());
}

/**
 * Changes the base name.
 * @param action Pointer to an action.
 */
public void edtBaseKeyPress(Action action)
{
	if (action.getDetails().key.keysym.sym == SDLK_RETURN)
	{
		_base.setName(_edtBase.getText());
	}
}

/**
 * Selects a new base to display.
 * @param action Pointer to an action.
 */
public void miniClick(Action action)
{
	int base = _mini.getHoveredBase();
	if (base < _game.getSavedGame().getBases().size())
	{
		_mini.setSelectedBase(base);
		_base = _game.getSavedGame().getBases().get(base);
		_state.setBase(_base);
		init();
	}
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
 * Goes to the Transfers window.
 * @param action Pointer to an action.
 */
public void btnTransfersClick(Action action)
{
	_game.pushState(new TransfersState(_game, _base));
}

/**
 * Goes to the Stores screen.
 * @param action Pointer to an action.
 */
public void btnStoresClick(Action action)
{
	_game.pushState(new StoresState(_game, _base));
}

/**
 * Goes to the Monthly Costs screen.
 * @param action Pointer to an action.
 */
public void btnMonthlyCostsClick(Action action)
{
	_game.pushState(new MonthlyCostsState(_game, _base));
}

}
