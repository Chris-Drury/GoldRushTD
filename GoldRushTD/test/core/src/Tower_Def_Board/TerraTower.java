package Tower_Def_Board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import Tower_Def_Projectile.Projectile;

public class TerraTower extends TowerDecorator{

	public TerraTower(Tower t) {
		super(t); 
		setTextures( new Texture(Gdx.files.internal("TileSkins/GridTile_TerraTower.png")), new Texture(Gdx.files.internal("TileSkins/GridTileOver_TerraTower.png")));
		setElement();
	}

	
	public int getCost() {return super.getCost() - 100 ;}; //upgrade to terra is 50 total, of 200 build cost
	public int getRefund() {return super.getRefund() + 20;}; //selling with terra adds 20, total of 120 refund
	public double getAttackSpeed() {return super.getAttackSpeed();};
	public double getRange() {return super.getRange();};
	public String getElementName() {return super.getElementName();};
	public Texture getTexture() {return super.getTexture();};
	public Texture getOverTexture() {return super.getOverTexture();};
	public String getName() {return super.getName();};
	public double getCooldown() {return super.getCooldown() - 25 * super.getAttackSpeed();};
	
	public Projectile attack(int damage, Rectangle target) {return super.attack(this.getDamage(),target);};
	

	public int getX() {return super.getX();};
	public int getY() {return super.getY();};
	
	
	
	public int getDamage() {return super.getDamage() + 1;};
	public void setElement() {super.setElement(Element.Terra);}
	public void setTextures(Texture text, Texture textover) {super.setTextures(text, textover);}
	public void setName(String name) { super.setName(name);}

	public boolean intersects(Rectangle e) {
		return super.intersects(e);
	}

	@Override
	public void setAttackTimer(double attackTimer) {
		super.setAttackTimer(attackTimer);
	};
	
	public double getAttackTimer() {
		return super.getAttackTimer();
	}
}

