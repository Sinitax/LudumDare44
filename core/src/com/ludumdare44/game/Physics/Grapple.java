package com.ludumdare44.game.Physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;

public class Grapple extends VisualPhysObject {
    Texture spriteSheet = new Texture("assets/models/characters/rogue/sprites.png");
    TextureRegion[][] spriteSheetMap = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 10, spriteSheet.getHeight() / 10);

    private Animation<TextureRegion> flyingAnimation = new Animation<>(0.04f, spriteSheetMap[0]);
    private Animation<TextureRegion> collideAnimation = new Animation<>(0.04f, spriteSheetMap[5]);
    private Animation<TextureRegion> currentAnimation;

    private static float grappleSpeed = 300.f;

    boolean delete = false;

    private float animationTimer = 0.f;
    private float maxDist = 350;

    private Vector2 initialPos;

    @Override
    public boolean alive() {
        return !delete;
    }

    @Override
    public boolean stagnant() {
        return false;
    }

    @Override
    public boolean visible() {
        return this.alive();
    }

    @Override
    public Vector2 getHitbox() {
        return null;
    }

    @Override
    public Vector2 getHitboxOffset() {
        return null;
    }

    @Override
    public void onCollision(PhysicsObject other) {
        this.setFspeed(new Vector2(0, 0));
    }

    @Override
    public int getZ() {
        return 2;
    }

    @Override
    public Vector2 getModelSize() {
        return null;
    }

    @Override
    public Vector2 getOriginOffset() {
        return null;
    }

    //VisualPhysObject
    @Override
    public int getFspeedMax() {
        return 0;
    }

    @Override
    public int getDecelMax() {
        return 0;
    }
    
    @Override
    public int getAccelMax() {
        return -10;
    }
    

    @Override
    public void update(float delta) {
        currentAnimation = flyingAnimation;
        animationTimer += delta;
        updatePos(delta);
        if (getPos().sub(initialPos).len() > maxDist) {
            delete = true;
        }
    }
    
    @Override
    public void render(GFXManager gfx) {
        Sprite sprite = new Sprite(currentAnimation.getKeyFrame(animationTimer));
        gfx.drawModel(sprite, getPos());
    }
    
    public Grapple(Vector2 ipos, Vector2 rpos) {
        super(new Vector2(ipos));
        initialPos = ipos;
        setSpeed(rpos.nor().scl(grappleSpeed));
    }
}