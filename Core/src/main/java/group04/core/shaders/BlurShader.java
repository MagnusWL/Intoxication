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
public class BlurShader implements ShaderInterface {

    @Override
    public ShaderProgram drawShader() {
        String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n"
                + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n"
                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n"
                + "uniform mat4 u_projTrans;\n"
                + " \n"
                + "varying vec4 vColor;\n"
                + "varying vec2 vTexCoord;\n"
                + "void main() {\n"
                + "	vColor = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n"
                + "	vTexCoord = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n"
                + "	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n"
                + "}";
        String fragmentShader = "//\"in\" attributes from our vertex shader\n"
                + "varying vec4 vColor;\n"
                + "varying vec2 vTexCoord;\n"
                + "\n"
                + "//declare uniforms\n"
                + "uniform sampler2D u_texture;\n"
                + "uniform float resolution;\n"
                + "uniform float radius;\n"
                + "uniform vec2 dir;\n"
                + "\n"
                + "void main() {\n"
                + "    //this will be our RGBA sum\n"
                + "    vec4 sum = vec4(0.0);\n"
                + "\n"
                + "    //our original texcoord for this fragment\n"
                + "    vec2 tc = vTexCoord;\n"
                + "\n"
                + "    //the amount to blur, i.e. how far off center to sample from \n"
                + "    //1.0 -> blur by one pixel\n"
                + "    //2.0 -> blur by two pixels, etc.\n"
                + "    float blur = radius/resolution; \n"
                + "\n"
                + "    //the direction of our blur\n"
                + "    //(1.0, 0.0) -> x-axis blur\n"
                + "    //(0.0, 1.0) -> y-axis blur\n"
                + "    float hstep = dir.x;\n"
                + "    float vstep = dir.y;\n"
                + "\n"
                + "    //apply blurring, using a 9-tap filter with predefined gaussian weights\n"
                + "\n"
                + "    sum += texture2D(u_texture, vec2(tc.x - 4.0*blur*hstep, tc.y - 4.0*blur*vstep)) * 0.0162162162;\n"
                + "    sum += texture2D(u_texture, vec2(tc.x - 3.0*blur*hstep, tc.y - 3.0*blur*vstep)) * 0.0540540541;\n"
                + "    sum += texture2D(u_texture, vec2(tc.x - 2.0*blur*hstep, tc.y - 2.0*blur*vstep)) * 0.1216216216;\n"
                + "    sum += texture2D(u_texture, vec2(tc.x - 1.0*blur*hstep, tc.y - 1.0*blur*vstep)) * 0.1945945946;\n"
                + "\n"
                + "    sum += texture2D(u_texture, vec2(tc.x, tc.y)) * 0.2270270270;\n"
                + "\n"
                + "    sum += texture2D(u_texture, vec2(tc.x + 1.0*blur*hstep, tc.y + 1.0*blur*vstep)) * 0.1945945946;\n"
                + "    sum += texture2D(u_texture, vec2(tc.x + 2.0*blur*hstep, tc.y + 2.0*blur*vstep)) * 0.1216216216;\n"
                + "    sum += texture2D(u_texture, vec2(tc.x + 3.0*blur*hstep, tc.y + 3.0*blur*vstep)) * 0.0540540541;\n"
                + "    sum += texture2D(u_texture, vec2(tc.x + 4.0*blur*hstep, tc.y + 4.0*blur*vstep)) * 0.0162162162;\n"
                + "\n"
                + "    //discard alpha for our simple demo, multiply by vertex color and return\n"
                + "    gl_FragColor = vColor * vec4(sum.rgb, 1.0);\n"
                + "}";
        return new ShaderProgram(vertexShader, fragmentShader);
    }

}
