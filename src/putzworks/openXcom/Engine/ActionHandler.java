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


// trying to translate this line into Java

// typedef State &(State::*ActionHandler)(Action*);
// Note: typedef is just way of using another name for a class, so I think this is trying to
// say that ActionHandler is just another way of saying "a method on the State class"


public interface ActionHandler {
	/**
	 * Action handling method.
	 * @param action
	 */
	public void handle(Action action);

}
