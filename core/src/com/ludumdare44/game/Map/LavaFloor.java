package com.ludumdare44.game.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.GFX.IRenderable;
import com.ludumdare44.game.UI.CameraManager;

import java.util.ArrayList;

public class LavaFloor implements IRenderable {
    Sprite lavaTexture = new Sprite(new Texture("assets/lavafloor.png"));
    private int tileCols;
    private CameraManager cameraManager;
    private Vector2 lastCameraPos;
    private Vector2 offset = new Vector2(0, 0);
    private float tileWidth, tileHeight;

    public void render(GFXManager gfx) {
        Vector2 cameraPos = new Vector2(cameraManager.getPos());
        if (lastCameraPos == null) {
            lastCameraPos = cameraPos;
        }
        cameraPos.sub(lastCameraPos);
        offset.add(cameraPos.scl(-1));
        offset.x = offset.x % tileWidth;

        float startX = cameraManager.getPos().x - cameraManager.getScreenSize().x / 2 - tileWidth;
        float startY = cameraManager.getPos().y - cameraManager.getScreenSize().y / 2;

        for (int i = 0; i < tileCols; i++) {
            gfx.batch.draw(lavaTexture, startX + offset.x + i * tileWidth + tileWidth, startY - offset.y - tileWidth / 6, tileWidth, tileHeight);
        }

        lastCameraPos = cameraManager.getPos();
    }

    public LavaFloor(CameraManager _cameraManager) {
        cameraManager = _cameraManager;
        tileCols = (int) (cameraManager.getScreenSize().x / lavaTexture.getWidth()) + 2;
        tileWidth = lavaTexture.getWidth();
        tileHeight = lavaTexture.getHeight();
    }
}