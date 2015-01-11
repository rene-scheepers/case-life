package com.caselife.game.camera;

import com.badlogic.gdx.graphics.PerspectiveCamera;

public class StrategyCamera extends PerspectiveCamera {

    private float boundsXLower;
    private float boundsXUpper;
    private float boundsYLower;
    private float boundsYUpper;
    private float boundsZLower;
    private float boundsZUpper;

    public StrategyCamera(float fieldOfViewY, float viewportWidth, float viewportHeight, float boundsXLower, float boundsXUpper, float boundsYLower, float boundsYUpper, float boundsZLower, float boundsZUpper) {
        super(fieldOfViewY, viewportWidth, viewportHeight);

        this.near = 1f;
        this.far = 2000f;

        this.boundsXLower = boundsXLower;
        this.boundsXUpper = boundsXUpper;
        this.boundsYLower = boundsYLower;
        this.boundsYUpper = boundsYUpper;
        this.boundsZLower = boundsZLower;
        this.boundsZUpper = boundsZUpper;
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
