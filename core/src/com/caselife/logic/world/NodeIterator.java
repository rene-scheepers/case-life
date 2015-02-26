package com.caselife.logic.world;

import java.util.Iterator;

public class NodeIterator implements Iterator<Node> {
    protected int x;
    protected int y;
    protected Node[][] nodes;
    protected boolean isFirst;

    public NodeIterator(Node[][] nodes) {
        isFirst = true;
        this.nodes = nodes;
    }

    /**
     * Decreases the position of the cursor.
     *
     * @return True if position was changed.
     */
    protected boolean decrease() {
        if (x == 0) {
            if (y == 0)
                return false;
            y--;
        } else {
            x--;
            y = nodes[x].length - 1;
        }
        return true;
    }

    /**
     * Increases the position of the cursor.
     *
     * @return True if position was changed.
     */
    protected boolean increase() {
        if (y == nodes[x].length - 1) {
            if (x == nodes.length - 1)
                return false;
            y = 0;
            x++;
        } else {
            y++;
        }
        return true;
    }

    @Override
    public boolean hasNext() {
        while (current() == null) {
            if (!isFirst) {
                if (!increase()) return false;
            }
            isFirst = false;
        }
        return x < nodes.length - 1 || (x == nodes.length - 1 && y < nodes[x].length - 1);
    }

    /**
     * Retrieves the Node at the current cursor position.
     *
     * @return Node at the current position of the cursor.
     */
    public Node current() {
        return nodes[x][y];
    }

    @Override
    public Node next() {
        increase();
        return current();
    }
}
