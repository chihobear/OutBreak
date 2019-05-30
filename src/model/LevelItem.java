package model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Observable;

import javafx.scene.Node;

/**
 * Represents a single item in the level.
 * 
 * @author Chenghao Xiong
 *
 */
public abstract class LevelItem extends Observable {
	// Shapes to represent the particular thing
	// For example, a staircase might consist of multiple squares
	protected transient List<Node> components;

	protected LevelItem(Node... components) {
		Objects.requireNonNull(components);
		this.components = Arrays.asList(components);
	}

	protected LevelItem(List<Node> components) {
		Objects.requireNonNull(components);
		this.components = components;
	}

	public List<Node> getComponents() {
		return components;
	}

}
