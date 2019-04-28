package com.ludumdare44.game.Map;

import com.ludumdare44.game.GFX.IRenderableObject;
import com.ludumdare44.game.Physics.PhysicsObject;
import com.ludumdare44.game.Physics.VisualPhysObject;

public abstract class ObjectAdder {
    public abstract void addPhysObject(PhysicsObject o);
    public abstract void addVisObject(VisualPhysObject o);
    public abstract void addRenderable(IRenderableObject o);
}
