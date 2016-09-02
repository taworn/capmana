package diy.capmana.scenes;

import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import diy.capmana.Font;
import diy.capmana.Game;
import diy.capmana.R;
import diy.capmana.Sprite;

/**
 * Game over scene.
 */
public class GameOverScene extends Scene {

    private static final String TAG = GameOverScene.class.getSimpleName();

    private Sprite spriteUI;
    private int menuIndex = 0;

    public GameOverScene(@Nullable Bundle bundle) {
        super(bundle);
        Log.d(TAG, "GameOverScene created");
        acquire(bundle);
        if (bundle != null) {
            onRestoreInstanceState(bundle);
        }
    }

    @Override
    public void acquire(@Nullable Bundle bundle) {
        Log.d(TAG, "acquire() called");
        super.acquire(bundle);
        spriteUI = new Sprite(Game.instance().getContext(), R.drawable.ui, 2, 2);
    }

    @Override
    public void release() {
        Log.d(TAG, "release() called");
        spriteUI.release();
        super.release();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSwipeTop() {
        Log.d(TAG, "onSwipeTop() called");
        changeScene(Game.SCENE_STAGE);
    }

    @Override
    public void onSwipeBottom() {
        Log.d(TAG, "onSwipeBottom() called");
        changeScene(Game.SCENE_TITLE);
    }

    @Override
    public void draw() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        Game game = Game.instance();
        float sx = 2.0f / game.getScreenWidth();
        float sy = 2.0f / game.getScreenHeight();

        Font font = game.getLargeFont();
        String buffer = "Game Over";
        PointF measure = font.measure(buffer, sx, sy);
        font.draw(buffer, 0 - (measure.x / 2), 0.25f, sx, sy);

        font = game.getMediumFont();
        buffer = "Continue";
        measure = font.measure(buffer, sx, sy);
        font.draw(buffer, 0 - (measure.x / 2), -0.10f, sx, sy);
        buffer = "Restart";
        measure = font.measure(buffer, sx, sy);
        font.draw(buffer, 0 - (measure.x / 2), -0.25f, sx, sy);

        float[] projectionMatrix = new float[16];
        float[] viewMatrix = new float[16];
        float[] combineViewProjectMatrix = new float[16];
        float[] translateMatrix = new float[16];
        float[] scaleMatrix = new float[16];
        float[] mvpMatrix = new float[16];
        float[] tempMatrix = new float[16];

        Matrix.orthoM(projectionMatrix, 0, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 25.0f);
        Matrix.setLookAtM(viewMatrix, 0,
                0.0f, 0.0f, 1.5f,    // eye
                0.0f, 0.0f, -15.0f,  // at
                0.0f, 1.0f, 0.0f);   // up
        Matrix.multiplyMM(combineViewProjectMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.scaleM(scaleMatrix, 0, 0.04f, 0.04f, 1.0f);

        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, -0.22f, -0.02f, 0.0f);
        Matrix.multiplyMM(tempMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, combineViewProjectMatrix, 0, tempMatrix, 0);
        spriteUI.draw(mvpMatrix, 2);

        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, -0.22f, -0.19f, 0.0f);
        Matrix.multiplyMM(tempMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, combineViewProjectMatrix, 0, tempMatrix, 0);
        spriteUI.draw(mvpMatrix, 3);

        computeFPS();
    }

}
