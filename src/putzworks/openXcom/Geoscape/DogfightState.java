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

import java.util.Vector;

import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.InteractiveSurface;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.RNG;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Engine.StateHandler;
import putzworks.openXcom.Engine.Surface;
import putzworks.openXcom.Engine.SurfaceSet;
import putzworks.openXcom.Engine.Timer;
import putzworks.openXcom.Interface.ImageButton;
import putzworks.openXcom.Interface.Text;
import putzworks.openXcom.Savegame.Craft;
import putzworks.openXcom.Savegame.CraftWeapon;
import putzworks.openXcom.Savegame.Target;
import putzworks.openXcom.Savegame.Ufo;

public class DogfightState extends State
{
	private Timer _animTimer, _moveTimer, _w1Timer, _w2Timer;
	private Surface _window, _battle, _weapon1, _range1, _weapon2, _range2, _damage;
	private InteractiveSurface _btnMinimize, _preview;
	private ImageButton _btnStandoff, _btnCautious, _btnStandard, _btnAggressive, _btnDisengage, _btnUfo;
	private ImageButton _mode;
	private Text _txtAmmo1, _txtAmmo2, _txtDistance, _txtStatus;
	private Globe _globe;
	private Craft _craft;
	private Ufo _ufo;
	private int _timeout, _currentDist, _targetDist, _currentRadius, _targetRadius;
	private Vector<Integer> _w1Dist, _w2Dist;
	private boolean _end, _destroy;

/**
 * Initializes all the elements in the Dogfight window.
 * @param game Pointer to the core game.
 * @param globe Pointer to the Geoscape globe.
 * @param craft Pointer to the craft intercepting.
 * @param ufo Pointer to the UFO being intercepted.
 */
public DogfightState(Game game, Globe globe, Craft craft, Ufo ufo)
{
	super(game);
	_globe = globe;
	_craft = craft;
	_ufo = ufo;
	_timeout = 50;
	_currentDist = 640;
	_targetDist = 560;
	_w1Dist =  new Vector<Integer>();
	_w2Dist = new Vector<Integer>();
	_end = false;
	_destroy = false;
	_targetRadius = _currentRadius = _ufo.getRules().getRadius();

	_screen = false;

	// Create objects
	_window = new Surface(160, 96, 80, 52);
	_battle = new Surface(77, 74, 83, 55);
	_weapon1 = new Surface(15, 17, 84, 104);
	_range1 = new Surface(21, 74, 99, 55);
	_weapon2 = new Surface(15, 17, 144, 104);
	_range2 = new Surface(21, 74, 123, 55);
	_damage = new Surface(22, 25, 173, 92);
	_btnMinimize = new InteractiveSurface(12, 12, 80, 52);
	_preview = new InteractiveSurface(160, 96, 80, 52);
	_btnStandoff = new ImageButton(36, 15, 163, 56);
	_btnCautious = new ImageButton(36, 15, 200, 56);
	_btnStandard = new ImageButton(36, 15, 163, 72);
	_btnAggressive = new ImageButton(36, 15, 200, 72);
	_btnDisengage = new ImageButton(36, 15, 200, 88);
	_btnUfo = new ImageButton(36, 17, 200, 104);
	_txtAmmo1 = new Text(16, 9, 84, 122);
	_txtAmmo2 = new Text(16, 9, 144, 122);
	_txtDistance = new Text(40, 9, 196, 124);
	_txtStatus = new Text(150, 9, 84, 137);

	_animTimer = new Timer(30);
	_moveTimer = new Timer(20);
	_w1Timer = new Timer(0);
	_w2Timer = new Timer(0);
	_mode = _btnStandoff;

	add(_window);
	add(_battle);
	add(_weapon1);
	add(_range1);
	add(_weapon2);
	add(_range2);
	add(_damage);
	add(_btnMinimize);
	add(_btnStandoff);
	add(_btnCautious);
	add(_btnStandard);
	add(_btnAggressive);
	add(_btnDisengage);
	add(_btnUfo);
	add(_txtAmmo1);
	add(_txtAmmo2);
	add(_txtDistance);
	add(_preview);
	add(_txtStatus);

	// Set up objects
	Surface graphic = _game.getResourcePack().getSurface("INTERWIN.DAT");
	graphic.setX(0);
	graphic.setY(0);
	graphic.getCrop().x = 0;
	graphic.getCrop().y = 0;
	int width = 160;
	int height = 96;
	graphic.getCrop().w = 160;
	graphic.getCrop().h = 96;
    _window.drawRectangle(graphic.getCrop(), 15);
	graphic.blit(_window);

    _preview.drawRectangle(graphic.getCrop(), 15);
	graphic.getCrop().y = 96;
	graphic.getCrop().h = 15;
	graphic.blit(_preview);
	graphic.setY(67);
	graphic.getCrop().y = 111;
	graphic.getCrop().h = 29;
	graphic.blit(_preview);
	graphic.setY(15);
	graphic.getCrop().y = 140 + 52 * _ufo.getRules().getSprite();
	graphic.getCrop().h = 52;
	graphic.blit(_preview);
	_preview.setVisible(false);
	_preview.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			previewClick(action);
		}
	});

	_btnMinimize.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnMinimizeClick(action);
		}
	});

	_btnStandoff.copy(_window);
	_btnStandoff.setColor(Palette.blockOffset(5)+4);
	_btnStandoff.setGroup(_mode);
	_btnStandoff.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnStandoffClick(action);
		}
	});

	_btnCautious.copy(_window);
	_btnCautious.setColor(Palette.blockOffset(5)+4);
	_btnCautious.setGroup(_mode);
	_btnCautious.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnCautiousClick(action);
		}
	});

	_btnStandard.copy(_window);
	_btnStandard.setColor(Palette.blockOffset(5)+4);
	_btnStandard.setGroup(_mode);
	_btnStandard.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnStandardClick(action);
		}
	});

	_btnAggressive.copy(_window);
	_btnAggressive.setColor(Palette.blockOffset(5)+4);
	_btnAggressive.setGroup(_mode);
	_btnAggressive.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnAggressiveClick(action);
		}
	});

	_btnDisengage.copy(_window);
	_btnDisengage.setColor(Palette.blockOffset(5)+4);
	_btnDisengage.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnDisengageClick(action);
		}
	});
	_btnDisengage.setGroup(_mode);

	_btnUfo.copy(_window);
	_btnUfo.setColor(Palette.blockOffset(5)+4);
	_btnUfo.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnUfoClick(action);
		}
	});

	_btnUfo.copy(_window);
	_btnUfo.setColor(Palette.blockOffset(5)+4);

	_txtAmmo1.setColor(Palette.blockOffset(5)+9);

	_txtAmmo2.setColor(Palette.blockOffset(5)+9);

	_txtDistance.setColor(Palette.blockOffset(5)+9);
	_txtDistance.setText("640");

	_txtStatus.setColor(Palette.blockOffset(5)+9);
	_txtStatus.setText(_game.getLanguage().getString("STR_STANDOFF"));

	SurfaceSet set = _game.getResourcePack().getSurfaceSet("INTICON.PCK");

	for (int i = 0; i < _craft.getRules().getWeapons(); i++)
	{
		CraftWeapon w = _craft.getWeapons().get(i);
		if (w == null)
			continue;

		Surface weapon = null, range = null;
		Text ammo = null;
		int x1, x2;
		if (i == 0)
		{
			weapon = _weapon1;
			range = _range1;
			ammo = _txtAmmo1;
			x1 = 2;
			x2 = 0;
		}
		else
		{
			weapon = _weapon2;
			range = _range2;
			ammo = _txtAmmo2;
			x1 = 0;
			x2 = 18;
		}

		// Draw weapon icon
		Surface frame = set.getFrame(w.getRules().getSprite() + 5);
		frame.setX(0);
		frame.setY(0);
		frame.blit(weapon);

		// Draw ammo
		StringBuffer ss = new StringBuffer();
		ss.append(w.getAmmo());
		ammo.setText(ss.toString());

		// Draw range (1 km = 1 pixel)
		short color = (short)(Palette.blockOffset(7) - 1);
		range.lock();

		int rangeY = range.getHeight() - w.getRules().getRange(), connectY = 57;
		for (int x = x1; x <= x1 + 18; x += 2)
		{
			range.setPixel(x, rangeY, color);
		}

		int minY = 0, maxY = 0;
		if (rangeY < connectY)
		{
			minY = rangeY;
			maxY = connectY;
		}
		else if (rangeY > connectY)
		{
			minY = connectY;
			maxY = rangeY;
		}
		for (int y = minY; y <= maxY; y++)
		{
			range.setPixel(x1 + x2, y, color);
		}
		for (int x = x2; x <= x2 + 2; x++)
		{
			range.setPixel(x, connectY, color);
		}
		range.unlock();
	}

	if (!(_craft.getRules().getWeapons() > 0 && _craft.getWeapons().get(0) != null))
	{
		_weapon1.setVisible(false);
		_range1.setVisible(false);
		_txtAmmo1.setVisible(false);
	}
	if (!(_craft.getRules().getWeapons() > 1 && _craft.getWeapons().get(1) != null))
	{
		_weapon2.setVisible(false);
		_range2.setVisible(false);
		_txtAmmo2.setVisible(false);
	}

	Surface frame = set.getFrame(_craft.getRules().getSprite() + 11);
	frame.setX(0);
	frame.setY(0);
	frame.blit(_damage);

	_animTimer.onTimer(new StateHandler() {
		public void handle(State state) {
			animate();
		}
	});
	_animTimer.start();

	_moveTimer.onTimer(new StateHandler() {
		public void handle(State state) {
			move();
		}
	});
	_moveTimer.start();

	_w1Timer.onTimer(new StateHandler() {
		public void handle(State state) {
			fireWeapon1();
		}
	});

	_w2Timer.onTimer(new StateHandler() {
		public void handle(State state) {
			fireWeapon2();
		}
	});

	// Set music
	_game.getResourcePack().getMusic("GMINTER").play();
}

