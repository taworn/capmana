package diy.capmana;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.support.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import diy.capmana.shaders.TextureShader;

/**
 * A texture class.
 */
public class Texture {

    private static final float[] verticesData = {
            // positions      // texture coords
            1.0f, 1.0f, 0.0f, 1.0f, 0.0f,
            1.0f, -1.0f, 0.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 0.0f, 0.0f, 0.0f,
            1.0f, -1.0f, 0.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 0.0f, 0.0f, 1.0f,
            -1.0f, 1.0f, 0.0f, 0.0f, 0.0f,
    };

    private FloatBuffer verticesBuffer;
    private int[] textureHandle = new int[1];

    /**
     * Constructs a texture.
     */
    public Texture() {
        verticesBuffer = ByteBuffer.allocateDirect(verticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        verticesBuffer.put(verticesData).position(0);
        textureHandle[0] = 0;
    }

    /**
     * Loads image.
     *
     * @param context    A context.
     * @param resourceId A resource identifier.
     */
    public void load(@NonNull Context context, int resourceId) {
        GLES20.glGenTextures(1, textureHandle, 0);

        // loads image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        // sets texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();
    }

    /**
     * Draws texture.
     */
    public void draw(@NonNull float[] mvpMatrix) {
        TextureShader shader = Game.instance().getTextureShader();
        shader.useProgram();
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        // uses texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
        GLES20.glUniform1i(shader.getSampler(), 0);

        // passing positions
        verticesBuffer.position(0);
        GLES20.glVertexAttribPointer(shader.getPosition(), 3, GLES20.GL_FLOAT, false, 5 * 4, verticesBuffer);
        GLES20.glEnableVertexAttribArray(shader.getPosition());

        // passing texture coordinates
        verticesBuffer.position(3);
        GLES20.glVertexAttribPointer(shader.getCoord(), 2, GLES20.GL_FLOAT, false, 5 * 4, verticesBuffer);
        GLES20.glEnableVertexAttribArray(shader.getCoord());

        // drawing
        GLES20.glUniformMatrix4fv(shader.getMVPMatrix(), 1, false, mvpMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
    }

    /**
     * Releases all resources.
     */
    public void release() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glDeleteTextures(1, textureHandle, 0);
    }

}
