
package view;

import static view.Level.BACKGROUND_HEIGHT;
import static view.Level.BACKGROUND_WIDTH;
import static view.Level.MAIN_CHARACTER_SPEED;
import static view.Level.PLAYER_SIZE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import controller.PlatformerMovementController;
import controller.PlatformerMovementController.SceneChangeMessage;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.LevelItem;
import model.LevelScene;
import model.LevelScene.ItemAddedMessage;
import model.LevelScene.ItemRemovedMessage;
import model.MainCharacter;
import model.enemies.Boss;
import view.levels.Level_one;

/**
 * Base Mario (Only movement part)
 * 
 * @author cheng
 *
 */
public class FinalProjectView extends Application implements Observer {
	private static final String LEVEL_DATA_FILENAME = "levelSave.dat";
	private static final Level INITIAL_LEVEL = new Level_one();
	private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

	private Label money = new Label("Income: 000");
	private Label livesLabel;
	private Label pauseLabel;
	private Rectangle Health = new Rectangle(200, 25);

	private Pane mainRoot = new Pane();
	private Pane gameRoot = new Pane();
	private MediaPlayer musicPlayer;
	private MediaPlayer sfxPlayer;
	private String MAIN_THEME = "Visager_-_04_-_Factory_Time.wav"; // For example
	private Media SONG;
	private MainCharacter player;

	private PlatformerMovementController movementController;

	private Level currentLevel = new Level_one();
	private Stage primaryStage;

	private VBox mainmenu;
	private int lightitem = 0;

	private AnimationTimer timer;
	private int exitboard = 0;
	private LeaderBoard lead;

	private static final Font FONT = Font.font("courier new", FontWeight.BOLD, 20);

	private boolean pause = false;
	private boolean gameRunning = false;

	private void playSound(String fileName) {
		Media sound = new Media(new File(fileName).toURI().toString());
		sfxPlayer = new MediaPlayer(sound);
		sfxPlayer.play();
	}

	private void resetLabels() {
		livesLabel = new Label("Lives: " + Integer.toString(player.getLives()));
		livesLabel.setTextFill(Color.YELLOW);
		livesLabel.setFont(Font.font("courier new", FontWeight.BOLD, 30));

		/////////////////////////////////////////////////////////////////////////////////////////////////
		///////////// coins
		money = new Label("Income: 000");
		money.setTextFill(Color.GOLD);
		money.setFont(Font.font("courier new", FontWeight.BOLD, 30));
		money.setLayoutX(BACKGROUND_WIDTH - 200);
		money.setLayoutY(0);

		updateIncomeLabel.changed(null, null, player.getMoney());
		// update label whenever player money changes
		player.getMoneyProperty().addListener(updateIncomeLabel);

		Health.setFill(new ImagePattern(new Image("health_bar.png")));
		Health.setLayoutX(150);
		Health.setLayoutY(10);
		updateHealthBar.changed(null, null, player.getHealth());;

		// update label whenever player health changes
		player.getHealthProperty().addListener(updateHealthBar);
	}

	private void setupPauseLabel() {
		pauseLabel = new Label("PAUSE");
		pauseLabel.setTranslateX(BACKGROUND_WIDTH / 2 - 20);
		pauseLabel.setTranslateY(200);
		pauseLabel.setTextFill(Color.AQUA);
		pauseLabel.setFont(FONT);
		mainRoot.getChildren().add(pauseLabel);
	}

	private void removePauseLabel() {
		mainRoot.getChildren().remove(pauseLabel);
	}

	ChangeListener<Number> updateIncomeLabel = (obs, old, newValue) -> {
		int newMoneyVal = newValue.intValue();
		playSound("getCoinSound.wav");
		money.setText("Income: " + String.format("%03d", newMoneyVal));
	};

	ChangeListener<Number> updateHealthBar = (obs, old, newValue) -> {
		int newHealthVal = newValue.intValue();
		if (newHealthVal <= 20) {
			Health.setFill(new ImagePattern(new Image("health_bar_4.png")));
		} else if (newHealthVal <= 40) {
			Health.setFill(new ImagePattern(new Image("health_bar_3.png")));
		} else if (newHealthVal <= 60) {
			Health.setFill(new ImagePattern(new Image("health_bar_2.png")));
		} else if (newHealthVal <= 80) {
			Health.setFill(new ImagePattern(new Image("health_bar_1.png")));
		}
	};

