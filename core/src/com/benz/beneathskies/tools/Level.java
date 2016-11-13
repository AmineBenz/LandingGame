package com.benz.beneathskies.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.benz.beneathskies.actors.HoverCraft;
import com.benz.beneathskies.actors.LandingPad;
import com.benz.beneathskies.commands.Command;
import com.benz.beneathskies.commands.doNothingCommand;
import com.benz.beneathskies.commands.hoverLeftCommand;
import com.benz.beneathskies.commands.hoverRightCommand;
import com.benz.beneathskies.commands.pushUpCommand;
import com.benz.beneathskies.events.Event;
import com.benz.beneathskies.events.PlayerShipOnLandingPad;
import com.benz.beneathskies.screens.GameScreen;
import com.benz.beneathskies.screens.uiTools.PlayHud;

import java.util.ArrayList;

import box2dLight.RayHandler;

import static com.benz.beneathskies.tools.Constants.PPM;
import static com.benz.beneathskies.tools.Constants.hoverForce;
import static com.benz.beneathskies.tools.UserDatas.wallData;

/**
 * Created by amineBenz on 07/03/2016.
 * a class that stores objects of a level, also save and loads levels from text files
 */
public class Level {

	boolean gameOver = false;


	GameScreen gameScreen;


	RayHandler rayHandler;

	OrthographicCamera gameCam;

	World world;
	TiledMap map;
	HoverCraft playerHoverCraft;
	ArrayList<LandingPad> landingPads;
	ArrayList<HoverCraft> ships;

	WorldContactListener worldContactListener;

	/**
	 * an array storing all of the events currently on the scene
	 */
	ArrayList<Event> eventList = new ArrayList<Event>();
	ArrayList<Event> eventsToRemove = new ArrayList<Event>();

	//the hud for the play screen
	PlayHud hud;


	public Level(GameScreen gameScreen){
		this.gameScreen = gameScreen;Gdx.app.log("TEST TEST", "levelou createdou");
		this.gameCam = gameScreen.getGameCam();
		hud = new PlayHud(this);
	}



	/**
	 * method that generates the level from a text file (xml? json? dunno)
	 * @param name
	 */
	public void loadLevel(String name){

	}

	/**
	 * method that saves the current level into a text file
	 * @param name
	 */
	public void saveLavel(String name){

	}


	/**
	 * debug method that creates a level for debugging
	 */
	public void createDebugLevel(){
		world = new World(new Vector2(0f,-9.8f),true);
		worldContactListener = new WorldContactListener(this);
		world.setContactListener(worldContactListener);
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(.05f);
		rayHandler.setShadows(false);
		TmxMapLoader mapLoader = new TmxMapLoader();
		map = mapLoader.load("tmxMaps\\debugMap5.tmx");

		ships = new ArrayList<HoverCraft>();
		ships.add(new HoverCraft(map, world, rayHandler, this));
		playerHoverCraft = ships.get(0);
		landingPads = new ArrayList<LandingPad>();

		createWorld();
	}

