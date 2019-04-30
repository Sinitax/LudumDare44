package com.ludumdare44.game.Cutscenes.events;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Cutscenes.CutsceneCharacter;
import com.ludumdare44.game.Cutscenes.CutsceneEvent;
import com.ludumdare44.game.Cutscenes.CutsceneScreen;

public class CharacterLerpEvent extends CutsceneEvent {

    protected final CutsceneCharacter character;
    protected final float lerpTime;
    protected float targetAlpha = -1;
    protected Vector2 targetOffset = null;

    protected float elapsedTime = 0;
    protected float originalAlpha;
    protected Vector2 originalOffset;

    public CharacterLerpEvent(CutsceneCharacter character, float lerpTime) {
        this.character = character;
        this.lerpTime = lerpTime;
    }

    public CharacterLerpEvent setAlpha(float targetAlpha) {
        this.targetAlpha = targetAlpha;
        return this;
    }

    public CharacterLerpEvent setOffset(Vector2 targetOffset) {
        this.targetOffset = targetOffset;
        return this;
    }

    @Override
    public boolean processEvent(float delta, CutsceneScreen screen) {
        if(elapsedTime == 0) {
            originalAlpha = character.getAlpha();
            originalOffset = character.getOffset().cpy();
        }

        if(elapsedTime >= lerpTime) {
            return true;
        }

        elapsedTime += delta;
        float lerpAmount = elapsedTime / lerpTime;

        if(targetOffset != null)
            character.setOffset(originalOffset.lerp(targetOffset, lerpAmount));
        if(targetAlpha != -1)
            character.setAlpha(MathUtils.lerp(originalAlpha, targetAlpha, lerpAmount));

        return false;
    }

    @Override
    public boolean skip() {
        return false;
    }
}
