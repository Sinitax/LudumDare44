package com.ludumdare44.game.GUI.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.GUI.Gui;
import com.ludumdare44.game.GUI.GuiElement;

import java.util.ArrayList;
import java.util.List;

public class ElementTextButton extends GuiElement {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected ElementLabel label;

    protected TextureRegion textureNormal;
    protected TextureRegion textureHover;
    protected TextureRegion texturePress;

    public ElementTextButton(Gui gui, int x, int y, int width, int height, String text) {
        super(gui);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = new ElementLabel(gui, x+width/2, y+height/2 - 3, text, Color.WHITE);
        this.label.alignCenter().alignMiddle();

        Texture buttonTexture = new Texture("assets/gui/button.png");
        TextureRegion[][] buttonTextures = new TextureRegion(buttonTexture).split(buttonTexture.getWidth()/3, buttonTexture.getHeight());
        textureNormal = buttonTextures[0][0];
        textureHover = buttonTextures[0][1];
        texturePress = buttonTextures[0][2];
    }

    @Override
    public void render(float delta, GFXManager gfx) {
        gui.renderTexturedRect(gfx, textureNormal, x, y, width, height);
        label.render(delta, gfx);
    }

    public interface IClickListener {
        void onClick();
    }

    protected final List<IClickListener> clickListeners = new ArrayList<>();

    public void onClick(IClickListener clickListener) {
        clickListeners.add(clickListener);
    }
}
