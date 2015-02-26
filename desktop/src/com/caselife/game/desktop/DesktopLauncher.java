package com.caselife.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.caselife.game.CaseLifeGame;

import java.awt.*;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        // Window.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        config.title = "Super awesome simulation shizzle";
        config.width = (int) (screenSize.getWidth() * 0.8);
        config.height = (int) (screenSize.getHeight() * 0.8);

        // FPS.
        config.backgroundFPS = 30;
        config.foregroundFPS = 60;
        //config.vSyncEnabled = false;

        // Anti-aliasing.
        config.samples = 8;

        config.fullscreen = false;

        new LwjglApplication(new CaseLifeGame(), config);
    }
}
