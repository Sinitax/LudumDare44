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

    private float grappleTimer = 0;
    private float reelCooldown = 200;

    public void update(float delta) {
        if (!player.isBusy()) {
            if (controlManager.justPressed(Input.Keys.SPACE)) {
                grappleTimer = 0;
                player.stopGrapple();
                Vector2 mouseVec = new Vector2(Gdx.input.getX(), cameraManager.getScreenSize().y - Gdx.input.getY()).add(cameraManager.getPos()).sub(cameraManager.getScreenSize().scl(0.5f));
                player.doGrapple(mouseVec);
            }
            /*
            if (controlManager.justPressed(Input.Keys.R)) {
                player.doAbility();
            }
            */
            if (controlManager.justPressed(Input.Keys.SHIFT_LEFT) && player.isGrappling()) {
                player.doJump();
            } else if (controlManager.justReleased(Input.Keys.SPACE)) {
                player.stopReel();
            }
            if (controlManager.isPressed(Input.Keys.SPACE)) {
                grappleTimer += delta;
                if (grappleTimer > reelCooldown) player.doReel();
            }

        }
    }

    public PlayerControls(ControlManager _controlManager, CameraManager _cameraManager, Player _player) {
        cameraManager = _cameraManager;
        controlManager = _controlManager;
        player = _player;
    }
}
