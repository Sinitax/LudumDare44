package com.ludumdare44.game.Physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ludumdare44.game.Characters.Player;
import com.ludumdare44.game.GFX.GFXManager;

public class Grapple extends VisualPhysObject {

    Texture spriteHook = new Texture("assets/grapple_hook.png");
    Texture spriteChain = new Texture("assets/grapple_chain.png");

    private static float grappleSpeed = 1400.f;

    private boolean destroyed = false;

    private Player player;

    private boolean grappled = false;

    private float maxDist = 850;

    private Vector2 initialPos;

    public boolean isGrappled() {
        return grappled;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public boolean isStagnant() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return !this.isDestroyed();
    }

    @Override
    public Vector2 getHitbox() {
        return new Vector2(1, 1);
    }

    @Override
    public void onCollision(PhysicsObject other, float delta) {
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
            destroyed = true;
        }
    }

    @Override
    public void destroy() {
        destroyed = true;
    }
    
    @Override
    public void render(GFXManager gfx) {
        final int pixelRatio = 3;

        int chainWidth = spriteChain.getWidth();
        int chainHeight = spriteChain.getHeight();
        int hookWidth = spriteHook.getWidth();
        int hookHeight = spriteHook.getHeight();
        float chainLength = player.getPos().sub(getPos()).len();
        float playerRotation = player.getPos().sub(getPos()).angle() + 90 - 360;
        if (playerRotation > 10) playerRotation = 10;
        else if (playerRotation < -10) playerRotation = -10;

        Vector2 grapplePos = player.getPos().add(player.getGrappleOffset().scl(player.facingDirection(), 1).rotate(playerRotation));
        grapplePos.x -= chainWidth * pixelRatio / 2.f;
        float rotation = grapplePos.cpy().sub(new Vector2(getPos().x - hookWidth * pixelRatio / 2.f, getPos().y)).angle() + 90;

        gfx.batch.end();
        gfx.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        gfx.shapeRenderer.line(getPos(), player.getPos().add(player.getGrappleOffset()));
        gfx.shapeRenderer.end();
        gfx.batch.begin();

        Matrix4 originalMatrix = gfx.batch.getTransformMatrix().cpy();
        Matrix4 transformationMatrix = gfx.batch.getTransformMatrix().cpy()
                .translate(grapplePos.x + chainWidth * pixelRatio / 2.f, grapplePos.y, 0)
                .rotate(0, 0, 1, rotation)
                .translate(-grapplePos.x - chainWidth * pixelRatio / 2.f, -grapplePos.y, 0);
        gfx.batch.setTransformMatrix(transformationMatrix);

        spriteChain.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.Repeat);

        gfx.batch.draw(spriteChain, grapplePos.x, grapplePos.y, chainWidth * pixelRatio, chainLength - player.getGrappleOffset().y - hookHeight / 2.f, 0, 0, 1, chainLength / chainHeight / pixelRatio);
        //gfx.batch.draw(spriteChain, grapplePos.x - chainWidth * pixelRatio / 2.f, grapplePos.y, chainWidth * pixelRatio / 2, 0,
        //        spriteChain.getWidth(), spriteChain.getHeight(), );
        //gfx.batch.draw(new Sprite(spriteChain), grapplePos.x - chainWidth * pixelRatio / 2.f, grapplePos.y ,
         //       chainWidth * pixelRatio / 2, 0, chainWidth * pixelRatio, 3.f * (chainLength - player.getGrappleOffset().y - hookHeight * pixelRatio / 2.f), 1, 1/3.f, rotation);

        gfx.batch.setTransformMatrix(originalMatrix);

        gfx.batch.draw(new TextureRegion(spriteHook), getPos().x - hookWidth * pixelRatio / 2.f, getPos().y - hookHeight * pixelRatio / 2.f , hookWidth * pixelRatio / 2, hookHeight * pixelRatio / 2, hookWidth*pixelRatio, hookHeight*pixelRatio, 1, 1, rotation);
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