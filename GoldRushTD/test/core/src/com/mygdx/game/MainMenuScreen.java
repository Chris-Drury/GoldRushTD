package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class MainMenuScreen implements Screen {

	final Group7 game;
	Screen gameScreen;
	
	Stage stage;
	Skin skin;
	Texture btnWindow;
	Button Name, playBtn;
	ButtonStyle NameStyle, btnStyle;
	OrthographicCamera camera;
	FillViewport port;
	
	public MainMenuScreen(final Group7 game) {
		//store the previous game
		this.game = game;
		
		//set the screen's camera and stage
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 720);
		
		port = new FillViewport(1280, 720, camera);
		stage = new Stage();
		stage.setViewport(port);
		Gdx.input.setInputProcessor(stage);
		
		//create all skins and add all needed textures for this screen
		skin = new Skin();
		btnWindow = new Texture(Gdx.files.internal("ButtonSkins/DEFEND.PNG"));
		skin.add("BtnOver", new Texture(Gdx.files.internal("ButtonSkins/DEFENDOver.PNG")));
		skin.add("Play", btnWindow);
		
		//create the play (DEFEND) button
		btnStyle = new ButtonStyle();
		btnStyle.up = skin.getDrawable("Play");
		btnStyle.over = skin.getDrawable("BtnOver");
		playBtn = new Button(btnStyle);
		playBtn.setPosition(port.getWorldWidth()/2 - playBtn.getWidth()/2 , port.getWorldHeight()/2 - playBtn.getHeight()/2);
		
		//add the button to the stage
		stage.addActor(playBtn);

		//create a new gameScreen and enter the screen
		playBtn.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
			game.MenuSelect.play();
			gameScreen = new GameScreen(game, getScreen());
			game.setScreen(gameScreen);
			}
		});
		
		//create a new button to display the project title. This can be used to create an unknown click listener if desired
		btnWindow = new Texture(Gdx.files.internal("UntitledTitle.png"));
		skin.add("Name", btnWindow);
		NameStyle = new ButtonStyle(skin.getDrawable("Name"),skin.getDrawable("Name"),skin.getDrawable("Name"));
		Name = new Button(NameStyle);
		Name.setPosition(port.getWorldWidth()/2 - Name.getWidth()/2 , 3*port.getWorldHeight()/4 - Name.getHeight()/2);
		stage.addActor(Name);
	};
	
	public MainMenuScreen getScreen() {
		return this;
	}
	
	@Override
	public void show() {
		game.MainMenu.play();
	}

	@Override
	public void render(float delta) {
		//clear previous screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//setup camera and act/draw the stage
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		stage.act();
		stage.draw();
		
		game.batch.begin();
		game.batch.end();
		
		//if Enter is pressed begin a game by setting the screen to a gameScreen
		if(Gdx.input.isKeyPressed(Keys.ENTER)) {
		game.MenuSelect.play();
		gameScreen = new GameScreen(game, getScreen());
		game.setScreen(gameScreen);}
		
		//if ESCAPE is pressed close the app
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	@Override
	public void pause() {
		game.MainMenu.pause();
	}

	@Override
	public void resume() {
		game.MainMenu.play();
	}

	@Override
	public void hide() {
		game.MainMenu.pause();
	}

	@Override
	public void dispose() {
		game.MainMenu.dispose();
		
	}

}
