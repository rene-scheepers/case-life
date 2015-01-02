package classes.life;

import classes.debug.PathLogger;
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
            Canvas canvas = context.getCanvas();

            double drawWidth = canvas.getWidth() / world.getWidth();
            if (drawWidth < 1) {
                drawWidth = 1;
            }

            double drawHeight = canvas.getHeight() / world.getHeight();
            if (drawHeight < 1) {
                drawHeight = 1;
            }

            Node node = getNode();

            context.setFill(Color.GREEN);
            context.fillRect(
                    node.getX() * world.getWidth(),
                    node.getY() * world.getHeight(),
                    1,
                    1
            );
        }
    }

}
