package classes.world;

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

public class World implements ISimulate {

    private ArrayList<Location> locations;
    private ArrayList<Animal> animals;
    private ArrayList<Plant> plants;

    private int width;
    private int height;

    public World(String path) {
        locations = new ArrayList<Location>();
        animals = new ArrayList<Animal>();
        plants = new ArrayList<Plant>();


        File file = new File(path);
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch(IOException exception) {
            return;
        }

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
                locations.add(location);
            }
        }
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public ArrayList<Location> getNeighbouringLocations() {
        throw new NotImplementedException();
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public void simulate() {

    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    public void addPlant(Plant plant) {
        plants.add(plant);
    }

    public void removePlant(Plant plant) {
        plants.remove(plant);
    }

}
