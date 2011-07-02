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

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Engine.Surface;
import putzworks.openXcom.Engine.SurfaceSet;
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.Soldier;

public class SoldierInfoState extends State
{
	private Base _base;
	private int _soldier;

	private Surface _bg, _rank;
	private TextButton _btnOk, _btnPrev, _btnNext, _btnArmor;
	private Text _txtArmor, _txtRank, _txtMissions, _txtKills, _txtCraft;
	private TextEdit _edtSoldier;

	private Text _txtTimeUnits, _txtStamina, _txtHealth, _txtBravery, _txtReactions, _txtFiring, _txtThrowing, _txtStrength;
	private Text _numTimeUnits, _numStamina, _numHealth, _numBravery, _numReactions, _numFiring, _numThrowing, _numStrength;
	private Bar _barTimeUnits, _barStamina, _barHealth, _barBravery, _barReactions, _barFiring, _barThrowing, _barStrength;


/**
 * Initializes all the elements in the Soldier Info screen.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 * @param soldier ID of the selected soldier.
 */
public SoldierInfoState(Game game, Base base, int soldier)
{
	super(game);
	_base = base;
	_soldier = soldier;

	// Create objects
	_bg = new Surface(320, 200, 0, 0);
	_rank = new Surface(26, 23, 4, 4);
	_btnPrev = new TextButton(28, 14, 0, 33);
	_btnOk = new TextButton(48, 14, 30, 33);
	_btnNext = new TextButton(28, 14, 80, 33);
	_btnArmor = new TextButton(60, 14, 130, 33);
	_edtSoldier = new TextEdit(200, 16, 40, 9);
	_txtArmor = new Text(120, 9, 194, 38);
	_txtRank = new Text(130, 9, 0, 48);
	_txtMissions = new Text(100, 9, 130, 48);
	_txtKills = new Text(100, 9, 230, 48);
	_txtCraft = new Text(130, 9, 0, 56);

	_txtTimeUnits = new Text(120, 9, 6, 82);
	_numTimeUnits = new Text(18, 9, 131, 82);
	_barTimeUnits = new Bar(170, 7, 150, 82);

	_txtStamina = new Text(120, 9, 6, 94);
	_numStamina = new Text(18, 9, 131, 94);
	_barStamina = new Bar(170, 7, 150, 94);

	_txtHealth = new Text(120, 9, 6, 106);
	_numHealth = new Text(18, 9, 131, 106);
	_barHealth = new Bar(170, 7, 150, 106);

	_txtBravery = new Text(120, 9, 6, 118);
	_numBravery = new Text(18, 9, 131, 118);
	_barBravery = new Bar(170, 7, 150, 118);

	_txtReactions = new Text(120, 9, 6, 130);
	_numReactions = new Text(18, 9, 131, 130);
	_barReactions = new Bar(170, 7, 150, 130);

	_txtFiring = new Text(120, 9, 6, 142);
	_numFiring = new Text(18, 9, 131, 142);
	_barFiring = new Bar(170, 7, 150, 142);

	_txtThrowing = new Text(120, 9, 6, 154);
	_numThrowing = new Text(18, 9, 131, 154);
	_barThrowing = new Bar(170, 7, 150, 154);

	_txtStrength = new Text(120, 9, 6, 166);
	_numStrength = new Text(18, 9, 131, 166);
	_barStrength = new Bar(170, 7, 150, 166);
	
	add(_bg);
	add(_rank);
	add(_btnOk);
	add(_btnPrev);
	add(_btnNext);
	add(_btnArmor);
	add(_edtSoldier);
	add(_txtArmor);
	add(_txtRank);
	add(_txtMissions);
	add(_txtKills);
	add(_txtCraft);

	add(_txtTimeUnits);
	add(_numTimeUnits);
	add(_barTimeUnits);

	add(_txtStamina);
	add(_numStamina);
	add(_barStamina);

	add(_txtHealth);
	add(_numHealth);
	add(_barHealth);

	add(_txtBravery);
	add(_numBravery);
	add(_barBravery);

	add(_txtReactions);
	add(_numReactions);
	add(_barReactions);

	add(_txtFiring);
	add(_numFiring);
	add(_barFiring);

	add(_txtThrowing);
	add(_numThrowing);
	add(_barThrowing);

	add(_txtStrength);
	add(_numStrength);
	add(_barStrength);
	
	// Set up objects
	_game.getResourcePack().getSurface("BACK06.SCR").blit(_bg);

	_btnOk.setColor(Palette.blockOffset(15)+9);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick((ActionHandler)SoldierInfoState.btnOkClick);

	_btnPrev.setColor(Palette.blockOffset(15)+9);
	_btnPrev.setText(L"<<");
	_btnPrev.onMouseClick((ActionHandler)SoldierInfoState.btnPrevClick);

	_btnNext.setColor(Palette.blockOffset(15)+9);
	_btnNext.setText(L">>");
	_btnNext.onMouseClick((ActionHandler)SoldierInfoState.btnNextClick);

	_btnArmor.setColor(Palette.blockOffset(15)+9);
	_btnArmor.setText(_game.getLanguage().getString("STR_ARMOR"));
	_btnArmor.onMouseClick((ActionHandler)SoldierInfoState.btnArmorClick);

	_edtSoldier.setColor(Palette.blockOffset(13)+10);
	_edtSoldier.setBig();
	_edtSoldier.onKeyboardPress((ActionHandler)SoldierInfoState.edtSoldierKeyPress);

	_txtArmor.setColor(Palette.blockOffset(13));

	_txtRank.setColor(Palette.blockOffset(13)+10);
	_txtRank.setSecondaryColor(Palette.blockOffset(13));
	
	_txtMissions.setColor(Palette.blockOffset(13)+10);
	_txtMissions.setSecondaryColor(Palette.blockOffset(13));

	_txtKills.setColor(Palette.blockOffset(13)+10);
	_txtKills.setSecondaryColor(Palette.blockOffset(13));

	_txtCraft.setColor(Palette.blockOffset(13)+10);
	_txtCraft.setSecondaryColor(Palette.blockOffset(13));


	_txtTimeUnits.setColor(Palette.blockOffset(15)+1);
	_txtTimeUnits.setText(_game.getLanguage().getString("STR_TIME_UNITS"));

	_numTimeUnits.setColor(Palette.blockOffset(13));

	_barTimeUnits.setColor(Palette.blockOffset(3));
	_barTimeUnits.setScale(1.0);
	_barTimeUnits.setInvert(true);

	_txtStamina.setColor(Palette.blockOffset(15)+1);
	_txtStamina.setText(_game.getLanguage().getString("STR_STAMINA"));

	_numStamina.setColor(Palette.blockOffset(13));

	_barStamina.setColor(Palette.blockOffset(9));
	_barStamina.setScale(1.0);
	_barStamina.setInvert(true);

	_txtHealth.setColor(Palette.blockOffset(15)+1);
	_txtHealth.setText(_game.getLanguage().getString("STR_HEALTH"));

	_numHealth.setColor(Palette.blockOffset(13));

	_barHealth.setColor(Palette.blockOffset(2));
	_barHealth.setScale(1.0);
	_barHealth.setInvert(true);

	_txtBravery.setColor(Palette.blockOffset(15)+1);
	_txtBravery.setText(_game.getLanguage().getString("STR_BRAVERY"));

	_numBravery.setColor(Palette.blockOffset(13));

	_barBravery.setColor(Palette.blockOffset(4));
	_barBravery.setScale(1.0);
	_barBravery.setInvert(true);

	_txtReactions.setColor(Palette.blockOffset(15)+1);
	_txtReactions.setText(_game.getLanguage().getString("STR_REACTIONS"));

	_numReactions.setColor(Palette.blockOffset(13));

	_barReactions.setColor(Palette.blockOffset(1));
	_barReactions.setScale(1.0);
	_barReactions.setInvert(true);

	_txtFiring.setColor(Palette.blockOffset(15)+1);
	_txtFiring.setText(_game.getLanguage().getString("STR_FIRING_ACCURACY"));

	_numFiring.setColor(Palette.blockOffset(13));

	_barFiring.setColor(Palette.blockOffset(8));
	_barFiring.setScale(1.0);
	_barFiring.setInvert(true);

	_txtThrowing.setColor(Palette.blockOffset(15)+1);
	_txtThrowing.setText(_game.getLanguage().getString("STR_THROWING_ACCURACY"));

	_numThrowing.setColor(Palette.blockOffset(13));

	_barThrowing.setColor(Palette.blockOffset(10));
	_barThrowing.setScale(1.0);
	_barThrowing.setInvert(true);

	_txtStrength.setColor(Palette.blockOffset(15)+1);
	_txtStrength.setText(_game.getLanguage().getString("STR_STRENGTH"));

	_numStrength.setColor(Palette.blockOffset(13));

	_barStrength.setColor(Palette.blockOffset(5));
	_barStrength.setScale(1.0);
	_barStrength.setInvert(true);
}

/**
 * The soldier names can change
 * after going into other screens.
 */
public void init()
{
	Soldier s = _base.getSoldiers().at(_soldier);
	_edtSoldier.setText(s.getName());

	SurfaceSet texture = _game.getResourcePack().getSurfaceSet("BASEBITS.PCK");
	texture.getFrame(s.getRankSprite()).setX(0);
	texture.getFrame(s.getRankSprite()).setY(0);
	texture.getFrame(s.getRankSprite()).blit(_rank);

	WStringstream ss;
	ss << s.getTimeUnits();
	_numTimeUnits.setText(ss.str());		
	_barTimeUnits.setMax(s.getTimeUnits());
	_barTimeUnits.setValue(s.getTimeUnits());

	WStringstream ss2;
	ss2 << s.getStamina();
	_numStamina.setText(ss2.str());		
	_barStamina.setMax(s.getStamina());
	_barStamina.setValue(s.getStamina());

	WStringstream ss3;
	ss3 << s.getHealth();
	_numHealth.setText(ss3.str());		
	_barHealth.setMax(s.getHealth());
	_barHealth.setValue(s.getHealth());

	WStringstream ss4;
	ss4 << s.getBravery();
	_numBravery.setText(ss4.str());		
	_barBravery.setMax(s.getBravery());
	_barBravery.setValue(s.getBravery());

	WStringstream ss5;
	ss5 << s.getReactions();
	_numReactions.setText(ss5.str());		
	_barReactions.setMax(s.getReactions());
	_barReactions.setValue(s.getReactions());

	WStringstream ss6;
	ss6 << s.getFiringAccuracy();
	_numFiring.setText(ss6.str());		
	_barFiring.setMax(s.getFiringAccuracy());
	_barFiring.setValue(s.getFiringAccuracy());

	WStringstream ss7;
	ss7 << s.getThrowingAccuracy();
	_numThrowing.setText(ss7.str());		
	_barThrowing.setMax(s.getThrowingAccuracy());
	_barThrowing.setValue(s.getThrowingAccuracy());

	WStringstream ss8;
	ss8 << s.getStrength();
	_numStrength.setText(ss8.str());		
	_barStrength.setMax(s.getStrength());
	_barStrength.setValue(s.getStrength());

	_txtArmor.setText(_game.getLanguage().getString("STR_NONE_UC"));

	WStringstream ss9;
	ss9 << _game.getLanguage().getString("STR_RANK_") << L'\x01' << _game.getLanguage().getString(s.getRankString());
	_txtRank.setText(ss9.str());

	WStringstream ss10;
	ss10 << _game.getLanguage().getString("STR_MISSIONS") << L'\x01' << s.getMissions();
	_txtMissions.setText(ss10.str());

	WStringstream ss11;
	ss11 << _game.getLanguage().getString("STR_KILLS") << L'\x01' << s.getKills();
	_txtKills.setText(ss11.str());

	WStringstream ss12;
	ss12 << _game.getLanguage().getString("STR_CRAFT_") << L'\x01';
	if (s.getCraft() == 0)
		ss12 << _game.getLanguage().getString("STR_NONE");
	else
		ss12 << s.getCraft().getName(_game.getLanguage());
	_txtCraft.setText(ss12.str());
}

/**
 * Changes the soldier name.
 * @param action Pointer to an action.
 */
public void edtSoldierKeyPress(Action action)
{
	if (action.getDetails().key.keysym.sym == SDLK_RETURN)
	{
		_base.getSoldiers().at(_soldier).setName(_edtSoldier.getText());
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
 * Goes to the previous soldier.
 * @param action Pointer to an action.
 */
public void btnPrevClick(Action action)
{
	if (_soldier == 0)
		_soldier = _base.getSoldiers().size() - 1;
	else
		_soldier--;
	init();
}

/**
 * Goes to the next soldier.
 * @param action Pointer to an action.
 */
public void btnNextClick(Action action)
{
	_soldier++;
	if (_soldier >= _base.getSoldiers().size())
		_soldier = 0;
	init();
}

/**
 * Shows the Select Armor window.
 * @param action Pointer to an action.
 */
public void btnArmorClick(Action action)
{
	_game.pushState(new SoldierArmorState(_game, _base, _soldier));
}

}
