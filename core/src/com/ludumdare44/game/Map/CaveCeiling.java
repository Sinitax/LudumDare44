package com.ludumdare44.game.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.GFX.IRenderable;
import com.ludumdare44.game.Physics.Obstacle;
import com.ludumdare44.game.UI.CameraManager;

import java.util.ArrayList;
import java.util.Random;

public class CaveCeiling implements IRenderable {
    private CameraManager cameraManager;
    private TextureRegion[] tiles;
    private ArrayList<Integer> ceilingHeights;
    private Obstacle[] tileObjects;

    private int tileScale = 2;
    private int tileBufferSize = 3;
    private int tileCols;
    private int tileSize;
    private int tileRows = 6;
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
            if (y + around[i][1] < 0 || ceilingHeights.get(x + around[i][0]) > y + around[i][1]) {
                bitmask += Math.pow(2, i);
            }
        }
        return  bitmask;
    }

    private int heightFromNext(int height) {
        if (height == tileRows - 1) {
            height -= random.nextInt(2) + 1;
        } else if (height == 1) {
            height += random.nextInt(2) + 1;
        } else {
            if (random.nextInt(tileRows) > tileRows - height) {
                height -= random.nextInt(Math.min(2, height+1));
            } else {
                height += random.nextInt(Math.min(2, tileRows - height));
            }
        }
        if (height < 2) height = 2;
        return height;
    }

    public void render(GFXManager gfx) {
        startX -= cameraManager.getPos().x - lastCameraX;

        if (startX < - (tileBufferSize + 1) * tileSize) {
            int removedTileCount = - (int) (startX + tileBufferSize * tileSize) / tileSize;
            for (int i = 0; i < removedTileCount; i++) {
                ceilingHeights.remove(0);
                ceilingHeights.add(heightFromNext(ceilingHeights.get(ceilingHeights.size() - 1)));
            }
            startX += removedTileCount * tileSize;
        } else if (startX > - (tileBufferSize - 1) * tileSize) {
            int removedTileCount = (int) (startX + tileBufferSize * tileSize) / tileSize;
            for (int i = 0; i < removedTileCount; i++) {
                ceilingHeights.remove(ceilingHeights.size() - 1);
                ceilingHeights.add(0, heightFromNext(ceilingHeights.get(0)));
            }
            startX -= removedTileCount * tileSize;
        }

        for (int j = tileBufferSize - 1; j < tileCols - tileBufferSize + 1; j++) {
            int sx = (int) (cameraManager.getPos().x - cameraManager.getScreenSize().x / 2 + startX + j * tileSize);
            int sy = (int) (cameraManager.getPos().y + cameraManager.getScreenSize().y / 2);
            tileObjects[j].update(sx + tileSize/2, sy - ceilingHeights.get(j) * tileSize/2 + tileSize, tileSize, ceilingHeights.get(j) * tileSize);
            for (int i = 0; i < tileRows; i++) {
                if (i < ceilingHeights.get(j)) {
                    gfx.batch.draw(tiles[getBitMask(j, i)], sx, sy - i * tileSize, tileSize, tileSize);
                }
            }
        }

        lastCameraX = cameraManager.getPos().x;
    }

    public CaveCeiling(CameraManager _cameraManager, ObjectManager objectManager, TextureRegion[] _tiles) {
        cameraManager = _cameraManager;
        tiles = _tiles;
        tileSize = tiles[0].getRegionWidth() * tileScale;
        tileCols =  ((int) cameraManager.getScreenSize().x / tiles[0].getRegionHeight()) + 1 + tileBufferSize * 2;
        random = new Random();

        ceilingHeights = new ArrayList<>();
        int height = random.nextInt(tileRows);
        for (int i = 0; i < tileCols; i++) {
            height = heightFromNext(height);
            ceilingHeights.add(height);
        }

        startX = - tileBufferSize * tileSize;

        tileObjects = new Obstacle[ceilingHeights.size()];
        for (int i = 0; i < ceilingHeights.size(); i++)
        {
            tileObjects[i] = new Obstacle(0, 0, 0,0);
            objectManager.addObject(tileObjects[i]);
        }
    }
}
