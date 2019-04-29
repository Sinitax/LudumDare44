package com.ludumdare44.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ludumdare44.game.Constants;
import com.ludumdare44.game.LudumDareGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 600;
		config.width = 800;
		config.title = Constants.GAME_TITLE;
		config.addIcon("assets/icons/128.png", Files.FileType.Internal);
		config.addIcon("assets/icons/32.png", Files.FileType.Internal);
		config.addIcon("assets/icons/16.png", Files.FileType.Internal);
		new LwjglApplication(new LudumDareGame(), config);
	}
}
