package com.ludumdare44.game.Cutscenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ludumdare44.game.GFX.GFXManager;

public class ScreenFader {

    protected enum FadeType {
        FADE_IN, FADE_OUT, FLASH
    }

    protected Color color;
    protected float fadeTime;

    protected final ShapeRenderer shapeRenderer = new ShapeRenderer();
    protected FadeType fadeType;
    protected float fadeProgress = 0;


    public ScreenFader(Color color, float fadeTime) {
        this.color = color;
        this.fadeTime = fadeTime;
    }

    public ScreenFader() {
        this(Color.BLACK, 2);
    }

    public ScreenFader setFadeTime(float fadeTime) {
        this.fadeTime = fadeTime;
        return this;
    }

    public ScreenFader setColor(Color color) {
        this.color = color;
        return this;
    }

    public void fadeIn() {
        fadeType = FadeType.FADE_IN;
        fadeProgress = 0;
    }

    public void fadeOut() {
        fadeType = FadeType.FADE_OUT;
        fadeProgress = 0;
    }

    public void flash() {
        fadeType = FadeType.FLASH;
        fadeProgress = 0;
    }

    public void render(GFXManager gfxManager, float delta) {
        if(fadeProgress >= fadeTime)
            return;

        fadeProgress += delta;

        switch(fadeType) {
            case FADE_IN:
            case FADE_OUT:
                float alpha = fadeProgress / fadeTime;
                if(fadeType == FadeType.FADE_IN)
                    alpha = 1 - alpha;

                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(color.r, color.g, color.b, alpha);
                shapeRenderer.rect(0, 0, gfxManager.screenSize.x, gfxManager.screenSize.y);
                shapeRenderer.end();

                Gdx.gl.glDisable(GL20.GL_BLEND);
                break;

            // TODO: Implement flash
            default:
                break;
        }
    }
}
