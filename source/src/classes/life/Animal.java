package classes.life;

import classes.Exceptions.LocationAlreadyOccupiedException;
import classes.enumerations.Digestion;
import classes.enumerations.Gender;
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

import java.util.*;

public class Animal extends Life implements IAnimal {

    private Gender gender;

    private Genetics genetics;

    private int energy;

    private int age;

    private World world;

    private State state;

    private Path path;

    private int recalculatePathInTurns = 10;

    public Animal(World world, Genetics genetics, Gender gender) {
        this.world = world;
        this.gender = gender;
        this.genetics = genetics;
        this.energy = genetics.getStamina();
    }

    /**
     * Gender
     *
     * @return The gender of this animal.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Gets the node on which the animal is currently positioned.
     *
     * @return The node on which the animal is located.
     */
    public Node getNode() {
        return world.getNodeForLife(this);
    }

    public int getAge() {
        return age;
    }

    /**
     * Hunger
     * Hunger is the current(energy) percentage of the total possible(stamina).
     *
     * @return The current hunger percentage.
     */
    public float getHunger() {
        return (float)energy / (float)genetics.getStamina() * 100;
    }

    /**
     * Energy
     *
     * @return
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * The genetics of this specific animal.
     *
     * @return
     */
    public Genetics getGenetics() {
        return genetics;
    }

    /**
     * What the animal is currently doing.
     *
     * @return
     */
    public State getState() {
        return state;
    }

    /**
     * Speed
     * @TODO WRITE THIS.
     *
     * @return The amount of nodes an animal can move in a single turn.
     */
    public int getSpeed() {
        int speed = getWeight() / 50;

        if (speed < 1) {
            speed = 1;
        }

        return speed;
    }

    /**
     * Weight
     * The weight is Energy - Strength + Legs * 10.
     *
     * @return Returns the weight of the animal.
     */
    public int getWeight() {
        int weight = energy - genetics.getStrength();
        if (weight < 0) {
            weight = 0;
        }

        return weight + genetics.getLegs() * 10;
    }

    private int wait;

    /**
     * Gets the A* Path for the animal.
     *
     * @param target The target node.
     * @return The path with the steps to follow to reach the target.
     */
    private Path getPath(Node target) {
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

        return null;
    }

    /**
     * Moves the animal onto the new node.
     *
     * @param newNode The neighbouring node, where the animal must move towards.
     *
     * @return TRUE if the animal did indeed move.
     */
    public boolean move(Node newNode) {
        if (newNode == null) {
            return false;
        }
        Node current = getNode();

        if (!current.getAdjacentNodes().contains(newNode)) {
            return false;
        }

        if (newNode.getHolder() != null || newNode.getLocationType().equals(LocationType.Obstacle)) {
            System.out.println("COLLISION");
            this.energy /= 2;
            return false;
        } else {
            energy -= genetics.getLegs();
            try {
                newNode.setHolder(this);
                current.unsetHolder();
            } catch (LocationAlreadyOccupiedException exception) {
                return false;
            }
            return true;
        }
    }

    /**
     * Checks if the animal can move onto the node.
     * Checks if the node is an Obstacle or other life is on the node.
     *
     * @param node
     *
     * @return TRUE if the node can move onto the node.
     */
    public boolean nodeIsTraversable(Node node) {
        if (node.getLocationType().equals(LocationType.Obstacle) || node.getHolder() != null) {
            return false;
        }
        return true;
    }

    /**
     * Check if the animal can mate with the given animal.
     * @param otherAnimal The animal to check the possibility with.
     * @return TRUE if the animal can mate with the other animal.
     */
    public boolean canPropagateWith(Animal otherAnimal) {
        if (energy - genetics.getReproductionCost() < 1) {
            return false;
        }

        if (otherAnimal.getGender().equals(gender)) {
            return false;
        }

        if (otherAnimal.getGenetics().getName() == genetics.getName()) {
            return false;
        }

        for(Node adjacent : otherAnimal.getNode().getAdjacentNodes()) {
            if (otherAnimal.nodeIsTraversable(adjacent)) {
                return true;
            }
        }

        return false;
    }

    public boolean canPropagateWith(Life otherLife) {
        if (otherLife instanceof Animal) {
            return canPropagateWith(((Animal) otherLife));
        }
        return false;
    }

    /**
     * Check if the given Interface is a food source for this animal.
     *
     * @param life The possible food source.
     *
     * @return TRUE if the given parameter is a valid food sources.
     */
    public boolean isFoodSource(Life life) {
        if (life.equals(this) || life.getEnergy() <= 0) {
            return false;
        }

        Digestion digestion = genetics.getDigestion();

        if (life instanceof Plant) {
            if (digestion.equals(Digestion.Omnivore) || digestion.equals(Digestion.Herbivore)) {
                return true;
            }
        } else if (life instanceof Animal) {
            if (digestion.equals(Digestion.Omnivore) || digestion.equals(Digestion.Carnivore)) {
                if (genetics.getName() == ((Animal) life).getGenetics().getName()) {
                    return false;
                }

                return true;
            }
        }

        return false;
    }

