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
import putzworks.openXcom.Geoscape.Globe;
import putzworks.openXcom.Interface.Text;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.BaseFacility;

public class PlaceLiftState extends State
{
	private Base _base;
	private Globe _globe;
	private BaseView _view;
	private Text _txtTitle;

/**
 * Initializes all the elements in the Place Lift screen.
 * @param game Pointer to the core game.
 * @param base Pointer to the base to get info from.
 * @param globe Pointer to the Geoscape globe.
 */
public PlaceLiftState(Game game, Base base, Globe globe)
{
	super(game);
	_base = base;
	_globe = globe;
	// Create objects
	_view = new BaseView(192, 192, 0, 8);
	_txtTitle = new Text(180, 9, 10, 0);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("PALETTES.DAT_1").getColors());

	add(_view);
	add(_txtTitle);

	// Set up objects
	_view.setFonts(_game.getResourcePack().getFont("BIGLETS.DAT"), _game.getResourcePack().getFont("SMALLSET.DAT"));
	_view.setTexture(_game.getResourcePack().getSurfaceSet("BASEBITS.PCK"));
	_view.setBase(_base);
	_view.setSelectable(_game.getRuleset().getBaseFacility("STR_ACCESS_LIFT").getSize());
	_view.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			viewClick(action);
		}
	});

	_txtTitle.setColor(Palette.blockOffset(13)+10);
	_txtTitle.setText(_game.getLanguage().getString("STR_SELECT_POSITION_FOR_ACCESS_LIFT"));
}

/**
 * Processes clicking on facilities.
 * @param action Pointer to an action.
 */
public void viewClick(Action action)
{
	BaseFacility fac = new BaseFacility(_game.getRuleset().getBaseFacility("STR_ACCESS_LIFT"), _base, _view.getGridX(), _view.getGridY());
	fac.setBuildTime(0);
	_base.getFacilities().add(fac);
	_game.popState();
	_game.pushState(new BasescapeState(_game, _base, _globe));
}

}
