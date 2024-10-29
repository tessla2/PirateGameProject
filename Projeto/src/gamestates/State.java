package gamestates;

import audio.AudioPlayer;
import ui.MenuButton;
import main.Game;

import java.awt.event.MouseEvent;

public class State {

	protected Game game;

	public State(Game game) {
		this.game = game;

	}

	public boolean isIn(MouseEvent e, MenuButton menuButton){
		return menuButton.getButtonsRec().contains(e.getX(), e.getY());
	}

	public Game getGame() {
		return game;
	}

	public void setGamesState(Gamestate state){
		switch (state){
			case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
			case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
		}

		Gamestate.state = state;
	}

}