    /**
     * Simulates a turn for the animal.
     */
    public void simulate() {
        if (energy <= 0) {
            world.removeLife(this);
        }

        if (path == null || recalculatePathInTurns < 1) {
            if (false && gender.equals(Gender.Male) && genetics.getReproductionThreshold() > getHunger()) {
                path = findNearestPropagator();
                if (path == null) {
                    path = findNearestFoodSource();
                }
            } else {
                path = findNearestFoodSource();
            }

            recalculatePathInTurns = 5;
        } else {
            Node next;
            if (path.hasNext()) {
                next = path.next();
                if (path.hasNext()) {
                    if (nodeIsTraversable(next)) {
                        move(next);
                        recalculatePathInTurns--;
                    } else {
                        path = null;
                    }
                }
            } else {
                next = path.last();
                if (nodeIsTraversable(next)) {
                    move(next);
                    path = null;
                } else {
                    Life holder = next.getHolder();
                    if (holder != null) {
                        if (isFoodSource(holder)) {
                            eat(holder);
                        } else {
                            if (canPropagateWith(holder) && false) {
                                if (propagate((Animal) holder)) {
                                    energy -= genetics.getReproductionCost();
                                }
                            }
                            path = null;
                        }
                    }
                }

            }
        }
    }


    /**
     * Finds the nearest path to a food source.
     * This path is the closest to the animal not the fastest path.
     *
     * @return Returns NULL
     */
    public Path findNearestFoodSource() {
        List<Node> sources = new ArrayList();
        for (Life life : world.getLives()) {
            if (isFoodSource(life)) {
                sources.add(life.getNode());
            }
        }

        if (sources.size() > 0) {
            Node current = getNode();
            Collections.sort(sources, new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {

                    System.out.println(o1.getPathsLeadingHere().size());
                    Double distance1 = world.getDiagonalDistance(current, o1) - o1.getHolder().getEnergy() * 0.1;
                    if (o1.getPathsLeadingHere().size() > 0) {
                        distance1 += 1000;
                    }
                    Double distance2 = world.getDiagonalDistance(current, o2) - o2.getHolder().getEnergy() * 0.1;
                    if (o2.getPathsLeadingHere().size() > 0) {
                        distance2 += 1000;
                    }
                    return Double.compare(distance1, distance2);
                }
            });

            for (Node source : sources) {
                Path path = getPath(source);
                if (path != null) {
                    return path;
                }
            }
        }
        return null;
    }

    public Path findNearestPropagator() {
        Node current = getNode();

        List<Node> sources = new ArrayList();
        for (Life life : world.getLives()) {
            if (canPropagateWith(life)) {
                sources.add(life.getNode());
            }
        }

        if (sources.size() > 0) {
            Collections.sort(sources, new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    Double distance1 = world.getDiagonalDistance(current, o1);
                    Double distance2 = world.getDiagonalDistance(current, o2);
                    return Double.compare(distance1, distance2);
                }
            });

            for (Node source : sources) {
                Path path = getPath(source);
                if (path != null) {
                    return path;
                }
            }
        }
        return null;
    }

    /**
     * Eat the food. Adding the energy to itself.
     *
     * @param food The food to be eaten.
     *
     * @return
     */
    @Override
    public boolean eat(IFood food) {
        int energy = food.getEaten();

        this.energy += energy;

        if (this.energy > genetics.getStamina()) {
            this.energy = genetics.getStamina();
        }

        state = State.Eating;

        return true;
    }

    /**
     * Reproduces with another animal.
     *
     * @param animal
     * @return
     */
    @Override
    public boolean propagate(Animal animal) {
        if (!canPropagateWith(animal)) {
            return false;
        }

        Random random = new Random();
        Genetics dna = new Genetics(genetics.getName(), genetics.getDigestion(), genetics.getLegs(), genetics.getReproductionCost(), genetics.getStamina(), genetics.getStrength(), genetics.getReproductionThreshold());
        Animal child = new Animal(world, dna, random.nextBoolean() ? Gender.Male : Gender.Female);

        Node node = animal.getNode();
        for (Node adjacent : node.getAdjacentNodes()) {
            if (!child.nodeIsTraversable(adjacent)) {
                continue;
            }

            try {
                world.addLife(child, adjacent);
                System.out.println("NEW ANIMAL");
                energy -= genetics.getReproductionCost();
                return true;
            } catch (Exception exception) {

            }
        }
        return false;
    }

    /**
     * The animals get eaten and killed.
     *
     * @return Returns the amount of energy possible for absorbtion.
     */
    @Override
    public int getEaten() {
        System.out.println("i get eaten ofzo");
        int energyEaten = energy;
        energy = 0;
        world.removeLife(this);
        return energyEaten;
    }

    /**
     * Draws the Animal to the Context.
     *
     * @param context
     */
    public void draw(GraphicsContext context) {
        Color color = Color.BROWN;
        if (genetics.getDigestion().equals(Digestion.Carnivore)) {
            color = Color.RED;
        } else if (genetics.getDigestion().equals(Digestion.Omnivore)) {
            color = Color.YELLOW;
        }

        Node node = getNode();

        context.setFill(color);
        context.fillRect(
                node.getX() + 0.1,
                node.getY() + 0.1,
                0.8,
                0.8
        );

        if (gender.equals(Gender.Female)) {
            context.setFill(Color.WHITE);
            context.fillRect(
                    node.getX() + 0.3,
                    node.getY() + 0.3,
                    0.4,
                    0.4
            );
        }
    }
}
