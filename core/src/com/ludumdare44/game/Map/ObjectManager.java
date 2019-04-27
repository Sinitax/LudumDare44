package com.ludumdare44.game.Map;

import com.badlogic.gdx.math.*;
import com.ludumdare44.game.Characters.Player;
import com.ludumdare44.game.Physics.Grapple;
import com.ludumdare44.game.Physics.Obstacle;
import com.ludumdare44.game.Physics.PhysicsObject;
import com.ludumdare44.game.Physics.VisualPhysObject;
import com.sun.org.apache.regexp.internal.RE;

import java.util.ArrayList;

public class ObjectManager {
    private ArrayList<PhysicsObject> physobjects;

    public ArrayList<PhysicsObject> getObjects() { return physobjects; }

    public void setObstacles(Obstacle[] obstacles) {
        for (Obstacle o : obstacles) {
            physobjects.add(o);
        }
    }

    public static Rectangle toRectangle(PhysicsObject vpo) {
        return new Rectangle(vpo.getPos().x + vpo.getHitboxOffset().x - vpo.getHitbox().x/2, vpo.getPos().y + vpo.getHitboxOffset().y - vpo.getHitbox().y/2, vpo.getHitbox().x, vpo.getHitbox().y);
    }

    public static void rectangleCollision(PhysicsObject vpo, Rectangle ro, float delta) {
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
        /*
        for (int j = 0; j < physobjects.size(); j++) {
            for (int i = 0; i < obstacles.length; i++) {
                rectangleCollision(physobjects.get(j), obstacles[i], delta);
            }
        }
         */

        ArrayList<PhysicsObject[]> collisionPairs = new ArrayList<>();
        //With other objects
        for (int i = 0; i < physobjects.size(); i++) {
            for (int j = i; j < physobjects.size(); j++) {
                if (i == j) continue;
                PhysicsObject p1 = physobjects.get(i);
                PhysicsObject p2 = physobjects.get(j);
                if (Intersector.overlaps(toRectangle(p1), toRectangle(p2))) {
                    if (p1 instanceof Obstacle && collisionPairs.indexOf(new PhysicsObject[] {p2, p1}) == -1) {
                        collisionPairs.add(new PhysicsObject[] {p1, p2});
                    }
                    p1.onCollision(p2);
                }
            }
        }

        for (PhysicsObject[] pair : collisionPairs) {
            rectangleCollision(pair[1], toRectangle(pair[0]), delta);
        }
    }

    public void update(float delta) {
        //check physics collisions
        ArrayList<PhysicsObject> deleteList = new ArrayList<>();
        for (int i = 0; i < physobjects.size(); i++) {
            PhysicsObject obj = physobjects.get(i);
            if (obj instanceof VisualPhysObject) {
                VisualPhysObject vobj = (VisualPhysObject) obj;
                if (!vobj.stagnant()) vobj.update(delta);
                if (!vobj.alive()) deleteList.add(obj);
            }
        }
        for (PhysicsObject o: deleteList) {
            physobjects.remove(o);
        }
        checkCollisions(delta);
    }

    public void addObject(PhysicsObject obj) {
        physobjects.add(obj);
    }

    public ObjectManager() {
        physobjects = new ArrayList<>();
    }
}
