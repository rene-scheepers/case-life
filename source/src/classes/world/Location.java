package classes.world;

import classes.enumerations.Type;

import java.awt.*;

/**
 * Created by Rene on 23-12-2014.
 */
public class Location {

    private Point point;
    private Type type;
    private World world;

    public Location(World world, int x, int y, Type type) {
        this.world = world;
        this.point = new Point(x, y);
        this.type = type;
    }

    public int getX() {
        return (int)point.getX();
    }

    public int getY() {
        return (int)point.getY();
    }

    public Type getType() {
        return type;
    }

    public World getWorld() {
        return world;
    }
}
