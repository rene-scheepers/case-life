package ui;

import life.game.classes.life.Life;
import life.game.classes.world.Node;

public interface IRender {

    public Node getCenter();

    public void setCenter(Node center);

    public Life getFollowing();

    public void setFollowing(Life follow);

    public void refresh();

}
