package diy.capmana;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import diy.capmana.scenes.PlayScene;
import diy.capmana.scenes.Scene;
import diy.capmana.scenes.TitleScene;

/**
 * A game class.
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

    private GestureDetector detector;

    private int positionHandle;
    private int colorHandle;
    private int mvpMatrixHandle;

    private Scene scene;

    public Game(Context context) {
        assert singleton == null;
        singleton = this;
        detector = new GestureDetector(context, new GestureListener());
        scene = new TitleScene();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // vertex shader and fragment shader
        final String vertexShader = ""
                + "uniform mat4 u_MVPMatrix;  \n"  // A constant representing the combined model/view/projection matrix.
                + "attribute vec4 a_Position; \n"  // Per-vertex position information we will pass in.
                + "attribute vec4 a_Color;    \n"  // Per-vertex color information we will pass in.
                + "varying vec4 v_Color;      \n"  // This will be passed into the fragment shader.
                + "void main() {              \n"  //
                + "  v_Color = a_Color;       \n"  // Pass the color through to the fragment shader.  It will be interpolated across the triangle.
                + "  gl_Position = u_MVPMatrix\n"  // gl_Position is a special variable used to store the final position.
                + "              * a_Position;\n"  // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
                + "}                          \n";
        final String fragmentShader = ""
                + "precision mediump float; \n"  // Set the default precision to medium.  We don't need as high of a precision in the fragment shader.
                + "varying vec4 v_Color;    \n"  // This is the color from the vertex shader interpolated across the triangle per fragment.
                + "void main() {            \n"  //
                + "  gl_FragColor = v_Color;\n"  // Pass the color directly through the pipeline.
                + "}                        \n";
        int vertexShaderHandle = loadShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        int fragmentShaderHandle = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
        int programHandle = GLES20.glCreateProgram();
        if (programHandle != 0) {
            GLES20.glAttachShader(programHandle, vertexShaderHandle);
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color");
            GLES20.glLinkProgram(programHandle);
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }
        mvpMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        positionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        colorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
        GLES20.glUseProgram(programHandle);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    private int loadShader(int shaderType, String sourceCode) {
        final int[] compileStatus = new int[1];
        int handle = GLES20.glCreateShader(shaderType);
        if (handle != 0) {
            GLES20.glShaderSource(handle, sourceCode);
            GLES20.glCompileShader(handle);
            GLES20.glGetShaderiv(handle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(handle);
                handle = 0;
            }
        }
        return handle;
    }

    public int getPositionHandle() {
        return positionHandle;
    }

    public int getColorHandle() {
        return colorHandle;
    }

    public int getMVPMatrixHandle() {
        return mvpMatrixHandle;
    }

    public void changeScene(int sceneId) {
        scene.finish();
        switch (sceneId) {
            case SCENE_TITLE:
                scene = new TitleScene();
                break;
            case SCENE_PLAY:
                scene = new PlayScene();
                break;
            case SCENE_DEFAULT:
            default:
                scene = new Scene();
                break;
        }
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        scene.render();
    }

    public void onSwipeTop() {
        scene.onSwipeTop();
    }

    public void onSwipeLeft() {
        scene.onSwipeLeft();
    }

    public void onSwipeRight() {
        scene.onSwipeRight();
    }

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
