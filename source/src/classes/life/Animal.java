package classes.life;

import classes.world.Location;

public class Animal implements Food {

    /** Inherited */

    private Digestion digestion;

    private float reproductionThreshold;

    private float reproductionCost;

    private int stamina;

    private int strength;

    private float swimmingThreshold;

    private float movementThreshold;

    private int legs;

    /** Animal specific */

    private int energy;

    private int hunger;

    private int direction;

    private int speed;

    private int age;

    private State state;

    private Location position;

    public int getWeight() {
        int weight = energy - strength;
        if (weight < 0) {
            weight = 0;
        }

        return weight + legs * 10;
    }

    public void move(Location newLocation) {
        if (newLocation.hasObstacle()) {
            this.energy /= 2;
            return;
        }

        energy -= legs;
        position = newLocation;
    }

    public void eat(Food food) {

    }

    @Override
    public int getEaten() {
        return 0;
    }
}
