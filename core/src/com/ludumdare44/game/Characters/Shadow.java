package com.ludumdare44.game.Characters;

import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.GFX.IRenderableObject;

public class Shadow implements IRenderableObject {
    Vector2 pos;
    Vector2 size;
    IRenderableObject owner;
    private int zLevel;

    @Override
    public boolean alive() {
        return owner != null && owner.alive();
    }

    @Override
    public boolean stagnant() {
        return owner != null && owner.stagnant();
    }

    @Override
    public boolean visible() {
        return owner != null && owner.visible();
    }

    @Override
    public Vector2 getOriginOffset() {
        return Vector2.Zero;
    }

    @Override
    public Vector2 getPos() {
        return owner.getPos();
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

    public Shadow(IRenderableObject _owner, int backgroundZ) {
        owner = _owner;
        zLevel = backgroundZ;
    }
}
