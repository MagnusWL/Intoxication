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

    public void addContainer(DataContainer container)
    {
        dataContainer.put(container.getClass(), container);
    }
  
    public void removeContainer(Class c)
    {
        dataContainer.remove(c);
    }
  
    public DataContainer getContainer(Class c)
    {
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
