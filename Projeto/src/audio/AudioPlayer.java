package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

    // Constantes para identificar músicas e efeitos específicas
    public static int MENU_1 = 0;
    public static int LEVEL_1 = 1;
    public static int LEVEL_2 = 2;


    public static int DIE = 0;
    public static int JUMP = 1;
    public static int GAMEOVER = 2;
    public static int LVL_COMPLETED = 3;
    public static int ATTACK_ONE = 4;
    public static int ATTACK_TWO = 5;
    public static int ATTACK_THREE = 6;

    private Clip[] songs, effects;       // Arrays para armazenar músicas e efeitos sonoros
    private int currentSongId;
    private float volume = 0.5f;
    private boolean songMute, effectMute;
    private Random rand = new Random();

    public AudioPlayer() {
        loadSongs();
        loadEffects();
        playSong(MENU_1);
    }

    // Carrega as músicas nos objetos Clip, a partir dos nomes definidos
    private void loadSongs() {
        String[] names = { "menu", "level1", "level2" };
        songs = new Clip[names.length];
        for (int i = 0; i < songs.length; i++)
            songs[i] = getClip(names[i]);
    }

    private void loadEffects() {
        String[] effectNames = { "die", "jump", "gameover", "lvlcompleted", "attack1", "attack2", "attack3" };
        effects = new Clip[effectNames.length];
        for (int i = 0; i < effects.length; i++)
            effects[i] = getClip(effectNames[i]);

        // Atualiza o volume dos efeitos após o carregamento
        updateEffectsVolume();
    }

    // Carrega um arquivo de áudio e retorna um Clip
    private Clip getClip(String name) {
        URL url = getClass().getResource("/audio/" + name + ".wav"); // caminho do arquivo de áudio
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c; // Retorna o Clip configurado com o áudio
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace(); // Imprime o erro caso ocorra
        }

        return null;
    }

    // Define o volume geral e atualiza o volume de músicas e efeitos
    public void setVolume(float volume) {
        this.volume = volume;
        updateSongVolume();
        updateEffectsVolume();
    }

    // Para a música atual se estiver ativa
    public void stopSong() {
        if (songs[currentSongId].isActive())
            songs[currentSongId].stop();
    }

    // Define a música do nível com base no índice do nível (par ou ímpar)
    public void setLevelSong(int lvlIndex) {
        if (lvlIndex % 2 == 0)
            playSong(LEVEL_1);
        else
            playSong(LEVEL_2);
    }

    public void lvlCompleted() {
        stopSong();
        playEffect(LVL_COMPLETED);
    }

    public void playAttackSound() {
        int start = 4;
        start += rand.nextInt(3); // Seleciona aleatoriamente um dos três sons de ataque
        playEffect(start);
    }

    // Toca o efeito sonoro especificado
    public void playEffect(int effect) {
        if (effects[effect].getMicrosecondPosition() > 0)
            effects[effect].setMicrosecondPosition(0);// Reinicia o efeito se já tocado
        effects[effect].start();
    }

    // Toca uma música especificada em loop
    public void playSong(int song) {
        stopSong(); // Para a música atual antes de iniciar a nova

        currentSongId = song;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0); // Reinicia a música
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY); // Toca em loop contínuo
    }

    // Alterna o mudo para as músicas
    public void toggleSongMute() {
        this.songMute = !songMute;
        for (Clip c : songs) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute); // Define o mudo para todas as músicas
        }
    }

    // Alterna o mudo para os efeitos sonoros
    public void toggleEffectMute() {
        this.effectMute = !effectMute;
        for (Clip c : effects) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute); // Define o mudo para todos os efeitos
        }
        if (!effectMute)
            playEffect(JUMP); // Toca um efeito "jump" se desmutado
    }

    // Atualiza o volume da música atual
    private void updateSongVolume() {
        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain); // Define o ganho de acordo com o volume
    }

    private void updateEffectsVolume() {
        for (Clip c : effects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain); // Define o ganho de cada efeito
        }
    }
}
