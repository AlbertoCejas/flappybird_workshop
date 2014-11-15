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
		


		
		
		
		
		reset();
	}
	
	public void dispose() {
		background.dispose();


	}
	
	public void reset() {

		for(int i = 0; i < 5; i++) {


			
			
		}
	}
	
	public Array<Rock> getRocks() {
		return rocks;
	}
	
	public void update(float cameraPosX) {
		
		// Place already shown rocks in the next scene
		for(Rock r: rocks) {


			
			
			
		}
	}
	
	public void draw(SpriteBatch batch, float Xorigin, float Xoffset) {
		// Draw background
		batch.draw(
			background, 
			Xorigin, 0, 
			BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		
		// Draw rocks

		
		
		// Draw current ground sprite and next one to keep it smooth

		
		
		
		
		

		// Draw current ceiling sprite and next one to keep it smooth


		
		
		
	}
}
