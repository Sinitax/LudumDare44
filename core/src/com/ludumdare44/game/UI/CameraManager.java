package com.ludumdare44.game.UI;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.GFX.IRenderable;
import com.ludumdare44.game.Physics.PhysicsObject;

import java.util.Random;

public class CameraManager extends PhysicsObject implements IRenderable {
    private final int maxUpdateDist = 500;
    OrthographicCamera camera;

    private float shakeDuration = 0;
    private float shakeElapsed = 0;
    private float shakeIntensity = 10;
    private float shakeX = 0;
    private float shakeY = 0;

    private Random random;

    private Vector2 screenSize;
    public Vector2 getScreenSize() {
        return screenSize;
    }

    @Override
    public int getDecelMax() {
        return 3000;
    }
    
    @Override
    public int getAccelMax() {
        return 1500;
    }
    
    @Override
    public int getFspeedMax() {
        return 0;
    }

    public void setShakeDuration(float duration) {
        shakeDuration = duration;
    }

    public void setShakeIntensity(float intensity) {
        shakeIntensity = intensity;
    }

    public void screenShake() {
        shakeElapsed = 0;
    }

    public void update(float delta) {
        //update position
        updatePos(delta);

        // camera shake
        if(shakeElapsed < shakeDuration) {
            float currentPower = shakeIntensity * ((shakeDuration - shakeElapsed) / shakeDuration);
            shakeX = -(random.nextFloat() - 0.5f) * currentPower;
            shakeY = -(random.nextFloat() - 0.5f) * currentPower;

            shakeElapsed += delta;
        }

    }

    public void render(GFXManager gfx) {
        camera = (OrthographicCamera) gfx.viewport.getCamera();
        float sx = (int) getPos().x + (int) shakeX - (int) screenSize.x/2;
        float sy = (int) getPos().y + (int) shakeY - (int) screenSize.y/2;
        camera.combined.set(new Matrix4().setToOrtho2D(sx, sy, screenSize.x, screenSize.y));
        gfx.shapeRenderer.setProjectionMatrix(camera.combined);
        gfx.batch.setProjectionMatrix(camera.combined);
    }
    
    public boolean shouldRender(Vector2 _pos, int maxlen) {
        if (_pos.x + maxlen > getPos().x - screenSize.x/2 && _pos.x - maxlen < getPos().x + screenSize.x/2) {
            if(_pos.y + maxlen > getPos().y - screenSize.y/2 && _pos.y - maxlen < getPos().y + screenSize.y/2) {
                return true;
            }
        }
        return false;
    }
    
    public boolean shouldUpdate(Vector2 _pos, int maxlen) {
        if (_pos.x + maxlen > getPos().x - screenSize.x/2 - maxUpdateDist && _pos.x - maxlen < getPos().x + screenSize.x/2 + maxUpdateDist) {
            if(_pos.y + maxlen > getPos().y - screenSize.y/2 - maxUpdateDist && _pos.y - maxlen < getPos().y + screenSize.y/2 + maxUpdateDist) {
                return true;
            }
        }
        return false;
    }
    
    public CameraManager(Vector2 _screenSize, Vector2 _pos) {
        super(new Vector2(_pos));
        random = new Random();
        screenSize = _screenSize;
        approachSpeed = 4;
    }
}
