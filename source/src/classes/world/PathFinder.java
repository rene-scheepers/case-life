package classes.world;

import classes.life.Animal;
import classes.world.World;
import javafx.collections.transformation.SortedList;

import java.util.ArrayList;

/**
 * Created by Rene on 29-12-2014.
 */
public class PathFinder {

    private ArrayList closed = new ArrayList();
    private ArrayList open = new ArrayList();

    private Node[][] nodes;
    private World world;
    private Animal animal;

    public PathFinder(World world, Animal animal) {
        closed = new ArrayList();

        this.world = world;
        this.animal = animal;

        nodes = new Node[world.getWidth()][world.getHeight()];
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                nodes[x][y] = new Node(x, y);
            }
        }
    }

    public Path findPath(Location target) {

        closed.clear();
        open.clear();
        nodes[0][0].setCost(0);
        nodes[target.getX()][target.getY()].setParent(null);

        while (open.size() != 0) {

            Node current = (Node) open.get(0);
            if (current == nodes[target.getX()][target.getY()]) {
                break;
            }

            open.remove(current);
            closed.add(current);

            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {

                    if (x == 0 && y == 0) {
                        continue;
                    }

                    Location location = world.getLocation(animal.getLocation().getX(), animal.getLocation().getY());
                    float cost = animal.getCost(location);


                }
            }
        }

    }


}
