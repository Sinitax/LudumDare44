package com.ludumdare44.game.Physics;

import com.badlogic.gdx.math.Vector2;

public class Obstacle extends PhysicsObject {
    private int width, height;

    @Override
    public int getFspeedMax() {
        return 0;
    }

    @Override
    public int getAccelMax() {
        return 0;
    }

    @Override
    public int getDecelMax() {
        return 0;
    }

    @Override
    public Vector2 getHitboxOffset() {
        return new Vector2(0, 0);
    }

    @Override
    public Vector2 getHitbox() {
        return new Vector2(width, height);
    }

    @Override
    public void onCollision(PhysicsObject other) {}

    public void update(int x, int y, int _width, int _height) {
        width = _width;
        height = _height;
        setPos(new Vector2(x, y));
    }

    public Obstacle(int _x, int _y, int _width, int _height) {
        super(new Vector2(_x, _y));
        width = _width;
        height = _height;
    }
}
