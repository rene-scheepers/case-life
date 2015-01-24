package com.caselife.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.caselife.game.render.orthographic.GameRenderer;
import com.caselife.logic.Simulator;
import com.caselife.logic.life.Animal;
import com.caselife.logic.life.Gender;
import com.caselife.logic.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CaseLifeGame extends ApplicationAdapter {
    private static AssetManager assets;
    private static HashMap<String, BitmapFont> fonts;

    private Renderer renderer;
    private World world;
    private Simulator simulator;
    private SpriteBatch spriteBatch;
    private Renderer renderer2d;
    private Renderer renderer3d;

    public void reloadContent() {
        if (assets != null) assets.clear();
        if (fonts != null) fonts.clear();

        loadContent();
    }

    public void loadContent() {
        if (assets == null) assets = new AssetManager();
        if (fonts == null) fonts = new HashMap<>();

        BitmapFont font = new BitmapFont();
        font.setColor(Color.BLACK);
        fonts.put("default", font);

        assets.load("maps/small.png", Texture.class);
        assets.load("tiles/plant.png", Texture.class);
        assets.load("tiles/herbivore.png", Texture.class);
        assets.load("tiles/land.png", Texture.class);
        assets.load("tiles/water.png", Texture.class);
        assets.load("tiles/obstacle.png", Texture.class);

        assets.finishLoading();
    }

    @Override
    public void create() {
        GLProfiler.enable();
        loadContent();
        Texture texture = assets.get("maps/small.png");

        world = World.instantiateWorldFromImage(texture);
        simulator = new Simulator(world);
        simulator.setSpeed(20);
        simulator.start();

        spriteBatch = new SpriteBatch();

        renderer3d = new com.caselife.game.render.perspective.GameRenderer(world, simulator);
        renderer = renderer2d = new GameRenderer(world, simulator);
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            renderer = renderer2d;
            renderer.activate();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            renderer = renderer3d;
            renderer.activate();
        }

        GLProfiler.reset();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 0);

        if (renderer != null) {
            renderer.render();
        }

        renderDebug();
    }

    private void renderDebug() {
        StringBuilder debugText = new StringBuilder();
        debugText.append(String.format("FPS: %s\r\nVertices: %s\r\nDraw calls: %s", Gdx.graphics.getFramesPerSecond(), GLProfiler.vertexCount.total, GLProfiler.drawCalls));

        if (renderer instanceof GameRenderer) {
            debugText.append("\r\n2d Rendering");
        } else if (renderer instanceof com.caselife.game.render.perspective.GameRenderer) {
            debugText.append("\r\n3d Rendering");
        }

        // Get all animals.
        List<Animal> animals = world.getLives().stream().filter(x -> x instanceof Animal).map(y -> (Animal) y).collect(Collectors.toList());

        int males = (int) animals.stream().filter(x -> x.getGender().equals(Gender.Male)).count();
        int females = animals.size() - males;
        String simulationText = String.format("Turn: %s\r\nAnimals: %s\r\nMale: %s\r\nFemale: %s\r\nDied: %s", simulator.getCurrentTurn(), animals.size(), males, females, world.getAnimalsKIA());

        spriteBatch.begin();
        fonts.get("default").drawMultiLine(spriteBatch, debugText, 10, 75);
        fonts.get("default").drawMultiLine(spriteBatch, simulationText, 225, 90);
        spriteBatch.end();
    }

    public static AssetManager getAssets() {
        return assets;
    }

    public static HashMap<String, BitmapFont> getFonts() { return fonts; }
}
