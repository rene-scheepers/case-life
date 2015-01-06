package classes.life;

import classes.world.Node;
import classes.world.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Plant extends Life {

    private static final int RESPAWN_TIME = 100;
    private static final int TIMES_EATEN_BEFORE_DEAD = 10;
    private static final int REGENERATION = 1;
    private static final int MAX_ENERGY = 100;

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
                energy += REGENERATION;
            }
        }
    }

    public void draw(GraphicsContext context) {

    }

    public void draw(GraphicsContext context, int centerX, int centerY) {
        Node node = getNode();

        if (energy == 0) {
            context.setFill(Color.WHEAT);
        } else {
            context.setFill(Color.GREEN);
        }



        context.setFill(Color.rgb(0, 255 * energy / MAX_ENERGY, 0));

        context.fillRect(
                node.getX() - centerX,
                node.getY() - centerY,
                1,
                1
        );
    }

}
