package classes.world;

import java.awt.*;

/**
 * Created by Rene on 23-12-2014.
 */
public class Location {

    private Point point;
    private Type type;

    public Location(int x, int y, Type type) {
        this.point = new Point(x, y);
        this.type = type;
    }

    public boolean hasObstacle() {
        return false;
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
}
