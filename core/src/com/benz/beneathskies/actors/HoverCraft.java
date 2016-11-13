package com.benz.beneathskies.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.benz.beneathskies.BodyEditorLoader;
import com.benz.beneathskies.events.PlayerShipDestroyed;
import com.benz.beneathskies.tools.Level;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import static com.benz.beneathskies.tools.Constants.PPM;
import static com.benz.beneathskies.tools.Constants.DEG_TO_RAD;
import static com.benz.beneathskies.tools.Constants.defaultHealth;
import static com.benz.beneathskies.tools.Constants.maximumDamage;
import static com.benz.beneathskies.tools.Constants.minimumDamage;
import static com.benz.beneathskies.tools.UserDatas.hoverCraftData;
import static com.benz.beneathskies.tools.Constants.PIby4;

/**
 * Created by amineBenz on 07/03/2016.
 * hoverCrafts are the playable mechs on the game
 */
public class HoverCraft {

	//time to recover when started, ships don't get damage
	float recoverTime = 0;

	//spaceShip stats
	float health; // health of the space ship

	TextureRegion currentFrame; // current frame to be drawn
	Body body; //the body in the Box2d world
	Fixture fixture;
	Vector2 position; // position of the body
	Vector2 texturePosition; // position of the texture to be drawn, calculated relatively from the body position using the origin


	//Animation tools
	float stateTime=0; //state time used to draw the animations
	float boostIndicator = 0; // state time used to draw intermediate boost animation

	Animation idleAnimation; // animation when engine is ON
	Animation pulseAnimation; // animation when pulse is in its max
	Animation boostAnimation; // animation when passing from idle to pulse

	TextureAtlas animationsAtlas; //these need to be regrouped into one atlas for all of the ship
	TextureAtlas pulseAnimationAtlas;
	TextureAtlas boostAnimationAtlas;

	//stores the angle towards which the body is facing
	float bodyAngle = 0;

	//debug lights
	RayHandler rayHandler; // reference
	PointLight pointLight;

	//parent references
	Level level;


	public HoverCraft(TiledMap map, World world, RayHandler rayHandler, Level level){

		//getting references
		health = defaultHealth;
		this.level = level;
		//the lights of the hover, should be referenced instead of instantiated here
		this.rayHandler = rayHandler;

		//TODO : put on a separate method (create animation)
		//getting the currentFrame, should be done from an atlas instead, and currentFrame also
		animationsAtlas = new TextureAtlas("idlehoverframes.atlas");
		TextureRegion [] idleAnimationTextures = new TextureRegion[4];
		idleAnimationTextures[0] = animationsAtlas.findRegion("idlehoverframe1");
		idleAnimationTextures[1] = animationsAtlas.findRegion("idlehoverframe2");
		idleAnimationTextures[2] = animationsAtlas.findRegion("idlehoverframe3");
		idleAnimationTextures[3] = animationsAtlas.findRegion("idlehoverframe2");

		pulseAnimationAtlas = new TextureAtlas("pushinghoverframes.atlas");
		TextureRegion[] pulseAnimationTextures = new TextureRegion[4];
		pulseAnimationTextures[0] = pulseAnimationAtlas.findRegion("pushinghoverframe1");
		pulseAnimationTextures[1] = pulseAnimationAtlas.findRegion("pushinghoverframe2");
		pulseAnimationTextures[2] = pulseAnimationAtlas.findRegion("pushinghoverframe3");
		pulseAnimationTextures[3] = pulseAnimationAtlas.findRegion("pushinghoverframe2");

		boostAnimationAtlas = new TextureAtlas("boosthoverframes.atlas");
		TextureRegion[] boostAnimationTextures = new TextureRegion[4];
		boostAnimationTextures[0] = boostAnimationAtlas.findRegion("boosthoverframe1");
		boostAnimationTextures[1] = boostAnimationAtlas.findRegion("boosthoverframe2");
		boostAnimationTextures[2] = boostAnimationAtlas.findRegion("boosthoverframe3");
		boostAnimationTextures[3] = boostAnimationAtlas.findRegion("boosthoverframe4");

		idleAnimation = new Animation(0.15f, idleAnimationTextures);
		pulseAnimation = new Animation(0.1f, pulseAnimationTextures);
		boostAnimation = new Animation(0.2f, boostAnimationTextures);
		currentFrame = idleAnimation.getKeyFrame(Gdx.graphics.getDeltaTime());

		//body loader the load the body of the hover
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("hoverbody.json"));

		//definition objects
		BodyDef bDef = new BodyDef();
		FixtureDef fDef = new FixtureDef();
		FixtureDef sensorF = new FixtureDef();

		//creating the object
		for (MapObject mapObject : map.getLayers().get("player").getObjects().getByType(RectangleMapObject.class)){

			//getting the coordinates of the object
			Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

			//defining the body
			bDef.type = BodyDef.BodyType.DynamicBody;
			bDef.position.set(((rectangle.getX()+rectangle.getWidth()/2)/PPM) , ((rectangle.getY()+rectangle.getHeight()/2)/PPM) );
			fDef.friction = .8f;
			fDef.density = .7f;
			fDef.restitution = .2f;
			//sensorF.isSensor = true;

			//creating body
			body = world.createBody(bDef);
			body.setUserData(hoverCraftData);

			//creating the fixture by the body loader class
			loader.attachFixture(body,"hoverbody",fDef, 2.99f);

			//TODO : put this sensor as the sensor of landing not the hole body
			//creating landing sensor fixture
			sensorF.isSensor = true;
			loader.attachFixture(body,"landingsensor",sensorF, 2.99f);
		}
		//setting up positions
		position = new Vector2((float) body.getPosition().x,(float) body.getPosition().y);
		texturePosition= new Vector2((float) position.x- currentFrame.getRegionWidth()/PPM/2,(float) position.y- currentFrame.getRegionHeight()/PPM/2);

