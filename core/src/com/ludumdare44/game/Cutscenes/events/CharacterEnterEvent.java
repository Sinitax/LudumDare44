package com.ludumdare44.game.Cutscenes.events;

import com.ludumdare44.game.Cutscenes.CutsceneCharacter;
import com.ludumdare44.game.Cutscenes.CutsceneCharacterPosition;
import com.ludumdare44.game.Cutscenes.CutsceneEvent;
import com.ludumdare44.game.Cutscenes.CutsceneScreen;

public class CharacterEnterEvent extends CutsceneEvent {

    protected final CutsceneCharacter character;
    protected final CutsceneCharacterPosition position;

    public CharacterEnterEvent(CutsceneCharacter character, CutsceneCharacterPosition position) {
        this.character = character;
        this.position = position;
    }

    public CharacterEnterEvent(CutsceneCharacter character) {
        this(character, null);
    }

    @Override
    public boolean processEvent(float delta, CutsceneScreen screen) {
        return true;
    }

    @Override
    public boolean skip() {
        return false;
    }
}
