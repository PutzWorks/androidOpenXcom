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
package putzworks.openXcom.Ufopaedia;

import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Language;
import putzworks.openXcom.Ruleset.*;
import putzworks.openXcom.Savegame.SavedGame.GameDifficulty;

public class Ufopaedia
{
	/// definition of an article list
	//typedef Vector<ArticleDefinition> ArticleDefinitionList;

	/// define Ufopaedia sections, which must be consistent
	public static final String UFOPAEDIA_XCOM_CRAFT_ARMAMENT = "XCOM_CRAFT_ARMAMENT";
	public static final String UFOPAEDIA_ALIEN_LIFE_FORMS = "ALIEN_LIFE_FORMS";
	public static final String UFOPAEDIA_ALIEN_RESEARCH = "ALIEN_RESEARCH";

	/**
	 * Adds a new article to the visible list, mainly after a successful research.
	 * @param game Pointer to actual game.
	 * @param article_id Article id to release.
	 */
	public void releaseArticle(Game game, String article_id)
	{
		// TODO: get definition from Ruleset and add it to UPSaved...
	}
	
	/**
	 * Creates a new article state dependent on the given article definition.
	 * @param game Pointer to actual game.
	 * @param article Article definition to create from.
	 * @returns Article state object if created, 0 otherwise.
	 */
	protected static ArticleState createArticleState(Game game, ArticleDefinition article)
	{
		switch(article.getType())
		{
			case UFOPAEDIA_TYPE_CRAFT:
				return new ArticleStateCraft(game, (ArticleDefinitionCraft) (article));
			case UFOPAEDIA_TYPE_CRAFT_WEAPON:
				return new ArticleStateCraftWeapon(game, (ArticleDefinitionCraftWeapon) (article));
			case UFOPAEDIA_TYPE_TEXT:
				return new ArticleStateText(game, (ArticleDefinitionText) (article));
			case UFOPAEDIA_TYPE_TEXTIMAGE:
				return new ArticleStateTextImage(game,  (ArticleDefinitionTextImage) (article));
		}
		return null;
	}
	
	/**
	 * Set UPSaved index and open the new state.
	 * @param game Pointer to actual game.
	 * @param article Article definition of the article to open.
	 */
	public static void openArticle(Game game, ArticleDefinition article)
	{
		game.getSavedGame().getUfopaedia().setCurrentArticle(article);
		game.pushState(createArticleState(game, article));
	}

	/**
	 * Checks if selected article_id is available . if yes, open it.
	 * Otherwise, open start state!
	 * @param game Pointer to actual game.
	 * @param article_id Article id to find.
	 */
	public void openArticle(Game game, String article_id)
	{
		// TODO: look if article id is available!
	}
	
	/**
	 * Open Ufopaedia start state, presenting the section selection buttons.
	 * @param game Pointer to actual game.
	 */
	public static void open(Game game)
	{
		game.pushState(new UfopaediaStartState(game));
	}
	
	/**
	 * Open the next article in the list. Loops to the first.
	 * @param game Pointer to actual game.
	 */
	public static void next(Game game)
	{
		ArticleDefinition article = game.getSavedGame().getUfopaedia().goNextArticle();
		if (article != null)
		{
			game.popState();
			game.pushState(createArticleState(game, article));
		}
	}
	
	/**
	 * Open the previous article in the list. Loops to the last.
	 * @param game Pointer to actual game.
	 */
	public static void prev(Game game)
	{
		ArticleDefinition article = game.getSavedGame().getUfopaedia().goPrevArticle();
		if (article != null)
		{
			game.popState();
			game.pushState(createArticleState(game, article));
		}
	}
	
	/**
	 * Build a string from a string template. A template can be a concatenation of string ids, 
	 * f.i. "STR_SECTOID + STR_AUTOPSY". Maybe must add constant for whitepace also.
	 * @param game Pointer to actual game.
	 * @param str_template String containing the text constants
	 * @returns The string built using the text constant(s).
	 */
	public static String buildText(Game game, String str_template)
	{
		// TODO: actually parse the template string.
		return game.getLanguage().getString(str_template);
	}
	
	/**
	 * Open Ufopaedia to test it without starting a whole game.
	 * @param game Pointer to actual game.
	 */
	public void runStandalone(Game game)
	{
		// set game language
		StringBuffer ss = new StringBuffer();
		ss.append(game.getResourcePack().getFolder() + "Language/English.lng");
		Language l = new Language();
		l.loadLng(ss.toString());
		game.setLanguage(l);
		
		// init game
		game.setRuleset(new XcomRuleset());
		game.setSavedGame(game.getRuleset().newSave(GameDifficulty.DIFF_BEGINNER));
		
		// open Ufopaedia
		open(game);
	}
	
}
