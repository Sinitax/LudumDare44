package com.ludumdare44.game;

import com.badlogic.gdx.graphics.Color;
import com.ludumdare44.game.Cutscenes.ScreenFader;
import com.ludumdare44.game.GUI.GuiScreen;
import com.ludumdare44.game.GUI.elements.ElementTextButton;

public class MenuScreen extends GuiScreen {

    protected ScreenFader fader = new ScreenFader(Color.BLACK, 1);

    public MenuScreen() {
        super(Color.PURPLE);
    }

    @Override
    public void show() {
        fader.fadeIn();
    }

    @Override
    protected void createGui(int width, int height) {
        add(new ElementTextButton(this, width/2-100, height/2-20, 200, 40, "Play"));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        fader.render(gfxManager, delta);
    }
}
