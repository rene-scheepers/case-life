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
import classes.world.NodeHeuristic;
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

    private NodeHeuristic path;

    public Animal(World world, Genetics genetics) throws LocationAlreadyOccupiedException {
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

    private NodeHeuristic getMostFavorableTarget(Node target) {
        SortedList<NodeHeuristic> openNodes = new SortedList<NodeHeuristic>();
        ArrayList<NodeHeuristic> closedNodes = new ArrayList();

        openNodes.add(new NodeHeuristic(getNode()));
        while (openNodes.getSize() >= 1) {
            NodeHeuristic current = openNodes.getFirst();

            for (Node node : current.getNode().getAdjacentNodes()) {

                if (node.getHolder() == null && !node.getLocationType().equals(LocationType.Obstacle)) {
                    boolean alreadyWalked = false;
                    for (NodeHeuristic walkableNode : openNodes.getObjects()) {
                        if (walkableNode.getNode().equals(node)) {
                            alreadyWalked = true;
                        }
                    }

                    if (!alreadyWalked) {
                        for (NodeHeuristic walkableNode : closedNodes) {
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
                            cost = 100;
                        } else {
                            cost = 10;
                        }

                        if (current.getParent() != null) {
                            cost += current.getCost();
                        }

                        NodeHeuristic adjacent = new NodeHeuristic(node, cost, heuristic);
                        adjacent.setParent(current);
                        openNodes.add(adjacent);
                    }
                }
            }

            if (current.getNode().equals(target)) {
                NodeHeuristic targetHeuristic = new NodeHeuristic(target, 0, 0);
                targetHeuristic.setParent(current);
                return targetHeuristic;
            }
            closedNodes.add(current);
            openNodes.remove(current);
        }

        return null;
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
        Node current = getNode();
        if (path == null || current.equals(path.getNode())) {
            path = getMostFavorableTarget(world.getNode(38,3));
        }

        NodeHeuristic parent = path.getParent();
        while(true) {
            if (parent.getParent() != null && !parent.getParent().getNode().equals(current)) {
                parent = parent.getParent();
            } else {
                break;
            }
        }

        move(parent.getNode());
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
