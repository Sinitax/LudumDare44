package com.ludumdare44.game.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.Physics.PhysicsObject;
import com.ludumdare44.game.Physics.VisualPhysObject;
import com.ludumdare44.game.UI.CameraManager;

public class Demon extends VisualPhysObject {
    private Sprite sprite = new Sprite(new Texture("assets/demon.png"));
    private CameraManager cameraManager;

    @Override
    public Vector2 getHitbox() {
        return new Vector2(80, 80);
    }

    @Override
    public Vector2 getHitboxOffset() {
        return new Vector2(0, 0);
    }

    @Override
    public void onCollision(PhysicsObject other) {
        if (other instanceof Player) {
            ((Player) other).kill();
        }
    }

    @Override
    public int getAccelMax() {
        return 200;
    }

    @Override
    public int getDecelMax() {
        return 1000;
    }

    @Override
    public int getFspeedMax() {
        return 80;
    }

    @Override
    public Vector2 getOriginOffset() {
        return null;
    }

    @Override
    public boolean alive() {
        return true;
    }

    @Override
    public boolean stagnant() {
        return false;
    }

    @Override
    public boolean visible() {
        return true;
    }

    @Override
    public Vector2 getModelSize() {
        return null;
    }

    @Override
    public void update(float delta) {
        updateSpeed(delta);
        updatePos(delta);
    }

    @Override
    public void render(GFXManager gfx) {
        gfx.batch.draw(sprite, getPos().x, getPos().y);
    }

    public Demon(CameraManager _cameraManager) {
        super(new Vector2(-1800, 0));
        cameraManager = _cameraManager;
    }
}
