package com.ludumdare44.game.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.LudumDare;
import com.ludumdare44.game.Physics.Grapple;
import com.ludumdare44.game.Physics.PhysicsObject;
import com.ludumdare44.game.Physics.VisualPhysObject;

public abstract class Player extends VisualPhysObject {
    private com.ludumdare44.game.Characters.Ability special;
    public abstract com.ludumdare44.game.Characters.Ability getSpecial();
    public com.ludumdare44.game.Characters.Ability attack;
    public abstract com.ludumdare44.game.Characters.Ability getAttack();

    protected Animation<TextureRegion> currentAnimation;
    public Sprite sprite;

    private float swingAmount  = 0;

    private boolean facingRight;

    private Grapple grapple;
    private boolean grappled;

    public float animationTime;
    private boolean busy = false;
    public boolean isBusy() { return busy; }
    public void setBusy(boolean _busy) { busy = _busy;}

    private boolean dead;

    private float energyMax = 50;

    private float energy = 0;
    public final float getEnergyMax() { return energyMax; }
    public final float getEnergy() { return energy; }
    public final void setEnergy(float _energy) {
        energy = _energy;
        if (energy < 0) energy = 0;
        if (energy > energyMax) energy = energyMax;
    }

    public abstract Texture getIcon();
    public final Vector2 getModelSize() {
        return getHitbox();
    }
    public abstract Vector2 getModelScale();
    
    @Override
    public Vector2 getHitbox() { return new Vector2(32, 95); }
    @Override
    public Vector2 getHitboxOffset() {
        return new Vector2(0, 0);
    }
    
    @Override
    public int getAccelMax() { return 1000; }
    @Override
    public int getDecelMax() { return 1700; }
    @Override
    public int getFspeedMax() { return 300; }


    @Override
    public boolean alive() { return !dead; }

    @Override
    public boolean stagnant() { return false; }

    @Override
    public boolean visible() { return true; }

    public abstract Animation<TextureRegion> getAirborneAnimation();
    public abstract Animation<TextureRegion> getLeftSwingAnimation();
    public abstract Animation<TextureRegion> getRightSwingAnimation();
    public abstract Animation<TextureRegion> getGrappleAnimation();

    /*
    public void faceDir(float x, boolean isRelative) {
        if (!isRelative) x = getRelativePos(new Vector2(x, 0)).x;
        if (x > 0) facingRight = true;
        else if (x < 0) facingRight = false;
    }

    public Vector2 getRelativePos(Vector2 pos) {
        Vector2 rpos = new Vector2(pos);
        rpos.add(new Vector2(cameraManager.getPos()).sub(new Vector2(screenSize).scl(0.5f)));
        rpos.sub(getPos());
        return rpos;
    }*/

    @Override
    public Vector2 getOriginOffset() {
        return new Vector2(0, -getModelSize().y/2);
    }

    @Override
    public void onCollision(PhysicsObject other) {
        if (other instanceof Grapple) {
            busy = false;
        }
    }
    
    @Override
    public void update(float delta) {
        currentAnimation = getAirborneAnimation();
        animationTime += delta;
        if (swingAmount == 1) {
            setFspeed(new Vector2(50, 0));
        } else if (swingAmount == -1) {
            setFspeed(new Vector2(-50, 0));
        } else {
            setFspeed(new Vector2(0, 0));
        }
        updatePos(delta);
        if (grapple != null) grapple.update(delta);
    }
    
    @Override
    public void render(GFXManager gfx) {
        if (!busy) { //determine animations as usual if not busy
            sprite = new Sprite(currentAnimation.getKeyFrame(animationTime, true));
        }
        if (!facingRight) {
            sprite.flip(true, false);
        }

        gfx.drawModel(sprite, getPos(), getModelScale());
        if (grapple != null) grapple.render(gfx);
    }

    protected void initAnimations() {
        special = getSpecial();
        attack = getAttack();
    }

    public void setSwing(float amount) {
        swingAmount = amount;
    }

    public void doGrapple(Vector2 target) {
        grapple = new Grapple(getPos(), target.sub(getPos()).scl(1, -1));
        // busy = true;
    }

    public void stopGrapple() {
        grapple = null;
    }

    public void doAbility() {

    }
    
    public Player(Vector2 _pos) {
        super(_pos);
    }
}