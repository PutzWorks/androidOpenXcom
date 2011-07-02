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
package putzworks.openXcom.Menu;

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Language;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Geoscape.GeoscapeErrorState;
import putzworks.openXcom.Interface.Text;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Interface.TextButton;
import putzworks.openXcom.Interface.TextEdit;
import putzworks.openXcom.Interface.TextList;
import putzworks.openXcom.Interface.Window;
import putzworks.openXcom.Interface.Window.WindowPopup;
import putzworks.openXcom.Savegame.SavedGame;

public class SaveGameState extends State
{
	private TextButton _btnCancel;
	private Window _window;
	private Text _txtTitle, _txtName, _txtTime, _txtDate;
	private TextList _lstSaves;
	private TextEdit _edtSave;
	private String _selected;

/**
 * Initializes all the elements in the Save Game screen.
 * @param game Pointer to the core game.
 */
public SaveGameState(Game game)
{
	super(game);
	_selected = "";
	// Create objects
	_window = new Window(this, 320, 200, 0, 0, WindowPopup.POPUP_BOTH);
	_btnCancel = new TextButton(80, 16, 120, 172);
	_txtTitle = new Text(310, 16, 5, 8);
	_txtName = new Text(150, 9, 16, 24);
	_txtTime = new Text(30, 9, 184, 24);
	_txtDate = new Text(30, 9, 214, 24);
	_lstSaves = new TextList(288, 128, 8, 32);
	_edtSave = new TextEdit(168, 9, 0, 0);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(6)), Palette.backPos, 16);

	add(_window);
	add(_btnCancel);
	add(_txtTitle);
	add(_txtName);
	add(_txtTime);
	add(_txtDate);
	add(_lstSaves);
	add(_edtSave);

	// Set up objects
	_window.setColor(Palette.blockOffset(8)+8);
	_window.setBackground(game.getResourcePack().getSurface("BACK01.SCR"));

	_btnCancel.setColor(Palette.blockOffset(8)+8);
	_btnCancel.setText(_game.getLanguage().getString("STR_CANCEL_UC"));
	_btnCancel.onMouseClick((ActionHandler)SaveGameState.btnCancelClick);

	_txtTitle.setColor(Palette.blockOffset(15)-1);
	_txtTitle.setBig();
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setText(_game.getLanguage().getString("STR_SELECT_SAVE_POSITION"));

	_txtName.setColor(Palette.blockOffset(15)-1);
	_txtName.setText(_game.getLanguage().getString("STR_NAME"));

	_txtTime.setColor(Palette.blockOffset(15)-1);
	_txtTime.setText(_game.getLanguage().getString("STR_TIME"));

	_txtDate.setColor(Palette.blockOffset(15)-1);
	_txtDate.setText(_game.getLanguage().getString("STR_DATE"));
	
	_lstSaves.setColor(Palette.blockOffset(8)+10);
	_lstSaves.setArrowColor(Palette.blockOffset(8)+8);
	_lstSaves.setColumns(5, 168, 30, 30, 30, 30);
	_lstSaves.setSelectable(true);
	_lstSaves.setBackground(_window);
	_lstSaves.setMargin(8);
	_lstSaves.onMouseClick((ActionHandler)SaveGameState.lstSavesClick);
	_lstSaves.addRow(1, L"<NEW SAVED GAME>");
	SavedGame.getList(_lstSaves, _game.getLanguage());

	_edtSave.setColor(Palette.blockOffset(8)+10);
	_edtSave.setVisible(false);
	_edtSave.onKeyboardPress((ActionHandler)SaveGameState.edtSaveKeyPress);
}

/**
 * Resets the palette since it's bound to change on other screens.
 */
public void init()
{
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(6)), Palette.backPos, 16);
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
 * Names the selected save.
 * @param action Pointer to an action.
 */
public void lstSavesClick(Action action)
{
	Text t = _lstSaves.getCell(_lstSaves.getSelectedRow(), 0);
	_selected = Language.wstrToUtf8(t.getText());
	t.setText(L"");
	_lstSaves.draw();
	if (_lstSaves.getSelectedRow() == 0)
	{
		_edtSave.setText(L"");
		_selected = "";
	}
	else
	{
		_edtSave.setText(Language.utf8ToWstr(_selected));
	}
	_edtSave.setX(_lstSaves.getX() + t.getX());
	_edtSave.setY(_lstSaves.getY() + t.getY());
	_edtSave.setVisible(true);
	_edtSave.focus();
}

/**
 * Saves the selected save.
 * @param action Pointer to an action.
 */
public void edtSaveKeyPress(Action action)
{
	if (action.getDetails().key.keysym.sym == SDLK_RETURN)
	{
		try
		{
			if (_selected != "")
			{
				String oldName = USER_DIR + _selected + ".sav";
				String newName = USER_DIR + Language.wstrToUtf8(_edtSave.getText()) + ".sav";
				if (rename(oldName.c_str(), newName.c_str()) != 0)
				{
					throw Exception("Failed to overwrite save");
				}
			}
			_game.getSavedGame().save(Language.wstrToUtf8(_edtSave.getText()));
		}
		catch (Exception e)
		{
			std.cerr << "ERROR: " << e.what() << std.endl;
			_game.pushState(new GeoscapeErrorState(_game, "STR_SAVE_UNSUCCESSFUL"));
		}
		catch (YAML.Exception e)
		{
			std.cerr << "ERROR: " << e.what() << std.endl;
			_game.pushState(new GeoscapeErrorState(_game, "STR_SAVE_UNSUCCESSFUL"));
		}
		_game.popState();
	}
}

}