/**
 * Deletes timers.
 */
public void clearDogfightState()
{
	_animTimer = null;
	_moveTimer = null;
	_w1Timer = null;
	_w2Timer = null;
}

/**
 * Runs the dogfighter timers.
 */
public void think()
{
	_animTimer.think(this, null);
	_moveTimer.think(this, null);
	_w1Timer.think(this, null);
	_w2Timer.think(this, null);
}

/**
 * Animates the window with a palette effect.
 */
public void animate()
{
	SDL_Color pal = _window.getPalette();
	SDL_Color[] newpal = new SDL_Color[16];
	for (int i = 0; i < 15; i++)
	{
		newpal[i] = pal[Palette.blockOffset(7) + i + 1];
	}
	newpal[15] = pal[Palette.blockOffset(7)];
	_window.setPalette(newpal, Palette.blockOffset(7), 16);

	// Clears text after a while
	if (_timeout == 0)
	{
		_txtStatus.setText("");
	}
	else
	{
		_timeout--;
	}
}

/**
 * Moves the craft towards the UFO according to
 * the current interception mode.
 */
public void move()
{
	// Update distance
	if (_currentDist < _targetDist)
	{
		_currentDist += 4;
	}
	else if (_currentDist > _targetDist)
	{
		_currentDist -= 2;
	}
	StringBuffer ss = new StringBuffer();
	ss.append(_currentDist);
	_txtDistance.setText(ss.toString());

	for (int i = 0; i < _craft.getRules().getWeapons(); i++)
	{
		CraftWeapon w = _craft.getWeapons().get(i);
		if (w == null)
			continue;
		Vector<Integer> wDist = null;
		Timer wTimer = null;
		if (i == 0)
		{
			wDist = _w1Dist;
			wTimer = _w1Timer;
		}
		else
		{
			wDist = _w2Dist;
			wTimer = _w2Timer;
		}

		for (int d: wDist)
		{
			// Update weapons
			(d) += 8;

			// Handle weapon damage
			if ((d) >= _currentDist)
			{
				int acc = RNG.generate(1, 100);
				if (acc <= w.getRules().getAccuracy() && !_ufo.isCrashed())
				{
					int damage = RNG.generate(w.getRules().getDamage() / 2, w.getRules().getDamage());
					_ufo.setDamage(_ufo.getDamage() + damage);
					setStatus("STR_UFO_HIT");
					_currentRadius += 4;
					_game.getResourcePack().getSoundSet("GEO.CAT").getSound(12).play();
				}
				d = wDist.remove(d);
			}
			else
			{
				++d;
			}
		}

		// Handle weapon firing
		if (!wTimer.isRunning() && _currentDist <= w.getRules().getRange() * 8 && w.getAmmo() > 0 && _mode != _btnStandoff && _mode != _btnDisengage && !_ufo.isCrashed())
		{
			wTimer.start();
			if (i == 0)
			{
				fireWeapon1();
			}
			else
			{
				fireWeapon2();
			}
		}
		else if (wTimer.isRunning() && (_currentDist > w.getRules().getRange() * 8 || w.getAmmo() == 0 || _mode == _btnStandoff || _mode == _btnDisengage || _ufo.isCrashed()))
		{
			wTimer.stop();
			if (w.getAmmo() == 0)
			{
				if (_mode == _btnCautious)
				{
					minimumDistance();
				}
				else if (_mode == _btnStandard)
				{
					maximumDistance();
				}
			}
		}
	}

	_battle.clear();

	// Draw UFO
	if (_targetRadius < _currentRadius)
	{
		_currentRadius--;
	}
	if (_currentRadius > 0)
	{
		for (int r = _currentRadius; r >= 0; r--)
		{
			_battle.drawCircle(_battle.getWidth() / 2, _battle.getHeight() - _currentDist / 8, r, Palette.blockOffset(7) + 4 + r);
		}
	}

	// Draw weapon shots
	for (int i = 0; i < _craft.getRules().getWeapons(); i++)
	{
		Vector<Integer> wDist = null;
		int off = 0;
		if (i == 0)
		{
			wDist = _w1Dist;
			off = -1;
		}
		else
		{
			wDist = _w2Dist;
			off = 1;
		}
		for (int d: wDist)
		{
			for (int j = -2; j <= 0; j++)
			{
				_battle.setPixel(_battle.getWidth() / 2 + off, _battle.getHeight() - (d) / 8 + j, Palette.blockOffset(7));
			}
		}
	}

	// Check when battle is over
	if ((_currentDist > 640 && _mode == _btnDisengage) || (_timeout == 0 && _ufo.isCrashed()))
	{
		_craft.returnToBase();
		if (_destroy)
		{
			// Disengage any other craft
			while (_ufo.getFollowers().size() > 0)
			{
				for (Target i: _ufo.getFollowers())
				{
					Craft c = (Craft)(i);
					if (c != null)
					{
						c.returnToBase();
						break;
					}
				}
			}

			// Clear UFO
			for (Ufo i: _game.getSavedGame().getUfos())
			{
				if (i == _ufo)
				{
					i = null;
					_game.getSavedGame().getUfos().remove(i);
					break;
				}
			}
		}
		_game.popState();
	}

	if (!_end && _ufo.isCrashed())
	{
		if (_ufo.isDestroyed())
		{
			setStatus("STR_UFO_DESTROYED");
			_game.getResourcePack().getSoundSet("GEO.CAT").getSound(11).play();
			_destroy = true;
		}
		else
		{
			setStatus("STR_UFO_CRASH_LANDS");
			_game.getResourcePack().getSoundSet("GEO.CAT").getSound(10).play();
			if (!_globe.insideLand(_ufo.getLongitude(), _ufo.getLatitude()))
			{
				_destroy = true;
			}
			else
			{
				_ufo.setHoursCrashed(24 + RNG.generate(0, 72));
			}
		}
		_targetRadius = 0;
		_end = true;
		_ufo.setSpeed(0);
	}
}

