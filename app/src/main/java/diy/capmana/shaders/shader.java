package diy.capmana.shaders;

import android.opengl.GLES20;

public class Shader {

    private int program = 0;

    public int getProgram() {
        return program;
    }

    public void useProgram() {
        GLES20.glUseProgram(program);
    }

    public void unuseProgram() {
        GLES20.glUseProgram(0);
    }

    protected boolean init(final String vertexSourceCode, final String fragmentSourceCode) {
        program = GLES20.glCreateProgram();
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSourceCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSourceCode);

        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
        GLES20.glDetachShader(program, fragmentShader);
        GLES20.glDetachShader(program, vertexShader);
        GLES20.glDeleteShader(fragmentShader);
        GLES20.glDeleteShader(vertexShader);
        return GLES20.glIsProgram(program);
    }

    protected int loadShader(int shaderType, final String sourceCode) {
        int result = GLES20.GL_FALSE;
        int id = GLES20.glCreateShader(shaderType);
        GLES20.glShaderSource(id, sourceCode);
        GLES20.glCompileShader(id);
        return id;
    }

    protected int getAttrib(final String name) {
        return GLES20.glGetAttribLocation(program, name);
    }

    protected int getUniform(final String name) {
        return GLES20.glGetUniformLocation(program, name);
    }

}
