package com.ludumdare44.game.Cutscenes;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;

public class CutsceneCharacter {

    protected final Sprite sprite;
    protected final String name;

    protected CutsceneCharacterPosition position;

    public CutsceneCharacter(Sprite sprite, String name) {
        this.sprite = sprite;
        this.name = name;
    }

    public void render(float delta, GFXManager gfxManager) {
        final int pixelRatio = 3;

        Vector2 screenPosition = new Vector2(
                gfxManager.screenSize.x*this.position.relativeScreenPosition.x,
                gfxManager.screenSize.y*this.position.relativeScreenPosition.y);

        float scaleWidth = sprite.getWidth()*pixelRatio;
        float scaledHeight = sprite.getHeight()*pixelRatio;

        gfxManager.batch.draw(sprite,
                screenPosition.x - scaleWidth/2, screenPosition.y - scaledHeight/2,
                scaleWidth, scaledHeight);
    }

    public void setPosition(CutsceneCharacterPosition position) {
        this.position = position;
    }
}
