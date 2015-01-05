package classes.debugging;

import java.util.concurrent.Callable;

/**
 * Created by Anton on 1/5/2015.
 */
public class DebugValue extends DebugStatistic {
    private Callable<String> displayedValue;

    public DebugValue(String name, Callable<String> displayedValue) {
        super(name);
        this.displayedValue = displayedValue;
    }

    public Callable<String> getDisplayedValue() {
        return displayedValue;
    }

    public void setDisplayedValue(Callable<String> displayedValue) {
        this.displayedValue = displayedValue;
    }

    public String displayText() {
        try {
            return getName() + ": " + (displayedValue != null ? displayedValue.call() : "NULL FUNCTION");
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
