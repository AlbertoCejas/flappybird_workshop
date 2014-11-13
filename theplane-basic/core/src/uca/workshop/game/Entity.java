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
		this.position = new Vector2();
		this.body = shape;
		this.width = width;
		this.height = height;
		
		body.setOrigin(width*0.5f, height*0.5f);
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
		body.setPosition(x, y);
	}
	
	public void translate(float x, float y) {
		position.x += x;
		position.y += y;
		
		body.translate(x, y);
	}
	
	public Polygon getPolygon() {
		return body;
	}
	
	public float getRotation() {
		return body.getRotation();
	}
	
	public void setRotation(float degrees) {
		body.setRotation(degrees);
	}
	
	public void setRegion(TextureRegion region) {
		this.region = region;
	}
	
	public void draw(SpriteBatch batch) {
		if (region == null) {
			return;
		}

		batch.draw(
			region, 
			position.x, position.y, 
			width, height);
	}
	
	//Assumes shaperenderer line's begin() has been called
	public void debug(ShapeRenderer sr) {
		sr.polygon(body.getTransformedVertices());
	}
	
	public static boolean collide(Entity e1, Entity e2) {
		return Intersector.overlapConvexPolygons(e1.body, e2.body);
	}
}
