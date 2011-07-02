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
import putzworks.openXcom.Geoscape.BuildNewBaseState;
import putzworks.openXcom.Geoscape.Globe;
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.BaseFacility;
import putzworks.openXcom.Savegame.Region;

public class BasescapeState extends State
{
	private BaseView _view;
	private MiniBaseView _mini;
	private Text _txtFacility, _txtBase, _txtLocation, _txtFunds;
	private TextButton _btnNewBase, _btnBaseInfo, _btnSoldiers, _btnCrafts, _btnFacilities, _btnResearch, _btnManufacture, _btnTransfer, _btnPurchase, _btnSell, _btnGeoscape;
	private Base _base;
	private Globe _globe;

/**
 * Initializes all the elements in the Basescape screen.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 * @param globe Pointer to the Geoscape globe.
 */
public BasescapeState(Game game, Base base, Globe globe)
{
	super(game);
	_base = base;
	_globe = globe;
	// Create objects
	_view = new BaseView(192, 192, 0, 8);
	_mini = new MiniBaseView(128, 16, 192, 41);
	_txtFacility = new Text(192, 9, 0, 0);
	_txtBase = new Text(127, 17, 193, 0);
	_txtLocation = new Text(126, 9, 194, 16);
	_txtFunds = new Text(126, 9, 194, 24);
	_btnNewBase = new TextButton(128, 12, 192, 58);
	_btnBaseInfo = new TextButton(128, 12, 192, 71);
	_btnSoldiers = new TextButton(128, 12, 192, 84);
	_btnCrafts = new TextButton(128, 12, 192, 97);
	_btnFacilities = new TextButton(128, 12, 192, 110);
	_btnResearch = new TextButton(128, 12, 192, 123);
	_btnManufacture = new TextButton(128, 12, 192, 136);
	_btnTransfer = new TextButton(128, 12, 192, 149);
	_btnPurchase = new TextButton(128, 12, 192, 162);
	_btnSell = new TextButton(128, 12, 192, 175);
	_btnGeoscape = new TextButton(128, 12, 192, 188);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("PALETTES.DAT_1").getColors());

	add(_view);
	add(_mini);
	add(_txtFacility);
	add(_txtBase);
	add(_txtLocation);
	add(_txtFunds);
	add(_btnNewBase);
	add(_btnBaseInfo);
	add(_btnSoldiers);
	add(_btnCrafts);
	add(_btnFacilities);
	add(_btnResearch);
	add(_btnManufacture);
	add(_btnTransfer);
	add(_btnPurchase);
	add(_btnSell);
	add(_btnGeoscape);

