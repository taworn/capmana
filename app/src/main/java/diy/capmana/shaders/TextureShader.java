package diy.capmana.shaders;

import android.opengl.GLES20;

public class TextureShader extends Shader {

    private int position = -1;
    private int coord = -1;
    private int mvpMatrix = -1;
    private int sampler = -1;

    public int getPosition() {
        return position;
    }

    public int getCoord() {
        return coord;
    }

    public int getMVPMatrix() {
        return mvpMatrix;
    }

    public int getSampler() {
        return sampler;
    }

    public TextureShader() {
        final String vertexSourceCode = ""
                + "attribute vec3 aVertexPosition;\n"
                + "attribute vec2 aTextureCoord;\n"
                + "uniform mat4 uMVPMatrix;\n"
                + "varying vec2 vTextureCoord;\n"
                + "void main() {\n"
                + "  //gl_Position = vec4(aVertexPosition, 1.0);\n"
                + "  gl_Position = uMVPMatrix * vec4(aVertexPosition, 1.0);\n"
                + "  vTextureCoord = aTextureCoord;\n"
                + "}\n";
        final String fragmentSourceCode = ""
                + "precision mediump float;\n"
                + "varying vec2 vTextureCoord;\n"
                + "uniform sampler2D uSampler;\n"
                + "void main() {\n"
                + "  vec4 textureColor = texture2D(uSampler, vTextureCoord);\n"
                + "  //vec4 textureColor = vec4(vTextureCoord.r, vTextureCoord.b, 0, 1);\n"
                + "  gl_FragColor = vec4(textureColor.rgb, textureColor.a);\n"
                + "  //gl_FragColor = vec4(vTextureCoord.r, 0.5, vTextureCoord.g, 1.0);\n"
                + "}\n";
        if (init(vertexSourceCode, fragmentSourceCode)) {
            position = GLES20.glGetAttribLocation(getProgram(), "aVertexPosition");
            coord = GLES20.glGetAttribLocation(getProgram(), "aTextureCoord");
            mvpMatrix = GLES20.glGetUniformLocation(getProgram(), "uMVPMatrix");
            sampler = GLES20.glGetUniformLocation(getProgram(), "uSampler");
        }
    }

}
