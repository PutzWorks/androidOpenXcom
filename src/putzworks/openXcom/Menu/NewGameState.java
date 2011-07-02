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
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Geoscape.BuildNewBaseState;
import putzworks.openXcom.Geoscape.GeoscapeState;
import putzworks.openXcom.Interface.Text;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Interface.TextButton;
import putzworks.openXcom.Interface.Window;
import putzworks.openXcom.Interface.Window.WindowPopup;
import putzworks.openXcom.Ruleset.XcomRuleset;
import putzworks.openXcom.Savegame.SavedGame.GameDifficulty;

public class NewGameState extends State
{
	TextButton _btnBeginner, _btnExperienced, _btnVeteran, _btnGenius, _btnSuperhuman;
	Window _window;
	Text _txtTitle;

/**
 * Initializes all the elements in the Difficulty window.
 * @param game Pointer to the core game.
 */
public NewGameState(Game game)
{
	super(game);
	// Create objects
	_window = new Window(this, 192, 180, 64, 10, WindowPopup.POPUP_VERTICAL);
	_btnBeginner = new TextButton(160, 18, 80, 55);
	_btnExperienced = new TextButton(160, 18, 80, 80);
	_btnVeteran = new TextButton(160, 18, 80, 105);
	_btnGenius = new TextButton(160, 18, 80, 130);
	_btnSuperhuman = new TextButton(160, 18, 80, 155);
	_txtTitle = new Text(192, 10, 64, 30);
	
	add(_window);
	add(_btnBeginner);
	add(_btnExperienced);
	add(_btnVeteran);
	add(_btnGenius);
	add(_btnSuperhuman);
	add(_txtTitle);

	// Set up objects
	_window.setColor(Palette.blockOffset(8)+8);
	_window.setBackground(_game.getResourcePack().getSurface("BACK01.SCR"));

	_btnBeginner.setColor(Palette.blockOffset(8)+8);
	_btnBeginner.setText(_game.getLanguage().getString("STR_1_BEGINNER"));
	_btnBeginner.onMouseClick((ActionHandler)NewGameState.btnBeginnerClick);

	_btnExperienced.setColor(Palette.blockOffset(8)+8);
	_btnExperienced.setText(_game.getLanguage().getString("STR_2_EXPERIENCED"));
	_btnExperienced.onMouseClick((ActionHandler)NewGameState.btnExperiencedClick);

	_btnVeteran.setColor(Palette.blockOffset(8)+8);
	_btnVeteran.setText(_game.getLanguage().getString("STR_3_VETERAN"));
	_btnVeteran.onMouseClick((ActionHandler)NewGameState.btnVeteranClick);

	_btnGenius.setColor(Palette.blockOffset(8)+8);
	_btnGenius.setText(_game.getLanguage().getString("STR_4_GENIUS"));
	_btnGenius.onMouseClick((ActionHandler)NewGameState.btnGeniusClick);

	_btnSuperhuman.setColor(Palette.blockOffset(8)+8);
	_btnSuperhuman.setText(_game.getLanguage().getString("STR_5_SUPERHUMAN"));
	_btnSuperhuman.onMouseClick((ActionHandler)NewGameState.btnSuperhumanClick);

	_txtTitle.setColor(Palette.blockOffset(8)+10);
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setSmall();
	_txtTitle.setText(_game.getLanguage().getString("STR_SELECT_DIFFICULTY_LEVEL"));
}

/**
 * Sets up a new saved game and jumps to the Geoscape.
 * @param diff Difficulty for the saved game.
 */
public void newGame(GameDifficulty diff)
{
	_game.setRuleset(new XcomRuleset());
	_game.setSavedGame(_game.getRuleset().newSave(diff));
	GeoscapeState gs = new GeoscapeState(_game);
	_game.setState(gs);
	gs.init();
	_game.pushState(new BuildNewBaseState(_game, _game.getSavedGame().getBases().back(), gs.getGlobe(), true));
}

/**
 * Creates a new game in Beginner difficulty and
 * jumps to the Geoscape screen.
 * @param action Pointer to an action.
 */
public void btnBeginnerClick(Action action)
{
	newGame(GameDifficulty.DIFF_BEGINNER);
}

/**
 * Creates a new game in Experienced difficulty and
 * jumps to the Geoscape screen.
 * @param action Pointer to an action.
 */
public void btnExperiencedClick(Action action)
{
	newGame(GameDifficulty.DIFF_EXPERIENCED);
}

/**
 * Creates a new game in Veteran difficulty and
 * jumps to the Geoscape screen.
 * @param action Pointer to an action.
 */
public void btnVeteranClick(Action action)
{
	newGame(GameDifficulty.DIFF_VETERAN);
}

/**
 * Creates a new game in Genius difficulty and
 * jumps to the Geoscape screen.
 * @param action Pointer to an action.
 */
public void btnGeniusClick(Action action)
{
	newGame(GameDifficulty.DIFF_GENIUS);
}

/**
 * Creates a new game in Superhuman difficulty and
 * jumps to the Geoscape screen.
 * @param action Pointer to an action.
 */
public void btnSuperhumanClick(Action action)
{
	newGame(GameDifficulty.DIFF_SUPERHUMAN);
}

}
