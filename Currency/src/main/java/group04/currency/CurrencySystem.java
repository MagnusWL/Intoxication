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

/**
 *
 * @author Michael-PC
 */
@ServiceProviders(value = {
    @ServiceProvider(service = IServiceInitializer.class),
    @ServiceProvider(service = IServiceProcessor.class)})

public class CurrencySystem implements IServiceInitializer, IServiceProcessor {

    private List<Entity> currencies;

    private Entity createCurrency(World world, Event e) {
        Entity currency = new Entity();
        Entity enemy = world.getEntity(e.getEntityID());
        currency.setEntityType(CURRENCY);
        currency.setX(enemy.getX());
        currency.setY(enemy.getY());
        currency.setSprite("currency");
        currencies.add(currency);
        
        currency.setShapeX(new float[]{
            5,
            5,
            20,
            20});
        currency.setShapeY(new float[]{
            0,
            100,
            100,
            0});
        
        return currency;
    }

    private void addCurrencyEvent(GameData gd, World world, Event e) {
        world.addEntity(createCurrency(world, e));
    }

    @Override
    public void start(GameData gd, World world) {
        currencies = new ArrayList<>();
    }

    @Override
    public void stop(GameData gd, World world) {
        for (Entity c : currencies) {
            world.removeEntity(c);
        }
    }

    @Override
    public void process(GameData gd, World world) {
        for (Event e : gd.getEvents()) {
            if (e.getType() == EventType.DROP_CURRENCY) {
                addCurrencyEvent(gd, world, e);
            }

        }
    }

}
