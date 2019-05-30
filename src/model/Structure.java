package model;

import java.util.List;

import javafx.scene.Node;

/**
 * Represents a structure that the user cannot pass through. In the future,
 * stuff like blocks that can be hit to get power-ups or pipes could extend this
 * 
 * @author Chenghao Xiong
 *
 */
public class Structure extends LevelItem {

	public Structure(Node... components) {
		super(components);
	}

	public Structure(List<Node> components) {
		super(components);
	}
}
