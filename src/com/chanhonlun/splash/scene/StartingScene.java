package com.chanhonlun.splash.scene;

import com.chanhonlun.splash.application.Game;
import com.chanhonlun.splash.util.EventEmitter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class StartingScene extends MyScene {
	
	public static final String ON_CLICK_START_BUTTON = "onClickStartButton";
	public static final String ON_CLICK_LEADERBOARD_BUTTON = "onClickLeaderboardButton";
	
	public StartingScene() {
		super();
		this.emitterMap.put(ON_CLICK_START_BUTTON, new EventEmitter<Object>());
		this.emitterMap.put(ON_CLICK_LEADERBOARD_BUTTON, new EventEmitter<Object>());
	}
	
	@Override
	protected Scene createScene() {

		BorderPane root = new BorderPane();
		root.setBackground(new Background(new BackgroundFill(Color.rgb(16, 90, 209), CornerRadii.EMPTY, Insets.EMPTY)));
		
		/*
		 * setup title text
		 */
		Text title = new Text(0, 200, "Splash!");
		title.setFont(new Font(36));
		title.setFill(Color.WHITE);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setWrappingWidth(Game.PANE_WIDTH);
		
		/*
		 * setup "start" button
		 */
		Button startButton = new Button();
		startButton.setText("Start Game");
		startButton.setMinHeight(30);
		startButton.setOnMouseClicked(event -> emitterMap.get(ON_CLICK_START_BUTTON).emit(event));
		
		/*
		 * setup "leaderboard" button
		 */
		Button leaderBoardButton = new Button();
		leaderBoardButton.setText("Leaderboard");
		leaderBoardButton.setLayoutY(50);
		leaderBoardButton.setMinHeight(30);
		leaderBoardButton.setOnMouseClicked(event -> emitterMap.get(ON_CLICK_LEADERBOARD_BUTTON).emit(event));
		
		/*
		 * add all object into pane
		 */
		VBox pane = new VBox();
		pane.getChildren().add(title);
		pane.getChildren().add(startButton);
		pane.getChildren().add(leaderBoardButton);
		pane.setAlignment(Pos.CENTER);
		VBox.setMargin(title, new Insets(0, 0, 200, 0));
		VBox.setMargin(startButton, new Insets(10));
		VBox.setMargin(leaderBoardButton, new Insets(10));
		
		root.setCenter(pane);
		
		Scene scene = new Scene(root, Game.PANE_WIDTH, Game.PANE_HEIGHT);
		scene.setOnMousePressed(event -> System.out.println("clicked screen" + event));
		
		return scene;
	}

}
