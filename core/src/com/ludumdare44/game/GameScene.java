package com.ludumdare44.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Characters.*;
import com.ludumdare44.game.Controls.ControlManager;
import com.ludumdare44.game.Controls.PlayerControls;
import com.ludumdare44.game.Cutscenes.ScreenFader;
import com.ludumdare44.game.GFX.*;
import com.ludumdare44.game.GUI.huds.GameHud;
import com.ludumdare44.game.Map.*;
import com.ludumdare44.game.Physics.PhysicsObject;
import com.ludumdare44.game.Physics.VisualPhysObject;

import java.util.ArrayList;
import java.util.List;

public class GameScene implements Screen {

	public enum DeathType {
		DEVIL, LAVA;
	}

	public interface IGameOverListener {
		void onGameOver(int soulsCollected, DeathType deathBy);
	}

	// custom controller classes
	private ObjectManager objectManager;
	private CameraManager cameraManager;
	private GFXManager gfxManager;
	private SpriteManager spriteManager;
	private ScreenFader fader;
	private GameHud hud;

	private Player player;
	private Demon demon;
	private BatSpawner batSpawner;
	private ParticleGenerator particleGenerator;

	private boolean lavaDeath = false;
	private boolean demonDeath = false;
	private boolean gameStarted = false;
	private boolean gameReady = false;

	private ObjectAdder objectAdder;

	private ControlManager controlManager;
	private PlayerControls playerControls;

	private EndlessBackground background1, background2, background3;
	private LavaFloor lavaFloor;
	private CaveCeiling caveCeiling;
	private FakePlatform platform;

	private Music theme;

	//other
	private FPSLogger fpsLogger;

    // debug variables
    private Texture debugTexture;
    private Animation<TextureRegion> debugAnimation;
    private Sprite debugSprite;

	private float timeSpent = 0;
	private float cutsceneTime = 2.8f;

	private float timeSpentGameOver = 0;
	private float gameOverTime = 1f;

    private List<IGameOverListener> gameOverListeners = new ArrayList<>();

