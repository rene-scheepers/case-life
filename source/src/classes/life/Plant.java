package classes.life;

import classes.interfaces.IFood;

public class Plant implements IFood {

    public static final int RESPAWN_TIME = 100;
    public static final int TIMES_EATEN_BEFORE_DEAD = 10;

    protected int energy;
    protected int timesDied;
    protected int timesEaten;

    public Plant(int energy) {
        this.energy = energy;
    }

    @Override
    public int getEaten() {
        timesEaten++;
        int eaten = energy * (timesEaten / TIMES_EATEN_BEFORE_DEAD);
        if (eaten > energy) {
            eaten = energy;
        }

        energy -= eaten;
        return eaten;
    }

    public void simulate() {
        if (energy == 0 && timesDied < RESPAWN_TIME) {
            timesEaten = 0;
            timesDied++;
        } else {
            energy++;
        }
    }

}
