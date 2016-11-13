package com.benz.beneathskies.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.benz.beneathskies.GameMain;

import box2dLight.RayHandler;

/**
 * Created by amineBenz on 07/03/2016.
 */
public class ChooseLevelScreen implements Screen {

	/**
	 * the game reference in a screen
	 */
	GameMain game;

	/**
	 * the sprite batch reference in this screen, got via getters and setters
	 */
	SpriteBatch batch;

	public ChooseLevelScreen(GameMain game){
		this.game = game;
		this.batch = game.getBatch();
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {

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
}
