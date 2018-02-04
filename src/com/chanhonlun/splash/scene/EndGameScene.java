package com.chanhonlun.splash.scene;

import com.chanhonlun.splash.application.Game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class EndGameScene extends MyScene {

	private int score;
	
	public void setScore(int score) { this.score = score; }
	
	@Override
	protected Scene createScene() {
		BorderPane root = new BorderPane();
		root.setBackground(new Background(new BackgroundFill(Color.rgb(16, 90, 209), CornerRadii.EMPTY, Insets.EMPTY)));
		
		/*
		 * setup title text
		 */
		Text title = new Text(0, 200, "score: " + score);
		title.setFont(new Font(36));
		title.setFill(Color.WHITE);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setWrappingWidth(Game.PANE_WIDTH);
		
		/*
		 * add all object into pane
		 */
		VBox pane = new VBox();
		pane.getChildren().add(title);
		pane.setAlignment(Pos.CENTER);
		VBox.setMargin(title, new Insets(0, 0, 200, 0));
		
		root.setCenter(pane);
		
		Scene scene = new Scene(root, Game.PANE_WIDTH, Game.PANE_HEIGHT);
		
		return scene;
	}

}
