package com.caselife.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;

public class StrategyCameraInputController extends CameraInputController {

    static public final float SCROLL_FACTOR = 0.5f;
    static public final float ROTATE_ANGLE = 80f;
    static public final float TRANSLATE_UNITS = 500f;

    public StrategyCameraInputController(StrategyCamera camera) {
        super(camera);

        this.scrollFactor = SCROLL_FACTOR;
        this.rotateAngle = ROTATE_ANGLE;
        this.translateUnits = TRANSLATE_UNITS;

    }

    @Override
    public void update() {
        Vector3 currentViewPosition = new Vector3();

        this.camera.view.getTranslation(currentViewPosition);
        System.out.println(currentViewPosition);
        if (currentViewPosition.y < 0) {
            System.out.println("WOLLW");
        }

        if (this.camera.position.y < 10) {
            this.camera.position.y = 10;
            System.out.println("LEL NUB");

        }
        super.update();
    }
}
