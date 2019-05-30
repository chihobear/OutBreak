package controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.shape.Circle;
import model.Collectable;
import model.Destructable;
import model.LevelItem;
import model.LevelScene;
import model.MainCharacter;
import model.MovingItem;
import model.Portal;
import model.Projectile;
import model.ProjectileHurler;
import model.Structure;
import model.enemies.Enemy;

/**
 * Controls and handles all movement for the platformer.
 * 
 * @author Bohan Li
 *
 */
public class PlatformerMovementController extends Observable {
	private LevelScene currentScene;

	private LevelScene nextScene = null;
	Set<LevelItem> itemsToDelete = new HashSet<>();

	Set<LevelItem> itemsToAdd = new HashSet<>();

	public PlatformerMovementController(LevelScene currentScene) {
		this.currentScene = currentScene;
	}

	/**
	 * Handles all movements of the items at an instance in time. Handles deleting
	 * items such as projectiles that have traveled too far or enemies with no
	 * health.
	 */
	public void handleItemMovements() {
		for (MovingItem item : currentScene.getMovingItems()) {
			moveItemX(item);
			moveItemY(item);
			if (nextScene != null) {
				break;
			}
			item.simulateGravity();

			if (item.getTranslateXProperty().get() < 0 || item.getTranslateYProperty().get() > currentScene.getHeight() || item.getTranslateYProperty().get() < 0 || item.getTranslateXProperty().get() > currentScene.getWidth()) {
				itemsToDelete.add(item);
			}
			if (item instanceof Projectile) {
				if (((Projectile) item).traveledFullDistance()) itemsToDelete.add(item);
			}
			if (item instanceof Enemy) {
				if (((Enemy) item).shouldJump() && item.canJump()) {
					item.jump();
				}
			}
			if (item instanceof ProjectileHurler) {
				ProjectileHurler hurler = (ProjectileHurler) item;
				if (hurler.canFire()) {
					itemsToAdd.add(hurler.fire());
					hurler.resetProjectileCooldown();
				} else
					hurler.decrementProjectileCooldownTimer();
			}
		}
		itemsToDelete.forEach(item -> currentScene.removeItem(item));
		itemsToDelete.clear();
		itemsToAdd.forEach(item -> currentScene.addItem(item));
		itemsToAdd.clear();
		if (nextScene != null) setCurrentScene();
	}

	/**
	 * Move an item horizontally based on their x-velocity. Projectiles are assumed
	 * to impact by traveling at least some in the x-direction. Collisions are
	 * detected here.
	 */
	public void moveItemX(MovingItem movingItem) {
		int speed = movingItem.getXVelocity();
		boolean movingRight = speed > 0;
		boolean isProj = movingItem instanceof Projectile;
		boolean isMC = movingItem instanceof MainCharacter;

		for (int i = 0; i < Math.abs(speed); i++) {
			for (LevelItem item : currentScene.getItems()) {
				List<Node> collidedComponents[] = collision(movingItem, item);
				if (item instanceof Structure) {
					// If the item model touches the bricks.
					for (int j = 0; j < collidedComponents[0].size(); j++) {
						Node component1 = collidedComponents[0].get(j);
						Node component2 = collidedComponents[1].get(j);

						if (movingRight) {
							if (movingItem instanceof Projectile) {
								itemsToDelete.add(movingItem);
								if (item instanceof Destructable) handleDestructable((Destructable) item, (Projectile) movingItem);
								return;
							}
							if (Math.abs(component1.getTranslateX() + getWidth(component1) - component2.getTranslateX()) < 1) {
								movingItem.moveXpixel(false);

								return;
							}
						} else {
							// Stop when touching
							if (movingItem instanceof Projectile) {
								itemsToDelete.add(movingItem);
								if (item instanceof Destructable) handleDestructable((Destructable) item, (Projectile) movingItem);
								return;
							}
							if (Math.abs(-component1.getTranslateX() + component2.getTranslateX() + getWidth(component2)) < 1) {
								movingItem.moveXpixel(true);
								return;
							}
						}
					}
				}
				if (isProj) {
					if (item instanceof Destructable && collidedComponents[0].size() != 0) {
						handleDestructable((Destructable) item, (Projectile) movingItem);
						return;
					}
				}
				if (isMC) {
					if (item instanceof Projectile && collidedComponents[0].size() != 0) {
						handleDestructable((Destructable) movingItem, (Projectile) item);
						return;
					}
				}

			}
			movingItem.moveXpixel(movingRight);
		}

	}

	/**
	 * Handles contact between a destructable and a projectile.
	 * 
	 * @param destructable
	 * @param projectile
	 */
	@SuppressWarnings("unlikely-arg-type")
	private void handleDestructable(Destructable destructable, Projectile projectile) {
		if (destructable.equals(projectile.getOwner())) {
			return;
		}
		destructable.setHealth(destructable.getHealth() - projectile.getDamage());
		if (destructable.getHealth() <= 0) {
			itemsToDelete.add((LevelItem) destructable);
			itemsToAdd.add(destructable.getItem());
		}
		itemsToDelete.add(projectile);
	}

