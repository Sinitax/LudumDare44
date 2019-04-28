package com.ludumdare44.game.Cutscenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Cutscenes.callbacks.ICutsceneCompleteListener;
import com.ludumdare44.game.GFX.GFXManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CutsceneScreen implements Screen {

    protected GFXManager gfxManager;

    protected final Color backgroundColor;
    protected final Queue<CutsceneEvent> cutsceneEvents = new LinkedList<>();
    protected final List<CutsceneCharacter> cutsceneCharacters = new ArrayList<>();
    protected final List<ICutsceneCompleteListener> completeListeners = new ArrayList<>();

    public CutsceneScreen(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public CutsceneScreen addCutsceneEvent(CutsceneEvent event) {
        cutsceneEvents.add(event);
        return this;
    }

    public CutsceneScreen onComplete(ICutsceneCompleteListener completeListener) {
        completeListeners.add(completeListener);
        return this;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(cutsceneEvents.isEmpty()) {
            for(ICutsceneCompleteListener listener : completeListeners)
                listener.cutsceneCompleted();
            completeListeners.clear();
            return;
        }

        CutsceneEvent currentEvent = cutsceneEvents.peek();
        if(currentEvent.processEvent(delta, this)) {
            cutsceneEvents.remove();
            return;
        }
        System.out.println(currentEvent.getClass());

        currentEvent.preRender(delta, this, gfxManager);

        // Render cutscene

        currentEvent.postRender(delta, this, gfxManager);
    }

    @Override
    public void resize(int width, int height) {
        this.gfxManager = new GFXManager(new Vector2(width, height));
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
