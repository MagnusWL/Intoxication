/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.common.services;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;
import java.util.ArrayList;
import group04.common.events.Event;


/**
 *
 * @author Michael-PC
 */
public interface IEnemyService {
    
    public void spawner(GameData gameData, World world, Entity spawner);
    
    public void controller(GameData gameData, World world, Entity player, Entity base, ArrayList<Entity> enemy);
    
    public void enemyHit(GameData gameData, World world, Entity enemyHit);
}
