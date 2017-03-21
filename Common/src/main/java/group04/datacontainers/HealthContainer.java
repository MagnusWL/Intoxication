package group04.datacontainers;

public class HealthContainer implements DataContainer{
    private int life;
    private int maxLife;    
    private String weaponOwned;

    public String getWeaponOwned() {
        return weaponOwned;
    }

    public void setWeaponOwned(String weaponOwned) {
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
}
