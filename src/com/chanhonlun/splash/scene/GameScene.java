package com.chanhonlun.splash.scene;

import java.util.LinkedList;
import java.util.List;

import com.chanhonlun.splash.application.Game;
import com.chanhonlun.splash.model.Enemy;
import com.chanhonlun.splash.model.NormalPlatform;
import com.chanhonlun.splash.model.Platform;
import com.chanhonlun.splash.model.Player;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class GameScene extends MyScene {

	private static final int PLAYER_GROUND_Y     = 50;
	private static final int ENEMY_AREA_PART     = 3; // top 1/3 height will be the enemy zone
	private static final int ENEMY_MAX_NUMBER    = 5;
	private static final int PLATFORM_MAX_NUMBER = 5;
	private static final int PLATFORM_SCREEN_PADDING_TOP    = 100;
	private static final int PLATFORM_SCREEN_PADDING_BOTTOM = 50;
	
	private List<Enemy> enemies;
	private Player player;
	private List<Platform> platforms;
	
	public GameScene() {
		this.enemies = new LinkedList<Enemy>();
	}
	
	@Override
	protected Scene createScene() {
		
		StackPane root = new StackPane();
		
		player = generatePlayer();
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
	
	private List<Platform> generatePlatforms() {
		
		List<Platform> platforms = new LinkedList<Platform>();
		
		for (int i = 0; i < PLATFORM_MAX_NUMBER; i++) {
			Platform platform = new NormalPlatform();
			
			int x = (int) (Math.floor(Math.random() * (Game.PANE_WIDTH - platform.getWidth())));
			int y = (int) (Math.floor(Math.random() * (Game.PANE_HEIGHT - PLATFORM_SCREEN_PADDING_TOP - PLATFORM_SCREEN_PADDING_BOTTOM - platform.getHeight())) + PLATFORM_SCREEN_PADDING_BOTTOM);
			
			platform.setXY(x, y);
			
			platforms.add(platform);
		}
		
		return platforms;
	}
}
