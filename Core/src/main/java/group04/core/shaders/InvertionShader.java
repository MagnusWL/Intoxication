/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.core.shaders;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import group04.core.shaders.ShaderInterface;

/**
 *
 * @author Josan gamle stodder
 */
public class InvertionShader implements ShaderInterface {

    public ShaderProgram drawShader(float r, float g, float b) {
        String vertexShader = "attribute vec4 a_position;\n"
                + "attribute vec4 a_color;\n"
                + "attribute vec2 a_texCoord0;\n"
                + "\n"
                + "uniform mat4 u_projTrans;\n"
                + "\n"
                + "varying vec4 vColor;\n"
                + "varying vec2 vTexCoord;\n"
                + "\n"
                + "void main() {\n"
                + "    vColor = a_color;\n"
                + "    vTexCoord = a_texCoord0;\n"
                + "    gl_Position =  u_projTrans * a_position;\n"
                + "}";
        String fragmentShader = "//SpriteBatch will use texture unit 0\n"
                + "uniform sampler2D u_texture;\n"
                + "\n"
                + "//\"in\" varyings from our vertex shader\n"
                + "varying vec4 vColor;\n"
                + "varying vec2 vTexCoord;\n"
                + "\n"
                + "void main() {\n"
                + "   //sample the texture\n"
                + "   vec4 texColor = texture2D(u_texture, vTexCoord);\n"
                + "\n"
                + "   //invert the red, green and blue channels\n"
                + "   texColor.rgb = vec3(1.0) - texColor.rgb;\n"
                + "\n"
                + "   //final color\n"
                + "   gl_FragColor = texColor * vColor;\n"
                + "}";

        return new ShaderProgram(vertexShader, fragmentShader);
    }
}
