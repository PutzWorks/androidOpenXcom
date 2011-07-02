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

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Engine.Surface;
import putzworks.openXcom.Interface.TextButton;

public class ArticleState extends State
{
		protected String _id;
		protected Surface _bg;
		protected TextButton _btnOk;
		protected TextButton _btnPrev;
		protected TextButton _btnNext;

	/**
	 * Constructor
	 * @param game Pointer to current game.
	 * @param article_id The article id of this article state instance.
	 */
	protected ArticleState(Game game, String article_id)

	{
		super(game);
		_id = article_id;
		// init background and navigation eleemnts
		_bg = new Surface(320, 200, 0, 0);
		_btnOk = new TextButton(30, 14, 5, 5);
		_btnPrev = new TextButton(30, 14, 40, 5);
		_btnNext = new TextButton(30, 14, 75, 5);
	}

	/**
	 * Set captions and click handlers for the common control elements.
	 */
	protected void initLayout()
	{
		add(_bg);
		add(_btnOk);
		add(_btnPrev);
		add(_btnNext);
		
		_btnOk.setText(_game.getLanguage().getString("STR_OK"));
		_btnOk.onMouseClick((ActionHandler)ArticleState.btnOkClick);
		_btnPrev.setText(L"<<");
		_btnPrev.onMouseClick((ActionHandler)ArticleState.btnPrevClick);
		_btnNext.setText(L">>");
		_btnNext.onMouseClick((ActionHandler)ArticleState.btnNextClick);
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
	 * Shows the previous available article.
	 * @param action Pointer to an action.
	 */
	protected void btnPrevClick(Action action)
	{
		Ufopaedia.prev(_game);
	}
	
	/**
	 * Shows the next available article. Loops to the first.
	 * @param action Pointer to an action.
	 */
	protected void btnNextClick(Action action)
	{
		Ufopaedia.next(_game);
	}

	public String getId() 
	{ 
		return _id; 
	}
	
}
