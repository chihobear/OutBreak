package view.levels;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.LevelScene;
import model.MainCharacter;
import model.Structure;
import model.enemies.Boss;
import view.Level;

/**
 * Boss level for the platformer.
 * 
 * @author Bohan Li
 *
 */
public class BossLevel extends Level {
	private static final long serialVersionUID = 1L;

	private static final int BRICK_SIZE = 60;

	private static final int PLAYER_INITIAL_X = 0;
	private static final int PLAYER_INITIAL_Y = 480;

	public static final int DESTRUCTABLE_STRUCTURE_HEALTH = 70;

	private static final int SCENE2_WIDTH = 3000;

	@Override
	public MainCharacter initContent(int lives) {
		Rectangle background = new Rectangle(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		ImagePattern bView = new ImagePattern(new Image("nycBackground.jpg"));
		background.setFill(bView);

		MainCharacter player = new MainCharacter(lives, createCharacter(PLAYER_INITIAL_X, PLAYER_INITIAL_Y, PLAYER_SIZE, PLAYER_SIZE, new Image("standStill.png")));
		setMainSprite(player);
		// scene 2 x

		List<Node> scene2platforms = new ArrayList<>();
		LevelScene scene2 = new LevelScene(getClass(), PLAYER_INITIAL_X, PLAYER_INITIAL_Y, SCENE2_WIDTH, BACKGROUND_HEIGHT, background, new ArrayList<>());
		Node platform = createCharacter(0 * BRICK_SIZE, 540, BRICK_SIZE, BRICK_SIZE, new Image("stoneWall.png"));
		firstScene = scene2;
		scene2.addItem(new Structure(platform));
		platform = createCharacter(1 * BRICK_SIZE, 540, BRICK_SIZE, BRICK_SIZE, new Image("stoneWall.png"));
		scene2.addItem(new Structure(platform));
		platform = createCharacter(1 * BRICK_SIZE, 480, BRICK_SIZE, BRICK_SIZE, new Image("stoneWall.png"));
		scene2.addItem(new Structure(platform));

		IntStream.range(2, 20).forEach(i -> {
			Node platform1 = createCharacter(i * BRICK_SIZE, 540, BRICK_SIZE, BRICK_SIZE, new Image("stoneWall.png"));
			scene2platforms.add(platform1);
		});
		scene2platforms.forEach(platform2 -> {
			scene2.addItem(new Structure(platform2));
		});
		Node oscillatingEnemy = createCharacter(9 * BRICK_SIZE, 480, BRICK_SIZE, BRICK_SIZE, new Image("zombie.png"));

		Boss oe = new Boss("BOSS", 30, 200, -3, 0, oscillatingEnemy);
		oe.setDamage(50);
		ImagePattern p1 = new ImagePattern(new Image("boss1.png"));
		ImagePattern p2 = new ImagePattern(new Image("boss2.png"));
		ImagePattern pLeft1 = new ImagePattern(new Image("boss3.png"));
		ImagePattern pLeft2 = new ImagePattern(new Image("boss4.png"));

		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(.1), evt -> ((Rectangle) oe.getComponents().get(0)).setFill(oe.getXVelocity() > 0 ? pLeft1 : p1)), new KeyFrame(Duration.seconds(.2), evt -> ((Rectangle) oe.getComponents().get(0)).setFill(oe.getXVelocity() > 0 ? pLeft2 : p2)));

		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();

		scene2.addItem(oe);
		return player;
	}

}
