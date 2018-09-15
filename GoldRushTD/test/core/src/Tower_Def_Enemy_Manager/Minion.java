package Tower_Def_Enemy_Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import Tower_Def_Board.Element;

public class Minion extends Enemy{

	public Minion(int x, int y) {
		health = 10;
		//ai = new Flying_AI();
		elementRes = Element.Aqua;
		sprite = new Sprite(new Texture(Gdx.files.internal("Enemies/Dwarven Miner_down.png")));
		this.x = x;
		this.y = y;
	};
	
}
