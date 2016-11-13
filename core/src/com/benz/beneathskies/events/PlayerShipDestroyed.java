package com.benz.beneathskies.events;

import com.benz.beneathskies.tools.Level;

/**
 * Created by amineBenz on 01/04/2016.
 */
public class PlayerShipDestroyed implements Event {

	Level level;

	public PlayerShipDestroyed(Level level){
		this.level = level;
	}


	@Override
	public boolean handled(float delta) {
		level.setGameOver(true);
		return true;
	}
}
