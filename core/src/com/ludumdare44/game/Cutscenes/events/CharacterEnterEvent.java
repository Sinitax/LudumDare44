package com.ludumdare44.game.Cutscenes.events;

import com.ludumdare44.game.Cutscenes.CutsceneCharacter;
import com.ludumdare44.game.Cutscenes.CutsceneCharacterPosition;
import com.ludumdare44.game.Cutscenes.CutsceneEvent;
import com.ludumdare44.game.Cutscenes.CutsceneScreen;

public class CharacterEnterEvent extends CutsceneEvent {

    protected final CutsceneCharacter character;

    public CharacterEnterEvent(CutsceneCharacter character, CutsceneCharacterPosition position) {
        this.character = character;
        this.character.setPosition(position);
    }

    public CharacterEnterEvent(CutsceneCharacter character) {
        this(character, null);
    }

    @Override
    public boolean processEvent(float delta, CutsceneScreen screen) {
        screen.addCharacter(character);
        return true;
    }

    @Override
    public boolean skip() {
        return false;
    }
}
