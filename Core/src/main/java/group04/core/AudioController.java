/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.core;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import group04.common.GameData;
import group04.core.managers.Assets;

/**
 *
 * @author Josan gamle stodder
 */
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
