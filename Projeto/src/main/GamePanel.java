package main;

import java.awt.*;
import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;
import static main.Game.*;

public class GamePanel extends JPanel {

	private MouseInputs mouseInputs;
	private Game game;
	public static final int BASE_WIDTH = 800;
	public static final int BASE_HEIGHT = 600;

	public GamePanel(Game game) {
		mouseInputs = new MouseInputs(this);
		this.game = game;
		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}

//	private void setPanelSize() {
//		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
//		setPreferredSize(size);
//		System.out.println("size : " + GAME_WIDTH + " : " + GAME_HEIGHT);
//	}

	private void setPanelSize() {
		// Obtém a resolução da tela
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// Ajusta o tamanho dos tiles para uma proporção da tela
		TILES_SIZE = (int) Math.min(screenSize.width / TILES_IN_WIDTH, screenSize.height / TILES_IN_HEIGHT);

		// Calcula o tamanho total do jogo baseado no novo tamanho dos tiles
		GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
		GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

		// Define o tamanho do painel de jogo
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);

		System.out.println("size : " + GAME_WIDTH + " : " + GAME_HEIGHT);
	}


	public void updateGame() {

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);

	}

	public Game getGame() {
		return game;
	}


}