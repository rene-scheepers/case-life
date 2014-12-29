package classes.interfaces;

import classes.life.Animal;
import classes.world.Location;

public interface IAnimal {

    public boolean eat(IFood food);

    public boolean move(Location newLocation);

    public boolean reproduce(Animal animal);

}
