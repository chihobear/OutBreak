package model;

/**
 * Represents a destructable thing in the platformer. Destructables may carry
 * collectables inside.
 * 
 * @author Chenghao Xiong
 *
 */
public interface Destructable {
	/**
	 * @return the health of the object
	 */
	int getHealth();

	/**
	 * Sets the health of the object.
	 * 
	 * @param newHealth
	 */
	void setHealth(int newHealth);

	/**
	 * @return the collectable dropped by the destructable upon death
	 */
	default Collectable getItem() {
		return null;
	}
}
