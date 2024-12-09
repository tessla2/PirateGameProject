package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import ui.*;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import utilz.LoadSave;
import static utilz.Constants.Environment.*;


public class Playing extends State implements Statemethods {
	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;
	private GameOverOverlay gameOverOverlay;
	private PauseOverlay pauseOverlay;
	private LevelCompletedOverlay levelCompletedOverlay;
	private GameCompletedOverlay gameCompletedOverlay;

	// Flags para controlar estados do jogo
	private boolean paused = false;
	private boolean playerDying;
	private boolean gameOver;
	private boolean lvlCompleted;
	public boolean setLevelCompleted;
	public boolean gameCompleted;

	// Controle de posição e limites de rolagem do nível
	private int xLvlOffset;
	private int leftBorder = (int) (0.2 * Game.GAME_WIDTH); // Borda esquerda onde começa a rolagem do nível
	private int rightBorder = (int) (0.8 * Game.GAME_WIDTH); // Borda direita onde começa a rolagem do nível
	private int maxLvlOffsetX;

	private BufferedImage backgroundImg, smallCloud;
	private int[] smallCloudsPos;
	private Random rnd = new Random();

	// Construtor inicializa o estado Playing com o jogo em execução
	public Playing(Game game) {
		super(game);
		initClasses();

		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
		smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);

