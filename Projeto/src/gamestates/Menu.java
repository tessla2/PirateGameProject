package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import utilz.LoadSave;

public class Menu extends State implements Statemethods {

	private BufferedImage backgroundImg;

	public Menu(Game game) {
		super(game);
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
	}

	@Override
	public void update() {
		// Atualizações necessárias
	}

	@Override
	public void draw(Graphics g) {
	    g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

	    // Estiliza e desenha o título do menu
	    g.setColor(Color.black);
	    g.setFont(new Font("Treasure Map Deadhand", Font.BOLD, 50));
	    String title = "Pirate Clown Nose";
	    int titleWidth = g.getFontMetrics().stringWidth(title);
	    g.drawString(title, (Game.GAME_WIDTH - titleWidth) / 2, 200);

	    // Desenha as opções do menu
	    g.setFont(new Font("Treasure Map Deadhand", Font.PLAIN, 30)); // Ajusta o tamanho da fonte para as opções
	    String[] options = { "Press Enter to Start", "Kill all enemies or", "go to the plank!" };

	    for (int i = 0; i < options.length; i++) {
	        int optionWidth = g.getFontMetrics().stringWidth(options[i]);
	        g.drawString(options[i], (Game.GAME_WIDTH - optionWidth) / 2, 400 + i * 50);
	    }
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Implementação de clique do mouse, se necessário
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Implementação de pressionamento do mouse, se necessário
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Implementação de liberação do mouse, se necessário
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Implementação de movimento do mouse, se necessário
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			Gamestate.state = Gamestate.PLAYING;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Implementação de liberação de tecla, se necessário
	}
}
