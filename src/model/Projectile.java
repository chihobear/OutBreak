package model;

import java.util.Arrays;
import java.util.List;

import javafx.scene.Node;

/**
 * Projectile in the platformer.
 * 
 * @author Chenghao Xiong
 *
 */
public class Projectile extends MovingItem {
	private int damage;
	private int range;
	private int distanceTraveledX = 0;
	private int distanceTraveledY = 0;
	private final ProjectileHurler owner;

	public Projectile(ProjectileHurler owner, int damage, int range, int xVelocity, int yVelocity, List<Node> components) {
		super(xVelocity, yVelocity, components);
		this.owner = owner;
		this.damage = damage;
		this.range = range;
	}

	public Projectile(ProjectileHurler owner, int damage, int range, int xVelocity, int yVelocity, Node... components) {
		this(owner, damage, range, xVelocity, yVelocity, Arrays.asList(components));
	}

	public Projectile(ProjectileHurler owner, int damage, int range, List<Node> components) {
		super(components);
		this.owner = owner;
		this.damage = damage;
		this.range = range;
	}

	public Projectile(ProjectileHurler owner, int damage, int range, Node... components) {
		this(owner, damage, range, Arrays.asList(components));
	}

	public int getDamage() {
		return damage;
	}

	public int getRange() {
		return range;
	}

	public ProjectileHurler getOwner() {
		return owner;
	}

	public boolean traveledFullDistance() {
		return range <= Math.max(distanceTraveledX, distanceTraveledY);
	}

	/**
	 * Projectiles can only move distance this.range before they disappear.
	 */
	@Override
	public void moveXpixel(boolean movingRight) {
		super.moveXpixel(movingRight);
		distanceTraveledX++;
	}

	/**
	 * Projectiles can only move distance this.range before they disappear.
	 */
	@Override
	public void moveYpixel(boolean movingDown) {
		super.moveYpixel(movingDown);
		distanceTraveledY++;
	}

	/**
	 * Projectiles are not affected by gravity
	 */
	@Override
	public void simulateGravity() {}
}
