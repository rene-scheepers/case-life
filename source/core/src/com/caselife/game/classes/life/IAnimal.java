package com.caselife.game.classes.life;

import com.caselife.game.classes.world.Node;

public interface IAnimal {

    public boolean eat(IFood food);

    public boolean move(Node newNode);

    public boolean propagate(Animal animal);

}
