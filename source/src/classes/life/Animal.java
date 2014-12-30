package classes.life;

import classes.Exceptions.LocationAlreadyOccupiedException;
import classes.enumerations.Digestion;
import classes.enumerations.Direction;
import classes.enumerations.LocationType;
import classes.enumerations.State;
import classes.interfaces.IAnimal;
import classes.interfaces.IFood;
import classes.interfaces.IPosition;
import classes.interfaces.ISimulate;
import classes.world.Node;
import classes.world.World;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class Animal extends Life implements IAnimal {

    private Genetics genetics;

    private int energy;

    private int hunger;

    private Direction direction;

    private int age;

    private World world;

    private State state;

    private HashMap<Node, Double> weightedMap = new HashMap();

    public Animal(World world, Genetics genetics) throws LocationAlreadyOccupiedException {
        this.world = world;
        this.genetics = genetics;
        this.energy = genetics.getStamina();
    }

    private void resetWeightedMap() {
        Node[][] nodes = world.getNodes();
        for (int x = 0; x < nodes.length; x++) {
            for (int y = 0; y < nodes[x].length; y++) {
                Node node = nodes[x][y];
                if (node.getLocationType().equals(LocationType.Obstacle) || node.getHolder() != null) {
                    continue;
                }

                weightedMap.put(nodes[x][y], 100.0);
            }
        }
    }

    public Node getNode() {
        return world.getNodeForLife(this);
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

    private void generateWeightedMap() {
        resetWeightedMap();
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                Node node = world.getNode(x, y);
                if (node.getHolder() == null) {
                    Double weight;
                    if (node.getLocationType().equals(LocationType.Water)) {
                        weight = 20.0;
                    } else {
                        weight = 10.0;
                    }

                    weightedMap.put(node, weight);
                }

//                Digestion digestion = genetics.getDigestion();
//                if (digestion.equals(Digestion.Carnivore) || digestion.equals(Digestion.Carnivore.Omnivore)) {
//                    for (Node adjacentNode : node.getAdjacentNodes()) {
//                        Double adjacentNodeWeight = weightedMap.get(adjacentNode);
//                        if (adjacentNodeWeight != null) {
//                            adjacentNodeWeight -= 15;
//                        }
//                    }
//                }
            }
        }
    }

    public boolean move(Node newNode) {
        Node current = getNode();
        //System.out.println(current);

        if (newNode == null) {
            return false;
        }

        if (newNode.getHolder() != null || newNode.getLocationType().equals(LocationType.Obstacle)) {
            this.energy /= 2;
            return false;
        } else {
            energy -= genetics.getLegs();
            try {
                newNode.setHolder(this);
                current.unsetHolder();
                state = State.Moving;
            } catch (LocationAlreadyOccupiedException exception) {
                return false;
            }
            return true;
        }
    }

    public void simulate() {
        generateWeightedMap();
        Node current = getNode();

        Iterator iterator = weightedMap.entrySet().iterator();

        Double lowestWeight = null;
        Node node = null;
        try {
        while(iterator.hasNext()) {
            Map.Entry pairs = (Map.Entry)iterator.next();

                if (current.getAdjacentNodes().contains(pairs.getKey())) {
                    if (lowestWeight == null || lowestWeight > (Double) pairs.getValue()) {
                        node = (Node) pairs.getKey();
                        lowestWeight = (Double) pairs.getValue();
                    }
                }
        }

        if (node != null) {
            move(node);
        }
        }catch(Exception ex) {

        }
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
        return true;
    }

    @Override
    public int getEaten() {
        world.removeLife(this);
        return 0;
    }
}
