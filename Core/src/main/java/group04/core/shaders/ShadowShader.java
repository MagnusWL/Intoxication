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
public class ShadowShader implements ShaderInterface {

    @Override
    public ShaderProgram drawShader(float r, float g, float b) {
        String vertexShader
                = "attribute vec4 a_position;\n"
                + "attribute vec4 a_color;\n"
                + "attribute vec2 a_texCoord0;\n"
                + "varying vec2 v_texCoords;\n"
                + "varying vec2 v_texCoordsShadowmap;\n"
                + "varying vec4 v_color;\n"
                + "uniform mat4 u_projTrans;\n"
                + "void main()\n"
                + "{\n"
                + "v_texCoords = a_texCoord0;\n"
                + "v_color = a_color;\n"
                + "v_color.a = v_color.a * (255.0/254.0);\n"
                + //this is a correction due to color float precision (see SpriteBatch's default shader)
                "vec3 screenPosition = u_projTrans * a_position;\n"
                + "v_texCoordsShadowmap = (screenPosition.xy * 0.5) + 0.5;\n"
                + "gl_Position = screenPosition;\n"
                + "}\n";
        String fragmentShader
                = "#ifdef GL_ES\n"
                + "precision mediump float;\n"
                + "#endif\n"
                + "varying vec2 v_texCoords;\n"
                + "varying vec2 v_texCoordsShadowmap;\n"
                + "varying vec4 v_color;\n"
                + "uniform sampler2D u_texture;\n"
                + "uniform sampler2D u_textureShadowmap;\n"
                + "void main()\n"
                + "{\n"
                + "vec4 textureColor = texture2D(u_texture, v_texCoords);\n"
                + "float shadowColor = texture2D(u_textureShadowmap, v_texCoordsShadowmap).r;\n"
                + "shadowColor = mix(shadowColor, 1.0, v_color.a);\n"
                + "textureColor.rgb *= shadowColor * v_color.rgb;\n"
                + "gl_FragColor = textureColor;\n"
                + "}\n";

        return new ShaderProgram(vertexShader, fragmentShader);
}

}
