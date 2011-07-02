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

import putzworks.openXcom.Basescape.BasescapeState;
import putzworks.openXcom.Battlescape.BattlescapeGenerator;
import putzworks.openXcom.Battlescape.BattlescapeState;
import putzworks.openXcom.Engine.Action;
import putzworks.openXcom.Engine.ActionHandler;
import putzworks.openXcom.Engine.Game;
import putzworks.openXcom.Engine.InteractiveSurface;
import putzworks.openXcom.Engine.Language;
import putzworks.openXcom.Engine.Palette;
import putzworks.openXcom.Engine.RNG;
import putzworks.openXcom.Engine.State;
import putzworks.openXcom.Engine.StateHandler;
import putzworks.openXcom.Engine.Surface;
import putzworks.openXcom.Engine.Timer;
import putzworks.openXcom.Interface.ImageButton;
import putzworks.openXcom.Interface.Text;
import putzworks.openXcom.Interface.Text.TextHAlign;
import putzworks.openXcom.Ruleset.RuleItem.BattleType;
import putzworks.openXcom.Savegame.Base;
import putzworks.openXcom.Savegame.BaseFacility;
import putzworks.openXcom.Savegame.Craft;
import putzworks.openXcom.Savegame.GameTime.TimeTrigger;
import putzworks.openXcom.Savegame.SavedBattleGame;
import putzworks.openXcom.Savegame.SavedBattleGame.MissionType;
import putzworks.openXcom.Savegame.Target;
import putzworks.openXcom.Savegame.Transfer;
import putzworks.openXcom.Savegame.Ufo;
import putzworks.openXcom.Savegame.Waypoint;
import putzworks.openXcom.Ufopaedia.Ufopaedia;

public class GeoscapeState extends State
{
	private Surface _bg;
	private Globe _globe;
	private ImageButton _btnIntercept, _btnBases, _btnGraphs, _btnUfopaedia, _btnOptions, _btnFunding;
	private ImageButton _timeSpeed;
	private ImageButton _btn5Secs, _btn1Min, _btn5Mins, _btn30Mins, _btn1Hour, _btn1Day;
	private InteractiveSurface _btnRotateLeft, _btnRotateRight, _btnRotateUp, _btnRotateDown, _btnZoomIn, _btnZoomOut;
	private Text _txtHour, _txtHourSep, _txtMin, _txtMinSep, _txtSec, _txtWeekday, _txtDay, _txtMonth, _txtYear;
	private Timer _timer;
	private boolean _pause, _music;
	private Vector<State> _popups;

/**
 * Initializes all the elements in the Geoscape screen.
 * @param game Pointer to the core game.
 */
public GeoscapeState(Game game)
{
	super(game);
	_pause = false;
	_music = false;
	_popups =  new Vector<State>();
	// Create objects
	_bg = new Surface(320, 200, 0, 0);
	_globe = new Globe(_game, 130, 100, 256, 200, 0, 0);

	_btnIntercept = new ImageButton(63, 11, 257, 0);
	_btnBases = new ImageButton(63, 11, 257, 12);
	_btnGraphs = new ImageButton(63, 11, 257, 24);
	_btnUfopaedia = new ImageButton(63, 11, 257, 36);
	_btnOptions = new ImageButton(63, 11, 257, 48);
	_btnFunding = new ImageButton(63, 11, 257, 60);

	_btn5Secs = new ImageButton(31, 13, 257, 112);
	_btn1Min = new ImageButton(31, 13, 289, 112);
	_btn5Mins = new ImageButton(31, 13, 257, 126);
	_btn30Mins = new ImageButton(31, 13, 289, 126);
	_btn1Hour = new ImageButton(31, 13, 257, 140);
	_btn1Day = new ImageButton(31, 13, 289, 140);

	_btnRotateLeft = new InteractiveSurface(12, 10, 259, 176);
	_btnRotateRight = new InteractiveSurface(12, 10, 283, 176);
	_btnRotateUp = new InteractiveSurface(13, 12, 271, 162);
	_btnRotateDown = new InteractiveSurface(13, 12, 271, 187);
	_btnZoomIn = new InteractiveSurface(23, 23, 295, 156);
	_btnZoomOut = new InteractiveSurface(13, 17, 300, 182);

	_txtHour = new Text(20, 16, 259, 74);
	_txtHourSep = new Text(4, 16, 279, 74);
	_txtMin = new Text(20, 16, 283, 74);
	_txtMinSep = new Text(4, 16, 303, 74);
	_txtSec = new Text(11, 8, 307, 80);
	_txtWeekday = new Text(59, 8, 259, 87);
	_txtDay = new Text(29, 8, 259, 94);
	_txtMonth = new Text(29, 8, 288, 94);
	_txtYear = new Text(59, 8, 259, 101);

	_timeSpeed = _btn5Secs;
	_timer = new Timer(100);

	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("PALETTES.DAT_0").getColors());

