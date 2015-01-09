package com.caselife.game.classes.world.pathfinding;

import com.caselife.game.classes.world.Node;

public class NodeHeuristic implements Comparable {

    private Node node;
    private double cost;
    private double heuristic;
    private NodeHeuristic parent;

    public NodeHeuristic(Node node) {
        this.node = node;
    }

    public NodeHeuristic(Node node, double cost, double heuristic) {
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

    public double getCost() {
        return cost;
    }

    public double getHeuristic() {
        return heuristic;
    }

    public double getTotal() {
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
