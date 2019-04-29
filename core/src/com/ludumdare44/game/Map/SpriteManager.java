package com.ludumdare44.game.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.*;
import com.ludumdare44.game.Constants;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.GFX.IRenderable;
import com.ludumdare44.game.GFX.IRenderableObject;
import com.ludumdare44.game.Physics.Obstacle;
import com.ludumdare44.game.Physics.PhysicsObject;
import com.ludumdare44.game.Physics.VisualPhysObject;
import com.ludumdare44.game.GFX.CameraManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SpriteManager {
    private boolean hasMap = false;
    private CameraManager cameraManager;

    public class TileRender implements IRenderableObject {
        int x, y, zLevel, tileLayer;
        Vector2 pos, originOffset;
        TextureRegion textureRegion;

        @Override
        public void update(float delta) {}

        @Override
        public boolean isDestroyed() {
            return false;
        }

        @Override
        public boolean isVisible() {
            return tileLayers[tileLayer].getCell((int)(stile.x + x), (int) (stile.y + y)) != null;
        } //renders only existing tiles

        @Override
        public Vector2 getPos() {
            return pos;
        }

        @Override
        public Vector2 getOriginOffset() {
            return originOffset;
        }

        @Override
        public Vector2 getModelSize() {
            return new Vector2(textureRegion.getRegionWidth() * mapScale, textureRegion.getRegionHeight() * mapScale);
        }

        @Override
        public int getZ() {
            return zLevel;
        }

        @Override
        public void setZ(int z) {
            return;
        }

        @Override
        public void render(GFXManager gfx) {
            int ctileX = (int)(stile.x + x);
            int ctileY = (int)(stile.y + y);

            TiledMapTileLayer.Cell cell = tileLayers[tileLayer].getCell(ctileX, ctileY);

            if (cell == null) return;
            if (cell.getTile() instanceof AnimatedTiledMapTile) {
                textureRegion = ((AnimatedTiledMapTile) cell.getTile()).getCurrentFrame().getTextureRegion();
            } else {
                textureRegion = cell.getTile().getTextureRegion();
            }

            Vector2 midpos = new Vector2(
                textureRegion.getRegionWidth() * mapScale * .5f,
                textureRegion.getRegionHeight() * mapScale * .5f
            );

            pos = new Vector2();
            pos.x = (cell.getTile().getOffsetX() * mapScale + ctileX * mapTileSize.x);
            pos.y = (cell.getTile().getOffsetY() * mapScale + ctileY * mapTileSize.y);

            originOffset = new Vector2();
            try {
                originOffset.x = cell.getTile().getProperties().get("originx", Integer.class);
                originOffset.y = cell.getTile().getProperties().get("originy", Integer.class);
            } catch (NullPointerException e) {
                originOffset.x = 0;
                originOffset.y = -textureRegion.getRegionHeight() * mapScale/2;
            }

            gfx.batch.draw(
                    textureRegion,
                    pos.x,
                    pos.y,
                    0,
                    0,
                    textureRegion.getRegionWidth(),
                    textureRegion.getRegionHeight(),
                    mapScale,
                    mapScale,
                    cell.getRotation()
            );

            pos.x += midpos.x;
            pos.y += midpos.y;
        }

        TileRender(int _x, int _y, int _zLevel, int _tileLayer) {
            x = _x;
            y = _y;
            zLevel = _zLevel;
            tileLayer = _tileLayer;

            textureRegion = new TextureRegion();
            pos = new Vector2(0, 0);
            originOffset = new Vector2(0, 0);
        }
    }

    private ArrayList<VisualPhysObject> physobjects;
    private ArrayList<PhysicsObject> obstacles;
    private List<List<IRenderableObject>> layers;

    private TiledMap map;
    private TmxMapLoader mloader = new TmxMapLoader();
    private Vector2 mapTileCount, mapTileSize;
    private int startx, starty;
    private int backgroundZ, foregroundZ;
    private float mapScale;

    private Vector2 stile = new Vector2();
    private Vector2 msize = new Vector2();

    private TiledMapTileLayer[] tileLayers;

    public static Rectangle toRectangle(VisualPhysObject vpo) {
        return new Rectangle(vpo.getPos().x + vpo.getHitboxOffset().x - vpo.getHitbox().x/2, vpo.getPos().y + vpo.getHitboxOffset().y - vpo.getHitbox().y/2, vpo.getHitbox().x, vpo.getHitbox().y);
    }

    public void loadMap(String name) {
        hasMap = true;
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMinFilter = Texture.TextureFilter.Nearest;
        params.textureMagFilter = Texture.TextureFilter.Nearest;
        map = mloader.load(name, params);
        MapProperties prop = map.getProperties();
        mapScale = prop.get("mapscale", Float.class);
        mapTileCount = new Vector2(prop.get("width", Integer.class), prop.get("height", Integer.class));
        mapTileSize = new Vector2(prop.get("tilewidth", Integer.class) * mapScale, prop.get("tileheight", Integer.class) * mapScale);
        startx = prop.get("startx", Integer.class); // ???
        starty = prop.get("starty", Integer.class);
        backgroundZ = prop.get("backgroundZ", Integer.class);
        foregroundZ = prop.get("foregroundZ", Integer.class);

        List<Rectangle> obstaclesTemp = new ArrayList<>();

        ArrayList<TiledMapTileLayer> tileLayersTemp = new ArrayList<>();

        int max = 0;
        for (int i = 0; i < map.getLayers().size(); i++) {
            MapLayer layer = map.getLayers().get(i);
            if (layer instanceof TiledMapTileLayer) {
                tileLayersTemp.add((TiledMapTileLayer) map.getLayers().get(i));
                MapProperties mprop = map.getLayers().get(i).getProperties();
                int zlevel = mprop.get("zLevel", Integer.class);
                if (zlevel > max) max = zlevel;
            }
            for (MapObject mobj : layer.getObjects()) {
                if (mobj instanceof RectangleMapObject) {
                    Rectangle r = ((RectangleMapObject) mobj).getRectangle();
                    r.setPosition(r.x * mapScale, r.y * mapScale);
                    r.setSize(r.width * mapScale, r.height * mapScale);
                    obstaclesTemp.add(r);
                }
            }
        }
        Rectangle[] rects = obstaclesTemp.toArray(new Rectangle[obstaclesTemp.size()]);
        for (Rectangle r: rects) {
            obstacles.add(new Obstacle((int) r.x, (int) r.y, (int) r.width, (int) r.height));
        }

        layers = new ArrayList<>();
        for (int i = 0; i < max+1; i++) layers.add(new ArrayList<>());

        tileLayers = new TiledMapTileLayer[tileLayersTemp.size()];

        updateMap();

        for (int i = 0; i < tileLayersTemp.size(); i++) {
            tileLayers[i] = tileLayersTemp.get(i);
            MapProperties mprop = tileLayersTemp.get(i).getProperties();
            int zlevel = mprop.get("zLevel", Integer.class);
            buildLayer(zlevel, i);
        }
    }

    private void updateMap() {
        Vector2 sv = new Vector2(cameraManager.getPos()).sub(new Vector2(cameraManager.getScreenSize()).scl(0.5f));
        stile = new Vector2((int) Math.floor(sv.x / mapTileSize.x), (int) Math.floor(sv.y / mapTileSize.y));
        Vector2 pos = new Vector2((int) stile.x * mapTileSize.x, (int) stile.y * mapTileSize.y);
        sv.add(cameraManager.getScreenSize());
        msize = new Vector2((int) ((sv.x - pos.x) / mapTileSize.x) + 1, (int) ((sv.y - pos.y) / mapTileSize.y) + 1);
    }

    private boolean overlapsModel(IRenderableObject i1, IRenderableObject i2) {
        float i1Right = i1.getPos().x + i1.getModelSize().x/2;
        float i1Left = i1.getPos().x - i1.getModelSize().x/2;
        float i1Top = i1.getPos().y + i1.getModelSize().y/2;
        float i1Bottom = i1.getPos().y - i1.getModelSize().y/2;

        float i2Right = i2.getPos().x + i2.getModelSize().x/2;
        float i2Left = i2.getPos().x - i2.getModelSize().x/2;
        float i2Top = i2.getPos().y + i2.getModelSize().y/2;
        float i2Bottom = i2.getPos().y - i2.getModelSize().y/2;
        if (i1Left < i2Right && i1Right > i2Left && i1Top > i2Bottom && i1Bottom < i2Top) return true;
        return false;
    }

    private boolean isModelHigher(IRenderableObject i1, IRenderableObject i2) {
        if (i1.getOriginOffset().y + i1.getPos().y > i2.getOriginOffset().y + i2.getPos().y) return true;
        else return false;
    }

    private void buildLayer(int zLevel, int tileLayer) {
        for (int i = 0; i < msize.x + 2; i++) {
            for (int j = 0; j < msize.y + 2; j++) {
                addObject(
                        new TileRender(
                                i,
                                j,
                                zLevel,
                                tileLayer
                        )
                );
            }
        }
    }

    public void render(GFXManager gfx) {
        ArrayList<IRenderableObject> deleteList = new ArrayList<>();
        for (int i = 0; i < layers.size(); i++) {
            for (int j = 0; j < layers.get(i).size(); j++){
                IRenderableObject obj = layers.get(i).get(j);
                if (obj.isVisible()) obj.render(gfx);
                if (obj.isDestroyed()) deleteList.add(obj);
            }
        }

        for (int i = 0; i < layers.size(); i++) {
            for (IRenderable obj : deleteList) {
                if (layers.get(i).indexOf(obj) != -1) layers.get(i).remove(obj);
            }
        }
    }

    public void addObject(IRenderableObject obj) {
        if (obj.getZ() == -1) obj.setZ(backgroundZ + 1);
        layers.get(obj.getZ()).add(obj);
        if (obj instanceof VisualPhysObject) physobjects.add((VisualPhysObject) obj);
    }

    public int getBackgroundZ() {
        return backgroundZ;
    }

    public int getForegroundZ() {
        return foregroundZ;
    }

    public void update(float delta) {
        for (int i = 0; i < layers.size(); i++) {
            for (int j = 0; j < layers.get(i).size(); j++) {
                //enemies?
            }
        }
        //check if visual collisions with player
        for (int i = 0; i < layers.size(); i++) {
            for (int j = 0; j < layers.get(i).size(); j++) {
                /*
                IRenderable ob = layers.get(i).get(j);
                if (overlapsModel(obj, player)) {
                    if (isModelHigher(item, player)) {
                        if (player.getZ() < item.getZ()) player.setZ(item.getZ());
                    } else if (player.getZ() > item.getZ()) {
                        player.setZ(item.getZ()-1);
                    }
                }
                 */
            }
        }
        for (int i = 0; i < physobjects.size(); i++) {
            VisualPhysObject obj = physobjects.get(i);
            if (obj.getZ() <= backgroundZ) obj.setZ(backgroundZ + 1); //above background
            if (obj.getZ() >= foregroundZ) obj.setZ(foregroundZ - 1); //under ceiling
        }
        for (int i = backgroundZ; i < foregroundZ; i++) {
            Collections.sort(layers.get(i), Comparator.comparingInt(o -> (int) -(o.getPos().y + o.getOriginOffset().y)));
        }

        //update map vars to positon
        if (hasMap) updateMap();
    }

    public PhysicsObject[] getObstacles() { // TODO: turn into physObjects
        return obstacles.toArray(new PhysicsObject[] {});
    }

    public void createLayers(int layerCount) {
        hasMap = false;
        layers = new ArrayList<>(layerCount);
        for (int i = 0; i < layerCount; i++) layers.add(new ArrayList<>());
    }

    public SpriteManager(CameraManager _cameraManager) {
        cameraManager = _cameraManager;
        physobjects = new ArrayList<>();
    }
}
