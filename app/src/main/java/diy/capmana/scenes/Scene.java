package diy.capmana.scenes;

import android.opengl.GLES20;
import android.util.Log;

import diy.capmana.Game;
import diy.capmana.shaders.NormalShader;

public class Scene {

    private static final String TAG = Scene.class.getSimpleName();

    private NormalShader normalShader;
    private int fps;
    private int frameCount;
    private long timeStart;

    public Scene() {
        init();
    }

    public void init() {
        Log.d(TAG, "init() called");

        Game game = Game.instance();
        normalShader = game.getNormalShader();

        fps = 0;
        frameCount = 0;
        timeStart = System.currentTimeMillis();
    }

    public void finish() {
        Log.d(TAG, "finish() called");
    }

    protected NormalShader getNormalShader() {
        return normalShader;
    }

    protected long getFPS() {
        return fps;
    }

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

    public void onSwipeTop() {
        Log.d(TAG, "onSwipeTop() called");
    }

    public void onSwipeLeft() {
        Log.d(TAG, "onSwipeLeft() called");
    }

    public void onSwipeRight() {
        Log.d(TAG, "onSwipeRight() called");
    }

    public void onSwipeBottom() {
        Log.d(TAG, "onSwipeBottom() called");
    }

    public void render() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        computeFPS();
    }

}
