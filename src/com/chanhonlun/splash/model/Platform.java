package com.chanhonlun.splash.model;

import java.io.File;

import com.chanhonlun.splash.scene.GameScene;

public abstract class Platform extends Unit {

	protected static final int DEFAULT_WIDTH  = 150;
	protected static final int DEFAULT_HEIGHT = 20;
	
	protected static final String PLATFORM_IMAGE = "resources/images/platform.png";
	
	public Platform() {
		this.image = new File(PLATFORM_IMAGE);
		
		this.width  = DEFAULT_WIDTH;
		this.height = DEFAULT_HEIGHT; 
	}
	
	public boolean isInBottom() {
		return this.y <= GameScene.PLAYER_PLATFORM_BOTTOM_AREA_Y;
	}
	
	public double diffWithBottom() {
		return this.y - GameScene.PLAYER_PLATFORM_BOTTOM_AREA_Y;
	}
	
	public boolean isBelowScreen() {
		return this.y + this.height < 0;
	}
}
