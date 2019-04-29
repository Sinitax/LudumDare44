package com.ludumdare44.game.Characters;

import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.Physics.PhysicsObject;
import com.ludumdare44.game.Physics.VisualPhysObject;

public class Shadow extends VisualPhysObject {
    Vector2 pos;
    Vector2 size;
    VisualPhysObject owner;
    private int zLevel;

    @Override
    public boolean isDestroyed() {
        return owner != null && owner.isDestroyed();
    }

    @Override
    public boolean isStagnant() {
        return owner != null && owner.isStagnant();
    }

    @Override
    public boolean isVisible() {
        return owner != null && owner.isVisible();
    }

    @Override
    public Vector2 getOriginOffset() {
        return Vector2.Zero;
    }

    public Vector2 getSize() {
        return owner.getModelSize();
    }

    @Override
    public int getZ() {
        return zLevel;
    }

    @Override
    public void setZ(int z) {
        zLevel = z;
    }

    @Override
    public Vector2 getModelSize() {
        return new Vector2(owner.getModelSize().x, 20);
    }

    public void update(float delta) {
    }

    public void render(GFXManager gfx) {
        gfx.drawShadow(pos.x, pos.y - size.y * 0.5f, size.x, 10);
    }

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
        return new Vector2(0, 0);
    }

    @Override
    public void onCollision(PhysicsObject other, float delta) {

    }

    public Shadow(VisualPhysObject _owner, int backgroundZ) {
        super(_owner.getPos());
        owner = _owner;
        zLevel = backgroundZ;
        follow(owner);
    }
}
