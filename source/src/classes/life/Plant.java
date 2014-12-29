package classes.life;

import classes.interfaces.IFood;
import classes.world.Location;

public class Plant extends Object implements IFood {

    public static final int RESPAWN_TIME = 100;
    public static final int TIMES_EATEN_BEFORE_DEAD = 10;

    protected Location location;
    protected int energy;
    protected int timesDied;
    protected int timesEaten;

    public Plant(Location location, int energy) {
        this.location = location;
        this.energy = energy;
    }

    public Location getLocation() {
        return location;
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
