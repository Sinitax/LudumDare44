package com.ludumdare44.game.Cutscenes.events;

import com.ludumdare44.game.Cutscenes.CutsceneCharacter;

public class CharacterExitEvent {

    protected final CutsceneCharacter character;

    public CharacterExitEvent(CutsceneCharacter character) {
        this.character = character;
    }
}
