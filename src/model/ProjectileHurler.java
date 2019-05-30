package model;

/**
 * Represents something that has the potential to fire a projectile.
 * 
 * @author Chenghao Xiong
 *
 */
public interface ProjectileHurler {
	/**
	 * @return whether or not the item is able to fire a projectile
	 */
	boolean canFire();

	/**
	 * @return a projectile fired at the hurler
	 */
	Projectile fire();

	/**
	 * Resets the projectile cooldown before the hurler can fire.
	 */
	void resetProjectileCooldown();

	/**
	 * Decrements the cooldown timer.
	 */
	void decrementProjectileCooldownTimer();
}
