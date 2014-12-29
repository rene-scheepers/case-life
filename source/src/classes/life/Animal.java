package classes.life;

import classes.Exceptions.LocationAlreadyOccupiedException;
import classes.enumerations.Direction;
import classes.enumerations.LocationType;
import classes.enumerations.State;
import classes.interfaces.IAnimal;
import classes.interfaces.IFood;
import classes.interfaces.ISimulate;
import classes.world.Location;
import classes.world.Path;
import classes.world.World;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Animal extends Object implements IFood, ISimulate, IAnimal {

    private Genetics genetics;

    private int energy;

    private int hunger;

    private Direction direction;

    private int age;

    private State state;

    private Location location;

    private Path path;

    public Animal(Genetics genetics, Location location) throws LocationAlreadyOccupiedException {
        this.genetics = genetics;
        this.energy = genetics.getStamina();
        this.location = location;
        location.setHolder(this);
    }

    public Location getLocation() {
        return location;
    }

    public int getAge() {
        return age;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getHunger() {
        return hunger;
    }

    public int getEnergy() {
        return energy;
    }

    public Genetics getGenetics() {
        return genetics;
    }

    public State getState() {
        return state;
    }

    public int getSpeed() {
        throw new NotImplementedException();
    }

    public int getWeight() {
        int weight = energy - genetics.getStrength();
        if (weight < 0) {
            weight = 0;
        }

        return weight + genetics.getLegs() * 10;
    }

    public boolean move(Location newLocation) {
        if (newLocation == null) {
            return false;
        }
        if (newLocation.getHolder() != null || newLocation.getLocationType().equals(LocationType.Obstacle)) {
            this.energy /= 2;
            return false;
        } else {
            energy -= genetics.getLegs();
            try {
                location = newLocation;
                newLocation.setHolder(this);
                location.unsetHolder();
                state = State.Moving;
            } catch(LocationAlreadyOccupiedException exception) {
                return false;
            }
            return true;
        }
    }

    public void simulate() {
        Random randomizer = new Random();

        Collection<Location> neighbouringLocations = location.getNeighbouringLocations().values();


        ArrayList<Location> locationByFavourability = new ArrayList<>();

        for (Location randomLocation : location.getNeighbouringLocations().values()) {
            if (location.getLocationType().equals(LocationType.Land)) {
                locationByFavourability.add(100, randomLocation);
            } else if (location.getLocationType().equals(LocationType.Water)) {
                locationByFavourability.add(10, randomLocation);
            } else {
                locationByFavourability[0] = randomLocation;
            }
        }

        move(locationByFavourability[locationByFavourability.length]);
    }

    public void findPath() {

    }

    public float getCost(Location location) {
        return 0;
    }

    @Override
    public boolean eat(IFood food) {
        int energy = food.getEaten();
        this.energy += energy;

        state = State.Eating;

        return true;
    }

    @Override
    public boolean reproduce(Animal animal) {
        try {
            Animal child = new Animal(this.genetics, this.location);

            location.getWorld().addObject(child);

            state = State.Reproducing;
        } catch(LocationAlreadyOccupiedException exception) {
            return false;
        }
        return true;
    }

    @Override
    public int getEaten() {
        location.getWorld().removeObject(this);
        return 0;
    }
}
