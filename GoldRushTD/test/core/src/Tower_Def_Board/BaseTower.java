package Tower_Def_Board;

import java.awt.geom.Point2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import Tower_Def_Projectile.BaseProjectile;
import Tower_Def_Projectile.Projectile;

public class BaseTower extends Tower {

	int damage, cost, refund;
	double attackSpeed, range;
	Element elementType;
	boolean atkAir, atkGnd;
	
	double cooldown;
	double attackTimer;
	
	public BaseTower(int x, int y) {
		name = "Tower";
		this.x = x;
		this.y = y;
		tileTexture =  new Texture(Gdx.files.internal("TileSkins/GridTile_Tower.png"));
		tileOverTexture = new Texture(Gdx.files.internal("TileSkins/GridTileOver_Tower.png"));
		
		//determine the baseTower's base stats to be adjusted by Tower Decorator
		damage = 1;
		attackSpeed = 1.0;
		range = 2.5;
		elementType = Element.Physical;
		
		cooldown = 100 * 1 / attackSpeed;
		
		attackTimer = 0;
		
		//create a center for the tower
		center = new Point2D.Float();
		
		atkGnd = true;
		atkAir = false;
		
		cost = 150;
		refund = 100;
	}

	@Override
	//if possible to attack, create a new projectile
	public Projectile attack(int damage, Rectangle target) {
		
	 return new BaseProjectile(damage, 575 + 77 *x + 23,2 + 77*y + 38, (float)attackSpeed, target);
	
	}

	public int getDamage() {return damage;};
	public int getCost() {return cost;};
	public int getRefund() {return refund;};
	public double getAttackSpeed() {return attackSpeed;};
	public double getRange() {return range;};
	public String getElementName() {return elementType.name();};
	public double getCooldown() {return cooldown;};
	
	public String getName() {return getElementName() + name;}

	public void setTextures(Texture text, Texture textover) {
		this.tileTexture = text;
		this.tileOverTexture = textover;
	};
	
	public Texture getTexture() {return tileTexture;}
	public Texture getOverTexture() {return tileOverTexture;}
	
	public void setElement(Element elementType) {this.elementType = elementType;}
	public void setName(String name) {this.name = name;};
	
	public boolean intersects(Rectangle e) {
		float circleDistanceX = (float) Math.abs(center.getX() - e.x / 77 / 2);
		float circleDistanceY = (float) Math.abs(center.getY() - e.y / 77);
		return(Math.sqrt(Math.pow(circleDistanceX, 2) + (Math.pow(circleDistanceY, 2))) < this.getRange());
	}
	
	public void setCenter() {
		center.setLocation((( super.getX() + 1 / 2)), ( super.getY() + 1 / 2));
	}
	
	public double getAttackTimer() {return attackTimer;}

	public void setAttackTimer(double attackTimer) {
		this.attackTimer = attackTimer;
	}
	
}
