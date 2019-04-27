package com.ludumdare44.game.GFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class GFXManager {
    public boolean DEBUG = false;

	public Matrix4 uiMatrix;
	public ExtendViewport viewport;
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	public OrthographicCamera camera;

	public Vector2 screenSize;

    // wrappers
	public void drawModel(Sprite sprite, Vector2 pos) { drawModel(sprite, pos, new Vector2(1,1));}
	public void drawModel(Sprite sprite, Vector2 pos, Vector2 scale) {
		Sprite temp = sprite;
		temp.setSize(sprite.getWidth() * scale.x, sprite.getHeight() * scale.y);
		temp.setPosition(pos.x - sprite.getWidth()/2, pos.y - sprite.getHeight()/2);
		temp.draw(this.batch);
	}
	public void drawModel(Texture texture, Vector2 pos) {
		this.batch.draw(texture, pos.x - texture.getWidth()/2, pos.y - texture.getHeight()/2);
	}

	public void drawDebug(Vector2 pos) {
	    boolean wasDrawing = false;
		if (this.batch.isDrawing()) {
            this.batch.end();
		    wasDrawing = true;
        }
        boolean wasRendering = false;
        if (!this.shapeRenderer.isDrawing()) {
            this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            wasRendering = true;
        }
		this.shapeRenderer.setColor(1,0,0,1);
		this.shapeRenderer.circle(pos.x, pos.y , 3);
		if (wasRendering) this.shapeRenderer.end();
		if (wasDrawing) this.batch.begin();
	}

	public void drawShadow(float x, float y, float width, float height) {
		this.batch.end();
		this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		this.shapeRenderer.setColor(0,0,0,0.4f);
		this.shapeRenderer.ellipse(x - width/2, y - height/2, width, height);
		this.shapeRenderer.end();
		this.batch.begin();
	}

	public void drawFrame(Vector2 pos, Vector2 rect) {
		boolean wasDrawing = false;
		if(batch.isDrawing()) {
			batch.end();
			wasDrawing = true;
		}
		this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.rect(pos.x - rect.x/2, pos.y - rect.y/2, rect.x, rect.y);
		shapeRenderer.end();
		if(wasDrawing) batch.begin();
	}

	public void drawLine(Vector2 p1, Vector2 p2) {
        boolean wasDrawing = false;
        if(batch.isDrawing()) {
            batch.end();
            wasDrawing = true;
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(p1, p2);
        shapeRenderer.end();
        if(wasDrawing) batch.begin();
    }

	public void resetProjection() {
		this.batch.setProjectionMatrix(this.uiMatrix);
		shapeRenderer.setProjectionMatrix(uiMatrix);
	}

	public void dispose() {
		this.batch.dispose();
	}

    public GFXManager(Vector2 _screenSize) {
		Gdx.graphics.setResizable(false);
		Gdx.graphics.setUndecorated(true);
		Gdx.gl.glClearColor(0, 0, 0, 1);

		this.screenSize = _screenSize;
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		viewport = new ExtendViewport(screenSize.x, screenSize.y, camera);
		uiMatrix = new Matrix4();
		uiMatrix.setToOrtho2D(0, 0, screenSize.x, screenSize.y);
	}
}
