package classes.life;

import classes.Exceptions.LocationAlreadyOccupiedException;
import classes.Exceptions.NoFoodSourceFoundException;
import classes.Exceptions.NoPathFoundException;
import classes.enumerations.Digestion;
import classes.enumerations.LocationType;
import classes.enumerations.State;
import classes.interfaces.IAnimal;
import classes.interfaces.IFood;
import classes.world.Node;
import classes.world.NodeHeuristic;
import classes.world.Path;
import classes.world.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class Animal extends Life implements IAnimal {

    private Genetics genetics;

    private int energy;

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
        int speed = getWeight() / 50;

        if (speed < 1) {
            speed = 1;
        }

        return speed;
    }

    public int getWeight() {
        int weight = energy - genetics.getStrength();
        if (weight < 0) {
            weight = 0;
        }

        return weight + genetics.getLegs() * 10;
    }

    private int wait;

    private Path getPath(Node target) throws NoPathFoundException {
        SortedList<NodeHeuristic> openNodes = new SortedList();
        ArrayList<Node> closedNodes = new ArrayList();

        openNodes.add(new NodeHeuristic(getNode()));
        while (openNodes.getSize() >= 1) {
            NodeHeuristic current = openNodes.getFirst();
            Node currentNode = current.getNode();
            for (Node node : currentNode.getAdjacentNodes()) {
                if (node.equals(target)) {
                    NodeHeuristic targetNodeHeuristic = new NodeHeuristic(target, current.getCost(), 0);
                    targetNodeHeuristic.setParent(current);
                    return new Path(targetNodeHeuristic);
                }

                if (node.getLocationType().equals(LocationType.Obstacle)) {
                    continue;
                }

                if (node.getHolder() != null) {
                    continue;
                }

                boolean alreadyWalked = false;
                if (closedNodes.contains(node)) {
                    alreadyWalked = true;
                }

                if (!alreadyWalked) {
                    for (NodeHeuristic walkableNode : openNodes.getObjects()) {
                        if (walkableNode.getNode().equals(node)) {
                            alreadyWalked = true;
                            break;
                        }
                    }
                }


                if (!alreadyWalked) {
                    double heuristic = world.getDiagonalDistance(node, target);

                    double cost;
                    if (node.getLocationType().equals(LocationType.Land)) {
                        cost = 10;
                    } else {
                        cost = 15;
                    }

                    if (current.getParent() != null) {
                        cost += current.getCost();
                    }

                    NodeHeuristic adjacent = new NodeHeuristic(node, cost, heuristic * cost);
                    adjacent.setParent(current);
                    openNodes.add(adjacent);
                }

            }

            closedNodes.add(current.getNode());
            openNodes.remove(current);
        }

        throw new NoPathFoundException();
    }

    public boolean isAlive() {
        return !(energy <= 0);
    }

    public boolean move(Node newNode) {
        Node current = getNode();

        if (newNode == null) {
            return false;
        }

        if (!current.getAdjacentNodes().contains(newNode)) {
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

    public boolean nodeIsTraversable(Node node) {
        if (node.equals(LocationType.Obstacle) || node.getHolder() != null) {
            return false;
        }
        return true;
    }

    public void simulate() {
        if (energy <= 0) {
            world.removeLife(this);
        }

        Node current = getNode();

        if (path == null) {
            path = findNearestFoodSource();
        } else {
            Node next = path.getNextStep(current);
            Life holder = next.getHolder();
            if (path.getLastNode().equals(next)) {
                if (holder == null) {
                    path = null;
                } else {
                    if (holder instanceof Plant && holder.getEnergy() > 0) {
                        eat(holder);
                    } else {
                        path = null;
                    }
                }
            } else if (!nodeIsTraversable(next)) {
                path = null;
            } else {
                int movementLeft = getSpeed();
                while(movementLeft > 0 && nodeIsTraversable(next)) {
                    move(next);
                    next = path.getNextStep(getNode());
                    movementLeft--;
                }
            }
        }
    }

    public Path findNearestFoodSource() {
        Node current = getNode();
        ArrayList<Life> living = world.getLife();

        SortedList<Path> sources = new SortedList<>();
        for (Life life : living) {
            if (life instanceof Plant && life.isAlive() && life.getEnergy() > 0) {
                try {
                    sources.add(getPath(life.getNode()));
                } catch(Exception ex) {

                }
            }
        }


//        Collections.sort(sources, new Comparator<Node>() {
//            @Override
//            public int compare(Node o1, Node o2) {
//                double distance1 = world.getDiagonalDistance(current, o1);
//                double distance2 = world.getDiagonalDistance(current, o2);
//
//                return Double.compare(distance1, distance2);
//            }
//        });

        return sources.getFirst();
//
//        try {
//            return getPath(sources.get(0));
//        } catch(Exception ex) {
//            return null;
//        }
    }

    public void draw(GraphicsContext context) {
        if (isAlive()) {
            Color color = Color.BROWN;
            if (genetics.getDigestion().equals(Digestion.Carnivore)) {
                color = Color.RED;
            } else if (genetics.getDigestion().equals(Digestion.Omnivore)) {
                color = Color.YELLOW;
            }

            Node node = getNode();

            context.setFill(color);
            context.fillRect(
                    node.getX() + 0.2,
                    node.getY() + 0.2,
                    0.6,
                    0.6
            );

            context.setFill(Color.BLACK);
            //context.fillText(String.valueOf(energy), node.getX() * drawWidth, node.getY() * drawHeight);
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
