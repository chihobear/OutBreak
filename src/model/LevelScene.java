package model;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import javafx.scene.shape.Rectangle;
import model.enemies.Enemy;
import view.Level;

/**
 * Represents a single piece of a level, with some start and end.
 * 
 * @author Bohan Li
 *
 */
public class LevelScene extends Observable implements Serializable {
	private int width;
	private int height;

	private transient Rectangle background;

	// necessary features for serialization
	private static final long serialVersionUID = 1L;
	private Class<? extends Level> loader;
	private HashSet<Point> collectables;
	private HashSet<Point> structures;
	private HashMap<String, List<Number>> enemies;
	// player properties for serialization
	private MainCharacter savedPlayer;
	private int health;
	private int money;
	private int startX;
	private int startY;

	// enemies, collectables, obstacles, etc.
	private transient List<LevelItem> items;

	// set of moving items, used set rather than list for superior removal
	// performance
	private transient Set<MovingItem> movingItems;

	public LevelScene(Class<? extends Level> loader, int startX, int startY, int width, int height, Rectangle background, List<LevelItem> items) {
		this.width = width;
		this.height = height;
		this.setBackground(background);
		this.items = items;
		this.startX = startX;
		this.startY = startY;
		this.loader = loader;

		this.movingItems = new HashSet<>();
		items.forEach(item -> {
			if (item instanceof MovingItem) {
				movingItems.add((MovingItem) item);
			}
		});
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

	/**
	 * Adds an item to the scene.
	 * 
	 * @param addedItem
	 */
	public synchronized void addItem(LevelItem addedItem) {
		if (addedItem == null) return;
		items.add(addedItem);
		if (addedItem instanceof MovingItem) movingItems.add((MovingItem) addedItem);
		setChanged();
		notifyObservers(new ItemAddedMessage(addedItem));
	}

	/**
	 * @param index
	 *            the index in the scene's items to delete
	 */
	public synchronized void removeItem(int index) {
		LevelItem item = items.remove(index);
		if (item != null) {
			if (item instanceof MovingItem) movingItems.remove(item);
			setChanged();
			notifyObservers(new ItemRemovedMessage(item));
		}
	}

	/**
	 * @param removedItem
	 *            the item to remove from the list
	 */
	public synchronized void removeItem(LevelItem removedItem) {
		boolean removed = items.remove(removedItem);
		if (removed) {
			if (removedItem instanceof MovingItem) movingItems.remove(removedItem);
			setChanged();
			notifyObservers(new ItemRemovedMessage(removedItem));
		}
	}

	public synchronized List<LevelItem> getItems() {
		return Collections.unmodifiableList(items);
	}

	public synchronized Set<MovingItem> getMovingItems() {
		return movingItems;
	}

	public Rectangle getBackground() {
		return background;
	}

	public void setBackground(Rectangle background) {
		this.background = background;
	}

	/**
	 * Message to send to the view, with item deleted.
	 * 
	 * @author Bohan Li
	 *
	 */
	public class ItemRemovedMessage {
		private LevelItem deletedItem;

		public ItemRemovedMessage(LevelItem deletedItem) {
			this.deletedItem = deletedItem;
		}

		public LevelItem getDeletedItem() {
			return deletedItem;
		}
	}

	/**
	 * Message to send to the view upon item added.
	 * 
	 * @author Bohan Li
	 *
	 */
	public class ItemAddedMessage {
		private LevelItem deletedItem;

		public ItemAddedMessage(LevelItem deletedItem) {
			this.deletedItem = deletedItem;
		}

		public LevelItem getDeletedItem() {
			return deletedItem;
		}
	}

	/**
	 * Initializes necessary fields for serialization.
	 * 
	 * @param player
	 *            the player whose info needs to be stored.
	 */
	public void serializePrep(MainCharacter player) {
		startX = (int) player.getTranslateXValue();
		startY = (int) player.getTranslateYValue();
		health = player.getHealth();
		money = player.getMoney();
		savedPlayer = player;
		collectables = new HashSet<>();
		structures = new HashSet<>();
		enemies = new HashMap<>();
		items.forEach(item -> {
			if (item instanceof Collectable) collectables.add(new Point((int) item.getComponents().get(0).getTranslateX(), (int) item.getComponents().get(0).getTranslateY()));
			else if (item instanceof Structure) structures.add(new Point((int) item.getComponents().get(0).getTranslateX(), (int) item.getComponents().get(0).getTranslateY()));
			else if (item instanceof Enemy) {
				Enemy enemy = (Enemy) item;
				List<Number> data = new ArrayList<>();
				data.add(enemy.getHealth());
				data.add(enemy.getXVelocity());
				data.add(enemy.getYVelocity());
				data.add(enemy.getTranslateXValue());
				data.add(enemy.getTranslateYValue());

				enemies.put(enemy.getName(), data);
			}
		});
	}

	/**
	 * Updates items based on a saved scene.
	 * 
	 * @param loadedScene
	 *            scene containing serialized data that has been prepared by
	 *            serializePrep
	 */
	public void loadDataFromExisting(LevelScene loadedScene) {
		List<LevelItem> itemsToDelete = new ArrayList<>();
		startX = loadedScene.getStartX();
		startY = loadedScene.getStartY();
		items.forEach(item -> {
			if (item instanceof Collectable) {
				if (!loadedScene.getCollectables().contains(new Point((int) item.getComponents().get(0).getTranslateX(), (int) item.getComponents().get(0).getTranslateY()))) {
					itemsToDelete.add(item);
				}
			} else if (item instanceof Structure) {
				if (!loadedScene.getStructures().contains(new Point((int) item.getComponents().get(0).getTranslateX(), (int) item.getComponents().get(0).getTranslateY()))) {
					itemsToDelete.add(item);
				}
			} else if (item instanceof Enemy) {
				Enemy enemy = (Enemy) item;
				List<Number> enemyInfo = loadedScene.getEnemies().get(enemy.getName());
				if (enemyInfo == null) itemsToDelete.add(item);
				else {
					enemy.setHealth((int) enemyInfo.get(0));
					enemy.setXVelocity((int) enemyInfo.get(1));
					enemy.setYVelocity((int) enemyInfo.get(2));
					enemy.setTranslateXAmount((double) enemyInfo.get(3));
					enemy.setTranslateYAmount((double) enemyInfo.get(4));
				}
			}
		});
		itemsToDelete.forEach(item -> removeItem(item));
	}

	public Class<? extends Level> getLoader() {
		return loader;
	}

	public HashSet<Point> getCollectables() {
		return collectables;
	}

	public HashSet<Point> getStructures() {
		return structures;
	}

	public HashMap<String, List<Number>> getEnemies() {
		return enemies;
	}

	public MainCharacter getSavedPlayer() {
		return savedPlayer;
	}

	public int getHealth() {
		return health;
	}

	public int getMoney() {
		return money;
	}

}
