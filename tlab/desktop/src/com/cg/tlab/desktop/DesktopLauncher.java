package com.cg.tlab.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cg.tlab.tlab;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "lab3";
		config.useGL30 = false;
		config.width = 800;
		config.height = 600;
		new LwjglApplication(new tlab(), config);
	}
}
