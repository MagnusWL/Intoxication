/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import group04.common.Entity;
import group04.common.EntityType;
import org.openide.util.Lookup;
import group04.common.GameData;
import group04.common.WeaponType;
import group04.common.World;
import group04.common.events.Event;
import group04.common.events.EventType;
import group04.common.services.IBoostService;
import group04.common.services.ICameraService;
import group04.common.services.IEnemyService;
import group04.common.services.IProjectileService;
import group04.common.services.IServiceInitializer;
import group04.common.services.IServiceProcessor;
import group04.common.services.IWeaponService;
import group04.currencycommon.ICurrencyService;
import group04.playercommon.PlayerEntity;
import group04.weaponcommon.WeaponEntity;
import java.util.ArrayList;

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

        for (IServiceInitializer i : Lookup.getDefault().lookupAll(IServiceInitializer.class)) {
            i.start(gameData, world);
        }

        Gdx.input.setInputProcessor(
                new InputController(gameData)
        );

        render = new Renderer(gameData);
        menu = new MenuHandler();

    }

    @Override
    public void render() {
        gameData.getKeys().update();
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
    }

    private void update() {

        gameData.setDelta(Gdx.graphics.getDeltaTime());

        for (ICameraService e : Lookup.getDefault().lookupAll(ICameraService.class)) {
            for (Entity player : world.getEntities(EntityType.PLAYER)) {
                e.followEntity(gameData, world, player);
            }
        }

        for (IServiceProcessor e : Lookup.getDefault().lookupAll(IServiceProcessor.class)) {
            e.process(gameData, world);
        }

        for (IWeaponService ips : Lookup.getDefault().lookupAll(IWeaponService.class)) {
            ips.pickUpWeapon(gameData, world);
        }
        
        playerProcess();
        enemyProcess();
        currencyProcess();
        boostProcess();
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
        for (Entity p : world.getEntities(EntityType.PLAYER)) {
        PlayerEntity player = (PlayerEntity) p;
        
            for (IWeaponService ips : Lookup.getDefault().lookupAll(IWeaponService.class)) {
                ips.playerAttack(gameData, world, p);
            }

            for (IProjectileService ips : Lookup.getDefault().lookupAll(IProjectileService.class)) {
                //ips.process(gameData, world);
                for (Event e : gameData.getAllEvents()) {
                    if (e.getType() == EventType.PLAYER_SHOOT_GUN) {
                        WeaponEntity weapon = (WeaponEntity)world.getEntity(player.getWeaponOwned());
                        
                        if (weapon.getWeaponType() == WeaponType.GUN) {
                            ips.playershootgun(gameData, world, p, weapon);

                        }
                        gameData.removeEvent(e);
                    } else if (e.getType() == EventType.PLAYER_SHOOT_ROCKET) {
                        WeaponEntity weapon = (WeaponEntity) world.getEntity(player.getWeaponOwned());
                        if (weapon.getWeaponType() == WeaponType.ROCKET) {
                            ips.playershootrocket(gameData, world, p, weapon);

                        }
                        gameData.removeEvent(e);
                    }
                }
            }

        }
    }

    private void boostProcess() {
        for (IBoostService e : Lookup.getDefault().lookupAll(IBoostService.class)) {

            for (Event event : gameData.getEvents()) {
                if (event.getType() == EventType.DROP_BOOST) {
                    e.dropBoost(world, world.getEntity(event.getEntityID()));
                    gameData.removeEvent(event);
                }
            }

            for (Event event : gameData.getEvents()) {
                if (event.getType() == EventType.PICKUP_BOOST) {
                    world.removeEntity(world.getEntity(event.getEntityID()));
                    gameData.removeEvent(event);

                    e.pickUpBoost(gameData, world, world.getEntity(event.getEntityID()), world.getEntity(event.getEntityID()));
                }
            }
        }
    }

    private void currencyProcess() {

        for (ICurrencyService e : Lookup.getDefault().lookupAll(ICurrencyService.class)) {

            for (Event event : gameData.getEvents()) {
                if (event.getType() == EventType.DROP_CURRENCY) {
                    e.dropCurrency(world.getEntity(event.getEntityID()));
                    gameData.removeEvent(event);
                }
            }

            for (Event event : gameData.getEvents()) {

                if (event.getType() == EventType.PICKUP_CURRENCY) {

                    world.removeEntity(world.getEntity(event.getEntityID()));
                    gameData.removeEvent(event);

                    for (Entity player : world.getEntities(EntityType.PLAYER)) {

                        e.pickUpCurrency(player);
                    }
                }
            }
        }
    }

    private void enemyProcess() {
        for (IEnemyService i : Lookup.getDefault().lookupAll(IEnemyService.class)) {
            Entity player = null;
            Entity base = null;
            Entity waveSpawner = null;
            ArrayList<Entity> enemies = new ArrayList<>();
            for (Entity p : world.getEntities(EntityType.PLAYER)) {
                player = p;
            }
            for (Entity b : world.getEntities(EntityType.BASE)) {
                base = b;
            }
            for (Entity w : world.getEntities(EntityType.WAVE_SPAWNER)) {
                waveSpawner = w;
            }
            for (Entity e : world.getEntities(EntityType.ENEMY)) {
                enemies.add(e);
            }

            for (IWeaponService ips : Lookup.getDefault().lookupAll(IWeaponService.class)) {
                for (Entity enemy : world.getEntities(EntityType.ENEMY)) {
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
                    i.enemyHit(gameData, world, enemyHit);
                    gameData.removeEvent(ev);
                }
            }

            for (IProjectileService ips : Lookup.getDefault().lookupAll(IProjectileService.class)) {
                for (Entity enemy : world.getEntities(EntityType.ENEMY)) {
                    for (Event e : gameData.getAllEvents()) {
                        if (e.getType() == EventType.ENEMY_SHOOT && e.getEntityID().equals(enemy.getID())) {
                            ips.enemyshoot(gameData, world, enemy, base, player);
                            gameData.removeEvent(e);
                        }
                    }
                }
            }

            try {
                i.spawner(gameData, world, waveSpawner);
            } catch (NullPointerException e) {
                System.out.println("waveSpawner = null");
            }
        }
    }
}
