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
import group04.currencycommon.CurrencyEntity;
import group04.currencycommon.ICurrencyService;
import group04.playercommon.PlayerEntity;

@ServiceProviders(value = {
    @ServiceProvider(service = IServiceInitializer.class),
    @ServiceProvider(service = ICurrencyService.class)})

public class CurrencySystem implements IServiceInitializer, ICurrencyService {

    private List<Entity> currencies;

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
    public void pickUpCurrency(World world, Entity player, Entity currency) {
        PlayerEntity entity = (PlayerEntity) player;
        CurrencyEntity cur = (CurrencyEntity) currency;
        entity.setMoney(entity.getMoney() + 10);
        world.removeEntity(world.getEntity(currency.getID()));
    }

    @Override
    public void dropCurrency(GameData gameData, World world, float x, float y) {
        CurrencyEntity currency = new CurrencyEntity();
        currency.setX(x);
        currency.setY(y);
        currency.setEntityType(CURRENCY);
        currency.setHasGravity(true);
        currency.setAnimateable(true);
        currency.setCurrentAnimation("currency_gold_animation");

        int spriteWidth = gameData.getSpriteInfo().get(currency.getCurrentAnimation())[0];
        int spriteHeight = gameData.getSpriteInfo().get(currency.getCurrentAnimation())[1];
        currency.setShapeX(new float[]{-(spriteWidth / 2) * gameData.getHitBoxScale(), -(spriteWidth / 2) * gameData.getHitBoxScale(), 
                                                spriteWidth / 2 * gameData.getHitBoxScale(), spriteWidth / 2 * gameData.getHitBoxScale()});
        currency.setShapeY(new float[]{-(spriteHeight / 2) * gameData.getHitBoxScale(), spriteHeight / 2 * gameData.getHitBoxScale(), 
                                                spriteHeight / 2 * gameData.getHitBoxScale(), -(spriteHeight / 2 * gameData.getHitBoxScale())});

        currencies.add(currency);
        world.addEntity(currency);
    }
}
