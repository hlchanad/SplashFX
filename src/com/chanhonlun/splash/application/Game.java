package com.chanhonlun.splash.application;

import com.chanhonlun.splash.scene.GameScene;
import com.chanhonlun.splash.scene.LeaderboardScene;
import com.chanhonlun.splash.scene.MyScene;
import com.chanhonlun.splash.scene.StartingScene;

import javafx.application.Application;
import javafx.stage.Stage;

public class Game extends Application {
	
	public static final String APP_NAME = "Splash!";
	
	public static final int PANE_WIDTH  = 450;
	public static final int PANE_HEIGHT = 600;
	
	public static void main(String[] args) {
		Game.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		MyScene startingScene    = new StartingScene();
		MyScene leaderboardScene = new LeaderboardScene();
		MyScene gameScene        = new GameScene();
		
		/*
		 * setup condition for changing scene
		 */
		startingScene.getEventEmitter(StartingScene.ON_CLICK_LEADERBOARD_BUTTON)
			.subscribe(event -> primaryStage.setScene(leaderboardScene.getScene()));
		
		startingScene.getEventEmitter(StartingScene.ON_CLICK_START_BUTTON)
			.subscribe(event -> primaryStage.setScene(gameScene.getScene()));
		
		leaderboardScene.getEventEmitter(LeaderboardScene.ON_CLICKED_BACK_BUTTON)
			.subscribe(event -> primaryStage.setScene(startingScene.getScene()));
		
		/*
		 * setup primary stage
		 */
		primaryStage.setTitle(APP_NAME);
		primaryStage.setScene(startingScene.getScene());
		primaryStage.show();	
	}
}
