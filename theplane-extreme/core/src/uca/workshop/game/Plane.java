package uca.workshop.game;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Plane extends InputAdapter {
	private static final Vector2 GRAVITY = new Vector2 (0,-1f);
	private static final float PLANE_JUMP_IMPULSE = 27f;
	private static final float PLANE_START_Y = 40f;
	private static final float PLANE_START_X = 20f;
	public static final float PLANE_WIDTH = 9;
	public static final float PLANE_HEIGHT = 7.45f;
	public static float plane_velocity_x = 20f;
	
	private Vector2 planePosition = new Vector2();
	private Vector2 planeVelocity = new Vector2();
	private float planeStateTime = 0;
	
	private Animation plane;
	private Texture frame1, frame2, frame3;
	private Rectangle body = new Rectangle();
	
	public Plane() {
		frame1 = new Texture("plane1.png");
		frame1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		frame2 = new Texture("plane2.png");
		frame3 = new Texture("plane3.png");
		
		
		plane = new Animation(0.05f, new TextureRegion(frame1), new TextureRegion(frame2), new TextureRegion(frame3), new TextureRegion(frame2));
		plane.setPlayMode(PlayMode.LOOP);
		
		reset();
	}
	
	public void dispose() {
		frame1.dispose();
		frame2.dispose();
		frame3.dispose();
	}
	
	public void reset() {
		plane_velocity_x = 20f;
		planeVelocity.set(0f, 0f);
		planePosition.set(PLANE_START_X, PLANE_START_Y);
	}
	
	public void setPosition(float x, float y) {
		planePosition.set(x, y);
	}
	
	public void setVelocity(float x, float y) {
		planeVelocity.set(0,0);
	}
	
	public Vector2 getPosition() {
		return planePosition;
	}
	
	public Rectangle getBody() {
		return body;
	}
	
	public void update(float delta) {
		planeStateTime += delta;
		planeVelocity.add(GRAVITY);
		planePosition.mulAdd(planeVelocity, delta);
		
		body.set(planePosition.x + 2, planePosition.y, PLANE_WIDTH - 2, PLANE_HEIGHT);
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(
			plane.getKeyFrame(planeStateTime), 
			planePosition.x, planePosition.y, 
			PLANE_WIDTH, PLANE_HEIGHT);
	}
	
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		planeVelocity.set(plane_velocity_x, PLANE_JUMP_IMPULSE);
		
		return true;
	}
}
