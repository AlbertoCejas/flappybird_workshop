package uca.workshop.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class Plane extends Entity {
	public static final float PLANE_VELOCITY_X = 20f;
	public static final float PLANE_WIDTH = 9;
	public static final float PLANE_HEIGHT = 7.45f;
	
	private static final float PLANE_START_Y = 40f;
	private static final float PLANE_START_X = 20f;
	private static final Vector2 GRAVITY = new Vector2 (0,-1f);
	
	private Animation animation;
	private float stateTime = 0;
	private Texture frame1, frame2, frame3;
	
	private Vector2 velocity = new Vector2();
	
	public Plane() {
		super(PLANE_WIDTH, PLANE_HEIGHT, createShape());
		
		frame1 = new Texture("plane1.png");
		frame1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		frame2 = new Texture("plane2.png");
		frame3 = new Texture("plane3.png");
		
		
		animation = new Animation(0.05f, new TextureRegion(frame1), new TextureRegion(frame2), new TextureRegion(frame3), new TextureRegion(frame2));
		animation.setPlayMode(PlayMode.LOOP);
		
		reset();
	}
	
	public void dispose() {
		frame1.dispose();
		frame2.dispose();
		frame3.dispose();
	}
	
	public void reset() {
		setVelocity(0.0f, 0.0f);
		setPosition(PLANE_START_X, PLANE_START_Y);
	}

	public void setVelocity(float x, float y) {
		velocity.x = x;
		velocity.y = y;
	}
	
	public void update(float delta) {
		stateTime += delta;
		
		Vector2 vel = new Vector2(velocity.add(GRAVITY));
		vel.scl(delta);
		translate(vel.x, vel.y);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		setRegion(animation.getKeyFrame(stateTime));
		super.draw(batch);
	}
	
	private static Polygon createShape() {
		float[] vertices = new float[8];
		
		// Left bottom corner
		vertices[0] = 1f;
		vertices[1] = 0f;
		// Top left corner
		vertices[2] = 1f;
		vertices[3] = PLANE_HEIGHT;
		// Top right corner
		vertices[4] = PLANE_WIDTH - 1f;
		vertices[5] = PLANE_HEIGHT;
		// Right bottom corner
		vertices[6] = PLANE_WIDTH - 1f;
		vertices[7] = 0f;
		
		return new Polygon(vertices);
	}
}
