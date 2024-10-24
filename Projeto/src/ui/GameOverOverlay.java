package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import static utilz.Constants.UI.UrmButtons.URM_SIZE;

public class GameOverOverlay {

	private Playing playing;
	private BufferedImage img;
	private int imgX, imgY, imgWidth, imgHeight;
	private UrmButton menu,play;

	public GameOverOverlay(Playing playing) {
		this.playing = playing;
		createImg();
		createButtons();
	}

	private void createButtons() {
		int menuX = (int) (300 * Game.SCALE);
		int playX = (int) (405 * Game.SCALE);
		int y = (int) (195 * Game.SCALE);
		play = new UrmButton(playX, y, URM_SIZE, URM_SIZE, 0);
		menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);

	}

	private void createImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
		imgWidth = (int) (img.getWidth() * Game.SCALE);
		imgHeight = (int) (img.getHeight() * Game.SCALE);
		imgX = Game.GAME_WIDTH / 2 - imgWidth / 2;
		imgY = (int) (100 * Game.SCALE);

	}

	public void draw(Graphics g) {

		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		g.drawImage(img, imgX, imgY, imgWidth, imgHeight, null);
		menu.draw(g);
		play.draw(g);
	}

	public void update(){
		menu.update();
		play.update();
	}

		public void mouseMoved(MouseEvent e) {
			play.setMouseOver(false);
			menu.setMouseOver(false);

			if (isIn(menu, e))
				menu.setMouseOver(true);
			else if (isIn(play, e))
				play.setMouseOver(true);
		}

		public void mouseReleased(MouseEvent e){
			if (isIn(menu, e)) {
				if (menu.isMousePressed()) {
					playing.resetAll();
					Gamestate.state = Gamestate.MENU;
				}
			} else if (isIn(play, e))
				if (play.isMousePressed()) {
					playing.resetAll();
				}
			menu.resetBool();
			play.resetBool();
		}

		public void mousePressed(MouseEvent e) {
			if (isIn(menu, e))
				menu.setMousePressed(true);
			else if (isIn(play, e))
				play.setMousePressed(true);

	}

	private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

}
