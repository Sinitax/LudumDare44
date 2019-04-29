package com.ludumdare44.game.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.Physics.PhysicsObject;
import com.ludumdare44.game.Physics.VisualPhysObject;

public class Bat extends VisualPhysObject {
    private static Texture batTexture = new Texture("assets/bat.png");
    public static Animation<TextureRegion> batAnimation = new Animation<>(0.14f, TextureRegion.split(batTexture, batTexture.getWidth()/4, batTexture.getHeight())[0]);
    private TextureRegion currentSprite;

    private boolean destroyed = false;

    private static int width = batAnimation.getKeyFrames()[0].getRegionWidth(), height = batAnimation.getKeyFrames()[0].getRegionHeight();

    private float bobTime = 0;
    private float animationTime = 0;

    private float bobAmount = 10;
    private float bobSpeed = 250f;
    private float bobHeight;

    private static float spriteScale = 2;

    @Override
    public Vector2 getHitbox() {
        return getModelSize().scl(0.5f);
    }

    @Override
    public Vector2 getHitboxOffset() {
        return new Vector2(0, 0);
    }

    @Override
    public void onCollision(PhysicsObject other, float delta) {
    }

    @Override
    public int getAccelMax() {
        return 200;
    }

    @Override
    public int getDecelMax() {
        return 1000;
    }

    @Override
    public int getFspeedMax() {
        return -1;
    }

    @Override
    public Vector2 getOriginOffset() {
        return new Vector2(0, 0);
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    private boolean stagnant = false;
    @Override
    public boolean isStagnant() { return stagnant; }

    public void destroy() {
        destroyed = true;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public Vector2 getModelSize() {
        return Bat.getSize();
    }

    public static Vector2 getSize() {
        return new Vector2(width * spriteScale, height * spriteScale);
    }

    @Override
    public void update(float delta) {
        bobTime = (bobTime + delta) % (360);
        animationTime = (animationTime + delta) % batAnimation.getAnimationDuration();
        currentSprite = batAnimation.getKeyFrame(animationTime);

        setPos(new Vector2(getPos().x, (float) (bobHeight + Math.sin(bobTime * (Math.PI / 180.f) * bobSpeed) * bobAmount)));
    }

    @Override
    public void render(GFXManager gfx) {
        gfx.batch.draw(batAnimation.getKeyFrame(animationTime), getPos().x - Bat.getSize().x / 2, getPos().y - Bat.getSize().y / 2, getSize().x, getSize().y);
    }

    public Bat(Vector2 spos) {
        super(spos);
        bobHeight = spos.y;
    }
}