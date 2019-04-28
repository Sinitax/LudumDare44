package com.ludumdare44.game.Cutscenes.events;

import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Cutscenes.CutsceneCharacter;
import com.ludumdare44.game.Cutscenes.CutsceneEvent;
import com.ludumdare44.game.Cutscenes.CutsceneScreen;

public class CharacterExitEvent extends CutsceneEvent {

    protected final CutsceneCharacter character;
    protected final CharacterLerpEvent lerpEvent;

    public CharacterExitEvent(CutsceneCharacter character, float fadeTime, Vector2 fadeOffset) {
        this.character = character;

        this.lerpEvent = new CharacterLerpEvent(character, fadeTime)
                .setAlpha(0)
                .setOffset(fadeOffset);
    }

    public CharacterExitEvent(CutsceneCharacter character, float fadeTime) {
        this(character, fadeTime, new Vector2());
    }

    public CharacterExitEvent(CutsceneCharacter character) {
        this(character, 0);
    }

    @Override
    public boolean processEvent(float delta, CutsceneScreen screen) {
        if(lerpEvent.processEvent(delta, screen)) {
            screen.removeCharacter(character);
            return true;
        }

        return false;
    }

    @Override
    public boolean skip() {
        return false;
    }
}
