package diy.capmana.shaders;

import android.opengl.GLES20;

public class TextShader extends Shader {

    private int position = -1;
    private int coord = -1;
    private int sampler = -1;

    public int getPosition() {
        return position;
    }

    public int getCoord() {
        return coord;
    }

    public int getSampler() {
        return sampler;
    }

    public TextShader() {
        final String vertexSourceCode = ""
                + "attribute vec2 position;\n"
                + "attribute vec2 coord;\n"
                + "varying vec2 texCoord;\n"
                + "void main() {\n"
                + "  gl_Position = vec4(position, 0, 1);\n"
                + "  texCoord = coord;\n"
                + "}\n";
        final String fragmentSourceCode = ""
                + "precision mediump float;\n"
                + "varying vec2 texCoord;\n"
                + "uniform sampler2D sampler;\n"
                + "void main() {\n"
                + "  vec4 textureColor = texture2D(sampler, texCoord);\n"
                + "  gl_FragColor = vec4(textureColor.rgb, textureColor.a);\n"
                + "}\n";
        if (init(vertexSourceCode, fragmentSourceCode)) {
            position = GLES20.glGetAttribLocation(getProgram(), "position");
            coord = GLES20.glGetAttribLocation(getProgram(), "coord");
            sampler = GLES20.glGetUniformLocation(getProgram(), "sampler");
        }
    }

}
