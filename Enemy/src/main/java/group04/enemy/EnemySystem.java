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
import group04.enemycommon.EnemyType;
import group04.itemdropscommon.IDropService;
import group04.playercommon.PlayerEntity;
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
    public void createEnemy(GameData gameData, World world, int x, int y, EnemyType enemyType, int currentLevel) {
        switch (enemyType) {
            case NARKO:
                createNeedleEnemy(gameData, world, x, y, currentLevel);
                break;
            case BEER:
                createBeerEnemy(gameData, world, x, y, currentLevel);
                break;
            case LSD:
                createLSDEnemy(gameData, world, x, y, currentLevel);
                break;
            case BOSS:
                createBossEnemy(gameData, world, x, y, currentLevel);
                break;
            case RAVE:
                createRaveEnemy(gameData, world, x, y, currentLevel);
                break;
            case JOINT:
                createJointEnemy(gameData, world, x, y, currentLevel);
                break;
        }
    }

    private void createNeedleEnemy(GameData gameData, World world, int x, int y, int currentLevel) {
         EnemyEntity enemyCharacter = new EnemyEntity();
        enemyCharacter.setEnemyType(EnemyType.NARKO);
        enemyCharacter.setJumpSpeed(0);
        enemyCharacter.setMovementSpeed((float) currentLevel / 10 * 1000);
        enemyCharacter.setHasGravity(true);
        enemyCharacter.setMaxLife(currentLevel * 2);
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
        // Hvis vi skal kunne skifte fokus-target efter hvem enemy går efter.
//        for(Entity player : world.getEntities(PlayerEntity.class))
//        {
//            enemyCharacter.setFocusTarget(player);
//        }
        enemies.add(enemyCharacter);
        world.addEntity(enemyCharacter);
    }
// Animations not yet developed

    private void createJointEnemy(GameData gameData, World world, int x, int y, int currentLevel) {
        EnemyEntity enemyCharacter = new EnemyEntity();
        enemyCharacter.setEnemyType(EnemyType.JOINT);
        enemyCharacter.setJumpSpeed(0);
        enemyCharacter.setMovementSpeed((float) currentLevel / 10 * 1000);
        enemyCharacter.setHasGravity(true);
        enemyCharacter.setMaxLife(currentLevel * 2);
        enemyCharacter.setLife(enemyCharacter.getMaxLife());
        enemyCharacter.setCurrentAnimation("enemyjoint_run_animation");
        enemyCharacter.setRunAnimation("enemyjoint_run_animation");
        enemyCharacter.setAttackAnimation("enemyjoint_attack_animation");

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

// Animations not yet developed
    private void createBossEnemy(GameData gameData, World world, int x, int y, int currentLevel) {
        EnemyEntity enemyCharacter = new EnemyEntity();
        enemyCharacter.setEnemyType(EnemyType.BOSS);
        enemyCharacter.setJumpSpeed(10);
        enemyCharacter.setMovementSpeed(((float)currentLevel / 10) * 1000);
        enemyCharacter.setHasGravity(true);
        enemyCharacter.setMaxLife(currentLevel * 20);
        enemyCharacter.setLife(enemyCharacter.getMaxLife());
        enemyCharacter.setCurrentAnimation("enemyboss_run_animation");
        enemyCharacter.setRunAnimation("enemyboss_run_animation");
        enemyCharacter.setAttackAnimation("enemyboss_attack_animation");
        enemyCharacter.setTag("boss");

        enemyCharacter.setK1(-4.676392131631996f);
        enemyCharacter.setK2(0.44302883625206846f);

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
        enemyCharacter.setBoss(true);

        gameData.addEvent(new Event(EventType.PICKUP_GUN, enemyCharacter.getID()));

        enemies.add(enemyCharacter);
        world.addEntity(enemyCharacter);
    }
    
   
    private float getBossPosX(GameData gameData, World world)
    {
        for(Entity e : world.getEntities(EnemyEntity.class))
        {
            if(e.getTag() == "boss")
            {
                return e.getX();
            }
        }
        return 0;
    }
    private float getBossPosY(GameData gameData, World world)
    {
        for(Entity e : world.getEntities(EnemyEntity.class))
        {
            if(e.getTag() == "boss")
            {
                return e.getY();
            }
        }
        return 0;
    }

// Animations not yet developed
    private void createRaveEnemy(GameData gameData, World world, int x, int y, int currentLevel) {
        EnemyEntity enemyCharacter = new EnemyEntity();
        enemyCharacter.setEnemyType(EnemyType.RAVE);
        enemyCharacter.setJumpSpeed(0);
        enemyCharacter.setMovementSpeed((float) currentLevel / 10 * 1000);
        enemyCharacter.setHasGravity(true);
        enemyCharacter.setMaxLife(currentLevel * 2);
        enemyCharacter.setLife(enemyCharacter.getMaxLife());
        enemyCharacter.setCurrentAnimation("enemyrave_run_animation");
        enemyCharacter.setRunAnimation("enemyrave_run_animation");
        enemyCharacter.setAttackAnimation("enemyrave_attack_animation");

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

// Animations not yet developed
    private void createLSDEnemy(GameData gameData, World world, int x, int y, int currentLevel) {
        EnemyEntity enemyCharacter = new EnemyEntity();
        enemyCharacter.setEnemyType(EnemyType.LSD);
        enemyCharacter.setJumpSpeed(0);
        enemyCharacter.setMovementSpeed((float) currentLevel / 10 * 1000);
        enemyCharacter.setHasGravity(true);
        enemyCharacter.setMaxLife(currentLevel * 2);
        enemyCharacter.setLife(enemyCharacter.getMaxLife());
        enemyCharacter.setCurrentAnimation("enemylsd_run_animation");
        enemyCharacter.setRunAnimation("enemylsd_run_animation");
        enemyCharacter.setAttackAnimation("enemylsd_attack_animation");

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

    private void createBeerEnemy(GameData gameData, World world, int x, int y, int currentLevel) {
        EnemyEntity enemyCharacter = new EnemyEntity();
        enemyCharacter.setEnemyType(EnemyType.BEER);
        enemyCharacter.setJumpSpeed(((float) currentLevel / 4) * 400);
        enemyCharacter.setMovementSpeed(((float) currentLevel / 4) * 160);
        enemyCharacter.setHasGravity(true);
        enemyCharacter.setMaxLife(currentLevel);
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
        enemyCharacter.setHitable(true);
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
        if (target.getX() - 50 > enemy.getX()) {
            enemy.setVelocity(enemy.getMovementSpeed());
        } else if (target.getX() + 50 < enemy.getX()) {
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

        if (enemyHit.isHitable()) {
            enemyHit.setLife(enemyHit.getLife() - 1);
            enemyHit.setHit(true);
        }

        //ENEMY DIES
        if (enemyHit.getLife() <= 0) {
            world.removeEntity(enemyHit.getWeaponOwned());
            world.removeEntity(enemyHit);

            //Drop item
            for (IDropService i : Lookup.getDefault().lookupAll(IDropService.class)) {
                i.dropItem(gameData, world, enemyHit.getX(), enemyHit.getY());
            }
        }
    }

    @Override
    public void start(GameData gameData, World world) {
    }
}
