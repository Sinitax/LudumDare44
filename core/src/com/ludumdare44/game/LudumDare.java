package com.ludumdare44.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Characters.Player;
import com.ludumdare44.game.Characters.DefaultPlayer;
import com.ludumdare44.game.Controls.ControlManager;
import com.ludumdare44.game.Controls.MenuControls;
import com.ludumdare44.game.Controls.PlayerControls;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.GFX.GifDecoder;
import com.ludumdare44.game.GFX.IRenderable;
import com.ludumdare44.game.GFX.IRenderableObject;
import com.ludumdare44.game.Map.*;
import com.ludumdare44.game.Physics.PhysicsObject;
import com.ludumdare44.game.Physics.VisualPhysObject;
import com.ludumdare44.game.UI.CameraManager;
import com.ludumdare44.game.UI.HUD;
import com.ludumdare44.game.UI.Menu.ControlsMenu;
import com.ludumdare44.game.UI.Menu.Menu;
import com.ludumdare44.game.UI.Menu.MenuManager;
import org.w3c.dom.css.Rect;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class LudumDare extends ApplicationAdapter {
	// custom controller classes
	private ObjectManager objectManager;
	private CameraManager cameraManager;
	private GFXManager gfxManager;
	private Player player;
	private HUD hud;
	private SpriteManager spriteManager;

	private ObjectAdder objectAdder;

	private ControlManager controlManager;
	private PlayerControls playerControls;
	private MenuControls menuControls;

	private EndlessBackground background1, background2;
	private CaveCeiling caveCeiling;

	// game settings
	private MenuManager menuManager;

	//other
	private FPSLogger fpsLogger;

    // debug variables
    private Texture debugTexture;
    private Animation<TextureRegion> debugAnimation;
    private Sprite debugSprite;

    private float timeSpent = 0;
    private float cutsceneTime = 2.5f;

    private void addObject(VisualPhysObject obj) {
		spriteManager.addObject(obj);
    	objectManager.addObject(obj);
	}

	//Main
	@Override
	public void create () {
		// display settings
		Vector2 screenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Vector2 startPos = new Vector2(screenSize.x/2, screenSize.y/2);

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

		cameraManager = new CameraManager(screenSize, new Vector2(-2000, screenSize.y/2), player, new Vector2(1, 0));
		cameraManager.setShakeDuration(cutsceneTime);
		cameraManager.setShakeIntensity(15);
		cameraManager.screenShake();

		controlManager = new ControlManager();
		Gdx.input.setInputProcessor(controlManager);
		playerControls = new PlayerControls(controlManager, cameraManager, player);
		menuControls = new MenuControls(controlManager, menuManager);

		menuManager = new MenuManager();
		spriteManager = new SpriteManager(cameraManager);
		spriteManager.createLayers(3);
		// spriteManager.loadMap("assets/maps/test_map.tmx"); // no map
		// objectManager.setObstacles(spriteManager.getObstacles()); // no map
		addObject(player);

		Texture spriteSheet = new Texture("assets/textures/tiles.png");
		int tileWidth = 16;
		int tileHeight = 16;

		TextureRegion[][] tileMap = TextureRegion.split(spriteSheet, tileWidth, tileHeight);

		caveCeiling = new CaveCeiling(cameraManager, objectManager, tileMap[0]);

		hud = new HUD(player);
		objectManager.addObject(player);

		Texture tempSheet = new Texture("assets/background2.png");
		TextureRegion[] tempMap = TextureRegion.split(tempSheet, tempSheet.getWidth(), tempSheet.getHeight())[0];
		background1 = new EndlessBackground(cameraManager, tempMap, true);

		tempSheet = new Texture("assets/background3.png");
		tempMap = TextureRegion.split(tempSheet, tempSheet.getWidth(), tempSheet.getHeight())[0];
		background2 = new EndlessBackground(cameraManager, tempMap, true);

		debugTexture = new Texture("assets/debug.png");
		debugAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets/debug.gif").read());
		debugSprite = new Sprite(new Texture("assets/debug.png"), 20, 20);

		Gdx.gl.glClearColor(0.17f, 0.17f, 0.17f, 1.f);
	}

	@Override
	public void render () {
        // update

		controlManager.update();
		menuControls.update();

		if (menuManager.enabled()) {
			// menu controls
			gfxManager.batch.begin();
			menuManager.render(gfxManager);
			gfxManager.batch.end();
			return;
		}

		fpsLogger.log();

		float delta = Gdx.graphics.getDeltaTime();

		timeSpent += delta;

		if (timeSpent > cutsceneTime) {
			if (timeSpent - delta < cutsceneTime) {
				cameraManager.setShakeIntensity(4);
				cameraManager.setShakeDuration(-1);
				cameraManager.screenShake();
			}
			playerControls.update(delta);

			objectManager.update(delta);
			spriteManager.update(delta);
			cameraManager.update(delta);
			// AnimatedTiledMapTile.updateAnimationBaseTime();
		} else {
			cameraManager.update(delta);
		}

        // render

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gfxManager.batch.begin();

		cameraManager.render(gfxManager);

		background1.render(gfxManager);
		background2.render(gfxManager);

		caveCeiling.render(gfxManager);
        spriteManager.render(gfxManager);

        /*
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
        //hud.render(gfxManager);
         */

        gfxManager.resetProjection();
		gfxManager.batch.end();
	}

	@Override
	public void dispose () {
		gfxManager.dispose();
	}
}