/**
 * Fires a shot from the first weapon
 * equipped on the craft.
 */
public void fireWeapon1()
{
	_w1Dist.add(8);
	CraftWeapon w1 = _craft.getWeapons().get(0);
	w1.setAmmo(w1.getAmmo() - 1);

	StringBuffer ss = new StringBuffer();
	ss.append(w1.getAmmo());
	_txtAmmo1.setText(ss.toString());

	_game.getResourcePack().getSoundSet("GEO.CAT").getSound(w1.getRules().getSound()).play();
}

/**
 * Fires a shot from the second weapon
 * equipped on the craft.
 */
public void fireWeapon2()
{
	_w2Dist.add(8);
	CraftWeapon w2= _craft.getWeapons().get(1);
	w2.setAmmo(w2.getAmmo() - 1);

	StringBuffer ss = new StringBuffer();
	ss.append(w2.getAmmo());
	_txtAmmo2.setText(ss.toString());

	_game.getResourcePack().getSoundSet("GEO.CAT").getSound(w2.getRules().getSound()).play();
}

/**
 * Sets the craft to the minimum distance
 * required to fire a weapon.
 */
public void minimumDistance()
{
	int max = 0;
	for (CraftWeapon i: _craft.getWeapons())
	{
		if (i == null)
			continue;
		if ((i).getRules().getRange() > max && (i).getAmmo() > 0)
		{
			max = (i).getRules().getRange();
		}
	}
	if (max == 0)
	{
		_targetDist = 560;
	}
	else
	{
		_targetDist = max * 8;
	}
}

