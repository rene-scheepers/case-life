package classes.world;

import classes.Exceptions.LocationAlreadyOccupiedException;
import classes.enumerations.Direction;
import classes.enumerations.LocationType;

import java.awt.*;
import java.util.HashMap;

public class Location {

    private int x;
    private int y;

    private LocationType type;
    private World world;

    private Object holder;

    public Location(World world, int x, int y, LocationType type) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public Object getHolder() {
        return holder;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public LocationType getLocationType() {
        return type;
    }

    public World getWorld() {
        return world;
    }

    public void setHolder(Object object) throws LocationAlreadyOccupiedException {
        if (holder == null) {
            holder = object;
        } else {
            throw new LocationAlreadyOccupiedException();
        }
    }

    public void unsetHolder() {
        holder = null;
    }

    @Override
    public String toString() {
        return String.format("X: %s, Y: %s, Type: %s", x, y, type.name());
    }
}
