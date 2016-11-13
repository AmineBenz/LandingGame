package com.benz.beneathskies.actors.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import box2dLight.PointLight;
import box2dLight.RayHandler;

/**
 * Created by amineBenz on 28/03/2016.
 */
public class LightSpot {

	Vector2 position;
	PointLight light;

	public LightSpot(RayHandler rayHandler, Color color, Vector2 position, Body body){
		this.position = position;
		light = new PointLight(rayHandler, 40, color, .7f, position.x, position.y);
		light.setXray(true);
		light.setSoft(true);
		light.attachToBody(body);
	}

	//constructor with more details


	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public PointLight getLight() {
		return light;
	}

	public void setLight(PointLight light) {
		this.light = light;
	}
}
