package com.benz.beneathskies.events;

/**
 * Created by amineBenz on 29/03/2016.
 * this interface represents all of the events possible in the game, each has a boolean handle method,
 * to check if handled or not, and the constructor is different for each one to build the logic of the event,
 * events are placed in a list in the level logic, where in each method they are checked.
 */
public interface Event {

	public boolean handled(float delta);
}
