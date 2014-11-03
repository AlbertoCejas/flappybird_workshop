package uca.workshop.game;

public class Rock extends Entity {
	public static final float ROCK_WIDTH = 13.55f;
	public static final float ROCK_HEIGHT = 30f;

	boolean counted;
	
	public Rock(float x, float y) {
		super(ROCK_WIDTH, ROCK_HEIGHT, ROCK_WIDTH - 7.0f, ROCK_HEIGHT - 5.0f);
		
		this.setPosition(x, y);
	}
}
