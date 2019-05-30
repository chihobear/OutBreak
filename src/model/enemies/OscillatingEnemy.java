package model.enemies;

import static java.lang.Math.abs;

import java.util.Arrays;
import java.util.List;

import javafx.scene.Node;

/**
 * Enemy that will walk back and forth, possibly jumping in the process.
 * 
 * @author Bohan Li
 *
 */
public class OscillatingEnemy extends Enemy {
	private final int oscillationDistance;
	private int distanceFromCenter = 0;

	public OscillatingEnemy(String name, int initialJumpVelocity, int oscillationDistance, int xVelocity, int yVelocity, List<Node> components) {
		super(name, xVelocity, yVelocity, components);
		this.oscillationDistance = oscillationDistance;
		this.initialJumpVelocity = initialJumpVelocity;
	}

	public OscillatingEnemy(String name, int initialJumpVelocity, int oscillationDistance, int xVelocity, int yVelocity, Node... components) {
		this(name, initialJumpVelocity, oscillationDistance, xVelocity, yVelocity, Arrays.asList(components));
	}

	/**
	 * Moves one pixel in the x-direction
	 * 
	 * @param movingRight
	 *            moves one pixel right if true, left otherwise
	 */
	@Override
	public void moveXpixel(boolean movingRight) {
		double offsetAmt = 0;
		for (Node component : components) {
			offsetAmt = component.getTranslateX() + (movingRight ? 1 : -1);
			distanceFromCenter += movingRight ? 1 : -1;
			component.setTranslateX(offsetAmt);
		}
		translateXamount.set(offsetAmt);

		if (distanceFromCenter >= abs(oscillationDistance)) xVelocity = -abs(xVelocity);
		if (distanceFromCenter <= -abs(oscillationDistance)) xVelocity = abs(xVelocity);
	}

}
