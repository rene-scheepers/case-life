package com.caselife.game.models;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

abstract public class ModelContainer implements RenderableProvider {

    static protected ModelBuilder modelBuilder = new ModelBuilder();

    abstract public void update();

    abstract public ModelInstance getModelInstance();

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        getModelInstance().getRenderables(renderables, pool);
    }
}
