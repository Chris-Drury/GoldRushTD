package Tower_Def_Board;

import com.badlogic.gdx.graphics.Texture;

//determine the base Tile
public abstract class Tile {
	
	Texture tileTexture, tileOverTexture;
	String name;
	int x, y;
	
	public Texture getTexture() {return tileTexture;};
	public Texture getOverTexture() {return tileOverTexture;};
	public String getName() {return name;};
	public int getX() {return x;};
	public int getY() {return y;};
}
