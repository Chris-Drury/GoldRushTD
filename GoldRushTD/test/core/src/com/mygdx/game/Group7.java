package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Group7 extends Game {
	SpriteBatch batch;
	BitmapFont font;
	Sound MenuSelect;
	Music MainMenu, GameScreen, GameOver;
	
	public void create () {
		//setup sprite batch and fonts
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		//import music
		MainMenu = Gdx.audio.newMusic(Gdx.files.internal("Music/Synth_With_Bell.mp3"));
		MainMenu.setLooping(true);
		MenuSelect = Gdx.audio.newSound(Gdx.files.internal("Music/Menu_Choice.mp3"));
		GameScreen = Gdx.audio.newMusic(Gdx.files.internal("Music/Epic_Song.mp3"));
		GameOver = Gdx.audio.newMusic(Gdx.files.internal("Music/dark-synth.mp3"));
		GameScreen.setLooping(true);
		GameScreen.setVolume(.6f);
		
		
		//set initial screen to the MainMenu
		this.setScreen(new MainMenuScreen(this));
		
	}

	public void render() {
		super.render();
	}
	
	public void dispose () {
		batch.dispose();
		font.dispose();
		
	}
	
}
