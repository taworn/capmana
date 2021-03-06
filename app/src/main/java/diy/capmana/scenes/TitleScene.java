package diy.capmana.scenes;

import android.content.res.Configuration;
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
import diy.capmana.game.GameData;

/**
 * Title game scene.
 */
public class TitleScene extends Scene {

    private static final String TAG = TitleScene.class.getSimpleName();

    private Font titleFont;
    private Sprite sprite;
    private Sprite spriteUI;
    private Animation aniDivo;
    private Animation aniHero;
    private float modelX;
    private int menuIndex;

    public TitleScene(@Nullable Bundle bundle) {
        super(bundle);
        Log.d(TAG, "TitleScene created");
        acquire(bundle);

        final int TIME = 300;
        aniDivo = new Animation();
        aniDivo.add(0, 8, 10, Animation.ON_END_CONTINUE, TIME);
        aniDivo.add(1, 10, 12, Animation.ON_END_CONTINUE, TIME);
        aniDivo.add(2, 12, 14, Animation.ON_END_CONTINUE, TIME);
        aniDivo.add(3, 14, 16, Animation.ON_END_CONTINUE, TIME);
        aniDivo.use(0);

        aniHero = new Animation();
        aniHero.add(0, 0, 2, Animation.ON_END_CONTINUE, TIME);
        aniHero.add(1, 2, 4, Animation.ON_END_CONTINUE, TIME);
        aniHero.add(2, 4, 6, Animation.ON_END_CONTINUE, TIME);
        aniHero.add(3, 6, 8, Animation.ON_END_CONTINUE, TIME);
        aniHero.use(0);

        modelX = 0.0f;
        menuIndex = 0;

        if (bundle != null) {
            onRestoreInstanceState(bundle);
        }
    }

    @Override
    public void acquire(@Nullable Bundle bundle) {
        Log.d(TAG, "acquire() called");
        super.acquire(bundle);
        titleFont = new Font(Game.instance().getContext(), 64, 0xFFFFFF80, Typeface.create((Typeface) null, Typeface.BOLD));
        sprite = new Sprite(Game.instance().getContext(), R.drawable.pacman, 8, 8);
        spriteUI = new Sprite(Game.instance().getContext(), R.drawable.ui, 2, 2);
    }

    @Override
    public void release() {
        Log.d(TAG, "release() called");
        spriteUI.release();
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
        Log.d(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onSaveInstanceState()");
        if (savedInstanceState != null) {
            savedInstanceState.putParcelable("aniDivo", aniDivo);
            savedInstanceState.putParcelable("aniHero", aniHero);
            savedInstanceState.putFloat("modelX", modelX);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            aniDivo = savedInstanceState.getParcelable("aniDivo");
            aniHero = savedInstanceState.getParcelable("aniHero");
            modelX = savedInstanceState.getFloat("modelX");
        }
    }

    @Override
    public void onSwipeTop() {
        Log.d(TAG, "onSwipeTop() called");
        GameData.instance().reset();
        changeScene(Game.SCENE_STAGE);
    }

    @Override
    public void onSwipeBottom() {
        Log.d(TAG, "onSwipeBottom() called");
        //GameData.instance().reset();
        changeScene(Game.SCENE_STAGE);
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

        String buffer;
        Font font = game.getMediumFont();
        buffer = "Start";
        measure = font.measure(buffer, sx, sy);
        font.draw(buffer, 0 - (measure.x / 2), -0.10f, sx, sy);
        buffer = "Continue";
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

        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.scaleM(scaleMatrix, 0, 0.04f, 0.04f, 1.0f);

        if (Game.instance().getContext().getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            Matrix.setIdentityM(translateMatrix, 0);
            Matrix.translateM(translateMatrix, 0, -0.32f, -0.05f, 0.0f);
            Matrix.multiplyMM(tempMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
            Matrix.multiplyMM(mvpMatrix, 0, combineViewProjectMatrix, 0, tempMatrix, 0);
            spriteUI.draw(mvpMatrix, 2);

            Matrix.setIdentityM(translateMatrix, 0);
            Matrix.translateM(translateMatrix, 0, -0.32f, -0.20f, 0.0f);
            Matrix.multiplyMM(tempMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
            Matrix.multiplyMM(mvpMatrix, 0, combineViewProjectMatrix, 0, tempMatrix, 0);
            spriteUI.draw(mvpMatrix, 3);
        }
        else {
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
        }

        computeFPS();
    }

}
