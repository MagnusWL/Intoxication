/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.spawnercommon;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;

/**
 *
 * @author Michael-PC
 */
public interface ISpawnerService {
    
        public void spawner(GameData gameData, World world, WaveSpawnerEntity spawner);

}
