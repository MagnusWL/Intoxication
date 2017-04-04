/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.inventory;


import group04.common.Entity;
import group04.inventorycommon.InventoryEntity;

import group04.common.World;
import group04.playercommon.PlayerEntity;
import group04.weaponcommon.WeaponEntity;

import java.util.ArrayList;
/**
 *
 * @author burno
 */
public class InventorySystem {

    
    

    public InventorySystem(int size) {
       
    }
    public ArrayList getInventory(World world) {
        for(Entity entity : world.getEntities(InventoryEntity.class))
        {
            InventoryEntity inventoryEntity = (InventoryEntity) entity;   
        return inventoryEntity.getInventory();
        }
        return null;
    }
    
    public void addToInventory(PlayerEntity player, WeaponEntity weapon) {
        

    }
}
