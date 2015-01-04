package classes.interfaces;

import classes.life.Animal;
import classes.world.Node;

public interface IAnimal {

    public boolean eat(IFood food);

    public boolean move(Node newNode);

    public boolean propagate(Animal animal);

}
