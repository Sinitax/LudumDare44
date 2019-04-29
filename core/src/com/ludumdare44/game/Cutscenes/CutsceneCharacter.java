package com.ludumdare44.game.Cutscenes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Constants;
import com.ludumdare44.game.GFX.GFXManager;

public class CutsceneCharacter {

    protected final TextureRegion texture;
    protected final String name;

    protected float alpha = 1;
    protected Vector2 offset = new Vector2();
    protected CutsceneCharacterPosition position;

    public CutsceneCharacter(TextureRegion texture, String name) {
        this.texture = texture;
        this.name = name;
    }

    public CutsceneCharacter(Texture texture, String name) {
        this(new TextureRegion(texture), name);
    }

    public void render(float delta, GFXManager gfxManager) {
        final int pixelRatio = Constants.PIXEL_SCALE;

        Vector2 screenPosition = new Vector2(
                gfxManager.screenSize.x*this.position.relativeScreenPosition.x,
                gfxManager.screenSize.y*this.position.relativeScreenPosition.y);

        float scaleWidth = texture.getRegionWidth()*pixelRatio;
        float scaledHeight = texture.getRegionHeight()*pixelRatio;

        if(alpha < 1) {
            gfxManager.batch.setColor(1, 1, 1, alpha);
        }

        gfxManager.batch.draw(texture,
                screenPosition.x - scaleWidth/2 + offset.x*pixelRatio,
                screenPosition.y - scaledHeight/2 + offset.y*pixelRatio,
                0, 0, scaleWidth, scaledHeight, 1, 1, 0);

        if(alpha < 1) {
            gfxManager.batch.setColor(1, 1, 1, 1);
        }
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public Vector2 getOffset() {
        return offset;
    }

    public void setOffset(Vector2 offset) {
        this.offset = offset;
    }

    public void setPosition(CutsceneCharacterPosition position) {
        this.position = position;
    }
}
