package Tower_Def_Board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class EmptyTile extends Tile{

	public EmptyTile(int x, int y, String name) {
		this.name = name;
		this.x = x;
		this.y = y;
		//make sure the name of the tile reflects the texture of the tower.
		tileTexture =  new Texture(Gdx.files.internal("TileSkins/GridTile_" + name + ".png"));
		tileOverTexture = new Texture(Gdx.files.internal("TileSkins/GridTileOver_" + name + ".png"));
	};


}
