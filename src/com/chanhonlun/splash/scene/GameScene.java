package com.chanhonlun.splash.scene;

import java.util.LinkedList;
import java.util.List;

import com.chanhonlun.splash.application.Game;
import com.chanhonlun.splash.model.Enemy;
import com.chanhonlun.splash.model.NormalPlatform;
import com.chanhonlun.splash.model.Platform;
import com.chanhonlun.splash.model.Player;
import com.chanhonlun.splash.util.EventEmitter;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class GameScene extends MyScene {

	private static final int PLAYER_GROUND_Y                         = 20;
	private static final int ENEMY_AREA_PART                         = 3; // top 1/3 height will be the enemy zone
	private static final int ENEMY_MAX_NUMBER                        = 5;
	private static final int PLATFORM_PART_NUMBER                    = 3;
    private static final int PLATFORM_NUMBER_PER_PART                = 2;
	private static final int PLATFORM_SCREEN_PADDING_TOP             = 100;
	private static final int PLATFORM_SCREEN_PADDING_BOTTOM          = 100;
	
	public static final String ON_LOSE = "onLose";
	
	private List<Enemy> enemies;
	private Player player;
	private List<Platform> platforms;
	
	public GameScene() {
		this.enemies = new LinkedList<Enemy>();
		this.emitterMap.put(ON_LOSE, new EventEmitter<Object>());
	}
	
	@Override
	protected Scene createScene() {
		
		StackPane root = new StackPane();
		
		player = generatePlayer();
		player.getFallDownEmitter().subscribe(point -> handlePlayerFallDown(point));
		root.getChildren().add(player.getNode());
		
		enemies = generateEnemies();
		enemies.forEach(enemy -> root.getChildren().add(enemy.getNode()));
		
		platforms = generatePlatforms();
		platforms.forEach(platform -> root.getChildren().add(platform.getNode()));

		player.getNode().toFront(); // needs to be invoked every time something is added to the pane
		
		Scene scene = new Scene(root, Game.PANE_WIDTH, Game.PANE_HEIGHT);

		scene.setOnKeyPressed(keyEvent -> player.handleKeyPressed(keyEvent));
		scene.setOnKeyReleased(keyEvent -> player.handleKeyReleased(keyEvent));
		
		return scene;
	}

	private void handlePlayerFallDown(Point2D point) {
		System.out.printf("Player coordinate: (%s, %s)\n", point.getX(), point.getY());
		
		if (player.fallBelowGround()) {
			// TODO: emit real score later
			player.stopJumping();
			this.emitterMap.get(ON_LOSE).emit(5000);
			return ;
		}
		
		for (Platform platform : platforms) {
			if (player.onPlatform(platform)) {
				player.setXY(player.getXY().getX(), platform.getXY().getY() + platform.getHeight());
				player.stopJumping();
				
				
				// TODO: move platforms down (smoothly)
				
				break;
			}
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
}
