/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.core.shaders;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 *
 * @author Josan gamle stodder
 */
public class VignetteShader implements ShaderInterface {

    @Override
    public ShaderProgram drawShader() {
        String vertexShader
                = "attribute vec3 a_position;\n"
                + "attribute vec4 a_color;\n"
                + "attribute vec2 a_texCoord0;\n"
                + "uniform mat4 u_projTrans;\n"
                + "\n"
                + "varying vec4 v_color;\n"
                + "varying vec2 v_texCoord0;\n"
                + "\n"
                + "void main() {\n"
                + "    v_color = a_color;\n"
                + "    v_texCoord0 = a_texCoord0;\n"
                + "    gl_Position =  u_projTrans * (a_position,1);\n"
                + "}";
        String fragmentShader
                = "in vec3 Color;\n"
                + "in vec2 Texcoord;\n"
                + "uniform sampler2D tex;\n"
                + "uniform vec2 resolution;\n"
                + "const float outerRadius = .95, innerRadius = .4, intensity = .7;\n"
                + "void main() {\n"
                + "vec4 col = texture(tex, Texcoord) * vec4(Color, 1.0);\n"
                + "vec2 relativePosition = (gl_FragCoord.xy / resolution) - 1;\n"
                + "float len = length(relativePosition);\n"
                + "float vignette = smoothstep(outerRadius, innerRadius, len);\n"
                + "col.rgb = mix(col.rgb, col.rgb * vignette, intensity);\n"
                + "gl_FragColor = col; //texture(tex, Texcoord) * vec4(Color, 1.0);\n"
                + "}\n";

        return new ShaderProgram(vertexShader, fragmentShader);

    }

}
