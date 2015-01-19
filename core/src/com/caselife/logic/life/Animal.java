package com.caselife.logic.life;


import com.caselife.logic.world.LocationType;
import com.caselife.logic.world.Node;
import com.caselife.logic.world.World;
import com.caselife.logic.world.pathfinding.IPathfinder;
import com.caselife.logic.world.pathfinding.Path;

import java.util.*;

public class Animal extends Life implements IAnimal {

    private Gender gender;

    private Genetics genetics;

    private int energy;

    private World world;

    private Path path;

    private int recalculatePathInTurns = 10;

    private IPathfinder pathfinder;

    public Animal(World world, IPathfinder pathfinder, Genetics genetics, Gender gender) {
        this.world = world;
        this.pathfinder = pathfinder;
        this.gender = gender;
        this.genetics = genetics;
        this.energy = (int) (genetics.getStamina() * 0.5);
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

    /**
     * Hunger
     * Hunger is the current(energy) percentage of the total possible(stamina).
     *
     * @return The current hunger percentage.
     */
    public float getHunger() {
        return Math.abs((float) energy / (float) genetics.getStamina() * 100);
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
     * Speed
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
     * Gets two-dimensional array for each node in the world with the cost.
     * -1 for non-traversable nodes.
     * >= 0 for traversable nodes.
     *
     * @return
     */
    private double[][] getMostRecentValueMap() {
        //return valueMap;

        Node[][] nodes = world.getNodes();
        double[][] recentMap = new double[world.getWidth()][world.getHeight()];
        for (int x = 0; x < nodes.length; x++) {
            for (int y = 0; y < nodes[x].length; y++) {
                Node node = nodes[x][y];

                if (node.getHolder() != null || node.getLocationType().equals(LocationType.Obstacle)) {
                    recentMap[x][y] = -1;
                } else {
                    if (node.getLocationType().equals(LocationType.Land)) {
                        recentMap[x][y] = 10;
                    } else {
                        recentMap[x][y] = 25;
                    }
                }
            }
        }

        return recentMap;
    }

    /**
     * Gets the A* Path for the animal.
     *
     * @param target The target node.
     * @return The path with the steps to setFollowing to reach the target.
     */
    private Path getPath(Node target) {
        Path path = pathfinder.getPath(getMostRecentValueMap(), getNode(), target);

        return path;
    }

    /**
     * Moves the animal onto the new node.
     *
     * @param newNode The neighbouring node, where the animal must move towards.
     * @return TRUE if the animal did indeed move.
     */
    public boolean move(Node newNode) {
        if (newNode == null) {
            return false;
        }

        Node current = getNode();
        if (current.equals(newNode)) {
            return false;
        }

        if (!nodeIsTraversable(newNode)) {
            return false;
        }

        if (!current.getAdjacentNodes().contains(newNode)) {
            return false;
        }

        energy -= genetics.getLegs();
        boolean canSetHolder = newNode.setHolder(this);
        current.unsetHolder();
        return canSetHolder;
    }

    /**
     * Checks if the animal can move onto the node.
     * Checks if the node is an Obstacle or other life is on the node.
     *
     * @param node
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
     *
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

        if (otherAnimal.getGenetics().getName() != genetics.getName()) {
            return false;
        }

        for (Node adjacent : otherAnimal.getNode().getAdjacentNodes()) {
            if (otherAnimal.nodeIsTraversable(adjacent)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the animal can propagate with the given life.
     *
     * @param otherLife
     * @return TRUE if the animal can mate with the other life object.
     */
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
            if (gender.equals(Gender.Male) && genetics.getReproductionThreshold() < getHunger()) {
                path = findNearestPropagator();
                if (path == null) {
                    path = findNearestFoodSource();
                    pathfinder.registerPath(path);
                }
            } else if (getHunger() < 900) {
                if (path != null) {
                    pathfinder.unRegisterPath(path);
                }
                path = findNearestFoodSource();
                pathfinder.registerPath(path);
            }
            recalculatePathInTurns = 5;
        } else if (path.getCurrent().equals(path.getTarget())) {
            Life holder = path.getCurrent().getHolder();
            if (holder != null) {
                if (isFoodSource(holder)) {
                    eat(holder);
                } else {
                    if (canPropagateWith(holder) && propagate((Animal) holder)) {
                        energy -= genetics.getReproductionCost();
                    }

                    pathfinder.unRegisterPath(path);
                    path = null;
                }
            } else {
                if (!move(path.getCurrent())) {
                    pathfinder.unRegisterPath(path);
                    path = null;
                }
            }
        } else if (path.hasNext()) {
            Node next = path.next();
            if (move(next)) {
                recalculatePathInTurns--;
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
                boolean registered = false;
                for (int i = 0; i < pathfinder.getRegisteredPaths().size(); i++) {
                    Path path = pathfinder.getRegisteredPaths().get(i);
                    if (path == null) {
                        pathfinder.unRegisterPath(path);
                    } else {
                        if (life.getNode().equals(path.getTarget())) {
                            registered = true;
                            break;
                        }
                    }
                }


                if (!registered) {
                    sources.add(life.getNode());
                }
            }
        }

        if (sources.size() > 0) {
            Node current = getNode();
            Collections.sort(sources, new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    Double distance1 = world.getDiagonalDistance(current, o1) - o1.getHolder().getEnergy() * 0.1;
                    Double distance2 = world.getDiagonalDistance(current, o2) - o2.getHolder().getEnergy() * 0.1;
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
     * Find shortest path to propagator.
     *
     * @return
     */
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
     * @return
     */
    @Override
    public boolean eat(IFood food) {
        int energy = food.getEaten();

        this.energy += energy;

        if (this.energy > genetics.getStamina()) {
            this.energy = genetics.getStamina();
        }
        return true;
    }

    /**
     * Reproduces with another animal.
     *
     * @param animal Other animal to propagate with.
     * @return
     */
    @Override
    public boolean propagate(Animal animal) {
        if (!canPropagateWith(animal)) {
            return false;
        }

        Random random = new Random();
        Genetics dna = Genetics.getPropagatingGenetics(this.getGenetics(), animal.getGenetics());
        Animal child = new Animal(world, pathfinder, dna, random.nextBoolean() ? Gender.Male : Gender.Female);

        Node node = getNode();
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
        System.out.println("I get eaten ofzo");
        int energyEaten = energy;
        energy = 0;
        world.removeLife(this);
        return energyEaten;
    }

}
