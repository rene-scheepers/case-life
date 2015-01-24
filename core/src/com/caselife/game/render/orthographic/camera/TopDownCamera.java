package com.caselife.game.render.orthographic.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.caselife.logic.world.World;

public class TopDownCamera extends OrthographicCamera {

    private float boundsXLower;
    private float boundsXUpper;
    private float boundsYLower;
    private float boundsYUpper;
    private float maxZoom;
    private float minZoom;

    public TopDownCamera(float viewportWidth, float viewportHeight, World world, float scale) {
        super(viewportWidth, viewportHeight);

        this.boundsXLower = -20;
        this.boundsXUpper = world.getWidth() * scale + 20;
        this.boundsYLower = -20;
        this.boundsYUpper = world.getHeight() * scale + 20;
        this.maxZoom = 100f;
        this.minZoom = 0.25f;

        zoom = 2.5f;

        translate(world.getWidth() * scale / 2, world.getHeight() * scale / 2);
        update();
    }


    @Override
    public void update() {
        position.x = Math.max(boundsXLower, position.x);
        position.x = Math.min(boundsXUpper, position.x);

        position.y = Math.max(boundsYLower, position.y);
        position.y = Math.min(boundsYUpper, position.y);

        zoom = Math.min(maxZoom, zoom);
        zoom = Math.max(minZoom, zoom);

        super.update();
    }
}
