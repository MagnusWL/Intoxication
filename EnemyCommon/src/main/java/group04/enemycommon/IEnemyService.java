/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.enemycommon;

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
    
    public void controller(GameData gameData, World world, Entity player, Entity base, ArrayList<EnemyEntity> enemy);
    
    public void enemyHit(GameData gameData, World world, EnemyEntity enemyHit);
    
    public void createEnemy(GameData gameData, World world, int x, int y, EnemyType enemyType, int currentLevel);
}
