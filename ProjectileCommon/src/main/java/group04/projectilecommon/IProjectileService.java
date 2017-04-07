/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.projectilecommon;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;
import group04.common.events.Event;

/**
 *
 * @author burno
 */
public interface IProjectileService {
  //  public void process(GameData gameData, World world, Entity player, Entity base);
    public void playershootgun(GameData gameData, World world, Entity player, Entity playerWeapon);
    public void playershootrocket(GameData gameData, World world, Entity player, Entity playerWeapon);
    public void enemyshoot(GameData gameData, World world, Entity enemy, Entity base, Entity player);
    public void playermeleeattack(GameData gameData, World world, Entity player, Entity playerWeapon);    
}
