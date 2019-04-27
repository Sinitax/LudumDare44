package com.ludumdare44.game.Characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Ability {
    private int abilityCost;
    private int afterDuration;
    private int cooldown;
    float elapsed;
    boolean inUse = false;
    boolean afterEffect = false;
    Player p;
    Animation<TextureRegion> animation;
    public abstract void perform();
    public void use() {
        if (p.getEnergy() < abilityCost || p.isBusy() || elapsed < cooldown) return;
        p.setEnergy(p.getEnergy() - abilityCost);
        inUse = true;
        elapsed = 0;
        p.setBusy(true);
    }

    public void update(float delta) {
        if (inUse) {
            if (afterEffect && elapsed >= afterDuration) {
                afterEffect = false;
                inUse = false;
                elapsed = 0;
            }
            if (p.isBusy() && elapsed >= animation.getAnimationDuration()) {
                p.setBusy(false);
                afterEffect = true;
                elapsed = 0;
            }
            perform();
            p.sprite = new Sprite(animation.getKeyFrame(elapsed, false));
        }
        if (inUse || elapsed < cooldown) {
            elapsed += delta;
        }
    }

    public boolean ready() {
        return !(p.getEnergy() < abilityCost || p.isBusy()); // elapsed < coolDown
    }
    Ability(Animation<TextureRegion> _animation, Player _p, int _cost, int _afterDurationMS, int _cooldownMS) {
        animation = _animation;
        p =  _p;
        afterDuration = _afterDurationMS/1000;
        abilityCost = _cost;
        cooldown = _cooldownMS/1000;
        elapsed = cooldown;
    }
}