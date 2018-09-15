package Tower_Def_Projectile;

import Tower_Def_Enemy_Manager.Enemy;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Tower_Def_Projectile.AquaProjectile;
import Tower_Def_Projectile.BaseProjectile;
import Tower_Def_Projectile.InfernoProjectile;
import Tower_Def_Projectile.ShockProjectile;
import Tower_Def_Projectile.TerraProjectile;
import Tower_Def_Projectile.ZephyrProjectile;


public abstract class Projectile {
// constructor to initialize 
	public Projectile(int damage, int x, int y, float speed, Rectangle enemy) {
		this.enemy = enemy;
		this.damage = damage;
		this.speed = speed;
		this.x = x;
		this.y = y;
	}
	
// enum for each projectile type
	public enum ProjectileType {
		Base, Inferno, Aqua, Shock, Terra, Zephyr
	}
	
// x position with getter and setter
	public int x;
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
	
// y position with getter and setter
	public int y;
	
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
// more variables to define projectile behavior
	public int damage;
	public float speed, angle; // angle not used yet
// a bounding rectangle that defines the projectiles targets position
	public Rectangle enemy;
// if the projectile hasn't hit it's target -> not used yet
	public boolean active;
// projectile sprite
	public Sprite sprite;
// check if target hit
	public boolean isActive() {
		return active;
	}
// set if target hit	
	public void setActive(boolean active) {
		this.active = active;
	}
// check if projectile rectangle overlaps enemy rectangle	
	public boolean checkCollision(Enemy enemy) {
		Rectangle minRect = sprite.getBoundingRectangle();
		return minRect.overlaps(enemy.getSprite().getBoundingRectangle());
	}
	
}
