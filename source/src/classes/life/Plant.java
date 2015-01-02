package classes.life;

import classes.enumerations.Digestion;
import classes.world.Node;
import classes.world.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Plant extends Life {

    public static final int RESPAWN_TIME = 100;
    public static final int TIMES_EATEN_BEFORE_DEAD = 10;
    public static final int MAX_ENERGY = 300;

    private int energy;
    private int timesDied;
    private int timesEaten;
    private World world;
    private boolean redraw = true;

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
        timesEaten++;
        int eaten = MAX_ENERGY / TIMES_EATEN_BEFORE_DEAD;

        if (eaten > energy) {
            eaten = energy;
        }
        energy -= eaten;
        return eaten;
    }

    public void simulate() {
        if (energy < 1 && timesDied < RESPAWN_TIME) {
            timesEaten = 0;
            timesDied++;
        } else {
            timesDied = 0;

            if (energy < MAX_ENERGY) {
                energy++;
            }
        }
    }

    public void draw(GraphicsContext context, double drawWidth, double drawHeight) {
        if (isAlive() && redraw) {
            Node node = getNode();
            double width = drawWidth;
            double height = drawHeight;

            context.setFill(Color.GREEN);
            context.fillRect(
                    node.getX() * drawWidth + (drawWidth - width) / 2,
                    node.getY() * drawHeight + (drawWidth - width) / 2,
                    width,
                    height
            );

//            context.setFill(Color.BLACK);
//            context.fillText(String.valueOf(energy), node.getX() * drawWidth, node.getY() * drawHeight);
            redraw = false;
        }
    }

}
