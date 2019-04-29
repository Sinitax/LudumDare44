package com.ludumdare44.game.Controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Characters.Player;
import com.ludumdare44.game.UI.CameraManager;

public class PlayerControls {
    private Player player;
    private ControlManager controlManager;
    private CameraManager cameraManager;


    public void update(float delta) {
        if (!player.isBusy()) {
            if (controlManager.justPressed(Input.Keys.E)) {
                player.stopGrapple();
                Vector2 mouseVec = new Vector2(Gdx.input.getX(), cameraManager.getScreenSize().y - Gdx.input.getY()).add(cameraManager.getPos()).sub(cameraManager.getScreenSize().scl(0.5f));
                player.doGrapple(mouseVec);
            } else if (controlManager.justPressed(Input.Keys.R)) {
                player.doAbility();
            } else if (controlManager.justPressed(Input.Keys.SPACE) && player.isGrappling()) {
                player.doJump();
            }
            if (controlManager.justPressed(Input.Keys.W)) {
                player.doReel();
            } else if (controlManager.justReleased(Input.Keys.W)) {
                player.stopReel();
            }

        }
    }

    public PlayerControls(ControlManager _controlManager, CameraManager _cameraManager, Player _player) {
        cameraManager = _cameraManager;
        controlManager = _controlManager;
        player = _player;
    }
}
