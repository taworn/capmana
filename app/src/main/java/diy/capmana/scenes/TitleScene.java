package diy.capmana.scenes;

import android.opengl.GLES20;
import android.util.Log;

import diy.capmana.Game;

public class TitleScene extends Scene {

    private static final String TAG = TitleScene.class.getSimpleName();

    public TitleScene() {
        Log.d(TAG, "TitleScene created");
    }

    @Override
    public void init() {
        super.init();
        Log.d(TAG, "init() called");
    }

    @Override
    public void finish() {
        Log.d(TAG, "finish() called");
        super.finish();
    }

    @Override
    public void onSwipeTop() {
        Log.d(TAG, "onSwipeTop() called");
    }

    @Override
    public void onSwipeLeft() {
        Log.d(TAG, "onSwipeLeft() called");
    }

    @Override
    public void onSwipeRight() {
        Log.d(TAG, "onSwipeRight() called");
    }

    @Override
    public void onSwipeBottom() {
        Log.d(TAG, "onSwipeBottom() called");
        Game.instance().changeScene(Game.SCENE_PLAY);
    }

    @Override
    public void render() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        computeFPS();
    }

}
