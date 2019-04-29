package com.ludumdare44.game.GUI;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Constants;
import com.ludumdare44.game.GFX.GFXManager;

import java.util.ArrayList;
import java.util.List;

public abstract class Gui extends InputAdapter {

    protected boolean ready = false;
    protected int width;
    protected int height;
    protected int scale;
    private final List<GuiElement> elements = new ArrayList<>();
    protected GFXManager gfx;

    protected int mouseX;
    protected int mouseY;

    public final void resizeGui(int width, int height, int scale) {
        this.ready = true;
        this.width = width / scale;
        this.height = height / scale;
        this.scale = scale;

        elements.clear();
        gfx = new GFXManager(new Vector2(width, height));
        createGui(this.width, this.height);
    }

    public final void resizeGui(int width, int height) {
        resizeGui(width, height, Constants.PIXEL_SCALE);
    }

    protected abstract void createGui(int width, int height);
    protected abstract void update(float delta);
    protected void preRender(float delta) {}
    protected void postRender(float delta) {}

    public final void render(float delta) {
        if(!ready)
            throw new RuntimeException("Gui is not ready. Call resizeGui() before rendering.");

        update(delta);

        gfx.batch.getTransformMatrix().scale(scale, scale, scale);
        preRender(delta);
        gfx.batch.begin();
        for(GuiElement element : elements) {
            element.render(delta, gfx);
        }
        gfx.batch.end();
        postRender(delta);
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
        this.mouseY = height - screenY/scale;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        this.mouseX = screenX/scale;
        this.mouseY = height - screenY/scale;
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for(GuiElement element : elements) {
            element.onMouseDown(screenX/scale, height - screenY/scale, button);
            if(element.blocksMouse(screenX/scale, height - screenY/scale))
                return false;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for(GuiElement element : elements) {
            element.onMouseUp(screenX/scale, height - screenY/scale, button);
            if(element.blocksMouse(screenX/scale, height - screenY/scale))
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
