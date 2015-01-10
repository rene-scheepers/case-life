package com.caselife.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;

public class StrategyCameraInputController extends CameraInputController {

    static public final float ROTATE_ANGLE = 80f;
    static public final float TRANSLATE_UNITS = 500f;

    public StrategyCameraInputController(StrategyCamera camera) {
        super(camera);

        this.scrollFactor = 0.025f;
        this.rotateAngle = ROTATE_ANGLE;
        this.translateUnits = TRANSLATE_UNITS;
    }
}
