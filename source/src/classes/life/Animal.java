package classes.life;

import classes.enumerations.Direction;
import classes.enumerations.LocationType;
import classes.enumerations.State;
import classes.interfaces.IAnimal;
import classes.interfaces.IFood;
import classes.interfaces.ISimulate;
import classes.world.Location;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Animal implements IFood, ISimulate, IAnimal {

    private Genetics genetics;

    private int energy;

    private int hunger;

    private Direction direction;

    private int age;

    private State state;

    private Location position;

    public Animal(Genetics genetics, Location position) {
        this.genetics = genetics;
        this.position = position;
        this.energy = genetics.getStamina();
    }

    public Location getPosition() {
        return position;
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
        if (newLocation.getType().equals(LocationType.Obstacle)) {
            this.energy /= 2;
            return;
        }

        energy -= genetics.getLegs();
        position = newLocation;
    }

    public void simulate() {
        
    }

    @Override
    public void eat(IFood food) {
        int energy = food.getEaten();
        this.energy += energy;
    }

    @Override
    public void reproduce(Animal animal) {
        Animal child = new Animal(this.genetics, this.position);
        position.getWorld().addAnimal(child);
    }

    @Override
    public int getEaten() {
        position.getWorld().removeAnimal(this);
        return 0;
    }
}
