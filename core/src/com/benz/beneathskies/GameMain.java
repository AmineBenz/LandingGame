package com.benz.beneathskies;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.benz.beneathskies.screens.ChooseLevelScreen;
import com.benz.beneathskies.screens.GameScreen;
import com.benz.beneathskies.screens.MainMenu;
import com.benz.beneathskies.screens.uiTools.menuCreator;

import box2dLight.RayHandler;

import static com.benz.beneathskies.tools.Constants.PPM;
import static com.benz.beneathskies.tools.Constants.portHeight;
import static com.benz.beneathskies.tools.Constants.portWidth;

public class GameMain extends Game {
	SpriteBatch batch;
	public menuCreator myMenuCreator;
	public MainMenu mainMenuScreen;
	public ChooseLevelScreen chooseLevelScreen;
	public GameScreen gameScreen;

	OrthographicCamera gameCam;
	Viewport gamePort;



	@Override
	public void create () {
		gameCam = new OrthographicCamera();
		gamePort = new FitViewport(portWidth,portHeight, gameCam);
		//gamePort = new ExtendViewport(portWidth,portHeight, gameCam);

		batch = new SpriteBatch();
		myMenuCreator = new menuCreator();
		mainMenuScreen = new MainMenu(this);
		chooseLevelScreen = new ChooseLevelScreen(this);
		gameScreen = new GameScreen(this);
		myMenuCreator.setScreens(mainMenuScreen,chooseLevelScreen,gameScreen );
		setScreen(mainMenuScreen);
		//batch.end();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		/**
		 * this code will ask the game (class heritage) to render the screen that is set
		 */
		super.render();
	}

	/**
	 * start a new game, setScreen to GameScreen
	 */
	public void play(){
		setScreen(gameScreen);
	}


	public SpriteBatch getBatch() {
		return batch;
	}

	public OrthographicCamera getGameCam() {
		return gameCam;
	}

	public Viewport getGamePort() {
		return gamePort;
	}

}
