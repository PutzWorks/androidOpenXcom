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
package putzworks.openXcom.Engine;

import java.util.HashMap;

public class Language
{
	private WString _name;
	private HashMap<String, WString> _strings;

/**
 * Initializes an empty language file.
 */
public Language()
{
	_name = L"";
	_strings= new HashMap<String, WString>();
}

/**
 * Takes an 8-bit string encoded in UTF-8 and converts it
 * to a wide-character string.
 * @note Adapted from http://www.linuxquestions.org/questions/programming-9/wstring-utf8-conversion-in-pure-c-701084/
 * @param src UTF-8 string.
 * @return Wide-character string.
 */
public WString utf8ToWstr(final String src)
{
	WString dest;
	wchar_t w = 0;
	int bytes = 0;
	wchar_t err = 0xfffd;
	for (size_t i = 0; i < src.size(); ++i)
	{
		unsigned char c = (unsigned char)src[i];
		if (c <= 0x7f) //first byte
		{
			if (bytes){
				dest.add(err);
				bytes = 0;
			}
			dest.add((wchar_t)c);
		}
		else if (c <= 0xbf) //second/third/etc byte
		{
			if (bytes)
			{
				w = ((w << 6)|(c & 0x3f));
				bytes--;
				if (bytes == 0)
					dest.add(w);
			}
			else
				dest.add(err);
		}
		else if (c <= 0xdf) //2byte sequence start
		{
			bytes = 1;
			w = c & 0x1f;
		}
		else if (c <= 0xef) //3byte sequence start
		{
			bytes = 2;
			w = c & 0x0f;
		}
		else if (c <= 0xf7) //4byte sequence start
		{
			bytes = 3;
			w = c & 0x07;
		}
		else{
			dest.add(err);
			bytes = 0;
		}
	}
	if (bytes)
		dest.add(err);
	return dest;
}

/**
 * Takes a wide-character string and converts it
 * to a 8-bit string encoded in UTF-8.
 * @note Adapted from http://www.linuxquestions.org/questions/programming-9/wstring-utf8-conversion-in-pure-c-701084/
 * @param src Wide-character string.
 * @return UTF-8 string.
 */
public String wstrToUtf8(final WString src)
{
	std.string dest;
	for (size_t i = 0; i < src.size(); ++i)
	{
		wchar_t w = src[i];
		if (w <= 0x7f)
		{
			dest.add((char)w);
		}
		else if (w <= 0x7ff)
		{
			dest.add(0xc0 | ((w >> 6)& 0x1f));
			dest.add(0x80| (w & 0x3f));
		}
		else if (w <= 0xffff)
		{
			dest.add(0xe0 | ((w >> 12)& 0x0f));
			dest.add(0x80| ((w >> 6) & 0x3f));
			dest.add(0x80| (w & 0x3f));
		}
		else if (w <= 0x10ffff)
		{
			dest.add(0xf0 | ((w >> 18)& 0x07));
			dest.add(0x80| ((w >> 12) & 0x3f));
			dest.add(0x80| ((w >> 6) & 0x3f));
			dest.add(0x80| (w & 0x3f));
		}
		else
			dest.add('?');
	}
	return dest;
}

/**
 * Loads pairs of null-terminated strings contained in
 * a raw text file into the Language. Each pair is made of
 * an ID and a localized string.
 * @param filename Filename of the LNG file.
 * @sa <a href="../language_id.html">Reference Table</a>
 */
public void loadLng(final String filename)
{
	_strings.clear();

	// Load file and put text in map
	std.ifstream txtFile (filename.c_str(), std.ios.in | std.ios.binary);
	if (!txtFile)
	{
		throw Exception("Failed to load LNG");
	}

	char value;
	String buffer, bufid;
	WString bufstr;
	boolean first = true, id = true;

	while (txtFile.read(value, 1))
	{
		if (value != '\0')
		{
			buffer += value;
		}
		else
		{
			// Get language name
			if (first)
			{
				_name = utf8ToWstr(buffer);
				first = false;
			}
			else
			{
				// Get ID
				if (id)
				{
					bufid = buffer;
				}
				// Get string
				else
				{
					bufstr = utf8ToWstr(buffer);
					_strings[bufid] = bufstr;
				}
				id = !id;
			}
			buffer.clear();
		}
	}

	if (!txtFile.eof())
	{
		throw Exception("Invalid data from file");
	}

	txtFile.close();
}

/**
 * Returns the language's name in its native language.
 * @return Language name.
 */
public final String getName()
{
	return _name;
}

/**
 * Returns the localizable string with the specified ID.
 * If it's not found, just returns the ID.
 * @param id ID of the string.
 * @return String with the requested ID.
 */
public final String getString(String id)
{
	Map<String, WString>.const_iterator s = _strings.find(id);
	if (s == _strings.end())
	{
		std.wcout << "WARNING: " << utf8ToWstr(id) << " not found in " << _name << std.endl;
		return utf8ToWstr(id);
	}
	else
	{
		return s.second;
	}
}

/**
 * Outputs all the language IDs and strings
 * to an HTML table.
 */
public final void toHtml()
{
	std.ofstream htmlFile ("lang.html", std.ios.out);
	htmlFile << "<table border=\"1\" width=\"100%\">" << std.endl;
	htmlFile << "<tr><th>ID String</th><th>English String</th></tr>" << std.endl;
	for (std.map<std.string, WString>.const_iterator i = _strings.begin(); i != _strings.end(); ++i)
	{
		htmlFile << "<tr><td>" << i.first << "</td><td>";
		for (WString.const_iterator j = i.second.begin(); j != i.second.end(); ++j)
		{
			if (j == 2 || j == '\n')
			{
				htmlFile << "<br />";
			}
			else
			{
				htmlFile << j;
			}
		}
		htmlFile << "</td></tr>" << std.endl;
	}
	htmlFile << "</table>" << std.endl;
	htmlFile.close();
}

}
