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
package putzworks.openXcom.Savegame;

import java.util.EnumSet;
import java.util.Vector;

import putzworks.openXcom.Battlescape.Position;
import putzworks.openXcom.Engine.RNG;
import putzworks.openXcom.Savegame.Soldier.SoldierRank;

public class BattleUnit
{
	public enum UnitStatus {STATUS_STANDING, STATUS_WALKING, STATUS_TURNING, STATUS_AIMING, STATUS_FALLING, STATUS_DEAD, STATUS_UNCONSCIOUS};
	public enum UnitFaction {FACTION_PLAYER, FACTION_HOSTILE, FACTION_NEUTRAL};
	public enum UnitSide {SIDE_FRONT(0), SIDE_LEFT(1), SIDE_RIGHT(2), SIDE_REAR(3), SIDE_UNDER(4);
		public int id;
		private UnitSide(int i){
			this.id = i;
		}
		public static UnitSide get(int code) { 
	    	 for(UnitSide s : EnumSet.allOf(UnitSide.class)){
	               if (s.id == code){return s;}
	    	 }
	    	 return null;
	     }
	}
	enum UnitBodyPart {BODYPART_HEAD(0), BODYPART_TORSO(1), BODYPART_RIGHTARM(2), BODYPART_LEFTARM(3), BODYPART_RIGHTLEG(4), BODYPART_LEFTLEG(5);
	public int id;
	private UnitBodyPart(int i){
		this.id = i;
	}
	public static UnitBodyPart get(int code) { 
    	 for(UnitBodyPart s : EnumSet.allOf(UnitBodyPart.class)){
               if (s.id == code){return s;}
    	 }
    	 return null;
     }
}

