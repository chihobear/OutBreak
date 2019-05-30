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
import model.enemies.OscillatingEnemy;
import model.structures.DestructableStructure;
import model.upgrades.CooldownReduceUpgrade;
import model.upgrades.DamageBoostUpgrade;
import model.upgrades.RangeBoostUpgrade;
import view.Level;

public class OurTestingLevel extends Level {
	private static final long serialVersionUID = 1L;

	private static final int BRICK_SIZE = 60;

	private static final int PLAYER_INITIAL_X = 0;

	private static final int PLAYER_INITIAL_Y = 480;

	public static final int DESTRUCTABLE_STRUCTURE_HEALTH = 70;

	private static final int totalWidth = 2400;

	private transient ArrayList<Node> platforms = new ArrayList<Node>();
	private transient ArrayList<Node> destructablePlatforms = new ArrayList<Node>();

	private transient ArrayList<Ellipse> coins = new ArrayList<Ellipse>();
	private transient ArrayList<Node> enemies = new ArrayList<Node>();

	/**
	 * Initialize all the components.
	 */
	@Override
	public MainCharacter initContent(int lives) {
		// background
		Rectangle background = new Rectangle(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		ImagePattern bView = new ImagePattern(new Image("nycBackground.jpg"));
		background.setFill(bView);
		firstScene = new LevelScene(getClass(), PLAYER_INITIAL_X, PLAYER_INITIAL_Y, totalWidth, BACKGROUND_HEIGHT, background, new ArrayList<>());

		/////////////////////////////////////////////////////////////
		// Set up coins
		for (int i = 0; i < 5; i++) {
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
		coins.get(0).setTranslateX(350);
		coins.get(0).setTranslateY(510);
		coins.get(1).setTranslateX(600);
		coins.get(1).setTranslateY(480);
		coins.get(2).setTranslateX(800);
		coins.get(2).setTranslateY(300);
		coins.get(3).setTranslateX(1000);
		coins.get(3).setTranslateY(350);
		coins.get(4).setTranslateX(1800);
		coins.get(4).setTranslateY(200);

		// set up the bricks and the player model.
		for (int i = 0; i < 40; i++) {
			if (i == 7 || i == 8 || i == 10 || i == 13 || i == 18 || i == 25 || i == 28 || i == 35) {} else {
				Node platform = createCharacter(i * BRICK_SIZE, 540, BRICK_SIZE, BRICK_SIZE, new Image("stoneWall.png"));
				platforms.add(platform);
			}
		}

		for (int i = 0; i < 20; i++) {
			if (i == 25 || i == 13 || i == 5 || i == 6 || i == 14 || i == 15) {
				Node platform = createCharacter(i * BRICK_SIZE, 420, BRICK_SIZE, BRICK_SIZE, new Image("stoneWall.png"));
				platforms.add(platform);
			}
		}

		for (int i = 0; i < 20; i++) {
			if (i == 6) {
				Node platform = createCharacter(i * BRICK_SIZE, 480, BRICK_SIZE, BRICK_SIZE, new Image("stoneWall.png"));
				platforms.add(platform);
			}
		}

		// TODO change the zombie
		IntStream.builder().add(1).add(15).add(19).add(21).add(20).build().forEach(i -> {

			enemies.add(createCharacter(i * BRICK_SIZE, 420, (PLAYER_SIZE), (PLAYER_SIZE), new Image("regularZ1.png")));
		});
		int count = 0;
		for (Node n : enemies) {

			OscillatingEnemy enemy = new OscillatingEnemy("zomb" + count, 0, 100, 2, 0, n);
			firstScene.addItem(enemy);
			setZombieSprite(enemy);
			count++;
		}

		for (int i = 25; i < 35; i++) {
			Node platform = createCharacter(i * BRICK_SIZE, 360, BRICK_SIZE, BRICK_SIZE, new Image("destructable.jpg"));
			destructablePlatforms.add(platform);

		}

		// upgrade test

		Node damageBoost = createCharacter(3 * BRICK_SIZE, 360, PLAYER_SIZE, PLAYER_SIZE / 2, new Image("shotgun1.png")); // change this
		Node projCDR = createCharacter(20 * BRICK_SIZE, 360, PLAYER_SIZE / 2, PLAYER_SIZE / 2, new Image("gun2.png"));
		Node rangeBoost = createCharacter(8 * BRICK_SIZE, 480, PLAYER_SIZE, PLAYER_SIZE / 2, new Image("sniper.png"));

		// block that drops upgrades
		Node platformContainsItem = createCharacter(29 * BRICK_SIZE, 480, BRICK_SIZE, BRICK_SIZE, new Image("stoneWall.png"));
		Rectangle insideRangeBoost = new Rectangle(BRICK_SIZE, BRICK_SIZE / 2);
		insideRangeBoost.setFill(new ImagePattern(new Image("sniper.png")));
		insideRangeBoost.setTranslateX(29 * BRICK_SIZE);
		insideRangeBoost.setTranslateY(480);

		firstScene.addItem(new DamageBoostUpgrade(damageBoost));
		firstScene.addItem(new CooldownReduceUpgrade(projCDR));
		firstScene.addItem(new RangeBoostUpgrade(rangeBoost));
		firstScene.addItem(new DestructableStructure(DESTRUCTABLE_STRUCTURE_HEALTH, new RangeBoostUpgrade(insideRangeBoost), platformContainsItem));
		platforms.forEach(platform -> firstScene.addItem(new Structure(platform)));
		destructablePlatforms.forEach(platform -> firstScene.addItem(new DestructableStructure(DESTRUCTABLE_STRUCTURE_HEALTH, null, platform)));
		coins.forEach(coin -> firstScene.addItem(new Coin(coin)));

		MainCharacter player = new MainCharacter(lives, createCharacter(PLAYER_INITIAL_X, PLAYER_INITIAL_Y, PLAYER_SIZE, PLAYER_SIZE, new Image("standStill.png")));

		setMainSprite(player);

		Level next = new BossLevel();
		next.initContent(lives);
		LevelScene scene2 = next.getFirstScene();

		Node portal = createCharacter(33 * BRICK_SIZE, 480, BRICK_SIZE, BRICK_SIZE, new Image("portal1.png"));
		setPortalSprite(portal);

		firstScene.addItem(new Portal(scene2, portal));
		return player;
	}
}
