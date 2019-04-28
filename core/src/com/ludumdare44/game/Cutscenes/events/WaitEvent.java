package com.ludumdare44.game.Cutscenes.events;

import com.ludumdare44.game.Cutscenes.CutsceneEvent;
import com.ludumdare44.game.Cutscenes.CutsceneScreen;

public class WaitEvent extends CutsceneEvent {

    protected final float waitSeconds;
    protected final boolean skippable;
    protected float timeWaited = 0;

    public WaitEvent(float waitSeconds, boolean skippable) {
        this.waitSeconds = waitSeconds;
        this.skippable = skippable;
    }

    public WaitEvent(float waitSeconds) {
        this(waitSeconds, false);
    }

    @Override
    public boolean processEvent(float delta, CutsceneScreen screen) {
        timeWaited += delta;
        if(timeWaited >= waitSeconds)
            return true;
        return false;
    }

    @Override
    public boolean skip() {
        return skippable;
    }
}
