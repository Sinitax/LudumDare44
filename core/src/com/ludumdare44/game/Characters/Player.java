package com.ludumdare44.game.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.Map.ObjectAdder;
import com.ludumdare44.game.Map.ObjectManager;
import com.ludumdare44.game.Physics.Grapple;
import com.ludumdare44.game.Physics.Obstacle;
import com.ludumdare44.game.Physics.PhysicsObject;
import com.ludumdare44.game.Physics.VisualPhysObject;

public abstract class Player extends VisualPhysObject {
    private Ability special;
    public abstract Ability getSpecial();
    private Ability attack;
    public abstract Ability getAttack();

    private ObjectAdder objectAdder;

    protected Sprite currentSprite;
    private Sprite sprite; // temp sprite

    public void setSprite(Sprite s) {
        currentSprite = s;
    }

    private boolean facingRight;

    private boolean grappling = false;
    public boolean isGrappling() { return grappling;}

    private boolean reel = false;

    private Grapple grapple;

    private boolean busy = false;
    public boolean isBusy() { return busy; }
    public void setBusy(boolean _busy) { busy = _busy;}

    private boolean dying = false;
    public boolean isDying() { return dying; }
    public void kill() { dying = true; }

    private boolean destroyed = false;
    public void destroy() {
        destroyed = true;
    }
    @Override
    public boolean isDestroyed() { return destroyed; }

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
    public int getFspeedMax() { return 800; }


    private boolean stagnant = false;
    @Override
    public boolean isStagnant() { return stagnant; }

    public void setStagnant(boolean v) { stagnant = v; }

    @Override
    public boolean isVisible() { return !destroyed; }

    public abstract Sprite getAirborneSprite();
    public abstract Sprite getLeftSwingSprite();
    public abstract Sprite getRightSwingSprite();
    public abstract Sprite getGrappleSprite();
    public abstract Sprite getDeathSprite();

    @Override
    public Vector2 getOriginOffset() {
        return new Vector2(0, -getModelSize().y/2);
    }

    @Override
    public void onCollision(PhysicsObject other, float delta) {
        if (other instanceof Obstacle) {
            ObjectManager.rectangleCollision(this, ObjectManager.toRectangle(other), delta);
            stopGrapple();
        }
    }
    
    @Override
    public void update(float delta) {
        if (getPos().y - getHitbox().y * 0.5 > 0) {
            updateSpeed(delta);
            if (grappling && grapple.isGrappled()) {
                Vector2 trpos = grapple.getPos().sub(getPos());
                Vector2 rpos = new Vector2(trpos).rotate90(-1);
                Vector2 nspeed = rpos.scl(getSpeed().dot(rpos.nor()));

                if (reel) {
                    nspeed.add(trpos.nor().scl(12000.f * delta));
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
        } else {
            Vector2 nspeed = new Vector2(0, -1000 * delta);
            setFspeedAbs(nspeed);
            setSpeed(nspeed);
        }

        updatePos(delta);

        if (getPos().y + getHitbox().y * 0.5 < 0) {
            destroy();
        }
    }

    @Override
    public void render(GFXManager gfx) {
        sprite = new Sprite(currentSprite);
        if (!dying) facingRight = (getSpeed().x >= 0 && !grappling || grappling && grapple.getPos().sub(getPos()).x > 0);
        if (!facingRight) {
            sprite.flip(true, false);
        }

        if (isGrappling() && grapple.isGrappled()) {
            float rotation = getPos().sub(grapple.getPos()).angle() + 90 - 360;
            if (rotation > 10) rotation = 10;
            else if (rotation < -10) rotation = -10;

            Matrix4 originalTransformMatrix = gfx.batch.getTransformMatrix().cpy();
            Matrix4 transformationMatrix = gfx.batch.getTransformMatrix()
                    .translate(getPos().x, getPos().y, 0)
                    .rotate(0, 0, 1, rotation)
                    .translate(-getPos().x, -getPos().y, 0);
            gfx.batch.setTransformMatrix(transformationMatrix);

            gfx.drawModel(sprite, getPos(), getModelScale());

            gfx.batch.setTransformMatrix(originalTransformMatrix);
        } else {
            gfx.drawModel(sprite, getPos(), getModelScale());
        }
    }

    protected void initAssets() {
        special = getSpecial();
        attack = getAttack();
        currentSprite = getAirborneSprite();
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

    public void doJump() {
        Vector2 jumpVec = new Vector2(200, 450);
        stopGrapple();
        int sign = 0;
        if (getSpeed().x >= 0) {
            sign = 1;
        } else {
            sign = -1;
        }
        Vector2 nspeed = getFspeed().add(jumpVec.scl(sign, 1));
        setFspeed(nspeed);
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
        this.weight = 85;
    }
}