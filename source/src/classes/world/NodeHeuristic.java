package classes.world;

public class NodeHeuristic implements Comparable {

    private Node node;
    private float cost;
    private float heuristic;
    private NodeHeuristic parent;
    private int depth;

    public NodeHeuristic(Node node) {
        this.node = node;
    }

    public NodeHeuristic(Node node, float cost, float heuristic) {
        this.node = node;
        this.cost = cost;
        this.heuristic = heuristic;
    }

    public Node getNode() {
        return node;
    }

    public void setParent(NodeHeuristic node) {
        parent = node;
    }

    public NodeHeuristic getParent() {
        return parent;
    }

    public float getCost() {
        return cost;
    }

    public float getHeuristic() {
        return heuristic;
    }

    public float getTotal() {
        return cost + heuristic;
    }

    @Override
    public String toString() {
        return "NodeHeuristic{" +
                "node=" + node +
                ", cost=" + cost +
                ", heuristic=" + heuristic +
                ", parent=" + parent +
                ", depth=" + depth +
                '}';
    }

    @Override
    public int compareTo(Object other) {
        if (other instanceof NodeHeuristic) {
            NodeHeuristic otherNode = (NodeHeuristic)other;

            if (getTotal() < otherNode.getTotal()) {
                return -1;
            } else if(getTotal() > otherNode.getTotal()) {
                return 1;
            }
        }

        return 0;
    }
}
