package com.benz.beneathskies.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.benz.beneathskies.BodyEditorLoader;
import com.benz.beneathskies.actors.objects.LightSpot;

import java.util.ArrayList;

import box2dLight.RayHandler;

import static com.benz.beneathskies.tools.Constants.PPM;
import static com.benz.beneathskies.tools.UserDatas.landingPad;

/**
 * Created by amineBenz on 28/03/2016.
 * the landing pad, is an entity that detects if a hovercraft is on it for many reasons,
 * like winning the game or getting supplies or changing the hover
 */
public class LandingPad {

	TextureRegion texture;
	Body body;
	Vector2 position;
	Vector2 texturePosition;

	ArrayList<LightSpot> coloredLights = new ArrayList<LightSpot>();
	ArrayList<LightSpot> blueLights = new ArrayList<LightSpot>();
	ArrayList<LightSpot> orangeLights = new ArrayList<LightSpot>();

	RayHandler rayHandler;


	public LandingPad(World world, Vector2 position2, RayHandler rayHandler){

		this.rayHandler = rayHandler;

		texture = new TextureRegion(new Texture("landingPadGreenLight.png"));

		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("landingpad.json"));

		//definition objects
		BodyDef bDef = new BodyDef();
		FixtureDef fDef = new FixtureDef();
		FixtureDef sensorF = new FixtureDef();
		sensorF.isSensor = true;

		bDef.type = BodyDef.BodyType.StaticBody;
		bDef.position.set(position2);
		fDef.friction = .8f;
		fDef.density = .9f;
		fDef.restitution = .2f;

		//creating body
		body = world.createBody(bDef);
		body.setUserData(landingPad);

		//setting position
		position = new Vector2((float) body.getPosition().x,(float) body.getPosition().y);
		texturePosition= new Vector2((float) position.x-texture.getRegionWidth()/PPM/2,(float) position.y-texture.getRegionHeight()/PPM/2);

		//creating the fixture by the body loader class
		loader.attachFixture(body,"landingpad",fDef, 5.99f);

		//creating landing sensor
		loader.attachFixture(body,"landingsensor", sensorF, 5.99f);

		//getting colored light positions
			//



	}

	public void update(float delta){
		position.set(body.getPosition().x, body.getPosition().y);
		texturePosition.set(position.x-texture.getRegionWidth()/PPM/2, position.y-texture.getRegionHeight()/PPM/2);
	}

	public void render(SpriteBatch batch){
		float width = texture.getRegionWidth()/PPM;
		float height = texture.getRegionHeight()/PPM;

		//drawing
		batch.draw(texture, texturePosition.x, texturePosition.y, width/2 , height/2 , width , height, 1, 1,0);
	}


}
