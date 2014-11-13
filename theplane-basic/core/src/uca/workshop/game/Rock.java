package uca.workshop.game;

import com.badlogic.gdx.math.Polygon;

public class Rock extends Entity {
	public static final float ROCK_WIDTH = 13.55f;
	public static final float ROCK_HEIGHT = 30f;

	boolean counted;
	
	public Rock(float x, float y) {
		super(ROCK_WIDTH, ROCK_HEIGHT, createShape());
		
		this.setPosition(x, y);
	}
	
	private static Polygon createShape() {
		float[] vertices = new float[6];
		// Left bottom corner
		vertices[0] = 0f;
		vertices[1] = 0f;
		// Right bottom corner
		vertices[2] = ROCK_WIDTH * 0.5f + 1.2f;
		vertices[3] = ROCK_HEIGHT;
		// Top of triangle
		vertices[4] = ROCK_WIDTH;
		vertices[5] = 0f;
		
		return new Polygon(vertices);
	}
}
