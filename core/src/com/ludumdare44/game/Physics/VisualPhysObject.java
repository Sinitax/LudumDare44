package com.ludumdare44.game.Physics;

import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.IRenderableObject;

public abstract class VisualPhysObject extends PhysicsObject implements IRenderableObject {
    public static float objectDepth = 16;
    private int zLevel = -1;
    public abstract Vector2 getHitbox();
    public abstract Vector2 getHitboxOffset();
    
    public abstract void onCollision(VisualPhysObject other);

    @Override
    public int getZ() {
        return zLevel;
    }

    @Override
    public void setZ(int z) {
        zLevel = z;
    }

    public VisualPhysObject(Vector2 pos) {
        super(pos);
    }
}
