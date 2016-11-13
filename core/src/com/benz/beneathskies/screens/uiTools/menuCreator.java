package com.benz.beneathskies.screens.uiTools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.benz.beneathskies.screens.ChooseLevelScreen;
import com.benz.beneathskies.screens.GameScreen;
import com.benz.beneathskies.screens.MainMenu;
import static com.benz.beneathskies.tools.Constants.PPM;
import static com.benz.beneathskies.tools.Constants.portWidth;
import static com.benz.beneathskies.tools.Constants.portHeight;

/**
 * Created by amineBenz on 22/03/2016.
 * a tool to create all the different stages (menus) possible for the app,
 * all methods return a Stage
 */
public class menuCreator {

	//parent Screen that call the tool
	MainMenu mainSuperScreen;
	//other screen accessable from this one
	ChooseLevelScreen levelSuperScreen;
	GameScreen gameSuperScreen;

	private Skin skin;
	TextureAtlas atlas;

	Viewport gamePort;

	public menuCreator(){
		this.gamePort = new ExtendViewport(portWidth*PPM/1.3f,portHeight*PPM/1.3f);

		createSkin();
	}

	/**
	 * this method sends the screens references from the game to the menu creator
	 * @param superMenu
	 * @param chooseLevelScreen
	 * @param gameScreen
	 */
	public void setScreens(MainMenu superMenu, ChooseLevelScreen chooseLevelScreen, GameScreen gameScreen) {
		this.mainSuperScreen = superMenu;
		this.levelSuperScreen = chooseLevelScreen;
		this.gameSuperScreen = gameScreen;

	}

	/***
	 * this method creates the skin for the ui (button styles,..)
	 */
	private void createSkin(){
		atlas = new TextureAtlas("uiAssets\\uiskin.atlas");
		skin = new Skin();

		//creating Buttons styles
		addButtonStyle("beginButton","beginButton");
		addButtonStyle("backButton", "backButton");
		addButtonStyle("continueButton","continueButton");
		addButtonStyle("exitButton","exitButton");
		addButtonStyle("optionButton","optionButton");
		//addButtonStyle("smallButton", "smallbutton");
	}

	/***
	 * a method that adds a button style to the default skin, name will be its name, textureName is the texture
	 * name in the atlas without the "Up/Down"
	 * @param name
	 * @param textureName
	 */
	private void addButtonStyle(String name, String textureName){
		TextureRegion textureRegion = new TextureRegion(atlas.findRegion(textureName+"Up"));
		TextureRegion textureRegionDown = new TextureRegion(atlas.findRegion(textureName+"Down"));

		Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
		buttonStyle.up = new TextureRegionDrawable(textureRegion);
		buttonStyle.down = new TextureRegionDrawable(textureRegionDown);
		buttonStyle.checked = new TextureRegionDrawable(textureRegion);
		buttonStyle.over = new TextureRegionDrawable(textureRegionDown);
		skin.add(name, buttonStyle);
	}

	/**
	 * this method will create a mainmenu stage and return it (contains three buttons: begin, options, exit),
	 * called one time and saved in memory
	 * @return
	 */
	public Stage createMainMenu(){
		Stage stage = new Stage(gamePort);
		Table table = new Table();
		table.setFillParent(true);

		Button beginButton = new Button(skin,"beginButton");
		Button optionButton = new Button(skin,"optionButton");
		Button exitButton = new Button(skin,"exitButton");

		beginButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mainSuperScreen.switchToPlay();
			}
		});
		optionButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mainSuperScreen.switchToOption();
			}
		});
		exitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});

		table.add(beginButton).pad(5);
		table.row();
		table.add(optionButton).pad(5);
		table.row();
		table.add(exitButton).pad(5);
		table.row();

		stage.addActor(table);
		return stage;
	}

	/**
	 * create the option menu stage of the main menu and returns it, called one time and saved in memory
	 * @return
	 */
	public Stage createOptionMenu(){
		Stage stage = new Stage(gamePort);
		Table table = new Table();
		table.setFillParent(true);

		Button optionButton = new Button(skin,"optionButton");
		Button backButton = new Button(skin,"backButton");

		backButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mainSuperScreen.switchToMain();
			}
		});

		table.add(optionButton).pad(5);
		table.row();
		table.add(backButton).pad(5);
		table.row();

		stage.addActor(table);

		return stage;
	}

	//TO DO

	/**
	 * creates a chooseLevelMenu stage to be saved in memory and then called from chooselevelScreen,
	 * the menu created is a menu that contains all the levels of the current world
	 * @return
	 */
	public Stage createChooseLevelMenu(){
		Stage stage = new Stage(gamePort);

		return stage;
	}

	//TO DO
	/**
	 * creates a chooseLevelMenu stage to be saved in memory and then called from chooselevelScreen,
	 * the menu created is a menu that contains all the worlds, worlds contain different levels
	 * @return
	 */
	public Stage createChooseWorldMenu(){
		Stage stage = new Stage(gamePort);

		return stage;
	}

	//TO DO
	/**
	 * creates a pause screen and saves it to be called then from GameScreen
	 * @return
	 */
	public Stage createPauseMenu(){
		Stage stage = new Stage(gamePort);

		return stage;
	}


	public Stage createEndLevelMenu(){
		Stage stage = new Stage(gamePort);

		atlas = new TextureAtlas(Gdx.files.internal("uielements.atlas"));

		//Creating global frame
		Image frame = new Image();
		frame.setDrawable(new TextureRegionDrawable(atlas.findRegion("laseredframeDown")));
		float xCenter = Gdx.graphics.getWidth()/2;
		float yCenter = Gdx.graphics.getHeight()/2;
		frame.setPosition(xCenter,yCenter);

		stage.addActor(frame);

		return stage;
	}
}
