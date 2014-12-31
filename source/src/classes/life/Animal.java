package classes.life;

import classes.Exceptions.LocationAlreadyOccupiedException;
import classes.enumerations.LocationType;
import classes.enumerations.State;
import classes.interfaces.IAnimal;
import classes.interfaces.IFood;
import classes.world.Node;
import classes.world.Path;
import classes.world.World;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class Animal extends Life implements IAnimal {

    private Genetics genetics;

    private int energy;

    private int hunger;

    private int age;

    private World world;

    private State state;

    private Path path;

    public Animal(World world, Genetics genetics) {
        this.world = world;
        this.genetics = genetics;
        this.energy = genetics.getStamina();
    }

    public Node getNode() {
        return world.getNodeForLife(this);
    }

    public int getAge() {
        return age;
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

    private Path getPath(Node target) {
        SortedList<Path> openNodes = new SortedList<Path>();
        ArrayList<Path> closedNodes = new ArrayList();

        openNodes.add(new Path(getNode()));
        while (openNodes.getSize() >= 1) {
            Path current = openNodes.getFirst();

            for (Node node : current.getNode().getAdjacentNodes()) {

                if (node.getHolder() == null && !node.getLocationType().equals(LocationType.Obstacle)) {
                    boolean alreadyWalked = false;
                    for (Path walkableNode : openNodes.getObjects()) {
                        if (walkableNode.getNode().equals(node)) {
                            alreadyWalked = true;
                        }
                    }

                    if (!alreadyWalked) {
                        for (Path walkableNode : closedNodes) {
                            if (walkableNode.getNode().equals(node)) {
                                alreadyWalked = true;
                            }
                        }
                    }

                    if (!alreadyWalked) {
                        Double distance = world.getDistanceBetweenLocations(node, target);

                        float heuristic = distance.floatValue();
                        heuristic = Math.abs(target.getX() - node.getX()) + Math.abs(target.getY() - node.getY());

                        float cost;
                        if (node.getLocationType().equals(LocationType.Land)) {
                            cost = 10;
                        } else {
                            cost = 50;
                        }

                        if (current.getParent() != null) {
                            cost += current.getCost();
                        }

                        Path adjacent = new Path(node, cost, heuristic);
                        adjacent.setParent(current);
                        openNodes.add(adjacent);
                    }
                }
            }

            if (current.getNode().equals(target)) {
                Path targetHeuristic = new Path(target, 0, 0);
                targetHeuristic.setParent(current);
                return targetHeuristic;
            }
            closedNodes.add(current);
            openNodes.remove(current);
        }

        return null;
    }

    public boolean isAlive() {
        return !(energy <= 0);
    }

    public boolean move(Node newNode) {
        Node current = getNode();

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
        if (energy <= 0) {
            world.removeLife(this);
        }

        Node current = getNode();
//        for (Node adjacent : current.getAdjacentNodes()) {
//            Life adjacentLife = adjacent.getHolder();
//            if (adjacentLife instanceof Plant && adjacentLife.getEnergy() > 0) {
//                eat(adjacentLife);
//                //return;
//            }
//        }

        if (path == null || current.equals(path.getNode())) {
            Node fewd= findNearestFoodSource();
            path = getPath(findNearestFoodSource());
        } else {
            Path parent = path.getParent();
            while (true) {
                if (parent.getParent() != null && !parent.getParent().getNode().equals(current)) {
                    parent = parent.getParent();
                } else {
                    if (parent.getNode().getHolder() == null) {
                        break;
                    } else {
                        break;
                        //parent = getPath(findNearestFoodSource());
                        //return;
                    }
                }
            }

            if (parent != null) {
                move(parent.getNode());
            }
        }
    }

    public Node findNearestFoodSource() {
        ArrayList<Life> living = world.getLife();

        ArrayList<Life> possibleSources = new ArrayList();
        for (Life life : living) {
            if (life instanceof Plant && life.isAlive() && life.getEnergy() > 0) {
                possibleSources.add(life);
            }
        }

        return possibleSources.get(0).getNode();
//        Random rand = new Random();
//        return possibleSources.get(rand.nextInt(possibleSources.size())).getNode();
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
