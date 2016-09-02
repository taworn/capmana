package diy.capmana.scenes;

import android.opengl.GLES20;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import diy.capmana.Game;
import diy.capmana.game.GameData;

/**
 * Next stage scene.
 */
public class NextStageScene extends Scene {

    private static final String TAG = NextStageScene.class.getSimpleName();

    private long timeStart;
    private long timeUsed;

    public NextStageScene(@Nullable Bundle bundle) {
        super(bundle);
        Log.d(TAG, "NextStageScene created");
        acquire(bundle);
        if (bundle != null) {
            onRestoreInstanceState(bundle);
        }
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
    public void draw() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        if (GameData.instance().nextStage())
            Game.instance().changeScene(Game.SCENE_STAGE);
        else
            Game.instance().changeScene(Game.SCENE_WIN);

        computeFPS();
    }

}
