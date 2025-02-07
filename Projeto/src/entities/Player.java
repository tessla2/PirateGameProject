package entities;

import static utilz.Constants.EnemyConstants.ATTACK;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class Player extends Entity {

	private BufferedImage[][] animations;
	private boolean moving = false, attacking = false;
	private boolean left, right, jump;
	private int[][] lvlData;
	private float xDrawOffset = 21 * Game.SCALE;
	private float yDrawOffset = 4 * Game.SCALE;

	// Jumping / Gravity
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

	// StatusBarUI
	private BufferedImage statusBarImg;

	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);

	// Health Bar
	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);
	private int healthBarYStart = (int) (14 * Game.SCALE);
	private int healthWidth = healthBarWidth;

	//Power Attack Bar
	private int powerBarWidth = (int) (104 * Game.SCALE);
	private int powerBarHeight = (int) (2 * Game.SCALE);
	private int powerBarXStart = (int) (44 * Game.SCALE);
	private int powerBarYStart = (int) (34 * Game.SCALE);
	private int powerWidth = powerBarWidth;
	private int powerMaxValue = 100;
	private int powerValue = powerMaxValue;


	private int flipX = 0;
	private int flipW = 1;

	private boolean attackChecked;
	private boolean powerAttackActive;
	private int powerAttackTimer;
	private int powerGrowSpeed = 15;
	private int powerGrowSpeedTimer;


	private Playing playing;

	private int tileY = 0;

	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		this.state = IDLE;
		this.maxHealth = 50;
		this.currentHealth = 50;
		this.walkSpeed = Game.SCALE;
		loadAnimations();
		initHitbox(20, 27);
		initAttackBox();
	}

	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (30 * Game.SCALE), (int) (20 * Game.SCALE));
	}

	public void update() {
		updateHealthBar();
		updatePowerBar();

		if (currentHealth <= 0) {
			if (state != DEAD) {
				state = DEAD;
				animation_timer = 0;
				animation_index = 0;
				playing.setPlayerDying(true);
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
			} else if (animation_index == GetSpriteAmount(DEAD) - 2 && animation_timer >= ANIMATION_SPEED - 1) {
				playing.setGameOver(true);
				playing.getGame().getAudioPlayer().stopSong();
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
			} else
				updateAnimationTick();

			return;
		}

		updateAttackBox();

		updatePos();
		if (moving) {
			checkPotionTouched();
			checkSpikesTouched();
			checkInsideWater();
			tileY = (int) (hitbox.y / Game.TILES_SIZE);
			if (powerAttackActive) {
				powerAttackTimer++;
				if (powerAttackTimer >= 35) {
					powerAttackTimer = 0;
					powerAttackActive = false;
				}
			}
		}
		if (attacking || powerAttackActive)
			checkAttack();

		updateAnimationTick();
		setAnimation();
	}

	private void checkSpikesTouched() {
		playing.checkSpikesTouched(this);

	}

	private void checkPotionTouched() {
		playing.checkPotionTouched(hitbox);
	}

	private void checkInsideWater() {
		if (IsEntityInWater(hitbox, playing.getLevelManager().getCurrentLevel().getLevelData()))
			currentHealth = 0;
	}

	private void checkAttack() {
		if (attackChecked || animation_index != 1)
			return;
		attackChecked = true;

		if (powerAttackActive)
			attackChecked = false;

		playing.checkEnemyHit(attackBox);
		playing.checkObjectHit(attackBox);
		playing.getGame().getAudioPlayer().playAttackSound();
	}

	private void updateAttackBox() {
		if (right || (powerAttackActive && flipW == 1))
			attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 5);
		else if (left || (powerAttackActive && flipW == -1))
			attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 5);

		attackBox.y = hitbox.y + (Game.SCALE * 20);
	}

	private void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
	}

	private void updatePowerBar() {
		powerWidth = (int) ((powerValue / (float) powerMaxValue) * powerBarWidth);

		powerGrowSpeedTimer++;
		if (powerGrowSpeedTimer >= powerGrowSpeed) {
			powerGrowSpeedTimer = 0;
			changePower(1);
		}
	}

	public void render(Graphics g, int lvlOffset) {
		g.drawImage(animations[state][animation_index], (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX, (int) (hitbox.y - yDrawOffset), width * flipW, height, null);
//		drawHitbox(g, lvlOffset);
//		drawAttackBox(g, lvlOffset);
		drawUI(g);
	}

	private void drawUI(Graphics g) {
		// Background ui
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

		// Health bar
		g.setColor(Color.red);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);

		// Power Bar
		g.setColor(Color.yellow);
		g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
	}

	private void updateAnimationTick() {
		animation_timer++;
		if (animation_timer >= ANIMATION_SPEED) {
			animation_timer = 0;
			animation_index++;
			if (animation_index >= GetSpriteAmount(state)) {
				animation_index = 0;
				attacking = false;
				attackChecked = false;
			}
		}
	}

	private void setAnimation() {
		int startAni = state;

		if (moving)
			state = RUNNING;
		else
			state = IDLE;

		if (inAir) {
			if (airSpeed < 0)
				state = JUMP;
			else
				state = FALLING;
		}

		if (powerAttackActive) {
			state = ATTACK;
			animation_index = 1;
			animation_timer = 0;
			return;
		}

		if (attacking) {
			state = ATTACK_1;
			if (startAni != ATTACK_1) {
				animation_index = 1;
				animation_timer = 0;
				return;
			}
		}
		if (startAni != state)
			resetAniTick();
	}

	private void resetAniTick() {
		animation_timer = 0;
		animation_index = 0;
	}

	private void updatePos() {
		moving = false;

		if (jump)
			jump();

		if (!inAir)
			if (!powerAttackActive)
				if ((!left && !right) || (right && left))
					return;

		float xSpeed = 0;

		if (left) {
			xSpeed -= walkSpeed;
			flipX = width;
			flipW = -1;
		}
		if (right) {
			xSpeed += walkSpeed;
			flipX = 0;
			flipW = 1;
		}

		if (powerAttackActive) {
			if (!left && !right) {
				if (flipW == -1)
					xSpeed = -walkSpeed;
				else
					xSpeed = walkSpeed;
			}

			xSpeed *= 3;
		}

		if (!inAir)
			if (!IsEntityOnFloor(hitbox, lvlData))
				inAir = true;

		if (inAir && !powerAttackActive) {
			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += GRAVITY;
				updateXPos(xSpeed);
			} else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if (airSpeed > 0)
					resetInAir();
				else
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
			}

		} else
			updateXPos(xSpeed);
		moving = true;
	}

	private void jump() {
		if (inAir)
			return;
		playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
		inAir = true;
		airSpeed = jumpSpeed;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpeed) {
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			hitbox.x += xSpeed;
		else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
			if (powerAttackActive) {
				powerAttackActive = false;
				powerAttackTimer = 0;
			}

		}
	}

	public void changeHealth(int value) {
		currentHealth += value;

		if (currentHealth <= 0)
			currentHealth = 0;
		else if (currentHealth >= maxHealth)
			currentHealth = maxHealth;
	}

	public void kill() {
		currentHealth = 0;
	}

	public void changePower(int value) {
		powerValue += value;
		if (powerValue >= powerMaxValue)
			powerValue = powerMaxValue;
		else if (powerValue <= 0)
			powerValue = 0;
	}

	private void loadAnimations() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_PIRATE);
		animations = new BufferedImage[7][8];
		for (int j = 0; j < animations.length; j++)
			for (int i = 0; i < animations[j].length; i++)
				animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);

		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
	}

	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		moving = false;
		state = IDLE;
		currentHealth = maxHealth;
		powerValue = powerMaxValue;

		hitbox.x = x;
		hitbox.y = y;

		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}

	public int getTileY() {
		return tileY;
	}

	public void powerAttack() {
		if (powerAttackActive)
			return;
		if (powerValue >= 60) {
			powerAttackActive = true;
			changePower(-60);
		}

	}

}