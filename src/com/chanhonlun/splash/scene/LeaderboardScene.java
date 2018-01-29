package com.chanhonlun.splash.scene;

import java.util.Iterator;

import com.chanhonlun.splash.application.Game;
import com.chanhonlun.splash.model.Leaderboard;
import com.chanhonlun.splash.model.Person;
import com.chanhonlun.splash.util.EventEmitter;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class LeaderboardScene extends MyScene {

	public static final String ON_CLICKED_BACK_BUTTON = "onClickBackButton";
	
	private Leaderboard leaderboard;
	
	public LeaderboardScene() {
		super();
		this.emitterMap.put(ON_CLICKED_BACK_BUTTON, new EventEmitter<Object>());
		this.leaderboard = new Leaderboard();
	}
	
	@Override
	public Scene createScene() {
		
		/*
		 * setup title text
		 */
		Text title = new Text("\nLeaderboard\n");
		title.setFont(new Font(36));
		title.setFill(Color.WHITE);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setWrappingWidth(Game.PANE_WIDTH);
		
		/*
		 * setup back button
		 */
		int backButtonWidth = 100;
		Button backButton = new Button();
		backButton.setText("Back");
		backButton.setTranslateX((Game.PANE_WIDTH - backButtonWidth) / 2);
		backButton.setPrefHeight(50);
		backButton.setPrefWidth(backButtonWidth);
		backButton.setOnMouseClicked(event -> emitterMap.get(ON_CLICKED_BACK_BUTTON).emit(event));
		
		Pane backButtonPane = new Pane();
		backButtonPane.setPadding(new Insets(20));
		backButtonPane.getChildren().add(backButton);
		
		
		
		/*
		 * setup leaderboard list group
		 */
		Pane leaderboardList = this.createLeaderboardList();

		
		/*
		 * add all object into pane
		 */
		BorderPane root = new BorderPane();
		root.setBackground(new Background(new BackgroundFill(Color.rgb(16, 90, 209), CornerRadii.EMPTY, Insets.EMPTY)));
		root.setTop(title);
		root.setCenter(leaderboardList);
		root.setBottom(backButtonPane);
		
		Scene scene = new Scene(root, Game.PANE_WIDTH, Game.PANE_HEIGHT);
		scene.setOnMousePressed(event -> System.out.println("clicked screen" + event));

		return scene;
	}
	
	private Pane createLeaderboardList() {
		
		GridPane grid = new GridPane();
		
		int i = 0;
		Iterator<Person> it = leaderboard.getPeopleIterator();
		while (it.hasNext()) {
			Person person = it.next();
			
			/*
			 * setup person name
			 */
			Text personName = new Text(person.getName());
			personName.setFont(new Font(24));
			personName.setWrappingWidth(Game.PANE_WIDTH/2);
			personName.setTextAlignment(TextAlignment.CENTER);
			
			/*
			 * setup person score
			 */
			Text personScore = new Text(Integer.toString(person.getScore()));
			personScore.setFont(new Font(24));
			personScore.setWrappingWidth(Game.PANE_WIDTH/2);
			personScore.setTextAlignment(TextAlignment.CENTER);
			
			/*
			 * setup line container
			 */
			grid.add(personName, 0, i);
			grid.add(personScore, 1, i);

			grid.setVgap(20);
//			grid.gridLinesVisibleProperty().set(true);
//			grid.getRowConstraints().add(new RowConstraints(50));
//			grid.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(5))));
			
			i++;
		}
		
		return grid;
	}

}
