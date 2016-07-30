package diy.capmana.scenes;

import android.util.Log;

public class Scene {

    private static final String TAG = Scene.class.getSimpleName();

    private long timeStart;
    private int frameCount;

    public Scene() {
        init();
    }

    protected void fps(long timeCurrent) {
        frameCount++;
        long timeUsage = timeCurrent - timeStart;
        if (timeUsage > 1000) {
            long fps = frameCount * 1000 / timeUsage;
            timeStart = timeCurrent;
            frameCount = 0;
            Log.d(TAG, "FPS: " + fps);
        }
    }

    public void init() {
        Log.d(TAG, "init() called");
    }

    public void finish() {
        Log.d(TAG, "finish() called");
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

    public void render(long timeCurrent) {
        Log.d(TAG, "render() called");
    }

}
