package classes.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Rene on 3-1-2015.
 */
public class Path implements Comparable {

    private List<Node> steps = new ArrayList();
    private double cost;

    public Path(NodeHeuristic path) {
        ArrayList<NodeHeuristic> nodes = new ArrayList();
        nodes.add(path);
        cost = path.getTotal();
        while(path.getParent() != null) {
            nodes.add(path.getParent());
            path = path.getParent();
        }

        Collections.reverse(nodes);

        for(NodeHeuristic node : nodes) {
            steps.add(node.getNode());
        }
    }

    public Node getLastNode() {
        return steps.get(steps.size() - 1);
    }

    public double getCost() {
        return cost;
    }

    public int getSize() {
        return steps.size();
    }


    public Node getNextStep(Node node) {
        int index = steps.indexOf(node) + 1;
        if (index < steps.size()) {
            return steps.get(index);
        }
        return null;
    }

    public boolean contains(NodeHeuristic node) {
        return steps.contains(node);
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Path) {
            return Double.compare(cost, ((Path)o).getCost());
        } else {
            return 0;
        }
    }
}
