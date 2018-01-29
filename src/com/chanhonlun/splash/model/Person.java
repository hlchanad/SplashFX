package com.chanhonlun.splash.model;

public class Person {
	
	private String name;
	private int score;
	
	public Person(String name, int score) {
		this.name = name;
		this.score = score;
	}

	public String getName() {
		return this.name;
	}
	
	public int getScore() {
		return this.score;
	}
}
