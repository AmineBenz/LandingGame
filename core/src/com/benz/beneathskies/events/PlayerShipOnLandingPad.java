package com.benz.beneathskies.events;

import com.benz.beneathskies.actors.HoverCraft;
import com.benz.beneathskies.tools.Level;

/**
 * Created by amineBenz on 29/03/2016.
 */
public class PlayerShipOnLandingPad implements Event {

	float landingTime = 0f;
	HoverCraft hoverCraft;
	Level level;

	public PlayerShipOnLandingPad(HoverCraft hoverCraft, Level level){
		this.hoverCraft = hoverCraft;
		this.level = level;
		//level.removeEventsByType(this.getClass().getSimpleName());
		//level.recieveEvent(this);
	}

	@Override
	public boolean handled(float delta) {
		if (hoverCraft.verifyLanding(delta)) {
			landingTime += delta;
			System.out.println(landingTime);
			if (landingTime>.1f){
				level.wonGame();
				return true;
			}
			return false;
		}
		return false;
	}
}
