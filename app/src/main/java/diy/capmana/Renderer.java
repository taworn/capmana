package diy.capmana;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * A renderer class.
 */
public class Renderer implements GLSurfaceView.Renderer {

    private static final int STRIDE_SIZE = 7 * 4;  // vertex + color, result in bytes

    private int mvpMatrixHandle;
    private int positionHandle;
    private int colorHandle;

    private float[] modelMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];
    private float[] mvpMatrix = new float[16];

    // a triangle in buffer
    private final FloatBuffer verticesId;

    public Renderer() {
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

        // Positions the eye behind the origin.
        float eyeX = 0.0f;
        float eyeY = 0.0f;
        float eyeZ = 1.5f;
        // We are looking toward the distance.
        float lookX = 0.0f;
        float lookY = 0.0f;
        float lookZ = -5.0f;
        // Sets our up vector.  This is where our head would be pointing were we holding the camera.
        float upX = 0.0f;
        float upY = 1.0f;
        float upZ = 0.0f;
        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = 1.0f;
        float left = -ratio;
        float right = ratio;
        float bottom = -1.0f;
        float top = 1.0f;
        float near = 1.0f;
        float far = 25.0f;
        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
        drawTriangle(verticesId);
    }

    private void drawTriangle(FloatBuffer triangleBuffer) {
        // passes in the position information
        triangleBuffer.position(0);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, STRIDE_SIZE, triangleBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);

        // passes in the color information
        triangleBuffer.position(3);
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, STRIDE_SIZE, triangleBuffer);
        GLES20.glEnableVertexAttribArray(colorHandle);

        // combines model, view, projection matrices and passes it in
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        // draws
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
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

}
