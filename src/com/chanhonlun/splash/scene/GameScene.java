package com.chanhonlun.splash.scene;

import java.util.LinkedList;
import java.util.List;

import com.chanhonlun.splash.application.Game;
import com.chanhonlun.splash.model.Enemy;
import com.chanhonlun.splash.model.Player;
import com.chanhonlun.splash.model.Unit;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class GameScene extends MyScene {

	private static final int PLAYER_GROUND_Y   = 50;
	public static final int ENEMY_AREA_PART    = 3; // top 1/3 height will be the enemy zone
	public static final int ENEMY_MAX_NUMBER   = 5;
	
	private List<Enemy> enemies;
	
	public GameScene() {
		this.enemies = new LinkedList<Enemy>();
	}
	
	@Override
	protected Scene createScene() {
		
		StackPane root = new StackPane();
		
		Unit player = generatePlayer(root);
		
		generateEnemy(root);
		
		Scene scene = new Scene(root, Game.PANE_WIDTH, Game.PANE_HEIGHT);

		scene.setOnKeyPressed(keyEvent -> player.handleKeyPressed(keyEvent));
		scene.setOnKeyReleased(keyEvent -> player.handleKeyReleased(keyEvent));
		
		return scene;
	}
	
	private Unit generatePlayer(StackPane stackPane) {
		Player player = new Player();
		
		int x = (Game.PANE_WIDTH - player.getWidth()) / 2;
		int y = PLAYER_GROUND_Y;
		
		player.setXY(x, y);
		
		stackPane.getChildren().add(player.getNode());
		
		return player;
	}
	
	private void generateEnemy(StackPane stackPane) {
		
		for (int i = 0; i < ENEMY_MAX_NUMBER; i++) {
			Enemy enemy = new Enemy();
			
			int x = (int) Math.floor(Math.random() * (Game.PANE_WIDTH - enemy.getWidth()));
			int y = (int) (Math.floor(Math.random() * (Game.PANE_HEIGHT / ENEMY_AREA_PART - enemy.getHeight())) + (Game.PANE_HEIGHT - Game.PANE_HEIGHT / ENEMY_AREA_PART));
			
			enemy.setXY(x, y);
			
			this.enemies.add(enemy);
			stackPane.getChildren().add(enemy.getNode());
		}
	}
}
