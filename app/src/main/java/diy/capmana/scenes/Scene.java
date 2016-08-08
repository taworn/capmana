package diy.capmana.scenes;

import android.opengl.GLES20;
import android.util.Log;

import diy.capmana.Game;

/**
 * A single game scene.
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
    public Scene() {
        Log.d(TAG, "Scene created");
        nextSceneId = -1;

        fps = 0;
        frameCount = 0;
        timeStart = System.currentTimeMillis();
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
     * Releases game resources in current scene.
     */
    public void release() {
        Log.d(TAG, "release() called");
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

}
