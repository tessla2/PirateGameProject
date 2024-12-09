package objects;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.ObjectConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public class GameObject {

	protected int x, y, objType;
	protected Rectangle2D.Float hitbox;
	protected boolean FaçaAnimation, active = true;
	protected int animationTimer, animatioCurrentIndex;
	protected int xDrawOffset, yDrawOffset;

	public GameObject(int x, int y, int objType) {
		this.x = x;
		this.y = y;
		this.objType = objType;
	}

	protected void updateAnimationTimer() {
		animationTimer++;
		if (animationTimer >= ANIMATION_SPEED) {
			animationTimer = 0;
			animatioCurrentIndex++;
			if (animatioCurrentIndex >= GetSpriteAmount(objType)) {
				animatioCurrentIndex = 0;
				if (objType == BARREL || objType == BOX) {
					FaçaAnimation = false;
					active = false;
				} else if (objType == CANNON_LEFT || objType == CANNON_RIGHT)
					FaçaAnimation = false;
			}
		}
	}

	public void reset() {
		animatioCurrentIndex = 0;
		animationTimer = 0;
		active = true;

		if (objType == BARREL || objType == BOX || objType == CANNON_LEFT || objType == CANNON_RIGHT)
			FaçaAnimation = false;
		else
			FaçaAnimation = true;
	}

	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
	}

//	public void drawHitbox(Graphics g, int xLvlOffset) {
//		g.setColor(Color.PINK);
//		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
//	}

	public int getObjType() {
		return objType;
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setAnimation(boolean doAnimation) {
		this.FaçaAnimation = doAnimation;
	}

	public int getxDrawOffset() {
		return xDrawOffset;
	}

	public int getyDrawOffset() {
		return yDrawOffset;
	}

	public int getAniIndex() {
		return animatioCurrentIndex;
	}

	public int getAnimationTimer() {
		return animationTimer;
	}

}