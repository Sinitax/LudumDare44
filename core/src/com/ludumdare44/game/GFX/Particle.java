package com.ludumdare44.game.GFX;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Constants;
import com.ludumdare44.game.Physics.PhysicsObject;
import com.ludumdare44.game.Physics.VisualPhysObject;

public class Particle extends VisualPhysObject {
    private Animation<TextureRegion> animation;
    private float timeSpent = 0.f;
    private float animationTimer = 0.f;
    private float particleVelocityMax = 270.f;
    private Vector2 posOffset = new Vector2(0, -60);

    private boolean hflip;

    private int frameWidth, frameHeight = 0;

    @Override
    public Vector2 getModelSize() {
        return new Vector2(frameWidth * Constants.PIXEL_SCALE, frameHeight * Constants.PIXEL_SCALE);
    }

    @Override
    public Vector2 getOriginOffset() {
        return new Vector2(0, 0);
    }

    @Override
    public boolean isStagnant() {
        return !isDestroyed();
    }

    @Override
    public void onCollision(PhysicsObject other, float delta) {}

    @Override
    public boolean isVisible() {
        return !isDestroyed();
    }

    @Override
    public boolean isDestroyed() {
        return timeSpent > animation.getAnimationDuration();
    }

    @Override
    public void destroy() {}

    @Override
    public Vector2 getHitbox() {
        return new Vector2(0, 0);
    }

    @Override
    public int getDecelMax() {
        return 100;
    }

    @Override
    public int getFspeedMax() {
        return 1000;
    }

    @Override
    public int getAccelMax() {
        return 100;
    }

    public void update(float delta) {
        timeSpent += delta;
        animationTimer = (animationTimer + delta) % animation.getAnimationDuration();

        updateSpeed(delta);
        updatePos(delta);
    }

    public void render(GFXManager gfx) {
        Sprite s = new Sprite(animation.getKeyFrame(animationTimer));
        s.flip(hflip, false);
        s.setAlpha(0.05f);
        gfx.batch.draw(s, getPos().x + posOffset.x - getModelSize().x / 2, getPos().y + posOffset.y - getModelSize().y /2 , getModelSize().x, getModelSize().y);
    }

    public Particle(Vector2 spos, Animation<TextureRegion> _animation) {
        super(spos);
        hflip = (Math.random() > 0.5);
        animation = _animation;
        weight = -10;
        frameWidth = animation.getKeyFrames()[0].getRegionWidth();
        frameHeight = animation.getKeyFrames()[0].getRegionHeight();
        Vector2 speed = new Vector2((float) (Math.random() - 0.5f), (float) (Math.random() - 0.5f)).nor().scl((float) Math.random() * particleVelocityMax);
        setFspeedAbs(speed);
        setSpeed(speed);
    }
}

