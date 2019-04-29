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

    public interface IClickListener {
        void onClick();
    }

    protected final List<IClickListener> clickListeners = new ArrayList<>();

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected ElementLabel label;

    protected TextureRegion textureNormal;
    protected TextureRegion textureHover;
    protected TextureRegion texturePress;
    protected int yOffset = 0;
    protected int pressedYOffset = 0;

    protected boolean pressed = false;

    public ElementTextButton(Gui gui, int x, int y, int width, int height, String text) {
        super(gui);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = new ElementLabel(gui, x+width/2, 0, text, Color.WHITE);
        this.label.alignCenter().alignMiddle();

        Texture buttonTexture = new Texture("assets/gui/button.png");
        yOffset = 0;
        pressedYOffset = -1;

        TextureRegion[][] buttonTextures = new TextureRegion(buttonTexture).split(buttonTexture.getWidth()/3, buttonTexture.getHeight());
        textureNormal = buttonTextures[0][0];
        textureHover = buttonTextures[0][1];
        texturePress = buttonTextures[0][2];
    }

    @Override
    public void render(float delta, GFXManager gfx) {
        TextureRegion tex = textureNormal;
        boolean hover = blocksMouse(gui.getMouseX(), gui.getMouseY());
        if(!hover)
            pressed = false;
        if(pressed)
            tex = texturePress;
        else if(hover)
            tex = textureHover;

        label.y = y + height/2 + (pressed ? pressedYOffset : yOffset);

        gui.renderTexturedRect(gfx, tex, x, y, width, height);
        label.render(delta, gfx);
    }

    @Override
    public boolean blocksMouse(int x, int y) {
        return x >= this.x && y >= this.y && x <= (this.x + width) && y <= (this.y + height);
    }

    @Override
    public void onMouseDown(int x, int y, int mouseButton) {
        if(blocksMouse(x, y))
            pressed = true;
    }

    @Override
    public void onMouseUp(int x, int y, int mouseButton) {
        if(blocksMouse(x, y) && pressed)
            for(IClickListener listener : clickListeners)
                listener.onClick();
        pressed = false;

    }

    public void onClick(IClickListener clickListener) {
        clickListeners.add(clickListener);
    }
}
