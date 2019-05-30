package model.enemies;

import java.util.Arrays;
import java.util.List;

import javafx.scene.Node;
import model.Destructable;
import model.MovingItem;

/**
 * Basic enemy class.
 * 
 * @author Bohan Li
 *
 */
public class Enemy extends MovingItem implements Destructable {
	protected int health;
	protected int damage;
	protected static int BASIC_ENEMY_HEALTH = 100;
	protected static int BASIC_ENEMY_DAMAGE = 20;
	protected String name;

	protected boolean canJump;
	protected boolean shouldJump;

	public Enemy(String name, Node... components) {
		this(name, Arrays.asList(components));
	}

	public Enemy(String name, List<Node> components) {
		this(name, 0, 0, components);
	}

	public Enemy(String name, int xVelocity, int yVelocity, List<Node> components) {
		super(xVelocity, yVelocity, components);
		health = BASIC_ENEMY_HEALTH;
		damage = BASIC_ENEMY_DAMAGE;
		canJump = false;
		shouldJump = true;
		this.name = name;
	}

	public Enemy(String name, int xVelocity, int yVelocity, Node... components) {
		this(name, xVelocity, yVelocity, Arrays.asList(components));
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int newHealth) {
		health = newHealth;
	}

	public int getDamage() {
		return damage;
	}

	public String getName() {
		return name;
	}

	public void setDamage(int d) {
		this.damage = d;
	}

	/**
	 * 
	 * @return the enemy's will to jump.
	 */
	public boolean shouldJump() {
		return shouldJump;
	}

	/**
	 * @param shouldJump
	 *            if this is set to true, the enemy will jump when it can
	 */
	public void setShouldJump(boolean shouldJump) {
		this.shouldJump = shouldJump;
	}
}
