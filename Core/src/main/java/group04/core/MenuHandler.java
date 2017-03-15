/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import group04.common.GameData;
import group04.common.GameKeys;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author burno
 */
public class MenuHandler {

    private SpriteBatch batch;
    private ShapeRenderer sr;
    private int gameState = 0;
    private Map<String, Sprite> images = new HashMap<>();

    public MenuHandler() {
        sr = new ShapeRenderer();
        batch = new SpriteBatch();
        addEnvironment();
    }

    public void renderOptions(GameData gameData) {
        
    }
    
    public void renderMenu(GameData gameData) {
        /* sr.begin(ShapeType.Filled);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sr.setColor(1, 0, 0, 1);
        sr.rect(100, 100, 300, 50);
        sr.rect(gameData.getDisplayHeight(), gameData.getDisplayWidth(), 300, 50);
        sr.end();*/
        drawSprites(gameData.getDisplayWidth(), gameData.getDisplayHeight());

        if (gameData.getKeys().isDown(GameKeys.MOUSE0) && 
                gameData.getMouseX() > images.get("start").getX()
                && gameData.getMouseX() < (images.get("start").getX() + images.get("start").getWidth())
                && gameData.getMouseY() > images.get("start").getY()
                && gameData.getMouseY() < (images.get("start").getY() + images.get("start").getHeight())) {
            setGameState(1);
        }
                if (gameData.getKeys().isDown(GameKeys.MOUSE0) && 
                gameData.getMouseX() > images.get("exit").getX()
                && gameData.getMouseX() < (images.get("exit").getX() + images.get("exit").getWidth())
                && gameData.getMouseY() > images.get("exit").getY()
                && gameData.getMouseY() < (images.get("exit").getY() + images.get("exit").getHeight())) {
                    System.exit(0);
        }
        
    }

    private void drawSprites(int width, int height) {
        batch.begin();
        drawSprite(images.get("menu"), 0, 0);
        // Start button
        drawSprite(images.get("start"), (int) ((width / 2) - (images.get("start").getWidth() / 2)),
                (int) ((height / 2) - (images.get("start").getHeight() / 2) + 100));
        // Exit button
        drawSprite(images.get("exit"), (int) ((width / 2) - (images.get("exit").getWidth() / 2)),
                (int) ((height / 2) - (images.get("exit").getHeight() / 2) - 100));
        //Options button
        drawSprite(images.get("options"), (int) ((width / 2) - (images.get("options").getWidth() / 2)),
                (int) ((height / 2) - (images.get("options").getHeight() / 2)));

        batch.end();
    }

    private void drawSprite(Sprite sprite, int x, int y) {
        sprite.setX(x);
        sprite.setY(y);
        sprite.draw(batch);
    }

    public int getGameState() {
        return gameState;
    }

    public void setGameState(int gameState) {
        this.gameState = gameState;
    }

    public void addEnvironment() {
        //Images: 
        Texture tex = new Texture(Gdx.files.internal("menubackground.png"));
        images.put("menu", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("start.png"));
        images.put("start", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("exit.png"));
        images.put("exit", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("options.png"));
        images.put("options", new Sprite(tex));
    }
}
