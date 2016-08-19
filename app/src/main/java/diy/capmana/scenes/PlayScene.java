package diy.capmana.scenes;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import diy.capmana.Animation;
import diy.capmana.Game;
import diy.capmana.R;
import diy.capmana.Sprite;

/**
 * Playing game scene.
 */
public class PlayScene extends Scene {

    private static final String TAG = PlayScene.class.getSimpleName();

    private Sprite sprite;
    private Animation aniHero;
    private Animation[] aniDivoes = new Animation[4];

    public PlayScene(@Nullable Bundle bundle) {
        super(bundle);
        Log.d(TAG, "PlayScene created");
        acquire(bundle);

        final int TIME = 300;
        aniHero = new Animation();
        aniHero.add(0, 0, 2, TIME);
        aniHero.add(1, 2, 4, TIME);
        aniHero.add(2, 4, 6, TIME);
        aniHero.add(3, 6, 8, TIME);
        aniHero.use(0);

        for (int i = 0; i < 4; i++) {
            int j = (i + 1) * 8;
            aniDivoes[i] = new Animation();
            aniDivoes[i].add(0, j, j + 2, TIME);
            aniDivoes[i].add(1, j + 2, j + 4, TIME);
            aniDivoes[i].add(2, j + 4, j + 6, TIME);
            aniDivoes[i].add(3, j + 6, j + 8, TIME);
            aniDivoes[i].use(0);
        }

        if (bundle != null) {
            onRestoreInstanceState(bundle);
        }
    }

    @Override
    public void acquire(@Nullable Bundle bundle) {
        super.acquire(bundle);
        Log.d(TAG, "acquire() called");
        sprite = new Sprite(Game.instance().getContext(), R.drawable.pacman, 8, 8);
    }

    @Override
    public void release() {
        Log.d(TAG, "release() called");
        sprite.release();
        super.release();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onSaveInstanceState()");
        if (savedInstanceState != null) {
            savedInstanceState.putParcelable("aniHero", aniHero);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState()");
        if (savedInstanceState != null) {
            aniHero = savedInstanceState.getParcelable("aniHero");
        }
    }

    @Override
    public void onSwipeTop() {
        Log.d(TAG, "onSwipeTop() called");
        aniHero.setVelocity(0.0f, 0.02f);
        aniHero.use(2);
    }

    @Override
    public void onSwipeLeft() {
        Log.d(TAG, "onSwipeLeft() called");
        aniHero.setVelocity(-0.02f, 0.0f);
        aniHero.use(0);
    }

    @Override
    public void onSwipeRight() {
        Log.d(TAG, "onSwipeRight() called");
        aniHero.setVelocity(0.02f, 0.0f);
        aniHero.use(1);
    }

    @Override
    public void onSwipeBottom() {
        Log.d(TAG, "onSwipeBottom() called");
        aniHero.setVelocity(0.0f, -0.02f);
        aniHero.use(3);
    }

    @Override
    public void draw() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        float[] projectionMatrix = new float[16];
        float[] viewMatrix = new float[16];
        float[] combineViewProjectMatrix = new float[16];
        float[] translateMatrix = new float[16];
        float[] scaleMatrix = new float[16];
        float[] mvpMatrix = new float[16];
        Matrix.orthoM(projectionMatrix, 0, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 25.0f);
        Matrix.setLookAtM(viewMatrix, 0,
                0.0f, 0.0f, 1.5f,    // eye
                0.0f, 0.0f, -15.0f,  // at
                0.0f, 1.0f, 0.0f);   // up
        Matrix.multiplyMM(combineViewProjectMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        float[] tempMatrix = new float[16];

        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.scaleM(scaleMatrix, 0, 0.05f, 0.05f, 1.0f);
        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, aniHero.getCurrentX(), aniHero.getCurrentY(), 0);
        Matrix.multiplyMM(tempMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, combineViewProjectMatrix, 0, tempMatrix, 0);
        boolean enableX = false, enableY = false;
        if (aniHero.getVelocityX() > 0.0f && aniHero.getCurrentX() < 0.95f)
            enableX = true;
        else if (aniHero.getVelocityX() < 0.0f && aniHero.getCurrentX() > -0.95f)
            enableX = true;
        if (aniHero.getVelocityY() > 0.0f && aniHero.getCurrentY() < 0.95f)
            enableY = true;
        else if (aniHero.getVelocityY() < 0.0f && aniHero.getCurrentY() > -0.95f)
            enableY = true;
        aniHero.playFrame(enableX, enableY);
        aniHero.draw(mvpMatrix, sprite);

        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, -0.5f, 0.5f, 0);
        Matrix.multiplyMM(tempMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, combineViewProjectMatrix, 0, tempMatrix, 0);
        aniDivoes[0].draw(mvpMatrix, sprite);

        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, 0.5f, 0.5f, 0);
        Matrix.multiplyMM(tempMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, combineViewProjectMatrix, 0, tempMatrix, 0);
        aniDivoes[1].draw(mvpMatrix, sprite);

        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, -0.5f, -0.5f, 0);
        Matrix.multiplyMM(tempMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, combineViewProjectMatrix, 0, tempMatrix, 0);
        aniDivoes[2].draw(mvpMatrix, sprite);

        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, 0.5f, -0.5f, 0);
        Matrix.multiplyMM(tempMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, combineViewProjectMatrix, 0, tempMatrix, 0);
        aniDivoes[3].draw(mvpMatrix, sprite);

        computeFPS();
    }

}
