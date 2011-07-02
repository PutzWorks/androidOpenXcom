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
package putzworks.openXcom.Geoscape;

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.InteractiveSurface;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Engine.Surface;

public class GraphsState extends State
{
	private Surface _bg;
	private InteractiveSurface _btnGeoscape;

/**
 * Initializes all the elements in the Graphs screen.
 * @param game Pointer to the core game.
 */
public GraphsState(Game game)
{
	super(game);
	// Create objects
	_bg = new Surface(320, 200, 0, 0);
	_btnGeoscape = new InteractiveSurface(32, 24, 288, 0);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("PALETTES.DAT_2").getColors());

	add(_bg);
	add(_btnGeoscape);

	// Set up objects
	_game.getResourcePack().getSurface("GRAPHS.SPK").blit(_bg);

	_btnGeoscape.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnGeoscapeClick(action);
		}
	});
}

/**
 * Returns to the previous screen.
 * @param action Pointer to an action.
 */
public void btnGeoscapeClick(Action action)
{
	_game.popState();
}

}
