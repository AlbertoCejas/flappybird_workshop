package uca.workshop.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
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
		super(PLANE_WIDTH, PLANE_HEIGHT, PLANE_WIDTH - 2.0f, PLANE_HEIGHT);
		
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
		
		Vector2 position = getPosition();
		
		velocity.add(GRAVITY);
		position.mulAdd(velocity, delta);

		super.update(delta);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		setRegion(animation.getKeyFrame(stateTime));
		super.draw(batch);
	}
}
