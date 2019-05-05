package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.SuperMario;

public class DesktopLauncher {

	public static boolean fullscreen;

	public static void main (String[] arg) {

		fullscreen = false ;

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		if (fullscreen){
			System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
			config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
			config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
			config.fullscreen = false;
		}

		new LwjglApplication(new SuperMario(), config);
	}
}
