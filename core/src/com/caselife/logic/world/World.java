package com.caselife.logic.world;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.caselife.logic.world.pathfinding.AStarPathfinder;
import com.caselife.logic.life.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class World implements Serializable, ISimulate {

    private ArrayList<Life> lives;

    private int animalsKIA = 0;
    private Nodes nodes;

    private int width;
    private int height;
    private AStarPathfinder pathfinder;

    private World() {
        lives = new ArrayList<>();
        pathfinder = new AStarPathfinder();
    }

    private World(int width, int height) {
        this();
        nodes = new Nodes(width, height);
        this.width = width;
        this.height = height;
    }

    private World(Node[][] nodes) {
        this();

        this.nodes = new Nodes(nodes);
        width = nodes.length;
        height = nodes[0].length;
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
        World world = new World(width, height);

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
                        life = new Animal(world, world.getPathfinder(), new Genetics("T-REX", Digestion.Carnivore, 4, 95, 400, 100, 90), Gender.Male);
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

    public Nodes getNodes() {
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
        for (int x = 0; x < nodes.length(); x++) {
            for (int y = 0; y < nodes.length(x); y++) {
                Node node = nodes.get(x, y);
                Life holder = node.getHolder();
                if (holder != null && holder.equals(life))
                    return node;

            }
        }
        return null;
    }

    /**
     * Returns the node at the x and y coordinates. If out of bounds, it wraps around and returns that node.
     *
     * @param x horizontal coordinate.
     * @param y vertical coordinate.
     * @return The node at the x and y coordinate. Wraps around if out of bounds.
     */
    public Node getNode(int x, int y) {
        // Example: x = -201, width = 100
        // -201 % 100 = -1
        // -1 + 100 = -99
        // -99 % 100 = -99

        // OR: x = 215, width = 100
        // 215 % 100 = 15
        // 15 + 100 = 115
        // 115 % 100 = 15

        x = (x % width + width) % width;
        y = (y % height + height) % height;

        return nodes.get(x, y);
    }

    public void setNode(int x, int y, LocationType type) {
        nodes.set(x, y, new Node(this, x, y, type));
    }

    public boolean addLife(Life life, Node node) {
        if (node.setHolder(life)) {
            System.out.println("NEW LIFE: " + life);
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
        animalsKIA++;
        System.out.println(String.format("DIED: %s", life));
    }

    public int getAnimalsKIA() {
        return animalsKIA;
    }
}