		// Posiciona aleatoriamente as nuvens no fundo
		smallCloudsPos = new int[8];
		for (int i = 0; i < smallCloudsPos.length; i++)
			smallCloudsPos[i] = (int) (90 * Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE));

		// Calcula os limites de deslocamento do nível
		calcLvlOffset();
		loadStartLevel();
	}

	// Carrega o próximo nível do jogo e reposiciona o jogador
	public void loadNextLevel() {
		resetAll();
		levelManager.loadNextLevel();
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
	}

	// Carrega os elementos do nível atual (inimigos e objetos)
	private void loadStartLevel() {
		enemyManager.loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObjects(levelManager.getCurrentLevel());
	}

	// Calcula o limite de deslocamento horizontal do nível
	private void calcLvlOffset() {
		maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
	}

	// Inicializa as principais classes do jogo
	private void initClasses() {
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		objectManager = new ObjectManager(this);

		// Inicializa o jogador e define a posição de spawn
		player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this);
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

		// Inicializa as sobreposições de pausa, fim de jogo, nível concluído e jogo concluído
		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
		gameCompletedOverlay = new GameCompletedOverlay(this);
	}

	@Override
	public void update() {
		if (paused) {
			pauseOverlay.update();
		} else if (lvlCompleted) {
			levelCompletedOverlay.update();
		} else if (gameCompleted){
			gameCompletedOverlay.update();
		} else if (gameOver) {
			gameOverOverlay.update();
		} else if (playerDying) {
			player.update();
		} else {
			levelManager.update();
			player.update();
			objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
		}
		checkCloseToBorder();
	}

	// Verifica se o jogador está próximo das bordas da tela para ajustar a rolagem do nível
	private void checkCloseToBorder() {
		int playerX = (int) player.getHitbox().x;
		int diff = playerX - xLvlOffset;

		if (diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		else if (diff < leftBorder)
			xLvlOffset += diff - leftBorder;

		if (xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if (xLvlOffset < 0)
			xLvlOffset = 0;
	}

	@Override
	public void draw(Graphics g) {
		// Desenha o fundo do jogo
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

		// Desenha o nível e seus elementos
		drawClouds(g);
		levelManager.draw(g, xLvlOffset);
		enemyManager.draw(g, xLvlOffset);
		objectManager.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);

		// Desenha as sobreposições de status do jogo
		if (paused) {
			pauseOverlay.draw(g);
		} else if (gameOver) {
			gameOverOverlay.draw(g);
		} else if (lvlCompleted) {
			levelCompletedOverlay.draw(g);
		} else if (gameCompleted){
			gameCompletedOverlay.draw(g);
		}
	}

	// Desenha nuvens no fundo do jogo
	private void drawClouds(Graphics g) {
		for (int i = 0; i < smallCloudsPos.length; i++)
			g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 2 * i - (int) (xLvlOffset * 0.7), smallCloudsPos[i],
					SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
	}

	// Reinicia todos os estados e reseta os elementos do nível
	public void resetAll() {
		gameOver = false;
		lvlCompleted = false;
		playerDying = false;
		player.resetAll();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObjects();
	}

	// Define o estado de fim de jogo
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}


	public void unpauseGame() { paused = false; }

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!gameOver) {
			if (e.getButton() == MouseEvent.BUTTON1)
				player.setAttacking(true);
			else if (e.getButton() == MouseEvent.BUTTON3)
				player.powerAttack();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(true);
				break;
			case KeyEvent.VK_D:
				player.setRight(true);
				break;
			case KeyEvent.VK_SPACE, KeyEvent.VK_W:
				player.setJump(true);
				break;
			case KeyEvent.VK_ESCAPE:
				paused = !paused;
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_A:
					player.setLeft(false);
					break;
				case KeyEvent.VK_D:
					player.setRight(false);
					break;
				case KeyEvent.VK_SPACE, KeyEvent.VK_W:
					player.setJump(false);
					break;
			}
		}
	}

	public void checkSpikesTouched(Player p) {
		objectManager.checkSpikesTouched(p);
	}

	public void checkPotionTouched(Rectangle2D.Float hitbox) {
		objectManager.checkObjectTouched(hitbox);
	}

	public void checkObjectHit(Rectangle2D.Float attackBox) {
		objectManager.checkObjectHit(attackBox);
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}

	// Métodos de eventos do mouse para controle de interface
	public void mouseDragged(MouseEvent e) {
		if (!gameOver && !lvlCompleted)
			if (paused)
				pauseOverlay.mouseDragged(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (gameOver)
			gameOverOverlay.mousePressed(e);
		else if (paused)
			pauseOverlay.mousePressed(e);
		else if (lvlCompleted)
			levelCompletedOverlay.mousePressed(e);
		else if (gameCompleted)
			gameCompletedOverlay.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (gameOver)
			gameOverOverlay.mouseReleased(e);
		else if (paused)
			pauseOverlay.mouseReleased(e);
		else if (lvlCompleted)
			levelCompletedOverlay.mouseReleased(e);
		else if (gameCompleted)
			gameCompletedOverlay.mouseReleased(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (gameOver)
			gameOverOverlay.mouseMoved(e);
		else if (paused)
			pauseOverlay.mouseMoved(e);
		else if (lvlCompleted)
			levelCompletedOverlay.mouseMoved(e);
		else if (gameCompleted)
			gameCompletedOverlay.mouseMoved(e);
	}

	// Métodos de acesso para outras classes do jogo
	public Player getPlayer() { return player; }
	public EnemyManager getEnemyManager() { return enemyManager; }
	public ObjectManager getObjectManager() { return objectManager; }
	public LevelManager getLevelManager() { return levelManager; }

	// Configura o offset máximo do nível
	public void setMaxLvlOffset(int lvlOffset) { this.maxLvlOffsetX = lvlOffset; }

	// Define o estado de nível concluído e avança para o próximo nível ou finaliza o jogo
	public void setLevelCompleted(boolean levelCompleted) {
		game.getAudioPlayer().lvlCompleted();
		if (levelManager.getLevelIndex() + 1 >= levelManager.getAmountOfLevels()) {
			// Sem mais níveis
			gameCompleted = true;
			levelManager.setLevelIndex(0);
			levelManager.loadNextLevel();
			resetAll();
			return;
		}
		this.lvlCompleted = levelCompleted;
	}

	// Define e reseta o estado de jogo concluído
	public void setPlayerDying(boolean playerDying) { this.playerDying = playerDying; }
	public void resetGameCompleted() { gameCompleted = false; }
}
