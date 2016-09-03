package diy.capmana;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import diy.capmana.game.GameData;
import diy.capmana.scenes.GameOverScene;
import diy.capmana.scenes.NextStageScene;
import diy.capmana.scenes.PlayScene;
import diy.capmana.scenes.Scene;
import diy.capmana.scenes.StageScene;
import diy.capmana.scenes.TitleScene;
import diy.capmana.scenes.WinScene;
import diy.capmana.shaders.NormalShader;
import diy.capmana.shaders.TextShader;
import diy.capmana.shaders.TextureShader;

/**
 * A simple game engine class.
 */
public class Game implements View.OnTouchListener, GLSurfaceView.Renderer {

    public static final int SCENE_DEFAULT = 0;
    public static final int SCENE_TITLE = 1;
    public static final int SCENE_STAGE = 2;
    public static final int SCENE_NEXT_STAGE = 3;
    public static final int SCENE_PLAY = 4;
    public static final int SCENE_GAMEOVER = 5;
    public static final int SCENE_WIN = 6;

    public static Game instance() {
        return singleton;
    }

    private static Game singleton = null;

    private static final String TAG = Game.class.getSimpleName();

    private Context context;
    private GestureDetector detector;
    private Bundle bundle;
    private int currentSceneId = SCENE_TITLE;

    private NormalShader normalShader;
    private TextShader textShader;
    private TextureShader textureShader;
    private Font smallFont;
    private Font mediumFont;
    private Font largeFont;
    private int screenWidth;
    private int screenHeight;

    private SoundPool soundPool;
    private int soundIdPacmanDead;
    private int soundIdPacmanEat;

    private Scene scene;
    private GameData gameData;

    /**
     * Initializes the game engine.
     */
    public Game(@NonNull Context context) {
        assert singleton == null;
        singleton = this;

        this.context = context;
        detector = new GestureDetector(context, new GestureListener());
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundIdPacmanDead = soundPool.load(context, R.raw.pacman_dead, 1);
        soundIdPacmanEat = soundPool.load(context, R.raw.pacman_eat, 1);

        bundle = null;
        scene = null;
        gameData = new GameData();
    }

    public void onPause() {
        Log.d(TAG, "onPause()");
        if (scene != null)
            scene.onPause();
    }

    public void onResume() {
        Log.d(TAG, "onResume()");
        if (scene != null)
            scene.onResume();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onSaveInstanceState()");
        bundle = new Bundle();
        savedInstanceState.putBundle("bundle", bundle);
        savedInstanceState.putInt("currentSceneId", currentSceneId);
        savedInstanceState.putParcelable("gameData", gameData);
        if (scene != null)
            scene.onSaveInstanceState(bundle);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState()");
        bundle = savedInstanceState.getBundle("bundle");
        currentSceneId = savedInstanceState.getInt("currentSceneId");
        gameData = savedInstanceState.getParcelable("gameData");
        if (scene != null)
            scene.onRestoreInstanceState(bundle);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        Log.d(TAG, "onSurfaceCreated(), scene = " + scene);
        normalShader = new NormalShader();
        textShader = new TextShader();
        textureShader = new TextureShader();
        smallFont = new Font(context, 16, 0xFFFFFFFF, null);
        mediumFont = new Font(context, 24, 0xFFFFFFFF, null);
        largeFont = new Font(context, 32, 0xFFFFFFFF, null);
        if (scene != null) {
            scene.release();
            scene.acquire(bundle);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        Log.d(TAG, "onSurfaceChanged()");
        GLES20.glViewport(0, 0, width, height);
        screenWidth = width;
        screenHeight = height;
    }

    /**
     * Called every render frame.
     */
    @Override
    public void onDrawFrame(GL10 unused) {
        if (scene != null)
            scene.render();
        else
            changeScene(currentSceneId);
    }

    /**
     * Changes the new scene.  This function must be called in render() only due to OpenGL ES thread.
     *
     * @param sceneId A scene identifier, look at SCENE_*.
     */
    public void changeScene(int sceneId) {
        Log.d(TAG, "changing to " + sceneId + ", bundle = " + bundle);
        if (scene != null)
            scene.release();
        switch (sceneId) {
            default:
            case SCENE_DEFAULT:
                scene = new Scene(bundle);
                break;
            case SCENE_TITLE:
                scene = new TitleScene(bundle);
                break;
            case SCENE_STAGE:
                scene = new StageScene(bundle);
                break;
            case SCENE_NEXT_STAGE:
                scene = new NextStageScene(bundle);
                break;
            case SCENE_PLAY:
                scene = new PlayScene(bundle);
                break;
            case SCENE_GAMEOVER:
                scene = new GameOverScene(bundle);
                break;
            case SCENE_WIN:
                scene = new WinScene(bundle);
                break;
        }
        bundle = null;
        currentSceneId = sceneId;
    }

    public Context getContext() {
        return context;
    }

    public NormalShader getNormalShader() {
        return normalShader;
    }

    public TextShader getTextShader() {
        return textShader;
    }

    public TextureShader getTextureShader() {
        return textureShader;
    }

    public Font getSmallFont() {
        return smallFont;
    }

    public Font getMediumFont() {
        return mediumFont;
    }

    public Font getLargeFont() {
        return largeFont;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void playPacmanDead() {
        soundPool.play(soundIdPacmanDead, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playPacmanEat() {
        soundPool.play(soundIdPacmanEat, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    /**
     * Called when user swipe to top.
     */
    public void onSwipeTop() {
        if (scene != null)
            scene.onSwipeTop();
    }

    /**
     * Called when user swipe to left.
     */
    public void onSwipeLeft() {
        if (scene != null)
            scene.onSwipeLeft();
    }

    /**
     * Called when user swipe to right.
     */
    public void onSwipeRight() {
        if (scene != null)
            scene.onSwipeRight();
    }

    /**
     * Called when user swipe to bottom.
     */
    public void onSwipeBottom() {
        if (scene != null)
            scene.onSwipeBottom();
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        }
                        else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    }
                    else {
                        onSwipeTop();
                    }
                }
                result = true;
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            return result;
        }

    }

}
