package model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Main character to play the platformer.
 *
 */
public class MainCharacter extends MovingItem implements ProjectileHurler, Destructable, Serializable {

	private static final long serialVersionUID = 1L;
	private int invincibleTimer; // player is invincible after a hit for a small bit
	private int projectileCooldownRemaining; // time where the player cannot fire a projectile

	// upgradable properties
	private int projectileCooldown;
	private int projectileDamage;
	private int projectileRange;
	private int projectileXSpeed;
	private int projectileYVelocity;

	private transient IntegerProperty money;
	private transient IntegerProperty health;
	private boolean isFacingRight; // useful for projectile direction handling
	private int lives;

	private static final int INVINCIBLE_DURATION = 120;
	private static final int PROJECTILE_BASE_COOLDOWN = 50;
	private static final int PROJECTILE_BASE_DAMAGE = 40;
	private static final int PROJECTILE_BASE_RANGE = 250;
	private static final int PROJECTILE_BASE_XVELO = 10;
	private static final int PROJECTILE_BASE_YVELO = 0;
	private static final int PLAYER_INITIAL_JUMP_VELOCITY = 28;

	private static final int PROJECTILE_WIDTH = 30;
	private static final int PROJECTILE_HEIGHT = 5;

	public MainCharacter(int lives, Node component) {
		this(lives, Arrays.asList(component));
	}

	private MainCharacter(int lives, List<Node> components) {
		super(components);
		health = new SimpleIntegerProperty();
		money = new SimpleIntegerProperty();
		health.set(100);
		money.set(0);
		this.lives = lives;
		setCanJump(true);
		isFacingRight = true;
		projectileCooldown = PROJECTILE_BASE_COOLDOWN;
		projectileDamage = PROJECTILE_BASE_DAMAGE;
		projectileRange = PROJECTILE_BASE_RANGE;
		projectileXSpeed = PROJECTILE_BASE_XVELO;
		projectileYVelocity = PROJECTILE_BASE_YVELO;
		initialJumpVelocity = PLAYER_INITIAL_JUMP_VELOCITY;
	}

	/**
	 * Creates a new player using this player's properties, with new health and
	 * money parameters.
	 * 
	 * @param components
	 *            node components for the new player
	 * @param health
	 * @param money
	 * @return new MainCharacter
	 */
	public MainCharacter cloneWithComponents(List<Node> components, int health, int money) {
		MainCharacter clone = new MainCharacter(lives, components);
		clone.setProjectileCooldown(projectileCooldown);
		clone.setProjectileDamage(projectileDamage);
		clone.setProjectileRange(projectileRange);
		clone.setProjectileXSpeed(projectileXSpeed);
		clone.setProjectileYVelocity(projectileYVelocity);

		clone.addMoney(money);
		clone.setHealth(health);

		return clone;
	}

	public int getHealth() {
		return health.get();
	}

	public void setHealth(int newHealth) {
		health.set(newHealth);
		setChanged();
		notifyObservers(health.get());
	}

	public int getMoney() {
		return money.get();
	}

	public IntegerProperty getMoneyProperty() {
		return money;
	}

	public IntegerProperty getHealthProperty() {
		return health;
	}

	public void addMoney(int newMoney) {
		money.set(money.get() + newMoney);;
	}

	public int getLives() {
		return lives;
	}

	public boolean isInvincible() {
		return invincibleTimer != 0;
	}

	public boolean canFire() {
		return projectileCooldownRemaining <= 0;
	}

	public void decrementProjectileCooldownTimer() {
		projectileCooldownRemaining--;
	}

	public void resetProjectileCooldown() {
		projectileCooldownRemaining = projectileCooldown;
	}

	public boolean isFacingRight() {
		return isFacingRight;
	}

	public void setFacingRight(boolean isFacingRight) {
		this.isFacingRight = isFacingRight;
	}

	public void decrementInvincibilityTimer() {
		invincibleTimer--;
	}

	public void resetInvincibility() {
		invincibleTimer = INVINCIBLE_DURATION;
	}

	/**
	 * Returns a projectile based on the player's current position.
	 */
	public Projectile fire() {
		Node component = new Rectangle(PROJECTILE_WIDTH, PROJECTILE_HEIGHT, Color.RED);
		int xVelocity = isFacingRight ? projectileXSpeed : -projectileXSpeed;
		Projectile projectile = new Projectile(this, projectileDamage, projectileRange, xVelocity, projectileYVelocity, component);
		double offset = components.get(0).getBoundsInParent().getWidth() / 2;
		component.setTranslateX(isFacingRight ? components.get(0).getTranslateX() + offset : components.get(0).getTranslateX() - offset);
		component.setTranslateY(components.get(0).getTranslateY() + 20);
		return projectile;
	}

	public int getProjectileCooldown() {
		return projectileCooldown;
	}

	public void setProjectileCooldown(int projectileCooldown) {
		this.projectileCooldown = projectileCooldown;
	}

	public int getProjectileDamage() {
		return projectileDamage;
	}

	public void setProjectileDamage(int projectileDamage) {
		this.projectileDamage = projectileDamage;
	}

	public int getProjectileRange() {
		return projectileRange;
	}

	public void setProjectileRange(int projectileRange) {
		this.projectileRange = projectileRange;
	}

	public int getProjectileXSpeed() {
		return projectileXSpeed;
	}

	public void setProjectileXSpeed(int projectileXSpeed) {
		this.projectileXSpeed = projectileXSpeed;
	}

	public int getProjectileYVelocity() {
		return projectileYVelocity;
	}

	public void setProjectileYVelocity(int projectileYVelocity) {
		this.projectileYVelocity = projectileYVelocity;
	}

	public void minusLives() {
		if (lives > 0) {
			lives--;
		}
	}
}
