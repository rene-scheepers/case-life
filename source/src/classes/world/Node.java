package classes.world;

/**
 * Created by Rene on 29-12-2014.
 */
public class Node implements Comparable {

    private Location location;
    private Double cost;

    public Node(Location location, Double cost) {
        this.location = location;
        this.cost = cost;
    }

    @Override
    public int compareTo(Object other) {
        if (other instanceof Node) {
            Node node = (Node) other;
            return cost.compareTo(node.getCost());
        }

        return 0;
    }

    public Location getLocation() {
        return location;
    }

    public Double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return String.format("Weight: %s, %s", cost, location);
    }
}
