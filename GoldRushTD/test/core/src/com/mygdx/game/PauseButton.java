package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class PauseButton extends Button {

	final GameScreen screen;
	Skin skin;
	ButtonStyle BtnStyle;
	Button Btn;
	
	PauseButton(float x, float y, GameScreen s){
		Btn = new Button();
		this.screen = s;
		skin = new Skin();
		
	skin.add("Menu", new Texture(Gdx.files.internal("ButtonSkins/MenuButton.png")));
	skin.add("MenuPressed", new Texture(Gdx.files.internal("ButtonSkins/MenuButtonPressed.png")));

	BtnStyle = new ButtonStyle(skin.getDrawable("Menu"), skin.getDrawable("MenuPressed"), skin.getDrawable("Menu"));
	BtnStyle.over = skin.getDrawable("MenuPressed");
	
	Btn.setStyle(BtnStyle);
	Btn.setPosition(x, y);
	Btn.setName("Menu");
	
	Btn.addListener(new ChangeListener() {
		public void changed (ChangeEvent event, Actor actor) {
			screen.game.MenuSelect.play();
			screen.pause();
		}
	});
	
	};
	
	
	public Button getBtn() {return Btn;};	
	
	
}
