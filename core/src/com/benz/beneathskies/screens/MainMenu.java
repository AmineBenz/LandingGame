package com.benz.beneathskies.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.benz.beneathskies.GameMain;

import box2dLight.RayHandler;

/**
 * Created by amineBenz on 07/03/2016.
 * this class renders and updates all of the main menu stages (main menu, options)
 * also sends to the chooseLevelScreen
 */
public class MainMenu implements Screen {

	/**
	 * the game reference in a screen
	 */
	GameMain game;

	/**
	 * the sprite batch reference in this screen, got via getters and setters
	 */
	SpriteBatch batch;


	/**
	 * the current rendered stage
	 * or the menu being rendered in this screen now
	 */
	Stage currentStage;

	/**
	 * different stages needed for the main menu
	 */
	Stage mainMenuStage;
	Stage optionMenu;

	/**
	 * constructor, create all of the stages possible and stores them until they are called,
	 * initialization of the stage is in the menuCreator
	 * @param game
	 */
	public MainMenu(GameMain game){
		this.game = game;
		this.batch = game.getBatch();
		mainMenuStage = game.myMenuCreator.createMainMenu();
		optionMenu = game.myMenuCreator.createOptionMenu();
		currentStage = mainMenuStage;

		Gdx.input.setInputProcessor(currentStage);
	}

	/**
	 * this methods switches from this screen to the chooseLevelScreen, or to the play screen for debug
	 */
	public void switchToPlay(){
		game.play();
	}

	/***
	 * this method switches to the options menu while staying in the same screen, only the currentStage changes
	 */
	public void switchToOption(){
		currentStage = optionMenu;
		Gdx.input.setInputProcessor(currentStage);
	}

	/***
	 * this method switches to the mainMenu, changes the currentStage
	 */
	public void switchToMain(){
		currentStage = mainMenuStage;
		Gdx.input.setInputProcessor(currentStage);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		currentStage.act();
		currentStage.draw();
	}

	@Override
	public void resize(int width, int height) {

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

	public SpriteBatch getBatch(){
		return batch;
	}
}
