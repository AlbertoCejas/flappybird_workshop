package uca.workshop.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import uca.workshop.game.Map.Rock;

public class PlaneGame extends ApplicationAdapter {
	
	static enum GameState {
		Start, Running, GameOver
	}
	
	private static final int SCENE_WIDTH = 80;
	private static final int SCENE_HEIGHT = 60;
	
	private SpriteBatch batch;
	private Viewport viewport;
	private Viewport viewport2;
	private OrthographicCamera camera;
	private OrthographicCamera UIcamera;
	
	private GameState gameState = GameState.Start;
	
	private Plane plane;
	private Map map;
	
	private float groundOffsetX = 0;
	
	private Texture ready, gameOver;
	private TextureRegion readyRegion, gameOverRegion;
	private BitmapFont font;
	private int score = 0;
	
	private Rectangle body1, body2 = new Rectangle();
	
	private Music music;
	private Sound explode;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		UIcamera = new OrthographicCamera();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		viewport2 = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), UIcamera);
		
		camera.position.x += SCENE_WIDTH*.5f;
		camera.position.y += SCENE_HEIGHT*.5f;
		camera.update();
		
		UIcamera.position.x += Gdx.graphics.getWidth()*.5f;
		UIcamera.position.y += Gdx.graphics.getHeight()*.5f;
		UIcamera.update();
		
		// Initialize font to draw the UI
		font = new BitmapFont(Gdx.files.internal("arial.fnt"));
		font.setColor(Color.BLACK);
		
		ready = new Texture("ready.png");
		gameOver = new Texture("gameover.png");
		readyRegion = new TextureRegion(ready);
		gameOverRegion = new TextureRegion(gameOver);
		
		// Create game entities
		plane = new Plane();
		map = new Map();
		
		// Music
		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		music.play();
		
		explode = Gdx.audio.newSound(Gdx.files.internal("explode.wav"));
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
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		viewport2.update(width, height);
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
		
		float delta = Gdx.graphics.getDeltaTime();

		if(Gdx.input.justTouched()) {
			if(gameState == GameState.Start) {
				gameState = GameState.Running;
				Gdx.input.setInputProcessor(plane);
				plane.setVelocity(Plane.PLANE_VELOCITY_X, 0);
			}
			if(gameState == GameState.GameOver) {
				gameState = GameState.Start;
				reset();
			}
		}
		
		// Keeps the camera focusing the plane
		camera.position.x = plane.getPosition().x + 20f;
		camera.update();
		
		if(gameState == GameState.Running) {	
			// Stores the X distance made by the plane
			if(camera.position.x - groundOffsetX > SCENE_WIDTH + 40) {
				groundOffsetX += SCENE_WIDTH;
			}
			
			// Update game entities
			map.update(camera.position.x);
			plane.update(delta);
			
			// Check collisions - Takes advantage of the upcoming loop to avoid looping twice
			body1 = plane.getBody();
			
			// Update UI
			// Update score from rocks
			Array<Rock> rocks = map.getRocks();
			
			for(Rock r : rocks) {
				// Check collisions with rocks
				body2.set(r.position.x + (Map.Rock.ROCK_WIDTH - 3f) / 2 + 1, r.position.y, 2f, Map.Rock.ROCK_HEIGHT - 1.25f);
				if(body1.overlaps(body2)) {
					if(gameState != GameState.GameOver)
						explode.play();
					
					Gdx.input.setInputProcessor(null);
					gameState = GameState.GameOver;
					plane.setVelocity(0,0);	
				}
				
				// Check if score should be raised by 1
				if(r.position.x < plane.getPosition().x && !r.counted) {
					score++;
					r.counted = true;
				}
			}
			
			Vector2 position = plane.getPosition();
			
			if(position.y < Map.GROUND_HEIGHT - 2 || 
				position.y + Plane.PLANE_HEIGHT > SCENE_HEIGHT - Map.GROUND_HEIGHT + 2) {
					if(gameState != GameState.GameOver) 
						explode.play();
				
				Gdx.input.setInputProcessor(null);
				gameState = GameState.GameOver;
				plane.setVelocity(0,0);
			}
		}
	}
	
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		update();
		
		// Draw world
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		map.draw(batch, camera.position.x - SCENE_WIDTH*.5f, groundOffsetX);
		plane.draw(batch);
		batch.end();
		
		// Draw score
		batch.setProjectionMatrix(UIcamera.combined);
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
}
