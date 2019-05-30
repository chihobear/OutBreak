package model;

import java.util.Arrays;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;

/**
 * Class to represent a movable object in the level.
 * 
 * @author Chenghao Xiong
 *
 */
public abstract class MovingItem extends LevelItem {
	protected int yVelocity;
	protected int xVelocity;
	protected int initialJumpVelocity;
	private boolean canJump;

	// DoubleProperties are observable my default, which is useful for the view
	protected DoubleProperty translateXamount;
	private DoubleProperty translateYamount;

	public MovingItem() {}

	public MovingItem(int xVelocity, int yVelocity, List<Node> components) {
		super(components);
		translateXamount = new SimpleDoubleProperty();
		translateYamount = new SimpleDoubleProperty();
		translateXamount.set(components.get(0).getTranslateX());
		translateYamount.set(components.get(0).getTranslateY());

		///// Cause bug there, I annotate it. The original player is now in the right
		///// position.

		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
	}

	public MovingItem(int xVelocity, int yVelocity, Node... components) {
		this(xVelocity, yVelocity, Arrays.asList(components));
	}

	public MovingItem(List<Node> components) {
		this(0, 0, components);
	}

	public MovingItem(Node... components) {
		this(0, 0, Arrays.asList(components));
	}

	public int getXVelocity() {
		return xVelocity;
	}

	public void setXVelocity(int xVelocity) {
		this.xVelocity = xVelocity;
	}

	public int getYVelocity() {
		return yVelocity;
	}

	public void setYVelocity(int yVelocity) {
		this.yVelocity = yVelocity;
	}

	/**
	 * Moves one pixel in the x-direction
	 * 
	 * @param movingRight
	 *            moves one pixel right if true, left otherwise
	 */
	public void moveXpixel(boolean movingRight) {
		double offsetAmt = 0;
		for (Node component : components) {
			offsetAmt = component.getTranslateX() + (movingRight ? 1 : -1);
			component.setTranslateX(offsetAmt);
		}
		translateXamount.set(offsetAmt);
	}

	/**
	 * Moves one pixel in the y-direction
	 * 
	 * @param movingDown
	 *            moves one pixel down, up otherwise
	 */
	public void moveYpixel(boolean movingDown) {
		double offsetAmt = 0;
		for (Node component : components) {
			offsetAmt = component.getTranslateY() + (movingDown ? 1 : -1);
			component.setTranslateY(offsetAmt);
		}
		translateYamount.set(offsetAmt);
	}

	/**
	 * Sets the x-coordinate of the item.
	 * 
	 * @param translateX
	 *            the x-coord
	 */
	public void setTranslateXAmount(double translateX) {
		components.forEach(component -> component.setTranslateX(translateX));
		translateXamount.set(translateX);
	}

	/**
	 * Sets the y-coordinate of the item.
	 * 
	 * @param translateY
	 *            the y-coord
	 */
	public void setTranslateYAmount(double translateY) {
		components.forEach(component -> component.setTranslateY(translateY));
		translateYamount.set(translateY);
	}

	public DoubleProperty getTranslateXProperty() {
		return translateXamount;
	}

	public DoubleProperty getTranslateYProperty() {
		return translateYamount;
	}

	public double getTranslateXValue() {
		return translateXamount.doubleValue();
	}

	public double getTranslateYValue() {
		return translateYamount.doubleValue();
	}

	/**
	 * @return whether or not the item can jump
	 */
	public boolean canJump() {
		return canJump;
	}

	/**
	 * 
	 * @param canJump
	 *            allows the item to jump when this is set to true.
	 */
	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}

	/**
	 * Initializes velocity for jumping.
	 */
	public void jump() {
		yVelocity -= initialJumpVelocity;
		setCanJump(false);
	}

	/**
	 * Simulates gravity for the movingItem
	 */
	public void simulateGravity() {
		if (yVelocity < 10) {
			yVelocity++;
		}
	}
}
