package classes.life;

import classes.Exceptions.LocationAlreadyOccupiedException;
import classes.enumerations.Digestion;
import classes.enumerations.Direction;
import classes.enumerations.LocationType;
import classes.enumerations.State;
import classes.interfaces.IAnimal;
import classes.interfaces.IFood;
import classes.interfaces.ISimulate;
import classes.world.Location;
import classes.world.Node;
import classes.world.World;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Array;
import java.util.*;

public class Animal extends Object implements IFood, ISimulate, IAnimal {

    private Genetics genetics;

    private int energy;

    private int hunger;

    private Direction direction;

    private int age;

    private State state;

    private Location location;

    private ArrayList<Node> weightedNodes = new ArrayList();

    public Animal(Genetics genetics, Location location) throws LocationAlreadyOccupiedException {
        this.genetics = genetics;
        this.energy = genetics.getStamina();
        this.location = location;
        location.setHolder(this);

    }

    private void generateMap() {
        World world = location.getWorld();

        weightedNodes.clear();
        for (Location otherLocation : world.getLocations()) {
            if (otherLocation == location) {
                continue;
            }

            double weight = 10;
            double distance = world.getDistanceBetweenLocations(location, otherLocation);
            weight += distance;

            if (otherLocation.getLocationType().equals(LocationType.Water)) {
                weight += 5;
            } else if (otherLocation.getLocationType().equals(LocationType.Obstacle)) {
                weight += 100;
            }

            Digestion digestion = genetics.getDigestion();
            Object holder = otherLocation.getHolder();
            if (holder != null) {
                if (holder instanceof Animal) {
                    Animal holderAnimal = (Animal)holder;
                    Digestion holderAnimalDigestion = holderAnimal.getGenetics().getDigestion();
                    if (holderAnimalDigestion.equals(Digestion.Carnivore) || holderAnimalDigestion.equals(Digestion.Omnivore)) {
                        weight += 5;
                    }
                    else {
                        weight -= 5;
                    }
                }
                else if(holder instanceof Plant) {
                    if (digestion.equals(Digestion.Omnivore) || digestion.equals(Digestion.Herbivore)) {
                        weight -= 200;
                    }
                }
            }

            weightedNodes.add(new Node(otherLocation, weight));
        }
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
                newLocation.setHolder(this);
                location.unsetHolder();
                location = newLocation;
                state = State.Moving;
            } catch (LocationAlreadyOccupiedException exception) {
                return false;
            }
            return true;
        }
    }

    public void simulate() {
        generateMap();

        Collections.sort(weightedNodes);
        ArrayList<Node> sameWeights = new ArrayList();
        for(Node node : weightedNodes) {
            if (sameWeights.size() == 0 || sameWeights.get(0).getCost().equals(node.getCost())) {
                sameWeights.add(node);
            }else {
                break;
            }
        }

        Random random = new Random();

        move(sameWeights.get(random.nextInt(sameWeights.size())).getLocation());
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
        } catch (LocationAlreadyOccupiedException exception) {
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
