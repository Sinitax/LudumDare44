package com.ludumdare44.game.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.Map.ObjectAdder;
import com.ludumdare44.game.Physics.Grapple;
import com.ludumdare44.game.Physics.Obstacle;
import com.ludumdare44.game.Physics.PhysicsObject;
import com.ludumdare44.game.Physics.VisualPhysObject;
import jdk.nashorn.internal.ir.GetSplitState;

public abstract class Player extends VisualPhysObject {
    private com.ludumdare44.game.Characters.Ability special;
    public abstract com.ludumdare44.game.Characters.Ability getSpecial();
    public com.ludumdare44.game.Characters.Ability attack;
    public abstract com.ludumdare44.game.Characters.Ability getAttack();

    private ObjectAdder objectAdder;

    protected Animation<TextureRegion> currentAnimation;
    public Sprite sprite;

    private float swingAmount  = 0;

    private boolean facingRight;

    private boolean grappling = false;
    public boolean isGrappling() { return grappling;}

    private boolean reel = false;

    private Grapple grapple;

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
    public Vector2 getHitbox() { return new Vector2(26, 70); }

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

    @Override
    public Vector2 getOriginOffset() {
        return new Vector2(0, -getModelSize().y/2);
    }

    @Override
    public void onCollision(PhysicsObject other) {
        if (other instanceof Obstacle) {
            stopGrapple();
        }
    }
    
    @Override
    public void update(float delta) {
        animationTime += delta;
        updateSpeed(delta);
        if (grappling && grapple.isGrappled()) {
            Vector2 trpos = grapple.getPos().sub(getPos());
            Vector2 rpos = new Vector2(trpos).rotate90(-1);
            Vector2 nspeed = rpos.scl(getSpeed().dot(new Vector2(rpos.nor())));

            if (reel) {
                nspeed.add(trpos.nor().scl(6000.f * delta));
            } else {
                Vector2 nspeed2 = trpos.scl(getSpeed().dot(new Vector2(trpos.nor())));
                if (nspeed2.x > 0 && nspeed2.y > 0) {
                    nspeed.y = nspeed.y + nspeed2.y;
                    nspeed.x = nspeed.x + nspeed2.x;
                }
            }
            setSpeed(nspeed);
            setFspeedAbs(nspeed);
        }

        updatePos(delta);
        if (getPos().y + getModelSize().y * getModelScale().y * 0.5 < 0) dead = true;

        facingRight = (getSpeed().x > 0 && !grappling || grappling && grapple.getPos().sub(getPos()).x > 0);
    }
    
    @Override
    public void render(GFXManager gfx) {
        currentAnimation = getAirborneAnimation();
        if (!busy) { //determine animations as usual if not busy
            sprite = new Sprite(currentAnimation.getKeyFrame(animationTime, true));
        }
        if (!facingRight) {
            sprite.flip(true, false);
        }

        gfx.drawModel(sprite, getPos(), getModelScale());
    }

    protected void initAnimations() {
        special = getSpecial();
        attack = getAttack();
    }

    public void setSwing(float amount) {
        swingAmount = amount;
    }

    public void doGrapple(Vector2 target) {
        grapple = new Grapple(this, target);
        grappling = true;
        objectAdder.addVisObject(grapple);
        // busy = true;
    }

    public void stopGrapple() {
        if (isGrappling()) {
            grapple.kill();
            grapple = null;
            grappling = false;
        }
    }

    public void doReel() {
        reel = true;
    }

    public void stopReel() {
        reel = false;
    }

    public void doAbility() {

    }
    
    public Player(Vector2 _pos, ObjectAdder _objectAdder) {
        super(_pos);
        objectAdder = _objectAdder;
        this.weight = 10;
    }
}