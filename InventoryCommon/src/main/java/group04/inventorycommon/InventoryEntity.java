/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.inventorycommon;

import group04.common.Entity;

import java.util.ArrayList;

/**
 *
 * @author burno
 */
public class InventoryEntity extends Entity {

    public ArrayList<Entity> inventory;

    public ArrayList<Entity> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<Entity> inventory) {
        this.inventory = inventory;
    }

}
