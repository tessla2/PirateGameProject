package ui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;
import ui.UrmButton.*;
import static utilz.Constants.UI.UrmButtons.*;


public class PauseOverlay {

    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuB, replayB, unpauseB;
    private AudioOptions audioOptions;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        audioOptions = playing.getGame().getAudioOptions();
        createUrmButtons();
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_MENU);
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (Game.SCALE);
    }

    private void createUrmButtons() {
        int menuX = (int) (285 * Game.SCALE);
        int replayX = (int) (356 * Game.SCALE);
        int unpausedX = (int) (425 * Game.SCALE);

        int bY = (int) (300 * Game.SCALE);
        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new UrmButton(unpausedX, bY, URM_SIZE, URM_SIZE, 0);
    }


     public void update() {
        menuB.update();
        replayB.update();
        unpauseB.update();
        audioOptions.update();

    }

        public void draw(Graphics g){
            // Background
            g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);

            // UrmButtons
            menuB.draw(g);
            replayB.draw(g);
            unpauseB.draw(g);

            audioOptions.draw(g);
        }


        public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
 if (isIn(e, menuB))
            menuB.setMousePressed(true);
        else if (isIn(e, replayB))
            replayB.setMousePressed(true);
        else if (isIn(e, unpauseB))
            unpauseB.setMousePressed(true);
        else
            audioOptions.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
         if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                Gamestate.state = Gamestate.MENU;
                playing.unpauseGame();
            }
        } else if (isIn(e, replayB)) {
            if (replayB.isMousePressed())
                System.out.println("replay lvl!");
        } else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed())
                playing.unpauseGame();
        } else audioOptions.mouseReleased(e);

        menuB.resetBool();
        replayB.resetBool();
        unpauseB.resetBool();

    }

    public void mouseMoved(MouseEvent e) {

        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);


        if (isIn(e, menuB))
            menuB.setMouseOver(true);
        else if (isIn(e, replayB))
            replayB.setMouseOver(true);
        else if (isIn(e, unpauseB))
            unpauseB.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);

    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

}