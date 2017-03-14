/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import group04.common.GameData;
import group04.common.GameKeys;

/**
 *
 * @author burno
 */
public class MenuHandler {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private int gameState = 0;

    public MenuHandler() {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
    }

    public void render(GameData gameData) {
        shapeRenderer.begin(ShapeType.Filled);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.rect(100, 100, 300, 50);
        shapeRenderer.rect(gameData.getDisplayHeight(), gameData.getDisplayWidth(), 300, 50);
        shapeRenderer.end();
        if (gameData.getKeys().isDown(GameKeys.MOUSE0)) {
            setGameState(1);
        }
    }

    public int getGameState() {
        return gameState;
    }

    public void setGameState(int gameState) {
        this.gameState = gameState;
    }
}
