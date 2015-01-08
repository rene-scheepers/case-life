package ui;

import classes.life.Life;

public interface IRender {

    public Life getFollowing();

    public void setFollowing(Life life);

    public void refresh();

}