/**
 * Sets the craft to the maximum distance
 * required to fire a weapon.
 */
public void maximumDistance()
{
	int min = 1000;
	for (CraftWeapon i: _craft.getWeapons())
	{
		if (i == null)
			continue;
		if ((i).getRules().getRange() < min && (i).getAmmo() > 0)
		{
			min = (i).getRules().getRange();
		}
	}
	if (min == 1000)
	{
		_targetDist = 560;
	}
	else
	{
		_targetDist = min * 8;
	}
}

/**
 * Updates the status text and restarts
 * the text timeout counter.
 * @param status New status text.
 */
public void setStatus(String status)
{
	_txtStatus.setText(_game.getLanguage().getString(status));
	_timeout = 50;
}

/**
 * Minimizes the dogfight window.
 * @param action Pointer to an action.
 */
public void btnMinimizeClick(Action action)
{
}

/**
 * Switches to Standoff mode (maximum range).
 * @param action Pointer to an action.
 */
public void btnStandoffClick(Action action)
{
	if (!_ufo.isCrashed())
	{
		setStatus("STR_STANDOFF");
		_targetDist = 560;
	}
}

/**
 * Switches to Cautious mode (maximum weapon range).
 * @param action Pointer to an action.
 */
public void btnCautiousClick(Action action)
{
	if (!_ufo.isCrashed())
	{
		setStatus("STR_CAUTIOUS_ATTACK");
		if (_craft.getNumWeapons() > 0 && _craft.getWeapons().get(0) != null)
		{
			_w1Timer.setInterval(_craft.getWeapons().get(0).getRules().getCautiousReload() * 75);
		}
		if (_craft.getNumWeapons() > 1 && _craft.getWeapons().get(1) != null)
		{
			_w2Timer.setInterval(_craft.getWeapons().get(1).getRules().getCautiousReload() * 75);
		}
		minimumDistance();
	}
}

