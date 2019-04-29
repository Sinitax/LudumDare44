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
        return new Rectangle(vpo.getPos().x + vpo.getHitboxOffset().x - vpo.getHitbox().x / 2, vpo.getPos().y + vpo.getHitboxOffset().y - vpo.getHitbox().y / 2, vpo.getHitbox().x, vpo.getHitbox().y);
    }

    public static void rectangleCollision(PhysicsObject vpo, Rectangle ro, float delta) {
        Vector2 nSpeed = vpo.getSpeed();
        Vector2 hitboxPos = vpo.getPos().add(vpo.getHitboxOffset());
        boolean vertOverlap = hitboxPos.y - vpo.getHitbox().y / 2 < ro.getY() + ro.getHeight() && hitboxPos.y + vpo.getHitbox().y / 2 > ro.getY();
        boolean horzOverlap = hitboxPos.x - vpo.getHitbox().x / 2 < ro.getX() + ro.getWidth() && hitboxPos.x + vpo.getHitbox().x / 2 > ro.getX();

        if (vertOverlap) nSpeed.y = 0;
        if (horzOverlap) nSpeed.x = 0;

        Vector2 vel = vpo.getSpeed().nor().scl(-1);
        for (int i = 0; i < 100 && ro.overlaps(toRectangle(vpo)); i++) {
            vpo.setPos(vpo.getPos().add(vel.cpy().scl(3f)));
        }

        vpo.setSpeed(nSpeed);
        vpo.setFspeed(nSpeed);
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
        // check physics collisions
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
