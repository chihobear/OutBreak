package view;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import model.LevelItem;
import model.LevelScene;
import model.MainCharacter;
import model.enemies.Enemy;

/**
 * A level, representing a scene's content creator.
 * 
 * @author Chenghao Xiong
 *
 */
public abstract class Level implements Serializable {

	protected static final long serialVersionUID = 1L;
	public static final int BACKGROUND_WIDTH = 1400;
	public static final int BACKGROUND_HEIGHT = 600;
	public static final int MAIN_CHARACTER_SPEED = 5;
	public static final int PLAYER_SIZE = 50;
	public static final int PROJECTILE_RADIUS = 5;

	protected HashMap<LevelItem, Timeline> spriteMap = new HashMap<>();

	protected ArrayList<Node> platforms = new ArrayList<Node>();
	protected ArrayList<Node> destructablePlatforms = new ArrayList<Node>();

	protected ArrayList<Ellipse> coins = new ArrayList<Ellipse>();
	protected ArrayList<Node> enemies = new ArrayList<Node>();
	protected ArrayList<Node> enemy_still = new ArrayList<Node>();

	protected LevelScene firstScene;

	public LevelScene getFirstScene() {
		return firstScene;
	}

	/**
	 * Initializes the content for firstScene.
	 * 
	 * @param lives
	 *            the player has
	 * @return a new player
	 */
	public abstract MainCharacter initContent(int lives);

	/**
	 * Creates a main character (in shape form currently)
	 * 
	 * @param x:
	 *            x coordinate.
	 * @param y:
	 *            y coordinate.
	 * @param w:
	 *            width.
	 * @param h:
	 *            height.
	 * @param color:
	 *            color.
	 * @return
	 * @throws FileNotFoundException
	 */
	protected Node createCharacter(int x, int y, int w, int h, Image img) {
		Rectangle character = new Rectangle(w, h);
		ImagePattern bView = new ImagePattern(img);
		character.setFill(bView);
		// Set the position of these characters.
		character.setTranslateX(x);
		character.setTranslateY(y);
		// character.setFill(color);
		return character;
	}

	/**
	 * Resets data structures for initialization.
	 */
	protected void reset() {
		platforms = new ArrayList<Node>();
		destructablePlatforms = new ArrayList<Node>();
		coins = new ArrayList<Ellipse>();
		enemies = new ArrayList<Node>();
		enemy_still = new ArrayList<Node>();
	}

	/**
	 * Sets up the main character's sprite.
	 * 
	 * @param player
	 */
	protected void setMainSprite(MainCharacter player) {
		ImagePattern right0 = new ImagePattern(new Image("standStill.png"));
		ImagePattern right1 = new ImagePattern(new Image("runGun1.png"));
		ImagePattern right2 = new ImagePattern(new Image("runGun2.png"));
		ImagePattern right3 = new ImagePattern(new Image("runGun3.png"));
		ImagePattern right4 = new ImagePattern(new Image("runGun4.png"));
		ImagePattern right5 = new ImagePattern(new Image("runGun5.png"));
		ImagePattern right6 = new ImagePattern(new Image("runGun6.png"));
		ImagePattern right7 = new ImagePattern(new Image("runGun7.png"));
		ImagePattern right8 = new ImagePattern(new Image("runGun8.png"));
		ImagePattern right9 = new ImagePattern(new Image("runGun9.png"));
		ImagePattern right10 = new ImagePattern(new Image("runGun10.png"));

		ImagePattern left0 = new ImagePattern(new Image("standLStill.png"));
		ImagePattern left1 = new ImagePattern(new Image("runGunL1.png"));
		ImagePattern left2 = new ImagePattern(new Image("runGunL2.png"));
		ImagePattern left3 = new ImagePattern(new Image("runGunL3.png"));
		ImagePattern left4 = new ImagePattern(new Image("runGunL4.png"));
		ImagePattern left5 = new ImagePattern(new Image("runGunL5.png"));
		ImagePattern left6 = new ImagePattern(new Image("runGunL6.png"));
		ImagePattern left7 = new ImagePattern(new Image("runGunL7.png"));
		ImagePattern left8 = new ImagePattern(new Image("runGunL8.png"));
		ImagePattern left9 = new ImagePattern(new Image("runGunL9.png"));
		ImagePattern left10 = new ImagePattern(new Image("runGunL10.png"));

		ImagePattern jumpL = new ImagePattern(new Image("jumpleft.png"));
		ImagePattern jumpR = new ImagePattern(new Image("jumpRight.png"));

		Timeline rightTL = new Timeline(new KeyFrame(Duration.seconds(.1), evt -> {
			setUpPlayerFrame(player, left0, right0, left1, right1, jumpL, jumpR);
		}), new KeyFrame(Duration.seconds(.2), evt -> {
			if (player.isInvincible()) {
				((Rectangle) player.getComponents().get(0)).setFill(Color.TRANSPARENT);
				return;
			}
			setUpPlayerFrame(player, left0, right0, left2, right2, jumpL, jumpR);
		}), new KeyFrame(Duration.seconds(.3), evt -> {
			setUpPlayerFrame(player, left0, right0, left3, right3, jumpL, jumpR);
		}), new KeyFrame(Duration.seconds(.4), evt -> {
			if (player.isInvincible()) {
				((Rectangle) player.getComponents().get(0)).setFill(Color.TRANSPARENT);
				return;
			}
			setUpPlayerFrame(player, left0, right0, left4, right4, jumpL, jumpR);
		}), new KeyFrame(Duration.seconds(.5), evt -> {
			setUpPlayerFrame(player, left0, right0, left5, right5, jumpL, jumpR);
		}), new KeyFrame(Duration.seconds(.6), evt -> {
			if (player.isInvincible()) {
				((Rectangle) player.getComponents().get(0)).setFill(Color.TRANSPARENT);
				return;
			}
			setUpPlayerFrame(player, left0, right0, left6, right6, jumpL, jumpR);
		}), new KeyFrame(Duration.seconds(.7), evt -> {
			setUpPlayerFrame(player, left0, right0, left7, right7, jumpL, jumpR);
		}), new KeyFrame(Duration.seconds(.8), evt -> {
			if (player.isInvincible()) {
				((Rectangle) player.getComponents().get(0)).setFill(Color.TRANSPARENT);
				return;
			}
			setUpPlayerFrame(player, left0, right0, left8, right8, jumpL, jumpR);
		}), new KeyFrame(Duration.seconds(.9), evt -> {
			setUpPlayerFrame(player, left0, right0, left9, right9, jumpL, jumpR);
		}), new KeyFrame(Duration.seconds(1), evt -> {
			if (player.isInvincible()) {
				((Rectangle) player.getComponents().get(0)).setFill(Color.TRANSPARENT);
				return;
			}
			setUpPlayerFrame(player, left0, right0, left10, right10, jumpL, jumpR);
		}));

		rightTL.setCycleCount(Animation.INDEFINITE);
		rightTL.play();
		spriteMap.put(player, rightTL);
	}

