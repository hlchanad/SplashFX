package com.chanhonlun.splash.scene;

import java.util.HashMap;
import java.util.Map;

import com.chanhonlun.splash.util.EventEmitter;

import javafx.scene.Scene;

public abstract class MyScene {

	protected Scene scene;
	protected Map<String, EventEmitter<Object>> emitterMap;
	
	public MyScene() {
		this.emitterMap = new HashMap<String, EventEmitter<Object>>();
		this.scene = null;
	}

	protected abstract Scene createScene();
	
	public Scene getScene() {
		if (scene == null) {
			scene = createScene();
		}
		return scene;
	}
	
	public EventEmitter<Object> getEventEmitter(String key) {
		return this.emitterMap.containsKey(key) ? this.emitterMap.get(key) : null;
	}
}
