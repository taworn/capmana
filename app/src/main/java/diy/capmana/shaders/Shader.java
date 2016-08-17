package diy.capmana.shaders;

import android.opengl.GLES20;

/**
 * A shader program.
 */
public class Shader {

    private int program = 0;

    /**
     * Gets this program.
     */
    public int getProgram() {
        return program;
    }

    /**
     * Uses this program.
     */
    public void useProgram() {
        GLES20.glUseProgram(program);
    }

    /**
     * Unuses this program.
     */
    public void unuseProgram() {
        GLES20.glUseProgram(0);
    }

    /**
     * Initializes shader programs.
     *
     * @param vertexSourceCode   A vertex shader source code.
     * @param fragmentSourceCode A fragment shader source code.
     * @return Returns true if successful, otherwise, it is false.
     */
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

    /**
     * Loads shader program.
     *
     * @param shaderType A shader program type.
     * @param sourceCode A shader source code.
     */
    protected int loadShader(int shaderType, final String sourceCode) {
        int id = GLES20.glCreateShader(shaderType);
        GLES20.glShaderSource(id, sourceCode);
        GLES20.glCompileShader(id);
        return id;
    }

}
