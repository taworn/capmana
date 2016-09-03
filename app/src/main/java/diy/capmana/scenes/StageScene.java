package diy.capmana.scenes;

import android.graphics.PointF;
import android.opengl.GLES20;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import diy.capmana.Font;
import diy.capmana.Game;
import diy.capmana.game.GameData;

/**
 * Stage scene.
 */
public class StageScene extends Scene {

    private static final String TAG = StageScene.class.getSimpleName();

    private long timeStart;
    private long timeUsed;

    public StageScene(@Nullable Bundle bundle) {
        super(bundle);
        Log.d(TAG, "StageScene created");
        acquire(bundle);
        timeStart = System.currentTimeMillis();
        timeUsed = 0;
        Log.d(TAG, "start stage " + GameData.instance().getStage() + 1);
        if (bundle != null) {
            onRestoreInstanceState(bundle);
        }
        GameData.instance().save();
        GameData.instance().clear();
    }

    @Override
    public void acquire(@Nullable Bundle bundle) {
        Log.d(TAG, "acquire() called");
        super.acquire(bundle);
    }

    @Override
    public void release() {
        Log.d(TAG, "release() called");
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
        timeStart = System.currentTimeMillis();
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
        changeScene(Game.SCENE_PLAY);
    }

    @Override
    public void onSwipeLeft() {
        Log.d(TAG, "onSwipeLeft() called");
        changeScene(Game.SCENE_PLAY);
    }

    @Override
    public void onSwipeRight() {
        Log.d(TAG, "onSwipeRight() called");
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

        Font font = game.getLargeFont();
        String buffer = "Stage " + (GameData.instance().getStage() + 1);
        PointF measure = font.measure(buffer, sx, sy);
        font.draw(buffer, 0 - (measure.x / 2), 0.0f, sx, sy);

        long timeUsed = System.currentTimeMillis() - timeStart;
        timeStart = System.currentTimeMillis();
        this.timeUsed += timeUsed;
        if (this.timeUsed >= 2000)
            Game.instance().changeScene(Game.SCENE_PLAY);

        computeFPS();
    }

}
