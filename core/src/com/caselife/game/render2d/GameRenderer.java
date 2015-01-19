package com.caselife.game.render2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Matrix4;
import com.caselife.game.Renderer;
import com.caselife.game.render2d.camera.TopDownCameraInputController;
import com.caselife.logic.Simulator;
import com.caselife.logic.life.Animal;
import com.caselife.logic.life.Life;
import com.caselife.logic.life.Plant;
import com.caselife.logic.world.LocationType;
import com.caselife.logic.world.Node;
import com.caselife.logic.world.World;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class GameRenderer implements Renderer {

    private TopDownCameraInputController cameraInputController;
    private SpriteBatch spriteBatch;
    private World world;
    private AssetManager assetManager;
    public OrthographicCamera camera;
    private TiledMap tiledMap;
    private MapRenderer renderer;
    private TiledMapTileLayer lifeLayer;

    public GameRenderer(World world, Simulator simulator) {
        spriteBatch = new SpriteBatch();
        this.world = world;
        this.assetManager = new AssetManager();
        tiledMap = new TiledMap();
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        cameraInputController = new TopDownCameraInputController(camera);

        Gdx.input.setInputProcessor(cameraInputController);

        lifeLayer = getLifeLayer();

        tiledMap.getLayers().add(getStaticLayer());
        tiledMap.getLayers().add(lifeLayer);
    }

    private TiledMapTileLayer getLifeLayer() {
        lifeLayer = new TiledMapTileLayer(world.getWidth(), world.getHeight(), 32, 32);

        Texture texturePlant = new Texture("/home/rene/Documents/Repositories/case-life/core/assets/tiles/plant.png");
        Texture textureHerbivore = new Texture("/home/rene/Documents/Repositories/case-life/core/assets/tiles/herbivore.png");

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

        Texture textureLand = new Texture("/home/rene/Documents/Repositories/case-life/core/assets/tiles/land.png");
        Texture textureWater = new Texture("/home/rene/Documents/Repositories/case-life/core/assets/tiles/water.png");
        Texture textureObstacle = new Texture("/home/rene/Documents/Repositories/case-life/core/assets/tiles/obstacle.png");
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
