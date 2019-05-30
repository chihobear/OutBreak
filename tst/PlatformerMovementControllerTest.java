import static controller.PlatformerMovementController.collision;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import controller.PlatformerMovementController;
import javafx.scene.shape.Rectangle;
import model.Coin;
import model.Collectable;
import model.LevelScene;
import model.MainCharacter;
import model.Portal;
import model.Projectile;
import model.Structure;
import model.enemies.Boss;
import model.enemies.Enemy;
import model.structures.DestructableStructure;

public class PlatformerMovementControllerTest {

	private PlatformerMovementController controller;

	@Before
	public void controllerSetup() {
		controller = new PlatformerMovementController(new LevelScene(null, 0, 0, 500, 500, new Rectangle(), new LinkedList<>()));
	}

	@Test
	public void testCollision() {
		Structure item1 = new Structure(new Rectangle(10, 10));
		Structure item2 = new Structure(new Rectangle(10, 10));
		assertEquals(collision(item1, item2)[0].size(), 1);
		item1.getComponents().get(0).setTranslateX(5);
		assertEquals(collision(item1, item2)[0].size(), 1);
		item1.getComponents().get(0).setTranslateX(11);
		assertEquals(collision(item1, item2)[0].size(), 0);
	}

	@Test
	public void testMoveItemX() {
		Structure s = new Structure(new Rectangle(10, 10));
		MainCharacter mc = new MainCharacter(0, new Rectangle(10, 10));
		s.getComponents().get(0).setTranslateX(20);
		mc.setXVelocity(6);
		controller.getCurrentScene().addItem(s);
		controller.moveItemX(mc);
		controller.moveItemX(mc);
		assertTrue(Math.abs(mc.getTranslateXValue() - 9) < 0.01);
		controller.getCurrentScene().removeItem(s);
		controller.moveItemX(mc);
		controller.moveItemX(mc);
		controller.moveItemX(mc);
		controller.moveItemX(mc);
		controller.moveItemX(mc);
		controller.moveItemX(mc);
		controller.getCurrentScene().addItem(s);
		mc.setXVelocity(-6);
		controller.moveItemX(mc);
		controller.moveItemX(mc);
		controller.moveItemX(mc);
		controller.moveItemX(mc);
		controller.moveItemX(mc);
		controller.moveItemX(mc);
		controller.moveItemX(mc);
		controller.moveItemX(mc);
		controller.moveItemX(mc);
		controller.moveItemX(mc);
		controller.moveItemX(mc);
		controller.moveItemX(mc);
		// player will pass through platforms if placed inside
		assertTrue(Math.abs(mc.getTranslateXValue() - 31) < 0.01);
		s.getComponents().get(0).setTranslateX(25);
		controller.moveItemX(mc);
		s.getComponents().get(0).setTranslateX(19);
		mc.setXVelocity(6);
		controller.moveItemX(mc);
		assertTrue(Math.abs(mc.getTranslateXValue() - 31) < 0.01);

		controller.getCurrentScene().removeItem(s);
		mc.setXVelocity(-6);
		Projectile p = new Projectile(null, 0, 0, 6, 0, new Rectangle(10, 10));
		controller.getCurrentScene().addItem(p);
		controller.moveItemX(mc);
		assertEquals(controller.getItemsToDelete().size(), 0);
		controller.moveItemX(mc);
		assertEquals(controller.getItemsToDelete().size(), 0);
		controller.moveItemX(mc);
		assertEquals(controller.getItemsToDelete().size(), 0);
		controller.moveItemX(mc);
		assertEquals(controller.getItemsToDelete().size(), 1);
		controller.getItemsToDelete().forEach(item -> controller.getCurrentScene().removeItem(item));
		controller.getItemsToDelete().clear();
		controller.getCurrentScene().addItem(s);

		// projectile hitting block from right
		p = new Projectile(null, 0, 0, -6, 0, new Rectangle(10, 10));
		p.setTranslateXAmount(40);
		controller.moveItemX(p);
		assertFalse(controller.getItemsToDelete().contains(p));
		controller.moveItemX(p);
		assertTrue(controller.getItemsToDelete().contains(p));
		controller.getItemsToDelete().forEach(item -> controller.getCurrentScene().removeItem(item));
		controller.getItemsToDelete().clear();

		// projectile hitting block from left
		p = new Projectile(null, 0, 0, 6, 0, new Rectangle(10, 10));
		controller.moveItemX(p);
		assertFalse(controller.getItemsToDelete().contains(p));
		controller.moveItemX(p);
		assertTrue(controller.getItemsToDelete().contains(p));
		controller.getItemsToDelete().forEach(item -> controller.getCurrentScene().removeItem(item));
		controller.getItemsToDelete().clear();
		controller.getCurrentScene().removeItem(s);

		// projectile hitting block from right
		s = new DestructableStructure(1, null, new Rectangle(10, 10));
		s.getComponents().get(0).setTranslateX(20);
		controller.getCurrentScene().addItem(s);
		p = new Projectile(null, 0, 0, -6, 0, new Rectangle(10, 10));
		p.setTranslateXAmount(40);
		controller.moveItemX(p);
		assertFalse(controller.getItemsToDelete().contains(p));
		controller.moveItemX(p);
		assertTrue(controller.getItemsToDelete().contains(p));
		assertFalse(controller.getItemsToDelete().contains(s));
		controller.getItemsToDelete().forEach(item -> controller.getCurrentScene().removeItem(item));
		controller.getItemsToDelete().clear();

		// projectile hitting block from left
		p = new Projectile(null, 0, 0, 6, 0, new Rectangle(10, 10));
		controller.moveItemX(p);
		assertFalse(controller.getItemsToDelete().contains(p));
		controller.moveItemX(p);
		assertTrue(controller.getItemsToDelete().contains(p));
		assertFalse(controller.getItemsToDelete().contains(s));
		controller.getItemsToDelete().forEach(item -> controller.getCurrentScene().removeItem(item));
		controller.getItemsToDelete().clear();

		// projectile hitting block from left
		p = new Projectile(null, 1000, 0, 6, 0, new Rectangle(10, 10));
		controller.moveItemX(p);
		assertFalse(controller.getItemsToDelete().contains(p));
		controller.moveItemX(p);
		assertTrue(controller.getItemsToDelete().contains(p));
		assertTrue(controller.getItemsToDelete().contains(s));
		controller.getItemsToDelete().forEach(item -> controller.getCurrentScene().removeItem(item));
		controller.getItemsToDelete().clear();
		controller.getCurrentScene().removeItem(s);

		Boss boss = new Boss(null, 0, 0, 0, 0, new Rectangle(10, 10));
		boss.setXVelocity(1);
		p = boss.fire();
		controller.getCurrentScene().addItem(boss);
		controller.getCurrentScene().addItem(p);
		p.setTranslateXAmount(p.getTranslateXValue() - 5);
		p.setTranslateYAmount(0);
		controller.moveItemX(p);
		assertFalse(controller.getItemsToDelete().contains(boss));

	}

