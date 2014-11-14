package uca.workshop.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Map {
	private Texture background;
	private static final float BACKGROUND_WIDTH = 80f;
	private static final float BACKGROUND_HEIGHT = 60f;
	
	public Map() {
		background = new Texture("background.png");
	}
	
	public void dispose() {
		background.dispose();
	}
	
	public void draw(SpriteBatch batch, float Xorigin) {
		// Draw background
		batch.draw(
			background, 
			Xorigin, 0, 
			BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
	}
}
