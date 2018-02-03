package com.chanhonlun.splash.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.chanhonlun.splash.application.Game;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Unit {


	public enum Alignment { BOTTOM_LEFT, CENTER };
	
	private static final int DEFAULT_X           = 0;
	private static final int DEFAULT_Y           = 0;
	private static final int DEFAULT_WIDTH       = 80;
	private static final int DEFAULT_HEIGHT      = 80;
	private static final Alignment DEFAULT_ALIGN = Alignment.BOTTOM_LEFT;
	private static final String DEFAULT_IMAGE    = "resources/images/mimikyu.png";
	
	protected File image;
	
	/**
	 * Assume that the origin (0, 0) of coordinate system starts at left bottom corner
	 * like the Cartesian coordinate system
	 * the x and y here represent the left bottom corner of this {@code Unit}
	 *  y ^
	 *    |
	 *    |    +-----------+
	 *    |    |           |
	 *    |    |           |
     *    |    |           |
	 *    |    +-----------+
	 *    |    ^
	 *    |    (x, y)
	 *    |
	 *  --+------------------------->
	 *  O |                         x
	 */
	protected int x, y;
	
	protected int width, height;
	
	protected Alignment alignment;
	
	protected Node node;
	
	public Unit() {
		this.image = new File(DEFAULT_IMAGE);
		this.x = DEFAULT_X;
		this.y = DEFAULT_Y;
		this.width     = DEFAULT_WIDTH;
		this.height    = DEFAULT_HEIGHT;
		this.alignment = DEFAULT_ALIGN;
		this.node = null;
	}
	
	public Unit(File image, int x, int y, int width, int height) {
		this.image = image;
		this.x = x;
		this.y = y;
		this.width     = width;
		this.height    = height;
		this.alignment = DEFAULT_ALIGN;
		this.node = null;
	}
	
	public void setXY(int x, int y) {
		this.x = x; 
		this.y = y;
	}
	
	protected Node generateNode() {
		Node node = null;
		try {
			node = new ImageView(new Image(new FileInputStream(this.image)));
			((ImageView) node).setFitWidth(width);
			((ImageView) node).setFitHeight(height);
		} catch (FileNotFoundException e) {
			System.err.println("generateNode(): FileNotFoundException, replaced with a dummy square");
		}
		
		// fall back case when don't have an image
		if (node == null) {
			Color[] colors = new Color[] {Color.ALICEBLUE, Color.AQUAMARINE, Color.BLANCHEDALMOND, Color.DARKGREY};
			Color color = colors[(int) Math.floor(Math.random() * colors.length)];
			node = new Rectangle(width, height, color);
		}
		
		// node = Rectangle/ ImageView
		node.setTranslateX(getTranslateXFromCoordinateX());
		node.setTranslateY(getTranslateYFromCoordinateY());
		
		return node;
	}
	
	public Node getNode() {
		if (node == null) {
			node = generateNode();
		}
		return node;
	}
	
	public boolean isCollide(Unit other) {
		return this.getNode().getBoundsInParent().intersects(other.getNode().getBoundsInParent());
	}
	
	public int getWidth()  { return width; }
	public int getHeight() { return height; }
	
	private int getTranslateXFromCoordinateX() {
		if (alignment == Alignment.CENTER) {
			return x - Game.PANE_WIDTH / 2;			
		} else if (alignment == Alignment.BOTTOM_LEFT){
			return x - Game.PANE_WIDTH / 2 + width / 2;
		}
		return x;
	}
	
	private int getTranslateYFromCoordinateY() {
		if (alignment == Alignment.CENTER) {
			return (y - Game.PANE_HEIGHT / 2) * -1;			
		} else if (alignment == Alignment.BOTTOM_LEFT){
			return (y - Game.PANE_HEIGHT / 2 + height / 2) * -1;
		}
		return y;
	}
}
