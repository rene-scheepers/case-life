package classes.interfaces;

public interface Food {

    /**
     * Eat the object. Returns the amount of energy that is eaten(absorbed).
     *
     * @return Return the amount of energy passed to the eater.
     */
    public int getEaten();

    /**
     * Simulate the object.
     */
    public void simulate();

}
