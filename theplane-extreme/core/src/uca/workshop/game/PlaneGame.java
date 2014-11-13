package uca.workshop.game;

import box2dLight.PointLight;
import box2dLight.RayHandler;

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
import com.badlogic.gdx.physics.box2d.World;
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
	private TextureRegion readyRegion, gameOverRegion;
	private BitmapFont font;
	private int score = 0;
	
	private Music music;
	private Sound explode;
	
	private boolean debugOn = false;
	private ShapeRenderer sr;
	
	// Box2D world to include lights
	private World world;
	// Lights handler
	private RayHandler rayHandler;
	private PointLight pointLight;
	Vector2 planePos;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		uiCamera = new OrthographicCamera();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		uiViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), uiCamera);
		
		camera.position.x += SCENE_WIDTH*.5f;
		camera.position.y += SCENE_HEIGHT*.5f;
		camera.update();
		
		uiCamera.position.x += Gdx.graphics.getWidth()*.5f;
		uiCamera.position.y += Gdx.graphics.getHeight()*.5f;
		uiCamera.update();
		
		// Initialise font to draw the UI
		font = new BitmapFont(Gdx.files.internal("arial.fnt"));
		font.setColor(Color.WHITE);
		
		ready = new Texture("ready.png");
		gameOver = new Texture("gameover.png");
		readyRegion = new TextureRegion(ready);
		gameOverRegion = new TextureRegion(gameOver);
		
		// Create game entities
		plane = new Plane();
		planeController = new PlaneController(plane);
		map = new Map();
		
		// Music
		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		music.play();
		
		// Sound effect played when the plane collides
		explode = Gdx.audio.newSound(Gdx.files.internal("explode.wav"));

		// To draw debug lines
		sr = new ShapeRenderer();
		
		// Initialize Box2D stuff
		world = new World(new Vector2(0, -9.8f), true);
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0.1f, 0.1f, 0.1f, 0.025f);
		
		planePos = plane.getPosition();
		pointLight = new PointLight(rayHandler, 32, Color.WHITE, 25, planePos.x + Plane.PLANE_WIDTH * .5f, planePos.y + Plane.PLANE_HEIGHT * .5f);
	}

	@Override
	public void dispose () {
		batch.dispose();
		plane.dispose();
		map.dispose();
		ready.dispose();
		gameOver.dispose();
		font.dispose();
		music.dispose();
		explode.dispose();
		sr.dispose();
		rayHandler.dispose();
		world.dispose();
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
		drawLights();
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
		if(gameState == GameState.Start) {
			batch.draw(ready, Gdx.graphics.getWidth() / 2 - readyRegion.getRegionWidth() / 2, Gdx.graphics.getHeight() / 2 - readyRegion.getRegionHeight() / 2);
		}
		if(gameState == GameState.GameOver) {
			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOverRegion.getRegionWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOverRegion.getRegionHeight() / 2);
		}
		if(gameState == GameState.GameOver || gameState == GameState.Running) {
			font.draw(batch, "" + score, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 60);
		}
		batch.end();
	}

	private void drawWorld() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		map.draw(batch, camera.position.x - SCENE_WIDTH*.5f, groundOffsetX);
		plane.draw(batch);
		batch.end();
	}
	
	private void drawLights() {
		rayHandler.setCombinedMatrix(viewport.getCamera().combined);
		rayHandler.updateAndRender();
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
		
		// Update light
		planePos = plane.getPosition();
		pointLight.setPosition(planePos.x + Plane.PLANE_WIDTH * .5f, planePos.y + Plane.PLANE_HEIGHT * .5f);
		
		checkCollisions();
	}
	
	private void updateStart() {
		if(Gdx.input.justTouched()) {
			gameState = GameState.Running;
			Gdx.input.setInputProcessor(planeController);
			plane.setVelocity(plane.getEngineXForce(), 0);
		}
	}

	private void checkCollisions() {
		Vector2 planePosition = plane.getPosition();
		
		Array<Rock> rocks = map.getRocks();
		
		for(Rock r : rocks) {
			// Check collisions with rocks
			if (Entity.collide(r, plane)) {
				if(gameState != GameState.GameOver)
					explode.play();
				
				Gdx.input.setInputProcessor(null);
				gameState = GameState.GameOver;
				plane.setVelocity(0,0);	
			}
			
			// Check if score should be raised by 1
			if(r.getPosition().x < planePosition.x && !r.counted) {
				score++;
				r.counted = true;
				plane.increaseEngineXForce(1);
			}
		}
		
		// Ground Collision
		if(planePosition.y < Map.GROUND_HEIGHT - 2 || 
			planePosition.y + Plane.PLANE_HEIGHT > SCENE_HEIGHT - Map.GROUND_HEIGHT + 2) {
				if(gameState != GameState.GameOver) 
					explode.play();
			
			Gdx.input.setInputProcessor(null);
			gameState = GameState.GameOver;
			plane.setVelocity(0,0);
		}
	}

	private void updateCamera() {
		camera.position.x = plane.getPosition().x + 20f;
		camera.update();
	}
}
