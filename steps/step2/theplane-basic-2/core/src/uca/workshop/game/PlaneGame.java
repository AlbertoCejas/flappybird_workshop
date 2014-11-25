package uca.workshop.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PlaneGame extends ApplicationAdapter {
	
	static enum GameState {
		Start,
		Running,
		GameOver
	}
	
	private static final int SCENE_WIDTH = 80;
	private static final int SCENE_HEIGHT = 60;
	
	private SpriteBatch batch;
	private Viewport viewport;
	private Viewport uiViewport;
	private OrthographicCamera camera;
	private OrthographicCamera uiCamera;
	
	private GameState gameState = GameState.Start;
	
	private Map map;
	
	private Texture ready;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		uiCamera = new OrthographicCamera();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		uiViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), uiCamera);
		// Center camera
		viewport.update(SCENE_WIDTH, SCENE_HEIGHT, true);
		uiViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		
		// Load ready texture
		ready = new Texture("ready.png");
		
		// Create game entities
		map = new Map();
	}

	@Override
	public void dispose () {
		batch.dispose();
		map.dispose();
		ready.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		uiViewport.update(width, height);
	}
	
	public void update() {
		switch(gameState) {
		case Start:
			updateStart();
			break;
		}
	}

	private void updateStart() {
		if(Gdx.input.justTouched()) {
			gameState = GameState.Running;
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		update();
		
		drawWorld();
		drawUI();
	}

	private void drawWorld() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		map.draw(batch, camera.position.x - SCENE_WIDTH*.5f);
		
		batch.end();
	}
	
	private void drawUI() {
		batch.setProjectionMatrix(uiCamera.combined);
		batch.begin();
		
		if(gameState == GameState.Start) {
			batch.draw(ready, uiViewport.getWorldWidth() / 2 - ready.getWidth() / 2, uiViewport.getWorldHeight() / 2 - ready.getHeight() / 2);
		}

		batch.end();
	}
}
