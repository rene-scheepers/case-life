package com.caselife.game.render3d.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;

public class StrategyCameraInputController extends CameraInputController {

    static public final float SCROLL_FACTOR = 0.025f;
    static public final float ROTATE_ANGLE = 120f;
    static public final float TRANSLATE_UNITS = 500f;
    
    static public final int STRAFE_LEFT_KEY = Input.Keys.A;
    static public final int STRAFE_RIGHT_KEY = Input.Keys.D;
    static public final int MOVE_FORWARD_KEY = Input.Keys.W; 
    static public final int MOVE_BACKWARD_KEY = Input.Keys.S; 

    private boolean strafeLeftKeyPressed;
    private boolean strafeRightKeyPressed;
    private boolean moveForwardKeyPressed;
    private boolean moveBackwardKeyPressed;
    

    public StrategyCameraInputController(StrategyCamera camera) {
        super(camera);

        this.scrollFactor = SCROLL_FACTOR;
        this.rotateAngle = ROTATE_ANGLE;
        this.translateUnits = TRANSLATE_UNITS;

        this.target = new Vector3(250,30,250);
        this.rotateRightKey = 0;
        this.rotateLeftKey = 0;
        this.forwardKey = 0;
        this.backwardKey = 0;
    }

    @Override
    public void update()
    {
        if (strafeLeftKeyPressed || strafeRightKeyPressed || moveBackwardKeyPressed || moveForwardKeyPressed) {
            final Vector3 vector = new Vector3();
            final float delta = Gdx.graphics.getDeltaTime();
            if (strafeLeftKeyPressed) {
                vector.set(camera.direction).crs(camera.up).nor().scl(delta * -translateUnits);
                camera.position.add(vector);
                camera.update();
            }

            if (strafeRightKeyPressed) {
                vector.set(camera.direction).crs(camera.up).nor().scl(delta * translateUnits);
                camera.position.add(vector);
            }

            if (moveForwardKeyPressed) {
                vector.set(camera.direction).nor().scl(delta * translateUnits);
                vector.y = 0;
                camera.position.add(vector);
            }

            if (moveBackwardKeyPressed) {
                vector.set(camera.direction).nor().scl(delta * -translateUnits);
                vector.y = 0;
                camera.position.add(vector);
            }

            if (autoUpdate) {
                camera.update();
            }
        }
    }

    @Override
    public boolean keyDown(int keycode)
    {

        switch (keycode) {
            case STRAFE_LEFT_KEY:
                strafeLeftKeyPressed = true;
                break;
            case STRAFE_RIGHT_KEY:
                strafeRightKeyPressed = true;
                break;
            case MOVE_FORWARD_KEY:
                moveForwardKeyPressed = true;
                break;
            case MOVE_BACKWARD_KEY:
                moveBackwardKeyPressed = true;
                break;
        }
        
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode)
    {
        switch (keycode) {
            case STRAFE_LEFT_KEY:
                strafeLeftKeyPressed = false;
                break;
            case STRAFE_RIGHT_KEY:
                strafeRightKeyPressed = false;
                break;
            case MOVE_FORWARD_KEY:
                moveForwardKeyPressed = false;
                break;
            case MOVE_BACKWARD_KEY:
                moveBackwardKeyPressed = false;
                break;
        }

        return super.keyUp(keycode);
    }

}
