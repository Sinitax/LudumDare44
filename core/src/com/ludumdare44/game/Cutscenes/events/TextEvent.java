package com.ludumdare44.game.Cutscenes.events;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import com.ludumdare44.game.Constants;
import com.ludumdare44.game.Cutscenes.CutsceneEvent;
import com.ludumdare44.game.Cutscenes.CutsceneScreen;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.GUI.Fonts;

public class TextEvent extends CutsceneEvent {

    protected final BitmapFont font = Fonts.createDefaultPixelFont();

    protected final String text;
    protected final float printSpeed = 1;

    protected float elapsedTime = 0;
    protected String printedText = "";

    public TextEvent(String text, float printSpeed) {
        this.text = text + " "; // The extra space fixes a bug where the last character does not render.
    }

    public TextEvent(String text) {
        this(text, 1);
    }

    // The lerpTime that it should take to print the text to screen.
    protected float calculatePrintTime(String text) {
        return printSpeed*text.length()/30f; // 30 characters per second
    }

    // Seconds to idle after printing the entire text.
    protected float calculateReadTime(String text) {
        return 2f;
    }

    @Override
    public boolean processEvent(float delta, CutsceneScreen screen) {
        final float printTime = calculatePrintTime(text);
        final float readTime = calculateReadTime(text);

        if(elapsedTime >= printTime+readTime)
            return true;

        if(elapsedTime < printTime) {
            int chars = (int) ((elapsedTime/printTime) * text.length());
            chars = Math.min(text.length(), chars);
            printedText = text.substring(0, chars);
        } else {

        }

        elapsedTime += delta;
        
        return false;
    }

    @Override
    public boolean skip() {
        return false;
    }

    @Override
    public void postRender(float delta, CutsceneScreen screen, GFXManager gfxManager) {
        super.postRender(delta, screen, gfxManager);

        gfxManager.batch.begin();
        font.getData().setScale(Math.max(Constants.PIXEL_SCALE-1, 1));
        font.draw(gfxManager.batch, printedText, gfxManager.screenSize.x*0.1f, gfxManager.screenSize.y*0.1f + font.getLineHeight()/2,
                gfxManager.screenSize.x*0.8f, Align.center, true);
        gfxManager.batch.end();
    }
}
