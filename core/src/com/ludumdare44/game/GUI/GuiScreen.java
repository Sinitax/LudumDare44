package com.ludumdare44.game.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Constants;
import com.ludumdare44.game.GFX.GFXManager;

public abstract class GuiScreen extends Gui implements Screen {

    protected GFXManager gfxManager;
    protected Color backgroundColor;

    public GuiScreen(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public GuiScreen() {
        this(Color.BLACK);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        render(delta, gfxManager);
    }

    @Override
    public void resize(int width, int height) {
        gfxManager = new GFXManager(new Vector2(width, height));
        resizeGui(width, height, Constants.PIXEL_SCALE);
        Gdx.input.setInputProcessor(this);
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
