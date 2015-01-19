package com.caselife.logic.life;

public interface IFood {

    /**
     * Eat the object. Returns the amount of energy that is eaten(absorbed).
     *
     * @return Return the amount of energy passed to the eater.
     */
    public int getEaten();

    /**
     *
     * @return
     */
    public int getEnergy();
}
