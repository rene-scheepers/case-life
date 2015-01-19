package com.caselife.logic.life;

import com.caselife.logic.world.Node;

public interface IAnimal {

    /**
     *
     * @param food
     * @return
     */
    public boolean eat(IFood food);

    /**
     *
     * @param newNode
     * @return
     */
    public boolean move(Node newNode);

    /**
     *
     * @param animal
     * @return
     */
    public boolean propagate(Animal animal);

}
