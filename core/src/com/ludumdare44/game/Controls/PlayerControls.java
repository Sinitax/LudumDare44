package com.ludumdare44.game.Controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Characters.Player;

public class PlayerControls {
    private Player player;
    private ControlManager controlManager;

    public void update() {
        if (!player.isBusy()) {
            if (controlManager.justPressed(Input.Keys.E)) {
                System.out.println("grapple");
                Vector2 mouseVec = new Vector2(Gdx.input.getX(), Gdx.input.getY());
                player.doGrapple(mouseVec);
            } else if (controlManager.justPressed(Input.Keys.R)) {
                player.doAbility();
            } else if (controlManager.justPressed(Input.Keys.SPACE)) {
                player.stopGrapple();
            }

        }

        if (controlManager.isPressed(Input.Keys.D)) {
            player.setSwing(1);
        } else if (controlManager.isPressed(Input.Keys.A)) {
            player.setSwing(-1);
        } else {
            player.setSwing(0);
        }
    }

    public PlayerControls(ControlManager _controlManager, Player _player) {
        controlManager = _controlManager;
        player = _player;
    }
}
