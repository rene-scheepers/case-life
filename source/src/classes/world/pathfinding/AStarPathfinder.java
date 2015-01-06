package classes.world.pathfinding;

import classes.SortedList;
import classes.world.Node;

import java.util.ArrayList;
import java.util.Map;

public class AStarPathfinder implements IPathfinder {

    private ArrayList<Path> registeredPaths = new ArrayList();

    private SortedList<NodeHeuristic> openNodes;
    private ArrayList<Node> closedNodes;

    public Path getPath(double[][] valueMap, Node origin, Node target) {
        openNodes = new SortedList();
        closedNodes = new ArrayList();

        openNodes.add(new NodeHeuristic(origin));
        while (openNodes.getSize() >= 1) {
            NodeHeuristic current = openNodes.getFirst();
            Node currentNode = current.getNode();
            for (Node node : currentNode.getAdjacentNodes()) {
                if (node.equals(target)) {
                    NodeHeuristic targetNodeHeuristic = new NodeHeuristic(node, current.getCost(), 0);
                    targetNodeHeuristic.setParent(current);
                    return new Path(targetNodeHeuristic);
                }

                double cost = valueMap[node.getX()][node.getY()];
                if (cost < 0) {
                    continue;
                }
                
                if (node.getHolder() != null) {
                    continue;
                }

                boolean alreadyWalked = false;
                if (closedNodes.contains(node)) {
                    alreadyWalked = true;
                }

                if (!alreadyWalked) {
                    for (NodeHeuristic walkableNode : openNodes.getObjects()) {
                        if (walkableNode.getNode().equals(node)) {
                            alreadyWalked = true;
                            break;
                        }
                    }
                }

                if (!alreadyWalked) {
                    double heuristic;

                    // If the tile is diagonal from the current tile the cost should be multiplied by 1.4.
                    if (node.getX() != currentNode.getX() && node.getY() != currentNode.getY()) {
                        cost = (double) Math.round(cost * 1.4);
                    }

                    int width = Math.abs(origin.getX() - target.getX());
                    int height = Math.abs(origin.getY() - target.getY());
                    double distance = Math.max(width, height);

                    heuristic = distance * cost;

                    if (current.getParent() != null) {
                        cost += current.getCost();
                    }

                    NodeHeuristic adjacent = new NodeHeuristic(node, cost, heuristic);
                    adjacent.setParent(current);
                    openNodes.add(adjacent);
                }

            }

            closedNodes.add(current.getNode());
            openNodes.remove(current);
        }

        return null;
    }

    public ArrayList<Path> getRegisteredPaths() {
        return registeredPaths;
    }

    @Override
    public void registerPath(Path path) {
        registeredPaths.add(path);
    }

    @Override
    public void unRegisterPath(Path path) {
        registeredPaths.remove(path);
    }
}
