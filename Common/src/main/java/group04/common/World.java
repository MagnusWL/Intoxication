package group04.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class World {

    private final Map<String, Entity> entityMap = new ConcurrentHashMap<>();

    public List<Entity> getEntities(EntityType... entityTypes) {
        List<Entity> r = new ArrayList<>();
        for(Entity e: entityMap.values())
            if(Arrays.asList(entityTypes).contains(e.getEntityType()))
                r.add(e);
        return r;
    }
    
    public Entity getEntity(String id)
    {
        return entityMap.get(id);
    }
    
    public Collection<Entity> getAllEntities() {
        return entityMap.values();
    }

    public void addEntity(Entity entity) {
        entityMap.put(entity.getID(),entity);
    }
    
    public void removeEntity (Entity entity) {
        entityMap.remove(entity.getID());
    }
}
