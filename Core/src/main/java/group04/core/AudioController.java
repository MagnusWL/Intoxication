package group04.core;

import com.badlogic.gdx.audio.Sound;
import group04.common.GameData;
import group04.core.managers.Assets;

public class AudioController {

    private Assets assets;

    public AudioController(GameData gameData, Assets assets) {
        this.assets = assets;

        Sound backgroundmusic = assets.getAssetManager().get(assets.getFilePaths().get("backgroundmusic.wav"), Sound.class
        );
        backgroundmusic.loop();
        backgroundmusic.play();

    }

    public void PlayAudio(String audio, float volume) {
        Sound soundfile = assets.getAssetManager().get(assets.getFilePaths().get(audio), Sound.class);
        soundfile.play(volume);
    }

    void update() {

    }

}
