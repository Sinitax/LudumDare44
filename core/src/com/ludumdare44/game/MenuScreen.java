package com.ludumdare44.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.ludumdare44.game.Cutscenes.ScreenFader;
import com.ludumdare44.game.GUI.GuiScreen;
import com.ludumdare44.game.GUI.elements.ElementLabel;
import com.ludumdare44.game.GUI.elements.ElementTextButton;

public class MenuScreen extends GuiScreen {

    protected final LudumDareGame game;
    protected ScreenFader fader = new ScreenFader(Color.BLACK, 0.5f);
    protected Texture devilTexture = new Texture("assets/devil.png");
    protected float time = 0;

    public MenuScreen(LudumDareGame game) {
        super(new Color(0x7e7e8fff));
        this.game = game;
    }

    @Override
    public void show() {
        fader.fadeIn();
    }

    @Override
    protected void createGui(int width, int height) {
        add(new ElementLabel(this, width/2, height*5/6, "Title", Color.WHITE))
                .alignCenter()
                .alignMiddle();

        add(new ElementTextButton(this, width/10, height/2+5, width*4/10, 30, "Play Game"))
                .onClick(() -> {
                    Gdx.input.setInputProcessor(null);
                    fader.fadeOut().onComplete(() -> {
                        game.playNormalMode();
                    });
                });
        add(new ElementTextButton(this, width/10, height/2-30-5, width*4/10, 30, "Endless Mode"))
                .onClick(() -> {
                    Gdx.input.setInputProcessor(null);
                    fader.fadeOut().onComplete(() -> {
                        game.playEndlessMode();
                    });
                });
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        gfxManager.batch.getTransformMatrix().scale(scale, scale, scale);
        gfxManager.batch.begin();

        time += delta;
        float devilYOffset = (float)Math.sin(time * 2f) * 4;
        gfxManager.batch.draw(devilTexture, width*3/4 - devilTexture.getWidth()/2, height/2 - devilTexture.getHeight()/2 + devilYOffset);

        gfxManager.batch.end();
        gfxManager.batch.getTransformMatrix().idt();

        fader.render(gfxManager, delta);
    }
}
