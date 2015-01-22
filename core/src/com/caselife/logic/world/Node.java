package com.caselife.logic.world;

import com.caselife.logic.life.Life;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private final World world;
    private final int x;
    private final int y;
    private List<Node> adjacentNodes;

    private LocationType type;

    private Life holder;

    public Node(World world, int x, int y, LocationType type) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.type = type;
    }
    
    private void loadAdjacentNodes()
    {
        adjacentNodes = new ArrayList();
        
        adjacentNodes.add(world.getNode(x - 1, y + 1));
        adjacentNodes.add(world.getNode(x, y + 1));
        adjacentNodes.add(world.getNode(x + 1, y + 1));

        adjacentNodes.add(world.getNode(x - 1, y));
        adjacentNodes.add(world.getNode(x + 1, y));

        adjacentNodes.add(world.getNode(x - 1, y - 1));
        adjacentNodes.add(world.getNode(x, y - 1));
        adjacentNodes.add(world.getNode(x + 1, y - 1));
    }

    public List<Node> getAdjacentNodes() {
        if (adjacentNodes == null) {
            loadAdjacentNodes();
        }

        return adjacentNodes;
    }

    public Life getHolder() {
        return holder;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public LocationType getLocationType() {
        return type;
    }

    public boolean setHolder(Life object) {
        if (holder == null) {
            holder = object;
        } else {
            return false;
        }
        return true;
    }

    public void unsetHolder() {
        holder = null;
    }

    @Override
    public String toString() {
        return String.format("X: %s, Y: %s, Type: %s, Holder: %s", x, y, type.name(), (holder != null ? holder.toString() : "null"));
    }
}
