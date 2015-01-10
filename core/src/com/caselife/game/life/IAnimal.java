package com.caselife.game.life;

import com.caselife.game.world.Node;

public interface IAnimal {

    public boolean eat(IFood food);

    public boolean move(Node newNode);

    public boolean propagate(Animal animal);

}
