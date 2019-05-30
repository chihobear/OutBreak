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
import javafx.scene.shape.Shape;
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

public class Level_two extends Level {
	private static final long serialVersionUID = 1L;

	private static final int BRICK_SIZE = 60;

	private static final int PLAYER_INITIAL_X = 0;
	private static final int PLAYER_INITIAL_Y = 480;

	public static final int DESTRUCTABLE_STRUCTURE_HEALTH = 70;

	private static final int totalWidth = 2400;

	private String coins_position[] = { "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000001,1111111000", "0000000011,1111111111,1111111100,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,1111111111,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000",
			"0000000000,0000000000,0000000000,0011111111,1100000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000" };

	private String brick_position[] = { "0000000000,0000000000,0000000000,0000000000", "0000000000,0000000001,0000000000,0000001110", "1111111111,1111110000,0011001100,0011100000", "0000000000,0000000000,0000000000,0000000000", "0000000000,0000000011,0000000011,1100000000", "0000000000,0111110001,1110001110,0001111111", "0000000001,0000000000,0000000000,0000000000", "0000011000,1111100000,0000000000,0000000000", "0011100000,0000000000,0001000000,0000000000", "1100000111,0000000000,0000011000,0001000100" };

	/**
	 * Initialize all the components.
	 */
	@Override
	public MainCharacter initContent(int lives) {
		// background
		reset();
		Rectangle background = new Rectangle(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		ImagePattern bView = new ImagePattern(new Image("nycBackground.jpg"));
		background.setFill(bView);
		firstScene = new LevelScene(getClass(), PLAYER_INITIAL_X, PLAYER_INITIAL_Y, totalWidth, BACKGROUND_HEIGHT, background, new ArrayList<>());

		/////////////////////////////////////////////////////////////
		// Set up coins
		for (int i = 0; i < 48; i++) {
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
		enemy_still.add(createCharacter(PLAYER_INITIAL_X, 100, (PLAYER_SIZE), (PLAYER_SIZE), new Image("upsideDownSpikes.png")));

		// upgrade test

		Node damageBoost = createCharacter(19 * BRICK_SIZE + 15, 20, PLAYER_SIZE, PLAYER_SIZE / 2, new Image("shotgun1.png")); // change this
		Node projCDR = createCharacter(21 * BRICK_SIZE, 240, PLAYER_SIZE, PLAYER_SIZE / 2, new Image("gun2.png"));
		Node rangeBoost = createCharacter(31 * BRICK_SIZE, 200, PLAYER_SIZE, PLAYER_SIZE / 2, new Image("sniper.png"));

		// block that drops upgrades
		// A hidden tunnel
		Level next_extra = new Level_bonus();
		next_extra.initContent(3);
		LevelScene scene_next_extra = next_extra.getFirstScene();

		Node platformHidingPortal = createCharacter(BRICK_SIZE, 60, BRICK_SIZE, BRICK_SIZE, new Image("destructable.jpg"));
		Node portal_extra = createCharacter(BRICK_SIZE + 2, 60 + 2, BRICK_SIZE - 5, BRICK_SIZE - 5, new Image("portal1.png"));

		ImagePattern p1_extra = new ImagePattern(new Image("portal1.png"));
		ImagePattern p2_extra = new ImagePattern(new Image("portal2.png"));
		ImagePattern p3_extra = new ImagePattern(new Image("portal3.png"));
		ImagePattern p4_extra = new ImagePattern(new Image("portal4.png"));
		ImagePattern p5_extra = new ImagePattern(new Image("portal5.png"));

		Timeline timeline_extra = new Timeline(new KeyFrame(Duration.seconds(.1), evt -> ((Shape) portal_extra).setFill(p1_extra)), new KeyFrame(Duration.seconds(.3), evt -> ((Shape) portal_extra).setFill(p2_extra)), new KeyFrame(Duration.seconds(.5), evt -> ((Shape) portal_extra).setFill(p3_extra)), new KeyFrame(Duration.seconds(.7), evt -> ((Shape) portal_extra).setFill(p4_extra)), new KeyFrame(Duration.seconds(1), evt -> ((Shape) portal_extra).setFill(p5_extra)));
		timeline_extra.setCycleCount(Animation.INDEFINITE);
		timeline_extra.play();
		firstScene.addItem(new Portal(scene_next_extra, portal_extra));

		firstScene.addItem(new DamageBoostUpgrade(damageBoost));
		firstScene.addItem(new CooldownReduceUpgrade(projCDR));
		firstScene.addItem(new RangeBoostUpgrade(rangeBoost));
		firstScene.addItem(new DestructableStructure(DESTRUCTABLE_STRUCTURE_HEALTH, null, platformHidingPortal));
		platforms.forEach(platform -> firstScene.addItem(new Structure(platform)));
		destructablePlatforms.forEach(platform -> firstScene.addItem(new DestructableStructure(DESTRUCTABLE_STRUCTURE_HEALTH, null, platform)));
		coins.forEach(coin -> firstScene.addItem(new Coin(coin)));
		int nameIndex[] = new int[1];
		enemy_still.forEach(enemy -> firstScene.addItem(new Enemy("L2_" + nameIndex[0]++, 0, 0, enemy)));

		MainCharacter player = new MainCharacter(lives, createCharacter(PLAYER_INITIAL_X, PLAYER_INITIAL_Y, PLAYER_SIZE, PLAYER_SIZE, new Image("standStill.png")));
		setMainSprite(player);
		Level next = new Level_four();
		next.initContent(3);
		LevelScene scene_next = next.getFirstScene();

		Node portal = createCharacter(33 * BRICK_SIZE, 60, BRICK_SIZE, BRICK_SIZE, new Image("portal1.png"));
		setPortalSprite(portal);

		firstScene.addItem(new Portal(scene_next, portal));
		return player;
	}
}
