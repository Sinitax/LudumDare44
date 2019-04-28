package com.ludumdare44.game.Physics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Characters.Player;
import com.ludumdare44.game.GFX.GFXManager;

public class Grapple extends VisualPhysObject {
    Texture spriteSheet = new Texture("assets/models/characters/rogue/sprites.png");
    TextureRegion[][] spriteSheetMap = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 10, spriteSheet.getHeight() / 10);

    private Animation<TextureRegion> flyingAnimation = new Animation<>(0.04f, spriteSheetMap[0]);
    private Animation<TextureRegion> collideAnimation = new Animation<>(0.04f, spriteSheetMap[5]);
    private Animation<TextureRegion> currentAnimation;

    private static float grappleSpeed = 1400.f;

    boolean delete = false;

    private Player player;

    private boolean grappled = false;

    private float animationTimer = 0.f;
    private float maxDist = 850;

    private Vector2 initialPos;

    public boolean isGrappled() {
        return grappled;
    }

    @Override
    public boolean alive() {
        return !delete && player.alive();
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
        return new Vector2(1, 1);
    }

    @Override
    public void onCollision(PhysicsObject other) {
        if (other instanceof Obstacle) {
            setSpeed(new Vector2(0, 0));
            setFspeed(new Vector2(0, 0));
            grappled = true;
        }
    }

    @Override
    public int getZ() {
        return 2;
    }

    @Override
    public Vector2 getModelSize() {
        TextureRegion frame = currentAnimation.getKeyFrames()[0];
        return new Vector2(frame.getRegionWidth(), frame.getRegionHeight());
    }

    @Override
    public Vector2 getOriginOffset() {
        return new Vector2(0, 0);
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
        updateSpeed(delta);
        updatePos(delta);
        if (getPos().sub(initialPos).len() > maxDist) {
            delete = true;
        }
    }

    public void kill() {
        delete = true;
    }
    
    @Override
    public void render(GFXManager gfx) {
        Sprite sprite = new Sprite(currentAnimation.getKeyFrame(animationTimer));
        gfx.drawModel(sprite, getPos());
        if (true) {//(grappled) {
            gfx.batch.end();
            gfx.shapeRenderer.setColor(Color.WHITE);
            gfx.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            gfx.shapeRenderer.line(player.getPos(), getPos());
            gfx.shapeRenderer.end();
            gfx.batch.begin();
        }
    }
    
    public Grapple(Player p, Vector2 rpos) {
        super(new Vector2(p.getPos()));
        rpos.sub(p.getPos()).nor();
        initialPos = p.getPos();
        player = p;
        Vector2 nspeed = new Vector2(rpos).scl(grappleSpeed);
        if (nspeed.len() < new Vector2(nspeed).add(new Vector2(rpos).scl(new Vector2(rpos).dot(p.getSpeed()))).len()) nspeed.add(rpos.scl(new Vector2(rpos).dot(p.getSpeed())));
        setSpeed(nspeed);
    }
}