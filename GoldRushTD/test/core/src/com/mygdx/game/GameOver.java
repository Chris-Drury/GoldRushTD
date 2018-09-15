package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class GameOver implements Screen {
	//store the game and main menu screen for later
	final Group7 game;
	final MainMenuScreen mainMenu;
	
	OrthographicCamera camera;
	FillViewport port;
	float aspRatio = Gdx.graphics.getWidth()/Gdx.graphics.getHeight();
	
	Stage stage;
	
	int finalRound;
	
	public GameOver(final Group7 game, final MainMenuScreen mainMenu, int finalRound) {
		//track the final round and hold onto the main menu screen 
		this.game = game;
		this.mainMenu = mainMenu;
		this.finalRound = finalRound;
		
		//set the camera and stage
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 720);
		
		port = new FillViewport(1280, 720, camera);
		
		stage = new Stage();
		stage.setViewport(port);
		
	}
	
	
	@Override
	public void show() {
		game.GameOver.play();
	}

	@Override
	public void render(float delta) {
		//clear previous screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		//draw the new stage for the new camera
		stage.act();
		stage.draw();
		
		//draw all fonts
		game.batch.begin();
		game.font.draw(game.batch, "GAME OVER", this.camera.viewportWidth*aspRatio/2, this.camera.viewportHeight*aspRatio/2);
		game.font.draw(game.batch, "Rounds completed: " + (finalRound - 1), this.camera.viewportWidth*aspRatio/2, this.camera.viewportHeight*aspRatio/2 - 25);
		game.font.draw(game.batch, "Press M to return to the menu", this.camera.viewportWidth*aspRatio/2, this.camera.viewportHeight*aspRatio/2 - 50);
		game.batch.end();
		game.GameOver.play();
		
		//if M is pressed return to the stored Main Menu screen
		if(Gdx.input.isKeyPressed(Keys.M)) {
			game.GameOver.stop();
			
			//set the stage to the Main Menu's stage
			Gdx.input.setInputProcessor(mainMenu.stage);
			game.setScreen(mainMenu);}
	}

	@Override
	public void resize(int width, int height) {
		//resize the viewport used by the stage
		stage.getViewport().update(width, height);
	}

	@Override
	public void pause() {
		game.GameOver.pause();
	}

	@Override
	public void resume() {
		game.GameOver.play();
	}

	@Override
	public void hide() {
		game.GameOver.pause();
	}

	@Override
	public void dispose() {
		game.GameOver.dispose();
		
	}

}
