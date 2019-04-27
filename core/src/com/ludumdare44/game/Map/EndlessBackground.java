package com.ludumdare44.game.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.GFX.IRenderable;
import com.ludumdare44.game.UI.CameraManager;

import java.util.ArrayList;

public class EndlessBackground implements IRenderable {
    private CameraManager cameraManager;
    int tWidth, tHeight;
    Vector2 msize;
    Vector2 lstile;
    float[] probabilities = new float[] {0.5f, 0.2f};
    TextureRegion[] tileMap;
    ArrayList<ArrayList<Integer>> indMap;

    public void update() {

    }

    public void render(GFXManager gfx) {
        Vector2 sv = new Vector2(cameraManager.getPos()).sub(new Vector2(cameraManager.getScreenSize()).scl(0.5f));
        Vector2 stile = new Vector2((int) sv.x / tWidth, (int) sv.y / tHeight);
        if (sv.x < 0) stile.x--;
        if (sv.y < 0) stile.y--;
        Vector2 pos = new Vector2((int) stile.x * tWidth, (int) stile.y * tHeight);
        sv.add(cameraManager.getScreenSize());
        Vector2 screenSize = new Vector2((int) ((sv.x - pos.x) / tWidth) + 1, (int) ((sv.y - pos.y) / tHeight) + 1);
        lstile.sub(stile);
        if (lstile.x == -1) {
            indMap.remove(0);
            indMap.add(new ArrayList<>());
            for (int i = 0; i < msize.x; i++) {
                indMap.get(indMap.size()-1).add(randomRegion());
            }
        } else if (lstile.x == 1) {
            indMap.remove(indMap.size()-1);
            indMap.add(0, new ArrayList<>());
            for (int i = 0; i < msize.x; i++) {
                indMap.get(0).add(randomRegion());
            }
        }
        if (lstile.y == -1) {
            for (int i = 0; i < msize.x; i++) {
                indMap.get(i).remove(0);
                indMap.get(i).add(randomRegion());
            }
        } else if (lstile.y == 1) {
            for (int i = 0; i < msize.x; i++) {
                indMap.get(i).remove(indMap.get(i).size()-1);
                indMap.get(i).add(0, randomRegion());
            }
        }
        for (int i = 0; i < screenSize.x; i++) {
            for (int j = 0; j < screenSize.y; j++) {
                gfx.batch.draw(tileMap[indMap.get(i).get(j)], pos.x + i * tWidth, pos.y + j * tHeight);
            }
        }
        lstile = stile;
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
        tWidth = tileMap[0].getRegionWidth();
        tHeight = tileMap[0].getRegionHeight();
        msize = new Vector2(new Vector2((int) (cameraManager.getScreenSize().x / tWidth) + 2, (int) (cameraManager.getScreenSize().y / tHeight) + 2));
        lstile = new Vector2(0,0);
        generate();
    }
}
