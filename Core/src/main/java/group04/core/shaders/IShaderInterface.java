/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.core.shaders;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 *
 * @author Mathias
 */
public interface IShaderInterface {
    public ShaderProgram drawShader(float r, float g, float b);
}
