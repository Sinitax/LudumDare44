package com.ludumdare44.game.GUI;

import com.ludumdare44.game.GUI.elements.ElementTextButton;

public class MenuGui extends Gui {

    public MenuGui(int width, int height, int scale) {
        super(width, height, scale);
    }

    @Override
    protected void setupGui() {
        add(new ElementTextButton(this, width/2-100, height/2-20, 200, 40, "Play"));
    }
}
