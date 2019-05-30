package model;

import javafx.scene.Node;

/**
 * Represents a collectable item in the thing. Implementation is still needed.
 * 
 * @author Chenghao Xiong
 *
 */
public abstract class Collectable extends LevelItem {

	public Collectable(Node... components) {
		super(components);
	}

	/**
	 * Applies the collectable's power to the player. We assume that collectables
	 * can only be obtained by players.
	 * 
	 * @param player
	 *            the player to receive the effect
	 */
	public abstract void applyEffect(MainCharacter player);
}
