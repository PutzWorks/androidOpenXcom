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
package putzworks.openXcom.Savegame;

import java.util.Vector;

import putzworks.openXcom.Ruleset.ArticleDefinition;

public class UfopaediaSaved
{
		/// article ids that are currently visible to the user.
		Vector<ArticleDefinition> _visible_articles;
		/// current selected article index (for prev/next navigation).
		int _current_index;

	/**
	 * Constructor
	 */
	public UfopaediaSaved()
	{
		_current_index = 0;

	}

	/**
	 * Insert an article to the visible list using insertion sort. Access to the list is by index.
	 * @param article Article definition of the article to insert.
	 */
	public void insertArticle(ArticleDefinition article)
	{
		// TODO: use insertion sort here!
		_visible_articles.add(article);
	}

	/**
	 * Fill an ArticleList with the currently visible ArticleIds of the given section.
	 * @param section Article section to find, e.g. "XCOM Crafts & Armaments", "Alien Lifeforms", etc.
	 * @param data Article definition list object to fill data in.
	 */
	public void getSectionList(String section, Vector<ArticleDefinition> data)
	{

		for (ArticleDefinition it: _visible_articles)
		{
			if ((it).section == section)
			{
				data.add(it);
			}
		}
	}

	/**
	 * set the current index to an article in the list found by a definition object.
	 * @param article Article definition of the selected article.
	 */
	public void setCurrentArticle(ArticleDefinition article)
	{
		_current_index = getArticleIndex(article.id);
	}

	/**
	 * move the current index to the next article.
	 * @returns Article definition of the newly selected article, 0 on error.
	 */
	public ArticleDefinition goNextArticle()
	{
		if (_current_index == _visible_articles.size() - 1)
		{
			// goto first
			_current_index = 0;
		}
		else
		{
			_current_index++;
		}

		return _visible_articles.get(_current_index);
	}

	/**
	 * move the current index to the previous article.
	 * @returns Article definition of the newly selected article, 0 on error.
	 */
	public ArticleDefinition goPrevArticle()
	{
		if (_current_index == 0)
		{
			// goto last
			_current_index = _visible_articles.size() - 1;
		}
		else
		{
			_current_index--;
		}

		return _visible_articles.get(_current_index);
	}


	/**
	 * Checks, if article id is in the visible list.
	 * @param article_id Article id to find.
	 * @returns true, if article id was found.
	 */
	public boolean isArticleAvailable(String article_id)
	{
		return (-1 != getArticleIndex(article_id));
	}

	/**
	 * Gets the index of the selected article_id in the visible list.
	 * If the id is not found, returns -1.
	 * @param article_id Article id to find.
	 * @returns Index of the given article id in the internal list, -1 if not found.
	 */
	public int getArticleIndex(String article_id)
	{
		int index = 0;

		for (ArticleDefinition it: _visible_articles)
		{
			if ((it).id == article_id)
			{
				return index;
			}
			index++;
		}
		return -1;
	}

}