	//Main
	@Override
	public void show () {
		// display settings
		Vector2 screenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Vector2 startPos = new Vector2(screenSize.x / 2, screenSize.y / 5 * 3);

		gfxManager = new GFXManager(screenSize);

		fpsLogger = new FPSLogger();


		objectManager = new ObjectManager();

		objectAdder = new ObjectAdder() {
			@Override
			public void addPhysObject(PhysicsObject o) {
				objectManager.addObject(o);
			}

			@Override
			public void addRenderable(IRenderableObject o) {
				spriteManager.addObject(o);
			}


			@Override
			public void addVisObject(VisualPhysObject o) {
			    addPhysObject(o);
			    addRenderable(o);
			}
		};

		player = new DefaultPlayer(startPos, objectAdder);
		demon = new Demon(new Vector2(-1700, screenSize.y / 2), player);

		particleGenerator = new ParticleGenerator(demon, "assets/particle_flames.png");

		cameraManager = new CameraManager(screenSize, new Vector2(-2000, screenSize.y/2), player, new Vector2(1, 0));
		cameraManager.setShakeDuration(cutsceneTime);
		cameraManager.setShakeIntensity(15);
		cameraManager.screenShake();

		controlManager = new ControlManager();
		Gdx.input.setInputProcessor(controlManager);
		playerControls = new PlayerControls(controlManager, cameraManager, player);

		spriteManager = new SpriteManager(cameraManager);
		spriteManager.createLayers(3);
		// spriteManager.loadMap("assets/maps/test_map.tmx"); // no map
		// objectManager.setObstacles(spriteManager.getObstacles()); // no map

		batSpawner = new BatSpawner(cameraManager, objectAdder);

		objectAdder.addVisObject(demon);
		objectAdder.addVisObject(player);

		theme = Gdx.audio.newMusic(Gdx.files.internal("assets/theme.wav"));
		theme.setLooping(true);
		theme.setVolume(Constants.VOLUME_MULTIPLIER);
		theme.play();

		demon.setStagnant(true);
		player.setStagnant(true);

		fader = new ScreenFader();
		fader.setFadeTime(1).fadeIn();

		hud = new GameHud(this, player);

		Texture spriteSheet = new Texture("assets/tiles.png");
		int tileWidth = 16;
		int tileHeight = 16;

		TextureRegion[][] tileMap = TextureRegion.split(spriteSheet, tileWidth, tileHeight);

		caveCeiling = new CaveCeiling(cameraManager, objectManager, tileMap[0]);
		platform = new FakePlatform(new Vector2(screenSize.x / 2 - 16 * 3, screenSize.y / 5 * 3 - player.getModelSize().y / 2 - 32), 8, 2, tileMap[0]);

		objectAdder.addRenderable(platform);

		lavaFloor = new LavaFloor(cameraManager);

		Texture tempSheet = new Texture("assets/background1.png");
		TextureRegion[] tempMap = TextureRegion.split(tempSheet, tempSheet.getWidth(), tempSheet.getHeight())[0];
		background1 = new EndlessBackground(cameraManager, tempMap, true, 0.1f);

		tempSheet = new Texture("assets/background2.png");
		tempMap = TextureRegion.split(tempSheet, tempSheet.getWidth(), tempSheet.getHeight())[0];
		background2 = new EndlessBackground(cameraManager, tempMap, true, 0.5f);

		tempSheet = new Texture("assets/background3.png");
		tempMap = TextureRegion.split(tempSheet, tempSheet.getWidth(), tempSheet.getHeight())[0];
		background3 = new EndlessBackground(cameraManager, tempMap, true, 0.8f);

		debugTexture = new Texture("assets/debug.png");
		debugAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets/debug.gif").read());
		debugSprite = new Sprite(new Texture("assets/debug.png"), 20, 20);

		Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1.f);
	}

	@Override
	public void render (float delta) {
        // update
		controlManager.update();

		fpsLogger.log();

		timeSpent += delta;

		lavaFloor.update(delta);

		objectManager.update(delta);
		particleGenerator.update(delta);

		if (timeSpent > cutsceneTime) {
			if (timeSpent - delta < cutsceneTime) {
				cameraManager.setShakeIntensity(4);
				cameraManager.setShakeDuration(-1);
				cameraManager.screenShake();
			}

			gameReady = true;

			if (gameStarted) {
				if (!player.getSpeed().isZero()) gameStarted = true;

				if (!player.isDying()) {
					playerControls.update(delta);
				}

				batSpawner.update(delta);
				spriteManager.update(delta);
				cameraManager.update(delta);

				if (player.inLava()) { // death by lava
					if (!lavaDeath) player.setAnimation(player.getDeathAnimation());
					player.kill();
					lavaDeath = true;
				}

				if (!lavaDeath && player.getPos().x < demon.getPos().x + demon.getHitbox().x / 2) { // death by demon
					if (!demonDeath) player.setSprite(new Sprite(player.getDeathAnimation().getKeyFrames()[0]));
					player.kill();
					demonDeath = true;
				}

				if (player.isDestroyed()) {
					timeSpentGameOver += delta;
					if(timeSpentGameOver >= gameOverTime && timeSpentGameOver-delta < gameOverTime) {
						fader.fadeOut().onComplete(() -> {
							DeathType deathBy = demonDeath ? DeathType.DEVIL : DeathType.LAVA;
							for (IGameOverListener listener : gameOverListeners)
								listener.onGameOver(player.getSouls(), deathBy);
							gameOverListeners.clear();
						});
					}
				}
			} else if (controlManager.justPressed(Input.Keys.SPACE)) {
				player.setStagnant(false);
				demon.setStagnant(false);

				player.doJump();
				player.setSpeed(player.getFspeed().scl(.4f));
				gameStarted = true;
			}
		} else {
			cameraManager.update(delta);
		}

        // render

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gfxManager.batch.begin();

		cameraManager.render(gfxManager);

		background1.render(gfxManager);
		background2.render(gfxManager);
		background3.render(gfxManager);

		caveCeiling.render(gfxManager);
		if (platform.getPos().x + platform.getModelSize().x / 2.f > cameraManager.getPos().x - cameraManager.getScreenSize().x / 2) {
			platform.render(gfxManager);
		}

		particleGenerator.render(gfxManager);
        spriteManager.render(gfxManager);


        lavaFloor.render(gfxManager);

        if(Constants.DEBUG_MODE) {
			gfxManager.batch.end();
			gfxManager.shapeRenderer.setColor(Color.RED);
			gfxManager.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			ArrayList<PhysicsObject> objects = objectManager.getObjects();
			for (int i = 0; i < objects.size(); i++) {
				PhysicsObject pobj = objects.get(i);
				Rectangle r = ObjectManager.toRectangle(pobj);
				gfxManager.shapeRenderer.rect(r.x, r.y, r.width, r.height);
			}
			gfxManager.shapeRenderer.end();
			gfxManager.batch.begin();
		}
        //hud.render(gfxManager);

        gfxManager.resetProjection();
		gfxManager.batch.end();

		hud.render(delta);

		fader.render(gfxManager, delta);
	}

	@Override
	public void resize(int width, int height) {
		hud.resizeGui(width, height);
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {
		theme.stop();
	}

	@Override
	public void dispose () {
		gfxManager.dispose();
	}

	public GameScene onGameOver(IGameOverListener gameOverListener) {
		gameOverListeners.add(gameOverListener);
		return this;
	}

	public boolean isLavaDeath() {
		return lavaDeath;
	}

	public boolean isDemonDeath() {
		return demonDeath;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public boolean isGameReady() {
		return gameReady;
	}
}
