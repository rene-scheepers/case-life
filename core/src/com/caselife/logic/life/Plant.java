package com.caselife.logic.life;

import com.caselife.logic.world.Node;
import com.caselife.logic.world.World;

public class Plant extends Life {

    public static final int RESPAWN_TIME = 100;
    public static final int TIMES_EATEN_BEFORE_DEAD = 10;
    public static final int REGENERATION = 1;
    public static final int MAX_ENERGY = 100;

    private int energy;
    private int timesDied;
    private World world;

    public Plant(World world, int energy) {
        this.world = world;
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    /**
     * Sets the new energy value clamped to the @see MAX_ENERGY specified.
     *
     * @param energy New energy value.
     */
    public void setEnergy(int energy) {
        this.energy = Math.min(MAX_ENERGY - this.energy, energy);
    }

    public Node getNode() {
        return world.getNodeForLife(this);
    }

    /**
     * @return The amount of energy eaten.
     */
    @Override
    public int getEaten() {
        int eaten = MAX_ENERGY / TIMES_EATEN_BEFORE_DEAD;

        if (eaten > energy) {
            eaten = energy;
        }
        energy -= eaten;
        return eaten;
    }

    public void simulate() {
        if (energy < 1 && timesDied < RESPAWN_TIME) {
            timesDied++;
        } else {
            timesDied = 0;

            if (energy < MAX_ENERGY) {
                setEnergy(energy + REGENERATION);
            }
        }
    }

    @Override
    public String toString() {
        return "Plant{" +
                "energy=" + energy +
                ", timesDied=" + timesDied +
                '}';
    }
}