	add(_bg);
	add(_globe);

	add(_btnIntercept);
	add(_btnBases);
	add(_btnGraphs);
	add(_btnUfopaedia);
	add(_btnOptions);
	add(_btnFunding);

	add(_btn5Secs);
	add(_btn1Min);
	add(_btn5Mins);
	add(_btn30Mins);
	add(_btn1Hour);
	add(_btn1Day);

	add(_btnRotateLeft);
	add(_btnRotateRight);
	add(_btnRotateUp);
	add(_btnRotateDown);
	add(_btnZoomIn);
	add(_btnZoomOut);

	add(_txtHour);
	add(_txtHourSep);
	add(_txtMin);
	add(_txtMinSep);
	add(_txtSec);
	add(_txtWeekday);
	add(_txtDay);
	add(_txtMonth);
	add(_txtYear);

	// Set up objects
	_game.getResourcePack().getSurface("GEOBORD.SCR").blit(_bg);

	Surface sidebar = null;
	if (_game.getLanguage().getName() == "DEUTSCH")
	{
		sidebar = _game.getResourcePack().getSurface("German.geo");
	}
	else if (_game.getLanguage().getName() == "FRANCAIS")
	{
		sidebar = _game.getResourcePack().getSurface("French.geo");
	}
	else if (_game.getLanguage().getName() == "ITALIANO")
	{
		sidebar = _game.getResourcePack().getSurface("Italian.geo");
	}
	else if (_game.getLanguage().getName() == "ESPANO")
	{
		sidebar = _game.getResourcePack().getSurface("Spanish.geo");
	}
	if (sidebar != null)
	{
		sidebar.setX(320 - sidebar.getWidth());
		sidebar.setY(0);
		sidebar.blit(_bg);
	}

