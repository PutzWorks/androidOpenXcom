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

import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Engine.Surface;
import putzworks.openXcom.Engine.SurfaceSet;
import putzworks.openXcom.Interface.Text;
import putzworks.openXcom.Interface.Text.*;
import putzworks.openXcom.Interface.TextButton;
import putzworks.openXcom.Interface.TextList;
import putzworks.openXcom.Interface.Window;

public class TestState extends State
{
	private SurfaceSet _set;
	private Surface _surf;
	private TextButton _button;
	private Window _window;
	private Text _text;
	private TextList _list;
	private int _i;

public void FontToBmp(final String font, int w, int h)
{
	String dat = "./" + font + ".DAT";
	String bmp = "./" + font + ".BMP";
	Surface s = new Surface(w, h*173);
	s.loadScr(dat);

	SDL_Color[] clr = new SDL_Color[8];
	clr[0].r = 0;
	clr[0].g = 0;
	clr[0].b = 0;
	for (int i = 1; i < 8; i++)
	{
		clr[i].r = 256-i*32;
		clr[i].g = 256-i*32;
		clr[i].b = 256-i*32;
	}
	s.setPalette(clr, 0, 8);

	SDL_SaveBMP(s.getSurface(), bmp);
}

public void BmpToFont(final String font)
{
	String dat = "./" + font + ".DAT";
	String bmp = "./" + font + ".BMP";
	SDL_Surface s = SDL_LoadBMP(bmp);
}

/**
 * Initializes all the elements in the test screen.
 * @param game Pointer to the core game.
 */
public TestState(Game game)
{
	super(game);
	// Create objects
	_window = new Window(this, 300, 180, 10, 10);
	_text = new Text(280, 120, 20, 50);
	_button = new TextButton(100, 20, 110, 150);
	_list = new TextList(300, 180, 10, 10);
	_set = _game.getResourcePack().getSurfaceSet("BASEBITS.PCK");
	_set.getFrame(1);
	
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("PALETTES.DAT_1").getColors());
	_game.setPalette(_game.getResourcePack().getPalette("BACKPALS.DAT").getColors(Palette.blockOffset(2)), Palette.backPos, 16);

	add(_window);
	add(_button);
	add(_text);
	add(_list);

	// Set up objects
	_window.setColor(Palette.blockOffset(15)+4);
	_window.setBackground(_game.getResourcePack().getSurface("BACK04.SCR"));
	
	_button.setColor(Palette.blockOffset(15)+4);
	_button.setText("LOLOLO");
	
	_text.setColor(Palette.blockOffset(15)+1);
	//_text.setBig();
	_text.setWordWrap(true);
	_text.setAlign(TextHAlign.ALIGN_CENTER);
	_text.setVerticalAlign(TextVAlign.ALIGN_MIDDLE);
	//_text.setText(_game.getLanguage().getString("STR_COUNCIL_TERMINATED"));

	_list.setColor(Palette.blockOffset(15)+1);
	_list.setColumns(3, 100, 50, 100);
	_list.addRow(2, "a", "b");
	_list.addRow(3, "lol", "welp", "yo");
	_list.addRow(1, "0123456789");
	
	_i = 0;

	FontToBmp("BIGLETS_R", 16, 16);
	FontToBmp("SMALLSET_R", 8, 9);
	FontToBmp("BIGLETS_P", 16, 16);
	FontToBmp("SMALLSET_P", 8, 9);
}


public void think()
{
	super.think();

	/*
	_text.setText(_game.getLanguage().getString(_i));
	_i++;
	*/
}

public void blit()
{
	super.blit();

	_set.getFrame(0).blit(_game.getScreen().getSurface());
}

/**
 * Generates a surface with a row of every single color currently
 * loaded in the game palette (like a rainbow). First used for
 * testing 8bpp functionality, still useful for debugging palette issues.
 * @return Test surface.
 */
private SDL_Surface testSurface()
{
	SDL_Surface surface;

	// Create surface
	surface = SDL_CreateRGBSurface(SDL_HWSURFACE, 256, 25, 8, 0, 0, 0, 0);
	
	if (surface == 0)
	{
		throw Exception(SDL_GetError());
	}

	// Lock the surface
	SDL_LockSurface(surface);

	short index = (short)surface.pixels;
	
	for (int j = 0; j < 25; j++)
		for (int i = 0; i < 256; i++, index++)
			index = (short)i;

	// Unlock the surface
	SDL_UnlockSurface(surface);

	return surface;
}

}
