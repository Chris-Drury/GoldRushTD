package Tower_Def_Board;

public class Map {

	public int xTowers = 10;
	public int yTowers = 14;
	
	public Tile[][] board;
	
	public Map() {
		//hardcode the board's path. This can be further used for multiple board selections is desired
		board = new Tile[yTowers][xTowers];
		int[][] BinaryTower =
			{{1,0,1,1,1,1,1,1,1,1},
			{1,0,0,1,1,1,1,1,1,1},
			{1,1,0,1,1,1,1,1,1,1},
			{1,1,0,1,1,1,1,1,1,1},
			{1,1,0,0,1,1,1,1,1,1},
			{1,1,1,0,1,1,1,1,1,1},
			{1,1,1,0,1,1,1,1,1,1},
			{1,1,1,0,0,0,0,1,1,1},
			{1,1,1,1,1,1,0,1,1,1},
			{1,1,1,1,1,1,0,1,1,1},
			{1,1,1,1,1,1,0,0,1,1},
			{1,1,1,1,1,1,1,0,1,1},
			{1,1,1,1,1,1,1,0,1,1},
			{1,1,1,1,1,1,1,0,1,1}};
		
		//if 1 create a "Grass" tile
		//if 0 create a "Path" tile
		for (int y = 0; y < yTowers; y++) {
			for (int x = 0; x < xTowers; x++) {
				if (BinaryTower[y][x] == 1)
					board[y][x] = new EmptyTile(x, y, "Grass");
				else
					board[y][x] = new EmptyTile(x, y, "Path");
			}
		}
	}
	
	public Tile getBoardTile(int x, int y){return board[y][x];};
	
	//if the board is updated in GameScreen reflect the change here. and update the tower's center variable
	public void updateBoardTile(int x, int y, Tile t) {
		board[y][x] = t;
		if (t instanceof Tower) { 
			((Tower) t).setCenter();
		}		
	}
}
