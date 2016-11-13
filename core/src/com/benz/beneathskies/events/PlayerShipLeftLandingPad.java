package com.benz.beneathskies.events;

import com.benz.beneathskies.tools.Level;

import java.util.ArrayList;

/**
 * Created by amineBenz on 29/03/2016.
 */
public class PlayerShipLeftLandingPad implements Event {
	Level level;
	public PlayerShipLeftLandingPad(Level level){
		this.level = level;
		level.removeEventsByType(PlayerShipOnLandingPad.class.getSimpleName());
	}

	@Override
	public boolean handled(float delta) {

		level.removeEventsByType(PlayerShipOnLandingPad.class.getSimpleName());
		return true;
	}
}