/**
 * Switches to Standard mode (minimum weapon range).
 * @param action Pointer to an action.
 */
public void btnStandardClick(Action action)
{
	if (!_ufo.isCrashed())
	{
		setStatus("STR_STANDARD_ATTACK");
		if (_craft.getNumWeapons() > 0 && _craft.getWeapons().get(0) != null)
		{
			_w1Timer.setInterval(_craft.getWeapons().get(0).getRules().getStandardReload() * 75);
		}
		if (_craft.getNumWeapons() > 1 && _craft.getWeapons().get(1) != null)
		{
			_w2Timer.setInterval(_craft.getWeapons().get(1).getRules().getStandardReload() * 75);
		}
		maximumDistance();
	}
}

/**
 * Switches to Aggressive mode (minimum range).
 * @param action Pointer to an action.
 */
public void btnAggressiveClick(Action action)
{
	if (!_ufo.isCrashed())
	{
		setStatus("STR_AGGRESSIVE_ATTACK");
		if (_craft.getNumWeapons() > 0 && _craft.getWeapons().get(0) != null)
		{
			_w1Timer.setInterval(_craft.getWeapons().get(0).getRules().getAggressiveReload() * 75);
		}
		if (_craft.getNumWeapons() > 1 && _craft.getWeapons().get(1) != null)
		{
			_w2Timer.setInterval(_craft.getWeapons().get(1).getRules().getAggressiveReload() * 75);
		}
		_targetDist = 64;
	}
}

/**
 * Disengages from the UFO.
 * @param action Pointer to an action.
 */
public void btnDisengageClick(Action action)
{
	if (!_ufo.isCrashed())
	{
		setStatus("STR_DISENGAGING");
		_targetDist = 800;
	}
}

/**
 * Shows a front view of the UFO.
 * @param action Pointer to an action.
 */
public void btnUfoClick(Action action)
{
	_preview.setVisible(true);
	// Disable all other buttons to prevent misclicks
	_btnStandoff.setVisible(false);
	_btnCautious.setVisible(false);
	_btnStandard.setVisible(false);
	_btnAggressive.setVisible(false);
	_btnDisengage.setVisible(false);
	_btnUfo.setVisible(false);
	_btnMinimize.setVisible(false);
}

/**
 * Shows a front view of the UFO.
 * @param action Pointer to an action.
 */
public void previewClick(Action action)
{
	_preview.setVisible(false);
	// Reenable all other buttons to prevent misclicks
	_btnStandoff.setVisible(true);
	_btnCautious.setVisible(true);
	_btnStandard.setVisible(true);
	_btnAggressive.setVisible(true);
	_btnDisengage.setVisible(true);
	_btnUfo.setVisible(true);
	_btnMinimize.setVisible(true);
}

}
