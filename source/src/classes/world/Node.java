package classes.world;

/**
 * Created by Rene on 29-12-2014.
 */
public class Node implements Comparable {

    private int x;
    private int y;
    private float cost = 0;
    private Node origin;
    private int depth = 0;

    public Node(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public void setParent(Node origin) {
        depth = origin.depth + 1;
        this.origin = origin;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getCost() {
        return cost;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public int compareTo(Object other) {
        if (other instanceof Node) {
            Node node = (Node)other;

            if (cost > node.cost) {
                return 1;
            } else if (cost < node.cost) {
                return  -1;
            }
        }

        return 0;

    }
}
