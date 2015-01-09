package classes.world;

import classes.exceptions.LocationAlreadyOccupiedException;
import classes.life.Life;

import java.util.ArrayList;

public class Node {

    private int x;
    private int y;

    private LocationType type;
    private ArrayList<Node> adjacentNodes = new ArrayList();

    private Life holder;

    public Node(int x, int y, LocationType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void addAdjacentNode(Node node) {
        adjacentNodes.add(node);
    }

    public ArrayList<Node> getAdjacentNodes() {
        return adjacentNodes;
    }

    public Life getHolder() {
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

    public void setHolder(Life object) throws LocationAlreadyOccupiedException {
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
        return String.format("X: %s, Y: %s, Type: %s, Holder: %s", x, y, type.name(), (holder != null ? holder.toString() : "null"));
    }
}
