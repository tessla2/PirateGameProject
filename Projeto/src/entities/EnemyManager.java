package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {

	private Playing playing;
	private BufferedImage[][] crabbyArr;
	private ArrayList<Crabby> crabbies = new ArrayList<>();

	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
	}

	public void loadEnemies(Level level) {
		crabbies = level.getCrabs();
	}

	public void update(int[][] lvlData, Player player) {
		boolean isAnyActive = false;
		for (Crabby c : crabbies)
			if (c.isActive()) {
				c.update(lvlData, player);
				isAnyActive = true;
			}
		if (!isAnyActive)
			playing.setLevelCompleted(true);
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawCrabs(g, xLvlOffset);
	}

	private void drawCrabs(Graphics g, int xLvlOffset) {
		for (Crabby c : crabbies) {
			if (c.isActive()) {
				int state = c.getState();
				int aniIndex = c.getAniIndex();

				// Limita aniIndex ao intervalo válido para evitar exceções
				aniIndex = Math.min(aniIndex, crabbyArr[state].length - 1);

				if (state < 0 || state >= crabbyArr.length) {
					System.err.println("State fora dos limites: " + state);
					continue; // Pula este `Crabby` com estado inválido
				}

				if (aniIndex < 0 || aniIndex >= crabbyArr[state].length) {
					System.err.println("AniIndex fora dos limites: " + aniIndex);
					continue; // Pula este `Crabby` com `aniIndex` inválido
				}

				BufferedImage image = crabbyArr[state][aniIndex];
				if (image != null) {
					g.drawImage(image, (int) c.getHitbox().x - xLvlOffset - CRABBY_DRAWOFFSET_X + c.flipX(),
							(int) c.getHitbox().y - CRABBY_DRAWOFFSET_Y, CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);
				}
			}
		}
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		for (Crabby c : crabbies)
			if (c.isActive())
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt((int)5);
					return;
				}
	}

	private void loadEnemyImgs() {
		crabbyArr = new BufferedImage[5][9];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
		for (int j = 0; j < crabbyArr.length; j++)
			for (int i = 0; i < crabbyArr[j].length; i++)
				crabbyArr[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT,
						CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
	}

	public void resetAllEnemies() {
		for (Crabby c : crabbies)
			c.resetEnemy();
	}

}