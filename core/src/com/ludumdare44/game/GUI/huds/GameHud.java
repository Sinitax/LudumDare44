package com.ludumdare44.game.GUI.huds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import com.ludumdare44.game.Characters.Player;
import com.ludumdare44.game.GUI.Fonts;
import com.ludumdare44.game.GUI.Gui;

public class GameHud extends Gui {

    protected final Texture hudTexture = new Texture("assets/gui/hud.png");
    protected final BitmapFont font = Fonts.createDefaultPixelFont();
    protected final Player player;

    public GameHud(Player player) {
        this.player = player;
    }

    @Override
    protected void createGui(int width, int height) {
        //add(new ElementLabel())
    }

    @Override
    protected void update(float delta) {

    }

    @Override
    protected void postRender(float delta) {
        gfx.batch.begin();

        int soulsX = 4;
        int soulsY = height-hudTexture.getHeight()-4;
        gfx.batch.draw(hudTexture, soulsX, soulsY);
        font.draw(gfx.batch, ""+player.getSouls(), soulsX+25, soulsY+hudTexture.getHeight()-8, 10, Align.center, false);

        gfx.batch.end();
    }
}
