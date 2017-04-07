/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.core;

import group04.core.managers.InputController;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import group04.basecommon.BaseEntity;
import group04.boostcommon.IBoostService;
import group04.cameracommon.ICameraService;
import group04.collisioncommon.ICollisionService;
import group04.common.Entity;
import group04.common.EntityType;
import org.openide.util.Lookup;
import group04.common.GameData;
import group04.common.WeaponType;
import group04.common.World;
import group04.common.events.Event;
import group04.common.events.EventType;
import group04.projectilecommon.IProjectileService;
import group04.common.services.IServiceInitializer;
import group04.common.services.IServiceProcessor;
import group04.currencycommon.ICurrencyService;
import group04.enemycommon.EnemyEntity;
import group04.enemycommon.IEnemyService;
import group04.mapcommon.IMapService;
import group04.movementcommon.IMovementService;
import group04.playercommon.PlayerEntity;
import group04.spawnercommon.ISpawnerService;
import group04.spawnercommon.WaveSpawnerEntity;
import group04.weaponcommon.IWeaponService;
import group04.weaponcommon.WeaponEntity;
import java.util.ArrayList;
import sun.nio.cs.ext.ISCII91;

/**
 *
 * @author Magnus
 */
public class Game implements ApplicationListener {

    private World world;
    private GameData gameData;
    private OrthographicCamera cam;
    Renderer render;
    MenuHandler menu;
    private FPSLogger fps = new FPSLogger();

    public Game() {

    }

    @Override
    public void create() {

        world = new World();
        gameData = new GameData();
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        gameData.setTileSize(16);
        /*        gameData.setMapWidth(gameData.getDisplayWidth() / gameData.getTileSize() * 2);
        gameData.setMapHeight(gameData.getDisplayHeight() / gameData.getTileSize());*/
        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();
        render = new Renderer(gameData);
        menu = new MenuHandler();
        

        for (IServiceInitializer i : Lookup.getDefault().lookupAll(IServiceInitializer.class)) {
            i.start(gameData, world);
        }

        Gdx.input.setInputProcessor(
                new InputController(gameData)
        );

    }

    @Override
    public void render() {
        gameData.setMouseX(Gdx.input.getX());
        gameData.setMouseY(gameData.getDisplayHeight() - Gdx.input.getY());
        if (menu.getGameState() == 1) {
            update();
            render.render(gameData, world);

        } else if (menu.getGameState() == 0) {
            menu.renderMenu(gameData);
        } else if (menu.getGameState() == 2) {
            menu.renderOptions(gameData);
        } else if (menu.getGameState() == 3) {
            menu.renderExit(gameData);
        }
        gameData.getKeys().update();

    }