	@Test
	public void testMoveItemY() {
		Structure s = new Structure(new Rectangle(10, 10));
		MainCharacter mc = new MainCharacter(0, new Rectangle(10, 10));
		s.getComponents().get(0).setTranslateY(20);
		mc.setYVelocity(6);
		controller.getCurrentScene().addItem(s);
		controller.moveItemY(mc);
		controller.moveItemY(mc);
		assertTrue(Math.abs(mc.getTranslateYValue() - 9) < 0.01);
		controller.getCurrentScene().removeItem(s);
		controller.moveItemY(mc);
		controller.moveItemY(mc);
		controller.moveItemY(mc);
		controller.moveItemY(mc);
		controller.moveItemY(mc);
		controller.moveItemY(mc);
		controller.getCurrentScene().addItem(s);
		mc.setYVelocity(-6);
		controller.moveItemY(mc);
		controller.moveItemY(mc);
		controller.moveItemY(mc);
		controller.moveItemY(mc);
		controller.moveItemY(mc);
		controller.moveItemY(mc);
		controller.moveItemY(mc);
		controller.moveItemY(mc);
		controller.moveItemY(mc);
		controller.moveItemY(mc);
		controller.moveItemY(mc);
		controller.moveItemY(mc);
		// player will pass through platforms if placed inside
		assertTrue(Math.abs(mc.getTranslateYValue() - 30) < 0.01);
		s.getComponents().get(0).setTranslateY(25);
		controller.moveItemY(mc);
		s.getComponents().get(0).setTranslateY(19);
		mc.setYVelocity(6);
		controller.moveItemY(mc);
		assertTrue(Math.abs(mc.getTranslateYValue() - 30) < 0.01);

		controller.getCurrentScene().removeItem(s);
		mc.setTranslateYAmount(30);
		mc.setYVelocity(-6);
		Portal p = new Portal(new LevelScene(null, 0, 0, 0, 0, new Rectangle(), new LinkedList<>()), new Rectangle(10, 10));
		controller.getCurrentScene().addItem(p);
		controller.moveItemY(mc);
		assertEquals(controller.getItemsToDelete().size(), 0);
		controller.moveItemY(mc);
		assertEquals(controller.getItemsToDelete().size(), 0);
		controller.moveItemY(mc);
		assertEquals(controller.getItemsToDelete().size(), 0);
		controller.moveItemY(mc);
		assertFalse(controller.getNextScene() == null);
		controller.getItemsToDelete().forEach(item -> controller.getCurrentScene().removeItem(item));
		controller.getItemsToDelete().clear();

		controller.getCurrentScene().removeItem(p);
		mc.setTranslateYAmount(30);
		mc.setYVelocity(-6);
		Collectable p1 = new Coin(new Rectangle(10, 10));
		controller.getCurrentScene().addItem(p1);
		controller.moveItemY(mc);
		assertEquals(controller.getItemsToDelete().size(), 0);
		controller.moveItemY(mc);
		assertEquals(controller.getItemsToDelete().size(), 0);
		controller.moveItemY(mc);
		assertEquals(controller.getItemsToDelete().size(), 0);
		controller.moveItemY(mc);
		assertEquals(controller.getItemsToDelete().size(), 1);
		controller.getItemsToDelete().forEach(item -> controller.getCurrentScene().removeItem(item));
		controller.getItemsToDelete().clear();

		controller.getCurrentScene().removeItem(p1);
		mc.setTranslateYAmount(30);
		mc.setYVelocity(-6);
		Enemy p11 = new Enemy(null, 0, 0, new Rectangle(10, 10));
		controller.getCurrentScene().addItem(p11);
		controller.moveItemY(mc);
		assertEquals(controller.getItemsToDelete().size(), 0);
		controller.moveItemY(mc);
		assertEquals(controller.getItemsToDelete().size(), 0);
		controller.moveItemY(mc);
		assertEquals(controller.getItemsToDelete().size(), 0);
		controller.moveItemY(mc);
		assertTrue(mc.isInvincible());
		controller.moveItemY(mc);
		controller.getItemsToDelete().forEach(item -> controller.getCurrentScene().removeItem(item));
		controller.getItemsToDelete().clear();

		controller.getCurrentScene().addItem(s);

		// projectile hitting block from right
		Projectile p111 = new Projectile(null, 0, 0, 0, -6, new Rectangle(10, 10));
		p111.setTranslateYAmount(40);
		controller.moveItemY(p111);
		assertFalse(controller.getItemsToDelete().contains(p111));
		controller.moveItemY(p111);
		assertTrue(controller.getItemsToDelete().contains(p111));
		controller.getItemsToDelete().forEach(item -> controller.getCurrentScene().removeItem(item));
		controller.getItemsToDelete().clear();

		// projectile hitting block from left
		p111 = new Projectile(null, 0, 0, 0, 6, new Rectangle(10, 10));
		controller.moveItemY(p111);
		assertFalse(controller.getItemsToDelete().contains(p111));
		controller.moveItemY(p111);
		assertTrue(controller.getItemsToDelete().contains(p111));
		controller.getItemsToDelete().forEach(item -> controller.getCurrentScene().removeItem(item));
		controller.getItemsToDelete().clear();
		controller.getCurrentScene().removeItem(s);

	}

