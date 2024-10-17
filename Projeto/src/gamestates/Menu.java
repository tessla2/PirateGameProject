package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import UI.MenuButton;
import main.Game;
import utilz.LoadSave;

public class Menu extends State implements Statemethods {

	private MenuButton[] buttons = new MenuButton[3];



	//private BufferedImage backgroundImg;

	public Menu(Game game) {
		super(game);
		loadButtons();
		//backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
	}

	private void loadButtons() {
		buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (150 * Game.SCALE), 0 , Gamestate.PLAYING);
		buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (220 * Game.SCALE), 1 , Gamestate.OPTIONS);
		buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (290 * Game.SCALE), 2 , Gamestate.QUIT);
	}

	@Override
	public void update() {
		for(MenuButton menuB : buttons)
			menuB.update();
	}

	@Override
	public void draw(Graphics g) {
		for(MenuButton menuB : buttons)
			menuB.draw(g);

//	    g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

//	    // Estiliza e desenha o título do menu
//	    g.setColor(Color.black);
//	    g.setFont(new Font("Treasure Map Deadhand", Font.BOLD, 50));
//	    String title = "Pirate Clown Nose";
//	    int titleWidth = g.getFontMetrics().stringWidth(title);
//	    g.drawString(title, (Game.GAME_WIDTH - titleWidth) / 2, 200);
//
//	    // Desenha as opções do menu
//	    g.setFont(new Font("Treasure Map Deadhand", Font.PLAIN, 30)); // Ajusta o tamanho da fonte para as opções
//	    String[] options = { "Press Enter to Start", "Kill all enemies or", "go to the plank!" };
//
//	    for (int i = 0; i < options.length; i++) {
//	        int optionWidth = g.getFontMetrics().stringWidth(options[i]);
//	        g.drawString(options[i], (Game.GAME_WIDTH - optionWidth) / 2, 400 + i * 50);
//	    }
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Implementação de clique do mouse, se necessário
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for(MenuButton menuB : buttons){
              if(isIn(e, menuB)){
				 menuB.setMousePressed(true);
				 break;
			  }
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for(MenuButton menuB : buttons){
			if(isIn(e, menuB)){
				if(menuB.isMousePressed())
					menuB.applyGamestate();
				break;
			}
		}
		resetButtons();
	}

	private void resetButtons() {
		for(MenuButton menuB : buttons){
			menuB.resetBooleans();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for(MenuButton menuB : buttons)
			menuB.setMouseOver(false);

			for(MenuButton menuB : buttons){
				if(isIn(e,menuB)){
					menuB.setMouseOver(true);
					break;
				}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
//		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//			Gamestate.state = Gamestate.PLAYING;

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Implementação de liberação de tecla, se necessário
	}
}