	_btnIntercept.copy(_bg);
	_btnIntercept.setColor(Palette.blockOffset(15)+8);
	_btnIntercept.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnInterceptClick(action);
		}
	});

	_btnBases.copy(_bg);
	_btnBases.setColor(Palette.blockOffset(15)+8);
	_btnBases.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnBasesClick(action);
		}
	});

	_btnGraphs.copy(_bg);
	_btnGraphs.setColor(Palette.blockOffset(15)+8);
	_btnGraphs.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnGraphsClick(action));
		}
	});

	_btnUfopaedia.copy(_bg);
	_btnUfopaedia.setColor(Palette.blockOffset(15)+8);
	_btnUfopaedia.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnUfopaediaClick(action);
		}
	});

	_btnOptions.copy(_bg);
	_btnOptions.setColor(Palette.blockOffset(15)+8);
	_btnOptions.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnOptionsClick(action);
		}
	});

	_btnFunding.copy(_bg);
	_btnFunding.setColor(Palette.blockOffset(15)+8);
	_btnFunding.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnFundingClick(action);
		}
	});

	_btn5Secs.copy(_bg);
	_btn5Secs.setColor(Palette.blockOffset(15)+8);
	_btn5Secs.setGroup(_timeSpeed);

	_btn1Min.copy(_bg);
	_btn1Min.setColor(Palette.blockOffset(15)+8);
	_btn1Min.setGroup(_timeSpeed);

	_btn5Mins.copy(_bg);
	_btn5Mins.setColor(Palette.blockOffset(15)+8);
	_btn5Mins.setGroup(_timeSpeed);

	_btn30Mins.copy(_bg);
	_btn30Mins.setColor(Palette.blockOffset(15)+8);
	_btn30Mins.setGroup(_timeSpeed);

	_btn1Hour.copy(_bg);
	_btn1Hour.setColor(Palette.blockOffset(15)+8);
	_btn1Hour.setGroup(_timeSpeed);

	_btn1Day.copy(_bg);
	_btn1Day.setColor(Palette.blockOffset(15)+8);
	_btn1Day.setGroup(_timeSpeed);

	_btnRotateLeft.onMousePress(new ActionHandler() {
		public void handle(Action action) {
			btnRotateLeftPress(action);
		}
	});
	_btnRotateLeft.onMouseRelease(new ActionHandler() {
		public void handle(Action action) {
			btnRotateLeftRelease(action);
		}
	});

	_btnRotateRight.onMousePress(new ActionHandler() {
		public void handle(Action action) {
			btnRotateRightPress(action);
		}
	});
	_btnRotateRight.onMouseRelease(new ActionHandler() {
		public void handle(Action action) {
			btnRotateRightRelease(action);
		}
	});

	_btnRotateUp.onMousePress(new ActionHandler() {
		public void handle(Action action) {
			btnRotateUpPress(action);
		}
	});
	_btnRotateUp.onMouseRelease(new ActionHandler() {
		public void handle(Action action) {
			btnRotateUpRelease(action);
		}
	});

	_btnRotateDown.onMousePress(new ActionHandler() {
		public void handle(Action action) {
			btnRotateDownPress(action);
		}
	});
	_btnRotateDown.onMouseRelease(new ActionHandler() {
		public void handle(Action action) {
			btnRotateDownRelease(action);
		}
	});

	_btnZoomIn.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnZoomInClick(action);
		}
	});

	_btnZoomOut.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			btnZoomOutClick(action);
		}
	});

	_txtHour.setBig();
	_txtHour.setColor(Palette.blockOffset(15)+4);
	_txtHour.setAlign(TextHAlign.ALIGN_RIGHT);
	_txtHour.setText("");

	_txtHourSep.setBig();
	_txtHourSep.setColor(Palette.blockOffset(15)+4);
	_txtHourSep.setText(":");

	_txtMin.setBig();
	_txtMin.setColor(Palette.blockOffset(15)+4);
	_txtMin.setText("");

	_txtMinSep.setBig();
	_txtMinSep.setColor(Palette.blockOffset(15)+4);
	_txtMinSep.setText(":");

	_txtSec.setSmall();
	_txtSec.setColor(Palette.blockOffset(15)+4);
	_txtSec.setText("");

	_txtWeekday.setSmall();
	_txtWeekday.setColor(Palette.blockOffset(15)+4);
	_txtWeekday.setText("");
	_txtWeekday.setAlign(TextHAlign.ALIGN_CENTER);

	_txtDay.setSmall();
	_txtDay.setColor(Palette.blockOffset(15)+4);
	_txtDay.setText("");
	_txtDay.setAlign(TextHAlign.ALIGN_CENTER);

	_txtMonth.setSmall();
	_txtMonth.setColor(Palette.blockOffset(15)+4);
	_txtMonth.setText("");
	_txtMonth.setAlign(TextHAlign.ALIGN_CENTER);

	_txtYear.setSmall();
	_txtYear.setColor(Palette.blockOffset(15)+4);
	_txtYear.setText("");
	_txtYear.setAlign(TextHAlign.ALIGN_CENTER);

	_timer.onTimer(new StateHandler() {
		public void handle(State state) {
			timeAdvance();
		}
	});
	_timer.start();

	timeDisplay();
}

/**
 * Deletes timers.
 */
public void clearGeoscapeState()
{
	_timer = null;
}

/**
 * Updates the timer display and resets the palette
 * since it's bound to change on other screens.
 */
public void init()
{
	// Set palette
	_game.setPalette(_game.getResourcePack().getPalette("PALETTES.DAT_0").getColors());

	timeDisplay();

	_globe.onMouseClick(new ActionHandler() {
		public void handle(Action action) {
			globeClick(action);
		}
	});
	_globe.focus();
	_globe.draw();

	// Set music if it's not already playing
	if (!_music)
	{
		StringBuffer ss = new StringBuffer();
		ss.append("GMGEO" << RNG.generate(1, 2));
		_game.getResourcePack().getMusic(ss.toString()).play();
		_music = true;
	}
}

/**
 * Runs the game timer and handles popups.
 */
