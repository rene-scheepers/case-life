package classes.life;

import classes.interfaces.Food;

/**
 * Created by Rene on 23-12-2014.
 */
public class Plant implements Food {

    public static final int RESPAWN_TIME = 100;
    public static final int TIMES_EATEN_BEFORE_DEAD = 10;

    protected int energy;
    protected int timesDied;
    protected int timesEaten;

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
