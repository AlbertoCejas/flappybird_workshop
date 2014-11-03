package uca.workshop.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Map {
	
	static class Rock {
		public static final float ROCK_WIDTH = 13.55f;
		public static final float ROCK_HEIGHT = 30f;
		Vector2 position = new Vector2();
		TextureRegion image;
		boolean counted;
		
		public Rock(float x, float y, TextureRegion image) {
			this.position.x = x;
			this.position.y = y;
			this.image = image;
		}
	}
	
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
			rocks.add(new Rock(60 + i * 25, isDown?60-30: 0, isDown? rockDownRegion: rockRegion));
		}
	}
	
	public Array<Rock> getRocks() {
		return rocks;
	}
	
	public void update(float cameraPosX) {
		
		// Place already shown rocks in the next scene
		for(Rock r: rocks) {
			if(cameraPosX - r.position.x > 40 + Rock.ROCK_WIDTH) {
				boolean isDown = MathUtils.randomBoolean();
				r.position.x += 5 * 25;
				r.position.y = isDown?60-30: 0;
				r.image = isDown? rockDownRegion: rockRegion;
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
			batch.draw(
				rockEntity.image, 
				rockEntity.position.x, rockEntity.position.y, 
				Rock.ROCK_WIDTH, Rock.ROCK_HEIGHT);
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
