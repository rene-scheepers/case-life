package classes.life;

import classes.enumerations.Direction;
import classes.enumerations.LocationType;
import classes.enumerations.State;
import classes.interfaces.IAnimal;
import classes.interfaces.IFood;
import classes.interfaces.ISimulate;
import classes.world.Location;
import classes.world.World;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Animal implements IFood, ISimulate, IAnimal {

    private Genetics genetics;

    private int energy;

    private int hunger;

    private Direction direction;

    private int age;

    private State state;

    private Location location;

    public Animal(Genetics genetics, Location position) {
        this.genetics = genetics;
        this.location = position;
        this.energy = genetics.getStamina();
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

    public void move(Location newLocation) {
        if (newLocation.getLocationType().equals(LocationType.Obstacle)) {
            this.energy /= 2;
            return;
        }

        energy -= genetics.getLegs();
        location = newLocation;
        state = State.Moving;
    }

    public void simulate() {
        World world = location.getWorld();
        Location newLocation = world.getLocation(location.getX(), location.getY() + 1);

        move(newLocation);
    }

    @Override
    public void eat(IFood food) {
        int energy = food.getEaten();
        this.energy += energy;

        state = State.Eating;
    }

    @Override
    public void reproduce(Animal animal) {
        Animal child = new Animal(this.genetics, this.location);
        location.getWorld().addAnimal(child);

        state = State.Reproducing;
    }

    @Override
    public int getEaten() {
        location.getWorld().removeAnimal(this);
        return 0;
    }
}
