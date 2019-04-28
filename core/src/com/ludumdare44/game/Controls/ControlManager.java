package com.ludumdare44.game.Controls;

import com.badlogic.gdx.InputAdapter;

public class ControlManager extends InputAdapter {
    private static int keysCount = 256;
    private boolean[] lkeyBuffer = new boolean[keysCount];
    private boolean[] keyBuffer = new boolean[keysCount];
    private boolean[] ckeyBuffer = new boolean[keysCount];

    @Override
    public boolean keyUp(int keycode) {
        ckeyBuffer[keycode] = false;
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        ckeyBuffer[keycode] = true;
        return true;
    }

    public boolean justPressed(int keycode) {
        return keyBuffer[keycode] && !lkeyBuffer[keycode];
    }

    public boolean justReleased(int keycode) {
        return !keyBuffer[keycode] && lkeyBuffer[keycode];
    }

    public boolean isPressed(int keycode) {
        return keyBuffer[keycode];
    }

    public void update() {
        for (int i = 0; i < keyBuffer.length; i++) {
            lkeyBuffer[i] = keyBuffer[i];
            keyBuffer[i] = ckeyBuffer[i];
        }
    }

    public ControlManager() {
    }
}