package group04.weapon;

import group04.weaponcommon.WeaponType;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import group04.common.Entity;
import group04.common.GameData;
import group04.common.GameKeys;
import group04.common.World;
import group04.common.events.Event;
import group04.common.events.EventType;
import group04.common.services.IServiceInitializer;
import group04.enemycommon.EnemyEntity;
import group04.playercommon.PlayerEntity;
import group04.weaponcommon.IWeaponService;
import group04.weaponcommon.WeaponEntity;

@ServiceProviders(value = {
    @ServiceProvider(service = IWeaponService.class),
    @ServiceProvider(service = IServiceInitializer.class)})

public class WeaponSystem implements IWeaponService, IServiceInitializer {

    public void createWeapon(GameData gameData, World world, Entity e, WeaponType type) {
        //Find out which weapon it is
        if (type == WeaponType.GUN) {
            createGun(gameData, world, e, type);
        }
        if (type == WeaponType.MELEE) {
            createMelee(gameData, world, e, type);
        }
        if (type == WeaponType.ROCKET) {
            createRocket(gameData, world, e, type);
        }
    }

    public void createGun(GameData gameData, World world, Entity e, WeaponType type) {
        WeaponEntity weapon = new WeaponEntity();
        weapon.setAnimateable(true);
        weapon.setCurrentAnimation("player_weapon_ranged_throwBottle_attack_animation");
        weapon.setIdleAnimation("player_weapon_ranged_throwbottle_run_animation");
        weapon.setAttackAnimation("player_weapon_ranged_throwbottle_attack_animation");
        weapon.setAttackAudio("throw_attack");
        weapon.setCurrentFrame(10);
        weapon.setAttackCooldown(5);
        weapon.setTimeSinceAttack(0);
        weapon.setWeaponCarrier(e.getID());
        weapon.setyCenter(20);
        weapon.setWeaponType(type);
        weapon.setX(e.getX());
        weapon.setY(e.getY());
        world.addEntity(weapon);
        if (e.getClass() == PlayerEntity.class) {
            PlayerEntity player = (PlayerEntity) e;
            player.setWeaponOwned(weapon);
        }
        if (e.getClass() == EnemyEntity.class) {
            EnemyEntity enemy = (EnemyEntity) e;
            enemy.setWeaponOwned(weapon);
        }
    }

    private void createMelee(GameData gameData, World world, Entity e, WeaponType type) {
        WeaponEntity weapon = new WeaponEntity();
        weapon.setAnimateable(true);
        weapon.setCurrentAnimation("player_weapon_melee_champaign_attack_animation");
        weapon.setIdleAnimation("player_weapon_melee_champaign_run_animation");
        weapon.setAttackAnimation("player_weapon_melee_champaign_attack_animation");
        weapon.setAttackAudio("champagne_attack");
        weapon.setAttackCooldown(3);
        weapon.setTimeSinceAttack(0);
        weapon.setCurrentFrame(6);
        weapon.setxCenter(20);
        weapon.setyCenter(81);
        weapon.setDamage(15);
        weapon.setWeaponCarrier(e.getID());
        weapon.setWeaponType(type);
        weapon.setShapeX(new float[]{-30, -30, 30, 30});
        weapon.setShapeY(new float[]{30, -30, -30, 30});
        world.addEntity(weapon);
        if (e.getClass() == PlayerEntity.class) {
            PlayerEntity player = (PlayerEntity) e;
            player.setWeaponOwned(weapon);
        }
        if (e.getClass() == EnemyEntity.class) {
            EnemyEntity enemy = (EnemyEntity) e;
            enemy.setWeaponOwned(weapon);
        }
    }

    private void createRocket(GameData gameData, World world, Entity e, WeaponType type) {
        WeaponEntity weapon = new WeaponEntity();
        weapon.setAnimateable(true);
        weapon.setCurrentAnimation("player_weapon_ranged_throwbottle_attack_animation");
        weapon.setAttackAnimation("player_weapon_ranged_throwbottle_attack_animation");
        weapon.setAttackCooldown(8);
        weapon.setTimeSinceAttack(0);
        weapon.setDamage(2);
        weapon.setWeaponCarrier(e.getID());
        weapon.setWeaponType(type);
        world.addEntity(weapon);
        if (e.getClass() == PlayerEntity.class) {
            PlayerEntity player = (PlayerEntity) e;
            player.setWeaponOwned(weapon);
        }
        if (e.getClass() == EnemyEntity.class) {
            EnemyEntity enemy = (EnemyEntity) e;
            enemy.setWeaponOwned(weapon);
        }
    }

