package classes.world;

import classes.life.Animal;
import classes.life.Plant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Rene on 23-12-2014.
 */
public class World {

    private ArrayList<Location> locations;
    private ArrayList<Animal> animals;
    private ArrayList<Plant> plants;

    private int width;
    private int height;

    public World() {
        this.locations = new ArrayList<Location>();
        this.animals = new ArrayList<Animal>();
        this.plants = new ArrayList<Plant>();

        this.width = 100;
        this.height = 100;

        Random random = new Random();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Type type;

                if (random.nextBoolean()) {
                    type = Type.Sea;
                } else {
                    type = Type.Land;
                }

                locations.add(new Location(x, y, type));
            }
        }
    }



}
