package group04.basecommon;

import group04.common.Entity;

public class BaseEntity extends Entity{

    private int life;
    private int maxLife;

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

}
