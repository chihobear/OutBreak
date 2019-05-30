package model.enemies;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Projectile;
import model.ProjectileHurler;

/**
 * Boss enemy for the platformer. Shoots projectiles, unlike the other enemies.
 * 
 * @author Bohan Li
 *
 */
public class Boss extends OscillatingEnemy implements ProjectileHurler {

	private static final int BOSS_PROJECTILE_CD = 10;
	private static final int BOSS_PROJECTILE_DAMAGE = 20;
	private static final int BOSS_PROJECTILE_RANGE = 1000;
	private static final int BOSS_HEALTH = 10000;
	private static final int PROJECTILE_WIDTH = 30;
	private static final int PROJECTILE_HEIGHT = 5;
	private static final int BOSS_PROJECTILE_SPEED = 30;

	private int projectileCooldownRemaining = 0; // time where the BOSS cannot fire a projectile
	private int projectileCooldown = BOSS_PROJECTILE_CD;

	public Boss(String name, int initialJumpVelocity, int oscillationDistance, int xVelocity, int yVelocity, List<Node> components) {
		super(name, initialJumpVelocity, oscillationDistance, xVelocity, yVelocity, components);
		shouldJump = true;
		health = BOSS_HEALTH;
	}

	public Boss(String name, int initialJumpVelocity, int oscillationDistance, int xVelocity, int yVelocity, Node... components) {
		super(name, initialJumpVelocity, oscillationDistance, xVelocity, yVelocity, components);
		shouldJump = true;
		health = BOSS_HEALTH;
	}

	@Override
	public boolean canFire() {
		return projectileCooldownRemaining == 0;
	}

	@Override
	public void resetProjectileCooldown() {
		projectileCooldownRemaining = projectileCooldown;
	}

	@Override
	public Projectile fire() {
		Node component = new Rectangle(PROJECTILE_WIDTH, PROJECTILE_HEIGHT, new Color(57 / 255, 255 / 255, 20 / 255, 1));

		int xVelocity = BOSS_PROJECTILE_SPEED * (int) Math.signum(this.xVelocity);
		Projectile projectile = new Projectile(this, BOSS_PROJECTILE_DAMAGE, BOSS_PROJECTILE_RANGE, xVelocity, 0, component);
		int offset = (int) (components.get(0).getBoundsInParent().getWidth() / 2);
		component.setTranslateX(xVelocity > 0 ? components.get(0).getTranslateX() + offset : components.get(0).getTranslateX() - offset);
		component.setTranslateY(components.get(0).getTranslateY() + 20);
		return projectile;
	}

	@Override
	public void decrementProjectileCooldownTimer() {
		projectileCooldownRemaining--;
	}

}