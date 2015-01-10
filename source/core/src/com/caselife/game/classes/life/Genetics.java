package com.caselife.game.classes.life;

import java.util.Random;

public class Genetics extends Object {

    private static final float EVOLUTION_RATE = 10;

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

    public static Genetics getPropagatingGenetics(Genetics genetics1, Genetics genetics2) {
        int legs = getRandomGeneticValue(genetics1.getLegs(), genetics2.getLegs());
        if (legs < 0) {
            legs = 0;
        } else if(legs > 10) {
            legs = 10;
        }

        int stamina = getRandomGeneticValue(genetics1.getStamina(), genetics2.getStamina());
        double reproductionCost = getRandomGeneticValue(genetics1.getReproductionCost(), genetics2.getReproductionCost());
        if (stamina < reproductionCost) {
            reproductionCost = stamina;
        }

        int strength = getRandomGeneticValue(genetics1.getStrength(), genetics2.getStrength());
        float reproductionThreshold = getRandomGeneticValue(genetics1.getReproductionThreshold(), genetics2.getReproductionThreshold());
        if (reproductionThreshold > 100) {
            reproductionThreshold = 100;
        }

        return new Genetics(genetics1.getName(), genetics1.getDigestion(), legs, reproductionCost, stamina, strength, reproductionThreshold);
    }

    private static int getRandomGeneticValue(int value1, int value2) {
        return (int) getRandomGeneticValue((double) value1, (double) value2);
    }

    private static float getRandomGeneticValue(float value1, float value2) {
        return (float)getRandomGeneticValue((double) value1, (double) value2);
    }

    private static double getRandomGeneticValue(double value1, double value2) {
        Random random = new Random();
        double factor = 1 + (random.nextInt((int) EVOLUTION_RATE * 2) - EVOLUTION_RATE) / 100;

        return (value1 + value2) / 2 * factor;
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

    @Override
    public String toString() {
        return "Genetics{" +
                "digestion=" + digestion +
                ", legs=" + legs +
                ", reproductionCost=" + reproductionCost +
                ", stamina=" + stamina +
                ", strength=" + strength +
                ", name='" + name + '\'' +
                ", reproductionThreshold=" + reproductionThreshold +
                '}';
    }
}
