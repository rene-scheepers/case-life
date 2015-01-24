package com.caselife.game.render.orthographic.camera;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class TopDownCameraInputController extends InputAdapter {

    static public final int LEFT_KEY = Input.Keys.A;
    static public final int RIGHT_KEY = Input.Keys.D;
    static public final int UP_KEY = Input.Keys.W;
    static public final int DOWN_KEY = Input.Keys.S;
    static public final int ZOOM_IN_KEY = Input.Keys.PLUS;
    static public final int ZOOM_OUT_KEY = Input.Keys.MINUS;

    private boolean zoomInPressed;
    private boolean zoomOutPressed;
    private boolean leftKeyPressed;
    private boolean rightKeyPressed;
    private boolean upKeyPressed;
    private boolean downKeyPressed;

    private TopDownCamera camera;

    public TopDownCameraInputController(TopDownCamera camera) {
        this.camera = camera;
    }

    public void update() {
        if (leftKeyPressed || rightKeyPressed || upKeyPressed || downKeyPressed || zoomInPressed || zoomOutPressed) {
            if (leftKeyPressed) {
                camera.translate(-5f * camera.zoom * 5, 0);
            }

            if (rightKeyPressed) {
                camera.translate(5f * camera.zoom * 5, 0);
            }

            if (upKeyPressed) {
                camera.translate(0, 5f * camera.zoom * 5);
            }

            if (downKeyPressed) {
                camera.translate(0, -5f * camera.zoom * 5);
            }

            if (zoomInPressed) {
                camera.zoom -= 0.05f;
            }

            if (zoomOutPressed) {
                camera.zoom += 0.05f;
            }

            camera.update();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case LEFT_KEY:
                leftKeyPressed = true;
                break;
            case RIGHT_KEY:
                rightKeyPressed = true;
                break;
            case UP_KEY:
                upKeyPressed = true;
                break;
            case DOWN_KEY:
                downKeyPressed = true;
                break;
            case ZOOM_IN_KEY:
            case Input.Keys.EQUALS:
                zoomInPressed = true;
                break;
            case ZOOM_OUT_KEY:
                zoomOutPressed = true;
                break;
        }

        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case LEFT_KEY:
                leftKeyPressed = false;
                break;
            case RIGHT_KEY:
                rightKeyPressed = false;
                break;
            case UP_KEY:
                upKeyPressed = false;
                break;
            case DOWN_KEY:
                downKeyPressed = false;
                break;
            case ZOOM_IN_KEY:
            case Input.Keys.EQUALS:
                zoomInPressed = false;
                break;
            case ZOOM_OUT_KEY:
                zoomOutPressed = false;
                break;
        }

        return super.keyUp(keycode);
    }

}
