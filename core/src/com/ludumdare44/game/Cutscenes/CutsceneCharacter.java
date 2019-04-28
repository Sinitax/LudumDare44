package com.ludumdare44.game.Cutscenes;

import com.badlogic.gdx.graphics.Texture;

public class CutsceneCharacter {

    protected final Texture texture;
    protected final String name;

    public CutsceneCharacter(Texture texture, String name) {
        this.texture = texture;
        this.name = name;
    }
}
