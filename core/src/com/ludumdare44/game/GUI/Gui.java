package com.ludumdare44.game.GUI;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ludumdare44.game.GFX.GFXManager;

import java.util.ArrayList;
import java.util.List;

public abstract class Gui extends InputAdapter {

    protected boolean setup = false;
    protected int width;
    protected int height;
    protected int scale;
    private final List<GuiElement> elements = new ArrayList<>();

    protected int mouseX;
    protected int mouseY;

    public void resizeGui(int width, int height, int scale) {
        this.setup = true;
        this.width = width / scale;
        this.height = height / scale;
        this.scale = scale;

        elements.clear();
        createGui(this.width, this.height);
    }

    protected abstract void createGui(int width, int height);

    public void render(float delta, GFXManager gfx) {
        if(!setup)
            throw new RuntimeException("Gui is not ready. Call resizeGui() before rendering.");

        gfx.batch.getTransformMatrix().scale(scale, scale, scale);
        gfx.batch.begin();
        for(GuiElement element : elements) {
            element.render(delta, gfx);
        }
        gfx.batch.end();
        gfx.batch.getTransformMatrix().idt();
    }

    protected <T extends GuiElement> T add(T element) {
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

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        this.mouseX = screenX/scale;
        this.mouseY = screenY/scale;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        this.mouseX = screenX/scale;
        this.mouseY = screenY/scale;
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for(GuiElement element : elements) {
            element.onMouseDown(screenX/scale, screenY/scale, button);
            if(element.blocksMouse(screenX/scale, screenY/scale))
                return false;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for(GuiElement element : elements) {
            element.onMouseUp(screenX/scale, screenY/scale, button);
            if(element.blocksMouse(screenX/scale, screenY/scale))
                return false;
        }
        return false;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }
}
