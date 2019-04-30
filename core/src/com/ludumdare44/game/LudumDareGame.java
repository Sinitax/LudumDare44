package com.ludumdare44.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Cutscenes.CutsceneCharacter;
import com.ludumdare44.game.Cutscenes.CutsceneCharacterPosition;
import com.ludumdare44.game.Cutscenes.CutsceneScreen;
import com.ludumdare44.game.Cutscenes.ScreenFader;
import com.ludumdare44.game.Cutscenes.events.*;

public class LudumDareGame extends Game {

    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }

    public void playNormalMode() {
        setScreen(createIntroCutscene().onComplete(() -> {
            setScreen(new GameScene().onGameOver((soulsCollected, deathBy) -> {
                setScreen(createGameOverCutscene(soulsCollected, deathBy).onComplete(() -> {
                    setScreen(new MenuScreen(this));
                }));
            }));
        }));
    }

    public void playEndlessMode() {
        setScreen(new GameScene().onGameOver((soulsCollected, deathBy) -> {
            playEndlessMode();
        }));
    }

    protected CutsceneScreen createIntroCutscene() {
        CutsceneCharacter characterPlayer = createPlayerCharacter();
        CutsceneCharacter characterDevil = createDevilCharacter();
        CutsceneScreen cutscene = new CutsceneScreen(new Color(0.26f, 0.26f, 0.31f, 1f), true);

        cutscene.addCutsceneEvent(new FadeEvent(new ScreenFader(Color.BLACK, 2).fadeIn()))
                .addCutsceneEvent(new CharacterEnterEvent(characterPlayer, CutsceneCharacterPosition.RIGHT, 1))
                .addCutsceneEvent(new WaitEvent(1))
                .addCutsceneEvent(new CharacterEnterEvent(characterDevil, CutsceneCharacterPosition.LEFT, 1))
                .addCutsceneEvent(new WaitEvent(2))
                .addCutsceneEvent(new TextEvent("We meet again."))
                .addCutsceneEvent(new TextEvent("Our pact is complete, now hand over your soul!"))
                .addCutsceneEvent(new TextEvent("You refuse?"))
                .addCutsceneEvent(new TextEvent("I guess there is something else you could do for me as payment..."))
                .addCutsceneEvent(new TextEvent("There are some stray souls that need to be collected in the underground."))
                .addCutsceneEvent(new TextEvent("I'll be watching your progress closely. There will a punishment if you're lacking."))
                .addCutsceneEvent(new TextEvent("Now, go! Before I change my mind..."))
                .addCutsceneEvent(new WaitEvent(0.5f))
                .addCutsceneEvent(new CharacterExitEvent(characterPlayer, 1f))
                .addCutsceneEvent(new WaitEvent(1.5f))
                .addCutsceneEvent(new FadeEvent(new ScreenFader(Color.BLACK, 1.5f).fadeOut()));

        return cutscene;
    }

    protected CutsceneScreen createGameOverCutscene(int soulsCollected, GameScene.DeathType deathBy) {
        CutsceneCharacter characterPlayer = createPlayerCharacter();
        CutsceneCharacter characterDevil = createDevilCharacter();
        CutsceneScreen cutscene = new CutsceneScreen(new Color(0.26f, 0.26f, 0.31f, 1f), true);

        cutscene.addCutsceneEvent(new CharacterEnterEvent(characterDevil, CutsceneCharacterPosition.LEFT))
                .addCutsceneEvent(new FadeEvent(new ScreenFader(Color.BLACK, 2).fadeIn()))
                .addCutsceneEvent(new WaitEvent(1))
                .addCutsceneEvent(new CharacterEnterEvent(characterPlayer, CutsceneCharacterPosition.RIGHT, 1))
                .addCutsceneEvent(new WaitEvent(2));

        if(deathBy == GameScene.DeathType.DEVIL)
            cutscene.addCutsceneEvent(new TextEvent("I told you I'd be watching closely."));
        else if(deathBy == GameScene.DeathType.LAVA)
            cutscene.addCutsceneEvent(new TextEvent("It looks like the lava made it hard to stay afloat."));

        cutscene.addCutsceneEvent(new TextEvent("Now, let's see how you did..."))
                .addCutsceneEvent(new TextEvent("You collected " + soulsCollected + " souls."));

        if(soulsCollected > 20) {
            cutscene.addCutsceneEvent(new TextEvent("I am impressed."))
                    .addCutsceneEvent(new TextEvent("I'll gladly accept these in exchange for your life."))
                    .addCutsceneEvent(new CharacterExitEvent(characterDevil, 2))
                    .addCutsceneEvent(new WaitEvent(1));
        } else {
            cutscene.addCutsceneEvent(new TextEvent("I am disappointed."))
                    .addCutsceneEvent(new TextEvent("This is not enough to buy back your soul."))
                    .addCutsceneEvent(new TextEvent("Now come!"))
                    .addCutsceneEvent(new CharacterLerpEvent(characterPlayer, 2.5f).setOffset(new Vector2(-150, 0)));
        }

        cutscene.addCutsceneEvent(new FadeEvent(new ScreenFader(Color.BLACK, 1).fadeOut()));

        return cutscene;
    }

    protected CutsceneCharacter createPlayerCharacter() {
        Texture playerTexture = new Texture("assets/player.png");
        TextureRegion playerTextureRegion = TextureRegion.split(playerTexture, playerTexture.getWidth()/7, playerTexture.getHeight())[0][0];
        playerTextureRegion.flip(true, false);
        return new CutsceneCharacter(playerTextureRegion, "Player");
    }

    protected CutsceneCharacter createDevilCharacter() {
        Texture devilTexture = new Texture("assets/devil.png");
        return new CutsceneCharacter(devilTexture, "Devil");
    }
}
