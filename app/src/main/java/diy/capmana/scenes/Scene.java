package diy.capmana.scenes;

import android.graphics.PointF;
import android.opengl.GLES20;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import diy.capmana.Font;
import diy.capmana.Game;

/**
 * A base game scene.
 */
public class Scene {

    private static final String TAG = Scene.class.getSimpleName();

    private int nextSceneId;
    private int fps;
    private int frameCount;
    private long timeStart;

    /**
     * Constructs a game scene.
     */
    public Scene(@Nullable Bundle bundle) {
        Log.d(TAG, "Scene created");
        nextSceneId = -1;
        fps = 0;
        frameCount = 0;
        timeStart = System.currentTimeMillis();
    }

    /**
     * Acquires game resources in current scene.
     */
    public void acquire(@Nullable Bundle bundle) {
        Log.d(TAG, "acquire() called");
    }

    /**
     * Releases game resources in current scene.
     */
    public void release() {
        Log.d(TAG, "release() called");
    }

    /**
     * Called when activity pause.
     */
    public void onPause() {
        Log.d(TAG, "onPause()");
    }

    /**
     * Called when activity resume.
     */
    public void onResume() {
        Log.d(TAG, "onResume()");
        frameCount = 0;
        timeStart = System.currentTimeMillis();
    }

    /**
     * Called when activity need to save instance state.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onSaveInstanceState()");
    }

    /**
     * Called when activity need to restore instance state.
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState()");
    }

    /**
     * Called when user swipe to top.
     */
    public void onSwipeTop() {
        Log.d(TAG, "onSwipeTop() called");
    }

    /**
     * Called when user swipe to left.
     */
    public void onSwipeLeft() {
        Log.d(TAG, "onSwipeLeft() called");
    }

    /**
     * Called when user swipe to right.
     */
    public void onSwipeRight() {
        Log.d(TAG, "onSwipeRight() called");
    }

    /**
     * Called when user swipe to bottom.
     */
    public void onSwipeBottom() {
        Log.d(TAG, "onSwipeBottom() called");
    }

    /**
     * Called every render frame.
     */
    public void render() {
        if (nextSceneId < 0)
            draw();
        else
            Game.instance().changeScene(nextSceneId);
    }

    /**
     * Called every render frame.  This function should be implement in children classes.
     */
    public void draw() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        computeFPS();
    }

    /**
     * Changes the new scene.
     *
     * @param sceneId A scene identifier, look at SCENE_*.
     */
    public void changeScene(int sceneId) {
        nextSceneId = sceneId;
    }

    /**
     * Gets current frames per second.
     */
    protected long getFPS() {
        return fps;
    }

    /**
     * Computes current frames per second.
     */
    protected void computeFPS() {
        frameCount++;
        long timeCurrent = System.currentTimeMillis();
        long timeUsage = timeCurrent - timeStart;
        if (timeUsage > 1000) {
            fps = (int) (frameCount * 1000 / timeUsage);
            timeStart = timeCurrent;
            frameCount = 0;
            Log.d(TAG, "FPS: " + fps);
        }

        String text = Integer.toString(fps);
        Game game = Game.instance();
        float sx = 2.0f / game.getScreenWidth();
        float sy = 2.0f / game.getScreenHeight();
        Font font = game.getSmallFont();
        PointF measure = font.measure(text, sx, sy);
        font.draw(text, 1 - measure.x, -1 + measure.y, sx, sy);
    }

}
