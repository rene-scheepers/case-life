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
import classes.world.Path;
import classes.world.World;
import javafx.scene.canvas.Canvas;
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
        throw new NotImplementedException();
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
        SortedList<Path> openNodes = new SortedList();
        ArrayList<Node> closedNodes = new ArrayList();

        int count = 0;
        openNodes.add(new Path(getNode()));
        while (openNodes.getSize() >= 1) {
            Path current = openNodes.getFirst();

            for (Node node : current.getNode().getAdjacentNodes()) {
                if (node.equals(target)) {
                    Path targetPath = new Path(target, current.getCost(), 0);
                    targetPath.setParent(current);
                    return targetPath;
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
                    for (Path walkableNode : openNodes.getObjects()) {
                        if (walkableNode.getNode().equals(node)) {
                            alreadyWalked = true;
                            break;
                        }
                    }
                }


                if (!alreadyWalked) {
                    Double distance = world.getDiagonalDistance(node, target);

                    float heuristic = distance.floatValue();

                    float cost;
                    if (node.getLocationType().equals(LocationType.Land)) {
                        cost = 2;
                    } else {
                        cost = 3;
                    }

                    if (current.getParent() != null) {
                        cost += current.getCost();
                    }

                    Path adjacent = new Path(node, cost, heuristic * cost);
                    adjacent.setParent(current);
                    openNodes.add(adjacent);
                }

            }

            closedNodes.add(current.getNode());
            count++;
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
            //energy -= genetics.getLegs();
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

    private Node getNextNodeInPath() {
        Path path = this.path;
        ArrayList<Node> nodes = new ArrayList();

        while (path.getParent() != null) {
            nodes.add(path.getNode());

            path = path.getParent();
        }

        if (nodes.size() > 2) {
            return nodes.get(nodes.size() - 1);
        } else {
            return null;
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

        if (wait > 0) {
            wait--;
            return;
        }

        if (path == null) {
            try {
                path = findNearestFoodSource();
                if (path == null) {

                    wait = 2;
                }
            } catch (NoFoodSourceFoundException exception) {
                wait = 2;
            }
        } else {
            Node next = getNextNodeInPath();
            Node current = getNode();
            if (nodeIsTraversable(next)) {
                move(next);
                if (current.equals(getNode())) {
                    System.out.println(current);
                    System.out.println(next);
                }
            } else {

                if (next.getHolder() instanceof Plant) {
                    Life life = next.getHolder();
                    if (life.getEnergy() == 0 || !life.isAlive()) {
                        path = null;
                    } else {
                        eat(life);
                        wait = 2;
                    }
                } else {
                    path = null;
                    wait = 2;
                }
            }
        }
    }

    public Path findNearestFoodSource() throws NoFoodSourceFoundException {
        Node current = getNode();
        ArrayList<Life> living = world.getLife();

        List<Node> sources = new ArrayList();
        for (Life life : living) {
            if (life instanceof Plant && life.isAlive() && life.getEnergy() > 0) {
                sources.add(life.getNode());
            }
        }

        Collections.sort(sources, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                double distance1 = world.getDiagonalDistance(current, o1);
                double distance2 = world.getDiagonalDistance(current, o2);

                return Double.compare(distance1, distance2);
            }
        });

        Random rand = new Random();
        while(true) {
            try {
                return getPath(sources.get(rand.nextInt(sources.size())));
            } catch (Exception ex){
                return null;
            }
        }

//        for (Node node : sources) {
//            try {
//                return getPath(node);
//            } catch (NoPathFoundException exception) {
//
//            }
//        }

        //
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
