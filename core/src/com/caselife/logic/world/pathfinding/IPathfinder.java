package com.caselife.logic.world.pathfinding;

import com.caselife.logic.world.Node;

import java.util.ArrayList;
import java.util.List;

public interface IPathfinder {

    public Path getPath(double[][] valueMap, Node origin, Node target);

    public void registerPath(Path path);

    public void unRegisterPath(Path path);

    public List<Path> getRegisteredPaths();

}
