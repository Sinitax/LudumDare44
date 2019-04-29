package com.ludumdare44.game.Cutscenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.ludumdare44.game.Cutscenes.callbacks.IFadeCompleteListener;
import com.ludumdare44.game.GFX.GFXManager;

import java.util.ArrayList;
import java.util.List;

public class ScreenFader {

    protected enum FadeType {
        FADE_IN, FADE_OUT, FLASH
    }

    protected Color color;
    protected float fadeTime;

    protected final List<IFadeCompleteListener> completeListeners = new ArrayList<>();
    protected final ShapeRenderer shapeRenderer = new ShapeRenderer();
    protected FadeType fadeType;
    protected float fadeProgress = 0;
    protected boolean completed = false;


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

    public ScreenFader onComplete(IFadeCompleteListener completeListener) {
        completeListeners.add(completeListener);
        return this;
    }

    public ScreenFader fadeIn() {
        fadeType = FadeType.FADE_IN;
        fadeProgress = 0;
        completed = false;
        return this;
    }

    public ScreenFader fadeOut() {
        fadeType = FadeType.FADE_OUT;
        fadeProgress = 0;
        completed = false;
        return this;
    }

    public ScreenFader flash() {
        fadeType = FadeType.FLASH;
        fadeProgress = 0;
        completed = false;
        return this;
    }

    public void render(GFXManager gfxManager, float delta) {
        if(completed) {
            return;
        }

        if(fadeProgress >= fadeTime) {
            for (IFadeCompleteListener listener : completeListeners)
                listener.fadeCompleted();
            completeListeners.clear();
            completed = true;
        }

        fadeProgress += delta;

        switch(fadeType) {
            case FADE_IN:
            case FADE_OUT:
                float alpha = fadeProgress / fadeTime;
                if(fadeType == FadeType.FADE_IN)
                    alpha = 1 - alpha;
                alpha = MathUtils.clamp(alpha, 0, 1);

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
