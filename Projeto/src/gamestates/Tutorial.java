package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import utilz.LoadSave;

public class Tutorial extends State implements Statemethods {

    private BufferedImage backgroundImg;
    private long blinkStartTime; // Para controlar o tempo de piscar
    private static final long BLINK_INTERVAL = 500; // Intervalo de 500ms
    private boolean isTextVisible = true;

    public Tutorial(Game game) {
        super(game);
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.TUTORIAL_BG);
    }

    public void update() {
        long currentTime = System.currentTimeMillis();

        // Verifica se o intervalo de piscar passou
        if (currentTime - blinkStartTime > BLINK_INTERVAL) {
            isTextVisible = !isTextVisible; // Alterna a visibilidade
            blinkStartTime = currentTime; // Reseta o tempo de piscar
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        g.setColor(Color.black);

        // Desenha as opções do tutorial
        g.setFont(new Font("Treasure Map Deadhand", Font.BOLD, 36));
        String[] options = {"Please", "Press Enter to Start" };


        for (int i = 0; i < options.length; i++) {
            int optionWidth = g.getFontMetrics().stringWidth(options[i]);
            // Desenha apenas se isTextVisible for verdadeiro
            if (isTextVisible) {
                g.drawString(options[i], (Game.GAME_WIDTH - optionWidth) / 2, 480 + i * 50);
            }
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            Gamestate.state = Gamestate.MENU;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
