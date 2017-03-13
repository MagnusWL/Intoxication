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
 * @author Magnus
 */
public interface ICollisionService {
    public boolean isColliding(World world, GameData gameData, Entity entity, float moveX, float moveY);
    public boolean isEntitiesColliding(World world, GameData gameData, Entity entity1, Entity entity2);
}
