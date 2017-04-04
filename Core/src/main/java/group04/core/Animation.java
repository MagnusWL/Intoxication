package group04.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.ArrayList;

public class Animation {
    private ArrayList<Sprite> sprites;
    private ArrayList<Sprite> redSprites;
    private int width, height;
    private float animationSpeed;
    
    public Animation(ArrayList<Sprite> sprites, int width, int height, float animationSpeed)
    {
        this.animationSpeed = animationSpeed;
        this.sprites = sprites;
        this.width = width; 
        this.height = height; 
        redSprites = new ArrayList<>();
        
        for(Sprite s : sprites) {
            Sprite spr = new Sprite(s.getTexture());
            spr.setColor(new Color(1, 0, 0, 0.95f));
            redSprites.add(spr);
        }
    }

    public ArrayList<Sprite> getRedSprites() {
        return redSprites;
    }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

    public void setSprites(ArrayList<Sprite> sprites) {
        this.sprites = sprites;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(float animationSpeed) {
        this.animationSpeed = animationSpeed;
    }
    
    
}
