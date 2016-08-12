package diy.capmana.scenes;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import diy.capmana.Game;
import diy.capmana.R;
import diy.capmana.Texture;

/**
 * Title game scene.
 */
public class TitleScene extends Scene {

    private static final String TAG = TitleScene.class.getSimpleName();

    private Texture texture;

    public TitleScene() {
        super();
        Log.d(TAG, "TitleScene created");
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
        changeScene(Game.SCENE_TITLE);
    }

    @Override
    public void onSwipeBottom() {
        Log.d(TAG, "onSwipeBottom() called");
        changeScene(Game.SCENE_PLAY);
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
        //Matrix.scaleM(scaleMatrix, 0, 0.0f, 0.0f, 1.0f);
        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, 0.0f, 0.0f, 0.0f);
        Matrix.multiplyMM(tempMatrix, 0, scaleMatrix, 0, translateMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, combineViewProjectMatrix, 0, tempMatrix, 0);
        texture.draw(mvpMatrix);

        computeFPS();
    }

}
