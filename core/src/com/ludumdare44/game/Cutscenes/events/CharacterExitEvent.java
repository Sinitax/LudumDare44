package com.ludumdare44.game.Cutscenes.events;

import com.ludumdare44.game.Cutscenes.CutsceneCharacter;
import com.ludumdare44.game.Cutscenes.CutsceneEvent;
import com.ludumdare44.game.Cutscenes.CutsceneScreen;

public class CharacterExitEvent extends CutsceneEvent {

    protected final CutsceneCharacter character;

    public CharacterExitEvent(CutsceneCharacter character) {
        this.character = character;
    }

    @Override
    public boolean processEvent(float delta, CutsceneScreen screen) {
        screen.removeCharacter(character);
        return true;
    }

    @Override
    public boolean skip() {
        return false;
    }
}