	/**
	 * creates the box2d world, objects, map, map and object should be initialized first,
	 * no rendering, only physics
	 */
	public void createWorld(){
		Body body;
		BodyDef bDef = new BodyDef();
		FixtureDef fDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();

		//creating ground and plateforms
		for (MapObject mapObject : map.getLayers().get("objects").getObjects().getByType(RectangleMapObject.class)){

			//obstacles are always rectangles
			Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

			bDef.type = BodyDef.BodyType.StaticBody;
			bDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/PPM, (rectangle.getY()+rectangle.getHeight()/2)/PPM);
			body = world.createBody(bDef);
			body.setUserData(wallData);

			//creating the shape of the body
			shape.setAsBox(rectangle.getWidth()/2/PPM, rectangle.getHeight()/2/PPM);
			fDef.shape = shape;
			body.createFixture(fDef);

		}
		//creating landing pads
		for (MapObject mapObject : map.getLayers().get("landingpad").getObjects().getByType(RectangleMapObject.class)){

			//obstacles are always rectangles
			Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

			Vector2 position = new Vector2();
			position.set((rectangle.getX()+rectangle.getWidth()/2)/PPM, (rectangle.getY()+rectangle.getHeight()/2)/PPM);

			LandingPad landingPad = new LandingPad(world, position, rayHandler);
			landingPads.add(landingPad);

		}
	}

	/**
	 * updates the logic of the level, the world, input list, the playerHoverCraft
	 * @param delta
	 */
	public void update(float delta){

		world.step(1 / 60f, 6, 2);
		handleEvents(delta);

		if (!(gameOver)) {
				ArrayList < Command > commands = handleInput(delta);

				//execute Command
				for (int i = 0; i < commands.size(); i++) {
					commands.get(i).execute(playerHoverCraft);
				}
		}
		else System.out.println("************************************************ Gameu ouevereu");
		//update the playerHoverCraft
		playerHoverCraft.update(delta);
		System.out.println("HP ::::: " + playerHoverCraft.getHealth());

		//for (LandingPad lp : landingPads){
		//	lp.update(delta);
		//}
		for (int i=0; i<landingPads.size(); i++){
			landingPads.get(i).update(delta);
		}

		//updating the hud
		hud.update(delta);

	}



	public void verifyGameOver(){
		playerHoverCraft.verifyLanding(Gdx.graphics.getDeltaTime());
	}

	/**
	 * renders the level, render the hovers and entities first then end the batch
	 * and render lights
	 * @param batch
	 * @param gameCam
	 * @param delta
	 */
	public void render(SpriteBatch batch, OrthographicCamera gameCam, float delta){
		batch.begin();
		playerHoverCraft.render(batch, gameCam, delta);

		//for (LandingPad lp : landingPads){
		//	lp.render(batch);
		//}
		for (int i=0; i<landingPads.size(); i++){
			landingPads.get(i).render(batch);
		}

		batch.end();
		//playerHoverCraft.renderLights(batch, gameCam);
		rayHandler.setCombinedMatrix(gameCam.combined);
		rayHandler.updateAndRender();


		//rendering the hud
		hud.render(batch);

	}


	/**
	 * this method uses the Command pattern, gets all the inputs and puts them into an
	 * array, the update method will decide what to execute and on what
	 * @param delta
	 * @return
	 */
	public ArrayList<Command> handleInput(float delta) {
		ArrayList<Command> commands = new ArrayList<Command>();
		if (Gdx.input.isKeyPressed(Input.Keys.X)){
			commands.add(new pushUpCommand());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			commands.add(new hoverLeftCommand(hoverForce));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			commands.add(new hoverRightCommand(hoverForce));
		}

		//Android part
		if (Gdx.input.isTouched()){
			commands.add(new pushUpCommand());
		}

		float rotation = Gdx.input.getAccelerometerY();
		if ( rotation>1 || rotation<-1)
		if (rotation>0)
			commands.add(new hoverRightCommand(rotation/50));
		else
			commands.add(new hoverLeftCommand(-rotation/50));

		commands.add(new doNothingCommand());
		return commands;
	}

	/**
	 * method used to store events in an array list, called by many objects like the contactListener
	 * @param event
	 */
	public void recieveEvent(Event event){
		eventList.add(event);
	}

	/**
	 * method used to parcour all of the events stored and verify each one, if handled deletes it
	 */
	public void handleEvents(float delta){

		for (Event event: eventList){
			if (event.handled(delta)) {
				//eventList.remove(event);
				eventsToRemove.add(event);
			}
		}
		eventsToRemove.add(new PlayerShipOnLandingPad(playerHoverCraft,this));
		eventList.removeAll(eventsToRemove);
		eventsToRemove = new ArrayList<Event>();
	}

	/**
	 * puts all events with that type into the list to remove
	 * @param eventType
	 */
	public void removeEventsByType(String eventType){
		for (Event event : eventList){
			if (event.getClass().getSimpleName().equals(eventType))
				eventsToRemove.add(event);
		}
	}

	/**
	 * method called when the level is finished, shows the levelFinished screen
	 */
	public void wonGame(){
		if (!gameOver) {
			System.out.println("gameWon Yeah!!");
			gameScreen.levelCompleted();
		}
	}


	public void changePlayer(int shipIndex){
		playerHoverCraft = ships.get(shipIndex);
	}


	//GETTERS AND SETTERS

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public TiledMap getMap() {
		return map;
	}

	public void setMap(TiledMap map) {
		this.map = map;
	}

	public HoverCraft getPlayerHoverCraft() {
		return playerHoverCraft;
	}

	public void setPlayerHoverCraft(HoverCraft playerHoverCraft) {
		this.playerHoverCraft = playerHoverCraft;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
}
