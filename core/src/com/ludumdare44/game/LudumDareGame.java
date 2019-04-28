package com.ludumdare44.game;

import com.badlogic.gdx.Game;
import com.ludumdare44.game.Cutscenes.CutsceneScreen;

public class LudumDareGame extends Game {

    @Override
    public void create() {
        //setScreen(new CutsceneScreen());
        setScreen(new GameScene());
    }
}
