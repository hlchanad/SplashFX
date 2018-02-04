package com.chanhonlun.splash.model;

import java.io.File;

import com.chanhonlun.splash.application.Game;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class Player extends Character {

	private static final int ANIMATION_KEY_FRAMES_MILLIS = 20;

	private static final String PLAYER_IMAGE = "resources/images/mimikyu.png";
	
	private static final int MOVE_HORIZONTAL_PIXEL = 5;
	private static final int MOVE_VERTICAL_PIXEL   = 10;
	private static final int MOVE_VERTICAL_MAX     = 200;
	
	// animations
	private Timeline leftSlider, rightSlider, jumper, diver;
	
	public Player() {
		this.image = new File(PLAYER_IMAGE);
		
		setupSlideAnimation();
		setupJumpAnimation();
	}
	
	private void setupSlideAnimation() {
		leftSlider = new Timeline(new KeyFrame(
			Duration.millis(ANIMATION_KEY_FRAMES_MILLIS),
			sliderActionEvent -> {
				int newX = x - MOVE_HORIZONTAL_PIXEL > 0 ? x - MOVE_HORIZONTAL_PIXEL : 0; 
				setXY(newX, y);					
			}
		));
		leftSlider.setCycleCount(Animation.INDEFINITE);
		
		rightSlider = new Timeline(new KeyFrame(
			Duration.millis(ANIMATION_KEY_FRAMES_MILLIS),
			sliderActionEvent -> {
				int newX = x + MOVE_HORIZONTAL_PIXEL < Game.PANE_WIDTH - width ? x + MOVE_HORIZONTAL_PIXEL : Game.PANE_WIDTH - width;
				setXY(newX, y);					
			}
		));
		rightSlider.setCycleCount(Animation.INDEFINITE);
	}
	
	private void setupJumpAnimation() {
		jumper = new Timeline(new KeyFrame(
			Duration.millis(ANIMATION_KEY_FRAMES_MILLIS), 
			jumperActionEvent -> setXY(x, y + MOVE_VERTICAL_PIXEL)
		));
		jumper.setCycleCount(MOVE_VERTICAL_MAX / MOVE_VERTICAL_PIXEL);
		
		diver = new Timeline(new KeyFrame(
			Duration.millis(ANIMATION_KEY_FRAMES_MILLIS), 
			diverActionEvent -> setXY(x, y - MOVE_VERTICAL_PIXEL)
		));
		diver.setCycleCount(MOVE_VERTICAL_MAX / MOVE_VERTICAL_PIXEL);
		
		jumper.setOnFinished(jumperActionEvent -> diver.play());
	}
	
	@Override
	protected Node generateNode() {
		Node node = super.generateNode();
		return node;
	}
	
	@Override
	public void handleKeyPressed(KeyEvent keyEvent) {
		super.handleKeyPressed(keyEvent);
		
		if (jumper.getStatus().equals(Status.RUNNING) || diver.getStatus().equals(Status.RUNNING)) {
			// can move left/ right ONLY when jumping 
			if (keyEvent.getCode().equals(KeyCode.LEFT)) {
				leftSlider.play();
			} else if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
				rightSlider.play();
			} 
		}
		else {
			if (keyEvent.getCode().equals(KeyCode.SPACE)) {
				jumper.play();
			}
		}
	}
	
	@Override
	public void handleKeyReleased(KeyEvent keyEvent) {
		super.handleKeyReleased(keyEvent);

		if (keyEvent.getCode().equals(KeyCode.LEFT)) {
			leftSlider.stop();
		} else if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
			rightSlider.stop();
		}
	}
}
