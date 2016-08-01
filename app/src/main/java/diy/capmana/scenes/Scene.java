package diy.capmana.scenes;

import android.opengl.GLES20;
import android.util.Log;

import diy.capmana.Game;

public class Scene {

    private static final String TAG = Scene.class.getSimpleName();

    private int positionHandle;
    private int colorHandle;
    private int mvpMatrixHandle;
    private int frameCount;
    private long fps;
    private long timeStart;

    public Scene() {
        init();
    }

    public void init() {
        Log.d(TAG, "init() called");

        Game game = Game.instance();
        positionHandle = game.getPositionHandle();
        colorHandle = game.getColorHandle();
        mvpMatrixHandle = game.getMVPMatrixHandle();
        frameCount = 0;
        fps = 0;
        timeStart = System.currentTimeMillis();
    }

    public void finish() {
        Log.d(TAG, "finish() called");
    }

    protected int getPositionHandle() {
        return positionHandle;
    }

    protected int getColorHandle() {
        return colorHandle;
    }

    protected int getMVPMatrixHandle() {
        return mvpMatrixHandle;
    }

    protected long getFPS() {
        return fps;
    }

    protected void computeFPS() {
        frameCount++;
        long timeCurrent = System.currentTimeMillis();
        long timeUsage = timeCurrent - timeStart;
        if (timeUsage > 1000) {
            fps = frameCount * 1000 / timeUsage;
            timeStart = timeCurrent;
            frameCount = 0;
            Log.d(TAG, "FPS: " + fps);
        }
    }

    protected void drawFPS() {
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
        drawFPS();
    }

}
