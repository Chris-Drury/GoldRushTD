package Tower_Def_Board;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import Tower_Def_Enemy_Manager.Enemy;
import Tower_Def_Projectile.Projectile;

public abstract class Tower extends Tile{

	public abstract Projectile attack(int damage, Rectangle target);
	public abstract int getDamage();
	public abstract int getCost();
	public abstract int getRefund();
	public abstract double getAttackSpeed();
	public abstract double getRange();
	public abstract String getElementName();
	public abstract Texture getTexture();
	public abstract Texture getOverTexture();
	public abstract String getName();
	public abstract double getCooldown();
	public abstract double getAttackTimer();
	
	public Point2D.Float center;
	
	public abstract void setTextures(Texture text, Texture textover);
	public abstract void setElement(Element elementType); 	
	public abstract void setName(String name);
	
	public abstract boolean intersects(Rectangle e);

	public abstract void setCenter();
	public abstract void setAttackTimer(double attackTimer);

}
