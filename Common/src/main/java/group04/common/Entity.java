package group04.common;

import group04.datacontainers.DataContainer;
import java.util.HashMap;
import java.util.UUID;

public class Entity {

    private EntityType entityType;
    private float x;
    private float y;
    private UUID ID = UUID.randomUUID();
    private HashMap<Class, DataContainer> dataContainer = new HashMap<>();
    private float[] shapeX;
    private float[] shapeY;
    private float velocity;
    private float verticalVelocity;
    private boolean hasGravity;
    private String drawable;
     private String currentAnimation;
    private boolean animateable = false;
    private double currentFrame;    

    public String getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(String currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    public boolean isAnimateable() {
        return animateable;
    }

    public void setAnimateable(boolean animateable) {
        this.animateable = animateable;
    }

    public double getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(double currentFrame) {
        this.currentFrame = currentFrame;
    }

    public String getDrawable() {
        return drawable;
    }

    public void setDrawable(String drawable) {
        this.drawable = drawable;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public float getVerticalVelocity() {
        return verticalVelocity;
    }

    public void setVerticalVelocity(float verticalVelocity) {
        this.verticalVelocity = verticalVelocity;
    }

    public boolean isHasGravity() {
        return hasGravity;
    }

    public void setHasGravity(boolean hasGravity) {
        this.hasGravity = hasGravity;
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

    public void addContainer(DataContainer container) {
        dataContainer.put(container.getClass(), container);
    }

    public void removeContainer(Class c) {
        dataContainer.remove(c);
    }

    public DataContainer getContainer(Class c) {
        return dataContainer.get(c);
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
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
