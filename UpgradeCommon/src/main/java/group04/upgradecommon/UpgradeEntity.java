/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.upgradecommon;

import group04.common.Entity;

/**
 *
 * @author Michael-PC
 */
public class UpgradeEntity extends Entity {

    private boolean open = false;

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
