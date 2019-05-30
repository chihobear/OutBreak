package model.upgrades;

import javafx.scene.Node;
import model.Collectable;
import model.MainCharacter;

/**
 * Upgrade for the player than can reduce the cooldown of their projectile.
 * 
 * @author Chenghao Xiong
 *
 */
public class CooldownReduceUpgrade extends Collectable {

	private static final int CDR_BASE_AMOUNT = 20;
	private int cdrAmount;

	public CooldownReduceUpgrade(Node... components) {
		super(components);
		this.cdrAmount = CDR_BASE_AMOUNT;
	}

	@Override
	public void applyEffect(MainCharacter player) {
		player.setProjectileCooldown(player.getProjectileCooldown() - cdrAmount);

	}

}
