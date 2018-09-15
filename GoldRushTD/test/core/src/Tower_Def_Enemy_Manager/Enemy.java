package Tower_Def_Enemy_Manager;

import com.badlogic.gdx.graphics.g2d.Sprite;

import Tower_Def_Board.Element;

public abstract class Enemy {

	public int health;
	//AI ai;
	Element elementRes;
	public Sprite sprite;
	public int x = 0;
	public int y = 0;
	
	//public AI getAI() {return ai;};
	public String getElementRes() {return elementRes.name();}
	public Sprite getSprite() {return sprite;};	
	
}
