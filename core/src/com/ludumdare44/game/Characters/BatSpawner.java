package com.ludumdare44.game.Characters;

import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.CameraManager;
import com.ludumdare44.game.Map.ObjectAdder;

public class BatSpawner {
    private CameraManager cameraManager;
    private ObjectAdder objectAdder;
    private int rows;
    private int yStart;
    private Vector2 lastCameraPos, offset = new Vector2(0, 0);

    public void update(float delta) {
        if (lastCameraPos == null) lastCameraPos = cameraManager.getPos();
        offset.add(cameraManager.getPos().sub(lastCameraPos));
        if (offset.x > Bat.getSize().x * 3) {
            float nx = cameraManager.getPos().x + cameraManager.getScreenSize().x / 2 + 50 + Bat.getSize().x * (int) (Math.random() * 3);
            float ny = yStart + Bat.getSize().y * (int) (Math.random() * rows);
            objectAdder.addVisObject(new Bat(new Vector2(nx, ny)));
            offset.x = offset.x % (Bat.getSize().x * 3);
        }
        lastCameraPos = cameraManager.getPos();
    }

    public BatSpawner(CameraManager _cameraManager, ObjectAdder _objectAdder) {
        cameraManager = _cameraManager;
        objectAdder = _objectAdder;
        rows = (int) (cameraManager.getScreenSize().y / 2 / Bat.getSize().y);
        yStart = 150;
    }
}