public void think()
{
	super.think();

	if (_popups.isEmpty())
	{
		// Handle timers
		_timer.think(this, null);
	}
	else
	{
		// Handle popups
		_globe.rotateStop();
		_game.pushState(_popups.firstElement());
		_popups.remove(_popups.firstElement());
	}

}

/**
 * Updates the Geoscape clock with the latest
 * game time and date in human-readable format.
 */
public void timeDisplay()
{
	StringBuffer ss = new StringBuffer(), ss2 = new StringBuffer();
	StringBuffer ss3 = new StringBuffer(), ss4 = new StringBuffer(), ss5 = new StringBuffer();

	ss.append(std.setfill('0') + std.setw(2) + _game.getSavedGame().getTime().getSecond());
	_txtSec.setText(Language.utf8ToWstr(ss.toString()));

	ss2.append(std.setfill('0') + std.setw(2) + _game.getSavedGame().getTime().getMinute());
	_txtMin.setText(Language.utf8ToWstr(ss2.toString()));

	ss3.append(_game.getSavedGame().getTime().getHour());
	_txtHour.setText(ss3.toString());

	ss4.append(_game.getSavedGame().getTime().getDay() + _game.getLanguage().getString(_game.getSavedGame().getTime().getDayString()));
	_txtDay.setText(ss4.toString());

	_txtWeekday.setText(_game.getLanguage().getString(_game.getSavedGame().getTime().getWeekdayString()));

	_txtMonth.setText(_game.getLanguage().getString(_game.getSavedGame().getTime().getMonthString()));

	ss5.append(_game.getSavedGame().getTime().getYear());
	_txtYear.setText(ss5.toString());
}

/**
 * Advances the game timer according to
 * the timer speed set, and calls the respective
 * triggers. The timer always advances in "5 secs"
 * cycles, regardless of the speed, otherwise it might
 * skip important steps. Instead, it just keeps advancing
 * the timer until the next speed step (eg. the next day
 * on 1 Day speed) or until an event occurs, since updating
 * the screen on each step would become cumbersomely slow.
 */
public void timeAdvance()
{
	int timeSpan = 0;
	if (_timeSpeed == _btn5Secs)
	{
		timeSpan = 1;
	}
	else if (_timeSpeed == _btn1Min)
	{
		timeSpan = 12;
	}
	else if (_timeSpeed == _btn5Mins)
	{
		timeSpan = 12 * 5;
	}
	else if (_timeSpeed == _btn30Mins)
	{
		timeSpan = 12 * 5 * 6;
	}
	else if (_timeSpeed == _btn1Hour)
	{
		timeSpan = 12 * 5 * 6 * 2;
	}
	else if (_timeSpeed == _btn1Day)
	{
		timeSpan = 12 * 5 * 6 * 2 * 24;
	}

	for (int i = 0; i < timeSpan && !_pause; i++)
	{
		TimeTrigger trigger;
		trigger = _game.getSavedGame().getTime().advance();
		switch (trigger)
		{
		case TIME_1MONTH:
			time1Month();
		case TIME_1DAY:
			time1Day();
		case TIME_1HOUR:
			time1Hour();
		case TIME_30MIN:
			time30Minutes();
		case TIME_10MIN:
			time10Minutes();
		case TIME_5SEC:
			time5Seconds();
		}
	}

	_pause = false;

	timeDisplay();
	_globe.draw();
}

/**
 * Takes care of any game logic that has to
 * run every game second, like craft movement.
 */
