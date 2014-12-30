package classes.world;

import classes.Exceptions.LocationAlreadyOccupiedException;
import classes.enumerations.Direction;
import classes.enumerations.LocationType;
import classes.interfaces.ISimulate;
import classes.life.Life;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class World implements ISimulate {

    private ArrayList<Life> life;

    private Node[][] nodes;

    private int width;
    private int height;

    public World(BufferedImage image) {
        life = new ArrayList();

        width = image.getWidth();
        height = image.getHeight();
        nodes = new Node[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = new Color(image.getRGB(x, y));
                String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getGreen());

                LocationType type;
                if (hex.equals("#000000")) {
                    type = LocationType.Land;
                } else if (hex.equals("#ed1c1c")) {
                    type = LocationType.Obstacle;
                } else {
                    type = LocationType.Water;

                }

                nodes[x][y] =new Node(x, y, type);
            }
        }

        for (int x = 0; x < nodes.length; x++) {
            for (int y =0; y < nodes[x].length; y++) {
                Node node = nodes[x][y];

                node.addAdjacentNode(getNode(x - 1, y + 1));
                node.addAdjacentNode(getNode(x, y + 1));
                node.addAdjacentNode(getNode(x + 1, y + 1));

                node.addAdjacentNode(getNode(x - 1,y ));
                node.addAdjacentNode(getNode(x + 1, y));

                node.addAdjacentNode(getNode(x - 1,y - 1));
                node.addAdjacentNode(getNode(x,y - 1));
                node.addAdjacentNode(getNode(x + 1,y - 1));
            }
        }

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

    public ArrayList<Life> getLife() {
        return life;
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
        this.life.add(life);
        node.setHolder(life);
    }

    public void simulate() {
        for (Life life : this.life) {
            life.simulate();
        }
    }

    public double getDistanceBetweenLocations(Node node1, Node node2) {
        double firstSide = Math.abs(Math.pow(node1.getX() - node2.getX(), 2));
        double secondSize = Math.abs(Math.pow(node1.getY() - node2.getY(), 2));

        return Math.sqrt(firstSide + secondSize);
    }

    public void removeLife(Life life) {
        this.life.remove(life);
    }

}
