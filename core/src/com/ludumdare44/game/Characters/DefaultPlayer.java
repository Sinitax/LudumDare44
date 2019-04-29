package com.ludumdare44.game.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Map.ObjectAdder;

public class DefaultPlayer extends Player {
    Texture spriteSheet = new Texture("assets/player.png");
    TextureRegion[] spriteSheetMap = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 7, spriteSheet.getHeight())[0];
    
    @Override
    public Vector2 getModelScale() {
        return new Vector2(3, 3);
    }

    @Override
    public Vector2 getHitboxOffset() {
        return new Vector2(0, -15);
    }

    @Override
    public Sprite getGrappleSprite() {
        return new Sprite(spriteSheetMap[1]);
    }

    @Override
    public Sprite getIdleSprite() {
        return new Sprite(spriteSheetMap[0]);
    }

    @Override
    public Animation<TextureRegion> getAirborneAnimation() {
        return new Animation<>(0.6f, spriteSheetMap[2], spriteSheetMap[3]);
    }

    @Override
    public Animation<TextureRegion> getDeathAnimation() { return new Animation<>(1f, spriteSheetMap[4], spriteSheetMap[5], spriteSheetMap[6]); }

    public DefaultPlayer(Vector2 pos, ObjectAdder objectAdder) {
        super(pos, objectAdder);
        initAssets();
    }
}
