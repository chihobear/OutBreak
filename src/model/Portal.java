package model;

import java.util.Arrays;
import java.util.List;

import javafx.scene.Node;

/**
 * Portal to the next scene in the platformer.
 * 
 * @author Chenghao Xiong
 *
 */
public class Portal extends LevelItem {
	// scene that the player goes to upon contact
	private LevelScene nextScene;

	public Portal(LevelScene nextScene, List<Node> components) {
		super(components);
		this.nextScene = nextScene;
	}

	public Portal(LevelScene nextScene, Node... components) {
		this(nextScene, Arrays.asList(components));
	}

	public LevelScene getNextScene() {
		return nextScene;
	}
}
