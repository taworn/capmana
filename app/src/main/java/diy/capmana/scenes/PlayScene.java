package diy.capmana.scenes;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import diy.capmana.Game;
import diy.capmana.R;
import diy.capmana.Texture;

/**
 * Playing game scene.
 */
public class PlayScene extends Scene {

    private static final String TAG = PlayScene.class.getSimpleName();

    private Texture texture;

    private float modelX = 0.0f;
    private float modelY = 0.0f;
    private float modelDx = 0.0f;
    private float modelDy = 0.0f;

    public PlayScene() {
        super();
        Log.d(TAG, "PlayScene created");
        texture = new Texture();
        texture.load(Game.instance().getContext(), R.drawable.a);
    }

    @Override
    public void release() {
        Log.d(TAG, "release() called");
        texture.release();
        super.release();
    }

    @Override
    public void onSwipeTop() {
        Log.d(TAG, "onSwipeTop() called");
        modelDx = 0.0f;
        modelDy = 0.2f;
    }

    @Override
    public void onSwipeLeft() {
        Log.d(TAG, "onSwipeLeft() called");
        modelDx = -0.2f;
        modelDy = 0.0f;
    }

    @Override
    public void onSwipeRight() {
        Log.d(TAG, "onSwipeRight() called");
        modelDx = 0.2f;
        modelDy = 0.0f;
    }

    @Override
    public void onSwipeBottom() {
        Log.d(TAG, "onSwipeBottom() called");
        modelDx = 0.0f;
        modelDy = -0.2f;
    }

    @Override
    public void draw() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        float[] projectionMatrix = new float[16];
        float[] viewMatrix = new float[16];
        float[] combineViewProjectMatrix = new float[16];
        float[] scaleMatrix = new float[16];
        float[] translateMatrix = new float[16];
        float[] mvpMatrix = new float[16];
        Matrix.orthoM(projectionMatrix, 0, -2.0f, 2.0f, -2.0f, 2.0f, 1.0f, 25.0f);
        Matrix.setLookAtM(viewMatrix, 0,
                0.0f, 0.0f, 1.5f,    // eye
                0.0f, 0.0f, -15.0f,  // at
                0.0f, 1.0f, 0.0f);   // up
        Matrix.multiplyMM(combineViewProjectMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        float[] tempMatrix = new float[16];

        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.scaleM(scaleMatrix, 0, 0.2f, 0.2f, 1.0f);
        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, modelX, modelY, 0);
        if (modelDx > 0.0f && modelX < 8.0f)
            modelX += modelDx;
        else if (modelDx < 0.0f && modelX > -8.0f)
            modelX += modelDx;
        if (modelDy > 0.0f && modelY < 8.0f)
            modelY += modelDy;
        else if (modelDy < 0.0f && modelY > -8.0f)
            modelY += modelDy;
        Matrix.multiplyMM(tempMatrix, 0, scaleMatrix, 0, translateMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, combineViewProjectMatrix, 0, tempMatrix, 0);
        texture.draw(mvpMatrix);

        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.scaleM(scaleMatrix, 0, 0.1f, 0.1f, 1.0f);
        for (int i = -18; i < 19; i += 2) {
            Matrix.setIdentityM(translateMatrix, 0);
            Matrix.translateM(translateMatrix, 0, (float) i, 18.0f, 0);
            Matrix.multiplyMM(tempMatrix, 0, scaleMatrix, 0, translateMatrix, 0);
            Matrix.multiplyMM(mvpMatrix, 0, combineViewProjectMatrix, 0, tempMatrix, 0);
            texture.draw(mvpMatrix);
        }
        for (int i = -18; i < 19; i += 2) {
            Matrix.setIdentityM(translateMatrix, 0);
            Matrix.translateM(translateMatrix, 0, (float) i, -18.0f, 0);
            Matrix.multiplyMM(tempMatrix, 0, scaleMatrix, 0, translateMatrix, 0);
            Matrix.multiplyMM(mvpMatrix, 0, combineViewProjectMatrix, 0, tempMatrix, 0);
            texture.draw(mvpMatrix);
        }
        for (int i = -18; i < 19; i += 2) {
            Matrix.setIdentityM(translateMatrix, 0);
            Matrix.translateM(translateMatrix, 0, 18.0f, (float) i, 0);
            Matrix.multiplyMM(tempMatrix, 0, scaleMatrix, 0, translateMatrix, 0);
            Matrix.multiplyMM(mvpMatrix, 0, combineViewProjectMatrix, 0, tempMatrix, 0);
            texture.draw(mvpMatrix);
        }
        for (int i = -18; i < 19; i += 2) {
            Matrix.setIdentityM(translateMatrix, 0);
            Matrix.translateM(translateMatrix, 0, -18.0f, (float) i, 0);
            Matrix.multiplyMM(tempMatrix, 0, scaleMatrix, 0, translateMatrix, 0);
            Matrix.multiplyMM(mvpMatrix, 0, combineViewProjectMatrix, 0, tempMatrix, 0);
            texture.draw(mvpMatrix);
        }

        computeFPS();
    }

}
