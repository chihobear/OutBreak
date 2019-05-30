package model.upgrades;

import javafx.scene.Node;
import model.Collectable;
import model.MainCharacter;

/**
 * Upgrade that will increase the range of the player's projectiles.
 * 
 * @author Chenghao Xiong
 *
 */
public class RangeBoostUpgrade extends Collectable {
	public static final int BOOST_AMOUNT_BASE = 50;
	public int boostAmount;

	public RangeBoostUpgrade(Node... components) {
		super(components);
		boostAmount = BOOST_AMOUNT_BASE;
	}

	@Override
	public void applyEffect(MainCharacter player) {
		player.setProjectileRange(player.getProjectileRange() + boostAmount);
	}

}
