/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.currency;

import java.util.List;
import group04.common.GameData;
import group04.common.World;
import group04.common.services.IServiceInitializer;
import group04.common.services.IServiceProcessor;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import group04.common.Entity;
import group04.common.EntityType;


/**
 *
 * @author Michael-PC
 */

@ServiceProviders(value={
        @ServiceProvider(service=IServiceInitializer.class),
@ServiceProvider(service=IServiceProcessor.class)})

public class CurrencySystem implements IServiceInitializer, IServiceProcessor {
    
    private List<Entity> currency;

    @Override
    public void start(GameData gd, World world) {
    }

    @Override
    public void stop(GameData gd, World world) {
    }

    @Override
    public void process(GameData gd, World world) {
        
    }
    
    private Entity createCurrency(GameData gd, World world) {
        Entity currencyUnit = new Entity();
                
        return currencyUnit;
    }
    
}
