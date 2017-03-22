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
import group04.datacontainers.AnimationContainer;
import group04.datacontainers.CollisionContainer;
import group04.datacontainers.UnitContainer;
import group04.datacontainers.MovementContainer;
import group04.datacontainers.PlayerContainer;
import java.util.ArrayList;
import group04.common.services.ICurrencyService;

@ServiceProviders(value = {
    @ServiceProvider(service = IServiceInitializer.class),
    @ServiceProvider(service = ICurrencyService.class)})

public class CurrencySystem implements IServiceInitializer, ICurrencyService {

    private List<Entity> currencies;

    private Entity createCurrency(World world, Event e) {
        Entity currency = world.getEntity(e.getEntityID());

        currency.setEntityType(CURRENCY);

        MovementContainer movementContainer = new MovementContainer();
        movementContainer.setHasGravity(true);

        AnimationContainer animationContainer = new AnimationContainer();
        animationContainer.setAnimateable(true);
        animationContainer.setCurrentAnimation("currency_gold");

        CollisionContainer collisionContainer = new CollisionContainer();
        collisionContainer.setShapeX(new float[]{
            1,
            1,
            39,
            39});

        collisionContainer.setShapeY(new float[]{
            1,
            39,
            39,
            1});

        currency.addContainer(movementContainer);
        currency.addContainer(animationContainer);
        currency.addContainer(collisionContainer);

        currencies.add(currency);

        return currency;
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
    public void pickUpCurrency(GameData gameData, World world, Entity player, Entity currency) {
        PlayerContainer playerContainer = new PlayerContainer();
        playerContainer.setMoney(playerContainer.getMoney() + 10);
    }

    @Override
    public Entity dropCurrency(World world, Entity currency) {
        
        //Entity currency = world.getEntity(e.getEntityID());

        currency.setEntityType(CURRENCY);

        MovementContainer movementContainer = new MovementContainer();
        movementContainer.setHasGravity(true);

        AnimationContainer animationContainer = new AnimationContainer();
        animationContainer.setAnimateable(true);
        animationContainer.setCurrentAnimation("currency_gold");

        CollisionContainer collisionContainer = new CollisionContainer();
        collisionContainer.setShapeX(new float[]{
            1,
            1,
            39,
            39});

        collisionContainer.setShapeY(new float[]{
            1,
            39,
            39,
            1});

        currency.addContainer(movementContainer);
        currency.addContainer(animationContainer);
        currency.addContainer(collisionContainer);

        currencies.add(currency);

        return currency;
        
    }
}
