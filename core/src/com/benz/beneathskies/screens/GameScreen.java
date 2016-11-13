package com.benz.beneathskies.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.benz.beneathskies.GameMain;
import com.benz.beneathskies.tools.Level;

import static com.benz.beneathskies.tools.Constants.PPM;

/**
 * Created by amineBenz on 07/03/2016.
 * this is the screen where the game engine takes place, pause pauses the game and shows a menu stage Pause,
 * exit redirects to the main menu.
 */
public class GameScreen implements Screen {

	/**
	 * the game reference in a screen
	 */
	GameMain game;

	/**
	 * the sprite batch reference in this screen, got via getters and setters
	 */
	SpriteBatch batch;

	/**
	 * the current level to be on the engine.
	 */
	Level currentLevel;


	//tiled map renderer
	MapRenderer mapRenderer;

	//box2d debug render
	Box2DDebugRenderer box2DDebugRenderer;

	//references to the GameMain screen render objects
	OrthographicCamera gameCam;
	Viewport gamePort;

	public GameScreen(GameMain game){
		//getting references from the gameMain
		this.game = game;
		gameCam = game.getGameCam();
		gamePort = game.getGamePort();
		this.batch = game.getBatch();

		//creating a new level to show
		currentLevel = new Level(this);

		//getting the proper level, now it is just a debug level
		currentLevel.createDebugLevel();

		//putting the camera on the right position
		Vector2 camPos = currentLevel.getPlayerHoverCraft().getPosition();
		gameCam.position.x = camPos.x;
		gameCam.position.y = camPos.y;

		//setting the properties of the map renderer, scale the map by 1/PPM
		float unitScale = (float) 1/PPM;
		mapRenderer = new OrthogonalTiledMapRenderer(currentLevel.getMap(),unitScale, batch);

		//box2d debug fixtures renderer
		box2DDebugRenderer = new Box2DDebugRenderer();

	}

	@Override
    public void show() {

    }

    @Override
    public void render(float delta) {
	    update(delta);
	    Gdx.gl.glClearColor(0,0,0,1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	    mapRenderer.render();
	    currentLevel.render(batch, gameCam, delta);




	    //box2DDebugRenderer.render(currentLevel.getWorld(),gameCam.combined);
    }

	/**
	 * updates the game state, updates the level, camera position
	 * @param delta
	 */
	private void update(float delta) {

		//updating the level
		currentLevel.update(delta);

		//updating camera without touching the z axe
		Vector2 camPos = currentLevel.getPlayerHoverCraft().getPosition();
		gameCam.position.x = (float) camPos.x;
		gameCam.position.y = (float) camPos.y;
		gameCam.update();

		//updating the mapRenderer the the camera
		mapRenderer.setView(gameCam);
	}

	public void levelCompleted(){
		game.setScreen(game.mainMenuScreen);
	}

	@Override
    public void resize(int width, int height) {
		gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

	public OrthographicCamera getGameCam() {
		return gameCam;
	}
}
