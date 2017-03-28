/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.weaponcommon;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;

/**
 *
 * @author Michael-PC
 */
public interface IWeaponService {
    
    public void playerAttack(GameData gameData, World world, Entity player);
    
    public void enemyAttack(GameData gameData, World world, Entity enemy, Entity player, Entity base);
    
    public void pickUpWeapon(GameData gameData, World world);
}
