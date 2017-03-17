package group04.common;

import java.awt.Polygon;
import java.util.UUID;

public class Entity {

    private EntityType entityType;
    private float x;
    private float y;
    private float[] shapeX;
    private float[] shapeY;
    private String sprite;
    private boolean grounded;
    private float movementSpeed;
    private float jumpSpeed;
    private float velocity;
    private float verticalVelocity;
    private int life;
    private int maxLife;
    private boolean hasGravity;
    private UUID ID = UUID.randomUUID();
    private int[][] map;
    private float angle;
    
    // Weapons
    private String weaponCarrier;
    private String weaponOwned;
    private int damage;
    private WeaponType weaponType;
    private float attackCooldown;
    private float timeSinceAttack;
    private boolean swinging;
    private EntityType shotFrom;
    private boolean explosive;
    private int explosionRadius;

    //Animation
    private String currentAnimation;
    private boolean animateable = false;
    private double currentFrame;

    private int value;
    private int currency;

    //Wave Spawner
    private int spawnTimer;
    private int spawnTimerMax;
    private int mobsSpawned;
    private int mobsSpawnedMax;
    private int spawnDuration;

    public String getWeaponOwned() {
        return weaponOwned;
    }

    public void setWeaponOwned(String weaponOwned) {
        this.weaponOwned = weaponOwned;
    }

    public float getAngle() {
        return angle;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }
    
    public void setAngle(float angle) {
        this.angle = angle;
    }

    public boolean isSwinging() {
        return swinging;
    }

    public EntityType getShotFrom() {
        return shotFrom;
    }

    public void setShotFrom(EntityType shotFrom) {
        this.shotFrom = shotFrom;
    }
    
    public void setSwinging(boolean swinging) {
        this.swinging = swinging;
    }
    
    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
    
    public String getWeaponCarrier() {
        return weaponCarrier;
    }

    public void setWeaponCarrier(String weaponCarrier) {
        this.weaponCarrier = weaponCarrier;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public int getSpawnDuration() {
        return spawnDuration;
    }

    public void setSpawnDuration(int spawnDuration) {
        this.spawnDuration = spawnDuration;
    }

    public boolean isExplosive() {
        return explosive;
    }

    public void setExplosive(boolean explosive) {
        this.explosive = explosive;
    }

    public int getExplosionRadius() {
        return explosionRadius;
    }

    public void setExplosionRadius(int explosionRadius) {
        this.explosionRadius = explosionRadius;
    }

    public int getMobsSpawned() {
        return mobsSpawned;
    }

    public void setMobsSpawned(int mobsSpawned) {
        this.mobsSpawned = mobsSpawned;
    }

    public int getMobsSpawnedMax() {
        return mobsSpawnedMax;
    }

    public void setMobsSpawnedMax(int mobsSpawnedMax) {
        this.mobsSpawnedMax = mobsSpawnedMax;
    }

    public double getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(double currentFrame) {
        this.currentFrame = currentFrame;
    }

    public boolean isAnimateable() {
        return animateable;
    }

    public void setAnimateable(boolean animateable) {
        this.animateable = animateable;
    }

    public int getSpawnTimer() {
        return spawnTimer;
    }

    public void setSpawnTimer(int spawnTimer) {
        this.spawnTimer = spawnTimer;
    }

    public int getSpawnTimerMax() {
        return spawnTimerMax;
    }

    public void setSpawnTimerMax(int spawnTimerMax) {
        this.spawnTimerMax = spawnTimerMax;
    }

    public String getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(String currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isGrounded() {
        return grounded;
    }

    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }

    public float getTimeSinceAttack() {
        return timeSinceAttack;
    }

    public void setTimeSinceAttack(float timeSinceAttack) {
        this.timeSinceAttack = timeSinceAttack;
    }

    public float getAttackCooldown() {
        return attackCooldown;
    }

    public void setAttackCooldown(float attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
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

    public float[] getShapeX() {
        return shapeX;
    }

    public void setShapeX(float[] shapeX) {
        this.shapeX = shapeX;
    }

    public float[] getShapeY() {
        return shapeY;
    }

    public void setShapeY(float[] shapeY) {
        this.shapeY = shapeY;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public float getVerticalVelocity() {
        return verticalVelocity;
    }

    public void setVerticalVelocity(float velocity) {
        this.verticalVelocity = velocity;
    }

    public boolean getHasGravity() {
        return hasGravity;
    }

    public void setHasGravity(boolean hasGravity) {
        this.hasGravity = hasGravity;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getID() {
        return ID.toString();
    }
}
