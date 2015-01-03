package classes.life;

import classes.enumerations.Digestion;
import classes.world.Node;
import classes.world.World;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Plant extends Life {

    public static final int RESPAWN_TIME = 100;
    public static final int TIMES_EATEN_BEFORE_DEAD = 10;
    public static final int MAX_ENERGY = 300;

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

    public boolean isAlive() {
        return true;
    }

    public Node getNode() {
        return world.getNodeForLife(this);
    }

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
                energy++;
            }
        }
    }

    public void draw(GraphicsContext context) {
        if (isAlive()) {
            Node node = getNode();

            if (energy == 0) {
                context.setFill(Color.WHEAT);
            } else {
                context.setFill(Color.GREEN);
            }


            context.setFill(Color.rgb(0, energy > 255 ? 255 : energy, 0));

            context.fillRect(
                    node.getX(),
                    node.getY(),
                    1,
                    1
            );
        }
    }

}
