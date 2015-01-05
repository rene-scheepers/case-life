package classes.debugging;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Created by Rene on 05-01-2015.
 */
public class DebugStatistic {
    private String name;
    private Callable<String> displayedValue;

    public DebugStatistic(String name, Callable<String> displayedValue) {
        this.name = name;
        this.displayedValue = displayedValue;
    }

    public Callable<String> getDisplayedValue() {
        return displayedValue;
    }

    public void setDisplayedValue(Callable<String> displayedValue) {
        this.displayedValue = displayedValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String displayText() {
        try {
            return getName() + ": " + displayedValue.call();
        } catch (Exception e) {
            return getName() + ": ERROR RETRIEVING VALUE.";
        }
    }

    @Override
    public String toString() {
        return "DebugStatistic{" +
                "name='" + name + '\'' +
                ", displayedValue=" + displayedValue +
                '}';
    }
}
