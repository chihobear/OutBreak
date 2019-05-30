package view.levels;

import java.util.ArrayList;

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
import view.Level;

/**
 * Bonus level for the platformer.
 *
 */
public class Level_bonus extends Level {
	private static final long serialVersionUID = 1L;
	private static final int BRICK_SIZE = 60;

	private static final int PLAYER_INITIAL_X = 0;
	private static final int PLAYER_INITIAL_Y = 480;

	public static final int DESTRUCTABLE_STRUCTURE_HEALTH = 70;

	private static final int totalWidth = 2400;

	private String coins_position[] = { "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000",
			"0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000", "1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111", "1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111" };

	private String brick_position[] = { "0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000", "0000000000,0000000000,0000000000,0000000000", "1111111111,1111111111,1111111111,1111111111" };

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
		for (int i = 0; i < 240; i++) {
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
		platforms.forEach(platform -> firstScene.addItem(new Structure(platform)));
		coins.forEach(coin -> firstScene.addItem(new Coin(coin)));

		MainCharacter player = new MainCharacter(lives, createCharacter(PLAYER_INITIAL_X, PLAYER_INITIAL_Y, PLAYER_SIZE, PLAYER_SIZE, new Image("standStill.png")));
		setMainSprite(player);
		Level next = new OurTestingLevel();
		next.initContent(3);
		LevelScene scene_next = next.getFirstScene();

		Node portal = createCharacter(33 * BRICK_SIZE, 400, BRICK_SIZE, BRICK_SIZE, new Image("portal1.png"));
		setPortalSprite(portal);

		firstScene.addItem(new Portal(scene_next, portal));
		return player;
	}
}
