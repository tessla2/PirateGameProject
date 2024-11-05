package utilz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

public class LoadSave {
	// PLAYER
	public static final String PLAYER_PIRATE = "player_sprites.png";

	// BG
	public static final String TUTORIAL_BG = "10.png";
	public static final String GAME_COMPLETED = "game_completed.png";
	public static final String MENU_BACKGROUND = "menu_background.png";
	public static final String PLAYING_BG_IMG = "5.png";
	public static final String DEATH_SCREEN = "death_screen.png";
	public static final String MENU_BG_IMG = "background_menu.png";
	public static final String COMPLETED_IMG = "completed_sprite.png";
	public static final String OPTIONS_MENU = "options_background.png";

	// ENVIRONMENT
	public static final String GRASS = "grass_atlas.png";
	public static final String BIG_CLOUDS = "big_clouds.png";
	public static final String SMALL_CLOUDS = "small_clouds.png";
	public static final String LEVEL_ATLAS = "outside_sprites.png";
	public static final String TRAP = "trap_atlas.png";
	public static final String WATER_TOP = "water_atlas_animation.png";
	public static final String WATER_BOTTOM = "water.png";
	public static final String TREE_ONE_ATLAS = "tree_one_atlas.png";
	public static final String TREE_TWO_ATLAS = "tree_two_atlas.png";

	// ENEMIES
	public static final String CRABBY_SPRITE = "crabby_sprite.png";
	public static final String SHARK_SPRITE = "shark_atlas.png";

	// HEALTH AND PAPAPA
	public static final String STATUS_BAR = "health_power_bar.png";

	// OBJECTS
	public static final String POTIONS = "potions_sprites.png";
	public static final String CONTAINER = "objects_sprites.png";
	public static final String CANNON = "cannon_atlas.png";
	public static final String CANNON_BALL = "ball.png";


	//UI
	public static final String MENU_BUTTONS = "button_atlas.png";
	public static final String PAUSE_MENU = "pause_menu.png";
	public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";

//	private static final String
//	private static final String

	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
		try {
			img = ImageIO.read(is);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}

	public static BufferedImage[] GetAllLevels() {
		URL url = LoadSave.class.getResource("/lvl");
		File file = null;

		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		File[] files = file.listFiles();
		File[] filesSorted = new File[files.length];

		for (int i = 0; i < filesSorted.length; i++)
			for (int j = 0; j < files.length; j++) {
				if (files[j].getName().equals((i + 1) + ".png"))
					filesSorted[i] = files[j];

			}

		BufferedImage[] imgs = new BufferedImage[filesSorted.length];

		for (int i = 0; i < imgs.length; i++)
			try {
				imgs[i] = ImageIO.read(filesSorted[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}

		return imgs;
	}

}