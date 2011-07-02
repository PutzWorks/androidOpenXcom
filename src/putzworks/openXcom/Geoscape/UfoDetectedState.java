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
package putzworks.openXcom.Geoscape;

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Interface.*;;
import putzworks.openXcom.Interface.Window.WindowPopup;
import putzworks.openXcom.Savegame.Ufo;

public class UfoDetectedState extends State
{
	Ufo _ufo;
	GeoscapeState _state;
	boolean _detected;

	TextButton _btnCentre, _btnCancel;
	Window _window;
	Text _txtUfo, _txtDetected;
	TextList _lstInfo;

/**
 * Initializes all the elements in the Ufo Detected window.
 * @param game Pointer to the core game.
 * @param ufo Pointer to the UFO to get info from.
 * @param state Pointer to the Geoscape.
 * @param detected Was the UFO detected?
 */
public UfoDetectedState(Game game, Ufo ufo, GeoscapeState state, boolean detected)
{
	super(game);
	_ufo = ufo;
	_state = state;
	_detected = detected;

	// Generate UFO ID
	if (_ufo.getId() == 0)
	{
		_ufo.setId(_game.getSavedGame().getUfoId());
		(_game.getSavedGame().getUfoId())++;
	}

	_screen = false;

	// Create objects
	_window = new Window(this, 208, 120, 24, 48, WindowPopup.POPUP_BOTH);
	_btnCentre = new TextButton(160, 12, 48, 128);
	_btnCancel = new TextButton(160, 12, 48, 144);
	_txtUfo = new Text(160, 16, 48, 56);
	_txtDetected = new Text(80, 8, 48, 72);
	_lstInfo = new TextList(160, 32, 48, 82);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(7)), Palette.backPos, 16);

	add(_window);
	add(_btnCentre);
	add(_btnCancel);
	add(_txtUfo);
	add(_txtDetected);
	add(_lstInfo);

	// Set up objects
	_window.setColor(Palette.blockOffset(8)+8);
	_window.setBackground(_game.getResourcePack().getSurface("BACK15.SCR"));

	_btnCentre.setColor(Palette.blockOffset(8)+8);
	_btnCentre.setText(_game.getLanguage().getString("STR_CENTER_ON_UFO_TIME_5_SECS"));
	_btnCentre.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnCentreClick(action);
		}
	});

	_btnCancel.setColor(Palette.blockOffset(8)+8);
	_btnCancel.setText(_game.getLanguage().getString("STR_CANCEL_UC"));
	_btnCancel.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnCancelClick(action);
		}
	});

	_txtDetected.setColor(Palette.blockOffset(8)+5);
	if (_detected)
	{
		_txtDetected.setText(_game.getLanguage().getString("STR_DETECTED"));
	}
	else
	{
		_txtDetected.setText("");
	}

	_txtUfo.setColor(Palette.blockOffset(8)+5);
	_txtUfo.setBig();
	_txtUfo.setText(_ufo.getName(_game.getLanguage()));
	
	_lstInfo.setColor(Palette.blockOffset(8)+5);
	_lstInfo.setColumns(2, 82, 78);
	_lstInfo.setDot(true);
	_lstInfo.addRow(2, _game.getLanguage().getString("STR_SIZE_UC"), _game.getLanguage().getString(_ufo.getRules().getSize()));
	_lstInfo.getCell(0, 1).setColor(Palette.blockOffset(8)+10);
	_lstInfo.addRow(2, _game.getLanguage().getString("STR_ALTITUDE"), _game.getLanguage().getString(_ufo.getAltitude()));
	_lstInfo.getCell(1, 1).setColor(Palette.blockOffset(8)+10);
	_lstInfo.addRow(2, _game.getLanguage().getString("STR_HEADING"), _game.getLanguage().getString(_ufo.getDirection()));
	_lstInfo.getCell(2, 1).setColor(Palette.blockOffset(8)+10);
	StringBuffer ss = new StringBuffer();
	ss.append(_ufo.getSpeed());
	_lstInfo.addRow(2, _game.getLanguage().getString("STR_SPEED"), ss.toString());
	_lstInfo.getCell(3, 1).setColor(Palette.blockOffset(8)+10);
	_lstInfo.draw();
}

/**
 * Resets the palette.
 */
public void init()
{
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(7)), Palette.backPos, 16);
}

/**
 * Centers on the UFO and returns to the previous screen.
 * @param action Pointer to an action.
 */
public void btnCentreClick(Action action)
{
	_state.timerReset();
	_state.getGlobe().center(_ufo.getLongitude(), _ufo.getLatitude());
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

}
