/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.playercommon;

import group04.common.Entity;
import group04.weaponcommon.WeaponEntity;

/**
 *
 * @author burno
 */
public class PlayerEntity extends Entity {

    private int money;
    private float movementSpeed;
    private float jumpSpeed;
    private int life;
    private int maxLife;    
    private Entity weaponOwned;

    public Entity getWeaponOwned() {
        return weaponOwned;
    }

    public void setWeaponOwned(WeaponEntity weaponOwned) {
        this.weaponOwned = weaponOwned;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getMaxLife() {
        return maxLife;
    }

    public void setMaxLife(int maxLife) {
        this.maxLife = maxLife;
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
