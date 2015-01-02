package classes.world;

public class Path implements Comparable {

    private Node node;
    private float cost;
    private float heuristic;
    private Path parent;

    public Path(Node node) {
        this.node = node;
    }

    public Path(Node node, float cost, float heuristic) {
        this.node = node;
        this.cost = cost;
        this.heuristic = heuristic;
    }

    public Node getNode() {
        return node;
    }

    public void setParent(Path node) {
        parent = node;
    }

    public Path getParent() {
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
        return "Path{" +
                "node=" + node +
                ", cost=" + cost +
                ", heuristic=" + heuristic +
                ", parent=" + parent +
                '}';
    }

    @Override
    public int compareTo(Object other) {
        if (other instanceof Path) {
            Path otherNode = (Path)other;

            if (getTotal() < otherNode.getTotal()) {
                return -1;
            } else if(getTotal() > otherNode.getTotal()) {
                return 1;
            }
        }

        return 0;
    }
}
