package com.ludumdare44.game.GFX;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ludumdare44.game.Physics.PhysicsObject;

import javax.swing.*;
import java.util.ArrayList;

public class ParticleGenerator {
    private ArrayList<Particle> particleList;
    private Animation<TextureRegion> animation;

    private PhysicsObject owner;
    private float particleVelocityMax = 270.f;

    private float timeSpent = 0.f;
    private float spawnTime = 0.03f;

    private int maxParticles = 300;

    public void update(float delta) {
        timeSpent += delta;

        ArrayList<Particle> deleteList = new ArrayList<>();
        for (Particle p : particleList) {
            p.update(delta);
            if (p.isDestroyed()) deleteList.add(p);
        }
        for (Particle p : deleteList) {
            particleList.remove(p);
        }

        int newParticles = (int) (timeSpent / spawnTime);
        if (timeSpent > spawnTime && particleList.size() < maxParticles) {
            for (int i = 0; i < newParticles; i++) {
                Vector2 speed = new Vector2((float) (Math.random() - 0.5f), (float) (Math.random() - 0.5f)).nor().scl((float) Math.random() * particleVelocityMax);
                particleList.add(new Particle(owner.getPos(), animation, speed));
            }
            timeSpent = timeSpent % spawnTime;
        }
    }

    public void render(GFXManager gfx) {
        for (Particle p: particleList) {
            p.render(gfx);
        }
    }

    public ParticleGenerator(PhysicsObject _owner, String filename) {
        owner = _owner;
        Texture t = new Texture(filename);
        animation = new Animation<>(0.3f, TextureRegion.split(t, t.getWidth() / 8, t.getHeight())[0]);
        particleList = new ArrayList<>();
    }
}
