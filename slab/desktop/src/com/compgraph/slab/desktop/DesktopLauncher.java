package com.compgraph.slab.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.compgraph.slab.slab;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "lab2";
		config.useGL30 = false;
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new slab(), config);
	}
}
