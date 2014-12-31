package classes.life;

import classes.world.Node;
import classes.world.World;

public class Plant extends Life {

    public static final int RESPAWN_TIME = 100;
    public static final int TIMES_EATEN_BEFORE_DEAD = 10;
    public static final int MAX_ENERGY = 300;

    private int energy;
    private int timesDied;
    private int timesEaten;
    private World world;

    public Plant(World world, int energy) {
        this.world = world;
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    public boolean isAlive() {
        return true;
    }

    public Node getNode() {
        return world.getNodeForLife(this);
    }

    @Override
    public int getEaten() {
        timesEaten++;
        int eaten = MAX_ENERGY / TIMES_EATEN_BEFORE_DEAD;

        if (eaten > energy) {
            eaten = energy;
        }
        energy -= eaten;
        return eaten;
    }

    public void simulate() {
        if (energy < 1 && timesDied < RESPAWN_TIME) {
            timesEaten = 0;
            timesDied++;
        } else {
            timesDied = 0;

            if (energy < MAX_ENERGY) {
                energy++;
            }
        }
    }

}