	@Test
	public void testHandleItemMovements() {
		LevelScene original = controller.getCurrentScene();
		Structure s = new Structure(new Rectangle(300, 10));
		s.getComponents().get(0).setTranslateY(13);
		MainCharacter mc = new MainCharacter(0, new Rectangle(10, 10));
		mc.setHealth(5000);
		mc.setXVelocity(10);
		mc.setProjectileXSpeed(100);
		mc.setProjectileDamage(0);
		Collectable coin = new Coin(new Rectangle(10, 10));
		coin.getComponents().get(0).setTranslateX(10);
		Boss boss = new Boss(null, 0, 10, 100, 0, new Rectangle(10, 10));
		boss.setTranslateXAmount(20);
		boss.setCanJump(true);
		Portal portal = new Portal(new LevelScene(null, 0, 0, 0, 0, new Rectangle(), new LinkedList<>()), new Rectangle(10, 10));
		portal.getComponents().get(0).setTranslateX(30);
		controller.getCurrentScene().addItem(s);
		controller.getCurrentScene().addItem(mc);
		controller.getCurrentScene().addItem(coin);
		controller.getCurrentScene().addItem(boss);
		controller.getCurrentScene().addItem(portal);
		controller.handleItemMovements();
		controller.handleItemMovements();
		controller.handleItemMovements();
		controller.handleItemMovements();
		controller.handleItemMovements();
		assertTrue(controller.getCurrentScene() != original);
		assertTrue(mc.getHealth() < 5000);
		mc.setXVelocity(-100);
		controller.handleItemMovements();
		assertFalse(controller.getCurrentScene().getItems().contains(mc));
	}

}
