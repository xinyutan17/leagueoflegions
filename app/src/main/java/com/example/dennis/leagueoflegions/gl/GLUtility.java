package com.example.dennis.leagueoflegions.gl;

import android.opengl.GLES20;
import android.util.Log;

public class GLUtility {
    private static final String DEBUG_TAG = "GLUtility";

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(DEBUG_TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    /**
     * Log a 4x4 matrix.
     * @param name
     * @param matrix
     */
    public static void logMatrix(String name, float[] matrix) {
        if (matrix.length != 16) {
            return;
        }
        Log.d(DEBUG_TAG, name);
        String str = "";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                str += String.format("%8.2f, ", matrix[4*j + i]);
            }
            Log.d(DEBUG_TAG, str);
            str = "";
        }
        Log.d(DEBUG_TAG, "\n");
    }

    /**
     * Log a 4x1 vector.
     * @param name
     * @param vector
     */
    public static void logVector(String name, float[] vector) {
        if (vector.length != 4) {
            return;
        }
        Log.d(DEBUG_TAG, String.format("%8s: (%8.2f, %8.2f, %8.2f, %8.2f)",
                name, vector[0], vector[1], vector[2], vector[3]));
    }
}
