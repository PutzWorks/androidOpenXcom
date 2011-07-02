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
import putzworks.openXcom.Interface.TextList;
import putzworks.openXcom.Ruleset.ArticleDefinitionCraftWeapon;
import putzworks.openXcom.Ruleset.ArticleDefinition.*;


public class ArticleStateCraftWeapon extends ArticleState
{
		protected Text _txtTitle;
		protected TextList _lstInfo;
	
	public ArticleStateCraftWeapon(Game game, ArticleDefinitionCraftWeapon defs) 
	{
		super(game, defs.id);
		// add screen elements
		_txtTitle = new Text(140, 32, 5, 24);
		
		// Set palette
		_game.setPalette(_game.getResourcePack().getPalette("PALETTES.DAT_4").getColors());
		
		super.initLayout();
		
		// add other elements
		add(_txtTitle);
		
		// Set up objects
		_game.getResourcePack().getSurface(defs.image_id).blit(_bg);
		_btnOk.setColor(Palette.blockOffset(1)+2);
		_btnPrev.setColor(Palette.blockOffset(1)+2);
		_btnNext.setColor(Palette.blockOffset(1)+2);
		
		_txtTitle.setColor(Palette.blockOffset(14)+15);
		_txtTitle.setBig();
		_txtTitle.setAlign(TextHAlign.ALIGN_LEFT);
		_txtTitle.setWordWrap(true);
		_txtTitle.setText(Ufopaedia.buildText(_game, defs.title));
		
		_lstInfo = new TextList(210, 111, 5, 80);
		add(_lstInfo);
		
		WStringstream ss;
		_lstInfo.setColor(Palette.blockOffset(14)+15);
		_lstInfo.setColumns(2, 134, 70);
		_lstInfo.setDot(true);
		_lstInfo.setBig();
		
		ss.str(L"");ss.clear();
		ss << defs.weapon.getDamage();
		
		_lstInfo.addRow(2, _game.getLanguage().getString("STR_DAMAGE").c_str(), ss.str().c_str());
		_lstInfo.getCell(0, 1).setColor(Palette.blockOffset(15)+4);
		
		ss.str(L"");ss.clear();
		ss << defs.weapon.getRange() << _game.getLanguage().getString("STR_KM").c_str();
		_lstInfo.addRow(2, _game.getLanguage().getString("STR_RANGE").c_str(), ss.str().c_str());
		_lstInfo.getCell(1, 1).setColor(Palette.blockOffset(15)+4);
		
		ss.str(L"");ss.clear();
		ss << defs.weapon.getAccuracy() << "%";
		_lstInfo.addRow(2, _game.getLanguage().getString("STR_ACCURACY").c_str(), ss.str().c_str());
		_lstInfo.getCell(2, 1).setColor(Palette.blockOffset(15)+4);
		
		ss.str(L"");ss.clear();
		ss << defs.weapon.getStandardReload() << _game.getLanguage().getString("STR_S").c_str();
		_lstInfo.addRow(2, _game.getLanguage().getString("STR_RE_LOAD_TIME").c_str(), ss.str().c_str());
		_lstInfo.getCell(3, 1).setColor(Palette.blockOffset(15)+4);
		
		_lstInfo.draw();
	}	
}
