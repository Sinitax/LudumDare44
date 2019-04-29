package com.ludumdare44.game.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Constants;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.Physics.PhysicsObject;
import com.ludumdare44.game.Physics.VisualPhysObject;

public class Demon extends VisualPhysObject {
    private Sprite sprite = new Sprite(new Texture("assets/devil.png"));

    private float timePassed = 0;

    private float bobAmount = 50;
    private float bobSpeed = 3f;
    private float bobHeight;

    private float maxFollowDist = 700;

    @Override
    public Vector2 getHitbox() {
        return getModelSize().scl(0.5f);
    }

    @Override
    public Vector2 getHitboxOffset() {
        return new Vector2(0, 0);
    }

    @Override
    public void onCollision(PhysicsObject other, float delta) {
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
        return -1;
    }

    @Override
    public Vector2 getOriginOffset() {
        return new Vector2(0, 0);
    }

    @Override
    public boolean isDestroyed() {
        return false;
    }

    @Override
    public void destroy() {
    }

    private boolean stagnant = false;

    @Override
    public boolean isStagnant() {
        return stagnant;
    }

    public void setStagnant(boolean v) {
        stagnant = v;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public Vector2 getModelSize() {
        return new Vector2(sprite.getWidth() * Constants.PIXEL_SCALE, sprite.getHeight() * Constants.PIXEL_SCALE);
    }

    @Override
    public void update(float delta) {
        if (!Constants.DEBUG_MODE) {
            timePassed += delta;

            float followDist = getFollowObject().getPos().x - getPos().x;

            approachSpeed = Math.min(1, Math.max(0.5f, followDist / (maxFollowDist / 2)));

            updateSpeed(delta);

            setPos(new Vector2(getPos().x, (float) (bobHeight + Math.sin(timePassed * bobSpeed) * bobAmount)));
            if (followDist > maxFollowDist)
                setPos(new Vector2(getFollowObject().getPos().x - maxFollowDist, getPos().y));
            updatePos(delta);
        }
    }

    @Override
    public void render(GFXManager gfx) {
        gfx.batch.draw(sprite, getPos().x - getModelSize().x / 2, getPos().y - getModelSize().y / 2, getModelSize().x, getModelSize().y);
    }

    public Demon(Vector2 spos, PhysicsObject followObject) {
        super(spos);
        bobHeight = spos.y;
        approachSpeed = 1;
        follow(followObject);
    }
}
