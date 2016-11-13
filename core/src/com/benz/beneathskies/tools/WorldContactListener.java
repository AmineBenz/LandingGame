package com.benz.beneathskies.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.benz.beneathskies.events.PlayerShipHitsSomething;
import com.benz.beneathskies.events.PlayerShipOnLandingPad;
import com.benz.beneathskies.events.PlayerShipLeftLandingPad;

import static com.benz.beneathskies.tools.UserDatas.hoverCraftData;
import static com.benz.beneathskies.tools.UserDatas.landingPad;

/**
 * Created by amineBenz on 27/03/2016.
 * this class if given to a world it will listen for collision events and generate command classes that
 * the logic level will execute
 */
public class WorldContactListener implements ContactListener {

	Level level;
	String a,b;

	public WorldContactListener(Level level){
		this.level = level;
	}

	/**
	 * Called when two fixtures begin to touch.
	 *
	 * @param contact
	 */
	@Override
	public void beginContact(Contact contact) {
		a = (String) contact.getFixtureA().getBody().getUserData();
		b = (String) contact.getFixtureB().getBody().getUserData();
		if (((a == hoverCraftData) && (b == landingPad)) || ((a == hoverCraftData) && (b == landingPad))){
			level.recieveEvent(new PlayerShipOnLandingPad(level.getPlayerHoverCraft(),level));
		}

		//ships get hitting damage
		if (a==hoverCraftData){
			WorldManifold contactManifold = contact.getWorldManifold();
			Vector2 normal = contactManifold.getNormal();
			Vector2 impact1 = contact.getFixtureB().getBody().getLinearVelocity();
			Vector2 impact2 = contact.getFixtureA().getBody().getLinearVelocity();
			Vector2 impact = impact1.add(impact2);
			System.out.println("normal :" + normal + " /impact : "+ impact + " otherImpact : "+ contact.getFixtureA().getBody().getLinearVelocity());
			level.recieveEvent(new PlayerShipHitsSomething(impact, normal, level));
		}
	}

	/**
	 * Called when two fixtures cease to touch.
	 *
	 * @param contact
	 */
	@Override
	public void endContact(Contact contact) {
		a = (String) contact.getFixtureA().getBody().getUserData();
		b = (String) contact.getFixtureB().getBody().getUserData();
		if (((a == hoverCraftData) && (b == landingPad)) || ((a == hoverCraftData) && (b == landingPad))){
			level.recieveEvent(new PlayerShipLeftLandingPad(level));
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}
