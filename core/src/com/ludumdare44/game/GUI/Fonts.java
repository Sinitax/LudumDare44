package com.ludumdare44.game.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Fonts {

    protected static final FileHandle FONT_DEFAULT = Gdx.files.internal("assets/fonts/Minimal5x7.ttf");
    protected static final FileHandle FONT_SMALL = Gdx.files.internal("assets/fonts/Kenney Mini Square.ttf");

    public static BitmapFont createDefaultPixelFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(FONT_DEFAULT);
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 16;
        BitmapFont bitmapFont = generator.generateFont(parameter);
        generator.dispose();

        return bitmapFont;
    }

    public static BitmapFont createHeaderPixelFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(FONT_DEFAULT);
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 16*2;
        parameter.borderStraight = true;
        parameter.borderWidth = 1;
        parameter.shadowColor = new Color(0, 0, 0, 0.4f);
        parameter.shadowOffsetX = 2;
        parameter.shadowOffsetY = 2;
        BitmapFont bitmapFont = generator.generateFont(parameter);
        generator.dispose();

        return bitmapFont;
    }

    public static BitmapFont createSmallPixelFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(FONT_SMALL);
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 8;
        BitmapFont bitmapFont = generator.generateFont(parameter);
        generator.dispose();

        return bitmapFont;
    }
}
