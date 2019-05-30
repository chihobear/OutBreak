package model.enemies;

import java.util.Arrays;
import java.util.List;

import javafx.scene.Node;

/**
 * Enemy that will continuously move left without jumping, like a goomba
 * 
 * @author Bohan Li
 *
 */
public class MovingLeftEnemy extends Enemy {
	protected static final int BASIC_SPEED = 6;

	public MovingLeftEnemy(String name, int xSpeed, List<Node> components) {
		super(name, -Math.abs(xSpeed), 0, components);
	}

	public MovingLeftEnemy(String name, int xSpeed, Node... components) {
		this(name, xSpeed, Arrays.asList(components));
	}

	public MovingLeftEnemy(String name, List<Node> components) {
		this(name, BASIC_SPEED, components);
	}

	public MovingLeftEnemy(String name, Node... components) {
		this(name, Arrays.asList(components));
	}

}
