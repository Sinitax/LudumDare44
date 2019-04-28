package com.ludumdare44.game.Controls;

import com.badlogic.gdx.InputAdapter;

public class ControlManager extends InputAdapter {
    private boolean[] lkeyBuffer = new boolean[156];
    private boolean[] keyBuffer = new boolean[156];
    private boolean[] ckeyBuffer = new boolean[156];

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