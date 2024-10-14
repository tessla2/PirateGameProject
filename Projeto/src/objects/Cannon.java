package objects;

import main.Game;

public class Cannon extends GameObject {

	private int tileY;

	public Cannon(int x, int y, int objType) {
		super(x, y, objType);
		tileY = y / Game.TILES_SIZE;
		initHitbox(40, 26);// default value of sprite

		hitbox.x -= (int) (4 * Game.SCALE);// center of tile, not flying around hehe
		hitbox.y += (int) (6 * Game.SCALE);
	}

	public void update() {
		if (FaçaAnimation)
			updateAnimationTimer();
	}

	public int getTileY() {
		return tileY;
	}

}
