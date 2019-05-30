package model.structures;

import java.util.Arrays;
import java.util.List;

import javafx.scene.Node;
import model.Collectable;
import model.Destructable;
import model.Structure;

/**
 * Structure that can be destroyed.
 * 
 * @author Chenghao Xiong
 *
 */
public class DestructableStructure extends Structure implements Destructable {
	protected int health;
	protected Collectable item;

	public DestructableStructure(int health, Collectable item, List<Node> components) {
		super(components);
		this.item = item;
		this.health = health;
	}

	public DestructableStructure(int health, Collectable item, Node... components) {
		this(health, item, Arrays.asList(components));
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public Collectable getItem() {
		return item;
	}
}