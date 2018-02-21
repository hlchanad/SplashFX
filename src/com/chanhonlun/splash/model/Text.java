package com.chanhonlun.splash.model;

import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Text extends Unit {

	javafx.scene.text.Text textLabel;
	
	public Text() {
		textLabel = new javafx.scene.text.Text(200, 200, "");
		this.node = generateNode();
	}
	
	public void setText(String text) {
		textLabel.setText(text);
	}
	
	@Override
	protected Node generateNode() {
		return textLabel;
	}

	public void setTextAlignment(TextAlignment textAlignement) {
		textLabel.setTextAlignment(textAlignement);
	}
	
	public void setTextSize(double size) {
		textLabel.setFont(new Font(size));
	}
}
