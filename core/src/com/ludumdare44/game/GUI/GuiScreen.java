package com.ludumdare44.game.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;

public abstract class GuiScreen extends Gui implements Screen {

    protected GFXManager gfxManager;

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        render(delta, gfxManager);
    }

    @Override
    public void resize(int width, int height) {
        gfxManager = new GFXManager(new Vector2(width, height));
        resizeGui(width, height, 3);
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
