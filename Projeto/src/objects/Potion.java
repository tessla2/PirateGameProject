package objects;

import main.Game;

public class Potion extends GameObject {

	public Potion(int x, int y, int objType) {
		super(x, y, objType);
		FaçaAnimation = true;
		initHitbox(7, 14);
		xDrawOffset = (int) (3 * Game.SCALE);
	}

	public void update() {
		updateAnimationTimer();
	}

}
