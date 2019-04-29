package com.ludumdare44.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.GUI.MenuGui;

public class MenuScreen implements Screen {

    protected GFXManager gfxManager;
    protected MenuGui menuGui;

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        menuGui.render(delta, gfxManager);
    }

    @Override
    public void resize(int width, int height) {
        gfxManager = new GFXManager(new Vector2(width, height));
        menuGui = new MenuGui(width, height,3);
        Gdx.input.setInputProcessor(menuGui);
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
