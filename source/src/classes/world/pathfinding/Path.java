package classes.world.pathfinding;

import classes.world.Node;

import java.util.*;
import java.util.function.Consumer;

public class Path implements Comparable, ListIterator<Node> {

    private ListIterator<Node> iterator;
    private List<Node> steps = new ArrayList();
    private double cost;

    private int currentIndex = 0;
    private Node target;

    public Path(NodeHeuristic path) {
        ArrayList<NodeHeuristic> nodes = new ArrayList();
        nodes.add(path);

        cost = path.getTotal();
        target = path.getNode();

        while (path.getParent() != null) {
            nodes.add(path.getParent());
            path = path.getParent();
        }

        Collections.reverse(nodes);

        for (NodeHeuristic node : nodes) {
            steps.add(node.getNode());
        }

        iterator = steps.listIterator();
        if (iterator.hasNext()) {
            next();
        }
    }

    public Node getCurrent() {
        return steps.get(currentIndex);
    }

    public Node getTarget() {
        return target;
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

    public Node last() {
        return steps.get(steps.size() - 1);
    }

    public Node first() {
        return steps.get(0);
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Path) {
            return Double.compare(cost, ((Path) o).getCost());
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
        currentIndex = iterator.nextIndex();
        return iterator.next();
    }

    @Override
    public boolean hasPrevious() {
        return iterator.hasPrevious();
    }

    @Override
    public Node previous() {
        currentIndex = iterator.previousIndex();
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
