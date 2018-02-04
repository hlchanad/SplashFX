package com.chanhonlun.splash.model;

import java.io.File;

public abstract class Platform extends Unit {

	protected static final int DEFAULT_WIDTH  = 150;
	protected static final int DEFAULT_HEIGHT = 20;
	
	protected static final String PLATFORM_IMAGE = "resources/images/platform.png";
	
	public Platform() {
		this.image = new File(PLATFORM_IMAGE);
		
		this.width  = DEFAULT_WIDTH;
		this.height = DEFAULT_HEIGHT; 
	}
}
