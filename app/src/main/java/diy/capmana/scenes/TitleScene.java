package diy.capmana.scenes;

import android.graphics.PointF;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import diy.capmana.Animation;
import diy.capmana.Font;
import diy.capmana.Game;
import diy.capmana.R;
import diy.capmana.Sprite;

/**
 * Title game scene.
 */
public class TitleScene extends Scene {

    private static final String TAG = TitleScene.class.getSimpleName();

    private Font titleFont;
    private Sprite sprite;
    private Animation aniHero;
    private Animation aniDivo;
    private float modelX;

    public TitleScene(@Nullable Bundle bundle) {
        super(bundle);
        Log.d(TAG, "TitleScene created");
        acquire(bundle);

        final int TIME = 300;
        aniHero = new Animation();
        aniHero.add(0, 0, 2, TIME);
        aniHero.add(1, 2, 4, TIME);
        aniHero.add(2, 4, 6, TIME);
        aniHero.add(3, 6, 8, TIME);
        aniHero.use(0);

        aniDivo = new Animation();
        aniDivo.add(0, 8, 10, TIME);
        aniDivo.add(1, 10, 12, TIME);
        aniDivo.add(2, 12, 14, TIME);
        aniDivo.add(3, 14, 16, TIME);
        aniDivo.use(0);
        modelX = 0.0f;

        if (bundle != null) {
            onRestoreInstanceState(bundle);
        }
    }

    @Override
    public void acquire(@Nullable Bundle bundle) {
        super.acquire(bundle);
        Log.d(TAG, "acquire() called");
        titleFont = new Font(Game.instance().getContext(), 64, 0xFFFFFF80, Typeface.create((Typeface) null, Typeface.BOLD));
        sprite = new Sprite(Game.instance().getContext(), R.drawable.pacman, 8, 8);
    }

    @Override
    public void release() {
        Log.d(TAG, "release() called");
        sprite.release();
        titleFont.release();
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
            savedInstanceState.putFloat("modelX", modelX);
            savedInstanceState.putParcelable("aniHero", aniHero);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState()");
        if (savedInstanceState != null) {
            modelX = savedInstanceState.getFloat("modelX");
            aniHero = savedInstanceState.getParcelable("aniHero");
        }
    }

    @Override
    public void onSwipeTop() {
        Log.d(TAG, "onSwipeTop() called");
        changeScene(Game.SCENE_PLAY);
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

        Game game = Game.instance();
        float sx = 2.0f / game.getScreenWidth();
        float sy = 2.0f / game.getScreenHeight();
        String title = "Capman";
        PointF measure = titleFont.measure(title, sx, sy);
        titleFont.draw(title, 0 - (measure.x / 2), 0.3f, sx, sy);

        Font font = game.getMediumFont();
        String menu = "Swipe up/down to Start";
        measure = font.measure(menu, sx, sy);
        font.draw(menu, 0 - (measure.x / 2), -0.5f, sx, sy);

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
        Matrix.scaleM(scaleMatrix, 0, 0.08f, 0.08f, 1.0f);
        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, modelX, 0.0f, 0.0f);
        Matrix.multiplyMM(tempMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, combineViewProjectMatrix, 0, tempMatrix, 0);
        aniHero.draw(mvpMatrix, sprite);

        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, modelX - 0.15f, 0.0f, 0.0f);
        Matrix.multiplyMM(tempMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, combineViewProjectMatrix, 0, tempMatrix, 0);
        aniDivo.draw(mvpMatrix, sprite);

        modelX -= 0.01f;
        if (modelX < -1.0f)
            modelX = 1.0f;

        computeFPS();
    }

}
