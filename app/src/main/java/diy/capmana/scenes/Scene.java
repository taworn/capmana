package diy.capmana.scenes;

import android.opengl.GLES20;
import android.util.Log;

import diy.capmana.Game;
import diy.capmana.shaders.NormalShader;

/**
 * A single game scene.
 */
public class Scene {

    private static final String TAG = Scene.class.getSimpleName();

    private NormalShader normalShader;
    private int fps;
    private int frameCount;
    private long timeStart;

    /**
     * Constructs a game scene.
     */
    public Scene() {
        init();
    }

    /**
     * Initializes a game scene.
     */
    public void init() {
        Log.d(TAG, "init() called");

        Game game = Game.instance();
        normalShader = game.getNormalShader();

        fps = 0;
        frameCount = 0;
        timeStart = System.currentTimeMillis();
    }

    /**
     * Uninitializes a game scene.
     */
    public void finish() {
        Log.d(TAG, "finish() called");
    }

    protected NormalShader getNormalShader() {
        return normalShader;
    }

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
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        computeFPS();
    }

}
