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

    public void renderExit(GameData gameData) {
        drawExitMenuSprites(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        if (gameData.getKeys().isDown(GameKeys.MOUSE0)
                && gameData.getMouseX() > images.get("yesexit").getX()
                && gameData.getMouseX() < (images.get("yesexit").getX() + images.get("yesexit").getWidth())
                && gameData.getMouseY() > images.get("yesexit").getY()
                && gameData.getMouseY() < (images.get("yesexit").getY() + images.get("yesexit").getHeight())) {
            System.exit(0);
        } else if (gameData.getKeys().isDown(GameKeys.MOUSE0)
                && gameData.getMouseX() > images.get("noexit").getX()
                && gameData.getMouseX() < (images.get("noexit").getX() + images.get("noexit").getWidth())
                && gameData.getMouseY() > images.get("noexit").getY()
                && gameData.getMouseY() < (images.get("noexit").getY() + images.get("noexit").getHeight())) {
            setGameState(0);
        }
    }

    public void renderOptions(GameData gameData) {
        drawOptionMenuSprites(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        if (gameData.getKeys().isDown(GameKeys.MOUSE0)
                && gameData.getMouseX() > images.get("optionsback").getX()
                && gameData.getMouseX() < (images.get("optionsback").getX() + images.get("optionsback").getWidth())
                && gameData.getMouseY() > images.get("optionsback").getY()
                && gameData.getMouseY() < (images.get("optionsback").getY() + images.get("optionsback").getHeight())) {
            setGameState(0);
        }
    }

    public void renderMenu(GameData gameData) {
        /* sr.begin(ShapeType.Filled);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sr.setColor(1, 0, 0, 1);
        sr.rect(100, 100, 300, 50);
        sr.rect(gameData.getDisplayHeight(), gameData.getDisplayWidth(), 300, 50);
        sr.end();*/
        drawMainMenuSprites(gameData.getDisplayWidth(), gameData.getDisplayHeight());

        if (gameData.getKeys().isDown(GameKeys.MOUSE0)
                && gameData.getMouseX() > images.get("start").getX()
                && gameData.getMouseX() < (images.get("start").getX() + images.get("start").getWidth())
                && gameData.getMouseY() > images.get("start").getY()
                && gameData.getMouseY() < (images.get("start").getY() + images.get("start").getHeight())) {
            setGameState(1);
        }
        if (gameData.getKeys().isDown(GameKeys.MOUSE0)
                && gameData.getMouseX() > images.get("quit").getX()
                && gameData.getMouseX() < (images.get("quit").getX() + images.get("quit").getWidth())
                && gameData.getMouseY() > images.get("quit").getY()
                && gameData.getMouseY() < (images.get("quit").getY() + images.get("quit").getHeight())) {
            setGameState(3);
        }
        if (gameData.getKeys().isDown(GameKeys.MOUSE0)
                && gameData.getMouseX() > images.get("options").getX()
                && gameData.getMouseX() < (images.get("options").getX() + images.get("options").getWidth())
                && gameData.getMouseY() > images.get("options").getY()
                && gameData.getMouseY() < (images.get("options").getY() + images.get("options").getHeight())) {
            setGameState(2);
        }

    }

    private void drawExitMenuSprites(int width, int height) {
        batch.begin();
        drawSprite(images.get("menu"), 0, 0);
        drawSprite(images.get("exitquestion"), (int) ((width / 5.8) - (images.get("exitquestion").getWidth() / 2)),
                (int) ((height / 1.6) - (images.get("exitquestion").getHeight() / 2) + 100));
        drawSprite(images.get("yesexit"), (int) (((width / 5.8) - (images.get("yesexit").getWidth() / 2))),
                (int) ((height / 1.945) - (images.get("yesexit").getHeight() / 2) ));
        drawSprite(images.get("noexit"), (int) (((width / 6.4) - (images.get("noexit").getWidth() / 2))),
                (int) ((height / 2.15) - (images.get("noexit").getHeight() / 2)- 100));
        batch.end();
    }

    private void drawOptionMenuSprites(int width, int height) {
        batch.begin();
        drawSprite(images.get("menu"), 0, 0);
        drawSprite(images.get("optionsback"),(int) (((width / 2) - (images.get("optionsback").getWidth() / 2))- 525),
                (int) ((height / 2) - (images.get("optionsback").getHeight() / 2) + 300));
        // TODO draw options
        batch.end();
    }

    private void drawMainMenuSprites(int width, int height) {
        batch.begin();
        drawSprite(images.get("menu"), 0, 0);
        // Start button
        drawSprite(images.get("start"), (int) ((width / 5.8) - (images.get("start").getWidth() / 2)),
                (int) ((height / 1.6) - (images.get("start").getHeight() / 2) + 100));
        // Exit button
        drawSprite(images.get("quit"), (int) ((width / 6.4) - (images.get("quit").getWidth() / 2)),
                (int) ((height / 2.15) - (images.get("quit").getHeight() / 2) - 100));
        //Options button
        drawSprite(images.get("options"), (int) ((width / 5.8) - (images.get("options").getWidth() / 2)),
                (int) ((height / 1.945) - (images.get("options").getHeight() / 2)));

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
        Texture tex = new Texture(Gdx.files.internal("menu.png"));
        images.put("menu", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("start.png"));
        images.put("start", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("options.png"));
        images.put("options", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("quit.png"));
        images.put("quit", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("exitquestion.png"));
        images.put("exitquestion", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("yesexit.png"));
        images.put("yesexit", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("noexit.png"));
        images.put("noexit", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("optionsback.png"));
        images.put("optionsback", new Sprite(tex));

    }
}
