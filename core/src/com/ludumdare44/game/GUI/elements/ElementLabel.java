package com.ludumdare44.game.GUI.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.GUI.Fonts;
import com.ludumdare44.game.GUI.Gui;
import com.ludumdare44.game.GUI.GuiElement;

public class ElementLabel extends GuiElement {

    protected final BitmapFont font;

    protected int x;
    protected int y;
    protected String text;
    protected Color color;
    protected int hAlign = Align.left;
    protected int vAlign = Align.top;

    public ElementLabel(Gui gui, int x, int y, String text, Color color, BitmapFont font) {
        super(gui);
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = color;
        this.font = font;
        font.setColor(color);
    }

    public ElementLabel(Gui gui, int x, int y, String text, Color color) {
        this(gui, x, y, text, color, Fonts.createDefaultPixelFont());
    }

    public ElementLabel(Gui gui, int x, int y, String text) {
        this(gui, x, y, text, Color.BLACK);
    }

    public ElementLabel alignLeft() {
        this.hAlign = Align.left;
        return this;
    }

    public ElementLabel alignCenter() {
        this.hAlign = Align.center;
        return this;
    }

    public ElementLabel alignRight() {
        this.hAlign = Align.right;
        return this;
    }

    public ElementLabel alignTop() {
        this.vAlign = Align.top;
        return this;
    }

    public ElementLabel alignMiddle() {
        this.vAlign = Align.center;
        return this;
    }

    public ElementLabel alignBottom() {
        this.vAlign = Align.bottom;
        return this;
    }

    @Override
    public void render(float delta, GFXManager gfx) {
        float yOffset = 0;
        if(vAlign == Align.center || vAlign == Align.bottom) {
            yOffset = font.getLineHeight() / (vAlign == Align.bottom ? 1 : 2);
        }
        font.draw(gfx.batch, text, x, y+yOffset, 0, hAlign, false);
    }

    @Override
    public boolean blocksMouse(int x, int y) {
        return false;
    }
}
