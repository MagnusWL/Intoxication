package group04.core.managers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import group04.common.GameData;
import group04.common.GameKeys;

public class InputController extends InputAdapter {

    private final GameData gameData;

    public InputController(GameData gameData) {
        this.gameData = gameData;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            gameData.getKeys().setKey(GameKeys.MOUSE0, true);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            gameData.getKeys().setKey(GameKeys.MOUSE0, false);
        }
        return true;
    }

    @Override
    public boolean keyDown(int k) {
        if (k == Input.Keys.W) {
            gameData.getKeys().setKey(GameKeys.W, true);
        }
        if (k == Input.Keys.A) {
            gameData.getKeys().setKey(GameKeys.A, true);
        }
        if (k == Input.Keys.S) {
            gameData.getKeys().setKey(GameKeys.S, true);
        }
        if (k == Input.Keys.D) {
            gameData.getKeys().setKey(GameKeys.D, true);
        }
        if (k == Input.Keys.U) {
            gameData.getKeys().setKey(GameKeys.U, true);
        }
        if (k == Input.Keys.I) {
            gameData.getKeys().setKey(GameKeys.I, true);
        }
        if (k == Input.Keys.J) {
            gameData.getKeys().setKey(GameKeys.J, true);
        }
        if (k == Input.Keys.K) {
            gameData.getKeys().setKey(GameKeys.K, true);
        }
        if (k == Input.Keys.L) {
            gameData.getKeys().setKey(GameKeys.L, true);
        }
        if (k == Input.Keys.O) {
            gameData.getKeys().setKey(GameKeys.O, true);
        }

        if (k == Input.Keys.SPACE) {
            gameData.getKeys().setKey(GameKeys.SPACE, true);
        }
        if (k == Input.Keys.Q) {
            gameData.getKeys().setKey(GameKeys.Q, true);
        }

        return true;
    }

    @Override
    public boolean keyUp(int k) {
        if (k == Input.Keys.W) {
            gameData.getKeys().setKey(GameKeys.W, false);
        }
        if (k == Input.Keys.A) {
            gameData.getKeys().setKey(GameKeys.A, false);
        }
        if (k == Input.Keys.S) {
            gameData.getKeys().setKey(GameKeys.S, false);
        }
        if (k == Input.Keys.D) {
            gameData.getKeys().setKey(GameKeys.D, false);
        }
        if (k == Input.Keys.U) {
            gameData.getKeys().setKey(GameKeys.U, false);
        }
        if (k == Input.Keys.I) {
            gameData.getKeys().setKey(GameKeys.I, false);
        }
        if (k == Input.Keys.J) {
            gameData.getKeys().setKey(GameKeys.J, false);
        }
        if (k == Input.Keys.K) {
            gameData.getKeys().setKey(GameKeys.K, false);
        }
        if (k == Input.Keys.L) {
            gameData.getKeys().setKey(GameKeys.L, false);
        }
        if (k == Input.Keys.O) {
            gameData.getKeys().setKey(GameKeys.O, false);
        }

        if (k == Input.Keys.SPACE) {
            gameData.getKeys().setKey(GameKeys.SPACE, false);
        }
        if (k == Input.Keys.Q) {
            gameData.getKeys().setKey(GameKeys.Q, false);
        }

        return true;
    }

}
