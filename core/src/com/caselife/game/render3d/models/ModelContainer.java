package com.caselife.game.render3d.models;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.caselife.logic.life.Life;

abstract public class ModelContainer {

    static protected ModelBuilder modelBuilder = new ModelBuilder();

    abstract public void update();

    abstract public ModelInstance getModelInstance();

    abstract public Life getLife();

}
