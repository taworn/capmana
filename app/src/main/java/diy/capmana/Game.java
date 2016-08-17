package diy.capmana;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import diy.capmana.scenes.PlayScene;
import diy.capmana.scenes.Scene;
import diy.capmana.scenes.TitleScene;
import diy.capmana.shaders.NormalShader;
import diy.capmana.shaders.TextShader;
import diy.capmana.shaders.TextureShader;

/**
 * A simple game engine class.
 */
public class Game implements View.OnTouchListener, GLSurfaceView.Renderer {

    public static final int SCENE_DEFAULT = 0;
    public static final int SCENE_TITLE = 1;
    public static final int SCENE_PLAY = 2;

    public static Game instance() {
        return singleton;
    }

    private static Game singleton = null;

    private static final String TAG = Game.class.getSimpleName();

    private Context context;
    private GestureDetector detector;

    private NormalShader normalShader;
    private TextShader textShader;
    private TextureShader textureShader;
    private Font smallFont;
    private Font mediumFont;
    private Font largeFont;
    private int screenWidth;
    private int screenHeight;

    private Scene scene;

    /**
     * Initializes the game engine.
     */
    public Game(Context context) {
        assert singleton == null;
        singleton = this;

        this.context = context;
        detector = new GestureDetector(context, new GestureListener());
        scene = null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        Log.d(TAG, "onSurfaceCreated()");
        normalShader = new NormalShader();
        textShader = new TextShader();
        textureShader = new TextureShader();
        smallFont = new Font(context, 16, 0xFFFFFFFF, null);
        mediumFont = new Font(context, 24, 0xFFFFFFFF, null);
        largeFont = new Font(context, 32, 0xFFFFFFFF, null);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        Log.d(TAG, "onSurfaceChanged()");
        GLES20.glViewport(0, 0, width, height);
        screenWidth = width;
        screenHeight = height;
        changeScene(SCENE_TITLE);
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

    /**
     * Changes the new scene.  This function must be called in render() only due to OpenGL ES thread.
     *
     * @param sceneId A scene identifier, look at SCENE_*.
     */
    public void changeScene(int sceneId) {
        if (scene != null)
            scene.release();
        switch (sceneId) {
            default:
            case SCENE_DEFAULT:
                scene = new Scene();
                break;
            case SCENE_TITLE:
                scene = new TitleScene();
                break;
            case SCENE_PLAY:
                scene = new PlayScene();
                break;
        }
    }

    /**
     * Called every render frame.
     */
    @Override
    public void onDrawFrame(GL10 unused) {
        scene.render();
    }

    /**
     * Called when user swipe to top.
     */
    public void onSwipeTop() {
        scene.onSwipeTop();
    }

    /**
     * Called when user swipe to left.
     */
    public void onSwipeLeft() {
        scene.onSwipeLeft();
    }

    /**
     * Called when user swipe to right.
     */
    public void onSwipeRight() {
        scene.onSwipeRight();
    }

    /**
     * Called when user swipe to bottom.
     */
    public void onSwipeBottom() {
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
