package com.ludumdare44.game.GUI;

import com.ludumdare44.game.GFX.GFXManager;

public abstract class GuiElement {

    protected final Gui gui;

    protected GuiElement(Gui gui) {
        this.gui = gui;
    }

    public abstract void render(float delta, GFXManager gfx);

    public abstract boolean blocksMouse(int x, int y);

    public void onMouseDown(int x, int y, int mouseButton) {}

    public void onMouseUp(int x, int y, int mouseButton) {}

}