public void time5Seconds()
{
	// Handle UFO logic
	for (Ufo i: _game.getSavedGame().getUfos())
	{
		(i).think();
		if ((i).reachedDestination() || (i).getHoursCrashed() == 0)
		{
			(i).setDetected(false);
		}
	}

	// Handle craft logic
	for (Base i: _game.getSavedGame().getBases())
	{
		for (Craft j: (i).getCrafts())
		{
			if ((j).getDestination() != null)
			{
				Ufo u = (Ufo)((j).getDestination());
				if (u != null && !u.getDetected())
				{
					(j).setDestination(null);
					Waypoint w = new Waypoint();
					w.setLongitude(u.getLongitude());
					w.setLatitude(u.getLatitude());
					popup(new UfoLostState(_game, u.getName(_game.getLanguage())));
					popup(new GeoscapeCraftState(_game, (j), _globe, w));
				}
			}
			(j).think();
			if ((j).reachedDestination())
			{
				Ufo u = (Ufo)((j).getDestination());
				Waypoint w = (Waypoint)((j).getDestination());
				if (u != null)
				{
					if (!u.isCrashed())
					{
						timerReset();
						_music = false;
						popup(new DogfightState(_game, _globe, (j), u));
					}
					else
					{
						if ((j).getNumSoldiers() > 0)
						{
							// look up polygons texture
							int texture = 0, shade = 0;
							_globe.getPolygonTextureAndShade(u.getLongitude(),u.getLatitude(), texture, shade);
							_music = false;
							timerReset();
							popup(new ConfirmLandingState(_game, j, texture, shade));
						}
						else
						{
							(j).returnToBase();
						}
					}
				}
				else if (w != null)
				{
					popup(new CraftPatrolState(_game, (j), _globe));
					(j).setSpeed((j).getRules().getMaxSpeed() / 2);
					(j).setDestination(null);
				}
			}
		}
	}

	// Clean up dead UFOs
	for (Ufo i: _game.getSavedGame().getUfos())
	{
		if ((i).reachedDestination() || (i).getHoursCrashed() == 0)
		{
			i = null;
			_game.getSavedGame().getUfos().remove(i);
		}
	}

	// Clean up unused waypoints
	for (Waypoint i: _game.getSavedGame().getWaypoints())
	{
		if ((i).getFollowers().isEmpty())
		{
			i = null;
			_game.getSavedGame().getWaypoints().remove(i);
		}
	}
}

/**
 * Takes care of any game logic that has to
 * run every game ten minutes, like fuel consumption.
 */
public void time10Minutes()
{
	for (Base i: _game.getSavedGame().getBases())
	{
		for (Craft j: (i).getCrafts())
		{
			if ((j).getStatus() == "STR_OUT")
			{
				(j).consumeFuel();
				if (!(j).getLowFuel() && (j).getFuel() <= (j).getFuelLimit())
				{
					(j).setLowFuel(true);
					(j).returnToBase();
					popup(new LowFuelState(_game, (j), this));
				}
			}
		}
	}
}

/**
 * Takes care of any game logic that has to
 * run every game half hour, like UFO detection.
 */
public void time30Minutes()
{
	// Spawn UFOs
	int chance = RNG.generate(1, 100);
	if (chance <= 50)
	{
		int type = RNG.generate(1, 3);
		Ufo u;
		switch (type)
		{
		case 1:
			u = new Ufo(_game.getRuleset().getUfo("STR_SMALL_SCOUT"));
			break;
		case 2:
			u = new Ufo(_game.getRuleset().getUfo("STR_MEDIUM_SCOUT"));
			break;
		case 3:
			u = new Ufo(_game.getRuleset().getUfo("STR_LARGE_SCOUT"));
			break;
		}
		u.setLongitude(RNG.generate(0.0, 2*Math.PI));
		u.setLatitude(RNG.generate(-Math.PI/2, Math.PI/2));
		Waypoint w = new Waypoint();
		w.setLongitude(RNG.generate(0.0, 2*Math.PI));
		w.setLatitude(RNG.generate(-Math.PI/2, Math.PI/2));
		u.setDestination(w);
		u.setSpeed(RNG.generate(u.getRules().getMaxSpeed() / 4, u.getRules().getMaxSpeed() / 2));
		_game.getSavedGame().getUfos().add(u);
	}

	// Handle craft maintenance
	for (Base i: _game.getSavedGame().getBases())
	{
		for (Craft j: (i).getCrafts())
		{
			if ((j).getStatus() == "STR_REFUELLING")
			{
				(j).refuel();
			}
		}
	}

	// Handle UFO detection
	for (Ufo u: _game.getSavedGame().getUfos())
	{
		if ((u).isCrashed())
			continue;
		if (!(u).getDetected())
		{
			boolean detected = false;
			for (Base b: _game.getSavedGame().getBases()) //!Detected
			{
				for (BaseFacility f: (b).getFacilities())
				{
					if ((f).getBuildTime() != 0)
						continue;
					if ((f).insideRadarRange(u))
					{
						int chance1 = RNG.generate(1, 100);
						if (chance1 <= (f).getRules().getRadarChance())
						{
							detected = true;
						}
					}
				}
				for (Craft c: (b).getCrafts()) //!Detected
				{
					if ((c).getLongitude() == (b).getLongitude() && (c).getLatitude() == (b).getLatitude() && (c).getDestination() == null)
						continue;
					if ((c).insideRadarRange(u))
					{
						detected = true;
					}
				}
			}
			if (detected)
			{
				(u).setDetected(detected);
				popup(new UfoDetectedState(_game, (u), this, true));
			}
		}
		else
		{
			boolean detected = false;
			for (Base b: _game.getSavedGame()) //!Detected
			{
				for (BaseFacility f: (b).getFacilities()) //!Detected
				{
					detected = detected || (f).insideRadarRange(u);
				}
				for (Craft c: (b).getCrafts()) //!Detected
				{
					detected = detected || (c).insideRadarRange(u);
				}
			}
			(u).setDetected(detected);
		}
	}
}