	// Set up objects
	_view.setFonts(_game.getResourcePack().getFont("BIGLETS.DAT"), _game.getResourcePack().getFont("SMALLSET.DAT"));
	_view.setTexture(_game.getResourcePack().getSurfaceSet("BASEBITS.PCK"));
	_view.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			viewClick(action);
		}
	});
	_view.onMouseOver(new ActionHandler() {
		public void handle(Action action) {
			viewMouseOver(action);
		}
	});
	_view.onMouseOut(new ActionHandler() {
		public void handle(Action action) {
			viewMouseOut(action);
		}
	});

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

	_txtFacility.setColor(Palette.blockOffset(13)+10);

	_txtBase.setColor(Palette.blockOffset(15)+1);
	_txtBase.setBig();

	_txtLocation.setColor(Palette.blockOffset(15)+6);

	_txtFunds.setColor(Palette.blockOffset(13)+10);

	_btnNewBase.setColor(Palette.blockOffset(13)+8);
	_btnNewBase.setText(_game.getLanguage().getString("STR_BUILD_NEW_BASE_UC"));
	_btnNewBase.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnNewBaseClick(action);
		}
	});

	_btnBaseInfo.setColor(Palette.blockOffset(13)+8);
	_btnBaseInfo.setText(_game.getLanguage().getString("STR_BASE_INFORMATION"));
	_btnBaseInfo.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnBaseInfoClick(action);
		}
	});

	_btnSoldiers.setColor(Palette.blockOffset(13)+8);
	_btnSoldiers.setText(_game.getLanguage().getString("STR_SOLDIERS_UC"));
	_btnSoldiers.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnSoldiersClick(action);
		}
	});

	_btnCrafts.setColor(Palette.blockOffset(13)+8);
	_btnCrafts.setText(_game.getLanguage().getString("STR_EQUIP_CRAFT"));
	_btnCrafts.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnCraftsClick(action);
		}
	});

	_btnFacilities.setColor(Palette.blockOffset(13)+8);
	_btnFacilities.setText(_game.getLanguage().getString("STR_BUILD_FACILITIES"));
	_btnFacilities.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnFacilitiesClick(action);
		}
	});

	_btnResearch.setColor(Palette.blockOffset(13)+8);
	_btnResearch.setText(_game.getLanguage().getString("STR_NOT_AVAILABLE"));
	_btnResearch.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnResearchClick(action);
		}
	});

	_btnManufacture.setColor(Palette.blockOffset(13)+8);
	_btnManufacture.setText(_game.getLanguage().getString("STR_NOT_AVAILABLE"));
	_btnManufacture.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnManufactureClick(action);
		}
	});

	_btnTransfer.setColor(Palette.blockOffset(13)+8);
	_btnTransfer.setText(_game.getLanguage().getString("STR_TRANSFER_UC"));
	_btnTransfer.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnTransferClick(action);
		}
	});

	_btnPurchase.setColor(Palette.blockOffset(13)+8);
	_btnPurchase.setText(_game.getLanguage().getString("STR_PURCHASE_RECRUIT"));
	_btnPurchase.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnPurchaseClick(action);
		}
	});

	_btnSell.setColor(Palette.blockOffset(13)+8);
	_btnSell.setText(_game.getLanguage().getString("STR_SELL_SACK_UC"));
	_btnSell.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnSellClick(action);
		}
	});

	_btnGeoscape.setColor(Palette.blockOffset(13)+8);
	_btnGeoscape.setText(_game.getLanguage().getString("STR_GEOSCAPE"));
	_btnGeoscape.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnGeoscapeClick(action);
		}
	});
}

/**
 *
 */
public void clearBasescapeState()
{
	// Clean up any temporary bases
	boolean exists = false;
	for (Base i : _game.getSavedGame().getBases()) //had a && !Exists in there...
	{
		if (i == _base)
		{
			exists = true;
		}
	}
	if (!exists)
	{
		_base = null;
	}
}

/**
 * The player can change the selected base
 * or change info on other screens.
 */
public void init()
{
	if (_game.getSavedGame().getBases().size() > 0)
	{
		boolean exists = false;
		for (Base i: _game.getSavedGame().getBases()) //had a && !Exists in there...
		{
			if (i == _base)
			{
				exists = true;
			}
		}
		// If base was removed, select first one
		if (!exists)
		{
			_base = _game.getSavedGame().getBases().front();
			_mini.setSelectedBase(0);
		}
	}
	else
	{
		// Use a blank base for special case when player has no bases
		_base = new Base(_game.getRuleset());
	}

	_view.setBase(_base);
	_mini.draw();
	_txtBase.setText(_base.getName());

	// Get area
	for (Region i: _game.getSavedGame().getRegions())
	{
		if ((i).getRules().insideRegion(_base.getLongitude(), _base.getLatitude()))
		{
			_txtLocation.setText(_game.getLanguage().getString((i).getRules().getType()));
			break;
		}
	}

	String s = _game.getLanguage().getString("STR_FUNDS");
	s += Text.formatFunding(_game.getSavedGame().getFunds());
	_txtFunds.setText(s);

	if (_game.getSavedGame().getBases().size() == 8)
	{
		_btnNewBase.setVisible(false);
	}
}

/**
 * Changes the base currently displayed on screen.
 * @param base Pointer to new base to display.
 */
public void setBase(Base base)
{
	_base = base;
	for (int i = 0; i < _game.getSavedGame().getBases().size(); ++i)
	{
		if (_game.getSavedGame().getBases().get(i) == _base)
		{
			_mini.setSelectedBase(i);
			break;
		}
	}
	init();
}

