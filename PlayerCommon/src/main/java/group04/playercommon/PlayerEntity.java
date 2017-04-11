package group04.playercommon;

import group04.common.Entity;
import group04.weaponcommon.WeaponEntity;

public class PlayerEntity extends Entity{

    private int lsdTimer;
    private int lsdAmount = 60 * 5;
    private int money;
    private float movementSpeed;
    private float jumpSpeed;
    private Entity weaponOwned;

    public int getLsdTimer() {
        return lsdTimer;
    }

    public void setLsdTimer(int lsdTimer) {
        this.lsdTimer = lsdTimer;
    }

    public int getLsdAmount() {
        return lsdAmount;
    }

    public void subtractLsdTimer() {
        if (lsdTimer != 0 ) {
            lsdTimer--;
        }
    }

    public Entity getWeaponOwned() {
        return weaponOwned;
    }

    public void setWeaponOwned(WeaponEntity weaponOwned) {
        this.weaponOwned = weaponOwned;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public float getJumpSpeed() {
        return jumpSpeed;
    }

    public void setJumpSpeed(float jumpSpeed) {
        this.jumpSpeed = jumpSpeed;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

}
