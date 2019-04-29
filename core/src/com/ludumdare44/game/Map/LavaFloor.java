package com.ludumdare44.game.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.GFX.IRenderable;
import com.ludumdare44.game.GFX.CameraManager;

import java.util.ArrayList;

public class LavaFloor implements IRenderable {
    private Texture lavaTexture = new Texture("assets/lavafloor.png");
    private Animation<TextureRegion> lavaAnimation = new Animation<TextureRegion>(0.5f, TextureRegion.split(lavaTexture, lavaTexture.getWidth()/3, lavaTexture.getHeight())[0]);
    private int tileCols;
    private CameraManager cameraManager;
    private Vector2 lastCameraPos;
    private Vector2 offset = new Vector2(0, 0);
    private float tileWidth, tileHeight;
    private ArrayList<Float> animationIndexes;
    private float delta;

    public void update(float dt) {
        delta = dt;
    }

    public void render(GFXManager gfx) {
        Vector2 cameraPos = new Vector2(cameraManager.getPos());
        if (lastCameraPos == null) {
            lastCameraPos = cameraPos;
        }
        cameraPos.sub(lastCameraPos);
        offset.add(cameraPos.scl(-1));
        int removedTiles = (int) (offset.x / tileWidth);

        // update tiles
        if (removedTiles < 0) {
            for (int i = 0; i < -removedTiles; i++) {
                animationIndexes.remove(0);
                animationIndexes.add(animationIndexes.size() - 1, randomFrameTime());
            }
        } else {
            for (int i = 0; i < removedTiles; i++) {
                animationIndexes.remove(animationIndexes.size() - 1);
                animationIndexes.add(0, randomFrameTime());
            }
        }

        offset.x = offset.x % tileWidth;

        float startX = cameraManager.getPos().x - cameraManager.getScreenSize().x / 2 - tileWidth;
        float startY = cameraManager.getPos().y - cameraManager.getScreenSize().y / 2;

        for (int i = 0; i < tileCols; i++) {
            animationIndexes.set(i, (animationIndexes.get(i) + delta) % lavaAnimation.getAnimationDuration());
            gfx.batch.draw(lavaAnimation.getKeyFrame(animationIndexes.get(i)), startX + offset.x + i * tileWidth, startY - offset.y - tileWidth / 6, tileWidth, tileHeight);
        }

        lastCameraPos = cameraManager.getPos();
    }

    private float randomFrameTime() {
        return (float) Math.random() * lavaAnimation.getFrameDuration() * lavaAnimation.getKeyFrames().length;
    }

    public LavaFloor(CameraManager _cameraManager) {
        cameraManager = _cameraManager;
        TextureRegion temptile = lavaAnimation.getKeyFrames()[0];
        tileWidth = temptile.getRegionWidth();
        tileHeight = temptile.getRegionHeight();
        tileCols = (int) (cameraManager.getScreenSize().x / tileWidth) + 2;
        animationIndexes = new ArrayList<>(tileCols);
        for (int i = 0; i < tileCols; i++) {
            animationIndexes.add(randomFrameTime());
        }
    }
}