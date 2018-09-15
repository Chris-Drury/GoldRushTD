package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class Menu_Button extends Button{
	Button MenuBtn;
	ButtonStyle Style;
	
	public Menu_Button(GameScreen screen) {
		
		screen.skin.add("Menu", new Texture(Gdx.files.internal("ButtonSkins/MenuButton.png")));
		screen.skin.add("MenuPressed", new Texture(Gdx.files.internal("ButtonSkins/MenuButtonPressed.png")));
	
		Style = new ButtonStyle(screen.skin.getDrawable("Menu"), screen.skin.getDrawable("MenuPressed"), screen.skin.getDrawable("Menu"));
		Style.over = screen.skin.getDrawable("MenuPressed");
		MenuBtn = new Button(Style);
		
		MenuBtn.setPosition(screen.camera.viewportWidth*screen.aspRatio - MenuBtn.getWidth() - 20 , screen.camera.viewportHeight*screen.aspRatio - MenuBtn.getHeight() - 20 );
		
		screen.stage.addActor(MenuBtn);
	
	}

	
}
