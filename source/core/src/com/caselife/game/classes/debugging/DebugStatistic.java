package com.caselife.game.classes.debugging;

/**
 * Created by Rene on 05-01-2015.
 */
public abstract class DebugStatistic {
    protected String name;
    protected boolean hidden;

    public DebugStatistic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
