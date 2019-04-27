package com.ludumdare44.game.Map;

import com.badlogic.gdx.math.*;
import com.ludumdare44.game.Characters.Player;
import com.ludumdare44.game.Physics.Grapple;
import com.ludumdare44.game.Physics.VisualPhysObject;
import com.sun.org.apache.regexp.internal.RE;
import sun.awt.X11.Visual;

import java.util.ArrayList;

public class ObjectManager {
    private ArrayList<VisualPhysObject> physobjects;
    private Rectangle[] obstacles;

    public void setObstacles(Rectangle[] obstacles) {
        this.obstacles = obstacles;
    }

    public static Rectangle toRectangle(VisualPhysObject vpo) {
        return new Rectangle(vpo.getPos().x + vpo.getHitboxOffset().x - vpo.getHitbox().x/2, vpo.getPos().y + vpo.getHitboxOffset().y - vpo.getHitbox().y/2, vpo.getHitbox().x, vpo.getHitbox().y);
    }

    public static void rectangleCollision(VisualPhysObject vpo, Rectangle ro, float delta) {
        Rectangle r = ObjectManager.toRectangle(vpo);
        Vector2 speed = vpo.getSpeed();
        Vector2 pos = new Vector2(vpo.getPos());
        Vector2 ppos = new Vector2(r.getX(), r.getY());
        ppos.x -= speed.x * delta;
        ppos.y -= speed.y * delta;
        if (r.overlaps(ro)) {
            boolean vertical = ppos.y + r.getHeight() < ro.getY() || ppos.y > ro.getY() + ro.getHeight();
            boolean horizontal = ppos.x + r.getWidth() < ro.getX() || ppos.x > ro.getX() + ro.getWidth();
            if (vertical && horizontal) {
                if (speed.x > speed.y) {
                    vertical = false;
                } else {
                    horizontal = false;
                }
                if (vpo instanceof Grapple) {
                    speed.x = 0;
                }
            }
            if (horizontal) {
                if (speed.x > 0) {
                    pos.x = ro.getX() - r.getWidth()/2 - .1f;
                } else {
                    pos.x = ro.getX() + ro.getWidth() + r.getWidth()/2 + .1f;
                }
                speed.x = 0;
            } else if (vertical) {
                if (speed.y > 0) {
                    pos.y = ro.getY() + (vpo.getHitbox().y/2 - r.getHeight()) - .1f;
                } else {
                    pos.y = ro.getY() + ro.getHeight() + vpo.getHitbox().y/2 + .1f;
                }
                speed.y = 0;
            }
            vpo.setSpeed(speed);
            vpo.setPos(pos);
        }
    }

    public void checkCollisions(float delta) {
        for (int j = 0; j < physobjects.size(); j++) {
            for (int i = 0; i < obstacles.length; i++) {
                rectangleCollision(physobjects.get(j), obstacles[i], delta);
            }
        }

        //With other objects
        for (int i = 0; i < physobjects.size(); i++) {
            for (int j = i; j < physobjects.size(); j++) {
                if (i == j) continue;
                VisualPhysObject p1 = physobjects.get(i);
                VisualPhysObject p2 = physobjects.get(j);
                if (Intersector.overlaps(toRectangle(p1), toRectangle(p2))) {
                    p1.onCollision(p2);
                    p2.onCollision(p1);
                }
            }
        }
    }

    public void update(float delta) {
        //check physics collisions
        checkCollisions(delta);
    }

    public void addObject(VisualPhysObject obj) {
        physobjects.add(obj);
    }

    public ObjectManager() {
        physobjects = new ArrayList<>();
        obstacles = new Rectangle[] {};
    }
}
