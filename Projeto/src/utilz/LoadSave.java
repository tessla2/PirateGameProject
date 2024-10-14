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
	public static final String MENU_BACKGROUND = "6.png";
	public static final String PLAYING_BG_IMG = "7.png";

	// ENVIRONMENT
	public static final String BIG_CLOUDS = "big_clouds.png";
	public static final String SMALL_CLOUDS = "small_clouds.png";
	public static final String LEVEL_ATLAS = "outside_sprites.png";
	public static final String TRAP = "trap_atlas.png";
	public static final String WATER_TOP = "water_atlas_animation.png";
	public static final String WATER_BOTTOM = "water.png";

	// ENEMIES
	public static final String CRABBY_SPRITE = "crabby_sprite.png";

	// HEALTH AND PAPAPA
	public static final String STATUS_BAR = "health_power_bar.png";

	// OBJECTS
	public static final String POTIONS = "potions_sprites.png";
	public static final String CONTAINER = "objects_sprites.png";
	public static final String CANNON = "cannon_atlas.png";
	public static final String CANNON_BALL = "ball.png";

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