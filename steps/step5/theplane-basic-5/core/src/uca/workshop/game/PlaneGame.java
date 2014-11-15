package uca.workshop.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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
	
	private Plane plane;
	private Map map;
	private PlaneController planeController;
	
	private float groundOffsetX = 0;
	
	private Texture ready, gameOver;
	private BitmapFont font;

	// score
	
	// music
	private Music music;
	private Sound explode;
	
	private boolean debugOn = false;
	private ShapeRenderer sr;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		uiCamera = new OrthographicCamera();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		uiViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), uiCamera);
		
		viewport.update(SCENE_WIDTH, SCENE_HEIGHT, true);
		uiViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		
		// Initialise font to draw the UI


		
		// Ready and gameover
		ready = new Texture("ready.png");


		
		// Create game entities
		plane = new Plane();
		planeController = new PlaneController(plane);
		map = new Map();
		
		// Music


		
		
		// Sound effect played when the plane collides


		// Debug
		sr = new ShapeRenderer();
	}

	@Override
	public void dispose () {
		batch.dispose();
		plane.dispose();
		map.dispose();
		ready.dispose();


		
		

	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		uiViewport.update(width, height);
	}
	
	public void reset() {
		groundOffsetX = 0;
		score = 0;
		
		camera.position.x = SCENE_WIDTH*.5f;
		camera.update();
		
		plane.reset();
		map.reset();
	}
	
	public void update() {
		switch(gameState) {
		case Start:
			updateStart();
			break;
		case Running:
			updateRunning();
			break;
		case GameOver:
			updateGameOver();
			break;
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		update();
		
		drawWorld();
		drawDebug();
		drawUI();
	}

	private void drawDebug() {
		if(debugOn) {
			sr.setProjectionMatrix(camera.combined);
			sr.begin(ShapeType.Line);
			plane.debug(sr);
			for(Rock r : map.getRocks()) {
				r.debug(sr);
			}
			sr.end();
		}
	}
	
	private void drawUI() {
		batch.setProjectionMatrix(uiCamera.combined);
		batch.begin();
		// Start
		if(gameState == GameState.Start) {
			batch.draw(ready, uiViewport.getWorldWidth() / 2 - ready.getWidth() / 2, uiViewport.getWorldHeight() / 2 - ready.getHeight() / 2);
		}
		// GameOver

		
		// Score -> GameOver or Running 

		
		batch.end();
	}

	private void drawWorld() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		map.draw(batch, camera.position.x - SCENE_WIDTH*.5f, groundOffsetX);
		plane.draw(batch);
		batch.end();
	}
	
	private void updateGameOver() {
		if(Gdx.input.justTouched()) {
			gameState = GameState.Start;
			reset();
		}
	}

	private void updateRunning() {
		float delta = Gdx.graphics.getDeltaTime();
		
		updateCamera();
		
		// Enables debug lines
		if(Gdx.input.isKeyJustPressed(Input.Keys.D))
			debugOn = !debugOn;
		
		// Stores the X distance made by the plane
		if(camera.position.x - groundOffsetX > SCENE_WIDTH + 40) {
			groundOffsetX += SCENE_WIDTH;
		}
		
		// Update game entities
		map.update(camera.position.x);
		plane.update(delta);
		
		checkCollisions();
	}
	
	private void updateStart() {
		if(Gdx.input.justTouched()) {
			gameState = GameState.Running;
			Gdx.input.setInputProcessor(planeController);
			plane.setVelocity(Plane.PLANE_VELOCITY_X, 0);
		}
	}

	private void checkCollisions() {
		Vector2 planePosition = plane.getPosition();
		
		Array<Rock> rocks = map.getRocks();
		
		for(Rock r : rocks) {
			// Check collisions with rocks

			
			
			
			
			// Check if score should be raised by 1

			
			
		}
		
		// Ground Collision

		
		
	}

	private void updateCamera() {
		camera.position.x = plane.getPosition().x + 20f;
		camera.update();
	}
}
