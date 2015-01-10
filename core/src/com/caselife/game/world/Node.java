package com.caselife.game.world;

import com.caselife.game.life.Life;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private final World world;
    private final int x;
    private final int y;

    private LocationType type;

    private Life holder;

    public Node(World world, int x, int y, LocationType type) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public List<Node> getAdjacentNodes() {
        List<Node> list = new ArrayList<>(8); // 8 adjacent nodes possible.
        for (int x = this.x - 1; x <= this.x + 1; x++) {
            for (int y = this.y - 1; y <= this.y + 1; y++) {
                Node loopNode = world.getNode(x, y);
                if (this.equals(loopNode) || loopNode == null)
                    continue;
                list.add(loopNode);
            }
        }
        return list;
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
