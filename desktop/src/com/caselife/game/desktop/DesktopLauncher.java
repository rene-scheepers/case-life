package com.caselife.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.caselife.game.CaseLifeGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        // Window.
        config.title = "Super awesome simulation shizzle";
        config.width = 1920;
        config.height = 1080;

        // FPS.
        config.backgroundFPS = 30;
        config.foregroundFPS = 60;

        // Anti-aliasing.
        config.samples = 8;

        config.fullscreen = false;

        new LwjglApplication(new CaseLifeGame(), config);
    }
}
