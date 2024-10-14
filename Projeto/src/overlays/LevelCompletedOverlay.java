package overlays;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;

public class LevelCompletedOverlay {

	private Playing playing;

	public LevelCompletedOverlay(Playing playing) {
		this.playing = playing;
	}

	public void update() {

	}

	public void draw(Graphics g) {
		// Fundo semi-transparente
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		// Texto "Level Completed"
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 40));
		String levelCompleted = "Level Completed";
		int levelCompletedWidth = g.getFontMetrics().stringWidth(levelCompleted);
		g.drawString(levelCompleted, (Game.GAME_WIDTH - levelCompletedWidth) / 2, 200);

		// Texto "Press Enter to proceed to Next Level"
		g.setFont(new Font("Arial", Font.PLAIN, 30));
		String pressEnter = "Press Enter to proceed to Next Level";
		int pressEnterWidth = g.getFontMetrics().stringWidth(pressEnter);
		g.drawString(pressEnter, (Game.GAME_WIDTH - pressEnterWidth) / 2, 250);
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			playing.proceedToNextLevel();
		}
	}

	public void keyReleased(KeyEvent e) {
	}

}
