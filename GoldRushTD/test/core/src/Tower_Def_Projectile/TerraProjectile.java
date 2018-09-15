package Tower_Def_Projectile;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import Tower_Def_Enemy_Manager.Enemy;
import Tower_Def_Projectile.Projectile;
//not used yet
public class TerraProjectile extends Projectile {

	public TerraProjectile(int damage, int x, int y, float speed, Rectangle enemy) {
		super(damage, x, y, speed, enemy);
	}

}