/**
 * Takes care of any game logic that has to
 * run every game hour, like transfers.
 */
public void time1Hour()
{
	// Handle craft maintenance
	for (Base i: _game.getSavedGame().getBases())
	{
		for (Craft j: (i).getCrafts())
		{
			if ((j).getStatus() == "STR_REPAIRS")
			{
				(j).repair();
			}
			else if ((j).getStatus() == "STR_REARMING")
			{
				String s = (j).rearm();
				if (s != "")
				{
					popup(new CannotRearmState(_game, this, _game.getLanguage().getString(s), (j).getName(_game.getLanguage()), (i).getName()));
				}
			}
		}
	}

	// Handle crashed UFOs expiring
	for (Ufo i: _game.getSavedGame().getUfos())
	{
		if ((i).isCrashed() && (i).getHoursCrashed() > 0)
		{
			(i).setHoursCrashed((i).getHoursCrashed() - 1);
		}
	}

	// Handle transfers
	boolean window = false;
	for (Base i: _game.getSavedGame().getBases())
	{
		for (Transfer j: (i).getTransfers())
		{
			(j).advance(i);
			if (!window && (j).getHours() == 0)
			{
				window = true;
			}
		}
	}
	if (window)
	{
		popup(new ItemsArrivingState(_game, this));
	}
}

/**
 * Takes care of any game logic that has to
 * run every game day, like constructions.
 */
public void time1Day()
{
	// Handle facility construction
	for (Base i: _game.getSavedGame().getBases())
	{
		for (BaseFacility j: (i).getFacilities())
		{
			if ((j).getBuildTime() > 0)
			{
				(j).build();
				if ((j).getBuildTime() == 0)
				{
					timerReset();
					popup(new ProductionCompleteState(_game, _game.getLanguage().getString((j).getRules().getType()), (i).getName()));
				}
			}
		}
	}
}

/**
 * Takes care of any game logic that has to
 * run every game month, like funding.
 */
public void time1Month()
{
	// Handle funding
	timerReset();
	_game.getSavedGame().monthlyFunding();
	popup(new MonthlyReportState(_game));
}

/**
 * Slows down the timer back to minimum speed,
 * for when important events occur.
 */
public void timerReset()
{
	SDL_Event ev;
	ev.button.button = SDL_BUTTON_LEFT;
	Action act = new Action(ev, _game.getScreen().getXScale(), _game.getScreen().getYScale());
	_btn5Secs.mousePress(act, this);
}

/**
 * Adds a new popup window to the queue
 * (this prevents popups from overlapping)
 * and pauses the game timer respectively.
 * @param state Pointer to popup state.
 */
public void popup(State state)
{
	_pause = true;
	_popups.add(state);
}

/**
 * Returns a pointer to the Geoscape globe for
 * access by other substates.
 * @return Pointer to globe.
 */
public final Globe getGlobe()
{
	return _globe;
}

/**
 * Processes any left-clicks on globe markers,
 * or right-clicks to scroll the globe.
 * @param action Pointer to an action.
 */

public void globeClick(Action action)
{
	int mouseX = (int)Math.floor(action.getXMouse() / action.getXScale()), mouseY = (int)Math.floor(action.getYMouse() / action.getYScale());

	// Clicking markers on the globe
	if (action.getDetails().button.button == SDL_BUTTON_LEFT)
	{
		Vector<Target> v = _globe.getTargets(mouseX, mouseY, false);
		if (v.size() > 0)
		{
			_game.pushState(new MultipleTargetsState(_game, v, null, this));
		}
	}
}

