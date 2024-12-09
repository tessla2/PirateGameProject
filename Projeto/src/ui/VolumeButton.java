package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.VolumeButton.*;

public class VolumeButton extends PauseButton {

    private BufferedImage[] imgs;
    private BufferedImage slider;
    private boolean mouseOver, mousePressed;
    private int index = 0; // Índice para alternar entre as imagens do botão
    private int buttonX, minX, maxX; // Posições do botão e limites da barra de volume
    private float floatValue = 0; // Valor do volume em uma escala de 0 a 1

    VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_WIDTH, height);

        // Ajusta a posição inicial do botão e define os limites da barra de volume
        bounds.x -= VOLUME_WIDTH / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        minX = x + VOLUME_WIDTH / 2;
        maxX = x + width - VOLUME_WIDTH / 2;

        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        imgs = new BufferedImage[3]; // Array para armazenar os diferentes estados do botão

        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

        // Carrega a imagem do slider
        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
    }

    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;
    }

    public void draw(Graphics g) {
        g.drawImage(slider, x, y, width, height, null);
        g.drawImage(imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public void resetBool() {
        mouseOver = false;
        mousePressed = false;
    }

    // Ajusta a posição X do botão de volume dentro dos limites da barra
    public void changeX(int x) {
        if (x < minX)
            buttonX = minX;
        else if (x > maxX)
            buttonX = maxX;
            buttonX = x; // Atualiza a posição do botão

        updateFloatValue(); // Atualiza o valor de volume baseado na nova posição
        bounds.x = buttonX - VOLUME_WIDTH / 2; // Atualiza a posição dos limites do botão
    }

    // Atualiza o valor de volume (floatValue) como uma fração do alcance total
    private void updateFloatValue() {
        float range = maxX - minX; // Intervalo total do slider
        float value = buttonX - minX; // Posição atual relativa ao mínimo
        floatValue = value / range; // Valor de volume entre 0 e 1
    }

    public float getFloatValue() {
        return floatValue;
    }
}
