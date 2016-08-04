package diy.capmana.shaders;

public class NormalShader extends Shader {

    private int position = -1;
    private int color = -1;
    private int mvpMatrix = -1;

    public int getPosition() {
        return position;
    }

    public int getColor() {
        return color;
    }

    public int getMVPMatrix() {
        return mvpMatrix;
    }

    public NormalShader() {
        final String vertexSourceCode = ""
                + "uniform mat4 u_MVPMatrix;  \n"  // A constant representing the combined model/view/projection matrix.
                + "attribute vec4 a_Position; \n"  // Per-vertex position information we will pass in.
                + "attribute vec4 a_Color;    \n"  // Per-vertex color information we will pass in.
                + "varying vec4 v_Color;      \n"  // This will be passed into the fragment Shader.
                + "void main() {              \n"  //
                + "  v_Color = a_Color;       \n"  // Pass the color through to the fragment Shader.  It will be interpolated across the triangle.
                + "  gl_Position = u_MVPMatrix\n"  // gl_Position is a special variable used to store the final position.
                + "              * a_Position;\n"  // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
                + "}                          \n";
        final String fragmentSourceCode = ""
                + "precision mediump float; \n"  // Set the default precision to medium.  We don't need as high of a precision in the fragment Shader.
                + "varying vec4 v_Color;    \n"  // This is the color from the vertex Shader interpolated across the triangle per fragment.
                + "void main() {            \n"  //
                + "  gl_FragColor = v_Color;\n"  // Pass the color directly through the pipeline.
                + "}                        \n";
        if (init(vertexSourceCode, fragmentSourceCode)) {
            position = getAttrib("a_Position");
            color = getAttrib("a_Color");
            mvpMatrix = getUniform("u_MVPMatrix");
        }
    }

}
