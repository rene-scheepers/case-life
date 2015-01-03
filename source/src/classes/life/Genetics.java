package classes.life;

import classes.enumerations.Digestion;

public class Genetics extends Object {

    private Digestion digestion;

    private int legs;

    private double reproductionCost;

    private int stamina;

    private int strength;

    private String name;

    private float reproductionThreshold;
//
//    private float swimmingThreshold;
//
//    private float movementThreshold;

    public Genetics(String name, Digestion digestion, int legs, double reproductionCost, int stamina, int strength, float reproductionThreshold) {
        this.name = name;
        this.digestion = digestion;
        this.legs = legs;
        this.reproductionCost = reproductionCost;
        this.stamina = stamina;
        this.strength = strength;
        this.reproductionThreshold = reproductionThreshold;
//        this.swimmingThreshold = swimmingThreshold;
//        this.movementThreshold = movementThreshold;
    }

    public String getName() {
        return name;
    }

    public float getReproductionThreshold() {
        return reproductionThreshold;
    }

    public Digestion getDigestion() {
        return digestion;
    }

    public int getLegs() {
        return legs;
    }

    public double getReproductionCost() {
        return reproductionCost;
    }

    public int getStamina() {
        return stamina;
    }

    public int getStrength() {
        return strength;
    }

}
