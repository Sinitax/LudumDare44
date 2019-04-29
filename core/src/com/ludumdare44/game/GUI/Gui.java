package com.ludumdare44.game.GUI;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.GFX.IRenderable;

import java.util.ArrayList;
import java.util.List;

public abstract class Gui extends InputAdapter {

    protected final int width;
    protected final int height;
    protected final int scale;
    private final List<GuiElement> elements = new ArrayList<>();

    public Gui(int width, int height, int scale) {
        this.width = width / scale;
        this.height = height / scale;
        this.scale = scale;
        setupGui();
    }

    protected abstract void setupGui();

    public void render(float delta, GFXManager gfx) {
        gfx.batch.getTransformMatrix().scale(scale, scale, scale);
        gfx.batch.begin();
        for(GuiElement element : elements) {
            element.render(delta, gfx);
        }
        gfx.batch.end();
        gfx.batch.getTransformMatrix().idt();
    }

    protected GuiElement add(GuiElement element) {
        elements.add(element);
        return element;
    }

    public void renderTexturedRect(GFXManager gfx, TextureRegion texture, int x, int y, int width, int height) {
        int patchWidth = texture.getRegionWidth()/3;
        int patchHeight = texture.getRegionHeight()/3;
        TextureRegion[][] patches = texture.split(patchWidth, patchHeight);

        gfx.batch.draw(patches[2][0], x, y);
        gfx.batch.draw(patches[2][2], x+width-patchWidth, y);
        gfx.batch.draw(patches[2][1], x+patchWidth, y, width-2*patchWidth, patchHeight);

        gfx.batch.draw(patches[0][0], x, y+height-patchHeight);
        gfx.batch.draw(patches[0][2], x+width-patchWidth, y+height-patchHeight);
        gfx.batch.draw(patches[0][1], x+patchWidth, y+height-patchHeight, width-2*patchWidth, patchHeight);

        gfx.batch.draw(patches[1][0], x, y+patchHeight, patchWidth, height-2*patchHeight);
        gfx.batch.draw(patches[1][2], x+width-patchWidth, y+patchHeight, patchWidth, height-2*patchHeight);
        gfx.batch.draw(patches[1][1], x+patchWidth, y+patchHeight, width-2*patchWidth, height-2*patchHeight);

    }
}