    private void update() {
        gameData.setDelta(Gdx.graphics.getDeltaTime());

        for (ICameraService e : Lookup.getDefault().lookupAll(ICameraService.class)) {
            for (Entity player : world.getEntities(PlayerEntity.class)) {
                e.followEntity(gameData, world, player);
            }
        }

        for (IServiceProcessor e : Lookup.getDefault().lookupAll(IServiceProcessor.class)) {
            e.process(gameData, world);
        }

        for (IMovementService e : Lookup.getDefault().lookupAll(IMovementService.class)) {
            e.process(gameData, world);
        }

        for (ISpawnerService i : Lookup.getDefault().lookupAll(ISpawnerService.class)) {
            for (Entity e : world.getEntities(WaveSpawnerEntity.class)) {
                i.spawner(gameData, world, (WaveSpawnerEntity) e);
            }
        }

        for (IWeaponService ips : Lookup.getDefault().lookupAll(IWeaponService.class)) {
            ips.pickUpWeapon(gameData, world);
        }

        platformProcess();
        playerProcess();
        enemyProcess();
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    private void playerProcess() {
        for (Entity p : world.getEntities(PlayerEntity.class)) {

            PlayerEntity player = (PlayerEntity) p;

            for (IWeaponService ips : Lookup.getDefault().lookupAll(IWeaponService.class)) {
                ips.playerAttack(gameData, world, p);
            }

            for (IProjectileService ips : Lookup.getDefault().lookupAll(IProjectileService.class)) {
                //ips.process(gameData, world);
                for (Event e : gameData.getAllEvents()) {
                    if (e.getType() == EventType.PLAYER_SHOOT_GUN) {
                        WeaponEntity weapon = (WeaponEntity) player.getWeaponOwned();
                        if (weapon.getWeaponType() == WeaponType.GUN) {
                            weapon.setCurrentFrame(0);
                            ips.playershootgun(gameData, world, p, weapon);
                        }
                        gameData.removeEvent(e);
                    } else if (e.getType() == EventType.PLAYER_SHOOT_ROCKET) {
                        WeaponEntity weapon = (WeaponEntity) player.getWeaponOwned();
                        if (weapon.getWeaponType() == WeaponType.ROCKET) {
                            ips.playershootrocket(gameData, world, p, weapon);

                        }
                        gameData.removeEvent(e);
                    } else if (e.getType() == EventType.PLAYER_SWING) {
                        WeaponEntity weapon = (WeaponEntity) player.getWeaponOwned();
                        if (weapon.getWeaponType() == WeaponType.MELEE) {
                            ips.playershootgun(gameData, world, p, weapon);
                            weapon.setCurrentFrame(0);
                        }
                        gameData.removeEvent(e);
                    }
                }
            }
        }
    }

    private void enemyProcess() {
        for (IEnemyService i : Lookup.getDefault().lookupAll(IEnemyService.class)) {
            Entity player = null;
            Entity base = null;
            ArrayList<EnemyEntity> enemies = new ArrayList<>();
            for (Entity p : world.getEntities(PlayerEntity.class)) {
                player = p;
            }
            for (Entity b : world.getEntities(BaseEntity.class)) {
                base = b;
            }
            for (Entity e : world.getEntities(EnemyEntity.class)) {
                enemies.add((EnemyEntity) e);
            }

            for (Event ev : gameData.getAllEvents()) {
                if (ev.getType() == EventType.ENEMY_SWING) {
                    Entity weapon = world.getEntity(ev.getEntityID());
                    gameData.removeEvent(ev);
                    if(weapon != null)
                        for (ICollisionService serv : Lookup.getDefault().lookupAll(ICollisionService.class)) {
                            if (serv.isEntitiesColliding(world, gameData, player, weapon)) {
                                player.setLife((int) (player.getLife() * 0.5f));
                            }
                        }
                    }
                }

        for (IWeaponService ips : Lookup.getDefault().lookupAll(IWeaponService.class)) {
            for (Entity enemy : world.getEntities(EnemyEntity.class)) {
                ips.enemyAttack(gameData, world, enemy, player, base);
            }
        }

        try {
            i.controller(gameData, world, player, base, enemies);
        } catch (NullPointerException e) {
            System.out.println("Base or player is null");
        }

        for (Event ev : gameData.getAllEvents()) {
            if (ev.getType() == EventType.ENTITY_HIT) {
                Entity enemyHit = world.getEntity(ev.getEntityID());
                if (enemyHit.getClass() == EnemyEntity.class) {
                    i.enemyHit(gameData, world, (EnemyEntity) enemyHit);
                    gameData.removeEvent(ev);
                }
            }
        }

        for (IProjectileService ips : Lookup.getDefault().lookupAll(IProjectileService.class)) {
            for (Entity enemy : world.getEntities(EnemyEntity.class)) {
                for (Event e : gameData.getAllEvents()) {
                    if (e.getType() == EventType.ENEMY_SHOOT && e.getEntityID().equals(enemy.getID())) {
                        ips.enemyshoot(gameData, world, enemy, base, player);
                        gameData.removeEvent(e);
                    }
                }
            }
        }

    }
}

private 

void platformProcess() {
        for (Event e : gameData.getAllEvents()) {
            if (e.getType() == EventType.PLATFORM_SPAWN) {
                for (IMapService i : Lookup.getDefault().lookupAll(IMapService.class

)) {
                    for (Entity ent : world.getEntities(BaseEntity.class
)) {
                        BaseEntity base = (BaseEntity) ent;
                        if (base.getPlatformLevel() == 1) {
                            i.process(gameData, "../../../Common/src/main/resources/mapplat1.object");
                        }
                        if (base.getPlatformLevel() == 2) {
                            i.process(gameData, "../../../Common/src/main/resources/mapplat2.object");

                        }
                        if (base.getPlatformLevel() == 3) {
                            i.process(gameData, "../../../Common/src/main/resources/mapplat3.object");

                        }
                        if (base.getPlatformLevel() == 4) {
                            i.process(gameData, "../../../Common/src/main/resources/mapplat4.object");

                        }
                    }
                }
            }
        }
    }
}
