package com.caselife.game;

import com.badlogic.gdx.graphics.PerspectiveCamera;

public class StrategyCamera extends PerspectiveCamera
{

    public StrategyCamera(float fieldOfViewY, float viewportWidth, float viewportHeight) {
        super(fieldOfViewY, viewportWidth, viewportHeight);

        this.near = 1f;
        this.far = 3000f;
    }

    @Override
    public void update() {

        position.x = Math.max(-40, position.x);
        position.x = Math.min(540, position.x);

        position.y = Math.max(40, position.y);
        position.y = Math.min(200, position.y);

        position.z = Math.max(-40, position.z);
        position.z = Math.min(540, position.z);


        super.update();

    }
}
