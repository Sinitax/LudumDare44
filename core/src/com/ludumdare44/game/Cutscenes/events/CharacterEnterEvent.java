package com.ludumdare44.game.Cutscenes.events;

import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Cutscenes.CutsceneCharacter;
import com.ludumdare44.game.Cutscenes.CutsceneCharacterPosition;
import com.ludumdare44.game.Cutscenes.CutsceneEvent;
import com.ludumdare44.game.Cutscenes.CutsceneScreen;

public class CharacterEnterEvent extends CutsceneEvent {

    protected final CutsceneCharacter character;
    protected final CharacterLerpEvent lerpEvent;

    public CharacterEnterEvent(CutsceneCharacter character, CutsceneCharacterPosition position, float fadeTime, Vector2 fadeOffset) {
        this.character = character;
        this.character.setPosition(position);

        if(fadeTime > 0) {
            this.character.setAlpha(0);
            this.character.setOffset(fadeOffset.cpy().scl(-1));
        }
        this.lerpEvent = new CharacterLerpEvent(character, fadeTime)
                .setAlpha(1)
                .setOffset(new Vector2());
    }

    public CharacterEnterEvent(CutsceneCharacter character, CutsceneCharacterPosition position, float fadeTime) {
        this(character, position, fadeTime, new Vector2());
    }

    public CharacterEnterEvent(CutsceneCharacter character, CutsceneCharacterPosition position) {
        this(character, position, 0);
    }

    @Override
    public boolean processEvent(float delta, CutsceneScreen screen) {
        screen.addCharacter(character);

        return lerpEvent.processEvent(delta, screen);
    }

    @Override
    public boolean skip() {
        return false;
    }
}
