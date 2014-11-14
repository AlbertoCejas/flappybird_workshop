package uca.workshop.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class Entity {
	private Vector2 position;
	private TextureRegion region;
	private float width;
	private float height;
	private Polygon body;
	
	public Entity(float width, float height, Polygon shape) {


		
		
		
	}
	
	public Vector2 getPosition() {

	}
	
	public void setPosition(float x, float y) {


		
	}
	
	public void translate(float x, float y) {


		
	}
	
	public Polygon getPolygon() {

	}
	
	public float getRotation() {

	}
	
	public void setRotation(float degrees) {

	}
	
	public void setRegion(TextureRegion region) {

	}
	
	public void draw(SpriteBatch batch) {
		if (region == null) {
			return;
		}


		
	}
	
	public static boolean collide(Entity e1, Entity e2) {

	}
}