	/**
	 * Helper function for setting up a player's sprite at a given frame. Parameters
	 * are images at particular frames
	 * 
	 * @param player
	 * @param leftStill
	 * @param rightStill
	 * @param leftMove
	 * @param rightMove
	 * @param leftJump
	 * @param rightJump
	 */
	private void setUpPlayerFrame(MainCharacter player, ImagePattern leftStill, ImagePattern rightStill, ImagePattern leftMove, ImagePattern rightMove, ImagePattern leftJump, ImagePattern rightJump) {
		if (player.canJump()) {
			if (player.getXVelocity() == 0) {
				if (player.isFacingRight()) {
					((Rectangle) player.getComponents().get(0)).setFill((rightStill));
				} else {
					((Rectangle) player.getComponents().get(0)).setFill((leftStill));
				}
			} else if (player.getXVelocity() > 0) {
				((Rectangle) player.getComponents().get(0)).setFill(rightMove);
			} else {
				((Rectangle) player.getComponents().get(0)).setFill(leftMove);
			}
		} else {
			((Rectangle) player.getComponents().get(0)).setFill(player.isFacingRight() ? rightJump : leftJump);
		}
	}

	/**
	 * Sets up the sprite for an enemy.
	 * 
	 * @param zombie
	 */
	protected void setZombieSprite(Enemy zombie) {
		ImagePattern p1 = new ImagePattern(new Image("regularZ1.png"));
		ImagePattern p2 = new ImagePattern(new Image("regularZ2.png"));
		ImagePattern p3 = new ImagePattern(new Image("regularZ3.png"));
		ImagePattern pleft1 = new ImagePattern(new Image("flippedZ1.png"));
		ImagePattern pleft2 = new ImagePattern(new Image("flippedZ2.png"));
		ImagePattern pleft3 = new ImagePattern(new Image("flippedZ3.png"));

		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(.1), evt -> ((Rectangle) zombie.getComponents().get(0)).setFill(zombie.getXVelocity() > 0 ? pleft1 : p1)), new KeyFrame(Duration.seconds(.2), evt -> ((Rectangle) zombie.getComponents().get(0)).setFill(zombie.getXVelocity() > 0 ? pleft2 : p2)), new KeyFrame(Duration.seconds(.3), evt -> ((Rectangle) zombie.getComponents().get(0)).setFill(zombie.getXVelocity() > 0 ? pleft3 : p3)));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	/**
	 * Sets up sprite for portal.
	 * 
	 * @param portal
	 */
	protected void setPortalSprite(Node portal) {
		ImagePattern p1 = new ImagePattern(new Image("portal1.png"));
		ImagePattern p2 = new ImagePattern(new Image("portal2.png"));
		ImagePattern p3 = new ImagePattern(new Image("portal3.png"));
		ImagePattern p4 = new ImagePattern(new Image("portal4.png"));
		ImagePattern p5 = new ImagePattern(new Image("portal5.png"));

		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(.1), evt -> ((Shape) portal).setFill(p1)), new KeyFrame(Duration.seconds(.2), evt -> ((Shape) portal).setFill(p2)), new KeyFrame(Duration.seconds(.3), evt -> ((Shape) portal).setFill(p3)), new KeyFrame(Duration.seconds(.4), evt -> ((Shape) portal).setFill(p4)), new KeyFrame(Duration.seconds(.5), evt -> ((Shape) portal).setFill(p5)));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	public HashMap<LevelItem, Timeline> getSpriteMap() {
		return spriteMap;
	}
}
