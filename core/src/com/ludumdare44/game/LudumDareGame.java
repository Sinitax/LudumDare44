package com.ludumdare44.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.ludumdare44.game.Cutscenes.CutsceneCharacter;
import com.ludumdare44.game.Cutscenes.CutsceneCharacterPosition;
import com.ludumdare44.game.Cutscenes.CutsceneScreen;
import com.ludumdare44.game.Cutscenes.ScreenFader;
import com.ludumdare44.game.Cutscenes.events.CharacterEnterEvent;
import com.ludumdare44.game.Cutscenes.events.FadeEvent;
import com.ludumdare44.game.Cutscenes.events.WaitEvent;

public class LudumDareGame extends Game {

    @Override
    public void create() {
        setScreen(createIntroCutscene().onComplete(() -> setScreen(new GameScene())));
        //setScreen(new GameScene());
    }

    protected CutsceneScreen createIntroCutscene() {
        CutsceneCharacter characterPlayer = new CutsceneCharacter(
                new Texture("assets/models/characters/rogue/icon.png"),
                "Player");
        CutsceneCharacter characterDevil = new CutsceneCharacter(
                new Texture("assets/models/enemies/devil/devil.png"),
                "Devil");

        CutsceneScreen cutscene = new CutsceneScreen(new Color(0.3f, 0.3f, 0.3f, 1f));
        cutscene.addCutsceneEvent(new FadeEvent(new ScreenFader(Color.BLACK, 2).fadeIn()))
                .addCutsceneEvent(new CharacterEnterEvent(characterPlayer, CutsceneCharacterPosition.RIGHT))
                .addCutsceneEvent(new WaitEvent(1))
                //.addCutsceneEvent(new CharacterExitEvent(characterDevil, CutsceneCharacterPosition.LEFT))
                .addCutsceneEvent(new FadeEvent(new ScreenFader(Color.BLACK, 2).fadeOut()));

        return cutscene;
    }
}
