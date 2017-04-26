package group04.upgradecommon;

import group04.common.Entity;

public class UpgradeEntity extends Entity {

    private boolean open = false;

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
