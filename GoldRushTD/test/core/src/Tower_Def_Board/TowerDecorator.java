package Tower_Def_Board;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import Tower_Def_Projectile.Projectile;

public abstract class TowerDecorator extends Tower{
	protected Tower decoratedTower;
	
	public TowerDecorator(Tower t ) {
		this.decoratedTower = t;
	}; 


	public int getCost() {return decoratedTower.getCost();};
	public int getRefund() {return decoratedTower.getRefund();};
	public double getAttackSpeed() {return decoratedTower.getAttackSpeed();};
	public double getRange() {return decoratedTower.getRange();};
	public String getElementName() {return decoratedTower.getElementName();};
	public Texture getTexture() {return decoratedTower.getTexture();};
	public Texture getOverTexture() {return decoratedTower.getOverTexture();}
	public String getName() {return decoratedTower.getName();};
	public int getDamage() {return decoratedTower.getDamage();};
	public double getCooldown() {return decoratedTower.getCooldown();};
	public double getAttackTimer() {return decoratedTower.getAttackTimer();};
	
	public Projectile attack(int damage, Rectangle target) {return decoratedTower.attack(damage, target);};
	
	public int getX() {return decoratedTower.x;};
	public int getY() {return decoratedTower.y;};
	
	public void setElement(Element element) {decoratedTower.setElement(element);};
	public void setTextures(Texture text, Texture textover) {decoratedTower.setTextures(text, textover);}
	public void setName(String name) {decoratedTower.name = name;};
	
	public void setCenter() {decoratedTower.setCenter();}
	public boolean intersects(Rectangle e) {return decoratedTower.intersects(e);}
	public void setAttackTimer(double attackTimer) {decoratedTower.setAttackTimer(attackTimer);};
	
	

}
