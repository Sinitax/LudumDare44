package com.ludumdare44.game.Physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;

public class Grapple extends VisualPhysObject {
    private Animation<TextureRegion> flyingAnimation;
    private Animation<TextureRegion> collideAnimation;
    private Animation<TextureRegion> currentAnimation;

    private float stateTime = 0;
    private Vector2 ipos;
    private Vector2 rpos;

    protected boolean destroy = false;

    @Override
    public boolean alive() {
        return !this.getSpeed().isZero();
    }

    @Override
    public boolean stagnant() {
        return false;
    }

    @Override
    public boolean visible() {
        return this.alive();
    }

    @Override
    public Vector2 getHitbox() {
        return null;
    }

    @Override
    public Vector2 getHitboxOffset() {
        return null;
    }

    @Override
    public void onCollision(VisualPhysObject other) {

    }

    @Override
    public int getZ() {
        return 2;
    }

    @Override
    public Vector2 getModelSize() {
        return null;
    }

    @Override
    public Vector2 getOriginOffset() {
        return null;
    }

    //VisualPhysObject
    @Override
    public int getFspeedMax() {
        return 0;
    }

    @Override
    public int getDecelMax() {
        return 0;
    }
    
    @Override
    public int getAccelMax() {
        return -1;
    }
    

    @Override
    public void update(float delta) {
        stateTime += delta;
        if (!hitGround) {
            updatePos(delta);
            if (hitGround) currentAnimation = collideAnimation;
        }
    }
    
    @Override
    public void render(GFXManager gfx) {
        Sprite sprite = new Sprite(currentAnimation.getKeyFrame(stateTime));
        gfx.drawModel(sprite, getPos());
    }
    
    public void launchToPointer() {
        Vector2 mouseVec = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        launch(mouseVec);
    }
    public void launch(Vector2 _fpos) {
        ipos = new Vector2(getPos());
        setFspeed(new Vector2(rpos));
    }

    public Grapple(Vector2 pos) {
        super(new Vector2(pos));
    }
}