	private Unit _unit;
	private UnitFaction _faction;
	private int _id;
	private Position _pos;
	private Position _lastPos;
	private int _direction, _toDirection;
	private Position _destination;
	private UnitStatus _status;
	private int _walkPhase, _fallPhase;
	private Vector<BattleUnit> _visibleUnits;
	private Vector<Tile> _visibleTiles;
	private int _tu, _energy, _health, _morale;
	private boolean _cached, _kneeled;
	private BattleItem _rightHandItem, _leftHandItem;
	private int[] _armor = new int[5];
	private int[] _fatalWounds = new int[6];

/**
 * Initializes a BattleUnit.
 * @param rules Pointer to RuleUnit object.
 * @param faction Which faction the units belongs to.
 */
public BattleUnit(Unit unit, UnitFaction faction)
{
	_unit = unit;
	_faction = faction;
	_id = 0;
	_pos = new Position();
	_lastPos = new Position();
	_direction = 0;
	_status = UnitStatus.STATUS_STANDING;
	_walkPhase = 0;
	_fallPhase = 0;
	_cached = false;
	_kneeled = false;

	_tu = unit.getTimeUnits();
	_energy = unit.getStamina();
	_health = unit.getHealth();
	_morale = 100;
	_armor[BattleUnit.UnitSide.SIDE_FRONT.id] = unit.getArmor().getFrontArmor();
	_armor[BattleUnit.UnitSide.SIDE_LEFT.id] = unit.getArmor().getSideArmor();
	_armor[BattleUnit.UnitSide.SIDE_RIGHT.id] = unit.getArmor().getSideArmor();
	_armor[BattleUnit.UnitSide.SIDE_REAR.id] = unit.getArmor().getRearArmor();
	_armor[BattleUnit.UnitSide.SIDE_UNDER.id] = unit.getArmor().getUnderArmor();
	for (int i = 0; i < 6; i++)
		_fatalWounds[i] = 0;
}

/**
 * Loads the unit from a YAML file.
 * @param node YAML node.
 */
public void load(final YAML.Node node)
{
	int a = 0;

	node["id"] >> _id;
	String name;
	node["faction"] >> a;
	_faction = (UnitFaction)a;
	node["status"] >> a;
	_status = (UnitStatus)a;

	node["X"] >> _pos.x;
	node["Y"] >> _pos.y;
	node["Z"] >> _pos.z;
	node["direction"] >> _direction;

	node["tu"] >> _tu;
	node["health"] >> _health;
	node["energy"] >> _energy;
	node["morale"] >> _morale;

	node["kneeled"] >> _kneeled;
}

/**
 * Saves the soldier to a YAML file.
 * @param out YAML emitter.
 */
public final void save(YAML.Emitter out)
{
	out << YAML.BeginMap;

	out << YAML.Key << "id" << YAML.Value << _id;
	out << YAML.Key << "faction" << YAML.Value << _faction;
	out << YAML.Key << "status" << YAML.Value << _status;

	out << YAML.Key << "X" << YAML.Value << _pos.x;
	out << YAML.Key << "Y" << YAML.Value << _pos.y;
	out << YAML.Key << "Z" << YAML.Value << _pos.z;
	out << YAML.Key << "direction" << YAML.Value << _direction;

	out << YAML.Key << "tu" << YAML.Value << _tu;
	out << YAML.Key << "health" << YAML.Value << _health;
	out << YAML.Key << "energy" << YAML.Value << _energy;
	out << YAML.Key << "morale" << YAML.Value << _morale;

	out << YAML.Key << "kneeled" << YAML.Value << _kneeled;

	out << YAML.EndMap;
}

/**
 * Returns the BattleUnit's unique ID.
 * @return Unique ID.
 */
public final int getId()
{
	return _id;
}

/**
 * Changes the BattleUnit's unique ID.
 * @param id Unique ID.
 */
public void setId(int id)
{
	_id = id;
}

/**
 * Returns the ruleset for the unit's type.
 * @return Pointer to ruleset.
 */
public final Unit getUnit()
{
	return _unit;
}

/**
 * Changes the BattleUnit's position.
 * @param pos position
 */
public void setPosition(final Position pos)
{
	_lastPos = _pos;
	_pos = pos;
}

/**
 * Gets the BattleUnit's position.
 * @return position
 */
public final Position getPosition()
{
	return _pos;
}

/**
 * Gets the BattleUnit's position.
 * @return position
 */
public final Position getLastPosition()
{
	return _lastPos;
}

/**
 * Gets the BattleUnit's destination.
 * @return destination
 */
public final Position getDestination()
{
	return _destination;
}

/**
 * Changes the BattleUnit's direction.
 * @param direction
 */
public void setDirection(int direction)
{
	_direction = direction;
	_toDirection = direction;
}

/**
 * Changes the BattleUnit's direction.
 * @return direction
 */
public final int getDirection()
{
	return _direction;
}

/**
 * Gets the unit's status.
 * @return the unit's status
 */
public final UnitStatus getStatus()
{
	return _status;
}

/**
 * startWalking
 * @param direction
 * @param destination
 */
public void startWalking(int direction, final Position destination)
{
	_direction = direction;
	_status = BattleUnit.UnitStatus.STATUS_WALKING;
	_walkPhase = 0;
	_destination = destination;
	_lastPos = _pos;
	_cached = false;
	_kneeled = false;
}

public void keepWalking()
{
	int middle, end;
	// diagonal walking takes double the steps
	middle = 4 + 4 * (_direction % 2);
	end = 8 + 8 * (_direction % 2);

	_walkPhase++;

	if (_walkPhase == middle)
	{
		// we assume we reached our destination tile
		// this is actually a drawing hack, so soldiers are not overlapped by floortiles
		_pos = _destination;
	}

	if (_walkPhase == end)
	{
		// we officially reached our destination tile
		_status = BattleUnit.UnitStatus.STATUS_STANDING;
		_walkPhase = 0;
	}

	_cached = false;
}

/*
 * Gets the walking phase for animation and sound.
 * return phase will always go from 0-7
 */
public final int getWalkingPhase()
{
	return _walkPhase % 8;
}

/*
 * Gets the walking phase for diagonal walking.
 * return phase this will be 0 or 8
 */
public final int getDiagonalWalkingPhase()
{
	return (_walkPhase / 8) * 8;
}

/**
 * Look at a point.
 * @param point.
 */
public void lookAt(final Position point)
{
	double ox = point.x - _pos.x;
	double oy = point.y - _pos.y;
	double angle = Math.atan2(ox, oy);
	if (angle < 0) angle += (Math.PI*2); // convert to a range from 0 to M_PI*2
	_toDirection = (int)((angle/(Math.PI/4))+Math.PI/4/2.0); // convert to 8 directions, somewhat rounded
	if (_toDirection > 7) _toDirection = 7;

	if (_toDirection != _direction)
	{
		_status = BattleUnit.UnitStatus.STATUS_TURNING;
	}
}

/**
 * Look at a direction.
 * @param direction.
 */
public void lookAt(int direction)
{
	_toDirection = direction;
	_status = BattleUnit.UnitStatus.STATUS_TURNING;
}

/**
 * Turn.
 */
public void turn()
{
    int a = _toDirection - _direction;
    if (a != 0) {
        if (a > 0) {
            if (a <= 4) {
                _direction++;
            } else {
                _direction--;
            }
        } else {
            if (a > -4) {
                _direction--;
            } else {
                _direction++;
            }
        }
        if (_direction < 0) _direction = 7;
        if (_direction > 7) _direction = 0;
		_cached = false;
    }

	if (_toDirection == _direction)
	{
		// we officially reached our destination
		_status = BattleUnit.UnitStatus.STATUS_STANDING;
	}
}

public void abortTurn()
{
	_status = BattleUnit.UnitStatus.STATUS_STANDING;
}

/**
 * Returns the unit's faction.
 * @return Faction.
 */
public final UnitFaction getFaction()
{
	return _faction;
}

/**
 * Sets the unit's cache flag.
 * @param cached
 */
public void setCached(boolean cached)
{
	_cached = cached;
}

/**
 * Check if the unit is still cached in the Map cache.
 * When the unit changes it needs to be re-cached.
 * @return boolean
 */
public final boolean isCached()
{
	return _cached;
}

/**
 * Kneel down and spend TUs.
 * @param to kneel or to stand up
 */
public void kneel(boolean kneeled)
{
	_kneeled = kneeled;
	setCached(false);
}

/**
 * Is kneeled down?
 * @return true/false
 */
public final boolean isKneeled()
{
	return _kneeled;
}

/**
 * Aim. (shows the right hand sprite and weapon holding)
 * @param aiming
 */
public void aim(boolean aiming)
{
	if (aiming)
		_status = BattleUnit.UnitStatus.STATUS_AIMING;
	else
		_status = BattleUnit.UnitStatus.STATUS_STANDING;

	setCached(false);
}

/**
 * Returns the soldier's amount of time units.
 * @return Time units.
 */
public final int getTimeUnits()
{
	return _tu;
}

/**
 * Returns the soldier's amount of stamina.
 * @return Stamina.
 */
public final int getEnergy()
{
	return _energy;
}

/**
 * Returns the soldier's amount of health.
 * @return Health.
 */
public final int getHealth()
{
	return _health;
}

/**
 * Returns the soldier's amount of bravery.
 * @return Bravery.
 */
public final int getMorale()
{
	return _morale;
}

/**
 * Do an amount of damage.
 * @param position
 * @param power
 */
public void damage(Position position, int power)
{
	int damage;
	UnitSide side;
	int impactheight, x=8, y=8;
	UnitBodyPart bodypart = null;
	// todo : determine direction we got hit (4 directions)
	// todo : armor reduction
	// todo : fatal wounds

	if (position == new Position(0, 0, 0))
	{
		side = BattleUnit.UnitSide.SIDE_UNDER;
	}
	else
	{
		// normalize x and y
		switch(_direction)
		{
		case 0: // heading north, all is the same
			x = position.x;
			y = position.y;
			break;
		case 1: // somewhere in between 0 and 2
			x = (position.x + (15 - position.y))/2;
			y = (position.y + position.x)/2;
			break;
		case 2: // heading east
			x = 15 - position.y;
			y = position.x;
			break;
		case 3:
			x = ((15 - position.y) + (15 - position.x))/2;
			y = (position.x + (15 - position.y))/2;
			break;
		case 4: // heading south, both axis inversed
			x = 15 - position.x;
			y = 15 - position.y;
			break;
		case 5:
			x = ((15 - position.x) + position.y)/2;
			y = ((15 - position.y) + (15 - position.x))/2;
			break;
		case 6: // heading west
			x = position.y;
			y = 15 - position.x;
			break;
		case 7:
			x = (position.y + position.x)/2;
			y = ((15 - position.x) + position.y)/2;
			break;
		}
		// determine side
		if (y > 9)
			side = BattleUnit.UnitSide.SIDE_FRONT;
		else if (y < 6)
			side = BattleUnit.UnitSide.SIDE_REAR;
		else if (x < 6)
			side = BattleUnit.UnitSide.SIDE_LEFT;
		else if (x > 9)
			side = BattleUnit.UnitSide.SIDE_RIGHT;
		else
			side = BattleUnit.UnitSide.SIDE_FRONT;
	}

	impactheight = 10*position.z/(isKneeled()?_unit.getKneelHeight():_unit.getStandHeight());

	if (impactheight > 4 && impactheight < 7) // torso
	{
		if (side == BattleUnit.UnitSide.SIDE_LEFT)
		{
			bodypart = BattleUnit.UnitBodyPart.BODYPART_LEFTARM;
		}else if (side == BattleUnit.UnitSide.SIDE_RIGHT)
		{
			bodypart = BattleUnit.UnitBodyPart.BODYPART_RIGHTARM;
		}else
		{
			bodypart = BattleUnit.UnitBodyPart.BODYPART_TORSO;
		}
	}else if (impactheight >= 7) //head
	{
		bodypart = BattleUnit.UnitBodyPart.BODYPART_HEAD;
	}else if (impactheight <=4) //legs
	{
		if (side == BattleUnit.UnitSide.SIDE_LEFT || side == BattleUnit.UnitSide.SIDE_FRONT)
		{
			bodypart = BattleUnit.UnitBodyPart.BODYPART_LEFTLEG;
		}else
		{
			bodypart = BattleUnit.UnitBodyPart.BODYPART_RIGHTLEG;
		}
	}

	damage = (power - getArmor(side));

	if (damage > 0)
	{
		// fatal wounds
		_fatalWounds[bodypart.id] += RNG.generate(1,3);
		// armor damage
		setArmor(getArmor(side) - (damage+5)/10, side);
		// health damage
		_health -= damage;
		if (_health < 0)
			_health = 0;
	}
}

/**
 * Returns the soldier's amount of bravery.
 * @return Bravery.
 */
public void startFalling()
{
	_status = BattleUnit.UnitStatus.STATUS_FALLING;
	_fallPhase = 0;
	setCached(false);
}

/**
 * Returns the soldier's amount of bravery.
 * @return Bravery.
 */
public void keepFalling()
{
	_fallPhase++;
	if (_fallPhase == 3)
	{
		_fallPhase = 2;
		if (_health == 0)
			_status = BattleUnit.UnitStatus.STATUS_DEAD;
		else
			_status = BattleUnit.UnitStatus.STATUS_UNCONSCIOUS;
	}
	setCached(false);
}


/**
 * Returns the soldier's amount of bravery.
 * @return Bravery.
 */
public final int getFallingPhase()
{
	return _fallPhase;
}

/**
 * Returns whether the soldier is out of combat, dead or unconscious.
 * @return flag if out or not.
 */
public final boolean isOut()
{
	return _status == BattleUnit.UnitStatus.STATUS_DEAD || _status == BattleUnit.UnitStatus.STATUS_UNCONSCIOUS;
}

/**
 * Spend time units if it can. Return false if it can't.
 * @param tu
 * @param debugmode If this is true, the function actually does noting.
 * @return flag if it could spend the time units or not.
 */
public boolean spendTimeUnits(int tu, boolean debugmode)
{
	if (debugmode) return true;

	if (tu <= _tu)
	{
		_tu -= tu;
		return true;
	}
	else
	{
		return false;
	}
}

/**
 * Set a specific number of timeunits.
 * @param tu
 */
public void setTimeUnits(int tu)
{
	_tu = tu;
}

/**
 * Add this unit to the list of visible units. Returns true if this is a new one.
 * @param unit
 */
public boolean addToVisibleUnits(BattleUnit unit)
{
	for (BattleUnit i: _visibleUnits)
	{
		if ((BattleUnit)(i) == unit)
		{
			return false;
		}
	}
	_visibleUnits.add(unit);
	return true;
}

public Vector<BattleUnit> getVisibleUnits()
{
	return _visibleUnits;
}

/**
 * Clear visible units.
 */
public void clearVisibleUnits()
{
	_visibleUnits.clear();
}

/**
 * Calculate firing accuracy.
 * Formula = accuracyStat * weaponAccuracy * kneelingbonus(1.15) * one-handPenalty(0.8) * woundsPenalty(% health) * critWoundsPenalty (-10%/wound)
 * @param weaponAccuracy
 * @return firing Accuracy
 */
public double getFiringAccuracy(int weaponAccuracy)
{
	double result = (double)(_unit.getFiringAccuracy()/100.0);

	result *= (double)(weaponAccuracy/100.0);

	if (_kneeled)
		result *= 1.15;

	result *= ((double)_health/(double)_unit.getHealth());

	return result;
}

/**
 * Calculate throwing accuracy.
 * Formula = accuracyStat * woundsPenalty(% health) * critWoundsPenalty (-10%/wound)
 * @param weaponAccuracy
 * @return firing Accuracy
 */
public double getThrowingAccuracy()
{
	double result = (double)(_unit.getFiringAccuracy()/100.0);

	result *= ((double)_health/(double)_unit.getHealth());

	return result;
}

/**
 * Set the armor value of a certain armor side.
 * @param armor Amount of armor.
 * @param side The side of the armor.
 */
public void setArmor(int armor, UnitSide side)
{
	if (armor < 0)
	{
		armor = 0;
	}
	_armor[side.id] = armor;
}

/**
 * Get the armor value of a certain armor side.
 * @param side The side of the armor.
 * @return Amount of armor.
 */
public int getArmor(UnitSide side)
{
	return _armor[side.id];
}

/**
 * Get total amount of fatal wounds this unit has.
 * @return Number of fatal wounds.
 */
public int getFatalWounds()
{
	int sum = 0;
	for (int i = 0; i < 6; i++)
		sum += _fatalWounds[i];
	return sum;
}

}
