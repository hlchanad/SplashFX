package com.chanhonlun.splash.scene;

import java.util.LinkedList;
import java.util.List;

import com.chanhonlun.splash.application.Game;
import com.chanhonlun.splash.model.Enemy;
import com.chanhonlun.splash.model.NormalPlatform;
import com.chanhonlun.splash.model.Platform;
import com.chanhonlun.splash.model.Player;
import com.chanhonlun.splash.model.Text;
import com.chanhonlun.splash.util.EventEmitter;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class GameScene extends MyScene {

	private static final int PLAYER_GROUND_Y                         = 20;
	private static final int ENEMY_AREA_PART                         = 3; // top 1/3 height will be the enemy zone
	private static final int ENEMY_MAX_NUMBER                        = 5;
	private static final int PLATFORM_PART_NUMBER                    = 3;
    private static final int PLATFORM_NUMBER_PER_PART                = 2;
	private static final int PLATFORM_SCREEN_PADDING_TOP             = 100;
	private static final int PLATFORM_SCREEN_PADDING_BOTTOM          = 100;
	
	private static final int    ANIMATION_KEY_FRAMES_MILLIS         = 30;
	private static final double MOVE_VERTICAL_PIXEL                 = 5;
	private static final double PLAYER_PLATFORM_BOTTOM_AREA_Y       = 20;
	
	private static final int SCORE_PER_PLATFORM_BELOW_SCREEN        = 10;
	private static final int SCORE_PER_KILLING_ENEMY                = 100;
	
	public static final String ON_LOSE = "onLose";
	
	private List<Enemy> enemies;
	private Player player;
	private List<Platform> platforms;
	
	private Platform playerPlatform;
	private boolean isJumpedFirstTime = false;
	
	private Timeline moveEverythingDown;
	
	private StackPane root;
	
	private int score;
	private Text scoreLabel;
	private Text scoreText;
	
	public GameScene() {
		this.emitterMap.put(ON_LOSE, new EventEmitter<Object>());
	}
	
	@Override
	protected Scene createScene() {
		
		root = new StackPane();
		
		player = generatePlayer();
		player.getEmitter(Player.EVENT_MOVE_UP).subscribe(point -> handlePlayerMoveUp(point));
		player.getEmitter(Player.EVENT_MOVE_DOWN).subscribe(point -> handlePlayerMoveDown(point));
		player.getEmitter(Player.EVENT_MOVE_HORIZONTALLY).subscribe(point -> handlePlayerMoveHorizontally(point));
		root.getChildren().add(player.getNode());
		
		enemies = generateEnemies();
		enemies.forEach(enemy -> root.getChildren().add(enemy.getNode()));
		
		platforms = generatePlatforms();
		platforms.forEach(platform -> root.getChildren().add(platform.getNode()));

		scoreText = new Text();
		scoreText.setText("Score: ");
		scoreText.setXY(30, Game.PANE_HEIGHT - 80);
		scoreText.setTextSize(24);
		root.getChildren().add(scoreText.getNode());
		
		scoreLabel = new Text();
		scoreLabel.setXY(100, Game.PANE_HEIGHT - 80);
		scoreLabel.setTextSize(24);
		updateScoreLabel();
		root.getChildren().add(scoreLabel.getNode());
		
		setSomeNodesOnTop();
		
		Scene scene = new Scene(root, Game.PANE_WIDTH, Game.PANE_HEIGHT);

		setupMoveDownAnimation();
		setupKeyListener(scene);
		
		return scene;
	}
	
	private void updateScoreLabel() {
		scoreLabel.setText(Integer.toString(score));
	}
	
	private void setupKeyListener(Scene scene) {
		scene.setOnKeyPressed(keyEvent -> {
			if (moveEverythingDown.getStatus().equals(Status.RUNNING)) {
				return ;
			}
			player.handleKeyPressed(keyEvent);
		});
		
		scene.setOnKeyReleased(keyEvent -> {
			if (moveEverythingDown.getStatus().equals(Status.RUNNING)) {
				return ;
			}
			player.handleKeyReleased(keyEvent);
		});
	}

	private void setupMoveDownAnimation() {
		moveEverythingDown = new Timeline(new KeyFrame(
			Duration.millis(ANIMATION_KEY_FRAMES_MILLIS),
			actionEvent -> {
				List<Platform> platformsToBeRemoved = new LinkedList<Platform>();
				
				platforms.forEach(platform -> {
					double newX = platform.getXY().getX(),
						   newY = platform.getXY().getY() - MOVE_VERTICAL_PIXEL;
					platform.setXY(newX, newY);
					
					if (newY + platform.getHeight() < 0) { 
						// pending to remove platform 
						platformsToBeRemoved.add(platform);
					}
				});
				
				platformsToBeRemoved.forEach(platform -> {
					score += SCORE_PER_PLATFORM_BELOW_SCREEN;
					updateScoreLabel();
					
					platforms.remove(platform);
					root.getChildren().remove(platform.getNode());
				});
				
				double newX = player.getXY().getX(),
					   newY = player.getXY().getY() - MOVE_VERTICAL_PIXEL;
				player.setXY(newX, newY);
				
				if (playerPlatform.getXY().getY() <= PLAYER_PLATFORM_BOTTOM_AREA_Y) {
					moveEverythingDown.stop();
				}
			}
		));
		moveEverythingDown.setCycleCount(Animation.INDEFINITE);
		
	}

	private void handlePlayerMoveUp(Point2D point) {
		isJumpedFirstTime = true;
		playerPlatform = null;
	}
	
	private void handlePlayerMoveDown(Point2D point) {
		
		if (player.fallBelowGround()) {
			// TODO: emit real score later
			player.stopFalling();
			this.emitterMap.get(ON_LOSE).emit(score);
			return ;
		}
		
		for (Platform platform : platforms) {
			if (player.onPlatform(platform)) {
				player.setXY(player.getXY().getX(), platform.getXY().getY() + platform.getHeight());
				player.stopFalling();
				playerPlatform = platform;
				break;
			}
		}
		
		if (playerPlatform != null && playerPlatform.getXY().getY() > PLAYER_PLATFORM_BOTTOM_AREA_Y) {
			int numberOfPlatformsToBeRemoved = 0;
			double moveDownPixel = playerPlatform.getXY().getY() - PLAYER_PLATFORM_BOTTOM_AREA_Y;
			for (Platform platform : platforms) {
				if (platform.getXY().getY() - moveDownPixel < 0) {
					numberOfPlatformsToBeRemoved ++;
				}
			}
			
			for (int i = 0; i < numberOfPlatformsToBeRemoved; i++) {
				respawnPlatform();
			}
			
			player.stopMotion();
			moveEverythingDown.play();
		}
	}
	
	private void handlePlayerMoveHorizontally(Point2D point) {
		
		// do nothing before first jump
		if (!isJumpedFirstTime) {
			return ;
		}
		
		boolean isOnPlatform = false;
		
		for (Platform platform : platforms) {
			if (player.onPlatform(platform)) {
				isOnPlatform = true;
				break;
			}
		}
		
		if (!isOnPlatform) {
			player.fall();
			playerPlatform = null;
		}
	}
	
	private Player generatePlayer() {
		Player player = new Player();
		
		int x = (Game.PANE_WIDTH - player.getWidth()) / 2;
		int y = PLAYER_GROUND_Y;
		
		player.setXY(x, y);
		
		return player;
	}
	
	private List<Enemy> generateEnemies() {
		
		List<Enemy> enemies = new LinkedList<Enemy>();
		
		for (int i = 0; i < ENEMY_MAX_NUMBER; i++) {
			Enemy enemy = new Enemy();
			
			int x = (int) Math.floor(Math.random() * (Game.PANE_WIDTH - enemy.getWidth()));
			int y = (int) (Math.floor(Math.random() * (Game.PANE_HEIGHT / ENEMY_AREA_PART - enemy.getHeight())) + (Game.PANE_HEIGHT - Game.PANE_HEIGHT / ENEMY_AREA_PART));
			
			enemy.setXY(x, y);
			
			enemies.add(enemy);
		}
		
		return enemies;
	}
	
	private Platform generatePlatform(int part) {
		Platform platform = new NormalPlatform();
		
		int pixelsPerPart = (Game.PANE_HEIGHT - PLATFORM_SCREEN_PADDING_TOP - PLATFORM_SCREEN_PADDING_BOTTOM) / PLATFORM_PART_NUMBER;
		
		int x = (int) (Math.floor(Math.random() * (Game.PANE_WIDTH - platform.getWidth())));
		int y = (int) (Math.floor(Math.random() * (pixelsPerPart - platform.getHeight())) + pixelsPerPart * part + PLATFORM_SCREEN_PADDING_BOTTOM);
		
		platform.setXY(x, y);
		
		return platform;
	}
	
	private List<Platform> generatePlatforms() {
		
		List<Platform> platforms = new LinkedList<Platform>();
		
		for (int i = 0; i < PLATFORM_PART_NUMBER; i++) {
			for (int j = 0; j < PLATFORM_NUMBER_PER_PART; j++) {
				platforms.add(generatePlatform(i));
			}
		}
		
		return platforms;
	}
	
	private void respawnPlatform() {
		Platform platform = generatePlatform(PLATFORM_PART_NUMBER); 
		platforms.add(platform);
		root.getChildren().add(platform.getNode());
		setSomeNodesOnTop();
	}

	private void setSomeNodesOnTop() {
		player.getNode().toFront();
		scoreText.getNode().toFront();
		scoreLabel.getNode().toFront();
	}
}
