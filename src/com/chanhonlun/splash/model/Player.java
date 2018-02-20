package com.chanhonlun.splash.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.chanhonlun.splash.application.Game;
import com.chanhonlun.splash.util.EventEmitter;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class Player extends Character {

	private static final String PLAYER_IMAGE = "resources/images/mimikyu.png";

	private static final int ANIMATION_KEY_FRAMES_MILLIS = 10;
	
	private static final double MOVE_HORIZONTAL_PIXEL               = 2.5;
	private static final double MOVE_VERTICAL_PIXEL                 = 5;
	private static final double MOVE_VERTICAL_MAX                   = 200;
	private static final double BELOW_GROUND_BUFFER_Y_PIXEL         = 50;
	private static final double STAND_ON_PLATFORM_BUFFER_PIXEL      = 5;
	private static final double PLAYER_ALLOW_LAND_LEFT_MARGIN       = 10;
	private static final double PLAYER_ALLOW_LAND_RIGHT_MARGIN      = 25;
	
	
	public static final String EVENT_MOVE_UP           = "EVENT_MOVE_UP";
	public static final String EVENT_MOVE_DOWN         = "EVENT_MOVE_DOWN";
	public static final String EVENT_MOVE_HORIZONTALLY = "EVENT_MOVE_HORIZONTALLY"; 
	
	private Map<String, EventEmitter<Point2D>> emitterMap;
	
	// animations
	private Timeline leftSlider, rightSlider, jumper, faller;
	
	public Player() {
		this.image = new File(PLAYER_IMAGE);
		
		this.emitterMap = new HashMap<String, EventEmitter<Point2D>>();
		this.emitterMap.put(EVENT_MOVE_UP, new EventEmitter<Point2D>());
		this.emitterMap.put(EVENT_MOVE_DOWN, new EventEmitter<Point2D>());
		this.emitterMap.put(EVENT_MOVE_HORIZONTALLY, new EventEmitter<Point2D>());
		
		setupSlideAnimation();
		setupJumpAnimation();
	}
	
	private void setupSlideAnimation() {
		leftSlider = new Timeline(new KeyFrame(
			Duration.millis(ANIMATION_KEY_FRAMES_MILLIS),
			sliderActionEvent -> {
				double newX = x - MOVE_HORIZONTAL_PIXEL > 0 ? x - MOVE_HORIZONTAL_PIXEL : 0,
					   newY = y; 
				setXY(newX, newY);
				if (!jumper.getStatus().equals(Status.RUNNING) && !faller.getStatus().equals(Status.RUNNING)) {
					emitterMap.get(EVENT_MOVE_HORIZONTALLY).emit(new Point2D(newX, newY));
				}
			}
		));
		leftSlider.setCycleCount(Animation.INDEFINITE);
		
		rightSlider = new Timeline(new KeyFrame(
			Duration.millis(ANIMATION_KEY_FRAMES_MILLIS),
			sliderActionEvent -> {
				double newX = x + MOVE_HORIZONTAL_PIXEL < Game.PANE_WIDTH - width ? x + MOVE_HORIZONTAL_PIXEL : Game.PANE_WIDTH - width,
					   newY = y;
				setXY(newX, newY);
				if (!jumper.getStatus().equals(Status.RUNNING) && !faller.getStatus().equals(Status.RUNNING)) {
					emitterMap.get(EVENT_MOVE_HORIZONTALLY).emit(new Point2D(newX, newY));
				}
			}
		));
		rightSlider.setCycleCount(Animation.INDEFINITE);
	}
	
	private void setupJumpAnimation() {
		jumper = new Timeline(new KeyFrame(
			Duration.millis(ANIMATION_KEY_FRAMES_MILLIS), 
			jumperActionEvent -> {
				double newX = x,
					   newY = y + MOVE_VERTICAL_PIXEL;
				setXY(newX, newY);
				emitterMap.get(EVENT_MOVE_UP).emit(new Point2D(newX, newY));
			}
		));
		jumper.setCycleCount((int) (MOVE_VERTICAL_MAX / MOVE_VERTICAL_PIXEL));
		
		faller = new Timeline(new KeyFrame(
			Duration.millis(ANIMATION_KEY_FRAMES_MILLIS), 
			fallerActionEvent -> {
				double newX = x,
					   newY = y - MOVE_VERTICAL_PIXEL;
				setXY(newX, newY);
				emitterMap.get(EVENT_MOVE_DOWN).emit(new Point2D(newX, newY));
			}
		));
		faller.setCycleCount(Animation.INDEFINITE);
		
		jumper.setOnFinished(jumperActionEvent -> faller.play());
	}
	
	@Override
	protected Node generateNode() {
		Node node = super.generateNode();
		return node;
	}
	
	@Override
	public void handleKeyPressed(KeyEvent keyEvent) {
		super.handleKeyPressed(keyEvent);
		
		switch (keyEvent.getCode()) {
			case UP:
				// only can jump when not in jumping animation
				if (!jumper.getStatus().equals(Status.RUNNING) && !faller.getStatus().equals(Status.RUNNING)) {
					jumper.play();
				}
				break;
			case LEFT:
				leftSlider.play();
				break;
			case RIGHT:
				rightSlider.play();
				break;
			default:
				break;
		}
	}
	
	@Override
	public void handleKeyReleased(KeyEvent keyEvent) {
		super.handleKeyReleased(keyEvent);

		switch (keyEvent.getCode()) {
			case LEFT:
				leftSlider.stop();
				break;
			case RIGHT:
				rightSlider.stop();
				break;
			default:
				break;
		}
	}
	
	public EventEmitter<Point2D> getEmitter(String key) {
		return this.emitterMap.get(key);
	}
	
	public void stopFalling() {
		faller.stop();
	}

	public boolean onPlatform(Platform platform) {
		Point2D platformXY = platform.getXY();
		
		return platformXY.getX() <= x + width - PLAYER_ALLOW_LAND_RIGHT_MARGIN && x + PLAYER_ALLOW_LAND_LEFT_MARGIN <= platformXY.getX() + platform.getWidth()
			&& platformXY.getY() + platform.getHeight() <= y && y <= platformXY.getY() + platform.getHeight() + STAND_ON_PLATFORM_BUFFER_PIXEL;
	}

	public boolean fallBelowGround() {
		return y + height < -BELOW_GROUND_BUFFER_Y_PIXEL; // buffer
	}

	public void fall() {
		if (!faller.getStatus().equals(Status.RUNNING)) {
			faller.play();
		}
		
	}
}
