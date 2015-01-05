package classes.interfaces;

import classes.world.Node;
import classes.world.pathfinding.Path;

import java.util.ArrayList;
import java.util.Map;

public interface IPathfinder {

    public Path getPath(Map<Node, Double> valueMap, Node origin, Node target);

    public void registerPath(Path path);

    public void unRegisterPath(Path path);

    public ArrayList<Path> getRegisteredPaths();

}