	/**
	 * Update the player model according to the keys stored in the Hashmap.
	 */
	private void updateState() {
		gameRunning = true;
		if (pause) return;
		if (keys.getOrDefault(KeyCode.UP, false) && player.getTranslateYValue() >= MAIN_CHARACTER_SPEED) {
			if (player.canJump()) {
				player.jump();
			}
		}

		// When left arrow is pressed, move the player model to the left but never go
		// out of
		// the scene.
		if (keys.getOrDefault(KeyCode.LEFT, false) && player.getTranslateXValue() > MAIN_CHARACTER_SPEED) {
			player.setXVelocity(-MAIN_CHARACTER_SPEED);
			movementController.moveItemX(player);
			player.setFacingRight(false);
		}

		// When right arrow is pressed, move the player model to the right but never go
		// out of
		// the scene.
		if (keys.getOrDefault(KeyCode.RIGHT, false) && player.getTranslateXValue() + PLAYER_SIZE <= movementController.getCurrentScene().getWidth() - MAIN_CHARACTER_SPEED) {
			player.setXVelocity(MAIN_CHARACTER_SPEED);
			movementController.moveItemX(player);
			player.setFacingRight(true);
		}

		if (!(keys.getOrDefault(KeyCode.LEFT, false) || keys.getOrDefault(KeyCode.RIGHT, false))) {
			player.setXVelocity(0);
		}

		// fire a projectile if possible
		if (keys.getOrDefault(KeyCode.SPACE, false) && player.canFire()) {
			movementController.getCurrentScene().addItem(player.fire());
			player.resetProjectileCooldown();
		} else {
			player.decrementProjectileCooldownTimer();
		}

		player.simulateGravity();

		if (player.isInvincible()) {
			player.decrementInvincibilityTimer();
		}
		movementController.moveItemY(player);
		movementController.handleItemMovements();
	}

