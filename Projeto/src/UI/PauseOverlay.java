package UI;

import main.Game;
import utilz.LoadSave;
import static utilz.Constants.UI.PauseButtons.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PauseOverlay {
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgWidth, bgHeight;
    private SoundButton musicButton, soundEffectButton;

    public PauseOverlay() {
        loadBackground();
        createSoundButtons();

    }

    private void createSoundButtons() {
        int soundX = (int) (410 * Game.SCALE);
        int musicY = (int) (115 * Game.SCALE);
        int soundEffectButtonY = (int) (165 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        soundEffectButton = new SoundButton(soundX, soundEffectButtonY, SOUND_SIZE, SOUND_SIZE);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_MENU);
        bgWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgWidth / 2;
        bgY = (int) (Game.SCALE);
    }

    public void update() {
        musicButton.update();
        soundEffectButton.update();
    }

    public void draw(Graphics g) {
        //Background
        g.drawImage(backgroundImg, bgX, bgY, bgWidth, bgHeight, null);

        //Sound buttons
        musicButton.draw(g);
        soundEffectButton.draw(g);

    }

    public void mouseDragged(MouseEvent e) {
    }


    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton))
            musicButton.setMousePressed(true);
        else if (isIn(e, soundEffectButton))
            soundEffectButton.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, musicButton)) {
            if (musicButton.isMousePressed())
                musicButton.setMuted(!musicButton.isMuted());
        } else if (isIn(e, soundEffectButton)) {
            if (soundEffectButton.isMousePressed()) ;
            soundEffectButton.setMuted(!soundEffectButton.isMuted());
        }
        musicButton.resetBooleans();
        soundEffectButton.resetBooleans();
    }

    public void mouseMoved(MouseEvent e){
        musicButton.setMouseOver(false);
        soundEffectButton.setMouseOver(false);

        if(isIn(e,musicButton))
            musicButton.setMouseOver(true);
        else if(isIn(e,soundEffectButton))
            soundEffectButton.setMouseOver(true);

    }
    private boolean isIn(MouseEvent e, PauseButton b){
        return b.getBounds().contains(e.getX(), e.getY());

    }


}
