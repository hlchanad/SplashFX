package com.chanhonlun.splash.model;

import java.io.File;

import com.chanhonlun.splash.application.Game;
import com.chanhonlun.splash.util.EventEmitter;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class Player extends Character {

	private static final int ANIMATION_KEY_FRAMES_MILLIS = 20;

	private static final String PLAYER_IMAGE = "resources/images/mimikyu.png";
	
	private static final double MOVE_HORIZONTAL_PIXEL               = 5;
	private static final double MOVE_VERTICAL_PIXEL                 = 10;
	private static final double MOVE_VERTICAL_MAX                   = 200;
	
	private static final int STAND_ON_PLATFORM_BUFFER_PIXEL         = 5;
	
	// animations
	private Timeline leftSlider, rightSlider, jumper, diver;
	
	private EventEmitter<Point2D> fallDownEmitter;
	
	public Player() {
		this.image = new File(PLAYER_IMAGE);
		
		this.fallDownEmitter = new EventEmitter<Point2D>();
		
		setupSlideAnimation();
		setupJumpAnimation();
	}
	
	private void setupSlideAnimation() {
		leftSlider = new Timeline(new KeyFrame(
			Duration.millis(ANIMATION_KEY_FRAMES_MILLIS),
			sliderActionEvent -> {
				if (jumper.getStatus() != Status.RUNNING && diver.getStatus() != Status.RUNNING) {
					return ;
				}
				
				double newX = x - MOVE_HORIZONTAL_PIXEL > 0 ? x - MOVE_HORIZONTAL_PIXEL : 0; 
				setXY(newX, y);
			}
		));
		leftSlider.setCycleCount(Animation.INDEFINITE);
		
		rightSlider = new Timeline(new KeyFrame(
			Duration.millis(ANIMATION_KEY_FRAMES_MILLIS),
			sliderActionEvent -> {
				if (jumper.getStatus() != Status.RUNNING && diver.getStatus() != Status.RUNNING) {
					return ;
				}
				
				double newX = x + MOVE_HORIZONTAL_PIXEL < Game.PANE_WIDTH - width ? x + MOVE_HORIZONTAL_PIXEL : Game.PANE_WIDTH - width;
				setXY(newX, y);
			}
		));
		rightSlider.setCycleCount(Animation.INDEFINITE);
	}
	
	private void setupJumpAnimation() {
		jumper = new Timeline(new KeyFrame(
			Duration.millis(ANIMATION_KEY_FRAMES_MILLIS), 
			jumperActionEvent -> {
				double newY = y + MOVE_VERTICAL_PIXEL;
				setXY(x, newY);
			}
		));
		jumper.setCycleCount((int) (MOVE_VERTICAL_MAX / MOVE_VERTICAL_PIXEL));
		
		diver = new Timeline(new KeyFrame(
			Duration.millis(ANIMATION_KEY_FRAMES_MILLIS), 
			diverActionEvent -> {
				double newY = y - MOVE_VERTICAL_PIXEL;
				setXY(x, newY);
				fallDownEmitter.emit(new Point2D(x, newY));
			}
		));
		diver.setCycleCount(Animation.INDEFINITE);
		
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
			if (keyEvent.getCode().equals(KeyCode.UP)) {
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
	
	public EventEmitter<Point2D> getFallDownEmitter() {
		return this.fallDownEmitter;
	}
	
	public void stopJumping() {
		diver.stop();
	}

	public boolean onPlatform(Platform platform) {
		Point2D platformXY = platform.getXY();
		
		return platformXY.getX() <= x + width && x <= platformXY.getX() + platform.getWidth()
			&& platformXY.getY() + platform.getHeight() <= y && y <= platformXY.getY() + platform.getHeight() + STAND_ON_PLATFORM_BUFFER_PIXEL;
	}

	public boolean fallBelowGround() {
		return y + height < -50; // buffer
	}
}
