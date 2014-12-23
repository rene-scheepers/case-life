package classes.interfaces;

import classes.life.Animal;
import classes.world.Location;

public interface IAnimal {

    public void eat(IFood food);

    public void move(Location newLocation);

    public void reproduce(Animal animal);

}
