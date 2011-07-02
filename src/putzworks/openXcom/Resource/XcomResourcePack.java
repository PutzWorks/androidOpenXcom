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
package putzworks.openXcom.Resource;

import putzworks.openXcom.Engine.Font;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.Surface;
import putzworks.openXcom.Geoscape.Globe;
import putzworks.openXcom.Geoscape.Polyline;
import putzworks.openXcom.Ruleset.MapDataSet;

public class XcomResourcePack extends ResourcePack
{

/**
 * Initializes the resource pack by loading all the resources
 * contained in the original game folder.
 * @param folder Subfolder to load resources from.
 */
public XcomResourcePack(final String folder)
{
	super(folder);
	// Load palettes
	for (int i = 0; i < 5; i++)
	{
		StringBuffer s1 = new StringBuffer(), s2 = new StringBuffer();
		s1.append(folder + "GEODATA/PALETTES.DAT");
		s2.append("PALETTES.DAT_" + i);
		_palettes.put(s2.toString(), new Palette());
		_palettes.get(s2.toString()).loadDat(insensitive(s1.toString()), 256, Palette.palOffset(i));
	}

	StringBuffer s1 = new StringBuffer(), s2 = new StringBuffer();
	s1.append(folder + "GEODATA/BACKPALS.DAT");
	s2.append("BACKPALS.DAT");
	_palettes.put(s2.toString(), new Palette());
	_palettes.get(s2.toString()).loadDat(insensitive(s1.toString()), 128);
	
	// Load fonts
	String font[] = {"BIGLETS.DAT",
						  "SMALLSET.DAT"};
	
	for (int i = 0; i < 2; i++)
	{
		StringBuffer s = new StringBuffer();
		s.append(folder + "GEODATA/" + font[i]);
		if (font[i] == "BIGLETS.DAT")
			_fonts.put(font[i], new Font(16, 16, 173));
		else if (font[i] == "SMALLSET.DAT")
			_fonts.put(font[i], new Font(8, 9, 173, -1));
		_fonts.get(font[i]).getSurface().loadScr(insensitive(s.toString()));
		_fonts.get(font[i]).load();
	}
		
	// Load language graphics
	String lang[] = {"English",
						  "French",
						  "German",
						  "Italian",
						  "Spanish"};

	for (int i = 1; i < 5; i++)
	{
		StringBuffer s3 = new StringBuffer(), s4 = new StringBuffer();
		s3.append(folder + "Language/" + lang[i] + ".geo");
		s4.append(lang[i] + ".geo");
		_surfaces.put(s4.toString(), new Surface(64, 154));
		_surfaces.get(s4.toString()).loadScr(insensitive(s3.toString()));
	}

	// Load surfaces
	{
		StringBuffer s = new StringBuffer();
		s.append(folder + "GEODATA/" + "INTERWIN.DAT");
		_surfaces.put("INTERWIN.DAT", new Surface(160, 556));
		_surfaces.get("INTERWIN.DAT").loadScr(insensitive(s.toString()));
	}

	String scrs[] = {"BACK01.SCR",
						  "BACK02.SCR",
						  "BACK03.SCR",
						  "BACK04.SCR",
						  "BACK05.SCR",
						  "BACK06.SCR",
						  "BACK07.SCR",
						  "BACK08.SCR",
						  "BACK09.SCR",
						  "BACK10.SCR",
						  "BACK11.SCR",
						  "BACK12.SCR",
						  "BACK13.SCR",
						  "BACK14.SCR",
						  "BACK15.SCR",
						  "BACK16.SCR",
						  "BACK17.SCR",
						  "GEOBORD.SCR",
						  "UP_BORD2.SCR"};

	for (int i = 0; i < 19; i++)
	{
		StringBuffer s = new StringBuffer();
		s << folder << "GEOGRAPH/" << scrs[i];
		_surfaces[scrs[i]] = new Surface(320, 200);
		_surfaces[scrs[i]].loadScr(insensitive(s.toString()));
	}

	String spks[] = {"UP001.SPK",
						  "UP002.SPK",
						  "UP003.SPK",
						  "UP004.SPK",
						  "UP005.SPK",
						  "UP006.SPK",
						  "UP007.SPK",
						  "UP008.SPK",
						  "UP009.SPK",
						  "UP010.SPK",
						  "UP011.SPK",
						  "UP012.SPK",
						  "UP013.SPK",
						  "UP014.SPK",
						  "UP015.SPK",
						  "UP016.SPK",
						  "UP017.SPK",
						  "UP018.SPK",
						  "UP019.SPK",
						  "UP020.SPK",
						  "UP021.SPK",
						  "UP022.SPK",
						  "UP023.SPK",
						  "UP024.SPK",
						  "UP025.SPK",
						  "UP026.SPK",
						  "UP027.SPK",
						  "UP028.SPK",
						  "UP029.SPK",
						  "UP030.SPK",
						  "UP031.SPK",
						  "UP032.SPK",
						  "UP033.SPK",
						  "UP034.SPK",
						  "UP035.SPK",
						  "UP036.SPK",
						  "UP037.SPK",
						  "UP038.SPK",
						  "UP039.SPK",
						  "UP040.SPK",
						  "UP041.SPK",
						  "UP042.SPK",
						  "GRAPHS.SPK"};

	for (int i = 0; i < 43; i++)
	{
		StringBuffer s;
		s.append(folder + "GEOGRAPH/" + spks[i]);
		_surfaces.put(spks[i], new Surface(320, 200));
		_surfaces.get(spks[i]).loadSpk(insensitive(s.toString()));
	}

	// Load surface sets
	String sets[] = {"BASEBITS.PCK",
						  "INTICON.PCK",
						  "TEXTURE.DAT"};

	for (int i = 0; i < 3; i++)
	{
		StringBuffer s;
		s.append(folder + "GEOGRAPH/" << sets[i]);

		String ext = sets[i].substr(sets[i].length()-3, sets[i].length());
		if (ext == "PCK")
		{
			String tab = sets[i].substr(0, sets[i].length()-4) + ".TAB";
			StringBuffer s2 = new StringBuffer();
			s2.append(folder + "GEOGRAPH/" + tab);
			_sets.put(sets[i], new SurfaceSet(32, 40));
			_sets.get(sets[i]).loadPck(insensitive(s.toString()), insensitive(s2.toString()));
		}
		else
		{
			_sets.put(sets[i], new SurfaceSet(32, 32));
			_sets.get(sets[i]).loadDat(insensitive(s.toString()));
		}
	}

	// Load polygons
	StringBuffer s = new StringBuffer();
	s.append(folder + "GEODATA/" + "WORLD.DAT");
	Globe.loadDat(insensitive(s.toString()), _polygons);

	// Load polylines (extracted from game)
	// -10 = Start of line
	// -20 = End of data
	double lines[] = {-10, 1.18901, -0.412334, 1.23918, -0.425424, 1.213, -0.471239, 1.22828, -0.490874, 1.23482, -0.482147, 1.30245, -0.541052, 1.29373,
					  -0.608684, 1.35918, -0.61741, 1.38099, -0.53887, 1.41154, -0.530144, 1.39626, -0.503964, 1.53153, -0.460331, 1.54025, -0.488692,
					  1.55116, -0.490874, 1.55334, -0.466876, 1.60352, -0.469057, 1.59916, -0.488692, 1.67552, -0.517054, 1.69515, -0.475602, 1.61661,
					  -0.386154, 1.61225, -0.436332, 1.56861, -0.440696, 1.56425, -0.460331, 1.54243, -0.462512, 1.53589, -0.449422, 1.55552, -0.373064,
					  -10, 6.13047, -0.726493, 6.17628, -0.726493, 6.1501, -0.645772, -10, 6.25264, -0.759218, 0.0109083, -0.73522, 0.0567232, -0.741765,
					  -10, 0.128718, -0.7614, 0.122173, -0.80067, 0.102538, -0.807215, 0.1309, -0.829031, 0.14399, -0.85303, 0.111265, -0.863938, 0.0719948,
					  -0.870483, 0.0501782, -0.885755, -10, 0.122173, -0.80067, 0.148353, -0.811578, 0.159261, -0.80067, 0.211621, -0.820305, 0.239983,
					  -0.811578, 0.239983, -0.794125, -10, 0.111265, -0.863938, 0.102538, -0.907571, 0.11781, -0.90539, 0.122173, -0.938114, -10, 0.139626,
					  -0.959931, 0.181078, -0.953386, -10, 0.248709, -0.942478, 0.261799, -0.887936, 0.213803, -0.877028, 0.242164, -0.85303, 0.229074,
					  -0.829031, 0.1309, -0.829031, -10, 0.0458149, -0.109083, 0.0479966, -0.148353, 0.0654498, -0.185441, 0.0698132, -0.237801, 0.0981748,
					  -0.244346, 0.122173, -0.224711, 0.17017, -0.222529, 0.231256, -0.235619, 0.257436, -0.211621, 0.19635, -0.113446, 0.176715, -0.126536,
					  0.148353, -0.0763582, -10, 0.438514, -0.554142, 0.436332, -0.383972, 0.595594, -0.383972, 0.628319, -0.410152, -10, 0.59123, -0.547597,
					  0.619592, -0.493056, -10, 0.283616, 0.4996, 0.349066, 0.495237, 0.349066, 0.434151, 0.362156, 0.469057, 0.407971, 0.440696, 0.447241,
					  0.449422, 0.510509, 0.386154, 0.545415, 0.390517, 0.558505, 0.469057, 0.575959, 0.464694, -10, 5.36252, 0.580322, 5.27962, 0.523599,
					  5.34071, 0.449422, 5.27089, 0.386154, 5.26653, 0.283616, 5.14436, 0.174533, 5.05491, 0.194168, 4.996, 0.14399, 5.01564, 0.0872665,
					  5.06364, 0.0763582, 5.06582, -0.0305433, 5.18145, -0.0370882, 5.15527, -0.0698132, 5.2229, -0.0938114, 5.2578, -0.019635, 5.35816,
					  -0.0327249, 5.38652, -0.0741765, -10, 4.10152, -0.85303, 4.45059, -0.85303, 4.62512, -0.855211, 4.71893, -0.837758, -10, 5.116, -0.776672,
					  5.08545, -0.824668, 5.03309, -0.785398, 4.97419, -0.785398, 4.95019, -0.770127, -10, 3.82227, -1.21519, 3.82227, -1.05374, 4.01426,
					  -0.977384, 3.95972, -0.949023, -10, 4.23897, -0.569414, 4.42659, -0.554142, 4.48113, -0.503964, 4.51386, -0.519235, 4.55531, -0.460331,
					  4.59022, -0.455967, -10, 4.82584, -0.728675, 4.84983, -0.750492, -10, 4.8062, -0.81376, 4.82802, -0.80067, -10, 0.545415, -1.21955, 0.549779,
					  -1.09738, 0.490874, -1.05156, -10, 0.488692, -1.04283, 0.490874, -0.981748, 0.569414, -0.933751, 0.554142, -0.909753, 0.698132, -0.863938,
					  0.665407, -0.818123, -10, 0.693768, -0.763582, 0.857393, -0.730857, -10, 0.861756, -0.805033, 0.831213, -0.87921, 1.0472, -0.885755, 1.0712,
					  -0.944659, 1.2021, -0.966476, 1.34172, -0.951204, 1.39626, -0.885755, 1.53589, -0.857393, 1.71042, -0.872665, 1.72569, -0.909753, 1.91986,
					  -0.859575, 2.03767, -0.870483, 2.08131, -0.872665, 2.09658, -0.922843, 2.19693, -0.925025, 2.23184, -0.86612, 2.34747, -0.842121, 2.32129,
					  -0.785398, 2.28638, -0.783217, 2.27984, -0.73522, 2.16857, -0.698132, -10, 1.88277, -0.375246, 1.8435, -0.407971, 1.77587, -0.370882,
					  1.73006, -0.386154, 1.72569, -0.423242, 1.7017, -0.418879, 1.72569, -0.477784, 1.69515, -0.475602, -10, 1.59916, -0.488692, 1.55116,
					  -0.490874, -10, 1.54025, -0.488692, 1.41154, -0.530144, -10, 1.35918, -0.61741, 1.28064, -0.687223, 1.40499, -0.737402, 1.39626, -0.785398,
					  1.4399, -0.78758, 1.44644, -0.824668, 1.49662, -0.822486, 1.50753, -0.857393, 1.53589, -0.857393, 1.5817, -0.789761, 1.67988, -0.746128,
					  1.8326, -0.724312, 1.95477, -0.7614, 1.95695, -0.785398, 2.09221, -0.815941, 2.02022, -0.833395, 2.03767, -0.870483, -20};

	Polyline l = null;
	int start = 0;
	for (int i = 0; lines[i] > -19.999; i++)
	{
		if (lines[i] < -9.999 && lines[i] > -10.001)
		{
			if (l != null)
			{
				_polylines.add(l);
			}
			int points = 0;
			for (int j = i + 1; lines[j] > -9.999; j++)
			{
				points++;
			}
			points /= 2;
			l = new Polyline(points);
			start = i + 1;
		}
		else
		{
			if ((i - start) % 2 == 0)
			{
				l.setLongitude((i - start) / 2, lines[i]);
			}
			else
			{
				l.setLatitude((i - start) / 2, lines[i]);
			}
		}
	}
	_polylines.add(l);

	// Load musics
	String mus[] = {"GMDEFEND",
						 "GMENBASE",
						 "GMGEO1",
						 "GMGEO2",
						 "GMINTER",
						 "GMINTRO1",
						 "GMINTRO2",
						 "GMINTRO3",
						 "GMLOSE",
						 "GMMARS",
						 "GMNEWMAR",
						 "GMSTORY",
						 "GMTACTIC",
						 "GMWIN"};
	String exts[] = {"OGG", "MP3", "MID"};
	int tracks[] = {3, 6, 0, 18, 2, 19, 20, 21, 10, 8, 9, 12, 17, 11};

	// Check which music version is available
	boolean cat = true;
	GMCatFile gmcat = 0;

	StringBuffer musDos = new StringBuffer();
	musDos.append(folder + "SOUND/GM.CAT");
	struct stat musInfo;
	if (stat(insensitive(musDos.toString()), musInfo) == 0)
	{
		cat = true;
		gmcat = new GMCatFile(insensitive(musDos.toString()));
	}
	else
	{
		cat = false;
	}

	for (int i = 0; i < 14; i++)
	{
		if (cat)
		{
			_musics[mus[i]] = gmcat.loadMIDI(tracks[i]);
		}
		else
		{
			_musics[mus[i]] = new Music();
			for (int j = 0; j < 3; j++)
			{
				StringBuffer s = new StringBuffer();
				s.append(folder + "SOUND/" + mus[i] + "." + exts[j]);
				struct stat info;
				if (stat(insensitive(s.toString()), info) == 0) 
				{
					_musics[mus[i]].load(insensitive(s.toString()));
					break;
				}
			}
		}
	}
	delete gmcat;

	// Load sounds
	String catsId[] = {"GEO.CAT",
							"BATTLE.CAT",
							"INTRO.CAT"};
	String catsDos[] = {"SOUND2.CAT",
							 "SOUND1.CAT",
							 "INTRO.CAT"};
	String catsWin[] = {"SAMPLE.CAT",
							 "SAMPLE2.CAT",
							 "SAMPLE3.CAT"};

	// Check which sound version is available
	String cats = 0;
	boolean wav = true;

	StringBuffer win = new StringBuffer(), dos = new StringBuffer();
	win.append(folder + "SOUND/" + catsWin[0]);
	dos.append(folder + "SOUND/" + catsDos[0]);
	struct stat sndInfo;
	if (stat(insensitive(win.toString()), sndInfo) == 0)
	{
		cats = catsWin;
		wav = true;
	}
	else if (stat(insensitive(dos.toString()), sndInfo) == 0)
	{
		cats = catsDos;
		wav = false;
	}

	for (int i = 0; i < 3; i++)
	{
		if (cats == 0)
		{
			_sounds[catsId[i]] = new SoundSet();
		}
		else
		{
			StringBuffer s;
			s << folder << "SOUND/" << cats[i];
			_sounds[catsId[i]] = new SoundSet();
			_sounds[catsId[i]].loadCat(insensitive(s.toString()), wav);
		}
	}

	TextButton.soundPress = _sounds["GEO.CAT"].getSound(0);
	Window.soundPopup[0] = _sounds["GEO.CAT"].getSound(1);
	Window.soundPopup[1] = _sounds["GEO.CAT"].getSound(2);
	Window.soundPopup[2] = _sounds["GEO.CAT"].getSound(3);

	loadBattlescapeResources(); // TODO load this at battlescape start, unload at battlescape end?
}


public void loadBattlescapeResources()
{
	// Load Battlescape ICONS
	StringBuffer s = new StringBuffer();
	s.append(_folder + "UFOGRAPH/" + "ICONS.PCK");
	_surfaces.put("ICONS.PCK", new Surface(320, 200));
	_surfaces.get("ICONS.PCK").loadSpk(insensitive(s.toString()));

	s.delete(0, s.length());
	StringBuffer s2 = new StringBuffer();
	s.append(_folder + "UFOGRAPH/" + "CURSOR.PCK");
	s2.append(_folder + "UFOGRAPH/" + "CURSOR.TAB");
	_sets.put("CURSOR.PCK", new SurfaceSet(32, 40));
	_sets.get("CURSOR.PCK").loadPck(insensitive(s.toString()), insensitive(s2.toString()));

	s.delete(0, s.length());
	s2.delete(0, s2.length());
	s.append(_folder + "UFOGRAPH/" + "SMOKE.PCK");
	s2.append(_folder + "UFOGRAPH/" + "SMOKE.TAB");
	_sets.put("SMOKE.PCK", new SurfaceSet(32, 40));
	_sets.get("SMOKE.PCK").loadPck(insensitive(s.toString()), insensitive(s2.toString()));

	s.delete(0, s.length());
	s2.delete(0, s2.length());
	s.append(_folder + "UFOGRAPH/" + "X1.PCK");
	s2.append(_folder + "UFOGRAPH/" + "X1.TAB");
	_sets.put("X1.PCK", new SurfaceSet(128, 64));
	_sets.get("X1.PCK").loadPck(insensitive(s.toString()), insensitive(s2.toString()));

	s.delete(0, s.length());
	s.append(_folder + "UFOGRAPH/" + "UNIBORD.PCK");
	_surfaces.put("UNIBORD.PCK", new Surface(320, 200));
	_surfaces.get("UNIBORD.PCK").loadSpk(insensitive(s.toString()));


	// Load Battlescape Terrain (only blacks are loaded, others are loaded just in time)
	String bsets[] = {"BLANKS.PCK"};

	for (int i = 0; i < 1; i++)
	{
		StringBuffer s = new StringBuffer();
		s.append(_folder + "TERRAIN/" + bsets[i]);
		String tab = bsets[i].substr(0, bsets[i].length()-4) + ".TAB";
		StringBuffer s2 = new StringBuffer();
		s2.append(_folder + "TERRAIN/" + tab);
		_sets.put(bsets[i], new SurfaceSet(32, 40));
		_sets.get(bsets[i]).loadPck(insensitive(s.toString()), insensitive(s2.toString()));
	}

	// Load Battlescape units
	String usets[] = {"SILACOID.PCK",
							"CELATID.PCK",
							"HANDOB.PCK",
							"CYBER.PCK",
							"FLOOROB.PCK",
							"SECTOID.PCK",
							"CIVF.PCK",
							"CIVM.PCK",
							"XCOM_1.PCK",
							"SNAKEMAN.PCK",
							"XCOM_0.PCK",
							"CHRYS.PCK",
							"TANKS.PCK",
							"FLOATER.PCK",
							"XCOM_2.PCK",
							"ZOMBIE.PCK",
							"MUTON.PCK",
							"X_REAP.PCK",
							"ETHEREAL.PCK",
							"X_ROB.PCK"
					 };

	for (int i = 0; i < 20; i++)
	{
		StringBuffer s = new StringBuffer();
		s.append(_folder + "UNITS/" + usets[i]);
		String tab = usets[i].substr(0, usets[i].length()-4) + ".TAB";
		StringBuffer s2 = new StringBuffer();
		s2.append(_folder + "UNITS/" + tab);
		_sets.put(usets[i], new SurfaceSet(32, 40));
		_sets.get(usets[i]).loadPck(insensitive(s.toString()), insensitive(s2.toString()));
	}
	s.delete(0, s.length());
	s.append(_folder + "UNITS/" + "BIGOBS.PCK");
	s2.delete(0, s2.length());
	s2.append(_folder + "UNITS/" + "BIGOBS.TAB");
	_sets.put("BIGOBS.PCK", new SurfaceSet(32, 48));
	_sets.get("BIGOBS.PCK").loadPck(insensitive(s.toString()), insensitive(s2.toString()));

	s.delete(0, s.length());
	s.append(_folder + "GEODATA/" + "LOFTEMPS.DAT");
	MapDataSet.loadLOFTEMPS(insensitive(s.toString()), _voxelData);
}

}
