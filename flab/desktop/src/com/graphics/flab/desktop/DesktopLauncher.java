package com.graphics.flab.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.graphics.flab.flab;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "lab1";
		config.useGL30 = false;
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new flab(), config);
	}
}
