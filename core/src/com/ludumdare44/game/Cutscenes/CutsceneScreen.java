package com.ludumdare44.game.Cutscenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.ludumdare44.game.Controls.ControlManager;
import com.ludumdare44.game.Cutscenes.callbacks.ICutsceneCompleteListener;
import com.ludumdare44.game.GFX.GFXManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CutsceneScreen implements Screen {

    protected ControlManager controlManager;
    protected GFXManager gfxManager;
    protected final ShapeRenderer shapeRenderer = new ShapeRenderer();

    protected final Color backgroundColor;
    protected final boolean cinematicBars;
    protected final Queue<CutsceneEvent> cutsceneEvents = new LinkedList<>();
    protected final List<CutsceneCharacter> cutsceneCharacters = new ArrayList<>();
    protected final List<ICutsceneCompleteListener> completeListeners = new ArrayList<>();

    protected float skipLabelAlpha = 0;

    public CutsceneScreen(Color backgroundColor, boolean cinematicBars) {
        controlManager = new ControlManager();
        Gdx.input.setInputProcessor(controlManager);

        this.backgroundColor = backgroundColor;
        this.cinematicBars = cinematicBars;
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
        // Input handling
        controlManager.update();
        if(controlManager.justPressed(Input.Keys.ESCAPE)) {
            if(skipLabelAlpha <= 0) {
                skipLabelAlpha = 2;
            } else {
                cutsceneEvents.clear();
            }
        }
        skipLabelAlpha -= delta;

        // Cutscene processing
        if(cutsceneEvents.isEmpty()) {
            for(ICutsceneCompleteListener listener : completeListeners)
                listener.cutsceneCompleted();
            completeListeners.clear();
            return;
        }

        CutsceneEvent currentEvent = cutsceneEvents.peek();
        if(currentEvent.processEvent(delta, this)) {
            cutsceneEvents.remove();
            render(delta);
            return;
        }

        // Cutscene rendering
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        currentEvent.preRender(delta, this, gfxManager);

        // Render characters
        gfxManager.batch.begin();
        for(CutsceneCharacter character : cutsceneCharacters) {
            character.render(delta, gfxManager);
        }
        gfxManager.batch.end();

        // Render cinematic bars
        if(cinematicBars) {
            final float marginPercent = 0.18f;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.rect(0, 0, gfxManager.screenSize.x, gfxManager.screenSize.y*marginPercent);
            shapeRenderer.rect(0, (1-marginPercent)*gfxManager.screenSize.y, gfxManager.screenSize.x, gfxManager.screenSize.y*marginPercent);
            shapeRenderer.end();
        }

        // Render skip label
        if(skipLabelAlpha > 0) {
            BitmapFont font = new BitmapFont();
            gfxManager.batch.setColor(1, 1, 1, MathUtils.clamp(skipLabelAlpha, 0, 1));
            gfxManager.batch.begin();
            font.draw(gfxManager.batch, "Press ESC to skip", gfxManager.screenSize.x*0.9f, gfxManager.screenSize.x*0.1f, 0, Align.right, false);
            gfxManager.batch.end();
            gfxManager.batch.setColor(1, 1, 1, 1);
        }

        currentEvent.postRender(delta, this, gfxManager);
    }

    public void addCharacter(CutsceneCharacter character) {
        cutsceneCharacters.add(character);
    }

    public void removeCharacter(CutsceneCharacter character) {
        cutsceneCharacters.remove(character);
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
