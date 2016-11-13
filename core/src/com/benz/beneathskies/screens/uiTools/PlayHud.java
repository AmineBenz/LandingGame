package com.benz.beneathskies.screens.uiTools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.benz.beneathskies.tools.Level;

import static com.benz.beneathskies.tools.Constants.PPM;
import static com.benz.beneathskies.tools.Constants.defaultHealth;
import static com.benz.beneathskies.tools.Constants.portHeight;
import static com.benz.beneathskies.tools.Constants.portWidth;

/**
 * Created by amineBenz on 01/04/2016.
 */
public class PlayHud implements Disposable {

	Level level;

	Stage stage;
	Skin skin;

	float spaceShipHealth = defaultHealth;

	Label healthLabel;

	Viewport hudPort;

	public PlayHud(Level level){
		//hudPort = new ExtendViewport(portWidth/PPM, portHeight/PPM, level.gameCam);
		hudPort = new ExtendViewport(portWidth*PPM, portHeight*PPM, new OrthographicCamera());
		stage = new Stage(hudPort);
		skin = new Skin();
		this.level = level;



		Table table = new Table(skin);
		table.right();
		table.top();

		String healthValue = "HP : " + spaceShipHealth;
		healthLabel = new Label(healthValue, new Label.LabelStyle(new BitmapFont(), Color.RED));
		//healthLabel.setPosition(-PPM,-PPM);
		healthLabel.setPosition(-0,-0);
		//table.add(healthValue).expandX();

		//stage.addActor(table);
		stage.addActor(healthLabel);

	}

	public void update(float delta){
		spaceShipHealth = level.getPlayerHoverCraft().getHealth();
		String healthValue = "HP : " + spaceShipHealth;
		healthLabel.setText(healthValue);

		stage.act();
	}

	public void render(SpriteBatch batch){
		stage.draw();
	}

	/**
	 * Releases all resources of this object.
	 */
	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}
}
