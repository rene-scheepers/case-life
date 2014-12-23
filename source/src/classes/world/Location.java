package classes.world;

import classes.enumerations.LocationType;

public class Location {

    private int x;
    private int y;

    private LocationType type;
    private World world;

    public Location(World world, int x, int y, LocationType type) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public LocationType getType() {
        return type;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public String toString() {
        return String.format("X: %s, Y: %s, Type: %s", x, y, type.name());
    }
}
