/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.common.services;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;

/**
 *
 * @author Michael-PC
 */
public interface IMovementService {
    
    public void movementWhenGrounded(GameData gameData, World world, Entity entity);
    
    public void movementWhenNotGrounded(GameData gameData, World world, Entity entity);
    
    public void movementWhenColliding(GameData gameData, World world, Entity entity, Entity target);
}
