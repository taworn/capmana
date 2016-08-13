package diy.capmana;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import diy.capmana.shaders.TextShader;

/**
 * A font class.
 */
public class Font {

    /**
     * A width size for single characters.
     */
    private int widthForOne;

    /**
     * A heightForAll size, it equal to all characters.
     */
    private int heightForAll;

    /**
     * UV matrix, only U part.
     */
    private float[] uData = new float[97];

    /**
     * Texture to display.
     */
    private int[] textureHandle = new int[1];

    /**
     * Constructs a font.
     */
    public Font() {
        widthForOne = 0;
        heightForAll = 0;
        for (int i = 0; i < 97; i++)
            uData[i] = 0;
        textureHandle[0] = 0;
    }

    /**
     * Constructs a font.
     */
    public Font(Context context, int fontSize, int color) {
        widthForOne = 0;
        heightForAll = 0;
        for (int i = 0; i < 97; i++)
            uData[i] = 0;
        textureHandle[0] = 0;
        load(context, fontSize, color);
    }

    /**
     * Loads font into cache.
     */
    public void load(Context context, int fontSize, int color) {
        GLES20.glGenTextures(1, textureHandle, 0);

        // prepares for paint
        int textSize = (int) (fontSize * context.getResources().getDisplayMetrics().density);
        Rect rect = new Rect();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(color);
        paint.setTextSize(textSize);
        paint.getTextBounds("M", 0, 1, rect);
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();

        // loads characters from ASCII 32 to 126 into bitmap
        widthForOne = rect.width();
        heightForAll = rect.height() + fm.descent;
        Bitmap bitmap = Bitmap.createBitmap(widthForOne * 96, heightForAll, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        bitmap.eraseColor(0x00000000);
        for (char c = 32; c < 127; c++) {
            canvas.drawText(Character.toString(c), (c - 32) * widthForOne, rect.height(), paint);
        }
        for (int i = 0; i < 97; i++) {
            uData[i] = (float) (i * widthForOne) / bitmap.getWidth();
        }

        // sets texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        // release bitmap
        bitmap.recycle();
    }

    /**
     * Measures text before draw.
     */
    public PointF measure(String text, float sx, float sy) {
        float w = 0;
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if (c >= 32 && c < 127)
                w += widthForOne * sx;
        }
        return new PointF(w, heightForAll * sy);
    }

    /**
     * Draws text.
     */
    public void draw(String text, float x, float y, float sx, float sy) {
        TextShader shader = Game.instance().getTextShader();
        shader.useProgram();
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        // uses texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

        // draws text one by one character
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if (c >= 32 && c < 127) {
                int uIndex = c - 32;
                float xx = x + widthForOne * sx;
                float yy = y + heightForAll * sy;
                float[] verticesData = {
                        // x y u v
                        xx, yy, uData[uIndex + 1], 0.0f,
                        xx, y, uData[uIndex + 1], 1.0f,
                        x, yy, uData[uIndex], 0.0f,
                        xx, y, uData[uIndex + 1], 1.0f,
                        x, y, uData[uIndex], 1.0f,
                        x, yy, uData[uIndex], 0.0f,
                };
                FloatBuffer verticesBuffer = ByteBuffer.allocateDirect(verticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                verticesBuffer.put(verticesData).position(0);

                // passing positions
                verticesBuffer.position(0);
                GLES20.glVertexAttribPointer(shader.getPosition(), 2, GLES20.GL_FLOAT, false, 4 * 4, verticesBuffer);
                GLES20.glEnableVertexAttribArray(shader.getPosition());

                // passing texture coordinates
                verticesBuffer.position(2);
                GLES20.glVertexAttribPointer(shader.getCoord(), 2, GLES20.GL_FLOAT, false, 4 * 4, verticesBuffer);
                GLES20.glEnableVertexAttribArray(shader.getCoord());

                // drawing
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

                // next characters
                x = xx;
            }
        }
    }

    /**
     * Releases all resources.
     */
    public void release() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glDeleteTextures(1, textureHandle, 0);
    }

}
