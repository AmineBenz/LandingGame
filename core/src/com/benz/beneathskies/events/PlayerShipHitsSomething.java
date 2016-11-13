package com.benz.beneathskies.events;

import com.badlogic.gdx.math.Vector2;
import com.benz.beneathskies.tools.Level;

/**
 * Created by amineBenz on 31/03/2016.
 */
public class PlayerShipHitsSomething implements Event {

	float damage = 0;
	
	public PlayerShipHitsSomething(Vector2 impact, Vector2 normal, Level level){
		damage = level.getPlayerHoverCraft().recieveDamage(impact, normal);
		System.out.println("----> damage = [ "+damage+" ]");
	}
	
	@Override
	public boolean handled(float delta) {
		return true;
	}
}
