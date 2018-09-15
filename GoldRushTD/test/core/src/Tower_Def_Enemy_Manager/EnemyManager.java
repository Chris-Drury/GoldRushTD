package Tower_Def_Enemy_Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class EnemyManager {
	//one array to store the Enemies and another to store their rectangles for the GameScreen's display
	public Array<Enemy> RoundEnemies;
	public Array<Rectangle> Enemies;
	public int roundEnemyCount = 0;
	int enemies = 0;
	int remainingEnemies = 0;
	int[] move = {0,0};
	
	public EnemyManager(int k) {
		//the amount of enemies in the wave is dependent on k
		RoundEnemies = new Array<Enemy>();
		Enemies = new Array<Rectangle>();
		roundEnemyCount = k;
	};

	public int spawnEnemy(float x, float y) {
		if (enemies < roundEnemyCount) {
			//for each needed enemy, create a minion and corresponding rectangle
			enemies++;
			remainingEnemies++;
				Rectangle enemy = new Rectangle();
				enemy.x = x;
				enemy.y = y;
				enemy.width = 32;
				enemy.height = 32;
				Enemies.add(enemy);
				RoundEnemies.add(new Minion((int) x, (int) y));
				return remainingEnemies;
		}
		else 
			return remainingEnemies;
	}
	
	public void moveEnemy(int ctrl, int idx, boolean fast) 
	{
		//depending on the GameScreen.enemyOverlay's return value, move the enemy rectangle's position
		//movement is faster if the boolean fast is set to true (if DPAD.RIGHT is pressed)
		if (ctrl == 0) {Enemies.get(idx).y = Enemies.get(idx).y;}
		else if (ctrl == 1 && fast) {
			Enemies.get(idx).y -= 300 * Gdx.graphics.getDeltaTime();
			RoundEnemies.get(idx).y -= 300 * Gdx.graphics.getDeltaTime();
		}	
		else if (ctrl == 1 ) {
			Enemies.get(idx).y -= 50 * Gdx.graphics.getDeltaTime();
			RoundEnemies.get(idx).y -= 50 * Gdx.graphics.getDeltaTime();
		}
		else if (ctrl == -1) {
			Enemies.get(idx).y += 50 * Gdx.graphics.getDeltaTime();
			RoundEnemies.get(idx).y += 50 * Gdx.graphics.getDeltaTime();
		}
		else if (ctrl == 2 && fast) {
			Enemies.get(idx).x -= 300 * Gdx.graphics.getDeltaTime();
			RoundEnemies.get(idx).x -= 300 * Gdx.graphics.getDeltaTime();
		}
		else if (ctrl == 2) {
			Enemies.get(idx).x -= 50 * Gdx.graphics.getDeltaTime();
			RoundEnemies.get(idx).x -= 50 * Gdx.graphics.getDeltaTime();
		}
		else if (ctrl == -2) {
			Enemies.get(idx).x += 50 * Gdx.graphics.getDeltaTime();
			RoundEnemies.get(idx).x += 50 * Gdx.graphics.getDeltaTime();
		}
		
	}
	
	//remove the enemy at index k
	public void removeEnemy(int k) {
		Enemies.removeIndex(k);
		RoundEnemies.removeIndex(k);
		remainingEnemies--;
	}
	
	//return the number of remaining enemies in the wave
	public int getRemainingNumber() {
		int idx = 0;
		for (int y = 0; y < RoundEnemies.size; y++) {
			if (RoundEnemies.get(y) != null)
				idx += 1;
		}
		return idx;
	};
	
	public int getRoundEnemies() {return roundEnemyCount;};
	public Enemy getEnemy(int y) {return RoundEnemies.get(y);};
	
}
