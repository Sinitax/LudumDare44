package com.ludumdare44.game.GUI;

import com.ludumdare44.game.GFX.GFXManager;

public abstract class GuiElement {

    protected final Gui gui;

    protected GuiElement(Gui gui) {
        this.gui = gui;
    }

    public abstract void render(float delta, GFXManager gfx);

}