    @Override
    public void start(GameData gameData, World world) {

    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity weapon : world.getEntities(WeaponEntity.class)) {
            world.removeEntity(weapon);
        }
    }

    @Override
    public void playerAttack(GameData gameData, World world, Entity entity) {
        PlayerEntity player = (PlayerEntity) entity;
        WeaponEntity weapon = (WeaponEntity) player.getWeaponOwned();

        Entity carrier = world.getEntity(weapon.getWeaponCarrier());

        if (gameData.getMouseX() < player.getX() - gameData.getCameraX()) {
            //FLIP
            weapon.setX(carrier.getX() - 75);
            weapon.setY(carrier.getY() + 30);
        } else {
            weapon.setX(carrier.getX() + 75);
            weapon.setY(carrier.getY() + 30);

        }

        weapon.setTimeSinceAttack(weapon.getTimeSinceAttack() + 10 * gameData.getDelta());

        if (weapon.getWeaponType() == WeaponType.GUN && carrier.getClass() == player.getClass() && gameData.getKeys().isDown(GameKeys.MOUSE0) && weapon.getTimeSinceAttack() > weapon.getAttackCooldown()) {
            gameData.addEvent(new Event(EventType.PLAYER_SHOOT_GUN, player.getID()));
            weapon.setTimeSinceAttack(0);
        } else if (weapon.getWeaponType() == WeaponType.MELEE && carrier.getClass() == player.getClass() && gameData.getKeys().isDown(GameKeys.MOUSE0) && weapon.getTimeSinceAttack() > weapon.getAttackCooldown()) {
            gameData.addEvent(new Event(EventType.PLAYER_SWING, player.getID()));
            weapon.setTimeSinceAttack(0);
        } else if (weapon.getWeaponType() == WeaponType.ROCKET && carrier.getClass() == player.getClass() && gameData.getKeys().isDown(GameKeys.MOUSE0) && weapon.getTimeSinceAttack() > weapon.getAttackCooldown()) {
            gameData.addEvent(new Event(EventType.PLAYER_SHOOT_ROCKET, player.getID()));
            weapon.setTimeSinceAttack(0);
        }
    }

    @Override
    public void enemyAttack(GameData gameData, World world, Entity enemy, Entity playerEntity, Entity base) {
        EnemyEntity enemyEntity = (EnemyEntity) enemy;
        WeaponEntity weapon = (WeaponEntity) enemyEntity.getWeaponOwned();

        if (enemyEntity.getVelocity() < 0) {
            weapon.setX(enemyEntity.getX() - 20);
            weapon.setY(enemyEntity.getY() + 30);
        } else {
            weapon.setX(enemyEntity.getX() + 60);
            weapon.setY(enemyEntity.getY() + 30);
        }

        weapon.setTimeSinceAttack(weapon.getTimeSinceAttack() + 10 * gameData.getDelta());

        if (weapon.getWeaponType().toString().equals("GUN") && enemyEntity.getClass() == enemy.getClass() && weapon.getTimeSinceAttack() > weapon.getAttackCooldown()) {
            gameData.addEvent(new Event(EventType.ENEMY_SHOOT, weapon.getWeaponCarrier()));
            weapon.setTimeSinceAttack(0);
        }

        if (!enemyEntity.getCurrentAnimation().equals(enemyEntity.getAttackAnimation()) && weapon.getWeaponType() == WeaponType.MELEE
                && enemyEntity.getClass() == enemy.getClass() && weapon.getTimeSinceAttack() > weapon.getAttackCooldown()
                && Math.abs(enemyEntity.getX() - playerEntity.getX()) < 100) {
            enemyEntity.setCurrentAnimation(enemyEntity.getAttackAnimation());
            gameData.addEvent(new Event(EventType.ENEMY_SWING, weapon.getWeaponCarrier()));
            weapon.setTimeSinceAttack(0);
        }
    }

    @Override
    public void pickUpWeapon(GameData gameData, World world) {
        for (Event e : gameData.getAllEvents()) {
            if (e.getType() == EventType.PICKUP_WEAPON) {
                if (world.getEntity(e.getEntityID()).getClass() == EnemyEntity.class) {
                    createWeapon(gameData, world, world.getEntity(e.getEntityID()), WeaponType.MELEE);
                } else {
                    createWeapon(gameData, world, world.getEntity(e.getEntityID()), WeaponType.MELEE);
                }

                gameData.removeEvent(e);
            }

            if (e.getType() == EventType.PICKUP_GUN) {
                if (world.getEntity(e.getEntityID()).getClass() == EnemyEntity.class) {
                    createWeapon(gameData, world, world.getEntity(e.getEntityID()), WeaponType.GUN);
                } else {
                    createWeapon(gameData, world, world.getEntity(e.getEntityID()), WeaponType.GUN);
                }

                gameData.removeEvent(e);
            }
        }
    }

    @Override
    public void switchWeapon(GameData gameData, World world, Entity player) {
        PlayerEntity playerEntity = (PlayerEntity) player;

        if (gameData.getKeys().isPressed(GameKeys.Q)) {
            if (((WeaponEntity) playerEntity.getWeaponOwned()).getWeaponType() == WeaponType.GUN) {
                world.removeEntity(playerEntity.getWeaponOwned());
                createWeapon(gameData, world, playerEntity, WeaponType.MELEE);
            } else if (((WeaponEntity) playerEntity.getWeaponOwned()).getWeaponType() == WeaponType.MELEE) {
                world.removeEntity(playerEntity.getWeaponOwned());
                createWeapon(gameData, world, playerEntity, WeaponType.GUN);
            }
        }
    }
}
