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
import putzworks.openXcom.Interface.*;
import putzworks.openXcom.Savegame.Base;

public class ResearchState extends State
{
	private Base _base;
	private TextButton _btnNew, _btnOk;
	private Window _window;
	private Text _txtTitle, _txtAvailable, _txtAllocated, _txtSpace, _txtProject, _txtScientists, _txtProgress;
	private TextList _lstResearch;

/**
 * Initializes all the elements in the Research screen.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 */
public ResearchState(Game game, Base base)
{
	super(game);
	_base = base;
	// Create objects
	_window = new Window(this, 320, 200, 0, 0);
	_btnNew = new TextButton(148, 16, 8, 176);
	_btnOk = new TextButton(148, 16, 164, 176);
	_txtTitle = new Text(310, 16, 5, 8);
	_txtAvailable = new Text(150, 9, 8, 24);
	_txtAllocated = new Text(150, 9, 160, 24);
	_txtSpace = new Text(300, 9, 8, 34);
	_txtProject = new Text(106, 9, 8, 44);
	_txtScientists = new Text(144, 9, 115, 44);
	_txtProgress = new Text(64, 9, 250, 44);
	_lstResearch = new TextList(288, 120, 8, 54);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(1)), Palette.backPos, 16);

	add(_window);
	add(_btnNew);
	add(_btnOk);
	add(_txtTitle);
	add(_txtAvailable);
	add(_txtAllocated);
	add(_txtSpace);
	add(_txtProject);
	add(_txtScientists);
	add(_txtProgress);
	add(_lstResearch);

	// Set up objects
	_window.setColor(Palette.blockOffset(13)+13);
	_window.setBackground(_game.getResourcePack().getSurface("BACK05.SCR"));
	
	_btnNew.setColor(Palette.blockOffset(15)+9);
	_btnNew.setText(_game.getLanguage().getString("STR_NEW_PROJECT"));

	_btnOk.setColor(Palette.blockOffset(15)+9);
	_btnOk.setText(_game.getLanguage().getString("STR_OK"));
	_btnOk.onMouseClick((ActionHandler)ResearchState.btnOkClick);

	_txtTitle.setColor(Palette.blockOffset(13)+10);
	_txtTitle.setBig();
	_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
	_txtTitle.setText(_game.getLanguage().getString("STR_CURRENT_RESEARCH"));

	_txtAvailable.setColor(Palette.blockOffset(13)+10);
	_txtAvailable.setSecondaryColor(Palette.blockOffset(13));
	WStringstream ss;
	ss << _game.getLanguage().getString("STR_SCIENTISTS_AVAILABLE") << L'\x01' << _base.getAvailableScientists();
	_txtAvailable.setText(ss.str());

	_txtAllocated.setColor(Palette.blockOffset(13)+10);
	_txtAllocated.setSecondaryColor(Palette.blockOffset(13));
	WStringstream ss2;
	ss2 << _game.getLanguage().getString("STR_SCIENTISTS_ALLOCATED") << L'\x01' << (_base.getTotalScientists() - _base.getAvailableScientists());
	_txtAllocated.setText(ss2.str());

	_txtSpace.setColor(Palette.blockOffset(13)+10);
	_txtSpace.setSecondaryColor(Palette.blockOffset(13));
	_txtSpace.setText(_game.getLanguage().getString("STR_LABORATORY_SPACE_AVAILABLE"));

	_txtProject.setColor(Palette.blockOffset(13)+10);
	_txtProject.setText(_game.getLanguage().getString("STR_RESEARCH_PROJECT"));

	_txtScientists.setColor(Palette.blockOffset(13)+10);
	_txtScientists.setText(_game.getLanguage().getString("STR_SCIENTISTS_ALLOCATED_UC"));

	_txtProgress.setColor(Palette.blockOffset(13)+10);
	_txtProgress.setText(_game.getLanguage().getString("STR_PROGRESS"));

	_lstResearch.setColor(Palette.blockOffset(15)+6);
	_lstResearch.setArrowColor(Palette.blockOffset(13)+13);
	_lstResearch.setColumns(3, 158, 82, 46);
	_lstResearch.setSelectable(true);
	_lstResearch.setBackground(_window);
	_lstResearch.setMargin(2);
	_lstResearch.addRow(3, "Laser Weapons", "30", "Good");
}

/**
 * Returns to the previous screen.
 * @param action Pointer to an action.
 */
public void btnOkClick(Action action)
{
	_game.popState();
}

}
