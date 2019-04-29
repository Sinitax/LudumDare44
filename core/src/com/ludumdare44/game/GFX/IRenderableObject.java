package com.ludumdare44.game.GFX;

import com.badlogic.gdx.math.Vector2;

public interface IRenderableObject extends IRenderable {
    Vector2 getOriginOffset();
    Vector2 getPos();
    Vector2 getModelSize();

    int getZ();
    void setZ(int z);

    void update(float delta);

    boolean isVisible(); // not isVisible -> do not render
    boolean isDestroyed(); // delete
}