package com.ludumdare44.game.Physics;

import com.badlogic.gdx.math.Vector2;

public abstract class PhysicsObject {
    private PhysicsObject followObject;
    private boolean following = false;
    private Vector2 speed = new Vector2(0,0);
    public final Vector2 getSpeed() { return speed; }
    public final void setSpeed(Vector2 _speed) {
        speed.x = _speed.x;
        speed.y = _speed.y;
    }
    
    private Vector2 fspeed = new Vector2(0,0);
    public final Vector2 getFspeed() { return fspeed; }
    public final void setFspeed(Vector2 _dir) {
        _dir = _dir.nor();
        fspeed.x =  _dir.x * getFspeedMax() * speedscale;
        fspeed.y =  _dir.y * getFspeedMax() * speedscale;
    }
    public final void setFspeedAbs(Vector2 _fspeed) {
        fspeed = _fspeed;
        if (fspeed.x > getFspeedMax()) {
            fspeed.x = getFspeedMax();
        } else if (fspeed.x < getFspeedMax()) {
            fspeed.x = -getFspeedMax();
        }
        if (fspeed.y > getFspeedMax()) {
            fspeed.y = getFspeedMax();
        } else if (fspeed.y < getFspeedMax()) {
            fspeed.y = -getFspeedMax();
        }
    }
    
    private Vector2 accel = new Vector2(0, 0);
    private Vector2 decel = new Vector2(0, 0);
    
    public float speedscale = 1;
    
    public abstract int getAccelMax();
    public abstract int getDecelMax();
    public abstract int getFspeedMax();
    
    private Vector2 pos;
    public final void setPos(Vector2 _pos) {
        pos.x = _pos.x;
        pos.y = _pos.y;
    }
    public final Vector2 getPos() { return new Vector2(pos); }
    
    public float approachSpeed = 1;
    public void follow(PhysicsObject obj) {
        following = true;
        followObject = obj;
    }
    
    public void approach(Vector2 fpos, float approachSpeed) {
        if (approachSpeed < 0) {
            pos.x = fpos.x;
            pos.y = fpos.y;
        } else {
            fspeed.x = (fpos.x - getPos().x) * approachSpeed;
            fspeed.y = (fpos.y - getPos().y) * approachSpeed;
        }
    }
    
    public void updatePos(float delta) {
        if (following) {
            approach(followObject.getPos(), approachSpeed);
        }
        
        // accelerate to speed
        float scale = delta * speedscale;
        accel.x = (float) Math.cos(Math.toRadians(fspeed.angle())) * getAccelMax() * scale;
        accel.y = (float) Math.sin(Math.toRadians(fspeed.angle())) * getAccelMax() * scale;
        decel.x = (float) Math.cos(Math.toRadians(speed.angle())) * getDecelMax() * scale;
        decel.y = (float) Math.sin(Math.toRadians(speed.angle())) * getDecelMax() * scale;
        
        Vector2 nspeed = new Vector2(fspeed).add(speed);
        
        boolean accel_b;
        if  (speed.x != fspeed.x) {
            accel_b = (fspeed.x - speed.x > 0) == (nspeed.x > 0);
            if (accel_b) {
                if (getAccelMax() == -1) speed.x = fspeed.x;
                else speed.x += Math.abs(accel.x) * Math.signum(fspeed.x);
            }
            else {
                if (getDecelMax() == -1) speed.x = fspeed.x;
                else speed.x -= Math.abs(decel.x) * Math.signum(speed.x);
            }
            if ((((fspeed.x - speed.x > 0) != (nspeed.x > 0)) && accel_b) || (((fspeed.x - speed.x > 0) == (nspeed.x > 0)) && !accel_b)) speed.x = fspeed.x;
        }
        if  (speed.y != fspeed.y) {
            accel_b = (fspeed.y - speed.y > 0) == (nspeed.y > 0);
            if (accel_b) {
                if (getAccelMax() == -1) speed.y = fspeed.y;
                else speed.y += Math.abs(accel.y) * Math.signum(fspeed.y);
            }
            else {
                if (getDecelMax() == -1) speed.y = fspeed.y;
                else speed.y -= Math.abs(decel.y) * Math.signum(speed.y);
            }
            if ((((fspeed.y - speed.y > 0) != (nspeed.y > 0)) && accel_b) || (((fspeed.y - speed.y > 0) == (nspeed.y > 0)) && !accel_b)) speed.y = fspeed.y;
        }
        
        pos.x += speed.x * delta;
        pos.y += speed.y * delta;
    }

    public abstract Vector2 getHitbox();
    public abstract Vector2 getHitboxOffset();

    public abstract void onCollision(PhysicsObject other);

    public PhysicsObject(Vector2 _pos) {
        pos = _pos;
    }
}
