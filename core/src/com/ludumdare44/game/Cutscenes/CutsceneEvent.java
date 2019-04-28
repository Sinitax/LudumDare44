package com.ludumdare44.game.Cutscenes;

import com.ludumdare44.game.GFX.GFXManager;

public abstract class CutsceneEvent {

    /**
     * Handles processing of the cutscene event.
     * @param delta
     * @param screen
     * @return true if this event has been completed.
     */
    public abstract boolean processEvent(float delta, CutsceneScreen screen);

    /**
     * Called if the player attempts to skip this event. If this event is skippable, completes what remains of the event.
     * @return true if this event has been skipped.
     */
    public abstract boolean skip();

    public void preRender(float delta, CutsceneScreen screen, GFXManager gfxManager) {}

    public void postRender(float delta, CutsceneScreen screen, GFXManager gfxManager) {}

}
