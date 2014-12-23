package classes.world;

import classes.enumerations.Type;
import classes.life.Animal;
import classes.life.Plant;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Random;

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

                locations.add(new Location(this, x, y, type));
            }
        }
    }

    public ArrayList<Location> getNeighbouringLocations() {
        throw new NotImplementedException();
    }

    public void addFood(Animal animal) {
        animals.add(animal);
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
