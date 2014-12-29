package classes.world;

import java.util.ArrayList;

/**
 * Created by Rene on 29-12-2014.
 */
public class Path {

    private ArrayList<Location> locations = new ArrayList();

    public Path() {

    }

    public void addLocation(Location location) {
        locations.add(location);
    }

    public Location getIndex(int i) {
        return locations.get(i);
    }

}
