package com.ludumdare44.game.Physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Characters.Player;
import com.ludumdare44.game.GFX.GFXManager;

public class Grapple extends VisualPhysObject {

    Texture spriteHook = new Texture("assets/models/weapons/grapple/grapple_hook.png");
    Texture spriteChain = new Texture("assets/models/weapons/grapple/grapple_chain.png");

    private static float grappleSpeed = 1400.f;

    private Vector2 grappleOffset = new Vector2(0, 0);

    boolean delete = false;

    private Player player;

    private boolean grappled = false;

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
            setPos(new Vector2(other.getPos().x, getPos().y));
            grappled = true;
        }
    }

    @Override
    public int getZ() {
        return 2;
    }

    @Override
    public Vector2 getModelSize() {
        return new Vector2(2, 2);
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
        final int pixelRatio = 3;

        int chainWidth = spriteChain.getWidth();
        int chainHeight = spriteChain.getHeight();
        int hookWidth = spriteHook.getWidth();
        int hookHeight = spriteHook.getHeight();
        float chainLength = player.getPos().sub(getPos()).len();
        float rotation = player.getPos().sub(getPos()).angle()+90;

        gfx.batch.end();
        gfx.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        gfx.shapeRenderer.line(getPos(), player.getPos());
        gfx.shapeRenderer.end();
        gfx.batch.begin();

        spriteChain.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.Repeat);
        Matrix4 originalTransformMatrix = gfx.batch.getTransformMatrix().cpy();
        Matrix4 transformationMatrix = gfx.batch.getTransformMatrix()
                .translate(player.getPos().x, player.getPos().y, 0)
                .rotate(0, 0, 1, rotation)
                .translate(-player.getPos().x, -player.getPos().y, 0);
        gfx.batch.setTransformMatrix(transformationMatrix);

        gfx.batch.draw(spriteChain, player.getPos().x - chainWidth * pixelRatio / 2.f + grappleOffset.x, player.getPos().y + grappleOffset.y,
                chainWidth*pixelRatio, chainLength, 0, 0, 1, chainLength/chainHeight/pixelRatio);

        gfx.batch.draw(new TextureRegion(spriteHook), player.getPos().x - hookWidth * pixelRatio / 2.f, player.getPos().y + chainLength - hookHeight * pixelRatio / 2.f, hookWidth*pixelRatio, hookHeight*pixelRatio);

        gfx.batch.setTransformMatrix(originalTransformMatrix);

        //gfx.batch.draw(new TextureRegion(spriteHook), getPos().x - hookWidth * pixelRatio / 2.f, getPos().y - hookHeight * pixelRatio / 2.f , 0, 0, hookWidth*pixelRatio, hookHeight*pixelRatio, 1, 1, rotation);
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