	/**
	 * Moves an item in the y-direction based on their y-velocity. Collisions are
	 * detected here.
	 */
	public void moveItemY(MovingItem movingItem) {

		int speed = movingItem.getYVelocity();
		boolean movingDown = speed > 0;
		boolean isMC = movingItem instanceof MainCharacter;

		for (int i = 0; i < Math.abs(speed); i++) {
			for (int k = 0; k < currentScene.getItems().size(); k++) {
				LevelItem item = currentScene.getItems().get(k);
				List<Node> collidedComponents[] = collision(movingItem, item);
				if (item instanceof Structure) {
					// If the item model touches the bricks.
					for (int j = 0; j < collidedComponents[0].size(); j++) {
						Node component1 = collidedComponents[0].get(j);
						Node component2 = collidedComponents[1].get(j);

						if (movingDown) {
							if (component1.getTranslateY() + getHeight(component1) == component2.getTranslateY()) {
								// Keep the object separated from the bricks.
								movingItem.moveYpixel(false);
								// At this time, the object model touches the ground, make it can jump again.
								movingItem.setCanJump(true);
								if (movingItem instanceof Projectile) itemsToDelete.add(movingItem);
								return;
							}
						} else {
							// If the object model hits a block, stop going up.
							if (component1.getTranslateY() == component2.getTranslateY() + getHeight(component2)) {
								if (movingItem instanceof Projectile) itemsToDelete.add(movingItem);
								return;
							}
						}
					}
				}
				if (isMC) { // effects for the player
					if (item instanceof Collectable) {
						if (collidedComponents[0].size() != 0) {// collectable has been touched
							((Collectable) item).applyEffect((MainCharacter) movingItem);
							itemsToDelete.add(item);
							return;
						}
					}
					if (item instanceof Portal) {
						if (collidedComponents[0].size() != 0) {
							nextScene = ((Portal) item).getNextScene();
							return;
						}
					}
					if (item instanceof Enemy) {
						if (collidedComponents[0].size() != 0) { // enemy has been hit, apply damage
							MainCharacter player = (MainCharacter) movingItem;
							if (!player.isInvincible()) {
								player.setHealth(player.getHealth() - ((Enemy) item).getDamage()); // apply damage
								player.resetInvincibility();
								return;
							}
						}
					}
				}
			}
			// Make a movement by one pixel.
			movingItem.setCanJump(false);
			movingItem.moveYpixel(movingDown);
		}
	}

	/**
	 * Determines whether or not this item collides with another level item
	 * 
	 * @param item1
	 * @param item2
	 * @return array of the colliding nodes if exists, null otherwise. The first
	 *         element in the array are a list of the components from item1, the
	 *         second from item2
	 */
	public static List<Node>[] collision(LevelItem item1, LevelItem item2) {
		@SuppressWarnings("unchecked")
		List<Node> collisions[] = new List[2];
		collisions[0] = new ArrayList<>();
		collisions[1] = new ArrayList<>();
		Objects.requireNonNull(item1);
		Objects.requireNonNull(item2);

		for (Node item1Component : item1.getComponents()) {
			for (Node item2Component : item2.getComponents()) {
				if (item1Component.getBoundsInParent().intersects(item2Component.getBoundsInParent())) {
					collisions[0].add(item1Component);
					collisions[1].add(item2Component);
				}
			}
		}
		return collisions;
	}

	public LevelScene getCurrentScene() {
		return currentScene;
	}

	public void setCurrentScene() {
		LevelScene oldScene = currentScene;
		this.currentScene = nextScene;
		setChanged();
		notifyObservers(new SceneChangeMessage(oldScene, nextScene));
		nextScene = null;
	}

	// helper functions for collisions, since circles are translated from their
	// center
	private static double getWidth(Node item) {
		double width = item.getBoundsInParent().getWidth();
		return item instanceof Circle ? width / 2 : width;
	}

	private static double getHeight(Node item) {
		double height = item.getBoundsInParent().getHeight();
		return item instanceof Circle ? height / 2 : height;
	}

	/**
	 * For testing.
	 * 
	 * @return scheduled list of items to delete
	 */
	public Set<LevelItem> getItemsToDelete() {
		return itemsToDelete;
	}

	/**
	 * For testing
	 * 
	 * @return next scene
	 *
	 */
	public LevelScene getNextScene() {
		return nextScene;
	}

	/**
	 * Message to alert the view upon a new scene transition.
	 * 
	 * @author Bohan Li
	 *
	 */
	public class SceneChangeMessage {
		private LevelScene newScene;
		private LevelScene oldScene;

		public SceneChangeMessage(LevelScene oldScene, LevelScene newScene) {
			this.newScene = newScene;
			this.oldScene = oldScene;
		}

		public LevelScene getNewScene() {
			return newScene;
		}

		public LevelScene getOldScene() {
			return oldScene;
		}
	}
}