package group04.enemy;

import group04.boostcommon.IBoostService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import group04.common.Entity;
import group04.common.EntityType;
import group04.common.GameData;
import group04.common.World;
import group04.common.events.Event;
import group04.common.events.EventType;
import group04.enemycommon.IEnemyService;
import group04.common.services.IServiceInitializer;
import group04.currencycommon.ICurrencyService;
import group04.enemycommon.EnemyEntity;
import org.openide.util.Lookup;

@ServiceProviders(value = {
    @ServiceProvider(service = IEnemyService.class),
    @ServiceProvider(service = IServiceInitializer.class)})

public class EnemySystem implements IEnemyService, IServiceInitializer {

    private List<EnemyEntity> enemies = new ArrayList<>();
    private final Random rand = new Random();

    private void dropItem(Entity drop, Entity enemy, World world, GameData gameData, EventType type) {
        world.addEntity(drop);
        gameData.addEvent(new Event(type, drop.getID()));
        drop.setX(enemy.getX());
        drop.setY(enemy.getY());
    }

    @Override
    public void createEnemy(GameData gameData, World world, int x, int y) {
//        createBeerEnemy(gameData, world, x, y);
        createNeedleEnemy(gameData, world, x, y);
    }

    private void createNeedleEnemy(GameData gameData, World world, int x, int y) {
        EnemyEntity enemyCharacter = new EnemyEntity();
        enemyCharacter.setJumpSpeed(300);
        enemyCharacter.setMovementSpeed(85);
        enemyCharacter.setHasGravity(true);
        enemyCharacter.setMaxLife(5);
        enemyCharacter.setLife(enemyCharacter.getMaxLife());
        enemyCharacter.setCurrentAnimation("enemynarko_run_animation");
        enemyCharacter.setRunAnimation("enemynarko_run_animation");
        enemyCharacter.setAttackAnimation("enemynarko_attack_animation");

        enemyCharacter.setHitable(true);
        int spriteWidth = gameData.getSpriteInfo().get(enemyCharacter.getCurrentAnimation())[0];
        int spriteHeight = gameData.getSpriteInfo().get(enemyCharacter.getCurrentAnimation())[1];
        enemyCharacter.setShapeX(new float[]{-(spriteWidth / 2) * gameData.getHitBoxScale(), -(spriteWidth / 2) * gameData.getHitBoxScale(),
            spriteWidth / 2 * gameData.getHitBoxScale(), spriteWidth / 2 * gameData.getHitBoxScale()});
        enemyCharacter.setShapeY(new float[]{-(spriteHeight / 2) * gameData.getHitBoxScale(), spriteHeight / 2 * gameData.getHitBoxScale(),
            spriteHeight / 2 * gameData.getHitBoxScale(), -(spriteHeight / 2 * gameData.getHitBoxScale())});

        enemyCharacter.setAnimateable(true);
        enemyCharacter.setEntityType(EntityType.ENEMY);
        enemyCharacter.setX(x);
        enemyCharacter.setY(y);

        gameData.addEvent(new Event(EventType.PICKUP_WEAPON, enemyCharacter.getID()));

        enemies.add(enemyCharacter);
        world.addEntity(enemyCharacter);
    }

    private void createBeerEnemy(GameData gameData, World world, int x, int y) {
        EnemyEntity enemyCharacter = new EnemyEntity();
        enemyCharacter.setJumpSpeed(300);
        enemyCharacter.setMovementSpeed(85);
        enemyCharacter.setHasGravity(true);
        enemyCharacter.setMaxLife(5);
        enemyCharacter.setLife(enemyCharacter.getMaxLife());
        enemyCharacter.setCurrentAnimation("enemybeer_run_animation");
        enemyCharacter.setRunAnimation("enemybeer_run_animation");
        enemyCharacter.setAttackAnimation("enemybeer_attack_animation");
        int spriteWidth = gameData.getSpriteInfo().get(enemyCharacter.getCurrentAnimation())[0];
        int spriteHeight = gameData.getSpriteInfo().get(enemyCharacter.getCurrentAnimation())[1];
        enemyCharacter.setShapeX(new float[]{-(spriteWidth / 2) * gameData.getHitBoxScale(), -(spriteWidth / 2) * gameData.getHitBoxScale(),
            spriteWidth / 2 * gameData.getHitBoxScale(), spriteWidth / 2 * gameData.getHitBoxScale()});
        enemyCharacter.setShapeY(new float[]{-(spriteHeight / 2) * gameData.getHitBoxScale(), spriteHeight / 2 * gameData.getHitBoxScale(),
            spriteHeight / 2 * gameData.getHitBoxScale(), -(spriteHeight / 2 * gameData.getHitBoxScale())});

        enemyCharacter.setAnimateable(true);
        enemyCharacter.setEntityType(EntityType.ENEMY);
        enemyCharacter.setX(x);
        enemyCharacter.setY(y);

        gameData.addEvent(new Event(EventType.PICKUP_WEAPON, enemyCharacter.getID()));

        enemies.add(enemyCharacter);
        world.addEntity(enemyCharacter);
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity e : enemies) {
            world.removeEntity(e);
        }
    }

    private void movementDecision(EnemyEntity enemy, Entity target, World world) {
        if (target.getX() - 100 > enemy.getX()) {
            enemy.setVelocity(enemy.getMovementSpeed());
        } else if (target.getX() + 100 < enemy.getX()) {
            enemy.setVelocity(-enemy.getMovementSpeed());
        } else {
            enemy.setVelocity(0);
        }

    }

    @Override
    public void controller(GameData gameData, World world, Entity player, Entity base, ArrayList<EnemyEntity> enemyList) {
        for (EnemyEntity enemy : enemyList) {
            float movementSpeed = enemy.getMovementSpeed();
            float jumpSpeed = enemy.getJumpSpeed();

            float distancePlayer = Float.MAX_VALUE;
            float distanceBase = Float.MAX_VALUE;
            distancePlayer = Math.abs(player.getX() - enemy.getX());
            distanceBase = Math.abs(base.getX() - enemy.getX());

            if (distancePlayer > distanceBase) {
                movementDecision(enemy, base, world);
            } else {
                movementDecision(enemy, player, world);
            }

            if (rand.nextFloat() > 0.99f) {
                if (enemy.isGrounded()) {
                    enemy.setVerticalVelocity(jumpSpeed);
                }
            }
        }
    }

    @Override
    public void enemyHit(GameData gameData, World world, EnemyEntity enemyHit) {

        if(enemyHit.isHitable()){
        enemyHit.setLife(enemyHit.getLife() - 1);
        enemyHit.setHit(true);
        }

        //ENEMY DIES
        if (enemyHit.getLife() <= 0) {
            world.removeEntity(enemyHit.getWeaponOwned());
            world.removeEntity(enemyHit);

            //DROPS CURRENCY
            for (ICurrencyService i : Lookup.getDefault().lookupAll(ICurrencyService.class)) {
                i.dropCurrency(gameData, world, enemyHit.getX(), enemyHit.getY());
            }

            //DROPS BOOST
            for (IBoostService ibs : Lookup.getDefault().lookupAll(IBoostService.class)) {
                ibs.dropBoost(gameData, world, enemyHit.getX(), enemyHit.getY());
            }
        }
    }

    @Override
    public void start(GameData gameData, World world) {
    }
}
