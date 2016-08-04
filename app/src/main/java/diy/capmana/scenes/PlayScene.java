package diy.capmana.scenes;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import diy.capmana.shaders.NormalShader;

public class PlayScene extends Scene {

    private static final String TAG = PlayScene.class.getSimpleName();

    private float modelX = 0.0f;
    private float modelY = 0.0f;
    private float modelDx = 0.0f;
    private float modelDy = 0.0f;

    // a triangle in buffer
    private FloatBuffer verticesId;

    private static final int STRIDE_SIZE = 7 * 4;  // vertex + color, result in bytes

    public PlayScene() {
        Log.d(TAG, "PlayScene created");
    }

    @Override
    public void init() {
        super.init();
        Log.d(TAG, "init() called");

        // this is a model we want to draw, a triangle
        final float[] verticesData = {
                // X, Y, Z, R, G, B, A
                -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
        };
        verticesId = ByteBuffer.allocateDirect(verticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        verticesId.put(verticesData).position(0);
    }

    @Override
    public void finish() {
        Log.d(TAG, "finish() called");
        super.finish();
    }

    @Override
    public void onSwipeTop() {
        Log.d(TAG, "onSwipeTop() called");
        modelDy = -0.1f;
    }

    @Override
    public void onSwipeLeft() {
        Log.d(TAG, "onSwipeLeft() called");
        modelDx = -0.1f;
    }

    @Override
    public void onSwipeRight() {
        Log.d(TAG, "onSwipeRight() called");
        modelDx = 0.1f;
    }

    @Override
    public void onSwipeBottom() {
        Log.d(TAG, "onSwipeBottom() called");
        modelDy = 0.1f;
    }

    @Override
    public void render() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        NormalShader normalShader = getNormalShader();
        normalShader.useProgram();

        float[] translateMatrix = new float[16];
        float[] rotateMatrix = new float[16];
        float[] viewMatrix = new float[16];
        float[] projectionMatrix = new float[16];
        float[] mvpMatrix = new float[16];

        // translating
        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, modelX + modelDx, modelY + modelDy, 0);
        modelX += modelDx;
        modelY += modelDy;
        modelDx = 0.0f;
        modelDy = 0.0f;

        // rotating
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 1000.0f) * ((int) time);
        Matrix.setIdentityM(rotateMatrix, 0);
        Matrix.rotateM(rotateMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);

        // viewing
        float eyeX = 0.0f;
        float eyeY = 0.0f;
        float eyeZ = 1.5f;
        float lookX = 0.0f;
        float lookY = 0.0f;
        float lookZ = -5.0f;
        float upX = 0.0f;
        float upY = 1.0f;
        float upZ = 0.0f;
        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        // projecting
        float ratio = 1.0f;
        float bottom = -1.0f;
        float top = 1.0f;
        float near = 1.0f;
        float far = 25.0f;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, bottom, top, near, far);

        // passes in the position information
        verticesId.position(0);
        GLES20.glVertexAttribPointer(normalShader.getPosition(), 3, GLES20.GL_FLOAT, false, STRIDE_SIZE, verticesId);
        GLES20.glEnableVertexAttribArray(normalShader.getPosition());

        // passes in the color information
        verticesId.position(3);
        GLES20.glVertexAttribPointer(normalShader.getColor(), 4, GLES20.GL_FLOAT, false, STRIDE_SIZE, verticesId);
        GLES20.glEnableVertexAttribArray(normalShader.getColor());

        // combines model, view, projection matrices and passes it in
        Matrix.multiplyMM(mvpMatrix, 0, rotateMatrix, 0, translateMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, mvpMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(normalShader.getMVPMatrix(), 1, false, mvpMatrix, 0);

        // draws
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        computeFPS();
    }

}
