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
import java.util.ArrayList;
import java.util.List;

import diy.capmana.shaders.TextureShader;

/**
 * A sprite class.
 */
public class Sprite {

    private int[] handle = new int[1];
    List<Float> uData = new ArrayList<Float>();
    List<Float> vData = new ArrayList<Float>();
    int sliceHorz = 0;
    int sliceVert = 0;

    /**
     * Constructs a sprite.
     */
    public Sprite(@NonNull Context context, int resourceId, int sliceHorz, int sliceVert) {
        load(context, resourceId, sliceHorz, sliceVert);
    }

    /**
     * Loads image.
     */
    public void load(@NonNull Context context, int resourceId, int sliceHorz, int sliceVert) {
        GLES20.glGenTextures(1, handle, 0);

        // loads image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        // sets texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handle[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();

        uData.clear();
        for (int i = 0; i <= sliceHorz; i++) {
            float f = (float) i / sliceHorz;
            uData.add(f);
        }
        vData.clear();
        for (int i = 0; i <= sliceVert; i++) {
            float f = (float) i / sliceVert;
            vData.add(f);
        }
        this.sliceHorz = sliceHorz;
        this.sliceVert = sliceVert;
    }

    /**
     * Draws sprite.
     */
    public void draw(@NonNull float[] mvpMatrix, int imageIndex) {
        TextureShader shader = Game.instance().getTextureShader();
        shader.useProgram();
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        // uses texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handle[0]);
        GLES20.glUniform1i(shader.getSampler(), 0);

        int uIndex = imageIndex % sliceHorz;
        int vIndex = imageIndex / sliceHorz;
        float u0 = uData.get(uIndex);
        float u1 = uData.get(uIndex + 1);
        float v0 = vData.get(vIndex);
        float v1 = vData.get(vIndex + 1);

        float verticesData[] = {
                // vertex         // coord
                1.0f, 1.0f, 0.0f, u1, v0,
                1.0f, -1.0f, 0.0f, u1, v1,
                -1.0f, 1.0f, 0.0f, u0, v0,
                1.0f, -1.0f, 0.0f, u1, v1,
                -1.0f, -1.0f, 0.0f, u0, v1,
                -1.0f, 1.0f, 0.0f, u0, v0,
        };
        FloatBuffer verticesBuffer = ByteBuffer.allocateDirect(verticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        verticesBuffer.put(verticesData).position(0);

        // passing positions
        verticesBuffer.position(0);
        GLES20.glVertexAttribPointer(shader.getPosition(), 3, GLES20.GL_FLOAT, false, 5 * 4, verticesBuffer);
        GLES20.glEnableVertexAttribArray(shader.getPosition());

        // passing sprite coordinates
        verticesBuffer.position(3);
        GLES20.glVertexAttribPointer(shader.getCoord(), 2, GLES20.GL_FLOAT, false, 5 * 4, verticesBuffer);
        GLES20.glEnableVertexAttribArray(shader.getCoord());

        // drawing
        GLES20.glUniformMatrix4fv(shader.getMVPMatrix(), 1, false, mvpMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
    }

    /**
     * Gets number of images.
     */
    public int getImageCount() {
        return sliceHorz * sliceVert;
    }

    /**
     * Releases all resources.
     */
    public void release() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glDeleteTextures(1, handle, 0);
    }

}
