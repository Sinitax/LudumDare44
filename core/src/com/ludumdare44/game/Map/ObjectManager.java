package com.ludumdare44.game.Map;

import com.badlogic.gdx.math.*;
import com.ludumdare44.game.Physics.Obstacle;
import com.ludumdare44.game.Physics.PhysicsObject;
import com.ludumdare44.game.Physics.VisualPhysObject;

import java.util.ArrayList;
import java.util.Collections;

public class ObjectManager {
    private ArrayList<PhysicsObject> physobjects;

    public ArrayList<PhysicsObject> getObjects() { return physobjects; }

    public void setObstacles(Obstacle[] obstacles) {
        Collections.addAll(physobjects, obstacles);
    }

    public static Rectangle toRectangle(PhysicsObject vpo) {
        return new Rectangle(vpo.getPos().x + vpo.getHitboxOffset().x - vpo.getHitbox().x/2, vpo.getPos().y + vpo.getHitboxOffset().y - vpo.getHitbox().y/2, vpo.getHitbox().x, vpo.getHitbox().y);
    }

    public static void rectangleCollision(PhysicsObject vpo, Rectangle ro, float delta) {
        Rectangle r = ObjectManager.toRectangle(vpo);
        Vector2 ppos = vpo.getPos().sub(vpo.getSpeed());
        Rectangle pr = new Rectangle(ppos.x, ppos.y, vpo.getHitbox().x, vpo.getHitbox().y);
        Vector2 npos = vpo.getPos();
        Vector2 nspeed = vpo.getSpeed();
        if (r.overlaps(ro)) {
            boolean vertical = r.y + r.getHeight() > ro.getY() || r.y < ro.getY() + ro.getHeight();
            boolean horizontal = r.x + r.getWidth() < ro.getX() || r.x > ro.getX() + ro.getWidth();
            if (horizontal) {
                if (pr.x + pr.getWidth() < ro.getX()) {
                    npos.x = ro.getX() - r.getWidth() / 2 - .1f;
                } else if (pr.x > ro.getX() + ro.getWidth()) {
                    npos.x = ro.getX() + ro.getWidth() + r.getWidth() / 2 + .1f;
                }
                nspeed.x = 0;
            }
            if (vertical) {
                if (pr.y + pr.getHeight() < ro.getY()) {
                    npos.y = ro.getY() - r.getHeight() / 2 - .1f;
                } else if (pr.y > ro.getY() + ro.getHeight()) {
                    npos.y = ro.getY() + ro.getHeight() + r.getHeight() / 2 + .1f;
                }
                nspeed.y = 0;
            }
            vpo.setSpeed(nspeed);
            vpo.setFspeed(new Vector2(0, 0));
            vpo.setPos(npos);
        }
    }

    private void checkCollisions(float delta) {
        //With other objects
        for (int i = 0; i < physobjects.size(); i++) {
            for (int j = 0; j < physobjects.size(); j++) {
                if (i == j) continue;
                PhysicsObject p1 = physobjects.get(i);
                PhysicsObject p2 = physobjects.get(j);
                if (Intersector.overlaps(toRectangle(p1), toRectangle(p2))) {
                    p1.onCollision(p2, delta);
                }
            }
        }
    }

    public void update(float delta) {
        //check physics collisions
        ArrayList<PhysicsObject> deleteList = new ArrayList<>();
        for (PhysicsObject obj : physobjects) {
            if (obj instanceof VisualPhysObject) {
                VisualPhysObject vobj = (VisualPhysObject) obj;
                if (!vobj.isStagnant()) vobj.update(delta);
                if (vobj.isDestroyed()) deleteList.add(obj);
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
