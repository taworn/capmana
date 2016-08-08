package diy.capmana.scenes;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import diy.capmana.Game;
import diy.capmana.R;
import diy.capmana.Texture;
import diy.capmana.shaders.NormalShader;

/**
 * Playing game scene.
 */
public class PlayScene extends Scene {

    private static final String TAG = PlayScene.class.getSimpleName();

    private Texture texture;

    private float modelX = 0.0f;
    private float modelY = 0.0f;
    private float modelDx = 0.0f;
    private float modelDy = 0.0f;

    // a triangle in buffer
    private FloatBuffer verticesId;

    private static final int STRIDE_SIZE = 7 * 4;  // vertex + color, result in bytes

    public PlayScene() {
        super();
        Log.d(TAG, "PlayScene created");
        texture = new Texture();
        texture.load(Game.instance().getContext(), R.drawable.b);
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
    public void release() {
        Log.d(TAG, "release() called");
        texture.release();
        super.release();
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
        changeScene(Game.SCENE_TITLE);
    }

    @Override
    public void draw() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST | GLES20.GL_TEXTURE_2D);

        float[] translateMatrix = new float[16];
        float[] scaleMatrix = new float[16];
        float[] viewMatrix = new float[16];
        float[] projectionMatrix = new float[16];
        float[] mvpMatrix = new float[16];

        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, -0.5f, 0, 0);
        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.scaleM(scaleMatrix, 0, 0.5f, 1.0f, 1.0f);
        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.setLookAtM(viewMatrix, 0,
                0.0f, 0.0f, 1.5f,    // eye
                0.0f, 0.0f, -15.0f,  // at
                0.0f, 1.0f, 0.0f);   // up
        Matrix.setIdentityM(projectionMatrix, 0);
        Matrix.orthoM(projectionMatrix, 0, -1.5f, 1.5f, -2.0f, 2.0f, 1.0f, 25.0f);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, translateMatrix, 0);
        texture.draw(mvpMatrix);

        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, 0.5f, 0, 0);
        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.scaleM(scaleMatrix, 0, 0.5f, 1.0f, 1.0f);
        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.setLookAtM(viewMatrix, 0,
                0.0f, 0.0f, 1.5f,    // eye
                0.0f, 0.0f, -15.0f,  // at
                0.0f, 1.0f, 0.0f);   // up
        Matrix.setIdentityM(projectionMatrix, 0);
        Matrix.orthoM(projectionMatrix, 0, -1.5f, 1.5f, -2.0f, 2.0f, 1.0f, 25.0f);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, translateMatrix, 0);
        texture.draw(mvpMatrix);


        NormalShader normalShader = Game.instance().getNormalShader();
        normalShader.useProgram();
        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, 0.5f, 0, 0);
        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.scaleM(scaleMatrix, 0, 0.5f, 1.0f, 1.0f);
        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.setLookAtM(viewMatrix, 0,
                0.0f, 0.0f, 1.5f,    // eye
                0.0f, 0.0f, -15.0f,  // at
                0.0f, 1.0f, 0.0f);   // up
        Matrix.setIdentityM(projectionMatrix, 0);
        Matrix.orthoM(projectionMatrix, 0, -1.5f, 1.5f, -2.0f, 2.0f, 1.0f, 25.0f);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, translateMatrix, 0);
        verticesId.position(0);
        GLES20.glVertexAttribPointer(normalShader.getPosition(), 3, GLES20.GL_FLOAT, false, STRIDE_SIZE, verticesId);
        GLES20.glEnableVertexAttribArray(normalShader.getPosition());
        verticesId.position(3);
        GLES20.glVertexAttribPointer(normalShader.getColor(), 4, GLES20.GL_FLOAT, false, STRIDE_SIZE, verticesId);
        GLES20.glEnableVertexAttribArray(normalShader.getColor());
        GLES20.glUniformMatrix4fv(normalShader.getMVPMatrix(), 1, false, mvpMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        computeFPS();
    }

}
