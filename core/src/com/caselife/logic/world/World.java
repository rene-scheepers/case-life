package com.caselife.logic.world;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.caselife.logic.world.pathfinding.AStarPathfinder;
import com.caselife.logic.life.*;

import java.io.Serializable;
import java.util.ArrayList;

public class World implements Serializable, ISimulate {

    private ArrayList<Life> lives;

    private Node[][] nodes;

    private int width;
    private int height;
    private AStarPathfinder pathfinder;

    private World(Node[][] nodes) {
        lives = new ArrayList();

        this.nodes = nodes;

        width = nodes.length;
        height = nodes[0].length;

        pathfinder = new AStarPathfinder();
    }

    public AStarPathfinder getPathfinder() {
        return pathfinder;
    }

    public static World instantiateWorldFromImage(Texture texture) {
        TextureData textureData = texture.getTextureData();
        textureData.prepare();
        Pixmap pixelMap = textureData.consumePixmap();

        int width = texture.getWidth();
        int height = texture.getHeight();
        Node[][] nodes = new Node[width][height];
        World world = new World(nodes);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Life life = null;

                // Wrap alpha byte from last to first. Then mask it of the value.
                String hex = String.format("#%06x", pixelMap.getPixel(x, y) >> 8 & 0xFFFFFF);

                // Get type of node.
                LocationType type;
                switch (hex) {
                    case "#000000":
                        type = LocationType.Water;
                        break;
                    case "#ed1c1c":
                        type = LocationType.Obstacle;
                        break;
                    default:
                        type = LocationType.Land;
                        break;
                }

                // Get life on node.
                switch (hex) {
                    case "#00ff00":
                        // Plant
                        life = new Plant(world, 100);
                        break;
                    case "#ff6a00":
                        // MALE Carnivore
                        //life = new Animal(world, world.getPathfinder(), new Genetics("T-REX", Digestion.Carnivore, 4, 95, 400, 100, 90), Gender.Male);
                        break;
                    case "#0026ff":
                        // MALE Omnivore
                        life = new Animal(world, world.getPathfinder(), new Genetics("OTHER", Digestion.Omnivore, 4, 95, 400, 100, 90), Gender.Male);
                        break;
                    case "#ff00ff":
                        // MALE Herbivore
                        life = new Animal(world, world.getPathfinder(), new Genetics("Hond", Digestion.Herbivore, 4, 95, 400, 100, 90), Gender.Male);
                        break;
                    case "something":
                        break;
                    case "something_else":
                        break;
                    case "#ee00ff":
                        life = new Animal(world, world.getPathfinder(), new Genetics("Hond", Digestion.Herbivore, 4, 95, 400, 100, 90), Gender.Female);
                        break;
                }

                // Add node.
                world.setNode(x, y, type);

                // Add life.
                if (life != null) {
                    world.addLife(life, world.getNode(x, y));
                }
            }
        }

        return world;
    }

    public Node[][] getNodes() {
        return nodes;
    }

    public ArrayList<Life> getLives() {
        return lives;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Node getNodeForLife(Life life) {
        for (int x = 0; x < nodes.length; x++) {
            for (int y = 0; y < nodes[x].length; y++) {
                Node node = nodes[x][y];
                Life holder = node.getHolder();
                if (holder != null && holder.equals(life)) {
                    return node;
                }
            }
        }

        return null;
    }

    public Node getNode(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return null;
        return nodes[x][y];
    }

    public void setNode(int x, int y, LocationType type) {
        nodes[x][y] = new Node(this, x, y, type);
    }

    public boolean addLife(Life life, Node node) {
        if (node.setHolder(life)) {
            lives.add(life);
        } else {
            return false;
        }
        return true;
    }

    public void simulate() {
        for (int i = 0; i < lives.size(); i++) {
            lives.get(i).simulate();
        }
    }

    public double getDiagonalDistance(Node origin, Node target) {
        int width = Math.abs(origin.getX() - target.getX());
        int height = Math.abs(origin.getY() - target.getY());

        return Math.max(width, height);
    }

    public void removeLife(Life life) {
        lives.remove(life);
        System.out.println("Life: " + life + " died.");
    }



}
