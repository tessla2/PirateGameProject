package overlays;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;

public class GameOverOverlay {

	private Playing playing;

	public GameOverOverlay(Playing playing) {
		this.playing = playing;
	}

	public void draw(Graphics g) {
		// Desenha o fundo semi-transparente
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		// Estiliza e desenha o texto "Game Over"
		g.setColor(Color.white);
		g.setFont(new Font("Serif", Font.BOLD, 50)); // Fonte padrão Serif para "Game Over"
		String title = "Game Over";
		int titleWidth = g.getFontMetrics().stringWidth(title);
		g.drawString(title, (Game.GAME_WIDTH - titleWidth) / 2, 150);

		// Desenha o texto "Press ESC to enter Main Menu!"
		g.setFont(new Font("Serif", Font.PLAIN, 30)); // Fonte padrão Serif para o prompt
		String prompt = "Do Better Next Time!";
		String prompt1 = "Press ESC to enter Main Menu!";
		int promptWidth = g.getFontMetrics().stringWidth(prompt);
		g.drawString(prompt, (Game.GAME_WIDTH - promptWidth) / 2, 300);
		g.drawString(prompt1, (Game.GAME_WIDTH - promptWidth) / 2, 350);

	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			playing.resetAll();
			Gamestate.state = Gamestate.MENU;
		}
	}
}
