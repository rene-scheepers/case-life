package classes.world;

import classes.enumerations.Direction;
import classes.enumerations.LocationType;
import classes.interfaces.ISimulate;
import classes.life.Animal;
import classes.life.Plant;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class World implements ISimulate {

    private ArrayList<Object> objects;

    private HashMap<String, Location> locations = new HashMap<>();

    private int width;
    private int height;

    public World(BufferedImage image) {
        objects = new ArrayList<Object>();

        width = image.getWidth();
        height = image.getHeight();

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

                Location location = new Location(this, x, y, type);
                locations.put(String.format("%s,%s", x, y), location);
            }
        }
    }

    public Location getLocation(int x, int y) {
        if (x >= width) {
            x = x % width;
        }

        if (y >= height) {
            y = y % height;
        }

        return locations.get(String.format("%s,%s", x, y));
    }

    public ArrayList<Location> getLocations() {
        return new ArrayList<Location>(locations.values());
    }

    public ArrayList<Object> getObjects() {
        return objects;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public HashMap<Direction, Location> getNeighbouringLocations(Location location) {
        HashMap<Direction, Location> neighbouringLocations = new HashMap<>();

        int x = location.getX();
        int y = location.getY();

        neighbouringLocations.put(Direction.NorthEast, getLocation(x + -1, y + 1));
        neighbouringLocations.put(Direction.North, getLocation(x + 0, y + 1));
        neighbouringLocations.put(Direction.NorthWest, getLocation(x + 1, y + 1));
        neighbouringLocations.put(Direction.West, getLocation(x + 1, y + 0));
        neighbouringLocations.put(Direction.SouthWest, getLocation(x + 1, y + -1));
        neighbouringLocations.put(Direction.South, getLocation(x + 0, y + -1));
        neighbouringLocations.put(Direction.SouthEast, getLocation(x + -1, y + -1));
        neighbouringLocations.put(Direction.West, getLocation(x + -1, y + 0));

        return neighbouringLocations;
    }

    public void addObject(Object object) {
        objects.add(object);
    }

    public void simulate() {
        for (Object object : objects) {
            if (object instanceof ISimulate) {
                ((ISimulate) object).simulate();
            }
        }
    }

    public double getDistanceBetweenLocations(Location location1, Location location2) {
        double firstSide = Math.abs(Math.pow(location1.getX() - location2.getX(), 2));
        double secondSize = Math.abs(Math.pow(location1.getY() - location2.getY(), 2));

        return Math.sqrt(firstSide + secondSize);
    }

    public void removeObject(Object object) {
        objects.remove(object);
    }

}
