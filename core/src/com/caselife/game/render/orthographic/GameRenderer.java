package com.caselife.game.render.orthographic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.caselife.game.CaseLifeGame;
import com.caselife.game.Renderer;
import com.caselife.game.render.orthographic.camera.TopDownCamera;
import com.caselife.game.render.orthographic.camera.TopDownCameraInputController;
import com.caselife.logic.Simulator;
import com.caselife.logic.life.Animal;
import com.caselife.logic.life.Life;
import com.caselife.logic.life.Plant;
import com.caselife.logic.world.LocationType;
import com.caselife.logic.world.Node;
import com.caselife.logic.world.World;

import java.util.ArrayList;

public class GameRenderer implements Renderer {

    private TopDownCameraInputController cameraInputController;
    private SpriteBatch spriteBatch;
    private World world;
    private TopDownCamera camera;
    private TiledMap tiledMap;
    private MapRenderer renderer;
    private TiledMapTileLayer lifeLayer;

    public GameRenderer(World world, Simulator simulator) {
        spriteBatch = new SpriteBatch();
        this.world = world;
        tiledMap = new TiledMap();
        renderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Camera.
        camera = new TopDownCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), world, 32);
        cameraInputController = new TopDownCameraInputController(camera);
        Gdx.input.setInputProcessor(cameraInputController);

        // Content loading.
        CaseLifeGame.getAssets().load("tiles/plant.png", Texture.class);
        CaseLifeGame.getAssets().load("tiles/herbivore.png", Texture.class);
        CaseLifeGame.getAssets().load("tiles/land.png", Texture.class);
        CaseLifeGame.getAssets().load("tiles/water.png", Texture.class);
        CaseLifeGame.getAssets().load("tiles/obstacle.png", Texture.class);
        CaseLifeGame.getAssets().finishLoading();

        // Initializing.
        lifeLayer = getLifeLayer();
        tiledMap.getLayers().add(getStaticLayer());
        tiledMap.getLayers().add(lifeLayer);
    }

    private TiledMapTileLayer getLifeLayer() {
        lifeLayer = new TiledMapTileLayer(world.getWidth(), world.getHeight(), 32, 32);

        Texture texturePlant = CaseLifeGame.getAssets().get("tiles/plant.png");
        Texture textureHerbivore = CaseLifeGame.getAssets().get("tiles/herbivore.png");

        ArrayList<Life> lives = (ArrayList<Life>) world.getLives().clone();
        for (Life life : lives) {
            Node node = life.getNode();

            TiledMapTile tile = null;
            if (life instanceof Animal) {
                tile = new StaticTiledMapTile(new TextureRegion(textureHerbivore));
            } else if (life instanceof Plant) {
                tile = new StaticTiledMapTile(new TextureRegion(texturePlant));
            }

            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            cell.setTile(tile);
            lifeLayer.setCell(node.getX(), node.getY(), cell);
        }

        return lifeLayer;
    }


    private TiledMapTileLayer getStaticLayer() {
        TiledMapTileLayer layer = new TiledMapTileLayer(world.getWidth(), world.getHeight(), 32, 32);

        Texture textureLand = CaseLifeGame.getAssets().get("tiles/land.png");
        Texture textureWater = CaseLifeGame.getAssets().get("tiles/water.png");
        Texture textureObstacle = CaseLifeGame.getAssets().get("tiles/obstacle.png");
        spriteBatch.begin();
        Node[][] nodes = world.getNodes();
        for (int x = 0; x < nodes.length; x++) {
            for (int y = 0; y < nodes[x].length; y++) {
                Node node = nodes[x][y];

                Texture texture;
                if (node.getLocationType().equals(LocationType.Land)) {
                    texture = textureLand;
                } else if (node.getLocationType().equals(LocationType.Obstacle)) {
                    texture = textureObstacle;
                } else {
                    texture = textureWater;
                }

                TiledMapTile tile = new StaticTiledMapTile(new TextureRegion(texture));

                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(tile);
                layer.setCell(x, y, cell);
            }
        }

        return layer;
    }

    @Override
    public void activate() {
        Gdx.input.setInputProcessor(cameraInputController);
    }

    @Override
    public void render() {
        cameraInputController.update();

        tiledMap.getLayers().remove(lifeLayer);
        getLifeLayer();
        tiledMap.getLayers().add(lifeLayer);
        renderer.setView(camera);
        renderer.render();
    }
}
