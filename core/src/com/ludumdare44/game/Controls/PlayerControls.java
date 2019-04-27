package com.ludumdare44.game.Controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Characters.Player;

public class PlayerControls {
    private Player p;

    public void update() {
        Vector2 mouseVec = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        if (!p.isBusy()) {

            if (Gdx.input.isButtonPressed(Input.Keys.SPACE)) {
                p.doGrapple(mouseVec);
            } else if (Gdx.input.isButtonPressed(Input.Keys.E)) {
                p.doAbility();
            }

            if (Gdx.input.isButtonPressed(Input.Keys.D)) {
                p.setSwing(1);
            } else if (Gdx.input.isButtonPressed(Input.Keys.A)) {
                p.setSwing(-1);
            } else {
                p.setSwing(0);
            }

        }
    }

    public PlayerControls(Player _p) {
        this.p = _p;
    }
}
