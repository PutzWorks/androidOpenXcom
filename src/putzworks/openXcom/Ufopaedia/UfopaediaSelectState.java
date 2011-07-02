/*
 * Copyright 2011 OpenXcom Developers.
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
package putzworks.openXcom.Ufopaedia;

import java.util.Vector;

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Interface.Text;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Interface.TextButton;
import putzworks.openXcom.Interface.TextList;
import putzworks.openXcom.Interface.Window;
import putzworks.openXcom.Interface.Window.WindowPopup;
import putzworks.openXcom.Ruleset.ArticleDefinition;

public class UfopaediaSelectState extends State
{
	protected String _section;
	protected Window _window;
	protected Text _txtTitle;
	protected TextButton _btnOk;
	protected TextList _lstSelection;
	protected Vector<ArticleDefinition> _article_list;
		

	public UfopaediaSelectState(Game game, String section)
	{
		super(game);
		_section = section;
		_screen = false;

		// set background window
		_window = new Window(this, 256, 180, 32, 10, WindowPopup.POPUP_NONE);

		// set title
		_txtTitle = new Text(224, 16, 48, 26);

		// set buttons
		_btnOk = new TextButton(224, 16, 48, 166);
		_lstSelection = new TextList(224, 104, 40, 50);

		// Set palette
		_game.setPalette(_game.getResourcePack().getPalette("PALETTES.DAT_0").getColors());

		add(_window);
		add(_txtTitle);
		add(_btnOk);
		add(_lstSelection);

		_window.setColor(Palette.blockOffset(15)+2);
		_window.setBackground(_game.getResourcePack().getSurface("BACK01.SCR"));

		_txtTitle.setColor(Palette.blockOffset(8)+10);
		_txtTitle.setBig();
		_txtTitle.setAlign(TextHAlign.ALIGN_CENTER);
		_txtTitle.setText(_game.getLanguage().getString("STR_SELECT_ITEM"));

		_btnOk.setColor(Palette.blockOffset(15)+2);
		_btnOk.setText(_game.getLanguage().getString("STR_OK"));
		_btnOk.onMouseClick((ActionHandler)UfopaediaSelectState.btnOkClick);

		_lstSelection.setColor(Palette.blockOffset(8)+5);
		_lstSelection.setArrowColor(Palette.blockOffset(15)+2);
		_lstSelection.setColumns(1, 206);
		_lstSelection.setSelectable(true);
		_lstSelection.setBackground(_window);
		_lstSelection.setMargin(18);
		_lstSelection.setAlign(TextHAlign.ALIGN_CENTER);
		_lstSelection.onMouseClick((ActionHandler)UfopaediaSelectState.lstSelectionClick);

		loadSelectionList();
	}

	/**
	 * Returns to the previous screen.
	 * @param action Pointer to an action.
	 */
	protected void btnOkClick(Action action)
	{
		_game.popState();
	}

	/**
	 *
	 * @param action Pointer to an action.
	 */
	protected void lstSelectionClick(Action action)
	{
		Ufopaedia.openArticle(_game, _article_list.get(_lstSelection.getSelectedRow()));
	}

	protected void loadSelectionList()
	{

		_article_list.clear();
		_game.getSavedGame().getUfopaedia().getSectionList(_section, _article_list);
		for(ArticleDefinition it:  _article_list)
		{
			_lstSelection.addRow(1, Ufopaedia.buildText(_game, (it).title).c_str());
		}
	}

	public void init()
	{
		// Set palette
		_game.setPalette(_game.getResourcePack().getPalette("PALETTES.DAT_0").getColors());

		super.init();
	}

}
