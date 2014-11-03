package uca.workshop.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Entity {
	private Vector2 position;
	private TextureRegion region;
	private float width;
	private float height;
	private Rectangle body;
	
	public Entity(float width, float height, float boundsWidth, float boundsHeight) {
		this.position = new Vector2();
		this.body = new Rectangle();
		this.width = width;
		this.height = height;
		
		this.body.setSize(boundsWidth, boundsHeight);
		
		updateBody();
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}
	
	public void setRegion(TextureRegion region) {
		this.region = region;
	}
	
	public void update(float delta) {
		updateBody();
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
	
	private void updateBody() {
		float paddingX = width - body.width;
		float paddingY = height - body.height;
		body.setPosition(position.x + paddingX, position.y + paddingY);
	}
	
	public static boolean collide(Entity e1, Entity e2) {
		return e1.body.overlaps(e2.body);
	}
}
