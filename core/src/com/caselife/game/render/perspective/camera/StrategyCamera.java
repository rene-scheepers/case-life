package com.caselife.game.render.perspective.camera;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.caselife.logic.world.World;

public class StrategyCamera extends PerspectiveCamera {

    private float boundsXLower;
    private float boundsXUpper;
    private float boundsYLower;
    private float boundsYUpper;
    private float boundsZLower;
    private float boundsZUpper;

    public StrategyCamera(float fieldOfViewY, float viewportWidth, float viewportHeight, World world, float scale) {
        super(fieldOfViewY, viewportWidth, viewportHeight);

        this.near = 1f;
        this.far = 2000f;

        this.boundsXLower = -20;
        this.boundsXUpper = world.getWidth() * scale + 20;
        this.boundsYLower = 30;
        this.boundsYUpper = 200;
        this.boundsZLower = -20;
        this.boundsZUpper = world.getHeight() * scale + 20;

        position.set(world.getWidth() * scale / 2, 150, world.getHeight() * scale);
        update();
    }


    @Override
    public void update() {
        position.x = Math.max(boundsXLower, position.x);
        position.x = Math.min(boundsXUpper, position.x);

        position.y = Math.max(boundsYLower, position.y);
        position.y = Math.min(boundsYUpper, position.y);

        position.z = Math.max(boundsZLower, position.z);
        position.z = Math.min(boundsZUpper, position.z);

        super.update();

    }
}
