package model.upgrades;

import javafx.scene.Node;
import model.Collectable;
import model.MainCharacter;

/**
 * Upgrade that will increase the damage of the player's projectiles.
 * 
 * @author Chenghao Xiong
 *
 */
public class DamageBoostUpgrade extends Collectable {

	public static final int BOOST_AMOUNT_BASE = 20;
	public int boostAmount;

	public DamageBoostUpgrade(Node... components) {
		super(components);
		boostAmount = BOOST_AMOUNT_BASE;
	}

	@Override
	public void applyEffect(MainCharacter player) {
		player.setProjectileDamage(player.getProjectileDamage() + boostAmount);
	}

}
