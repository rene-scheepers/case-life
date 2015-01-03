package classes.world;

import java.util.*;
import java.util.function.Consumer;

public class Path implements Comparable, ListIterator<Node> {

    private ListIterator<Node> iterator;
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

        iterator = steps.listIterator();
        if (iterator.hasNext()) {
            iterator.next();
        }
    }

    public double getCost() {
        return cost;
    }

    public int getSize() {
        return steps.size();
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

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Node next() {
        return iterator.next();
    }

    @Override
    public boolean hasPrevious() {
        return iterator.hasPrevious();
    }

    @Override
    public Node previous() {
        return iterator.previous();
    }

    @Override
    public int nextIndex() {
        return iterator.nextIndex();
    }

    @Override
    public int previousIndex() {
        return iterator.previousIndex();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public void set(Node node) {
        iterator.set(node);
    }

    @Override
    public void add(Node node) {
        iterator.add(node);
    }

    @Override
    public void forEachRemaining(Consumer<? super Node> action) {
        iterator.forEachRemaining(action);
    }
}
