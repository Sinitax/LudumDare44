package com.ludumdare44.game.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ludumdare44.game.Characters.Player;
import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.GFX.IRenderable;

public class HUD implements IRenderable {
    //private static Texture statusBarSprite = new Texture("assets/hud/status_bar.png");
    private Player p;

    public void render(GFXManager gfx) {
        /*//Player Stat Bar
        Sprite sprite = new Sprite(statusBarSprite);
        sprite.setSize(sprite.getWidth() * 2, sprite.getHeight() * 2);
        sprite.setPosition(30,gfx.screenSize.y - 100);
        sprite.draw(gfx.batch);

        //add stats
        Pixmap pixmap = new Pixmap(200, 40, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLUE);
        pixmap.fillRectangle(0 , 15, (int) (this.p.getEnergy() * 200/this.p.getEnergyMax()), 10);
        gfx.batch.draw(new Texture(pixmap), (int) sprite.getX() + 90, (int) sprite.getY() + 20);
        pixmap.dispose();

        //add icon
        sprite = new Sprite(this.p.getIcon());
        sprite.setSize(65, 65);
        sprite.setPosition(39, gfx.screenSize.y - 94);
        sprite.draw(gfx.batch);*/
    }
    
    public HUD(Player _p) {
        this.p = _p;
    }
}