/**
 * Opens the Intercept window.
 * @param action Pointer to an action.
 */
public void btnInterceptClick(Action action)
{
	_game.pushState(new InterceptState(_game, _globe));
}

/**
 * Goes to the Basescape screen.
 * @param action Pointer to an action.
 */
public void btnBasesClick(Action action)
{
	if (_game.getSavedGame().getBases().size() > 0)
	{
		_game.pushState(new BasescapeState(_game, _game.getSavedGame().getBases().firstElement(), _globe));
	}
	else
	{
		_game.pushState(new BasescapeState(_game, null, _globe));
	}
}

/**
 * Goes to the Graphs screen.
 * @param action Pointer to an action.
 */
public void btnGraphsClick(Action action)
{
	//_game.pushState(new GraphsState(_game));

	/* Daiky: uncomment this bit to start a terror mission */
	_game.getSavedGame().setBattleGame(new SavedBattleGame());
	BattlescapeGenerator bgen = new BattlescapeGenerator(_game);
	bgen.setMissionType(MissionType.MISS_TERROR);
	//bgen.setMissionType(MISS_UFOASSAULT);
	bgen.setWorldTexture(1);
	bgen.setWorldShade(1);
	bgen.setCraft(_game.getSavedGame().getBases().get(0).getCrafts().get(0));
	bgen.run();
	bgen = null;
	_music = false;
	_game.pushState(new BattlescapeState(_game));
}

/**
 * Goes to the Ufopaedia window.
 * @param action Pointer to an action.
 */
public void btnUfopaediaClick(Action action)
{
	Ufopaedia.open(_game);
}

/**
 * Opens the Options window.
 * @param action Pointer to an action.
 */
public void btnOptionsClick(Action action)
{
	_game.pushState(new OptionsState(_game));
}

/**
 * Goes to the Funding screen.
 * @param action Pointer to an action.
 */
public void btnFundingClick(Action action)
{
	_game.pushState(new FundingState(_game));
}

/**
 * Starts rotating the globe to the left.
 * @param action Pointer to an action.
 */
public void btnRotateLeftPress(Action action)
{
	_globe.rotateLeft();
}

/**
 * Stops rotating the globe to the left.
 * @param action Pointer to an action.
 */
public void btnRotateLeftRelease(Action action)
{
	_globe.rotateStop();
}

/**
 * Starts rotating the globe to the right.
 * @param action Pointer to an action.
 */
public void btnRotateRightPress(Action action)
{
	_globe.rotateRight();
}

/**
 * Stops rotating the globe to the right.
 * @param action Pointer to an action.
 */
public void btnRotateRightRelease(Action action)
{
	_globe.rotateStop();
}

/**
 * Starts rotating the globe upwards.
 * @param action Pointer to an action.
 */
public void btnRotateUpPress(Action action)
{
	_globe.rotateUp();
}

/**
 * Stops rotating the globe upwards.
 * @param action Pointer to an action.
 */
public void btnRotateUpRelease(Action action)
{
	_globe.rotateStop();
}

/**
 * Starts rotating the globe downwards.
 * @param action Pointer to an action.
 */
public void btnRotateDownPress(Action action)
{
	_globe.rotateDown();
}

/**
 * Stops rotating the globe downwards.
 * @param action Pointer to an action.
 */
public void btnRotateDownRelease(Action action)
{
	_globe.rotateStop();
}

/**
 * Zooms into the globe.
 * @param action Pointer to an action.
 */
public void btnZoomInClick(Action action)
{
	if (action.getDetails().button.button == SDL_BUTTON_LEFT)
	{
		_globe.zoomIn();
	}
	else if (action.getDetails().button.button == SDL_BUTTON_RIGHT)
	{
		_globe.zoomMax();
	}
}

/**
 * Zooms out of the globe.
 * @param action Pointer to an action.
 */
public void btnZoomOutClick(Action action)
{
	if (action.getDetails().button.button == SDL_BUTTON_LEFT)
	{
		_globe.zoomOut();
	}
	else if (action.getDetails().button.button == SDL_BUTTON_RIGHT)
	{
		_globe.zoomMin();
	}
}

}