/**
 * Goes to the Build New Base screen.
 * @param action Pointer to an action.
 */
public void btnNewBaseClick(Action action)
{
	Base base = new Base(_game.getRuleset());
	_game.popState();
	_game.pushState(new BuildNewBaseState(_game, base, _globe, false));
}

/**
 * Goes to the Base Info screen.
 * @param action Pointer to an action.
 */
public void btnBaseInfoClick(Action action)
{
	_game.pushState(new BaseInfoState(_game, _base, this));
}

/**
 * Goes to the Soldiers screen.
 * @param action Pointer to an action.
 */
public void btnSoldiersClick(Action action)
{
	_game.pushState(new SoldiersState(_game, _base));
}

/**
 * Goes to the Crafts screen.
 * @param action Pointer to an action.
 */
public void btnCraftsClick(Action action)
{
	_game.pushState(new CraftsState(_game, _base));
}

/**
 * Opens the Build Facilities window.
 * @param action Pointer to an action.
 */
public void btnFacilitiesClick(Action action)
{
	_game.pushState(new BuildFacilitiesState(_game, _base, this));
}

/**
 * Goes to the Research screen.
 * @param action Pointer to an action.
 */
public void btnResearchClick(Action action)
{
	//_game.pushState(new ResearchState(_game, _base));
}

/**
 * Goes to the Manufacture screen.
 * @param action Pointer to an action.
 */
public void btnManufactureClick(Action action)
{
	//_game.pushState(new ManufactureState(_game, _base));
}

/**
 * Goes to the Purchase screen.
 * @param action Pointer to an action.
 */
public void btnPurchaseClick(Action action)
{
	_game.pushState(new PurchaseState(_game, _base));
}

/**
 * Goes to the Sell screen.
 * @param action Pointer to an action.
 */
public void btnSellClick(Action action)
{
	_game.pushState(new SellState(_game, _base));
}

/**
 * Goes to the Select Destination Base window.
 * @param action Pointer to an action.
 */
public void btnTransferClick(Action action)
{
	_game.pushState(new TransferBaseState(_game, _base));
}

/**
 * Returns to the previous screen.
 * @param action Pointer to an action.
 */
public void btnGeoscapeClick(Action action)
{
	_game.popState();
}

/**
 * Processes clicking on facilities.
 * @param action Pointer to an action.
 */
public void viewClick(Action action)
{
	BaseFacility fac = _view.getSelectedFacility();
	if (fac != null)
	{
		// Pre-calculate values to ensure base stays connected
		int x = -1, y = -1, squares = 0;
		for (BaseFacility i: _base.getFacilities())
		{
			if ((i).getRules().getLift())
			{
				x = (i).getX();
				y = (i).getY();
			}
			squares += (i).getRules().getSize() * (i).getRules().getSize();
		}
		squares -= fac.getRules().getSize() * fac.getRules().getSize();

		// Is facility in use?
		if (fac.inUse())
		{
			_game.pushState(new BasescapeErrorState(_game, "STR_FACILITY_IN_USE"));
		}
		// Would base become disconnected? (ocuppied squares connected to Access Lift < total squares occupied by base)
		else if (_view.countConnected(x, y, 0, fac) < squares)
		{
			_game.pushState(new BasescapeErrorState(_game, "STR_CANNOT_DISMANTLE_FACILITY"));
		}
		else
		{
			_game.pushState(new DismantleFacilityState(_game, _base, fac));
		}
	}
}

/**
 * Displays the name of the facility the mouse is over.
 * @param action Pointer to an action.
 */
public void viewMouseOver(Action action)
{
	BaseFacility f = _view.getSelectedFacility();
	if (f == null)
		_txtFacility.setText("");
	else
		_txtFacility.setText(_game.getLanguage().getString(f.getRules().getType()));
}

/**
 * Clears the facility name.
 * @param action Pointer to an action.
 */
public void viewMouseOut(Action action)
{
	_txtFacility.setText("");
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
		if (base < _game.getSavedGame().getBases().size()){
			_base = _game.getSavedGame().getBases().get(base);
		}
		init();
	}
}

}
