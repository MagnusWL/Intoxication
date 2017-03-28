package group04.map;

import org.openide.util.lookup.ServiceProvider;
import group04.common.Entity;
import group04.common.EntityType;
import group04.common.GameData;
import group04.common.World;
import group04.common.services.IServiceInitializer;
import group04.datacontainers.MapContainer;
import group04.mapcommon.MapEntity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServiceProvider(service = IServiceInitializer.class)

public class Initializer implements IServiceInitializer {

    

    public Initializer() {
    }

    @Override
    public void start(GameData gameData, World world) {
//        map = generateMap(gameData);
        Entity map = loadMap(gameData);
        world.addEntity(map);
    }

    @Override
    public void stop(GameData gameData, World world) {

    }

    private Entity loadMap(GameData gameData) {

        
        FileInputStream fin = null;
        ObjectInputStream ois = null;
        try {
            fin = new FileInputStream(new File("../../../Common/src/main/resources/map.object").getAbsolutePath());
            ois = new ObjectInputStream(fin);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Initializer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Initializer.class.getName()).log(Level.SEVERE, null, ex);
        }

        MapEntity newMap = null;

        if (ois != null) {
            int[][] newMapInt;

            try {
                MapContainer mapContainer = new MapContainer();
                
                newMapInt = (int[][]) ois.readObject();
                gameData.setMapHeight(newMapInt[0].length);
                gameData.setMapWidth(newMapInt.length);
                newMap = new MapEntity();
                newMap.addContainer(mapContainer);
                newMap.setEntityType(EntityType.MAP);
                mapContainer.setMap(newMapInt);
            } catch (IOException ex) {
                Logger.getLogger(Initializer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Initializer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (newMap != null) {
            return newMap;
        }

        return null;
    }
}