		//defining the lights
		pointLight = new PointLight(this.rayHandler, 40, Color.LIGHT_GRAY, 2f, position.x, position.y);
		pointLight.setSoft(true);
		pointLight.setXray(true);
	}

	/**
	 * updates the state of the hover, position, texturePosition, hovers based on angle, light position
	 * @param delta
	 */
	public boolean update(float delta){

		boostIndicator -=delta; // min 1 here plus 2 on pushing up

		if (recoverTime<1f) recoverTime +=delta; //refreshing the shield
		System.out.println(" recover time =====> " + recoverTime);

		//TODO : verify if player or not, every ship can be destroyed
		//check if the ship is destroyed, throw an event that the player is destroyed
		if (health<=0)
			level.recieveEvent(new PlayerShipDestroyed(level));

		//getting the new position and texture position, the condition is that body and all textures should have the same origin
		position.set(body.getPosition().x, body.getPosition().y);
		texturePosition.set(position.x- currentFrame.getRegionWidth()/PPM/2, position.y- currentFrame.getRegionHeight()/PPM/2);

		//Deleting 2 PIs on each sides of the angle
		//calling the hover method to maintain horizontal position
		bodyAngle = body.getAngle();
		hover(delta,bodyAngle);

		pointLight.setPosition(position);

		//update frame time
		stateTime += delta;
		return true; // don't know why
	}

	/**
	 * draws the appropriate currentFrame on its position, scales it by PPM
	 * @param batch
	 * @param camera
	 * @param delta
	 */
	public void render(SpriteBatch batch, OrthographicCamera camera, float delta){
		//getting the current texture
		chooseCurrentFrame();

		//to get the scale and origin
		float width = currentFrame.getRegionWidth()/PPM;
		float height = currentFrame.getRegionHeight()/PPM;

		//drawing
		batch.draw(currentFrame, texturePosition.x, texturePosition.y, width/2 , height/2 , width , height, 1, 1,bodyAngle*DEG_TO_RAD);
	}


	/**
	 * decides which frame to be rendered, the chosen is referenced on currentFrame
	 */
	private void chooseCurrentFrame(){
		System.out.println(" ***********************************======> BoostTime: "+boostIndicator+" / stateTime : "+stateTime);
		if (boostIndicator<=0){
			boostIndicator =0;
			currentFrame = idleAnimation.getKeyFrame(stateTime, true);
		}else
		if (boostIndicator >=.8f){
			boostIndicator = .8f;
			currentFrame = pulseAnimation.getKeyFrame(stateTime, true);
		}
		else{
			stateTime =0;
			currentFrame = boostAnimation.getKeyFrame(boostIndicator, true);
		}
	}


	/**
	 * pushes the hover up by a force (should be on the hover properties)
	 */
	public void pushUp(){
		body.applyLinearImpulse(new Vector2(0,.5f), body.getWorldCenter(), true);
		boostIndicator += (2*Gdx.graphics.getDeltaTime());
		//body.applyLinearImpulse(new Vector2(0,.5f), origin , true);
	}

	/**
	 * hovers the hover right by a force (should be on the hover properties)
	 */
	public void hoverRight(float force){
		body.applyLinearImpulse(new Vector2(force,0), body.getWorldCenter(), true);
		//body.applyTorque(-0.3f, true);
		float nextAngle = bodyAngle + (body.getAngularVelocity() * 2/3);
		body.applyTorque(-PIby4-nextAngle, true);
	}

	/**
	 * hovers the hover left by a force (should be on the hover properties)
	 */
	public void hoverLeft(float force){
		body.applyLinearImpulse(new Vector2(-force,0), body.getWorldCenter(), true);
		//body.applyTorque(0.3f, true);
		float nextAngle = bodyAngle + (body.getAngularVelocity() * 2/3);
		body.applyTorque(PIby4-nextAngle, true);
	}

	/**
	 * gets the hover to always be facing up means angle must be 0
	 * @param delta
	 * @param angle
	 */
	public void hover(float delta, float angle){
		if (angle>6.28f || angle<-6.28f)
			body.setTransform(position, 0);
		float nextAngle = angle + (body.getAngularVelocity() * 2/3);
		if (angle<0 && angle> 3.14){
			body.applyTorque(-nextAngle,true);
		}
		else
			body.applyTorque(-nextAngle,true);
	}


	/**
	 * verifies if the ship is properly landing
	 * @param delta
	 * @return
	 */
	public boolean verifyLanding(float delta){
		if ((body.getLinearVelocity().y ==0) && ((bodyAngle < 1f)&&(bodyAngle> -1f))){
			return true;
		}
		return false;
	}


	/**
	 * gets a vector of impact and one for collision normal and collects the damage and the subs the health value
	 * @param impact
	 * @param normal
	 * @return
	 */
	public float recieveDamage(Vector2 impact, Vector2 normal){
		if (recoverTime<1f) return 0f;
		impact.scl(normal);
		float damage = impact.len2();
		System.out.println("result :" + impact + "/ --> damage = " + damage);
		if (damage>minimumDamage && damage<maximumDamage){
			//inflict Damage
			health -= damage;
		}

		return damage;
	}

	//GETTERS AND SETTERS

	public Vector2 getPosition() {
		return position;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
}
