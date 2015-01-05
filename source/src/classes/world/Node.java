package classes.world;

import classes.Exceptions.LocationAlreadyOccupiedException;
import classes.enumerations.LocationType;
import classes.life.Life;

import java.util.ArrayList;
import java.util.Collections;

public class Node {

    private int x;
    private int y;
    private ArrayList<Path> pathsLeadingHere = new ArrayList();

    private LocationType type;
    private ArrayList<Node> adjacentNodes = new ArrayList();

    private Life holder;

    public Node(int x, int y, LocationType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void addPathsLeadingHere(Path path) {
        pathsLeadingHere.add(path);
    }

    public ArrayList<Path> getPathsLeadingHere() {
        return pathsLeadingHere;
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
        if (holder != null) {
            return String.format("X: %s, Y: %s, Type: %s, Holder: %s", x, y, type.name(), holder.toString());
        } else {
            return String.format("X: %s, Y: %s, Type: %s, Holder: %s", x, y, type.name(), "null");
        }
    }
}
