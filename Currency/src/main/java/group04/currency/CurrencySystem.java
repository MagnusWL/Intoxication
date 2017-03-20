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
import static group04.common.EntityType.CURRENCY;
import group04.common.events.Event;
import group04.common.events.EventType;
import java.util.ArrayList;

@ServiceProviders(value = {
    @ServiceProvider(service = IServiceInitializer.class),
    @ServiceProvider(service = IServiceProcessor.class)})

public class CurrencySystem implements IServiceInitializer, IServiceProcessor {

    private List<Entity> currencies;

    private Entity createCurrency(World world, Event e) {
        Entity currency = world.getEntity(e.getEntityID());
        currency.setEntityType(CURRENCY);
        
        currency.setHasGravity(true);
        currency.setAnimateable(true);
        currency.setCurrentAnimation("currency_gold");
        currencies.add(currency);
        
        currency.setShapeX(new float[]{
            1,
            1,
            39,
            39});
        currency.setShapeY(new float[]{
            1,
            39,
            39,
            1});
        
        return currency;
    }
    
    private void pickupCurrency(GameData gameData, Event e, World world) {
        world.removeEntity(world.getEntity(e.getEntityID()));
        
        for (Entity player : world.getEntities(EntityType.PLAYER)) {
            player.setMoney(player.getMoney() + 10);
        }
    }

    @Override
    public void start(GameData gameData, World world) {
        currencies = new ArrayList<>();
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity c : currencies) {
            world.removeEntity(c);
        }
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Event e : gameData.getEvents()) {
            if (e.getType() == EventType.DROP_CURRENCY) {
                createCurrency(world, e);
                gameData.removeEvent(e);
            }
            
            if (e.getType() == EventType.PICKUP_CURRENCY) {
                pickupCurrency(gameData, e, world);
                gameData.removeEvent(e);
            }

        }
    }

}
