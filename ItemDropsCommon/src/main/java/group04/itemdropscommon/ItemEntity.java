package group04.itemdropscommon;

import group04.common.Entity;

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
