package com.ludumdare44.game.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.GFX.IRenderable;
import com.ludumdare44.game.GFX.IRenderableObject;

public class FakePlatform implements IRenderableObject {
    private int x, y, tileRows, tileCols, tileSize;
    TextureRegion[] tiles;
    private int zLevel = 0;

    @Override
    public Vector2 getOriginOffset() {
        return new Vector2(0, 0);
    }

    @Override
    public Vector2 getModelSize() {
        return new Vector2(tileCols * tileSize, tileRows * tileSize);
    }

    @Override
    public int getZ() {
        return 0;
    }

    @Override
    public Vector2 getPos() {
        return new Vector2(x, y);
    }

    @Override
    public void setZ(int z) {
        zLevel = z;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean isDestroyed() {
        return false;
    }

    private int[][] around = new int[][] {
            {0, 1},
            {-1, 0},
            {1, 0},
            {0, -1}
    };

    private int getBitMask(int x, int y) {
        int bitmask = 0;
        for (int i = 0; i < around.length; i++) {
            if (y + around[i][1] >= tileRows || y + around[i][1] < 0 || x + around[i][0] >= tileCols || x + around[i][0] < 0) continue;
            bitmask += Math.pow(2, i);
        }
        return  bitmask;
    }

    public void render(GFXManager gfx) {
        float startx = x - tileCols / 2.f * tileSize;
        float starty = y - tileRows / 2.f * tileSize;

        for (int i = 0; i < tileRows; i++) {
            for (int j = 0; j < tileCols; j++) {
                gfx.batch.draw(tiles[getBitMask(j, i)], startx + j * tileSize, starty + i * tileSize, tileSize, tileSize);
            }
        }
    }

    public FakePlatform(Vector2 pos, int width, int height, TextureRegion[] _tiles) {
        x = (int) pos.x;
        y = (int) pos.y;
        tileCols = width;
        tileRows = height;
        tiles = _tiles;
        tileSize = tiles[0].getRegionHeight() * 2;
    }
}
