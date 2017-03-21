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
import org.openide.util.Lookup;
import group04.common.GameData;
import group04.common.World;
import group04.common.services.IServiceInitializer;
import group04.common.services.IServiceProcessor;

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
        fps.log();

        for (IServiceProcessor e : Lookup.getDefault().lookupAll(IServiceProcessor.class)) {
            e.process(gameData, world);
        }

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
}
