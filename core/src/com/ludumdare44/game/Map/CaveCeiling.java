package com.ludumdare44.game.Map;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.GFX.IRenderable;
import com.ludumdare44.game.UI.CameraManager;

import java.awt.*;
import java.util.Random;

public class CaveCeiling implements IRenderable {
    private CameraManager cameraManager;
    private TextureRegion[] tiles;
    private boolean[][] ceilingTiles;

    private int tileBufferSize = 3;
    private int tileCols;
    private int tileSize;
    private int tileRows = 5;
    private float startX;
    private float lastCameraX = 0;

    private int[][] around = new int[][] {
            {0, -1},
            {-1, 0},
            {1, 0},
            {0, 1}
    };

    private Random random;

    private int getBitMask(int x, int y) {
        int bitmask = 0;
        for (int i = 0; i < around.length; i++) {
            if (y + around[i][1] >= tileRows || x + around[i][0] >= tileCols || x + around[i][0] < 0) continue;
            if (y + around[i][1] < 0 || ceilingTiles[y + around[i][1]][x + around[i][0]]) {
                bitmask += Math.pow(2, i);
            }
        }
        return  bitmask;
    }

    public void render(GFXManager gfx) {
        startX -= (cameraManager.getPos().x - (cameraManager.getScreenSize().x/2) - lastCameraX);

        if (startX < - (tileBufferSize + 1) * tileSize) {
            int removedTileCount = - (int) (startX - tileBufferSize * tileSize) / tileSize;
            for (int i = removedTileCount; i < tileCols; i++) {
                for (int j = tileRows - 1; j >= 0; j--) { // overwrite
                    ceilingTiles[j][i - removedTileCount] = ceilingTiles[j][i];
                }
            }
            for (int i = tileCols - removedTileCount; i < tileCols; i++) {
                int height = random.nextInt(tileRows);
                for (int j = 0; j < 1 + height; i++) {
                    ceilingTiles[j][i] = true;
                }
            }
            startX += removedTileCount * tileSize;
        } else if (startX > - tileBufferSize * tileSize) {
            int removedTileCount = - (int) (startX + tileBufferSize * tileSize) / tileSize;
            for (int i = tileCols - removedTileCount - 1; i >= 0; i++) {
                for (int j = tileRows - 1; j >= 0; j--) { // overwrite
                    ceilingTiles[j][i + removedTileCount] = ceilingTiles[j][i];
                }
            }
            for (int i = 0; i < removedTileCount; i++) {
                int height = random.nextInt(tileRows);
                for (int j = 0; j < 1 + height; i++) {
                    ceilingTiles[j][i] = true;
                }
            }
            startX -= removedTileCount * tileSize;
        }

        for (int i = 0; i < tileRows; i++) {
            for (int j = tileBufferSize - 1; j < tileCols - tileBufferSize + 1; j++) {
                if (ceilingTiles[i][j]) {
                    float tempX = startX + j * tileSize;
                    float tempY = cameraManager.getScreenSize().y - (i+1) * tileSize;
                    gfx.batch.draw(tiles[getBitMask(j, i)], tempX, tempY);
                    gfx.drawDebug(new Vector2(tempX + tileSize/2, tempY + tileSize/2));
                }
            }
        }

        lastCameraX = cameraManager.getPos().x - cameraManager.getScreenSize().x/2;
    }

    private void genTileCol(int x1, int x2) {

    }

    public CaveCeiling(CameraManager _cameraManager, TextureRegion[] _tiles) {
        cameraManager = _cameraManager;
        tiles = _tiles;
        tileSize = tiles[0].getRegionWidth();
        tileCols =  ((int) cameraManager.getScreenSize().x / tiles[0].getRegionHeight()) + 1 + tileBufferSize * 2;
        random = new Random();

        ceilingTiles = new boolean[tileRows][tileCols];
        int height = random.nextInt(tileRows-3)+3;
        System.out.println(height);
        System.out.println(tileRows);
        for (int i = 0; i < tileCols; i++) {
            for (int j = 0; j < height; j++) {
                ceilingTiles[j][i] = true;
            }

            if (random.nextInt(tileRows) > tileRows - 1 - height) {
                height -= random.nextInt(Math.max(1, Math.min(2, height)));
            } else {
                height += random.nextInt(Math.max(1, Math.min(2, tileRows - height)));
            }
            if (height < 1) height = 1;
        }

        startX = - tileBufferSize * tileSize;
    }
}