	@Override
	public void start(Stage primaryStage) {
		//////////// mainmenu part

		getleader();

		this.primaryStage = primaryStage;
		mainmenu(this.primaryStage);

		primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
			if (gameRunning) {
				movementController.getCurrentScene().serializePrep(player);
				saveObject(LEVEL_DATA_FILENAME, movementController.getCurrentScene());
			}
		});
		///////////
		/*
		 * this.primaryStage = primaryStage; nth_game(3); AnimationTimer timer = new
		 * AnimationTimer() {
		 * 
		 * @Override public void handle(long now) { updateState(); } }; timer.start();
		 */

	}

	/**
	 * start from main menu
	 * 
	 * @param primaryStage
	 */
	private void mainmenu(Stage primaryStage) {
		Scene scene = new Scene(mainContent(primaryStage));
		lightitem = 0;
		meunevent(scene);
	}

	/**
	 * Game over and restart menu
	 * 
	 * @param primaryStage
	 */
	private void exitmeun(Stage primaryStage) {
		Scene scene = new Scene(exitContent(primaryStage));
		lightitem = 0;
		meunevent(scene);
	}

	/**
	 * leader board menu
	 * 
	 * @param primaryStage
	 */
	private void leader(Stage primaryStage) {
		Scene scene = new Scene(leaderContent(primaryStage));
		lightitem = 0;
		meunevent(scene);
	}

	/**
	 * event for menu
	 * 
	 * @param scene
	 */
	private void meunevent(Scene scene) {
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.UP) {
				if (lightitem > 0) {
					getMenuItem(lightitem).setActive(false);
					getMenuItem(--lightitem).setActive(true);
				}
			}
			if (event.getCode() == KeyCode.DOWN) {
				if (lightitem < mainmenu.getChildren().size() - 1) {
					getMenuItem(lightitem).setActive(false);
					getMenuItem(++lightitem).setActive(true);

				}
			}
			if (event.getCode() == KeyCode.ENTER) {
				getMenuItem(lightitem).activate();
			}
		});
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * For main menu
	 * 
	 * @param itemindex
	 * @return
	 */
	private MenuItem getMenuItem(int itemindex) {
		return (MenuItem) mainmenu.getChildren().get(itemindex);
	}

	/**
	 * main menu content
	 * 
	 * @param primaryStage
	 * @return
	 */
	private Parent mainContent(Stage primaryStage) {
		Pane root = new Pane();
		root.setPrefSize(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		Rectangle title = new Rectangle(BACKGROUND_WIDTH / 7, BACKGROUND_HEIGHT / 7);
		ImagePattern mainLogo = new ImagePattern(new Image("OUTBREAK.PNG"));
		title.setFill(mainLogo);
		title.setTranslateX(590);
		title.setTranslateY(200);

		Rectangle back = new Rectangle(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		MenuItem exit = new MenuItem("EXIT");
		exit.setOnActivate(() -> System.exit(0));
		MenuItem start = new MenuItem("START");
		start.setOnActivate(() -> {

			this.primaryStage = primaryStage;

			timer = new AnimationTimer() {
				@Override
				public void handle(long now) {
					updateState();
				}
			};
			nth_game(3);
		});
		MenuItem leader = new MenuItem("LEADERBOARD");
		leader.setOnActivate(() -> {
			this.primaryStage = primaryStage;
			leader(this.primaryStage);
		});////////
		mainmenu = new VBox(10, start, leader, exit);
		mainmenu.setAlignment(Pos.TOP_CENTER);
		mainmenu.setTranslateX(580);
		mainmenu.setTranslateY(320);

		getMenuItem(0).setActive(true);
		root.getChildren().addAll(back, mainmenu, title);
		return root;
	}

	/**
	 * game over menu content
	 * 
	 * @param primaryStage
	 * @return
	 */
	private Parent exitContent(Stage primaryStage) {
		Pane root = new Pane();
		root.setPrefSize(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		Rectangle back = new Rectangle(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		Rectangle gameOver = new Rectangle(BACKGROUND_WIDTH / 7, BACKGROUND_HEIGHT / 7);
		ImagePattern endLogo = new ImagePattern(new Image("GAMEOVER.png"));
		gameOver.setFill(endLogo);
		gameOver.setTranslateX(580);
		gameOver.setTranslateY(200);
		MenuItem exit = new MenuItem("EXIT");
		exit.setOnActivate(() -> System.exit(0));
		MenuItem start = new MenuItem("PLAY AGAIN");
		start.setOnActivate(() -> {
			this.primaryStage = primaryStage;
			nth_game(3);
		});
		// add some leader board feature
		MenuItem leader = new MenuItem("LEADER");
		leader.setOnActivate(() -> {
			this.primaryStage = primaryStage;
			leader(this.primaryStage);
		});////////
		mainmenu = new VBox(10, start, leader, exit);
		mainmenu.setAlignment(Pos.TOP_CENTER);
		mainmenu.setTranslateX(580);
		mainmenu.setTranslateY(320);

		getMenuItem(0).setActive(true);
		root.getChildren().addAll(back, gameOver, mainmenu);
		return root;
	}

	/**
	 * leader board menu content
	 * 
	 * @param primaryStage
	 * @return
	 */
	private Parent leaderContent(Stage primaryStage) {
		Pane root = new Pane();
		root.setPrefSize(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		Rectangle back = new Rectangle(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		MenuItem exit = new MenuItem("EXIT");
		exit.setOnActivate(() -> System.exit(0));
		MenuItem backto = new MenuItem("BACK");
		backto.setOnActivate(() -> {
			this.primaryStage = primaryStage;
			if (exitboard == 0) mainmenu(this.primaryStage);
			else
				exitmeun(this.primaryStage);

		});
		// add some leader board feature

		String str = "Number      Name       Coins\n";
		for (int i = 0; i < lead.getname().length; i++) {
			str += "No." + (i + 1) + "      " + lead.getname()[i] + "       " + lead.getcoin()[i] + "\n";
		}
		Text lead = new Text(str);
		Text name = new Text("Leader Board");
		lead.setFont(FONT);
		lead.setFill(Color.YELLOW);
		lead.setTranslateX(420);
		lead.setTranslateY(220);
		name.setFont(FONT);
		name.setFill(Color.YELLOW);
		name.setTranslateX(400);
		name.setTranslateY(170);
		////////
		mainmenu = new VBox(10, backto, exit);
		mainmenu.setAlignment(Pos.TOP_CENTER);
		mainmenu.setTranslateX(880);
		mainmenu.setTranslateY(320);

		getMenuItem(0).setActive(true);
		root.getChildren().addAll(back, mainmenu, name, lead);
		return root;
	}

	private void getleader() {
		File file = new File("leader_board.dat");
		if (file.exists()) {
			try {
				FileInputStream file_in = new FileInputStream("leader_board.dat");
				ObjectInputStream in = new ObjectInputStream(file_in);
				lead = (LeaderBoard) in.readObject();
				in.close();
				file_in.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
				System.out.println("Leaderboard data was corrupted and could not be loaded.");
				lead = new LeaderBoard();
			}
		} else {
			lead = new LeaderBoard();
		}
	}

	private void loadLevelData(int numLives) {
		File file = new File(LEVEL_DATA_FILENAME);
		if (file.exists()) {
			try {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(LEVEL_DATA_FILENAME));
				LevelScene savedScene = (LevelScene) in.readObject();
				in.close();

				currentLevel = savedScene.getLoader().newInstance();
				player = currentLevel.initContent(numLives);
				currentLevel.getFirstScene().loadDataFromExisting(savedScene);
				currentLevel.getSpriteMap().get(player).stop();
				player = savedScene.getSavedPlayer().cloneWithComponents(player.getComponents(), savedScene.getHealth(), savedScene.getMoney());
				currentLevel.setMainSprite(player);
				player.setTranslateXAmount(currentLevel.getFirstScene().getStartX());
				player.setTranslateYAmount(currentLevel.getFirstScene().getStartY());
				return;
			} catch (IOException ex) {
				ex.printStackTrace();
				// System.exit(1);
			} catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
				System.out.println("Save data was corrupted and could not be loaded");
				// System.exit(1);
			}
		}
		player = currentLevel.initContent(numLives);
	}

	private static void saveObject(String fileName, Object obj) {
		try {
			FileOutputStream file = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(obj);
			out.close();
			file.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			// System.exit(1);
		}
	}

	/**
	 * Deletes save data file.
	 */
	private void deleteSave(String fileName) {
		new File(fileName).delete();
		currentLevel = INITIAL_LEVEL;
	}

	/**
	 * For main menu
	 * 
	 * @author wangchen
	 *
	 */
	private static class MenuItem extends HBox {
		private Text text, c1;
		private Runnable script;

		public MenuItem(String name) {
			super(15);
			setAlignment(Pos.CENTER);
			c1 = new Text("-> ");
			c1.setFont(FONT);// addfont
			c1.setEffect(new GaussianBlur(2));

			text = new Text(name);
			text.setFont(FONT);// addfont
			text.setEffect(new GaussianBlur(2));

			getChildren().addAll(c1, text);
			setActive(false);
		}

		public void setActive(boolean b) {
			c1.setFill(b ? Color.RED : Color.WHITE);
			text.setFill(b ? Color.RED : Color.WHITE);
		}

		public void setOnActivate(Runnable r) {
			script = r;
		}

		public void activate() {
			if (script != null) {
				script.run();
			}
		}

	}

	private void nth_game(int nth_live) {
		MAIN_THEME = "Visager_-_04_-_Factory_Time.wav";
		SONG = new Media(new File(MAIN_THEME).toURI().toString());
		if (musicPlayer != null) musicPlayer.stop();
		musicPlayer = new MediaPlayer(SONG);
		musicPlayer.setVolume(.5);
		musicPlayer.play();

		loadLevelData(nth_live);

		resetLabels();
		initializeNewScene(null, currentLevel.getFirstScene());
		movementController = new PlatformerMovementController(currentLevel.getFirstScene());
		movementController.addObserver(this);
		primaryStage.getScene().setRoot(new Pane());
		Scene scene = new Scene(mainRoot);

		scene.setOnKeyPressed(keyPressHandler);
		scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
		primaryStage.setTitle("Outbreak Video Game");

		primaryStage.setScene(scene);
		primaryStage.show();

		player.getHealthProperty().addListener((obs, old, newValue) -> {
			int newHealth = newValue.intValue();
			playSound("ouch.wav");
			if (newHealth <= 0) {
				handleDeath();
			}
		});

		player.getTranslateYProperty().addListener((obs, old, newValue) -> {
			int position = newValue.intValue();

			if (position == movementController.getCurrentScene().getBackground().getHeight()) {
				handleDeath();
			}
		});

		timer.start();
	}

	EventHandler<KeyEvent> keyPressHandler = event -> {
		if (event.getCode() == KeyCode.ENTER) {
			if (!pause) {
				setupPauseLabel();
			} else
				removePauseLabel();
			pause = !pause;
		}
		keys.put(event.getCode(), true);
	};

	private void handleDeath() {
		deleteSave(LEVEL_DATA_FILENAME);
		player.minusLives();
		gameRunning = false;
		int lives = player.getLives();
		if (lives == 0) {
			musicPlayer.stop();
			timer.stop();
			exitboard = 1;
			exitmeun(this.primaryStage);
		} else {
			initialization();
			nth_game(lives);
		}
	}

	private void initialization() {
		keys = new HashMap<KeyCode, Boolean>();
		mainRoot = new Pane();
		gameRoot = new Pane();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof SceneChangeMessage) {
			SceneChangeMessage message = (SceneChangeMessage) arg;
			initializeNewScene(message.getOldScene(), message.getNewScene());
		} else if (arg instanceof ItemRemovedMessage) {
			LevelItem item = ((ItemRemovedMessage) arg).getDeletedItem();
			delete(item);
			if (item instanceof Boss) {
				timer.stop();
				gameRunning = false;
				deleteSave(LEVEL_DATA_FILENAME);
				exitboard = 0;
				Text youWin = new Text("You win!");
				Label enterYourName = new Label("Enter your name: ");
				Label storename = new Label("(Only save first 8 chars)");
				youWin.setTranslateX(BACKGROUND_WIDTH / 2 - 20);
				youWin.setTranslateY(200);
				TextField userName = new TextField();
				youWin.setFill(Color.RED);
				youWin.setFont(FONT);
				enterYourName.setFont(FONT);
				enterYourName.setTextFill(Color.RED);
				storename.setFont(FONT);
				storename.setTextFill(Color.RED);
				userName.setFont(FONT);
				userName.setStyle("-fx-text-fill: white;");
				userName.setPrefWidth(400);
				userName.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(2), Insets.EMPTY)));
				userName.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(1, 1, 1, 1))));
				HBox box = new HBox(enterYourName, userName, storename);
				box.setMaxHeight(10);
				box.setMaxWidth(1200);
				box.setTranslateX(BACKGROUND_WIDTH / 5);
				box.setTranslateY(300);
				mainRoot.getChildren().add(box);
				mainRoot.getChildren().add(youWin);
				primaryStage.getScene().setOnKeyPressed(event -> {
					if (event.getCode() == KeyCode.ENTER) {
						if (lead.top5(player.getMoney())) {
							lead.settop5(userName.getText(), player.getMoney());
						}
						saveObject("leader_board.dat", lead);
						getleader();
						leader(primaryStage);
					}
				});
			}
		} else if (arg instanceof ItemAddedMessage) {
			LevelItem item = ((ItemAddedMessage) arg).getDeletedItem();
			add(item);
		}
	}

	private void delete(LevelItem item) {
		for (Node component : item.getComponents()) { // remove all components from display upon removal
			gameRoot.getChildren().remove(component);
		}
	}

	private void add(LevelItem item) {
		for (Node component : item.getComponents()) { // remove all components from display upon removal
			gameRoot.getChildren().add(component);
		}
	}

	private void initializeNewScene(LevelScene oldScene, LevelScene newScene) {
		if (oldScene != null) {
			oldScene.deleteObserver(this);
		}
		newScene.addObserver(this);
		initialization();
		gameRoot.getChildren().add(player.getComponents().get(0));
		player.setTranslateXAmount(newScene.getStartX());
		player.setTranslateYAmount(newScene.getStartY());
		newScene.getItems().forEach(item -> {
			item.getComponents().forEach(component -> {
				gameRoot.getChildren().add(component);
			});
		});

		ChangeListener<Number> screenMover = (obs, old, newValue) -> {
			int positionInScene = newValue.intValue();

			// Set the point to let the scene move.
			double move_point = newScene.getBackground().getWidth() / 2 + 50;
			if (positionInScene > move_point && positionInScene < newScene.getWidth() - move_point + 100) {
				gameRoot.setLayoutX(-(positionInScene - move_point));
			}
			if (positionInScene >= newScene.getWidth() - move_point + 100) {
				gameRoot.setLayoutX(-(newScene.getWidth() - move_point + 100 - move_point));
			}
		};
		// Let the scene move.
		player.getTranslateXProperty().addListener(screenMover);
		mainRoot.getChildren().addAll(newScene.getBackground(), livesLabel, money, Health, gameRoot);
		mainRoot.setMaxWidth(BACKGROUND_WIDTH);
		Scene scene = new Scene(mainRoot);
		screenMover.changed(null, null, player.getTranslateXValue());

		scene.setOnKeyPressed(keyPressHandler);
		scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
		primaryStage.setTitle("Mario");

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
