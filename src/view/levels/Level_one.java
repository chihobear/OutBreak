package view.levels;

import java.util.ArrayList;
import java.util.stream.IntStream;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.Coin;
import model.LevelScene;
import model.MainCharacter;
import model.Portal;
import model.Structure;
import model.enemies.Enemy;
import model.enemies.OscillatingEnemy;
import model.structures.DestructableStructure;
import model.upgrades.CooldownReduceUpgrade;
import model.upgrades.DamageBoostUpgrade;
import model.upgrades.RangeBoostUpgrade;
import view.Level;

/**
 * First level in the platformer.
 *
 */
public class Level_one extends Level {
	private static final long serialVersionUID = 1L;
	private static final int BRICK_SIZE = 60;

	private static final int PLAYER_INITIAL_X = 0;
	private static final int PLAYER_INITIAL_Y = 480;

	public static final int DESTRUCTABLE_STRUCTURE_HEALTH = 70;

	private static final int totalWidth = 2400;

	private String coins_position[] = { "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000111111,1111000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000011111,1111111111", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000",
			"0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000111111" };

	private String brick_position[] = { "0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,1100000000", "0000000000,0111110001,1110001110,0001111111", "0000000001,0000000000,0000000000,0000000000", "0000011000,0011100000,0000000000,0000000000", "0000000000,0000000000,0001000000,0000000000", "1111100111,1110111011,1100011111,1101011011" };

	/**
	 * Initialize all the components.
	 */
	@Override
	public MainCharacter initContent(int lives) {
		reset();

		// background
		Rectangle background = new Rectangle(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		ImagePattern bView = new ImagePattern(new Image("nycBackground.jpg"));
		background.setFill(bView);
		firstScene = new LevelScene(getClass(), PLAYER_INITIAL_X, PLAYER_INITIAL_Y, totalWidth, BACKGROUND_HEIGHT, background, new ArrayList<>());

		/////////////////////////////////////////////////////////////
		// Set up coins
		for (int i = 0; i < 31; i++) {
			Ellipse coin = new Ellipse(6, 12);
			coins.add(coin);
			ImagePattern c1 = new ImagePattern(new Image("coinSprite1.png"));
			ImagePattern c2 = new ImagePattern(new Image("coinSprite2.png"));
			ImagePattern c3 = new ImagePattern(new Image("coinSprite3.png"));
			ImagePattern c4 = new ImagePattern(new Image("coinSprite4.png"));
			coin.setFill(c1);
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(.1), evt -> coin.setFill(c1)), new KeyFrame(Duration.seconds(.3), evt -> coin.setFill(c2)), new KeyFrame(Duration.seconds(.5), evt -> coin.setFill(c3)), new KeyFrame(Duration.seconds(.7), evt -> coin.setFill(c4)), new KeyFrame(Duration.seconds(.9), evt -> coin.setFill(c1)));
			timeline.setCycleCount(Animation.INDEFINITE);
			timeline.play();
		}
		// Set up coins.
		int coin_num = 0;
		for (int i = 0; i < coins_position.length; i++) {
			char[] position = coins_position[i].toCharArray();
			int comma_num = 0;
			for (int j = 0; j < position.length; j++) {
				if (position[j] == '1') {
					coins.get(coin_num).setTranslateX((j - comma_num) * 20);
					coins.get(coin_num).setTranslateY(i * 60 - 20);
					coin_num++;
				} else if (position[j] == ',') {
					comma_num++;
				}
			}
		}

		// set up the bricks.
		for (int i = 0; i < brick_position.length; i++) {
			char[] position = brick_position[i].toCharArray();
			int comma_num = 0;
			for (int j = 0; j < position.length; j++) {
				if (position[j] == '1') {
					Node platform = createCharacter((j - comma_num) * BRICK_SIZE, i * BRICK_SIZE, BRICK_SIZE, BRICK_SIZE, new Image("stoneWall.png"));
					platforms.add(platform);
				} else if (position[j] == ',') {
					comma_num++;
				}
			}
		}

		// TODO change the zombie
		IntStream.builder().add(10).build().forEach(i -> {
			enemies.add(createCharacter(i * BRICK_SIZE, 0, (PLAYER_SIZE), (PLAYER_SIZE), new Image("regularZ1.png")));
		});
		int count = 0;
		for (Node n : enemies) {

			OscillatingEnemy enemy = new OscillatingEnemy("zomb" + count, 0, 100, 2, 0, n);
			firstScene.addItem(enemy);
			setZombieSprite(enemy);
			count++;
		}

		enemy_still.add(createCharacter(23 * BRICK_SIZE, 440, (PLAYER_SIZE), (PLAYER_SIZE), new Image("spike.png")));
		enemy_still.add(createCharacter(14 * BRICK_SIZE, 480, (PLAYER_SIZE), (PLAYER_SIZE), new Image("spike.png")));

		// upgrade test

		Node damageBoost = createCharacter(3 * BRICK_SIZE, 360, PLAYER_SIZE, PLAYER_SIZE / 2, new Image("shotgun1.png")); // change this
		Node projCDR = createCharacter(20 * BRICK_SIZE, 240, PLAYER_SIZE, PLAYER_SIZE / 2, new Image("gun2.png"));
		Node rangeBoost = createCharacter(8 * BRICK_SIZE, 480, PLAYER_SIZE, PLAYER_SIZE / 2, new Image("sniper.png"));

		firstScene.addItem(new DamageBoostUpgrade(damageBoost));
		firstScene.addItem(new CooldownReduceUpgrade(projCDR));
		firstScene.addItem(new RangeBoostUpgrade(rangeBoost));

		platforms.forEach(platform -> firstScene.addItem(new Structure(platform)));
		destructablePlatforms.forEach(platform -> firstScene.addItem(new DestructableStructure(DESTRUCTABLE_STRUCTURE_HEALTH, null, platform)));
		coins.forEach(coin -> firstScene.addItem(new Coin(coin)));
		int nameIndex[] = new int[1];

		enemy_still.forEach(enemy -> firstScene.addItem(new Enemy("L1_" + nameIndex[0]++, 0, 0, enemy)));

		MainCharacter player = new MainCharacter(lives, createCharacter(PLAYER_INITIAL_X, PLAYER_INITIAL_Y, PLAYER_SIZE, PLAYER_SIZE, new Image("standStill.png")));
		setMainSprite(player);
		Level next = new Level_two();
		next.initContent(3);
		LevelScene scene_next = next.getFirstScene();

		Node portal = createCharacter(33 * BRICK_SIZE, 480, BRICK_SIZE, BRICK_SIZE, new Image("portal1.png"));
		setPortalSprite(portal);

		firstScene.addItem(new Portal(scene_next, portal));
		return player;
	}
}
