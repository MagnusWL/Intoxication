/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.itemdropscommon;

import group04.common.Entity;

/**
 *
 * @author Mathias
 */
public class ItemEntity extends Entity {
    
    private int goldAmount;
    private ItemType type;

    public void setType(ItemType type) {
        this.type = type;
    }

    public ItemType getType() {
        return type;
    }

    public int getGoldAmount() {
        return goldAmount;
    }

    public void setGoldAmount(int goldAmount) {
        this.goldAmount = goldAmount;
    }
    
    
    
}
