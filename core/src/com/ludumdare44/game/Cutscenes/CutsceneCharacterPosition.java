package com.ludumdare44.game.Cutscenes;

import com.badlogic.gdx.math.Vector2;

public enum CutsceneCharacterPosition {
    LEFT(new Vector2(0.2f, 0.5f)),
    RIGHT(new Vector2(0.8f, 0.5f)),
    TOP(new Vector2(0.5f, 0.75f));

    public final Vector2 relativeScreenPosition;

    CutsceneCharacterPosition(Vector2 relativeScreenPosition) {
        this.relativeScreenPosition = relativeScreenPosition;
    }
}
