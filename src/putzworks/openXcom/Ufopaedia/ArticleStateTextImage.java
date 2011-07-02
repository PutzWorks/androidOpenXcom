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

import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Interface.Text;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Ruleset.ArticleDefinitionTextImage;
import putzworks.openXcom.Ruleset.ArticleDefinition.*;

public class ArticleStateTextImage extends ArticleState
{
	protected Text _txtTitle;
	protected Text _txtInfo;
	
	public ArticleStateTextImage(Game game, ArticleDefinitionTextImage defs) 
	{
		super(game, defs.id);
		// add screen elements
		_txtTitle = new Text(140, 32, 5, 24);
		
		// Set palette
		_game.setPalette(_game.getResourcePack().getPalette("PALETTES.DAT_3").getColors());
		
		super.initLayout();
		
		// add other elements
		add(_txtTitle);
		
		// Set up objects
		_game.getResourcePack().getSurface(defs.image_id).blit(_bg);
		_btnOk.setColor(Palette.blockOffset(5)+3);
		_btnPrev.setColor(Palette.blockOffset(5)+3);
		_btnNext.setColor(Palette.blockOffset(5)+3);
		
		_txtTitle.setColor(Palette.blockOffset(15)+4);
		_txtTitle.setBig();
		_txtTitle.setAlign(TextHAlign.ALIGN_LEFT);
		_txtTitle.setWordWrap(true);
		_txtTitle.setText(Ufopaedia.buildText(_game, defs.title));

		int text_height = _txtTitle.getTextHeight();
		
		_txtInfo = new Text(150, 166 - text_height, 5, 25 + text_height);
		add(_txtInfo);
		
		_txtInfo.setColor(Palette.blockOffset(14)+15);
		_txtInfo.setAlign(TextHAlign.ALIGN_LEFT);
		_txtInfo.setWordWrap(true);
		_txtInfo.setText(Ufopaedia.buildText(_game, defs.text));
	}	
}
