package ui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.UI.PauseButtons.*;
import static utilz.Constants.UI.UrmButtons.*;
import static utilz.Constants.UI.VolumeButton.*;


public class PauseOverlay {

    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private SoundButton musicButton, soundEffectButton;
    private UrmButton menuB, replayB, unpauseB;
    private VolumeButton volumeButton;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createSoundButtons();
        createUrmButtons();
        createVolumeButton();

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

    private void createSoundButtons() {
        int soundX = (int) (410 * Game.SCALE);
        int musicY = (int) (115 * Game.SCALE);
        int soundEffectButtonY = (int) (165 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        soundEffectButton = new SoundButton(soundX, soundEffectButtonY, SOUND_SIZE, SOUND_SIZE);
    }

    private void createVolumeButton(){
        int vX = (int) (276 * Game.SCALE);
        int vY = (int) (252 * Game.SCALE);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

     public void update() {

        musicButton.update();
        soundEffectButton.update();

        menuB.update();
        replayB.update();
        unpauseB.update();

        volumeButton.update();
    }

        public void draw(Graphics g){
            // Background
            g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);

            // Sound buttons
            musicButton.draw(g);
            soundEffectButton.draw(g);

            // UrmButtons
            menuB.draw(g);
            replayB.draw(g);
            unpauseB.draw(g);

            // Volume Button
            volumeButton.draw(g);
        }


        public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            volumeButton.changeX(e.getX());
        }

    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton))
            musicButton.setMousePressed(true);
        else if (isIn(e, soundEffectButton))
            soundEffectButton.setMousePressed(true);
        else if (isIn(e, menuB))
            menuB.setMousePressed(true);
        else if (isIn(e, replayB))
            replayB.setMousePressed(true);
        else if (isIn(e, unpauseB))
            unpauseB.setMousePressed(true);
        else if (isIn(e, volumeButton))
            volumeButton.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, musicButton)) {
            if (musicButton.isMousePressed())
                musicButton.setMuted(!musicButton.isMuted());

        } else if (isIn(e, soundEffectButton)) {
            if (soundEffectButton.isMousePressed())
                soundEffectButton.setMuted(!soundEffectButton.isMuted());
        } else if (isIn(e, menuB)) {
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
        }

        musicButton.resetBool();
        soundEffectButton.resetBool();
        menuB.resetBool();
        replayB.resetBool();
        unpauseB.resetBool();
        volumeButton.resetBool();

    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        soundEffectButton.setMouseOver(false);
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if (isIn(e, musicButton))
            musicButton.setMouseOver(true);
        else if (isIn(e, soundEffectButton))
            soundEffectButton.setMouseOver(true);
        else if (isIn(e, menuB))
            menuB.setMouseOver(true);
        else if (isIn(e, replayB))
            replayB.setMouseOver(true);
        else if (isIn(e, unpauseB))
            unpauseB.setMouseOver(true);
        else if (isIn(e, volumeButton))
            volumeButton.setMouseOver(true);

    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

}