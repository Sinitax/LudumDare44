package com.ludumdare44.game.Cutscenes.events;

import com.ludumdare44.game.Cutscenes.CutsceneEvent;
import com.ludumdare44.game.Cutscenes.CutsceneScreen;
import com.ludumdare44.game.Cutscenes.ScreenFader;
import com.ludumdare44.game.GFX.GFXManager;

public class FadeEvent extends CutsceneEvent {

    protected final ScreenFader fader;
    protected boolean isCompleted = false;

    public FadeEvent(ScreenFader fader) {
        this.fader = fader;
        this.fader.onComplete(() -> {
            isCompleted = true;
        });
    }

    @Override
    public boolean processEvent(float delta, CutsceneScreen screen) {
        return isCompleted;
    }

    @Override
    public boolean skip() {
        return false;
    }

    @Override
    public void postRender(float delta, CutsceneScreen screen, GFXManager gfxManager) {
        super.postRender(delta, screen, gfxManager);
        fader.render(gfxManager, delta);
    }
}
