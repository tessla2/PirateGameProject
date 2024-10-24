package ui;

import gamestates.Gamestate;
import main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

import static utilz.Constants.UI.PauseButtons.SOUND_SIZE;
import static utilz.Constants.UI.VolumeButton.SLIDER_WIDTH;
import static utilz.Constants.UI.VolumeButton.VOLUME_HEIGHT;

public class AudioOptions {
    private VolumeButton volumeButton;
    private SoundButton musicButton, soundEffectButton;



    public AudioOptions() {
        createSoundButtons();
        createVolumeButton();
    }


    private void createVolumeButton() {
        int vX = (int) (276 * Game.SCALE);
        int vY = (int) (252 * Game.SCALE);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    private void createSoundButtons() {
        int soundX = (int) (410 * Game.SCALE);
        int musicY = (int) (110 * Game.SCALE);
        int soundEffectButtonY = (int) (160 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        soundEffectButton = new SoundButton(soundX, soundEffectButtonY, SOUND_SIZE, SOUND_SIZE);
    }

    public void update(){
        musicButton.update();
        soundEffectButton.update();
    }

    public void draw(Graphics g){
        // Sound buttons
        musicButton.draw(g);
        soundEffectButton.draw(g);

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
        }

        musicButton.resetBool();
        soundEffectButton.resetBool();
        volumeButton.resetBool();

    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        soundEffectButton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if (isIn(e, musicButton))
            musicButton.setMouseOver(true);
        else if (isIn(e, soundEffectButton))
            soundEffectButton.setMouseOver(true);
        else if (isIn(e, volumeButton))
            volumeButton.setMouseOver(true);
    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }


}