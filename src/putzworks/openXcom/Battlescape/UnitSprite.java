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
package putzworks.openXcom.Battlescape;

import putzworks.openXcom.Engine.Surface;
import putzworks.openXcom.Engine.SurfaceSet;
import putzworks.openXcom.Savegame.BattleItem;
import putzworks.openXcom.Savegame.BattleUnit;
import putzworks.openXcom.Savegame.Soldier;

public class UnitSprite extends Surface
{
	private BattleUnit _unit;
	private BattleItem _item;
	private SurfaceSet _unitSurface;
	private SurfaceSet _itemSurface;

/**
 * Sets up a UnitSprite with the specified size and position.
 * @param width Width in pixels.
 * @param height Height in pixels.
 * @param x X position in pixels.
 * @param y Y position in pixels.
 */
public UnitSprite(int width, int height, int x, int y) 
{
	super(width, height, x, y);
	_unit = null; 
	_item = null;

}


/**
 * Changes the pack for the UnitSprite to get resources for rendering.
 * @param res Pointer to the resource pack.
 */
public void setSurfaces(SurfaceSet unitSurface, SurfaceSet itemSurface)
{
	_unitSurface = unitSurface;
	_itemSurface = itemSurface;
}

/**
 * Links this sprite to a BattleUnit to get the data for rendering.
 * @param unit Pointer to the BattleUnit.
 */
public void setBattleUnit(BattleUnit unit)
{
	_unit = unit;
}

/**
 * Links this sprite to a BattleItem to get the data for rendering.
 * @param unit Pointer to the BattleItem.
 */
public void setBattleItem(BattleItem item)
{
	_item = item;
}

/**
 * Draws a unit, using the drawing rules of the unit.
 * This function is called by Map, for each unit on the screen.
 */
public void draw()
{
	Surface torso = null, legs = null, leftArm = null, rightArm = null, item = null;

	// magic numbers
	int maleTorso = 32, femaleTorso = 267, legsStand = 16, legsKneel = 24, die = 264;
	int larmStand = 0, rarmStand = 8, rarm1H = 232, larm2H = 240, ramr2H = 248, rarmShoot = 256;
	int legsWalk[] = { 56, 56+24, 56+24*2, 56+24*3, 56+24*4, 56+24*5, 56+24*6, 56+24*7 };
	int larmWalk[] = { 40, 40+24, 40+24*2, 40+24*3, 40+24*4, 40+24*5, 40+24*6, 40+24*7 };
	int rarmWalk[] = { 48, 48+24, 48+24*2, 48+24*3, 48+24*4, 48+24*5, 48+24*6, 48+24*7 };
	int yoffWalk[] = {1, 0, -1, 0, 1, 0, -1, 0};
	int offX[] = { 8, 10, 7, 4, -9, -11, -7, -3 };
	int offY[] = { -6, -3, 0, 2, 0, -4, -7, -9 };
	int offYKneel = 4;

	clear();
	Soldier soldier = (Soldier)(_unit.getUnit());

	if (_unit.isOut())
	{
		// unit is drawn as an item
		return;
	}

	if (_unit.getStatus() == BattleUnit.UnitStatus.STATUS_FALLING)
	{
		torso = _unitSurface.getFrame(die + _unit.getFallingPhase());
		torso.blit(this);
		return;
	}

	if (soldier != null && soldier.getGender() == Soldier.SoldierGender.GENDER_FEMALE)
	{
		torso = _unitSurface.getFrame(femaleTorso + _unit.getDirection());
	}
	else
	{
		torso = _unitSurface.getFrame(maleTorso + _unit.getDirection());
	}

	// when walking, torso(fixed sprite) has to be animated up/down
	if (_unit.getStatus() == BattleUnit.UnitStatus.STATUS_WALKING)
	{
		torso.setY(yoffWalk[_unit.getWalkingPhase()]);
		legs = _unitSurface.getFrame(legsWalk[_unit.getDirection()] + _unit.getWalkingPhase());
		leftArm = _unitSurface.getFrame(larmWalk[_unit.getDirection()] + _unit.getWalkingPhase());
		rightArm = _unitSurface.getFrame(rarmWalk[_unit.getDirection()] + _unit.getWalkingPhase());
	}
	else
	{
		if (_unit.isKneeled())
		{
			legs = _unitSurface.getFrame(legsKneel + _unit.getDirection());
		}
		else
		{
			legs = _unitSurface.getFrame(legsStand + _unit.getDirection());
		}
		leftArm = _unitSurface.getFrame(larmStand + _unit.getDirection());
		rightArm = _unitSurface.getFrame(rarmStand + _unit.getDirection());
	}

	// holding an item
	if (_item != null)
	{
		// draw handob item
		if (_unit.getStatus() == BattleUnit.UnitStatus.STATUS_AIMING)
		{
			int dir = (_unit.getDirection() + 2)%8;
			item = _itemSurface.getFrame(_item.getRules().getHandSprite() + dir);
			item.setX(offX[_unit.getDirection()]);
			item.setY(offY[_unit.getDirection()]);
		}
		else
		{
			item = _itemSurface.getFrame(_item.getRules().getHandSprite() + _unit.getDirection());
			item.setX(0);
			item.setY(0);
		}

		// draw arms holding the item
		if (_item.getRules().getTwoHanded())
		{
			leftArm = _unitSurface.getFrame(larm2H + _unit.getDirection());
			if (_unit.getStatus() == BattleUnit.UnitStatus.STATUS_AIMING)
			{
				rightArm = _unitSurface.getFrame(rarmShoot + _unit.getDirection());
			}
			else
			{
				rightArm = _unitSurface.getFrame(ramr2H + _unit.getDirection());
			}
		}
		else
		{
			rightArm = _unitSurface.getFrame(rarm1H + _unit.getDirection());
		}

		// the fixed arm(s) have to be animated up/down when walking
		if (_unit.getStatus() == BattleUnit.UnitStatus.STATUS_WALKING)
		{
			item.setY(yoffWalk[_unit.getWalkingPhase()]);
			rightArm.setY(yoffWalk[_unit.getWalkingPhase()]);
			if (_item.getRules().getTwoHanded())
				leftArm.setY(yoffWalk[_unit.getWalkingPhase()]);
		}
	}

	// offset everything but legs when kneeled
	if (_unit.isKneeled())
	{
		leftArm.setY(offYKneel);
		rightArm.setY(offYKneel);
		torso.setY(offYKneel);
		item?item.setY(item.getY() + offYKneel):void();
	}
	else if (_unit.getStatus() != BattleUnit.UnitStatus.STATUS_WALKING)
	{
		leftArm.setY(0);
		rightArm.setY(0);
		torso.setY(0);
	}
	
	// blit order depends on unit direction
	switch (_unit.getDirection())
	{
	case 0: leftArm.blit(this); legs.blit(this); item?item.blit(this):public void(); torso.blit(this); rightArm.blit(this); break;
	case 1: leftArm.blit(this); legs.blit(this); torso.blit(this); item?item.blit(this):public void(); rightArm.blit(this); break;
	case 2: leftArm.blit(this); legs.blit(this); torso.blit(this); item?item.blit(this):public void(); rightArm.blit(this); break;
	case 3: legs.blit(this); torso.blit(this); leftArm.blit(this); rightArm.blit(this); item?item.blit(this):public void(); break;
	case 4: rightArm.blit(this); legs.blit(this); torso.blit(this); leftArm.blit(this); item?item.blit(this):public void(); break;
	case 5: rightArm.blit(this); legs.blit(this); item?item.blit(this):public void(); torso.blit(this); leftArm.blit(this); break;
	case 6: rightArm.blit(this); legs.blit(this); item?item.blit(this):public void(); torso.blit(this); leftArm.blit(this); break;
	case 7: item?item.blit(this):public void(); leftArm.blit(this); rightArm.blit(this); legs.blit(this); torso.blit(this); break;
	}
}

/**
 * Blits the unit onto another surface. 
 * @param surface Pointer to another surface.
 */
public void blit(Surface surface)
{
	super.blit(surface);
}

}
