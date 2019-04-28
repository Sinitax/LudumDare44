package com.ludumdare44.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ludumdare44.game.Cutscenes.CutsceneCharacter;
import com.ludumdare44.game.Cutscenes.CutsceneCharacterPosition;
import com.ludumdare44.game.Cutscenes.CutsceneScreen;
import com.ludumdare44.game.Cutscenes.ScreenFader;
import com.ludumdare44.game.Cutscenes.events.*;

public class LudumDareGame extends Game {

    @Override
    public void create() {
        setScreen(createIntroCutscene().onComplete(() -> setScreen(new GameScene())));
        //setScreen(new GameScene());
    }

    protected CutsceneScreen createIntroCutscene() {
        CutsceneCharacter characterPlayer = new CutsceneCharacter(
                new Sprite(new Texture("assets/models/characters/rogue/icon.png")),
                "Player");
        CutsceneCharacter characterDevil = new CutsceneCharacter(
                new Sprite(new Texture("assets/models/enemies/devil/devil.png")),
                "Devil");

        CutsceneScreen cutscene = new CutsceneScreen(new Color(0.3f, 0.3f, 0.3f, 1f), true);
        cutscene.addCutsceneEvent(new FadeEvent(new ScreenFader(Color.BLACK, 2).fadeIn()))
                .addCutsceneEvent(new CharacterEnterEvent(characterPlayer, CutsceneCharacterPosition.RIGHT))
                .addCutsceneEvent(new WaitEvent(1))
                .addCutsceneEvent(new CharacterEnterEvent(characterDevil, CutsceneCharacterPosition.LEFT))
                .addCutsceneEvent(new WaitEvent(2))
                .addCutsceneEvent(new TextEvent("We meet again."))
                .addCutsceneEvent(new TextEvent("Our pact is complete, now hand over your soul!"))
                .addCutsceneEvent(new TextEvent("You refuse? I guess there is something else you could do for me as payment..."))
                .addCutsceneEvent(new TextEvent("There are some stray souls that need to be collected in the underground."))
                .addCutsceneEvent(new TextEvent("I'll be watching your progress closely. There will a punishment if you're lacking."))
                .addCutsceneEvent(new TextEvent("Now, go! Before I change my mind..."))
                .addCutsceneEvent(new WaitEvent(1))
                .addCutsceneEvent(new CharacterExitEvent(characterPlayer))
                .addCutsceneEvent(new WaitEvent(1))
                .addCutsceneEvent(new FadeEvent(new ScreenFader(Color.BLACK, 1.5f).fadeOut()));

        return cutscene;
    }
}
