package com.ludumdare44.game.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.GFX.IRenderable;
import com.ludumdare44.game.UI.CameraManager;

import java.util.ArrayList;

public class EndlessBackground implements IRenderable {
    private CameraManager cameraManager;
    int tileWidth, tileHeight;
    int tileCols, tileRows;
    Vector2 msize;
    float[] probabilities = new float[] {0.5f, 0.2f};
    TextureRegion[] tileMap;
    ArrayList<ArrayList<Integer>> indMap;

    Vector2 lastCameraPos, offset;

    public void update() {

    }

    public void render(GFXManager gfx) {
        Vector2 cameraPos = new Vector2(cameraManager.getPos());
        if (lastCameraPos == null) {
            lastCameraPos = cameraPos;
        }
        cameraPos.sub(lastCameraPos);
        offset.add(cameraPos.scl(0.4f));
        Vector2 stile = new Vector2((int) offset.x / tileWidth, (int) offset.y / tileHeight);

        if (stile.x == 1) {
            indMap.remove(0);
            indMap.add(new ArrayList<>());
            for (int i = 0; i < msize.x; i++) {
                indMap.get(indMap.size()-1).add(randomRegion());
            }
            offset.x -= tileWidth;
        } else if (stile.x == -1) {
            indMap.remove(indMap.size()-1);
            indMap.add(0, new ArrayList<>());
            for (int i = 0; i < msize.x; i++) {
                indMap.get(0).add(randomRegion());
            }
            offset.x += tileWidth;
        }
        if (stile.y == 1) {
            for (int i = 0; i < msize.x; i++) {
                indMap.get(i).remove(0);
                indMap.get(i).add(randomRegion());
            }
            offset.y += tileHeight;
        } else if (stile.y == -1) {
            for (int i = 0; i < msize.x; i++) {
                indMap.get(i).remove(indMap.get(i).size()-1);
                indMap.get(i).add(0, randomRegion());
            }
            offset.y -= tileHeight;
        }

        float startX = cameraManager.getPos().x - cameraManager.getScreenSize().x / 2 - tileWidth;
        float startY = cameraManager.getPos().y - cameraManager.getScreenSize().y / 2 - tileWidth;
        for (int i = 0; i < tileCols; i++) {
            for (int j = 0; j < tileRows; j++) {
                gfx.batch.draw(tileMap[indMap.get(i).get(j)], startX - offset.x + i * tileWidth, startY - offset.y + j * tileHeight);
            }
        }
        lastCameraPos = cameraManager.getPos();
    }

    private int randomRegion() {
        double rand = Math.random();
        for (int i = 0; i < probabilities.length && i < tileMap.length; i++) {
            if (rand < probabilities[i]) {
                return i;
            } else rand -= probabilities[i];
        }
        return 0;
    }

    public void generate() {
        indMap = new ArrayList<>();
        for (int i = 0; i < msize.x; i++) {
            indMap.add(new ArrayList<>());
            for (int j = 0; j < msize.y; j++) {
                indMap.get(i).add(randomRegion());
            }
        }
    }

    public EndlessBackground(CameraManager _cameraManager, TextureRegion[] _tiles) {
        cameraManager = _cameraManager;
        tileMap = _tiles;
        tileWidth = tileMap[0].getRegionWidth();
        tileHeight = tileMap[0].getRegionHeight();
        tileCols = (int) cameraManager.getScreenSize().x / tileWidth + 2;
        tileRows = (int) cameraManager.getScreenSize().y / tileHeight + 2;
        msize = new Vector2(new Vector2(tileCols, tileRows));
        offset = new Vector2(0, 0);
        generate();
    }
}
