package com.caselife.game.ui;

import com.caselife.game.classes.life.Life;
import com.caselife.game.classes.world.Node;

public interface IRender {

    public Node getCenter();

    public void setCenter(Node center);

    public Life getFollowing();

    public void setFollowing(Life follow);

    public void refresh();

}
