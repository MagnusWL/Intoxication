/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.core.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Josan gamle stodder
 */
public class Assets {
    public static final AssetManager manager = new AssetManager();
    private static List<AssetDescriptor<Sound>> soundAssets = new ArrayList<AssetDescriptor<Sound>>();
    private static List<AssetDescriptor<Texture>> textureAssets = new ArrayList<AssetDescriptor<Texture>>();
    
    public static void load()
    {
        for (AssetDescriptor<Sound> soundFile : soundAssets)
        {
            manager.load(soundFile);
        }
        for(AssetDescriptor<Texture> textureFile : textureAssets)
        {
            manager.load(textureFile);
        }
    }
    
    public void addSoundAsset(String soundAssetName)
    {
        soundAssets.add(new AssetDescriptor<Sound>(soundAssetName, Sound.class));
    }
    
    public void addImageAsset(String textureAssetName)
    {
        textureAssets.add(new AssetDescriptor<Texture>(textureAssetName, Texture.class));
    }
    
    public static void dispose()
    {
        manager.dispose();
    }
    
    
}
