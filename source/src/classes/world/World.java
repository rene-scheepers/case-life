package classes.world;

import classes.exceptions.LocationAlreadyOccupiedException;

import classes.life.Digestion;
import classes.life.Gender;
import classes.ISimulate;
import classes.life.Animal;
import classes.life.Genetics;
import classes.life.Life;
import classes.life.Plant;
import classes.world.pathfinding.AStarPathfinder;
import javafx.scene.canvas.*;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

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

        for (int x = 0; x < nodes.length; x++) {
            for (int y = 0; y < nodes[x].length; y++) {
                Node node = nodes[x][y];

                node.addAdjacentNode(getNode(x - 1, y + 1));
                node.addAdjacentNode(getNode(x, y + 1));
                node.addAdjacentNode(getNode(x + 1, y + 1));

                node.addAdjacentNode(getNode(x - 1, y));
                node.addAdjacentNode(getNode(x + 1, y));

                node.addAdjacentNode(getNode(x - 1, y - 1));
                node.addAdjacentNode(getNode(x, y - 1));
                node.addAdjacentNode(getNode(x + 1, y - 1));
            }
        }

        pathfinder = new AStarPathfinder();
    }

    public AStarPathfinder getPathfinder() {
        return pathfinder;
    }

    public static World instantiateWorldFromImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Node[][] nodes = new Node[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                java.awt.Color color = new java.awt.Color(image.getRGB(x, y));
                String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());

                LocationType type;
                if (hex.equals("#000000")) {
                    type = LocationType.Water;
                } else if (hex.equals("#ed1c1c")) {
                    type = LocationType.Obstacle;
                } else {
                    type = LocationType.Land;
                }

                nodes[x][y] = new Node(x, y, type);
            }
        }

        World world = new World(nodes);

        Random random = new Random();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                java.awt.Color color = new java.awt.Color(image.getRGB(x, y));
                String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());

                Life life = null;
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

                if (life != null) {
                    try {
                        world.addLife(life, nodes[x][y]);
                    } catch (LocationAlreadyOccupiedException exception) {

                    }
                }
            }
        }

        return world;
    }

    public Node getNode(int x, int y) {
        x = x % width;
        y = y % height;

        while (x < 0) {
            x += width;
        }

        while (y < 0) {
            y += height;
        }

        return nodes[x][y];
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

    public void addLife(Life life, Node node) throws LocationAlreadyOccupiedException {
        lives.add(life);
        node.setHolder(life);
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
        System.out.println("LIFE LOST ITS LIVE IN LIFE");
        lives.remove(life);
    }



}
