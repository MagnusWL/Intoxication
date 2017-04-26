package group04.inventorycommon;

import group04.common.Entity;
import java.util.ArrayList;

public class InventoryEntity extends Entity {

    public ArrayList<Entity> inventory;

    public ArrayList<Entity> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<Entity> inventory) {
        this.inventory = inventory;
    }

}
