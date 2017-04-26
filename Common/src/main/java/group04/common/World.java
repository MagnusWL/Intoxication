package group04.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class World {

    private Map<String, Entity> entityMap = new ConcurrentHashMap<>();

    public List<Entity> getEntities(Class... classes) {
        List<Entity> r = new ArrayList<>();
        for (Entity e : entityMap.values()) {
            if (Arrays.asList(classes).contains(e.getClass())) {
                r.add(e);
            }
        }
        return r;
    }

    public Entity getEntity(String id) {
        return entityMap.get(id);
    }

    public Collection<Entity> getAllEntities() {
        return entityMap.values();
    }

    public void addEntity(Entity entity) {
        entityMap.put(entity.getID(), entity);
    }

    public void removeEntity(Entity entity) {
        entityMap.remove(entity.getID());
    }
    
    public Map<String, Entity> getMap()
    {
        return entityMap;
    }

    public void setMap(Map<String, Entity> newMap)
    {
        entityMap = newMap;
    }
}