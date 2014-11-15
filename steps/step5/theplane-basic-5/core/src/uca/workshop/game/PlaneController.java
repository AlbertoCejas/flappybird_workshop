package uca.workshop.game;

import com.badlogic.gdx.InputAdapter;

public class PlaneController extends InputAdapter {
	private static final float PLANE_JUMP_IMPULSE = 25f;
	
	private final Plane plane;
	
	public PlaneController(Plane plane) {
		this.plane = plane;
	}
	
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		plane.setVelocity(Plane.PLANE_VELOCITY_X, PLANE_JUMP_IMPULSE);
		
		return true;
	}
}
