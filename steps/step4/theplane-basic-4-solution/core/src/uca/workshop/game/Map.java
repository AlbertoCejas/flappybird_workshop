package uca.workshop.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Map {
	private Array<Rock> rocks = new Array<Rock>();
	private Texture background, ground, rock;
	private TextureRegion groundRegion, ceilingRegion, rockRegion, rockDownRegion;
	
	private static final float BACKGROUND_WIDTH = 80f;
	private static final float BACKGROUND_HEIGHT = 60f;
	
	public static final float GROUND_WIDTH = 80f;
	public static final float GROUND_HEIGHT = 7f;
	
	public Map() {
		background = new Texture("background.png");
		ground = new Texture("ground.png");
		rock = new Texture("rock.png");
		
		groundRegion = new TextureRegion(ground);
		ceilingRegion = new TextureRegion(ground);
		ceilingRegion.flip(true, true);
		
		rockRegion = new TextureRegion(rock);
		rockDownRegion = new TextureRegion(rock);
		rockDownRegion.flip(true, true);
		
		reset();
	}
	
	public void dispose() {
		background.dispose();
		ground.dispose();
		rock.dispose();
	}
	
	public void reset() {
		rocks.clear();
		for(int i = 0; i < 5; i++) {
			boolean isDown = MathUtils.randomBoolean();
			Rock rock = new Rock(60 + i * 25, isDown?60-30: 0);
			rock.setRegion(isDown? rockDownRegion: rockRegion);
			rock.setRotation(isDown? 180f : 0f);
			rocks.add(rock);
		}
	}
	
	public Array<Rock> getRocks() {
		return rocks;
	}
	
	public void update(float cameraPosX) {
		
		// Place already shown rocks in the next scene
		for(Rock r: rocks) {
			Vector2 position = r.getPosition();
			if(cameraPosX - position.x > 40 + Rock.ROCK_WIDTH) {
				boolean isDown = MathUtils.randomBoolean();
				r.setRotation(0f); // Reset orientation
				r.setPosition(position.x + 5 * 25, isDown?60-30: 0);
				r.setRegion(isDown? rockDownRegion: rockRegion);
				r.setRotation(isDown? 180f : 0f);
				r.counted = false;
			}
		}
	}
	
	public void draw(SpriteBatch batch, float Xorigin, float Xoffset) {
		// Draw background
		batch.draw(
			background, 
			Xorigin, 0, 
			BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		
		// Draw rocks
		for(Rock rockEntity : rocks) {
			rockEntity.draw(batch);
		}
		
		// Draw current ground sprite and next one to keep it smooth
		batch.draw(
			groundRegion, 
			Xoffset, 0, 
			GROUND_WIDTH, GROUND_HEIGHT);
		batch.draw(
			groundRegion, 
			Xoffset + GROUND_WIDTH, 0, 
			GROUND_WIDTH, GROUND_HEIGHT);

		// Draw current ceiling sprite and next one to keep it smooth
		batch.draw(
			ceilingRegion, 
			Xoffset, 60-7, 
			GROUND_WIDTH, GROUND_HEIGHT);
		batch.draw(
			ceilingRegion, 
			Xoffset + GROUND_WIDTH, 60-7, 
			GROUND_WIDTH, GROUND_HEIGHT);
	}
}
