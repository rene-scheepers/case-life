package com.caselife.game.desktop;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.caselife.game.CaseLifeGame;
import org.lwjgl.opengl.*;

import java.awt.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 1920;
        config.height = 1080;
        config.backgroundFPS = 30;
        config.foregroundFPS = 60;
        config.title = "Super awesome simulation shizzle";

		new LwjglApplication(new CaseLifeGame(), config);
	}
}
